package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BetterMineshaftFeatureConfig implements FeatureConfiguration {
    public static final Codec<BetterMineshaftFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
        instance.group (
            Codec.doubleRange(0, 1.0)
                .fieldOf("probability")
                .forGetter(config -> config.probability),
            BetterMineshaftStructureFeature.Type.CODEC
                .fieldOf("type")
                .forGetter(config -> config.type)
        ).apply(instance, BetterMineshaftFeatureConfig::new));

    public static final BetterMineshaftFeatureConfig DEFAULT = new BetterMineshaftFeatureConfig(0.003, BetterMineshaftStructureFeature.Type.NORMAL);

    public final double probability;
    public final BetterMineshaftStructureFeature.Type type;

    public BetterMineshaftFeatureConfig(double probability, BetterMineshaftStructureFeature.Type type) {
        this.probability = probability;
        this.type = type;
    }
}
