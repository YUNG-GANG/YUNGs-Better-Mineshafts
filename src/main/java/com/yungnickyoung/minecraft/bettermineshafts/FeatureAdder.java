package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class FeatureAdder {
    public static void addBetterMineshafts(Biome biome) {
        // TODO - replace hardcoded spawnrates w/ config option
        BetterMineshaftFeatureConfig config = new BetterMineshaftFeatureConfig(0.004D, BetterMineshaftFeature.Type.NORMAL);

        if (biome.getCategory() == Biome.Category.MESA)
            config = new BetterMineshaftFeatureConfig(.004D, BetterMineshaftFeature.Type.MESA);

        biome.addStructureFeature(
            BetterMineshafts.BETTER_MINESHAFT_FEATURE.configure(config)
        );

        try {
            List<ConfiguredFeature<?, ?>> list = biome.getFeaturesForStep(GenerationStep.Feature.UNDERGROUND_STRUCTURES);
            for (int i = 0; i < list.size(); i++) {
                ConfiguredFeature<?, ?> feature = list.get(i);
                if (((DecoratedFeatureConfig) (feature.config)).feature.feature instanceof MineshaftFeature) {
                    list.set(i, BetterMineshafts.BETTER_MINESHAFT_FEATURE.configure(config).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
                }
            }
        } catch (Exception e) {
            BetterMineshafts.LOGGER.warn(
                String.format(
                    "Encountered error attempting to replace vanilla mineshaft from biome %s: %s",
                    biome.getName().asString(),
                    e
                )
            );
        }
    }
}
