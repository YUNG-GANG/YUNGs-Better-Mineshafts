package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.minecraftforge.common.config.Config;

public class ModCompatConfig {
    @Config.Name("Use Rustic Lanterns")
    @Config.Comment("If Rustic is installed, mineshafts will occasionally have Rustic lanterns hanging from the ceiling.")
    @Config.RequiresWorldRestart
    public boolean rusticLanternsEnabled = true;

    @Config.Name("Use Charm Lanterns")
    @Config.Comment("If Charm is installed, mineshafts will occasionally have Charm lanterns hanging from the ceiling.")
    @Config.RequiresWorldRestart
    public boolean charmLanternsEnabled = true;

    @Config.Ignore // Ignore this since I couldn't get it to work properly
    @Config.Name("Use Varied Commodities Candles")
    @Config.Comment("If Varied Commodities is installed, mineshafts will occasionally have Varied Commodities candles on the walls.")
    @Config.RequiresWorldRestart
    public boolean variedcommoditiesCandlesEnabled = true;
}
