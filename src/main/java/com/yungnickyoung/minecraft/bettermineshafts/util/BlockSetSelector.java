package com.yungnickyoung.minecraft.bettermineshafts.util;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;

import java.util.*;

/**
 * Describes a set of blocks and the probability of each block in the set being chosen.
 * Includes static members used for each Better Mineshaft biome variant.
 */
public class BlockSetSelector {
    /**
     * Map of block to its corresponding probability.
     * The total sum of all the probabilities cannot exceed 1.
     */
    private Map<IBlockState, Float> entries = new HashMap<>();
    private IBlockState defaultBlock = Blocks.AIR.getDefaultState();

    public BlockSetSelector(IBlockState defaultBlock) {
        this.defaultBlock = defaultBlock;
    }

    public BlockSetSelector() {
    }

    public static BlockSetSelector from(IBlockState... blockStates) {
        BlockSetSelector selector = new BlockSetSelector();
        float chance = 1f / blockStates.length;

        for (IBlockState state : blockStates) {
            selector.addBlock(state, chance);
        }

        return selector;
    }

    public BlockSetSelector addBlock(IBlockState blockState, float chance) {
        // Abort if blockState already a part of this selector
        for (Map.Entry<IBlockState, Float> entry : entries.entrySet()) {
            if (entry.getKey() == blockState) {
                BetterMineshafts.LOGGER.warn(String.format("WARNING: duplicate block %s added to BlockSelector!", blockState.toString()));
                return this;
            }
        }

        // Attempt to add blockState to entries
        float currTotal = entries.values().stream().reduce(Float::sum).orElse(0f);
        float newTotal = currTotal + chance;
        if (newTotal > 1) { // Total probability cannot exceed 1
            BetterMineshafts.LOGGER.warn(String.format("WARNING: block %s added to BlockSelector exceeds max probabiltiy of 1!", blockState.toString()));
            return this;
        }
        entries.put(blockState, chance);
        return this;
    }

    public IBlockState get(Random random) {
        float target = random.nextFloat();
        float currBottom = 0;

        for (Map.Entry<IBlockState, Float> entry : entries.entrySet()) {
            float chance = entry.getValue();
            if (currBottom <= target && target < currBottom + chance) {
                return entry.getKey();
            }

            currBottom += chance;
        }

        // No match found
        return this.defaultBlock;
    }
}
