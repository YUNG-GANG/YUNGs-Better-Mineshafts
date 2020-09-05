package com.yungnickyoung.minecraft.bettermineshafts.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraftforge.common.BiomeDictionary;

import java.io.IOException;

/**
 * GSON TypeAdapter to serialize/deserialize {@link BiomeDictionary.Type}.
 */
public class BiomeDictionaryTypeAdapter extends TypeAdapter<BiomeDictionary.Type> {
    public BiomeDictionary.Type read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        return BiomeDictionary.Type.getType(reader.nextString());
    }

    public void write(JsonWriter writer, BiomeDictionary.Type tag) throws IOException {
        if (tag == null) {
            writer.nullValue();
            return;
        }

        writer.value(tag.getName());
    }
}
