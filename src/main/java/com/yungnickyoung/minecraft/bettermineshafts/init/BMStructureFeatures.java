package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;

public class BMStructureFeatures {
    public static StructureFeature<BetterMineshaftFeatureConfig> MINESHAFT_STRUCTURE = new BetterMineshaftStructureFeature(BetterMineshaftFeatureConfig.CODEC);

    /**
     * Creates and registers the Better Mineshaft structure.
     */
    public static void init() {
        FabricStructureBuilder.create(new ResourceLocation(BetterMineshafts.MOD_ID, "bettermineshaft"), MINESHAFT_STRUCTURE)
            .step(GenerationStep.Decoration.UNDERGROUND_STRUCTURES)
            .defaultConfig(new StructureFeatureConfiguration(1, 0, 593751784))
            .register();
    }
}
