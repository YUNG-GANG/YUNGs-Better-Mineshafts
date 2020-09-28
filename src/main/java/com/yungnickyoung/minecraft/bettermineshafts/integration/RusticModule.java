package com.yungnickyoung.minecraft.bettermineshafts.integration;

import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import net.minecraft.util.EnumFacing;
import rustic.common.blocks.BlockLantern;
import rustic.common.blocks.ModBlocks;

public class RusticModule extends CompatModule {
    public RusticModule() {
        super("rustic");
    }

    @Override
    public void enable() {
        super.enable();
        if (ModBlocks.IRON_LANTERN != null) {
            this.addIfAbsent(this.lanterns, ModBlocks.IRON_LANTERN.getDefaultState().withProperty(BlockLantern.FACING, EnumFacing.DOWN));
        }
        if (ModBlocks.GOLDEN_LANTERN != null) {
            this.addIfAbsent(this.lanterns, ModBlocks.GOLDEN_LANTERN.getDefaultState().withProperty(BlockLantern.FACING, EnumFacing.DOWN));
        }
    }

    @Override
    public boolean shouldBeEnabled() {
        return super.shouldBeEnabled() && Configuration.modCompat.rusticLanternsEnabled;
    }
}
