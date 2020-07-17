package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class BMForgeConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> mineshaftSpawnRate;
    public static final ForgeConfigSpec.ConfigValue<Integer> minY;
    public static final ForgeConfigSpec.ConfigValue<Integer> maxY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> mesaEnabled;
    public static final ForgeConfigSpec.ConfigValue<Boolean> desertEnabled;
    public static final ForgeConfigSpec.ConfigValue<Boolean> redDesertEnabled;
    public static final ForgeConfigSpec.ConfigValue<Boolean> iceEnabled;
    public static final ForgeConfigSpec.ConfigValue<Boolean> snowEnabled;
    public static final ForgeConfigSpec.ConfigValue<Boolean> jungleEnabled;
    public static final ForgeConfigSpec.ConfigValue<Boolean> savannaEnabled;
    public static final ForgeConfigSpec.ConfigValue<Boolean> mushroomEnabled;

    public static final Vines vines;

    static {
        BUILDER.push("YUNG's Better Mineshafts");

        mineshaftSpawnRate = BUILDER
            .worldRestart()
            .define("Mineshaft Spawn Rate", .003);

        minY = BUILDER
            .worldRestart()
            .comment(
                " The lowest the floor of the shaft can be.\n" +
                " Default: 13")
            .define("Minimum y-coordinate", 13);

        maxY = BUILDER
            .worldRestart()
            .comment(
                " The highest the floor of the shaft can be.\n" +
                " Be careful, setting this too high can make mineshafts poke through ocean floors.\n" +
                " Default: 37")
            .define("Maximum y-coordinate", 37);

        mesaEnabled = BUILDER
            .worldRestart()
            .define("Enable Mesa Variant", true);

        desertEnabled = BUILDER
            .worldRestart()
            .define("Enable Desert Variant", true);

        redDesertEnabled = BUILDER
            .worldRestart()
            .define("Enable Red Desert Variant", true);

        iceEnabled = BUILDER
            .worldRestart()
            .define("Enable Ice Variant", true);

        snowEnabled = BUILDER
            .worldRestart()
            .define("Enable Snow Variant", true);

        jungleEnabled = BUILDER
            .worldRestart()
            .define("Enable Jungle Variant", true);

        savannaEnabled = BUILDER
            .worldRestart()
            .define("Enable Savanna Variant", true);

        mushroomEnabled = BUILDER
            .worldRestart()
            .define("Enable Mushroom Variant", true);

        vines = new Vines(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static class Vines {
        public final ForgeConfigSpec.ConfigValue<Double> vineFreq;
        public final ForgeConfigSpec.ConfigValue<Double> vineFreqJungle;

        public Vines(final ForgeConfigSpec.Builder BUILDER) {
            BUILDER
                .comment(
                    "##########################################################################################################\n" +
                    "# Vine generation settings.\n" +
                    "##########################################################################################################")
                .push("Vines");

            vineFreq = BUILDER
                .comment(" How often vines spawn in all variants except jungle.")
                .worldRestart()
                .defineInRange("Vine Frequency", .25, 0, 1);

            vineFreqJungle = BUILDER
                .comment(" How often vines spawn in jungle mineshafts.")
                .worldRestart()
                .defineInRange("Jungle Mineshaft Vine Frequency", .6, 0, 1);

            BUILDER.pop();
        }
    }
}
