package com.yungnickyoung.minecraft.bettermineshafts.integration;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import noppes.vc.VCBlocks;
import noppes.vc.blocks.tiles.TileCandle;

public class VariedCommoditiesModule extends CompatModule {
    public VariedCommoditiesModule() {
        super("variedcommodities");
    }

    @Override
    public void enable() {
        super.enable();
        // Currently unused because they are super wonky
//        this.addIfAbsent(this.lanterns, VCBlocks.lamp.getDefaultState());
//        this.addIfAbsent(this.leftTorches, VCBlocks.candle.getDefaultState());
//        this.addIfAbsent(this.rightTorches, VCBlocks.candle.getDefaultState());
    }

    @Override
    public boolean shouldBeEnabled() {
        return super.shouldBeEnabled() && Configuration.modCompat.variedcommoditiesCandlesEnabled;
    }

    public boolean isTorchVariedCommoditiesCandle(IBlockState torchBlock) {
        return this.isEnabled() && torchBlock == VCBlocks.candle.getDefaultState();
    }

    public void spawnCandleEntity(World world, int x, int y, int z, int rotation) {
        TileEntity candleEntity = world.getTileEntity(new BlockPos((double) x + .5, (double) y + .5, (double) z + .5));
        BetterMineshafts.LOGGER.info(candleEntity + " -- " + new BlockPos((double) x + .5, (double) y + .5, (double) z + .5));
        if (candleEntity instanceof TileCandle) {
            ((TileCandle) candleEntity).rotation = rotation;
        }
    }
}
