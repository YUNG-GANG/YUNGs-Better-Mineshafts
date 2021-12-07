package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMultimap;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.mixin.BiomeSelectionContextImplAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.impl.biome.modification.BiomeSelectionContextImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import java.util.HashMap;
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

    public static ConfiguredStructureFeature<?, ?> LUSH_MINSHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
            .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.LUSH));

    /**
     * Map of exception biome keys (typically rare biomes) to mineshaft variant.
     * These are checked before the standardMap.
     */
    public static Map<String, ConfiguredStructureFeature<?, ?>> exceptionMap;

    /**
     * Map of biome category to mineshaft variant.
     */
    public static Map<Biome.BiomeCategory, ConfiguredStructureFeature<?, ?>> normalMap;

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
        Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_lush"), LUSH_MINSHAFT);
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
        exceptionMap.put("lush_caves", BMConfiguredStructureFeatures.LUSH_MINSHAFT);

        // Add all other standard entries
        normalMap.put(Biome.BiomeCategory.MESA, BMConfiguredStructureFeatures.MESA_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.JUNGLE, BMConfiguredStructureFeatures.JUNGLE_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.ICY, BMConfiguredStructureFeatures.SNOW_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.TAIGA, BMConfiguredStructureFeatures.SNOW_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.DESERT, BMConfiguredStructureFeatures.DESERT_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.MUSHROOM, BMConfiguredStructureFeatures.MUSHROOM_MINESHAFT);
        normalMap.put(Biome.BiomeCategory.SAVANNA, BMConfiguredStructureFeatures.SAVANNA_MINESHAFT);

        replaceMineshaftsInBiomes();
    }

    private static void replaceMineshaftsInBiomes() {
        BiomeModifications.create(new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_replacement"))
            .add(ModificationPhase.ADDITIONS,
                BMConfiguredStructureFeatures::biomeHasVanillaMineshaft,
                (selectionContext, modificationContext) -> {
                    // First, remove vanilla mineshaft
                    modificationContext.getGenerationSettings().removeStructure(StructureFeature.MINESHAFT);

                    // Grab biome information for fetching the proper Better Mineshaft configured structure feature
                    ResourceLocation biomeName = selectionContext.getBiomeKey().location();
                    Biome.BiomeCategory biomeCategory = selectionContext.getBiome().getBiomeCategory();

                    // Don't spawn in oceans or beaches.
                    // This is done to minimize the possibility of weird water interactions.
                    if (biomeCategory == Biome.BiomeCategory.OCEAN || biomeCategory == Biome.BiomeCategory.BEACH) {
                        return;
                    }

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

                    if (mineshaft != null) { // Set mineshaft if it's an exceptional entry
                        modificationContext.getGenerationSettings().addBuiltInStructure(mineshaft);
                    } else { // Not an exception --> set variant based on biome category
                        mineshaft = normalMap.getOrDefault(biomeCategory, NORMAL_MINESHAFT);
                        modificationContext.getGenerationSettings().addBuiltInStructure(mineshaft);
                    }
                });
    }

    private static boolean biomeHasVanillaMineshaft(BiomeSelectionContext context) {
        if (context instanceof BiomeSelectionContextImpl) {
            ResourceKey<Biome> biomeKey = context.getBiomeKey();
            RegistryAccess registryAccess = ((BiomeSelectionContextImplAccessor)context).getDynamicRegistries();

            // Since the biome->structure mapping is now stored in the chunk generator configurations, it's no longer
            // trivial to detect if a given biome _could_ spawn a structure. To still support the API, we now do this on a
            // per-chunk-generator level.
            Registry<NoiseGeneratorSettings> chunkGeneratorSettings = registryAccess.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY);

            for (Map.Entry<ResourceKey<NoiseGeneratorSettings>, NoiseGeneratorSettings> entry : chunkGeneratorSettings.entrySet()) {
                StructureSettings structuresConfig = entry.getValue().structureSettings();
                ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> configuredStructureToBiomeMap = structuresConfig.structures(StructureFeature.MINESHAFT);

                for (ConfiguredStructureFeature<?, ?> configuredStructureFeature : configuredStructureToBiomeMap.keySet()) {
                    if (configuredStructureFeature.feature == StructureFeature.MINESHAFT) {
                        ImmutableCollection<ResourceKey<Biome>> biomes = configuredStructureToBiomeMap.get(configuredStructureFeature);
                        boolean anyMatch = biomes.stream()
                                .map(ResourceKey::location)
                                .anyMatch(keyString -> keyString.equals(biomeKey.location()));
                        if (anyMatch) return true;
                    }
                }
            }
        }
        return false;
    }
}
