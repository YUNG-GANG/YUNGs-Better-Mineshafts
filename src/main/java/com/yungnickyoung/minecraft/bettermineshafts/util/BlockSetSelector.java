package com.yungnickyoung.minecraft.bettermineshafts.util;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Describes a set of blocks and the probability of each block in the set being chosen.
 * Includes static members used for each Better Mineshaft biome variant.
 */
public class BlockSetSelector {
    /**
     * Main theme blocks.
     */
    public static BlockSetSelector NORMAL = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK))
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public static BlockSetSelector MESA = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK))
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN), 0.05f)
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE), 0.05f)
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW), 0.05f)
        .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public static BlockSetSelector JUNGLE = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE))
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.2f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public static BlockSetSelector SNOW = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE))
        .addBlock(Blocks.SNOW.getDefaultState(), .25f)
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .2f)
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public static BlockSetSelector ICE = new BlockSetSelector(Blocks.PACKED_ICE.getDefaultState())
        .addBlock(Blocks.SNOW.getDefaultState(), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.1f);

    public static BlockSetSelector DESERT = new BlockSetSelector(Blocks.SANDSTONE.getDefaultState())
        .addBlock(Blocks.SAND.getDefaultState(), 0.3f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public static BlockSetSelector RED_DESERT = new BlockSetSelector(Blocks.RED_SANDSTONE.getDefaultState())
        .addBlock(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), 0.3f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), 0.1f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    public static BlockSetSelector MUSHROOM = new BlockSetSelector()
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), .33333f)
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
        .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f);

    public static BlockSetSelector ACACIA = new BlockSetSelector(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
        .addBlock(Blocks.AIR.getDefaultState(), 0.2f);

    /**
     * Stone brick blocks.
     */
    public static BlockSetSelector STONE_BRICK_NORMAL = new BlockSetSelector()
        .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.33333f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.33333f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.33333f);

    public static BlockSetSelector STONE_BRICK_JUNGLE = new BlockSetSelector()
        .addBlock(Blocks.STONEBRICK.getDefaultState(), .25f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), .25f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), .25f)
        .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), .25f);

    public static BlockSetSelector STONE_BRICK_SNOW = new BlockSetSelector()
        .addBlock(Blocks.SNOW.getDefaultState(), .5f)
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .5f);

    public static BlockSetSelector STONE_BRICK_ICE = new BlockSetSelector()
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .9f)
        .addBlock(Blocks.SNOW.getDefaultState(), .1f);

    public static BlockSetSelector STONE_BRICK_DESERT = new BlockSetSelector()
        .addBlock(Blocks.SAND.getDefaultState(), .25f)
        .addBlock(Blocks.SANDSTONE.getDefaultState(), .35f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), .2f)
        .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), .2f);

    public static BlockSetSelector STONE_BRICK_RED_DESERT = new BlockSetSelector()
        .addBlock(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), .25f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState(), .35f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), .2f)
        .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), .2f);

    public static BlockSetSelector STONE_BRICK_MUSHROOM = new BlockSetSelector()
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), .33333f)
        .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
        .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f);

    /**
     * List of pairs of blocks and their corresponding probabilities.
     * The total sum of all the probabilities cannot exceed 1.
     */
    private List<Pair<IBlockState, Float>> entries = new ArrayList<>();
    private IBlockState defaultBlock = Blocks.AIR.getDefaultState();

    public BlockSetSelector() {
    }

    public BlockSetSelector(IBlockState defaultBlock) {
        this.defaultBlock = defaultBlock;
    }

    public BlockSetSelector addBlock(IBlockState blockState, float chance) {
        // Abort if blockState already a part of this selector
        for (Pair<IBlockState, Float> entry : entries) {
            if (entry.getLeft() == blockState) {
                BetterMineshafts.LOGGER.warn(String.format("WARNING: duplicate block %s added to BlockSelector!", blockState.toString()));
                return this;
            }
        }

        // Attempt to add blockState to entries
        float currTotal = (float) entries.stream().mapToDouble(Pair::getRight).sum();
        float newTotal = currTotal + chance;
        if (newTotal > 1) { // Total probability cannot exceed 1
            BetterMineshafts.LOGGER.warn(String.format("WARNING: block %s added to BlockSelector exceeds max probabiltiy of 1!", blockState.toString()));
            return this;
        }
        entries.add(new Pair<>(blockState, chance));
        return this;
    }

    public IBlockState get(Random random) {
        float target = random.nextFloat();
        float currBottom = 0;

        for (Pair<IBlockState, Float> entry : entries) {
            float chance = entry.getRight();
            if (currBottom <= target && target < currBottom + chance) {
                return entry.getLeft();
            }

            currBottom += chance;
        }

        // No match found
        return this.defaultBlock;
    }
}
