package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.google.common.collect.ImmutableMap;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ModStructures {
    // Structure
    public static Structure<BetterMineshaftConfig> BM_STRUCTURE = new BetterMineshaftStructure(BetterMineshaftConfig.field_236541_a_);

    // Structure features
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> NORMAL_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.NORMAL));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> MESA_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.MESA));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> JUNGLE_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.JUNGLE));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> SNOW_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.SNOW));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> MUSHROOM_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.MUSHROOM));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> SAVANNA_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.SAVANNA));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> DESERT_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.DESERT));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> REDDESERT_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.RED_DESERT));

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> ICE_MINESHAFT =
        BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.ICE));

    // List of all structure features to make updating spawn rate easy
    public static final List<StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>>> STRUCTURE_FEATURE_LIST = Arrays.asList(
        NORMAL_MINESHAFT, MESA_MINESHAFT, JUNGLE_MINESHAFT,
        SNOW_MINESHAFT, MUSHROOM_MINESHAFT, SAVANNA_MINESHAFT,
        DESERT_MINESHAFT, REDDESERT_MINESHAFT, ICE_MINESHAFT
    );

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Structure.class, ModStructures::registerStructure);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModStructures::commonSetup);
    }

    public static void registerStructure(final RegistryEvent.Register<Structure<?>> event) {
        // Add structure to registry and set its name
        BM_STRUCTURE.setRegistryName(new ResourceLocation("mineshaft"));
        Structure.field_236385_u_.put(BM_STRUCTURE, GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
        Structure.field_236365_a_.put("Mineshaft", BM_STRUCTURE);
        event.getRegistry().register(BM_STRUCTURE);

        // Add structure and placement info to settings
        FlatGenerationSettings.STRUCTURES.put(BM_STRUCTURE, BM_STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.NORMAL)));
        DimensionStructuresSettings.field_236191_b_ = // Default structures
            ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                .putAll(DimensionStructuresSettings.field_236191_b_)
                .put(BM_STRUCTURE, new StructureSeparationSettings(1, 0, 0))
                .build();
        DimensionSettings.field_242740_q.getStructures().field_236193_d_.put(BM_STRUCTURE, new StructureSeparationSettings(1, 0, 0));

        // Register structure features
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft".toLowerCase(Locale.ROOT), NORMAL_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_mesa".toLowerCase(Locale.ROOT), MESA_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_jungle".toLowerCase(Locale.ROOT), JUNGLE_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_mushroom".toLowerCase(Locale.ROOT), MUSHROOM_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_savanna".toLowerCase(Locale.ROOT), SAVANNA_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_desert".toLowerCase(Locale.ROOT), DESERT_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_reddesert".toLowerCase(Locale.ROOT), REDDESERT_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_ice".toLowerCase(Locale.ROOT), ICE_MINESHAFT);
        Registry.register((MutableRegistry<StructureFeature<?, ?>>) WorldGenRegistries.field_243654_f, "Mineshaft_snow".toLowerCase(Locale.ROOT), SNOW_MINESHAFT);
    }

    /**
     * Adds better mineshafts structure to all applicable biomes.
     */
    public static void commonSetup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ModStructurePieces.init();

            // Set config based on biome
            for (Biome biome : WorldGenRegistries.field_243657_i) {
                // Only operate on biomes that have mineshafts
                if (!biome.func_242440_e().func_242493_a(Structure.field_236367_c_)) {
                    continue;
                }

                // Remove vanilla mineshaft
                // From biome
                biome.field_242421_g.get(GenerationStage.Decoration.UNDERGROUND_STRUCTURES.ordinal()).remove(Structure.field_236367_c_);

                // From biome generation settings
                biome.func_242440_e().field_242485_g = new ArrayList<>(biome.func_242440_e().func_242487_a());
                biome.func_242440_e().field_242485_g.removeIf(supplier -> supplier.get().field_236268_b_ == Structure.field_236367_c_);

                // No mineshafts in oceans or beaches
                if (biome.getCategory() == Biome.Category.OCEAN || biome.getCategory() == Biome.Category.BEACH) {
                    continue;
                }

                StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> structureFeature;
                String biomeName = biome.toString();
                try {
                    biomeName = biomeName.split(":")[1];
                } catch (Exception ignored) {}

                // Determine mineshaft variant based on biome
                if (biome.getCategory() == Biome.Category.MESA) {
                    if (biomeName.equalsIgnoreCase("badlands")) {
                        structureFeature = getVariant(REDDESERT_MINESHAFT);
                    } else {
                        structureFeature = getVariant(MESA_MINESHAFT);
                    }
                } else if (biome.getCategory() == Biome.Category.JUNGLE) {
                    structureFeature = getVariant(JUNGLE_MINESHAFT);
                }
                else if (biome.getCategory() == Biome.Category.ICY || biome.getCategory() == Biome.Category.TAIGA) {
                    if (biomeName.equalsIgnoreCase("ice_spikes")) {
                        structureFeature = getVariant(ICE_MINESHAFT);
                    } else {
                        structureFeature = getVariant(SNOW_MINESHAFT);
                    }
                }
                else if (biome.getCategory() == Biome.Category.DESERT) {
                    if (biomeName.equalsIgnoreCase("desert_lakes")) {
                        structureFeature = getVariant(REDDESERT_MINESHAFT);
                    } else {
                        structureFeature = getVariant(DESERT_MINESHAFT);
                    }                }
                else if (biome.getCategory() == Biome.Category.MUSHROOM) {
                    structureFeature = getVariant(MUSHROOM_MINESHAFT);
                }
                else if (biome.getCategory() == Biome.Category.SAVANNA) {
                    structureFeature = getVariant(SAVANNA_MINESHAFT);
                } else {
                    structureFeature = getVariant(NORMAL_MINESHAFT);
                }

                // Add mineshaft to biome & biome generation settings
                biome.field_242421_g.get(GenerationStage.Decoration.UNDERGROUND_STRUCTURES.ordinal()).add(BM_STRUCTURE);
                biome.func_242440_e().func_242487_a().add(() -> structureFeature);
            }
        });
    }

    /**
     * Returns the given type if its mineshaft variant is enabled.
     * Otherwise returns a backup type that is enabled.
     */
    private static StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> getVariant(
        StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> variant
    ) {
        if (variant == REDDESERT_MINESHAFT) {
            if (BMConfig.redDesertEnabled) return REDDESERT_MINESHAFT;
            if (BMConfig.desertEnabled) return DESERT_MINESHAFT;
        } else if (variant == DESERT_MINESHAFT) {
            if (BMConfig.desertEnabled) return DESERT_MINESHAFT;
        } else if (variant == MESA_MINESHAFT) {
            if (BMConfig.mesaEnabled) return MESA_MINESHAFT;
            if (BMConfig.redDesertEnabled) return REDDESERT_MINESHAFT;
        } else if (variant == ICE_MINESHAFT) {
            if (BMConfig.iceEnabled) return ICE_MINESHAFT;
            if (BMConfig.snowEnabled) return SNOW_MINESHAFT;
        } else if (variant == SNOW_MINESHAFT) {
            if (BMConfig.snowEnabled) return SNOW_MINESHAFT;
        } else if (variant == JUNGLE_MINESHAFT) {
            if (BMConfig.jungleEnabled) return JUNGLE_MINESHAFT;
        } else if (variant == SAVANNA_MINESHAFT) {
            if (BMConfig.savannaEnabled) return SAVANNA_MINESHAFT;
        } else if (variant == MUSHROOM_MINESHAFT) {
            if (BMConfig.mushroomEnabled) return MUSHROOM_MINESHAFT;
        }
        return NORMAL_MINESHAFT;
    }
}
