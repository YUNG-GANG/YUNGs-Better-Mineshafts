package com.yungnickyoung.minecraft.bettermineshafts.config;

public class BMConfig {
    public static void bake() {
        mineshaftSpawnRate = BMForgeConfig.mineshaftSpawnRate.get();
    }

    public static double mineshaftSpawnRate;
}
