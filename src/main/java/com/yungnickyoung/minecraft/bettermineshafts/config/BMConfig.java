package com.yungnickyoung.minecraft.bettermineshafts.config;

import com.yungnickyoung.minecraft.bettermineshafts.init.ModStructures;

public class BMConfig {
    public static void bake() {
        mineshaftSpawnRate = BMForgeConfig.mineshaftSpawnRate.get();
        minY = BMForgeConfig.minY.get();
        maxY = BMForgeConfig.maxY.get();
        mesaEnabled = BMForgeConfig.mesaEnabled.get();
        desertEnabled = BMForgeConfig.desertEnabled.get();
        redDesertEnabled = BMForgeConfig.redDesertEnabled.get();
        iceEnabled = BMForgeConfig.iceEnabled.get();
        snowEnabled = BMForgeConfig.snowEnabled.get();
        jungleEnabled = BMForgeConfig.jungleEnabled.get();
        savannaEnabled = BMForgeConfig.savannaEnabled.get();
        mushroomEnabled = BMForgeConfig.mushroomEnabled.get();
        vineFreq = BMForgeConfig.vines.vineFreq.get();
        vineFreqJungle = BMForgeConfig.vines.vineFreqJungle.get();
        oreEnabled = BMForgeConfig.ores.enabled.get();
        cobbleOre = BMForgeConfig.ores.cobble.get();
        coalOre = BMForgeConfig.ores.coal.get();
        ironOre = BMForgeConfig.ores.iron.get();
        redstoneOre = BMForgeConfig.ores.redstone.get();
        goldOre = BMForgeConfig.ores.gold.get();
        lapisOre = BMForgeConfig.ores.lapis.get();
        emeraldOre = BMForgeConfig.ores.emerald.get();
        diamondOre = BMForgeConfig.ores.diamond.get();
        ModStructures.STRUCTURE_FEATURE_LIST.forEach(structureFeature ->
            structureFeature.field_236269_c_.probability = mineshaftSpawnRate
        );
    }

    public static double mineshaftSpawnRate;
    public static int minY;
    public static int maxY;

    public static boolean mesaEnabled;
    public static boolean desertEnabled;
    public static boolean redDesertEnabled;
    public static boolean iceEnabled;
    public static boolean snowEnabled;
    public static boolean jungleEnabled;
    public static boolean savannaEnabled;
    public static boolean mushroomEnabled;

    public static double vineFreq;
    public static double vineFreqJungle;

    public static boolean oreEnabled;
    public static int cobbleOre;
    public static int coalOre;
    public static int ironOre;
    public static int redstoneOre;
    public static int goldOre;
    public static int lapisOre;
    public static int emeraldOre;
    public static int diamondOre;
}