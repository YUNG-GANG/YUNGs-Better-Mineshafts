package com.yungnickyoung.minecraft.bettermineshafts.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import java.io.IOException;

/**
 * GSON TypeAdapter to serialize/deserialize {@link BlockStateContainer.StateImplementation}.
 */
public class BlockStateContainerAdapter extends TypeAdapter<BlockStateContainer.StateImplementation> {
    /**
     * This method probably won't be called.
     * It's only here because it's necessary for the override,
     * and so I've added basic read logic (that doesn't support properties) just in case.
     * Instead, see {@link BlockStateAdapter#read}.
     */
    public BlockStateContainer.StateImplementation read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        IBlockState blockState;
        String blockString = reader.nextString();

        try {
            blockState = Block.getBlockFromName(blockString).getDefaultState();
        } catch (Exception e) {
            BetterMineshafts.LOGGER.warn("variants.json: Unable to read block '{}': {}", blockString, e.toString());
            BetterMineshafts.LOGGER.warn("Using air instead...");
            blockState = Blocks.AIR.getDefaultState();
        }

        if (blockState == null) {
            BetterMineshafts.LOGGER.warn("variants.json: Unable to read block '{}': null block returned.", blockString);
            BetterMineshafts.LOGGER.warn("Using air instead...");
            blockState = Blocks.AIR.getDefaultState();
        }

        return (BlockStateContainer.StateImplementation) blockState;
    }

    public void write(JsonWriter writer, BlockStateContainer.StateImplementation blockState) throws IOException {
        if (blockState == null) {
            writer.nullValue();
            return;
        }

        writer.value(String.valueOf(blockState));
    }
}
