package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class BMConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> mineshaftSpawnRate;
    public static final ForgeConfigSpec.ConfigValue<Integer> minY;
    public static final ForgeConfigSpec.ConfigValue<Integer> maxY;
    public static final ForgeConfigSpec.ConfigValue<String> whitelistedDimensions;
    public static final ForgeConfigSpec.ConfigValue<String> blacklistedBiomes;
    public static final ConfigOres ores;
    public static final ConfigSpawnRates spawnRates;

    static {
        BUILDER.push("YUNG's Better Mineshafts");

        mineshaftSpawnRate = BUILDER
                .worldRestart()
                .comment(" Default: .003")
                .define("Mineshaft Spawn Rate", .003);

        minY = BUILDER
                .worldRestart()
                .comment(
                        " The lowest a mineshaft can spawn.\n" +
                        " Default: -55")
                .define("Minimum y-coordinate", -55);

        maxY = BUILDER
                .worldRestart()
                .comment(
                        """
                                The highest the a mineshaft can spawn.
                                Be careful, setting this too high may make mineshafts poke through ocean floors.
                                Default: 30""".indent(1))
                .define("Maximum y-coordinate", 30);

        whitelistedDimensions = BUILDER
                .comment(
                        """
                                List of dimensions that will have Better Strongholds.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:overworld, minecraft:the_nether, undergarden:undergarden]"
                                Default: "[minecraft:overworld]\"""".indent(1))
                .worldRestart()
                .define("Whitelisted Dimensions", "[minecraft:overworld]");

        blacklistedBiomes = BUILDER
                .comment(
                        """
                                List of biomes that will NOT have Better Strongholds.
                                List must be comma-separated values enclosed in square brackets.
                                Entries must have the mod namespace included.
                                For example: "[minecraft:plains, byg:alps]"
                                Default: "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]\"""".indent(1))
                .worldRestart()
                .define("Blacklisted Biomes", "[minecraft:ocean, minecraft:frozen_ocean, minecraft:deep_ocean, minecraft:warm_ocean, minecraft:lukewarm_ocean, minecraft:cold_ocean, minecraft:deep_lukewarm_ocean, minecraft:deep_cold_ocean, minecraft:deep_frozen_ocean, minecraft:beach, minecraft:snowy_beach, minecraft:river, minecraft:frozen_river]");

        ores = new ConfigOres(BUILDER);
        spawnRates = new ConfigSpawnRates(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}