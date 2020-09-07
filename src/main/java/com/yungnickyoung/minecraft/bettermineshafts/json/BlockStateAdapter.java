package com.yungnickyoung.minecraft.bettermineshafts.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.IStringSerializable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * GSON TypeAdapter to serialize/deserialize {@link IBlockState}.
 */
public class BlockStateAdapter extends TypeAdapter<IBlockState> {
    public IBlockState read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        IBlockState blockState;
        Map<String, String> properties = new HashMap<>();
        String fullString = reader.nextString();
        String blockString = fullString;

        int startIndex = fullString.indexOf('[');
        int stopIndex = fullString.indexOf(']');

        if (startIndex != -1) {
            blockString = fullString.substring(0, startIndex);
            if (stopIndex < startIndex) {
                BetterMineshafts.LOGGER.error("variants.json: Malformed property {}. Missing a bracket?", fullString);
                BetterMineshafts.LOGGER.error("Using air instead...");
                return Blocks.AIR.getDefaultState();
            }

            int index = startIndex + 1;
            String currKey = "";
            StringBuilder currString = new StringBuilder();

            while (index <= stopIndex) {
                char currChar = fullString.charAt(index);

                if (currChar == '=') {
                    currKey = currString.toString();
                    currString = new StringBuilder();
                } else if (currChar == ',' || currChar == ']') {
                    properties.put(currKey, currString.toString());
                    currString = new StringBuilder();
                } else {
                    currString.append(fullString.charAt(index));
                }

                index++;
            }
        }

        try {
            blockState = Block.getBlockFromName(blockString).getDefaultState();
        } catch (Exception e) {
            BetterMineshafts.LOGGER.error("variants.json: Unable to read block '{}': {}", blockString, e.toString());
            BetterMineshafts.LOGGER.error("Using air instead...");
            return Blocks.AIR.getDefaultState();
        }

        if (blockState == null) {
            BetterMineshafts.LOGGER.error("variants.json: Unable to read block '{}': null block returned.", blockString);
            BetterMineshafts.LOGGER.error("Using air instead...");
            return Blocks.AIR.getDefaultState();
        }

        if (properties.size() > 0) {
            blockState = getConfiguredBlockState(blockState, properties);
        }

        return blockState;
    }

    /**
     * Attempts to parse the properties from the provided properties map and apply them to the provided blockstate
     * @param blockState
     * @param properties Map of property names to property values
     * @param <T> The type of the property enum, usually resides within the Block's class
     * @return The configured blockstate
     */
    @SuppressWarnings("unchecked")
    private <T extends Enum<T> & IStringSerializable> IBlockState getConfiguredBlockState(IBlockState blockState, Map<String, String> properties) {
        // Convert string property name/val into actual stuff that I can somehow apply to the blockstate
        Block block = blockState.getBlock();

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Iterate class and superclass fields, searching for property
            try {
                boolean found = false;
                Class<?> blockClass = block.getClass();
                while (blockClass != Block.class && !found) {
                    Field[] fields = blockClass.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Object _field = field.get(blockState);

                        if (_field instanceof IProperty) {
                            IProperty<T> property = (IProperty<T>) _field;

                            if (property.getName().equals(key)) {
                                Object val = property.parseValue(value).orNull();
                                Class<T> blockEnumClass = property.getValueClass();
                                blockState = blockState.withProperty(property, Objects.requireNonNull(blockEnumClass.cast(val)));
                                found = true;
                                break;
                            }
                        }
                    }
                    blockClass = blockClass.getSuperclass();
                }
                if (!found) {
                    BetterMineshafts.LOGGER.error("variants.json: Unable to find property {} for block {}", key, block.getLocalizedName());
                }
            } catch (Exception e) {
                BetterMineshafts.LOGGER.error("variants.json: Encountered error while attempting to apply property {}={} to block {}: {}",
                    key, value, block.getLocalizedName(), e.toString());
            }
        }

        return blockState;
    }

    public void write(JsonWriter writer, IBlockState blockState) throws IOException {
        if (blockState == null) {
            writer.nullValue();
            return;
        }

        writer.value(String.valueOf(blockState));
    }
}
