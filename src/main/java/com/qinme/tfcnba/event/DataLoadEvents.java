package com.qinme.tfcnba.event;

import com.qinme.tfcnba.Config;
import com.qinme.tfcnba.TFCNBAddon;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

/**
 * 数据加载事件处理器
 * 用于在数据加载时应用配置的食物数值
 */
@EventBusSubscriber(modid = TFCNBAddon.MODID)
public class DataLoadEvents {
    
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        if (Config.ENABLE_LOGGING.get()) {
            TFCNBAddon.LOGGER.info("[超级营养食物] 数据加载中，准备应用配置值...");
        }
        
        if (Config.ENABLE_LOGGING.get()) {
            TFCNBAddon.LOGGER.info("[超级营养食物] 当前配置值：");
            TFCNBAddon.LOGGER.info("  谷物: {}", Config.SUPER_FOOD_GRAIN.get());
            TFCNBAddon.LOGGER.info("  水果: {}", Config.SUPER_FOOD_FRUIT.get());
            TFCNBAddon.LOGGER.info("  蔬菜: {}", Config.SUPER_FOOD_VEGETABLES.get());
            TFCNBAddon.LOGGER.info("  蛋白质: {}", Config.SUPER_FOOD_PROTEIN.get());
            TFCNBAddon.LOGGER.info("  乳制品: {}", Config.SUPER_FOOD_DAIRY.get());
            TFCNBAddon.LOGGER.info("  口渴值: {}", Config.SUPER_FOOD_WATER.get());
            TFCNBAddon.LOGGER.info("  饥饿值: {}", Config.SUPER_FOOD_HUNGER.get());
        }
    }
}
