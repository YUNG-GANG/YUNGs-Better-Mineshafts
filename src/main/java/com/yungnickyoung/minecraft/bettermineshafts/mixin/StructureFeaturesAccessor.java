package com.yungnickyoung.minecraft.bettermineshafts.mixin;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureFeatures.class)
public interface StructureFeaturesAccessor {
    @Accessor
    static ConfiguredStructureFeature<MineshaftConfiguration, ? extends StructureFeature<MineshaftConfiguration>> getMINESHAFT() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static ConfiguredStructureFeature<MineshaftConfiguration, ? extends StructureFeature<MineshaftConfiguration>> getMINESHAFT_MESA() {
        throw new UnsupportedOperationException();
    }
}
