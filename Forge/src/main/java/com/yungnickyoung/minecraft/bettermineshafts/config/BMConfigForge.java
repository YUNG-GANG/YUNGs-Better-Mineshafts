package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class BMConfigForge {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Double> mineshaftSpawnRate;
    public static final ForgeConfigSpec.ConfigValue<Integer> minY;
    public static final ForgeConfigSpec.ConfigValue<Integer> maxY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> disableVanillaMineshafts;
    public static final ConfigOresForge ores;
    public static final ConfigSpawnRatesForge spawnRates;

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

        disableVanillaMineshafts = BUILDER
                .worldRestart()
                .comment(
                        """
                                Whether or not vanilla mineshafts should be disabled.
                                Default: true""".indent(1))
                .define("Disable Vanilla Mineshafts", true);

        ores = new ConfigOresForge(BUILDER);
        spawnRates = new ConfigSpawnRatesForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}