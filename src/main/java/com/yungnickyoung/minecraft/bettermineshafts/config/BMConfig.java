package com.yungnickyoung.minecraft.bettermineshafts.config;

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

}
