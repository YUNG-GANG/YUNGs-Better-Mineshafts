package com.yungnickyoung.minecraft.bettermineshafts.init;

import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BMModConfiguredStructureFeatures {
    public static ConfiguredStructureFeature<?, ?> CONFIGURED_MINESHAFT = BMModStructureFeatures.BETTER_MINESHAFT.get()
        .configured(FeatureConfiguration.NONE);
}
