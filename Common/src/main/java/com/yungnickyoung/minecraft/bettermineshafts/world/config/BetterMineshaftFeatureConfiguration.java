package com.yungnickyoung.minecraft.bettermineshafts.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BetterMineshaftFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<BetterMineshaftFeatureConfiguration> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    Codec.BOOL.fieldOf("flammableLegs").forGetter((config) -> config.flammableLegs),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("replacementRate").forGetter((config) -> config.replacementRate),
                    BetterMineshaftStructureFeature.LegVariant.CODEC.fieldOf("legVariant").forGetter((config) -> config.legVariant),
                    MineshaftDecorationChances.CODEC.fieldOf("decorationChances").forGetter((config) -> config.decorationChances),
                    MineshaftBlockStates.CODEC.fieldOf("blockStates").forGetter((config) -> config.blockStates),
                    MineshaftBlockstateRandomizers.CODEC.fieldOf("blockStateRandomizers").forGetter((config) -> config.blockStateRandomizers))
            .apply(instance, BetterMineshaftFeatureConfiguration::new));

    public boolean flammableLegs;
    public float replacementRate;
    public BetterMineshaftStructureFeature.LegVariant legVariant;
    public MineshaftDecorationChances decorationChances;
    public MineshaftBlockStates blockStates;
    public MineshaftBlockstateRandomizers blockStateRandomizers;

    public BetterMineshaftFeatureConfiguration(boolean flammableLegs, float replacementRate,
                                               BetterMineshaftStructureFeature.LegVariant legVariant,
                                               MineshaftDecorationChances decorationChances,
                                               MineshaftBlockStates blockStates,
                                               MineshaftBlockstateRandomizers blockStateRandomizers) {
        this.flammableLegs = flammableLegs;
        this.replacementRate = replacementRate;
        this.legVariant = legVariant;
        this.decorationChances = decorationChances;
        this.blockStates = blockStates;
        this.blockStateRandomizers = blockStateRandomizers;
    }
}
