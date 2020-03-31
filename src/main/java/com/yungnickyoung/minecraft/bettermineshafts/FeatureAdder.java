package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.lang.reflect.Field;
import java.util.Map;

public class FeatureAdder {
    public static void addBetterMineshafts(Biome biome) {
        removeVanillaMineshaft(biome);

        // TODO - replace hardcoded spawnrates w/ config option
        BetterMineshaftFeatureConfig config = new BetterMineshaftFeatureConfig(0.004D, BetterMineshaftFeature.Type.NORMAL);

        biome.addStructureFeature(
            BetterMineshafts.BETTER_MINESHAFT_FEATURE.configure(config)
        );

        biome.addFeature(
            GenerationStep.Feature.UNDERGROUND_STRUCTURES,
            BetterMineshafts.BETTER_MINESHAFT_FEATURE.configure(config)
                .createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT))
        );
    }

    @SuppressWarnings("unchecked")
    private static void removeVanillaMineshaft(Biome biome) {
        /// Use reflection to access biome's structure feature list and remove vanilla mineshaft from it
        try {
            Field field = biome.getClass().getSuperclass().getDeclaredField("structureFeatures");
            field.setAccessible(true);
            Map<StructureFeature<?>, FeatureConfig> structureFeatures =
                (Map<StructureFeature<?>, FeatureConfig>)field.get(biome);
            structureFeatures.keySet().removeIf(feature -> feature == BetterMineshafts.VANILLA_MINESHAFT_FEATURE);
            field.set(biome, structureFeatures);
        } catch (Exception e) {
            BetterMineshafts.LOGGER.warn(
                String.format(
                    "Encountered error attempting to remove vanilla mineshaft from biome %s: %s",
                    biome.getName().asString(),
                    e
                )
            );
        }
    }
}
