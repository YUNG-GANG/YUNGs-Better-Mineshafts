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
                BetterMineshafts.LOGGER.error("Better Mineshafts JSON: Malformed property {}. Missing a bracket?", fullString);
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
            BetterMineshafts.LOGGER.error("Better Mineshafts JSON: Unable to read block '{}': {}", blockString, e.toString());
            BetterMineshafts.LOGGER.error("Using air instead...");
            return Blocks.AIR.getDefaultState();
        }

        if (blockState == null) {
            BetterMineshafts.LOGGER.error("Better Mineshafts JSON: Unable to read block '{}': null block returned.", blockString);
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
//        Class<T> blockEnumClass = (Class<T>) Arrays.stream(blockClass.getDeclaredClasses()).filter(Class::isEnum).findFirst().orElse(null);

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

//            // Colored variants are an exception - the "color" property is located in BlockColored and uses EnumDyeColor
//            if (key.equals("color")) {
//                if (block instanceof BlockColored) {
//                    if (BlockColored.COLOR.parseValue(value).isPresent()) {
//                        blockState = blockState.withProperty(BlockColored.COLOR, BlockColored.COLOR.parseValue(value).get());
//                        continue;
//                    } else {
//                        BetterMineshafts.LOGGER.warn("Advanced Options - Unrecognized color '{}' for block '{}'", value, blockState.getBlock().getLocalizedName());
//                    }
//                } else {
//                    BetterMineshafts.LOGGER.warn("Advanced Options - Unable to read block '{}' for property 'color'", blockState.getBlock().getLocalizedName());
//                    BetterMineshafts.LOGGER.warn("This block is probably a modded block that Better Mineshafts can't fully support. Ignoring 'color' property...");
//                    continue;
//                }
//            }
//
//            // Logs are another exception for their "variant" and "axis" properties.
//            // Logs in 1.12.2 are split into two types, OldLog and NewLog, so we must process them separately for the 'variant' property
//            if (block instanceof BlockOldLog && key.equals("variant")) {
//                if (BlockOldLog.VARIANT.parseValue(value).isPresent()) {
//                    blockState = blockState.withProperty(BlockOldLog.VARIANT, BlockOldLog.VARIANT.parseValue(value).get());
//                    continue;
//                } else {
//                    BetterMineshafts.LOGGER.warn("Advanced Options - Unrecognized variant '{}' for block '{}'", value, blockState.getBlock().getLocalizedName());
//                }
//            } else if (block instanceof BlockNewLog && key.equals("variant")) {
//                if (BlockNewLog.VARIANT.parseValue(value).isPresent()) {
//                    blockState = blockState.withProperty(BlockNewLog.VARIANT, BlockNewLog.VARIANT.parseValue(value).get());
//                    continue;
//                } else {
//                    BetterMineshafts.LOGGER.warn("Advanced Options - Unrecognized variant '{}' for block '{}'", value, blockState.getBlock().getLocalizedName());
//                }
//            }
//
//            // Log block 'axis' property is shared between the two log types
//            if (block instanceof BlockLog && key.equals("axis")) {
//                if (BlockLog.LOG_AXIS.parseValue(value).isPresent()) {
//                    blockState = blockState.withProperty(BlockLog.LOG_AXIS, BlockLog.LOG_AXIS.parseValue(value).get());
//                    continue;
//                } else {
//                    BetterMineshafts.LOGGER.warn("Advanced Options - Unrecognized axis '{}' for block '{}'", value, blockState.getBlock().getLocalizedName());
//                }
//            }

            // Main logic - iterate class fields, searching for property
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
            } catch (Exception e) {
                BetterMineshafts.LOGGER.error("Advanced Options - Encountered error while attempting to apply property {}={} to block {}: {}",
                    key, value, blockState.getBlock().getLocalizedName(), e.toString());
            }
        }

        return blockState;
    }

    /**
     * This method probably won't be called.
     * It's only here because it's necessary for the override, and so I've duplicated the write logic just in case.
     * Instead, see {@link BlockStateContainerAdapter#write}.
     */
    public void write(JsonWriter writer, IBlockState blockState) throws IOException {
        if (blockState == null) {
            writer.nullValue();
            return;
        }

        writer.value(String.valueOf(blockState));
    }
}
