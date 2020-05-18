package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;

import java.util.List;

public class BMStructureFeature {

    private static StructureFeature<?> VANILLA_MINESHAFT_FEATURE;
    public static StructureFeature<BetterMineshaftFeatureConfig> BETTER_MINESHAFT_FEATURE;

    public static void init() {
        // Register feature and structure
        BETTER_MINESHAFT_FEATURE = Registry.register(
            Registry.FEATURE,
            new Identifier(BetterMineshafts.MOD_ID, "mineshaft"),
            new BetterMineshaftFeature(BetterMineshaftFeatureConfig::deserialize)
        );
        Registry.register(
            Registry.STRUCTURE_FEATURE,
            new Identifier(BetterMineshafts.MOD_ID, "mineshaft"),
            BETTER_MINESHAFT_FEATURE
        );

        // Get vanilla mineshaft, used to check which biomes should be modified
        VANILLA_MINESHAFT_FEATURE = Registry.STRUCTURE_FEATURE.get(new Identifier("minecraft:mineshaft"));

        // Add Better Mineshaft to applicable biomes, replacing vanilla mineshafts
        Registry.BIOME.forEach(biome -> {
            if (biome.hasStructureFeature(VANILLA_MINESHAFT_FEATURE)) {
                addBetterMineshafts(biome);
            }
        });
    }

    /**
     * Adds Better Mineshaft to the given biome, and removes vanilla mineshaft from its feature list.
     */
    private static void addBetterMineshafts(Biome biome) {
        // TODO - replace hardcoded spawnrates w/ config option
        BetterMineshaftFeatureConfig config = new BetterMineshaftFeatureConfig(0.003D, BetterMineshaftFeature.Type.NORMAL);

        // TODO - add biome variant check here
        if (biome.getCategory() == Biome.Category.MESA)
            config = new BetterMineshaftFeatureConfig(.003D, BetterMineshaftFeature.Type.MESA);

        removeVanillaMineshafts(biome);

        // Add structure feature (allows spawning of new mineshaft in biome)
        biome.addStructureFeature(BETTER_MINESHAFT_FEATURE.configure(config));

        // Add feature (allows generation of structure in biome)
        biome.addFeature(GenerationStep.Feature.UNDERGROUND_STRUCTURES, BETTER_MINESHAFT_FEATURE.configure(config));
    }

    /**
     * Removes vanilla mineshaft from a biome's feature list.
     */
    private static void removeVanillaMineshafts(Biome biome) {
        try {
            List<ConfiguredFeature<?, ?>> list = biome.getFeaturesForStep(GenerationStep.Feature.UNDERGROUND_STRUCTURES);
            for (int i = 0; i < list.size(); i++) {
                ConfiguredFeature<?, ?> feature = list.get(i);
                if (((DecoratedFeatureConfig) (feature.config)).feature.feature instanceof MineshaftFeature) {
                    list.remove(i);
//                    list.set(i, BetterMineshafts.BETTER_MINESHAFT_FEATURE.configure(config).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
//                    list.add(BetterMineshafts.BETTER_MINESHAFT_FEATURE.configure(config).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
                    break;
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
