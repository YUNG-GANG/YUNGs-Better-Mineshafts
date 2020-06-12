package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
public class BetterMineshaftFeatureConfig implements IFeatureConfig {
    public final double probability;
    public final BetterMineshaftStructure.Type type;

    public BetterMineshaftFeatureConfig(double probability, BetterMineshaftStructure.Type type) {
        this.probability = probability;
        this.type = type;
    }

    @Override
    @ParametersAreNonnullByDefault
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
        BetterMineshaftStructure.Type type = BetterMineshaftStructure.Type.byName(dynamic.get("type").asString(""));
        return new BetterMineshaftFeatureConfig(f, type);
    }
}
