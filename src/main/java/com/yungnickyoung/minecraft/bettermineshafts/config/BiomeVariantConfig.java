package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.config.Config;

public class BiomeVariantConfig {
    @Config.Name("Enable Mesa Variant")
    @Config.RequiresWorldRestart
    public boolean mesaEnabled = true;

    @Config.Name("Enable Desert Variant")
    @Config.RequiresWorldRestart
    public boolean desertEnabled = true;

    @Config.Name("Enable Red Desert Variant")
    @Config.RequiresWorldRestart
    public boolean redDesertEnabled = true;

    @Config.Name("Enable Ice Variant")
    @Config.RequiresWorldRestart
    public boolean iceEnabled = true;

    @Config.Name("Enable Snow Variant")
    @Config.RequiresWorldRestart
    public boolean snowEnabled = true;

    @Config.Name("Enable Jungle Variant")
    @Config.RequiresWorldRestart
    public boolean jungleEnabled = true;

    @Config.Name("Enable Savanna Variant")
    @Config.RequiresWorldRestart
    public boolean savannaEnabled = true;

    @Config.Name("Enable Mushroom Variant")
    @Config.RequiresWorldRestart
    public boolean mushroomEnabled = true;
}
