package com.yungnickyoung.minecraft.bettermineshafts.integration;

import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import svenhjol.charm.crafting.feature.Lantern;
import svenhjol.charm.crafting.block.BlockLantern;

public class CharmModule extends CompatModule {
    public CharmModule() {
        super("charm");
    }

    @Override
    public void enable() {
        super.enable();
        this.addIfAbsent(this.lanterns, Lantern.ironLantern.getDefaultState().withProperty(BlockLantern.HANGING, true));
        this.addIfAbsent(this.lanterns, Lantern.goldLantern.getDefaultState().withProperty(BlockLantern.HANGING, true));
    }

    @Override
    public boolean shouldBeEnabled() {
        return super.shouldBeEnabled() && Configuration.modCompat.charmLanternsEnabled;
    }
}
