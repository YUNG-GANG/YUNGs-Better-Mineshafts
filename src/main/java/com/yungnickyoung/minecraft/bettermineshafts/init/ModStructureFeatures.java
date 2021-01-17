package com.yungnickyoung.minecraft.bettermineshafts.init;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModStructureFeatures {
    //  The mineshaft StructureFeature
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> MINESHAFT_STRUCTURE_FEATURE = ModStructures.MINESHAFT_STRUCTURE.get()
        .func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG);
}
