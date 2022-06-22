package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class BMConfigForge {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> minY;
    public static final ForgeConfigSpec.ConfigValue<Integer> maxY;
    public static final ConfigOresForge ores;
    public static final ConfigSpawnRatesForge spawnRates;

    static {
        BUILDER.push("YUNG's Better Mineshafts");

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
                                The highest a mineshaft can spawn.
                                Default: 30""".indent(1))
                .define("Maximum y-coordinate", 30);

        ores = new ConfigOresForge(BUILDER);
        spawnRates = new ConfigSpawnRatesForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}