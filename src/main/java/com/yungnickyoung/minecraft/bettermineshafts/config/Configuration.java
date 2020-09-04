package com.yungnickyoung.minecraft.bettermineshafts.config;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraftforge.common.config.Config;

@Config(modid = BMSettings.MOD_ID, name = "bettermineshafts-1_12_2")
public class Configuration {
    @Config.Name("Mineshaft Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment("Default: .003")
    @Config.RangeDouble(min = 0, max = 1)
    public static double mineshaftSpawnRate = .003;

    @Config.Name("Minimum y-coordinate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The lowest the floor of a mineshaft can be. If you set this too low the mineshafts will get burned by lava!\n" +
        "Default: 17")
    @Config.RangeInt(min = 0, max = 255)
    public static int minY = 17;

    @Config.Name("Maximum y-coordinate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The highest the floor of a mineshaft can be.\n" +
        "Default: 37")
    @Config.RangeInt(min = 0, max = 255)
    public static int maxY = 37;

    @Config.Name("Spawn Rates & More")
    @Config.Comment("Customize the spawn rates for various mineshaft parts and decorations.")
    public static SpawnRatesConfig spawnRates = new SpawnRatesConfig();

    @Config.Name("Biome Variants")
    @Config.Comment("Enable or disable different biome-dependent mineshaft variants.")
    public static BiomeVariantConfig biomeVariants = new BiomeVariantConfig();

    @Config.Name("Ore Deposits")
    @Config.Comment("Configure ore deposit spawn chances. MAKE SURE ALL THE VALUES ADD UP TO 100, or things won't work the way you want them to!!")
    public static OresConfig ores = new OresConfig();

    @Config.Name("Mod Compatibility")
    public static ModCompatConfig modCompat = new ModCompatConfig();
}
