package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.feature.*;

public class BMStructureFeatures {
    public static StructureFeature<BetterMineshaftFeatureConfig> MINESHAFT_STRUCTURE = new BetterMineshaftStructure(BetterMineshaftFeatureConfig.CODEC);

    /**
     * Creates and registers the Better Mineshaft structure.
     */
    public static void init() {
        FabricStructureBuilder.create(new Identifier(BetterMineshafts.MOD_ID, "bettermineshaft"), MINESHAFT_STRUCTURE)
            .step(GenerationStep.Feature.UNDERGROUND_STRUCTURES)
            .defaultConfig(new StructureConfig(1, 0, 593751784))
            .superflatFeature(MINESHAFT_STRUCTURE.configure(BetterMineshaftFeatureConfig.DEFAULT))
            .register();
    }
}
