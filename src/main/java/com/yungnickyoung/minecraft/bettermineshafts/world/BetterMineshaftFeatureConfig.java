package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BetterMineshaftFeatureConfig implements FeatureConfig {
    public final double probability;
    public final BetterMineshaftFeature.Type type;

    public BetterMineshaftFeatureConfig(double probability, BetterMineshaftFeature.Type type) {
        this.probability = probability;
        this.type = type;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops,
            ops.createMap(ImmutableMap.of(
                ops.createString("probability"),
                ops.createDouble(this.probability),
                ops.createString("type"),
                ops.createString(this.type.getName())
            ))
        );
    }

    public static <T> BetterMineshaftFeatureConfig deserialize(Dynamic<T> dynamic) {
        float f = dynamic.get("probability").asFloat(0.0F);
        BetterMineshaftFeature.Type type = BetterMineshaftFeature.Type.byName(dynamic.get("type").asString(""));
        return new BetterMineshaftFeatureConfig(f, type);
    }
}
