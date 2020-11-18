package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BetterMineshaftConfig implements FeatureConfig {
    public static final Codec<BetterMineshaftConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group (
            Codec.doubleRange(0, 1.0)
                .fieldOf("probability")
                .forGetter(config -> config.probability),
            BetterMineshaftStructure.Type.CODEC
                .fieldOf("type")
                .forGetter(config -> config.type)
        ).apply(instance, BetterMineshaftConfig::new));

    public static final BetterMineshaftConfig DEFAULT = new BetterMineshaftConfig(0.003, BetterMineshaftStructure.Type.NORMAL);

    public final double probability;
    public final BetterMineshaftStructure.Type type;

    public BetterMineshaftConfig(double probability, BetterMineshaftStructure.Type type) {
        this.probability = probability;
        this.type = type;
    }
}
