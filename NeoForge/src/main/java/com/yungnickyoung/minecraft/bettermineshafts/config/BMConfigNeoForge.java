package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class BMConfigNeoForge {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Boolean> disableVanillaMineshafts;
    public static final ModConfigSpec.ConfigValue<Integer> minY;
    public static final ModConfigSpec.ConfigValue<Integer> maxY;
    public static final ConfigOresNeoForge ores;
    public static final ConfigSpawnRatesNeoForge spawnRates;

    static {
        BUILDER.push("YUNG's Better Mineshafts");

        disableVanillaMineshafts = BUILDER
                .worldRestart()
                .comment(
                        " Whether or not vanilla mineshafts should be disabled.\n" +
                        " Default: true")
                .define("Disable Vanilla Mineshafts", true);

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

        ores = new ConfigOresNeoForge(BUILDER);
        spawnRates = new ConfigSpawnRatesNeoForge(BUILDER);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}