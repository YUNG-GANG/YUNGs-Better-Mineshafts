package com.yungnickyoung.minecraft.bettermineshafts.config;

import me.shedaniel.autoconfig.annotation.ConfigEntry;

public class ConfigSpawnRatesFabric {
    @ConfigEntry.Gui.Tooltip
    public double lanternSpawnRate = .0067;

    @ConfigEntry.Gui.Tooltip
    public double torchSpawnRate = .02;

    @ConfigEntry.Gui.Tooltip
    public double workstationSpawnRate = .025;

    @ConfigEntry.Gui.Tooltip
    public double workstationDungeonSpawnRate = .25;

    @ConfigEntry.Gui.Tooltip
    public double smallShaftSpawnRate = .07;

    @ConfigEntry.Gui.Tooltip
    public double cobwebSpawnRate = .15;

    @ConfigEntry.Gui.Tooltip
    public double smallShaftChestMinecartSpawnRate = .00125;

    @ConfigEntry.Gui.Tooltip
    public double mainShaftChestMinecartSpawnRate = .01;

    @ConfigEntry.Gui.Tooltip
    public double smallShaftTntMinecartSpawnRate = .0025f;

    @ConfigEntry.Gui.Tooltip
    public double mainShaftTntMinecartSpawnRate = .0025f;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int zombieVillagerRoomSpawnChance = 2;

    @ConfigEntry.Gui.Tooltip(count = 2)
    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int smallShaftPieceChainLength = 9;
}
