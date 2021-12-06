package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.mixin.StructureFeaturesAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.mixin.StructureSettingsAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.StructureSettings;
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

    public static ConfiguredStructureFeature<?, ?> LUSH_MINSHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
            .configured(new BetterMineshaftFeatureConfig(BetterMineshafts.CONFIG.mineshaftSpawnRate, BetterMineshaftStructureFeature.Type.LUSH));

    // List of all structure features to make updating spawn rate easy
    public static final List<ConfiguredStructureFeature<?, ?>> CONFIGURED_STRUCTURE_FEATURES =
        Arrays.asList(
            NORMAL_MINESHAFT, MESA_MINESHAFT, JUNGLE_MINESHAFT,
            SNOW_MINESHAFT, MUSHROOM_MINESHAFT, SAVANNA_MINESHAFT,
            DESERT_MINESHAFT, REDDESERT_MINESHAFT, ICE_MINESHAFT,
            LUSH_MINSHAFT
        );

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

        /* Fabric API is currently broken for structures. Workaround is done below instead of using BiomeModifications. */
//        replaceMineshaftsInBiomes();

        /*
         * NOTE: BiomeModifications from Fabric API does not work in 1.18 currently.
         * Instead, we will use the below to add our structure to overworld biomes.
         * Remember, this is temporary until Fabric API finds a better solution for adding structures to biomes.
         * Credit to TelepathicGrunt for this.
         */
        ResourceLocation runAfterFabricAPIPhase = new ResourceLocation(BetterMineshafts.MOD_ID, "run_after_fabric_api");
        ServerWorldEvents.LOAD.addPhaseOrdering(Event.DEFAULT_PHASE, runAfterFabricAPIPhase);

        ServerWorldEvents.LOAD.register(runAfterFabricAPIPhase, (MinecraftServer minecraftServer, ServerLevel serverLevel) -> {
            StructureSettings worldStructureConfig = serverLevel.getChunkSource().getGenerator().getSettings();

            // Make a copy of the structure-biome map, excluding vanilla mineshafts so they don't spawn.
            ImmutableMap.Builder<StructureFeature<?>, ImmutableMultimap<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>>> tempStructureToMultiMap = ImmutableMap.builder();
            ((StructureSettingsAccessor) worldStructureConfig).getConfiguredStructures().entrySet().stream()
                    .filter(entry -> entry.getKey() != StructureFeature.MINESHAFT)
                    .forEach(tempStructureToMultiMap::put);

            // Create the multimap of Configured Structures to biomes we will need.
            ImmutableMultimap.Builder<ConfiguredStructureFeature<?, ?>, ResourceKey<Biome>> tempConfiguredStructureBiomeMultiMap = ImmutableMultimap.builder();

            // Add the registrykey of all biomes that this Configured Structure can spawn in.
            for (Map.Entry<ResourceKey<Biome>, Biome> biomeEntry : minecraftServer.registryAccess().ownedRegistry(Registry.BIOME_REGISTRY).get().entrySet()) {
                Biome.BiomeCategory biomeCategory = biomeEntry.getValue().getBiomeCategory();
                ResourceLocation biomeName = biomeEntry.getKey().location();

                // Don't spawn in blacklisted biomes
                if (biomeCategory == Biome.BiomeCategory.OCEAN || biomeCategory == Biome.BiomeCategory.BEACH) {
                    continue;
                }

                // Only spawn in biomes with normal mineshafts
//                if (!((StructureSettingsAccessor) worldStructureConfig).getConfiguredStructures().get(StructureFeature.MINESHAFT).containsValue(biomeEntry.getKey())) {
//                    continue;
//                }

                // The "minecraft:badlands" biome requires an exact match, and is therefore a special exception
                if (biomeName.toString().equals("minecraft:badlands")) {
                    tempConfiguredStructureBiomeMultiMap.put(REDDESERT_MINESHAFT, biomeEntry.getKey());
                    continue;
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
                    tempConfiguredStructureBiomeMultiMap.put(mineshaft, biomeEntry.getKey());
                } else { // Not an exception --> set variant based on biome category
                    mineshaft = normalMap.getOrDefault(biomeCategory, null);
                    if (mineshaft != null) {
                        tempConfiguredStructureBiomeMultiMap.put(mineshaft, biomeEntry.getKey());
                    } else {
                        tempConfiguredStructureBiomeMultiMap.put(NORMAL_MINESHAFT, biomeEntry.getKey());
                    }
                }
            }

            // Alternative way to add our structures to a fixed set of biomes by creating a set of biome registry keys.
            // To create a custom registry key that points to your own biome, do this:
            // RegistryKey.of(Registry.BIOME_KEY, new Identifier("modid", "custom_biome"))
//            ImmutableSet<RegistryKey<Biome>> overworldBiomes = ImmutableSet.<RegistryKey<Biome>>builder()
//                    .add(BiomeKeys.FOREST)
//                    .add(BiomeKeys.MEADOW)
//                    .add(BiomeKeys.PLAINS)
//                    .add(BiomeKeys.SAVANNA)
//                    .add(BiomeKeys.SNOWY_PLAINS)
//                    .add(BiomeKeys.SWAMP)
//                    .add(BiomeKeys.SUNFLOWER_PLAINS)
//                    .add(BiomeKeys.TAIGA)
//                    .build();
//            overworldBiomes.forEach(biomeKey -> tempConfiguredStructureBiomeMultiMap.put(STConfiguredStructures.CONFIGURED_RUN_DOWN_HOUSE, biomeKey));

            // Add our structure and its associated configured structures + containing biomes to the settings
            tempStructureToMultiMap.put(BMStructureFeatures.MINESHAFT_STRUCTURE, tempConfiguredStructureBiomeMultiMap.build());

            // Save our updates
            ((StructureSettingsAccessor) worldStructureConfig).setConfiguredStructures(tempStructureToMultiMap.build());


            //////////// DIMENSION BASED STRUCTURE SPAWNING (OPTIONAL) ////////////
//            // Controls the dimension blacklisting and/or whitelisting
//            // If the spacing or our structure is not added for a dimension, the structure doesn't spawn in that dimension.
//            // Note: due to a quirk with how Noise Settings are shared between dimensions, you need this mixin to make a
//            // deep copy of the noise setting per dimension for your dimension whitelisting/blacklisting to work properly:
//            // https://github.com/TelepathicGrunt/RepurposedStructures-Fabric/blob/1.18/src/main/java/com/telepathicgrunt/repurposedstructures/mixin/world/ChunkGeneratorMixin.java
//
//            // Need temp map as some mods use custom chunk generators with immutable maps in themselves.
//            Map<StructureFeature<?>, StructureConfig> tempMap = new HashMap<>(worldStructureConfig.getStructures());
//
//            // Make absolutely sure modded dimension can or cannot spawn our structures.
//            // New dimensions under the minecraft namespace will still get it (datapacks might do this)
//            if(serverWorld.getRegistryKey().equals(World.OVERWORLD)) {
//                tempMap.put(STStructures.RUN_DOWN_HOUSE, FabricStructureImpl.STRUCTURE_TO_CONFIG_MAP.get(STStructures.RUN_DOWN_HOUSE));
//            }
//            else {
//                tempMap.remove(STStructures.RUN_DOWN_HOUSE);
//            }
//
//            // Set the new modified map of structure spacing to the dimension's chunkgenerator.
//            ((StructuresConfigAccessor)worldStructureConfig).setStructures(tempMap);

        });
    }

    private static void replaceMineshaftsInBiomes() {
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
