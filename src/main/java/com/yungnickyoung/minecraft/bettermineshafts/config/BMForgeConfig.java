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
    public static final Ores ores;

    static {
        BUILDER.push("YUNG's Better Mineshafts");

        mineshaftSpawnRate = BUILDER
            .worldRestart()
            .comment(" Default: .003")
            .define("Mineshaft Spawn Rate", .003);

        minY = BUILDER
            .worldRestart()
            .comment(
                " The lowest the floor of a mineshaft can be.\n" +
                " Default: 13")
            .define("Minimum y-coordinate", 13);

        maxY = BUILDER
            .worldRestart()
            .comment(
                " The highest the floor of a mineshaft can be.\n" +
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
        ores = new Ores(BUILDER);

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

    public static class Ores {
        public final ForgeConfigSpec.ConfigValue<Boolean> enabled;
        public final ForgeConfigSpec.ConfigValue<Integer> cobble;
        public final ForgeConfigSpec.ConfigValue<Integer> coal;
        public final ForgeConfigSpec.ConfigValue<Integer> iron;
        public final ForgeConfigSpec.ConfigValue<Integer> redstone;
        public final ForgeConfigSpec.ConfigValue<Integer> gold;
        public final ForgeConfigSpec.ConfigValue<Integer> lapis;
        public final ForgeConfigSpec.ConfigValue<Integer> emerald;
        public final ForgeConfigSpec.ConfigValue<Integer> diamond;

        public Ores(final ForgeConfigSpec.Builder BUILDER) {
            BUILDER
                .comment(
                    "##########################################################################################################\n" +
                    "# Ore deposit settings.\n" +
                    "##########################################################################################################")
                .push("Ore Deposits");

            enabled = BUILDER
                .worldRestart()
                .define("Enable Ore Deposits", true);

            cobble = BUILDER
                .comment(
                    " Chance of an ore deposit being cobblestone only.\n" +
                    " Default: 50")
                .worldRestart()
                .defineInRange("Cobble Spawn Chance (Empty Deposit)", 50, 0, 100);

            coal = BUILDER
                .comment(
                    " Chance of an ore deposit containing coal.\n" +
                    " Default: 20")
                .worldRestart()
                .defineInRange("Coal Spawn Chance", 20, 0, 100);

            iron = BUILDER
                .comment(
                    " Chance of an ore deposit containing iron.\n" +
                    " Default: 9")
                .worldRestart()
                .defineInRange("Iron Spawn Chance", 9, 0, 100);

            redstone = BUILDER
                .comment(
                    " Chance of an ore deposit containing redstone.\n" +
                    " Default: 7")
                .worldRestart()
                .defineInRange("Redstone Spawn Chance", 7, 0, 100);

            gold = BUILDER
                .comment(
                    " Chance of an ore deposit containing gold.\n" +
                    " Default: 7")
                .worldRestart()
                .defineInRange("Gold Spawn Chance", 7, 0, 100);

            lapis = BUILDER
                .comment(
                    " Chance of an ore deposit containing lapis lazuli.\n" +
                    " Default: 3")
                .worldRestart()
                .defineInRange("Lapis Spawn Chance", 3, 0, 100);

            emerald = BUILDER
                .comment(
                    " Chance of an ore deposit containing emerald.\n" +
                    " Default: 3")
                .worldRestart()
                .defineInRange("Emerald Spawn Chance", 3, 0, 100);

            diamond = BUILDER
                .comment(
                    " Chance of an ore deposit containing diamond.\n" +
                    " Default: 1")
                .worldRestart()
                .defineInRange("Diamond Spawn Chance", 1, 0, 100);

            BUILDER.pop();
        }
    }
}
