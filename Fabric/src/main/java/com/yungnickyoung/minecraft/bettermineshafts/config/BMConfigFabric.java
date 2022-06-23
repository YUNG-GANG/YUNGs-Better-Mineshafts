package com.yungnickyoung.minecraft.bettermineshafts.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "bettermineshafts-fabric-1_19")
public class BMConfigFabric implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public int minY = -55;

    @ConfigEntry.Gui.Tooltip
    public int maxY = 30;

    @ConfigEntry.Gui.Tooltip
    public boolean disableVanillaMineshafts = true;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public ConfigSpawnRatesFabric spawnRates = new ConfigSpawnRatesFabric();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip(count = 3)
    public ConfigOresFabric ores = new ConfigOresFabric();
}
