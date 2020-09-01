package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.config.Config;

public class SpawnRatesConfig {
    @Config.Name("Lantern Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for lanterns in the main shaft.\n" +
            "Default: .0067")
    @Config.RangeDouble(min = 0, max = 1)
    public float lanternSpawnRate = .0067f;

    @Config.Name("Torch Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for torches in small shafts.\n" +
        "Default: .02")
    @Config.RangeDouble(min = 0, max = 1)
    public float torchSpawnRate = .02f;

    @Config.Name("Workstation Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for workstation side rooms along the main shaft.\n" +
        "Default: .025")
    @Config.RangeDouble(min = 0, max = 1)
    public float workstationSpawnRate = .025f;

    @Config.Name("Workstation Cellar Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for workstation cellars below workstations along the main shaft.\n" +
        "Default: .25")
    @Config.RangeDouble(min = 0, max = 1)
    public float workstationDungeonSpawnRate = .25f;

    @Config.Name("Small Shaft Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for smaller tunnels that generate along the main shaft.\n" +
        "Default: .07")
    @Config.RangeDouble(min = 0, max = 1)
    public float smallShaftSpawnRate = .07f;

    @Config.Name("Cobweb Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for cobwebs.\n" +
        "Default: .15")
    @Config.RangeDouble(min = 0, max = 1)
    public float cobwebSpawnRate = .15f;

    @Config.Name("Small Shaft Chest Minecart Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for minecarts holding chests in small shafts.\n" +
        "Default: .00125")
    @Config.RangeDouble(min = 0, max = 1)
    public float smallShaftChestMinecartSpawnRate = .00125f;

    @Config.Name("Main Shaft Chest Minecart Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for minecarts holding chests in the main shaft.\n" +
        "Default: .01")
    @Config.RangeDouble(min = 0, max = 1)
    public float mainShaftChestMinecartSpawnRate = .01f;

    @Config.Name("Small Shaft TNT Minecart Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for minecarts holding TNT in small shafts.\n" +
        "Default: .0025")
    @Config.RangeDouble(min = 0, max = 1)
    public float smallShaftTntMinecartSpawnRate = .0025f;

    @Config.Name("Main Shaft TNT Minecart Spawn Rate")
    @Config.RequiresWorldRestart
    @Config.Comment(
        "The spawn rate for minecarts holding TNT in the main shaft.\n" +
        "Default: .0025")
    @Config.RangeDouble(min = 0, max = 1)
    public float mainShaftTntMinecartSpawnRate = .0025f;

    @Config.Name("Abandoned Miners' Outpost Spawn Chance")
    @Config.Comment(
        "Percent chance of an Abandoned Miners' Outpost to spawn at the end of a small mineshaft tunnel.\n" +
            "Default: 2")
    @Config.RequiresWorldRestart
    @Config.RangeInt(min = 0, max = 100)
    public int zombieVillagerRoomSpawnRate = 2;

    @Config.Name("Small Shaft Piece Chain Length")
    @Config.Comment(
        "The number of \"pieces\" (e.g. straight, turn, ladder, intersection, etc.) in a single small shaft.\n" +
        "This determines the overall length of small shafts.\n" +
        "Default: 9")
    @Config.RequiresWorldRestart
    @Config.RangeInt(min = 0)
    public int smallShaftPieceChainLength = 9;

    @Config.Name("Vines")
    public VinesConfig vines = new VinesConfig();
}
