package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.BetterMineshaftFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class StructureFeatureModule {
    public static StructureFeature<BetterMineshaftFeatureConfiguration> BETTER_MINESHAFT = new BetterMineshaftStructureFeature();
}
