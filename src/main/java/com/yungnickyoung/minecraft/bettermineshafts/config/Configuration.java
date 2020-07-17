package com.yungnickyoung.minecraft.bettermineshafts.config;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraftforge.common.config.Config;

@Config(modid = BetterMineshafts.MOD_ID, name = "bettermineshafts-1_12_2")
public class Configuration {
    @Config.Name("Mineshaft Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment("Default: .003")
    public static double mineshaftSpawnRate = .003;

    @Config.Name("Minimum y-coordinate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The lowest the floor of a mineshaft can be.\n" +
        "Default: 13")
    public static int minY = 13;

    @Config.Name("Maximum y-coordinate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The highest the floor of a mineshaft can be.\n" +
        "Be careful, setting this too high can make mineshafts poke through ocean floors." +
        "Default: 37")
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

    @Config.Name("Vines")
    public static Vines vines = new Vines();

    @Config.Name("Ore Deposits")
    @Config.Comment("Configure ore deposit spawn chances. MAKE SURE ALL THE VALUES ADD UP TO 100, or things won't work the way you want them to!!")
    public static Ores ores = new Ores();

    @Config.Name("Mod Compatibility")
    public static ModCompat modCompat = new ModCompat();

    public static class ModCompat {
        @Config.Name("Use Rustic Lanterns")
        @Config.Comment("If Rustic is installed, mineshafts will occasionally have Rustic lanterns hanging from the ceiling.")
        @Config.RequiresWorldRestart
        public boolean rusticLanternsEnabled = true;

        @Config.Name("Use Charm Lanterns")
        @Config.Comment("If Charm is installed, mineshafts will occasionally have Charm lanterns hanging from the ceiling.")
        @Config.RequiresWorldRestart
        public boolean charmLanternsEnabled = true;
    }

    public static class Vines {
        @Config.Name("Vine Chance")
        @Config.Comment(
            "How often vines spawn in all variants except jungle.\n" +
            "Default: .25")
        @Config.RequiresWorldRestart
        public float vineFreq = .25f;

        @Config.Name("Jungle Mineshaft Vine Chance")
        @Config.Comment(
            "How often vines spawn in jungle mineshafts.\n" +
            "Default: .6")
        @Config.RequiresWorldRestart
        public float vineFreqJungle = .6f;
    }

    public static class Ores {
        @Config.Name("Enable Ore Deposits")
        @Config.RequiresWorldRestart
        public boolean enabled = true;

        @Config.Name("Cobble Spawn Chance (Empty deposit)")
        @Config.Comment(
            "Chance of an ore deposit being cobblestone only.\n" +
            "Default: 50")
        @Config.RequiresWorldRestart
        public int cobble = 50;

        @Config.Name("Coal Spawn Chance")
        @Config.Comment(
            "Chance of an ore deposit containing coal.\n" +
            "Default: 20")
        @Config.RequiresWorldRestart
        public int coal = 20;

        @Config.Name("Iron Spawn Chance")
        @Config.Comment(
            "Chance of an ore deposit containing iron.\n" +
            "Default: 9")
        @Config.RequiresWorldRestart
        public int iron = 9;

        @Config.Name("Redstone Spawn Chance")
        @Config.Comment(
            "Chance of an ore deposit containing redstone.\n" +
            "Default: 7")
        @Config.RequiresWorldRestart
        public int redstone = 7;

        @Config.Name("Gold Spawn Chance")
        @Config.Comment(
            "Chance of an ore deposit containing gold.\n" +
            "Default: 7")
        @Config.RequiresWorldRestart
        public int gold = 7;

        @Config.Name("Lapis Spawn Chance")
        @Config.Comment(
            "Chance of an ore deposit containing lapis.\n" +
            "Default: 3")
        @Config.RequiresWorldRestart
        public int lapis = 3;

        @Config.Name("Emerald Spawn Chance")
        @Config.Comment(
            "Chance of an ore deposit containing emerald.\n" +
            "Default: 3")
        @Config.RequiresWorldRestart
        public int emerald = 3;

        @Config.Name("Diamond Spawn Chance")
        @Config.Comment(
            "Chance of an ore deposit containing diamond.\n" +
            "Default: 1")
        @Config.RequiresWorldRestart
        public int diamond = 1;
    }
}
