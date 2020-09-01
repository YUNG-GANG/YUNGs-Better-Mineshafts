package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.config.Config;

public class OresConfig {
    @Config.Name("Enable Ore Deposits")
    @Config.RequiresWorldRestart
    public boolean enabled = true;

    @Config.Name("Cobble Spawn Chance (Empty deposit)")
    @Config.Comment(
        "Percent chance of an ore deposit being cobblestone only.\n" +
        "Default: 50")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int cobble = 50;

    @Config.Name("Coal Spawn Chance")
    @Config.Comment(
        "Percent chance of an ore deposit containing coal.\n" +
        "Default: 20")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int coal = 20;

    @Config.Name("Iron Spawn Chance")
    @Config.Comment(
        "Percent chance of an ore deposit containing iron.\n" +
        "Default: 9")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int iron = 9;

    @Config.Name("Redstone Spawn Chance")
    @Config.Comment(
        "Percent chance of an ore deposit containing redstone.\n" +
        "Default: 7")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int redstone = 7;

    @Config.Name("Gold Spawn Chance")
    @Config.Comment(
        "Percent chance of an ore deposit containing gold.\n" +
        "Default: 7")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int gold = 7;

    @Config.Name("Lapis Spawn Chance")
    @Config.Comment(
        "Percent chance of an ore deposit containing lapis.\n" +
        "Default: 3")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int lapis = 3;

    @Config.Name("Emerald Spawn Chance")
    @Config.Comment(
        "Percent chance of an ore deposit containing emerald.\n" +
        "Default: 3")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int emerald = 3;

    @Config.Name("Diamond Spawn Chance")
    @Config.Comment(
        "Percent chance of an ore deposit containing diamond.\n" +
        "Default: 1")
    @Config.RangeInt(min = 0, max = 100)
    @Config.RequiresWorldRestart
    public int diamond = 1;
}
