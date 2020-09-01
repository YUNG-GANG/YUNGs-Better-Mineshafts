package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.config.Config;

public class VinesConfig {
    @Config.Name("Vine Chance")
    @Config.Comment(
        "How often vines spawn in all mineshaft biome variants except jungle.\n" +
        "By default, jungle mineshafts have more vines than the other biome variants.\n" +
        "Default: .25")
    @Config.RequiresWorldRestart
    public float vineFreq = .25f;

    @Config.Name("Jungle Mineshaft Vine Chance")
    @Config.Comment(
        "How often vines spawn in jungle mineshafts.\n" +
        "By default, jungle mineshafts have more vines than the other biome variants.\n" +
        "Default: .6")
    @Config.RequiresWorldRestart
    public float vineFreqJungle = .6f;
}
