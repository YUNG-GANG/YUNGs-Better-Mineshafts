package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.gen.feature.IFeatureConfig;

@MethodsReturnNonnullByDefault
public class BetterMineshaftConfig implements IFeatureConfig {
    public static final Codec<BetterMineshaftConfig> CODEC = RecordCodecBuilder.create(builder ->
        builder.group(
            Codec.DOUBLE.fieldOf("probability").forGetter(config -> config.probability),
            BetterMineshaftStructure.Type.field_236324_c_.fieldOf("type").forGetter(config -> config.type)
        ).apply(builder, BetterMineshaftConfig::new));

    public double probability;
    public final BetterMineshaftStructure.Type type;

    public BetterMineshaftConfig(double probability, BetterMineshaftStructure.Type type) {
        this.probability = probability;
        this.type = type;
    }
}
