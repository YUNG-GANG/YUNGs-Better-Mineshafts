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
import net.minecraft.world.gen.feature.Feature;
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
    public static final Structure<BetterMineshaftConfig> BM_STRUCTURE = new BetterMineshaftStructure(BetterMineshaftConfig.field_236541_a_);

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
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Feature.class, ModStructures::registerStructure);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModStructures::commonSetup);
    }

    public static void registerStructure(final RegistryEvent.Register<Feature<?>> event) {
        // Add structure to registry and set its name
        BM_STRUCTURE.setRegistryName(new ResourceLocation("mineshaft"));
        Structure.field_236385_u_.put(BM_STRUCTURE, GenerationStage.Decoration.UNDERGROUND_STRUCTURES);
        Structure.field_236365_a_.put("Mineshaft", BM_STRUCTURE);
        Registry.register(Registry.STRUCTURE_FEATURE, "Mineshaft".toLowerCase(Locale.ROOT), BM_STRUCTURE);

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

                // Determine mineshaft variant based on biome
                if (biome.getCategory() == Biome.Category.MESA) {
                    structureFeature = MESA_MINESHAFT;
                } else if (biome.getCategory() == Biome.Category.JUNGLE) {
                    structureFeature = JUNGLE_MINESHAFT;
                }
                else if (biome.getCategory() == Biome.Category.ICY || biome.getCategory() == Biome.Category.TAIGA) {
                    structureFeature = SNOW_MINESHAFT;
                }
                else if (biome.getCategory() == Biome.Category.DESERT) {
                    structureFeature = DESERT_MINESHAFT;
                }
                else if (biome.getCategory() == Biome.Category.MUSHROOM) {
                    structureFeature = MUSHROOM_MINESHAFT;
                }
                else if (biome.getCategory() == Biome.Category.SAVANNA) {
                    structureFeature = SAVANNA_MINESHAFT;
                } else {
                    structureFeature = NORMAL_MINESHAFT;
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
    private static BetterMineshaftStructure.Type getType(BetterMineshaftStructure.Type type) {
        if (type == BetterMineshaftStructure.Type.RED_DESERT) {
            if (BMConfig.redDesertEnabled) return BetterMineshaftStructure.Type.RED_DESERT;
            if (BMConfig.desertEnabled) return BetterMineshaftStructure.Type.DESERT;
        } else if (type == BetterMineshaftStructure.Type.DESERT) {
            if (BMConfig.desertEnabled) return BetterMineshaftStructure.Type.DESERT;
        } else if (type == BetterMineshaftStructure.Type.MESA) {
            if (BMConfig.mesaEnabled) return BetterMineshaftStructure.Type.MESA;
            if (BMConfig.redDesertEnabled) return BetterMineshaftStructure.Type.RED_DESERT;
        } else if (type == BetterMineshaftStructure.Type.ICE) {
            if (BMConfig.iceEnabled) return BetterMineshaftStructure.Type.ICE;
            if (BMConfig.snowEnabled) return BetterMineshaftStructure.Type.SNOW;
        } else if (type == BetterMineshaftStructure.Type.SNOW) {
            if (BMConfig.snowEnabled) return BetterMineshaftStructure.Type.SNOW;
        } else if (type == BetterMineshaftStructure.Type.JUNGLE) {
            if (BMConfig.jungleEnabled) return BetterMineshaftStructure.Type.JUNGLE;
        } else if (type == BetterMineshaftStructure.Type.SAVANNA) {
            if (BMConfig.savannaEnabled) return BetterMineshaftStructure.Type.SAVANNA;
        } else if (type == BetterMineshaftStructure.Type.MUSHROOM) {
            if (BMConfig.mushroomEnabled) return BetterMineshaftStructure.Type.MUSHROOM;
        }
        return BetterMineshaftStructure.Type.NORMAL;
    }
}
