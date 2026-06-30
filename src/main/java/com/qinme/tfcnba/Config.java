package com.qinme.tfcnba;

import net.neoforged.neoforge.common.ModConfigSpec;

// TFC Addon Configuration
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // General settings
    public static final ModConfigSpec.BooleanValue ENABLE_LOGGING = BUILDER
            .comment("是否启用详细日志输出")
            .define("enableLogging", true);

    // 超级营养食物配置
    public static final ModConfigSpec.DoubleValue SUPER_FOOD_GRAIN = BUILDER
            .comment("超级营养食物提供的谷物营养值")
            .defineInRange("superFood.grain", 2.0, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue SUPER_FOOD_FRUIT = BUILDER
            .comment("超级营养食物提供的水果营养值")
            .defineInRange("superFood.fruit", 2.0, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue SUPER_FOOD_VEGETABLES = BUILDER
            .comment("超级营养食物提供的蔬菜营养值")
            .defineInRange("superFood.vegetables", 2.0, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue SUPER_FOOD_PROTEIN = BUILDER
            .comment("超级营养食物提供的蛋白质营养值")
            .defineInRange("superFood.protein", 2.0, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue SUPER_FOOD_DAIRY = BUILDER
            .comment("超级营养食物提供的乳制品营养值")
            .defineInRange("superFood.dairy", 2.0, 0.0, 10.0);

    public static final ModConfigSpec.IntValue SUPER_FOOD_WATER = BUILDER
            .comment("超级营养食物提供的口渴值（满值100）")
            .defineInRange("superFood.water", 30, 0, 100);

    public static final ModConfigSpec.IntValue SUPER_FOOD_HUNGER = BUILDER
            .comment("超级营养食物提供的饥饿值（满值20）")
            .defineInRange("superFood.hunger", 8, 0, 20);

    public static final ModConfigSpec.DoubleValue SUPER_FOOD_SATURATION = BUILDER
            .comment("超级营养食物提供的饱和度")
            .defineInRange("superFood.saturation", 0.6, 0.0, 10.0);

    public static final ModConfigSpec.DoubleValue SUPER_FOOD_DECAY = BUILDER
            .comment("超级营养食物的腐烂速度（0为不腐烂）")
            .defineInRange("superFood.decay", 2.0, 0.0, 100.0);

    static final ModConfigSpec SPEC = BUILDER.build();
}
