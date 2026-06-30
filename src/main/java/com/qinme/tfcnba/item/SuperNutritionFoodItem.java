package com.qinme.tfcnba.item;

import com.qinme.tfcnba.Config;
import com.qinme.tfcnba.TFCNBAddon;

import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.player.IPlayerInfo;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * 超级营养食物
 * 同时补充五种营养、口渴值和原版饥饿值，数值可通过配置文件调整
 *
 * 在 TFC 中，PlayerMixin 完全替换了 Player.eat()，使得非 IFood 物品无法通过
 * 原版路径获得食物效果。因此我们在此手动调用 TFC API 和原版饥饿系统。
 *
 * 设计原则：无论口渴值或五种营养值是否已满，该食物始终可使用。
 */
public class SuperNutritionFoodItem extends Item {

    public SuperNutritionFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32; // 32 ticks = 1.6 秒
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // 无条件开始食用——alwaysEdible + isEdible()=true 保证不会被 vanilla/TFC 拦截
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide) {
                // 直接调用底层 API 应用所有食物效果
                applyFoodEffects(player);
            }

            // 播放进食完成音效
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, 1.0F);

            // 消耗物品（创造模式不消耗）
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return stack;
    }

    /**
     * 直接调用底层 TFC API 和原版 API 应用食物效果。
     *
     * 不使用 playerInfo.eat(FoodData) 的原因：
     * eat(FoodData) 内部调用 addThirst() 等方法，可能在满值时
     * 有隐性跳空逻辑。直接操作底层可完全绕过这些限制。
     */
    private void applyFoodEffects(Player player) {
        try {
            IPlayerInfo playerInfo = IPlayerInfo.get(player);
            if (playerInfo == null) {
                if (Config.ENABLE_LOGGING.get()) {
                    TFCNBAddon.LOGGER.warn("[超级营养食物] 无法获取 TFC 玩家数据，请确保已安装 TerraFirmaCraft");
                }
                return;
            }

            float hunger = Config.SUPER_FOOD_HUNGER.get();
            float thirst = Config.SUPER_FOOD_WATER.get().floatValue();
            float saturation = Config.SUPER_FOOD_SATURATION.get().floatValue();

            float[] nutrients = new float[]{
                Config.SUPER_FOOD_GRAIN.get().floatValue(),
                Config.SUPER_FOOD_FRUIT.get().floatValue(),
                Config.SUPER_FOOD_VEGETABLES.get().floatValue(),
                Config.SUPER_FOOD_PROTEIN.get().floatValue(),
                Config.SUPER_FOOD_DAIRY.get().floatValue()
            };

            // 1. 口渴值：直接调用 addThirst，即使已满也执行（TFC 内部会自动 cap 在 100）
            playerInfo.addThirst(thirst);

            // 2. 原版饥饿值/饱和度：直接操作原版 FoodData（绕过 TFC Player.eat() 替换）
            if (hunger > 0) {
                player.getFoodData().eat((int) hunger, saturation);
            }

            // 3. TFC 五种营养：通过 INutritionData.addNutrients() 直接添加
            //    此方法在服务端执行，内部有 cap 逻辑（每项 capped 在 1.0）
            //    但不会因为已满而拒绝执行
            FoodData foodData = new FoodData(
                (int) hunger,
                thirst,
                saturation,
                0,  // intoxication
                nutrients,
                Config.SUPER_FOOD_DECAY.get().floatValue()
            );
            playerInfo.nutrition().addNutrients(foodData, player.getFoodData().getFoodLevel());

            // 4. 同步 TFC 对原版饥饿值的跟踪
            playerInfo.nutrition().setHungerAndUpdate(player.getFoodData().getFoodLevel());

            if (Config.ENABLE_LOGGING.get()) {
                TFCNBAddon.LOGGER.info("[超级营养食物] 玩家食用了超级营养食物");
                TFCNBAddon.LOGGER.info("  原版饥饿: +{}", (int) hunger);
                TFCNBAddon.LOGGER.info("  口渴值: +{:.1f} (当前: {:.0f}/100)", thirst, playerInfo.getThirst());
                TFCNBAddon.LOGGER.info("  谷物: +{:.1f}", nutrients[0]);
                TFCNBAddon.LOGGER.info("  水果: +{:.1f}", nutrients[1]);
                TFCNBAddon.LOGGER.info("  蔬菜: +{:.1f}", nutrients[2]);
                TFCNBAddon.LOGGER.info("  蛋白质: +{:.1f}", nutrients[3]);
                TFCNBAddon.LOGGER.info("  乳制品: +{:.1f}", nutrients[4]);
                TFCNBAddon.LOGGER.info("  平均营养: {:.1f}%", playerInfo.nutrition().getAverageNutrition());
            }

        } catch (NoClassDefFoundError | Exception e) {
            TFCNBAddon.LOGGER.error("[超级营养食物] 应用食物效果失败: {}", e.getMessage());
            if (Config.ENABLE_LOGGING.get()) {
                TFCNBAddon.LOGGER.debug("[超级营养食物] 异常详情", e);
            }
        }
    }
}
