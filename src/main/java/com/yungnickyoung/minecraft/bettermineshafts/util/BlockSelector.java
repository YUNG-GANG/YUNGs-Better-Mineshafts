package com.yungnickyoung.minecraft.bettermineshafts.util;

import com.mojang.datafixers.util.Pair;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Describes a set of blocks and the probability of each block in the set being chosen.
 * Includes static members used for each Better Mineshaft biome variant.
 */
public class BlockSelector {
    /**
     * Main theme blocks.
     */
    public static BlockSelector NORMAL = new BlockSelector(Blocks.OAK_PLANKS.getDefaultState())
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f);

    public static BlockSelector MESA = new BlockSelector(Blocks.DARK_OAK_PLANKS.getDefaultState())
        .addBlock(Blocks.BROWN_TERRACOTTA.getDefaultState(), 0.05f)
        .addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 0.05f)
        .addBlock(Blocks.YELLOW_TERRACOTTA.getDefaultState(), 0.05f)
        .addBlock(Blocks.WHITE_TERRACOTTA.getDefaultState(), 0.05f)
        .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f);

    public static BlockSelector JUNGLE = new BlockSelector(Blocks.JUNGLE_PLANKS.getDefaultState())
        .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.2f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
        .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 0.05f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f);

    public static BlockSelector ICE = new BlockSelector(Blocks.SPRUCE_PLANKS.getDefaultState())
        .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .25f)
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .1f)
        .addBlock(Blocks.BLUE_ICE.getDefaultState(), .1f)
        .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f);

    public static BlockSelector DESERT = new BlockSelector(Blocks.SANDSTONE.getDefaultState())
        .addBlock(Blocks.SAND.getDefaultState(), 0.3f)
        .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), 0.1f)
        .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
        .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f);

    /**
     * Stone brick blocks.
     */
    public static BlockSelector STONE_BRICK_NORMAL = new BlockSelector()
        .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f);

    public static BlockSelector STONE_BRICK_JUNGLE = new BlockSelector()
        .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .25f)
        .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), .25f)
        .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), .25f)
        .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), .25f);

    public static BlockSelector STONE_BRICK_ICE = new BlockSelector()
        .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .5f)
        .addBlock(Blocks.PACKED_ICE.getDefaultState(), .25f)
        .addBlock(Blocks.BLUE_ICE.getDefaultState(), .25f);

    public static BlockSelector STONE_BRICK_DESERT = new BlockSelector()
        .addBlock(Blocks.SAND.getDefaultState(), .25f)
        .addBlock(Blocks.SANDSTONE.getDefaultState(), .25f)
        .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), .2f)
        .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), .2f)
        .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), .1f);

    /**
     * List of pairs of blocks and their corresponding probabilities.
     * The total sum of all the probabilities cannot exceed 1.
     */
    private List<Pair<BlockState, Float>> entries = new ArrayList<>();
    private BlockState defaultBlock = Blocks.CAVE_AIR.getDefaultState();

    public BlockSelector() {
    }

    public BlockSelector(BlockState defaultBlock) {
        this.defaultBlock = defaultBlock;
    }

    public BlockSelector addBlock(BlockState blockState, float chance) {
        // Abort if blockState already a part of this selector
        for (Pair<BlockState, Float> entry : entries) {
            if (entry.getFirst() == blockState) {
                BetterMineshafts.LOGGER.warn(String.format("WARNING: duplicate block %s added to BlockSelector!", blockState.toString()));
                return this;
            }
        }

        // Attempt to add blockState to entries
        float currTotal = (float) entries.stream().mapToDouble(Pair::getSecond).sum();
        float newTotal = currTotal + chance;
        if (newTotal > 1) { // Total probability cannot exceed 1
            BetterMineshafts.LOGGER.warn(String.format("WARNING: block %s added to BlockSelector exceeds max probabiltiy of 1!", blockState.toString()));
            return this;
        }
        entries.add(new Pair<>(blockState, chance));
        return this;
    }

    public BlockState get(Random random) {
        float target = random.nextFloat();
        float currBottom = 0;

        for (Pair<BlockState, Float> entry : entries) {
            float chance = entry.getSecond();
            if (currBottom <= target && target < currBottom + chance) {
                return entry.getFirst();
            }

            currBottom += chance;
        }

        // No match found
        return this.defaultBlock;
    }
}
