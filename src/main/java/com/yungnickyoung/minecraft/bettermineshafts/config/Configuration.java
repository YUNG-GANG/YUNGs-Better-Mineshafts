package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.config.Config;

@Config(modid = BMSettings.MOD_ID, name = "bettermineshafts-1_12_2")
public class Configuration {
    @Config.Name("Mineshaft Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment("Default: .003")
    @Config.RangeDouble(min = 0, max = 1)
    public static double mineshaftSpawnRate = .003;

    @Config.Name("Spawn Rates & More")
    @Config.Comment("Customize the spawn rates for various mineshaft parts and decorations.")
    public static SpawnRatesConfig spawnRates = new SpawnRatesConfig();

    @Config.Name("Ore Deposits")
    @Config.Comment("Configure ore deposit spawn chances. MAKE SURE ALL THE VALUES ADD UP TO 100, or things won't work the way you want them to!!")
    public static OresConfig ores = new OresConfig();

    @Config.Name("Mod Compatibility")
    public static ModCompatConfig modCompat = new ModCompatConfig();
}
