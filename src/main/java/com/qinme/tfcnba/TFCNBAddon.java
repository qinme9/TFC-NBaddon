package com.qinme.tfcnba;

import org.slf4j.Logger;

import com.qinme.tfcnba.item.SuperNutritionFoodItem;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(TFCNBAddon.MODID)
public class TFCNBAddon {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "tfcnbaddon";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // Create Deferred Registers for various registries
    // Uncomment and use as needed for your addon content

    // Blocks
    // public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    // Items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Creative Mode Tabs
    // public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
    //     DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Block Entities
    // public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
    //     DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    // Menu Types
    // public static final DeferredRegister<MenuType<?>> MENU_TYPES =
    //     DeferredRegister.create(Registries.MENU, MODID);

    // Recipe Types
    // public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
    //     DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    // Recipe Serializers
    // public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
    //     DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);

    // 超级营养食物 - 同时补充五种营养和口渴值
    // 食物属性通过 data/tfcnbaddon/tfc/food/super_nutrition_food.json 定义
    // 数值可通过配置文件调整（需配合数据加载事件实现）
    public static final DeferredItem<SuperNutritionFoodItem> SUPER_NUTRITION_FOOD = ITEMS.register(
            "super_nutrition_food",
            () -> new SuperNutritionFoodItem(new Item.Properties()
                    .stacksTo(16)
                    .food(new FoodProperties.Builder()
                            .nutrition(8)
                            .saturationModifier(0.6f)
                            .alwaysEdible()
                            .build()))
    );

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public TFCNBAddon(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register Deferred Registers to the mod event bus
        // Uncomment as you add content
        // BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        // CREATIVE_MODE_TABS.register(modEventBus);
        // BLOCK_ENTITIES.register(modEventBus);
        // MENU_TYPES.register(modEventBus);
        // RECIPE_TYPES.register(modEventBus);
        // RECIPE_SERIALIZERS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("[TFC:NBAddon] TFC附属模组加载中...");

        // Register TFC addon content here
        // Examples: heating recipes, alloy recipes, knapping types, etc.
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Add items to creative tabs
        // 添加到食物标签页
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(SUPER_NUTRITION_FOOD);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("[TFC:NBAddon] 服务器启动中...");
    }
}