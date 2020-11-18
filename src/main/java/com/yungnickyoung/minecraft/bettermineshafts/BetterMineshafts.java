package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.BMConfiguredStructureFeatures;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructureFeatures;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructureFeaturePieces;
import io.netty.util.internal.ConcurrentSet;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.StructureFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BetterMineshafts implements ModInitializer {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final boolean DEBUG_LOG = true;

    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);

    // TODO - replace hardcoded spawnrates w/ config option
    public static final double SPAWN_RATE = .003;

    @Override
    public void onInitialize() {
        // Initialization & Registration
        BMStructureFeatures.init();
        BMConfiguredStructureFeatures.init();
        BMStructureFeaturePieces.init();

        // Add variants to biomes
        // Red desert variant in badlands biome
        addToBiome("normal_mineshaft_addition",
            context -> biomeHasVanillaMineshaft(context),
//                && !biomeAlreadyHasBetterMineshaft(context)
//                && context.getBiome().getCategory() == Biome.Category.MESA
//                && context.getBiomeKey().getValue().equals(new Identifier("minecraft", "badlands")),
            context -> context.getGenerationSettings().addBuiltInStructure(BMConfiguredStructureFeatures.REDDESERT_MINESHAFT)
        );

        // Mesa variant in all other mesa biomes
        addToBiome("normal_mineshaft_addition",
            context -> biomeHasVanillaMineshaft(context)
                && !biomeAlreadyHasBetterMineshaft(context)
                && context.getBiome().getCategory() == Biome.Category.MESA,
            context -> context.getGenerationSettings().addBuiltInStructure(BMConfiguredStructureFeatures.MESA_MINESHAFT)
        );

        // Remove vanilla mineshafts from biomes we added Better Mineshafts to
        BiomeModifications.create(new Identifier(MOD_ID, "vanilla_mineshaft_removal")).add(
            ModificationPhase.REMOVALS,
            context -> context.getBiome().getGenerationSettings().hasStructureFeature(BMStructureFeatures.MINESHAFT_STRUCTURE),
            context -> context.getGenerationSettings().removeStructure(StructureFeature.MINESHAFT));
    }

    // Helper method to help reduce amount of code we need to write for adding structures to biomes
    private static void addToBiome(String modificationName, Predicate<BiomeSelectionContext> selectorPredicate, Consumer<BiomeModificationContext> biomeAdditionConsumer) {
        BiomeModifications.create(new Identifier(MOD_ID, modificationName)).add(ModificationPhase.ADDITIONS, selectorPredicate, biomeAdditionConsumer);
    }


    private static boolean biomeHasVanillaMineshaft(BiomeSelectionContext context) {
        return context.hasBuiltInStructure(ConfiguredStructureFeatures.MINESHAFT) || context.hasBuiltInStructure(ConfiguredStructureFeatures.MINESHAFT_MESA);
    }

    private static boolean biomeAlreadyHasBetterMineshaft(BiomeSelectionContext context) {
        return BMConfiguredStructureFeatures.CONFIGURED_STRUCTURE_FEATURES
            .stream()
            .anyMatch(context::hasBuiltInStructure);
    }
}
