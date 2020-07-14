package com.yungnickyoung.minecraft.bettermineshafts.config;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraftforge.common.config.Config;

@Config(modid = BetterMineshafts.MOD_ID, name = "bettermineshafts-1_12_2")
public class Configuration {
    @Config.Name("Mineshaft Spawn Rate")
    @Config.RequiresWorldRestart
    public static double mineshaftSpawnRate = .003;

    @Config.Name("Minimum y-coordinate")
    @Config.RequiresWorldRestart
    public static int minY = 13;

    @Config.Name("Maximum y-coordinate")
    @Config.RequiresWorldRestart
    @Config.Comment("The highest the floor of the shaft can be.\n" +
        "Be careful, setting this too high can make mineshafts poke through ocean floors.")
    public static int maxY = 37;

    @Config.Name("Enable Mesa Variant")
    @Config.RequiresWorldRestart
    public static boolean mesaEnabled = true;

    @Config.Name("Enable Desert Variant")
    @Config.RequiresWorldRestart
    public static boolean desertEnabled = true;

    @Config.Name("Enable Red Desert Variant")
    @Config.RequiresWorldRestart
    public static boolean redDesertEnabled = true;

    @Config.Name("Enable Ice Variant")
    @Config.RequiresWorldRestart
    public static boolean iceEnabled = true;

    @Config.Name("Enable Snow Variant")
    @Config.RequiresWorldRestart
    public static boolean snowEnabled = true;

    @Config.Name("Enable Jungle Variant")
    @Config.RequiresWorldRestart
    public static boolean jungleEnabled = true;

    @Config.Name("Enable Savanna Variant")
    @Config.RequiresWorldRestart
    public static boolean savannaEnabled = true;

    @Config.Name("Enable Mushroom Variant")
    @Config.RequiresWorldRestart
    public static boolean mushroomEnabled = true;

    @Config.Name("Mod Compatibility")
    public static ModCompat modCompat = new ModCompat();

    public static class ModCompat {
        @Config.Name("Use Rustic Lanterns")
        @Config.Comment("If Rustic is installed, mineshafts will occasionally have lanterns hanging from the ceiling.")
        @Config.RequiresWorldRestart
        public boolean rusticLanternsEnabled = true;
    }
}
