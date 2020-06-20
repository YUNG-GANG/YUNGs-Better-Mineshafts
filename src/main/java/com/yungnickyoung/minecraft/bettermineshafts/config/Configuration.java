package com.yungnickyoung.minecraft.bettermineshafts.config;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraftforge.common.config.Config;

@Config(modid = BetterMineshafts.MOD_ID, name = "bettermineshafts-1_12_2")
public class Configuration {
    @Config.Name("Mineshaft Spawn Rate")
    @Config.RequiresWorldRestart
    public static double mineshaftSpawnRate = .03;
}
