package com.qinme.tfcnba;

import org.slf4j.Logger;

import com.qinme.tfcnba.block.FridgeBlock;
import com.qinme.tfcnba.block.entity.FridgeBlockEntity;
import com.qinme.tfcnba.item.SuperNutritionFoodItem;
import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(TFCNBAddon.MODID)
public class TFCNBAddon {
    public static final String MODID = "tfcnbaddon";
    public static final Logger LOGGER = LogUtils.getLogger();

    // Blocks
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    // Items
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Block Entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    // --- 冰箱 ---
    public static final DeferredBlock<FridgeBlock> FRIDGE_BLOCK = BLOCKS.register("fridge",
        () -> new FridgeBlock(BlockBehaviour.Properties.of()
            .strength(1.0f)
            .sound(SoundType.METAL)
        ));

    public static final DeferredItem<BlockItem> FRIDGE_ITEM = ITEMS.register("fridge",
        () -> new BlockItem(FRIDGE_BLOCK.get(), new Item.Properties().stacksTo(64)));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FridgeBlockEntity>> FRIDGE_BLOCK_ENTITY =
        BLOCK_ENTITIES.register("fridge",
            () -> BlockEntityType.Builder.of(FridgeBlockEntity::new, FRIDGE_BLOCK.get()).build(null));

    // --- 超级营养食物 ---
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

    public TFCNBAddon(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("[TFC:NBAddon] TFC附属模组加载中...");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(SUPER_NUTRITION_FOOD);
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(FRIDGE_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("[TFC:NBAddon] 服务器启动中...");
    }
}
