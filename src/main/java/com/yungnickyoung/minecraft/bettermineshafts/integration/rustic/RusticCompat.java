package com.yungnickyoung.minecraft.bettermineshafts.integration.rustic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import rustic.common.blocks.BlockLantern;
import rustic.common.blocks.ModBlocks;

import java.util.Random;

public class RusticCompat {
    public static boolean ENABLED = false;
    public static IBlockState IRON_LANTERN;
    public static IBlockState GOLDEN_LANTERN;

    public static void init(boolean enabled) {
        ENABLED = enabled;
        IRON_LANTERN = ModBlocks.IRON_LANTERN.getDefaultState().withProperty(BlockLantern.FACING, EnumFacing.DOWN);
        GOLDEN_LANTERN = ModBlocks.GOLDEN_LANTERN.getDefaultState().withProperty(BlockLantern.FACING, EnumFacing.DOWN);
    }

    public static IBlockState getLantern(Random random) {
        float r = random.nextFloat();
        if (r < .5f) return IRON_LANTERN;
        return GOLDEN_LANTERN;
    }
}
