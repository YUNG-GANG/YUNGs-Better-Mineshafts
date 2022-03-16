package com.yungnickyoung.minecraft.bettermineshafts.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class MineshaftDecorationChances {
    public static final Codec<MineshaftDecorationChances> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("vineChance").forGetter((config) -> config.vineChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("snowChance").forGetter((config) -> config.snowChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("cactusChance").forGetter((config) -> config.cactusChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("deadBushChance").forGetter((config) -> config.deadBushChance),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("mushroomChance").forGetter((config) -> config.mushroomChance),
                    Codec.BOOL.fieldOf("lushDecorations").forGetter((config) -> config.lushDecorations))
            .apply(instance, MineshaftDecorationChances::new));

    public float vineChance;
    public float snowChance;
    public float cactusChance;
    public float deadBushChance;
    public float mushroomChance;
    public boolean lushDecorations;

    public MineshaftDecorationChances(float vineChance, float snowChance, float cactusChance, float deadBushChance,
                                      float mushroomChance, boolean lushDecorations) {
        this.vineChance = vineChance;
        this.snowChance = snowChance;
        this.cactusChance = cactusChance;
        this.deadBushChance = deadBushChance;
        this.mushroomChance = mushroomChance;
        this.lushDecorations = lushDecorations;
    }
}
