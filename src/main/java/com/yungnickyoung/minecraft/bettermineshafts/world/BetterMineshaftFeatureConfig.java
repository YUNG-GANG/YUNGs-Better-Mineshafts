package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BetterMineshaftFeatureConfig implements FeatureConfig {
    public static final Codec<BetterMineshaftFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group (
            Codec.doubleRange(0, 1.0)
                .fieldOf("probability")
                .forGetter(config -> config.probability),
            BetterMineshaftStructure.Type.CODEC
                .fieldOf("type")
                .forGetter(config -> config.type)
        ).apply(instance, BetterMineshaftFeatureConfig::new));

    public static final BetterMineshaftFeatureConfig DEFAULT = new BetterMineshaftFeatureConfig(0.003, BetterMineshaftStructure.Type.NORMAL);

    public final double probability;
    public final BetterMineshaftStructure.Type type;

    public BetterMineshaftFeatureConfig(double probability, BetterMineshaftStructure.Type type) {
        this.probability = probability;
        this.type = type;
    }
}
