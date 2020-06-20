package com.yungnickyoung.minecraft.bettermineshafts.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class SurfaceUtil {
    public static int getSurfaceHeight(World world, ColPos pos) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(pos.x, 255, pos.z);

        // Edge case: blocks go all the way up to build height
        if (world.getBlockState(new BlockPos(blockPos.getX(), 255, blockPos.getZ())) != Blocks.AIR.getDefaultState())
            return 255;

        for (int y = 255; y >= 0; y--) {
            IBlockState blockState = world.getBlockState(blockPos);
            if (blockState != Blocks.AIR.getDefaultState())
                return y;
            blockPos.move(EnumFacing.DOWN);
        }

        return 1; // Surface somehow not found
    }
}
