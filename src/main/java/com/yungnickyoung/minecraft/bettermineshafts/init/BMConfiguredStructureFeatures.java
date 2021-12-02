package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.mixin.StructureFeaturesAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BMConfiguredStructureFeatures {
    public static ConfiguredStructureFeature<?, ?> NORMAL_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.NORMAL));

    public static ConfiguredStructureFeature<?, ?> MESA_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.MESA));

    public static ConfiguredStructureFeature<?, ?> JUNGLE_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.JUNGLE));

    public static ConfiguredStructureFeature<?, ?> SNOW_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.SNOW));

    public static ConfiguredStructureFeature<?, ?> MUSHROOM_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.MUSHROOM));

    public static ConfiguredStructureFeature<?, ?> SAVANNA_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.SAVANNA));

    public static ConfiguredStructureFeature<?, ?> DESERT_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.DESERT));

    public static ConfiguredStructureFeature<?, ?> REDDESERT_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.RED_DESERT));

    public static ConfiguredStructureFeature<?, ?> ICE_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.ICE));

    // List of all structure features to make updating spawn rate easy
    public static final List<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURE_FEATURES =
        Arrays.asList(
            NORMAL_MINESHAFT, MESA_MINESHAFT, JUNGLE_MINESHAFT,
            SNOW_MINESHAFT, MUSHROOM_MINESHAFT, SAVANNA_MINESHAFT,
            DESERT_MINESHAFT, REDDESERT_MINESHAFT, ICE_MINESHAFT
        );
    
    // Map of exception biome keys (typically rare biomes) to mineshaft variant.
    // These are checked before the standardMap.
    public static Map<String, ConfiguredStructureFeature<?, ?>> exceptionMap;

    // Map of biome category to mineshaft variant.
    public  static Map<Biome.BiomeCategory, ConfiguredStructureFeature<?, ?>> normalMap;

    /**
     * Registers configured structure features.
     */
    public static void init() {
        registerConfiguredStructures();
        addConfiguredStructuresToBiomes();
    }

    /**
     * Registers all the configured structure features for the mineshaft variants.
     */
    private static void registerConfiguredStructures() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_normal"), NORMAL_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_mesa"), MESA_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_jungle"), JUNGLE_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_mushroom"), MUSHROOM_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_savanna"), SAVANNA_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_desert"), DESERT_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_reddesert"), REDDESERT_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_ice"), ICE_MINESHAFT);
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_snow"), SNOW_MINESHAFT);
    }

    /**
     * Adds all the configured structure features to their proper biomes.
     */
    private static void addConfiguredStructuresToBiomes() {
        exceptionMap = new HashMap<>();
        normalMap = new HashMap<>();

        // Add exceptional biome entries
        exceptionMap.put("ice", BMConfiguredStructureFeatures.ICE_MINESHAFT);
        exceptionMap.put("icy", BMConfiguredStructureFeatures.ICE_MINESHAFT);
        exceptionMap.put("desert_lakes", BMConfiguredStructureFeatures.REDDESERT_MINESHAFT);

        // Add all other standard entries
        normalMap.put(Biome.BiomeCategory.MESA, BMConfiguredStructureFeatures.MESA_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.JUNGLE, BMConfiguredStructureFeatures.JUNGLE_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.ICY, BMConfiguredStructureFeatures.SNOW_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.TAIGA, BMConfiguredStructureFeatures.SNOW_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.DESERT, BMConfiguredStructureFeatures.DESERT_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.MUSHROOM, BMConfiguredStructureFeatures.MUSHROOM_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.SAVANNA, BMConfiguredStructureFeatures.SAVANNA_MINESHAFT);

        BiomeModifications.create(new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_replacement"))
            .add(ModificationPhase.ADDITIONS,
                selectionContext -> biomeHasVanillaMineshaft(selectionContext),
                (selectionContext, modificationContext) -> {
                    // First, remove vanilla mineshaft
                    modificationContext.getGenerationSettings().removeStructure(StructureFeature.MINESHAFT);

                    // Grab biome information for fetching the proper Better Mineshaft configured structure feature
                    ResourceLocation biomeName = selectionContext.getBiomeKey().location();
                    Biome.BiomeCategory biomeCategory = selectionContext.getBiome().getBiomeCategory();

                    // The "minecraft:badlands" biome requires an exact match, and is therefore a special exception
                    if (biomeName.toString().equals("minecraft:badlands")) {
                        modificationContext.getGenerationSettings().addBuiltInStructure(BMConfiguredStructureFeatures.REDDESERT_MINESHAFT);
                        return;
                    }

                    ConfiguredStructureFeature<?, ?> mineshaft = null;

                    // Check if this biome is an exception
                    for (String biomePhrase : exceptionMap.keySet()) {
                        if (biomeName.toString().contains(biomePhrase)) {
                            mineshaft = exceptionMap.get(biomePhrase);
                            break;
                        }
                    }

                    if (mineshaft != null) { // Set mineshaft if it's an exception
                        modificationContext.getGenerationSettings().addBuiltInStructure(mineshaft);
                    } else { // Not an exception --> set variant based on biome category
                        mineshaft = normalMap.getOrDefault(biomeCategory, null);
                        if (mineshaft != null) {
                            modificationContext.getGenerationSettings().addBuiltInStructure(mineshaft);
                        } else {
                            modificationContext.getGenerationSettings().addBuiltInStructure(BMConfiguredStructureFeatures.NORMAL_MINESHAFT);
                        }
                    }
                });
    }

    private static boolean biomeHasVanillaMineshaft(BiomeSelectionContext context) {
        return context.hasBuiltInStructure(StructureFeaturesAccessor.getMINESHAFT()) || context.hasBuiltInStructure(StructureFeaturesAccessor.getMINESHAFT_MESA());
    }
}
