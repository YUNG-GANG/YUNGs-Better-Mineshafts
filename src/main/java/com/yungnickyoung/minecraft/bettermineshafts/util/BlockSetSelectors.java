package com.yungnickyoung.minecraft.bettermineshafts.util;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

/**
 * Singleton class holding all of our BlockSetSelectors.
 * The class is a singleton so that it may be synchronized with the JSON file as a single source of truth.
 */
public class BlockSetSelectors {
    /**
     * Singleton stuff.
     */
    public static BlockSetSelectors instance; // This technically shouldn't be public, but it is necessary for loading data from JSON
    private BlockSetSelectors() {}

    public static BlockSetSelectors get() {
        if (instance == null) {
            instance = new BlockSetSelectors();
        }
        return instance;
    }

    /**
     * Main theme blocks.
     */
    public BlockSetSelector NORMAL = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK))
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector MESA = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK))
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN), 0.05f)
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE), 0.05f)
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW), 0.05f)
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector JUNGLE = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE))
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.2f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector SNOW = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE))
        .addBlock(Blocks.SNOW.getDefaultState(), .25f)
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .2f)
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector ICE = new BlockSetSelector(Blocks.PACKED_ICE.getDefaultState())
        .addBlock(Blocks.SNOW.getDefaultState(), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.1f);

    public BlockSetSelector DESERT = new BlockSetSelector(Blocks.SANDSTONE.getDefaultState())
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector RED_DESERT = new BlockSetSelector(Blocks.RED_SANDSTONE.getDefaultState())
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector MUSHROOM = new BlockSetSelector()
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), .33333f)
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
        .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f);

    public BlockSetSelector ACACIA = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    /**
     * Floor variants.
     */
    public BlockSetSelector FLOOR_DESERT = new BlockSetSelector(Blocks.SANDSTONE.getDefaultState())
        .addBlock(Blocks.SAND.getDefaultState(), 0.3f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector FLOOR_RED_DESERT = new BlockSetSelector(Blocks.RED_SANDSTONE.getDefaultState())
        .addBlock(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), 0.3f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public BlockSetSelector FLOOR_MUSHROOM = new BlockSetSelector()
        .addBlock(Blocks.MYCELIUM.getDefaultState(), 1);

    /**
     * Stone brick blocks.
     */
    public BlockSetSelector STONE_BRICK_NORMAL = new BlockSetSelector()
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.33333f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.33333f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.33333f);

    public BlockSetSelector STONE_BRICK_JUNGLE = new BlockSetSelector()
        .addBlock(Blocks.STONEBRICK.getDefaultState(), .25f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), .25f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), .25f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), .25f);

    public BlockSetSelector STONE_BRICK_SNOW = new BlockSetSelector()
        .addBlock(Blocks.SNOW.getDefaultState(), .5f)
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .5f);

    public BlockSetSelector STONE_BRICK_ICE = new BlockSetSelector()
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .9f)
        .addBlock(Blocks.SNOW.getDefaultState(), .1f);

    public BlockSetSelector STONE_BRICK_DESERT = new BlockSetSelector()
        .addBlock(Blocks.SANDSTONE.getDefaultState(), .5f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), .1f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), .2f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), .2f);

    public BlockSetSelector STONE_BRICK_RED_DESERT = new BlockSetSelector()
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState(), .5f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), .1f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), .2f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), .2f);

    public BlockSetSelector STONE_BRICK_MUSHROOM = new BlockSetSelector()
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), .33333f)
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
        .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f);
}
