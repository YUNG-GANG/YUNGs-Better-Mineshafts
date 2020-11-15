package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.google.common.collect.ImmutableMap;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Supplier;

public class ModStructures {
    // Deferred registry for automatic registration
    public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterMineshafts.MOD_ID);

    // The mineshaft Structure
    public static final RegistryObject<Structure<BetterMineshaftConfig>> MINESHAFT_STRUCTURE = DEFERRED_REGISTRY_STRUCTURE
        .register("mineshaft", () -> new BetterMineshaftStructure(BetterMineshaftConfig.CODEC));

    /**
     * Initializes deferred registry and adds event listeners.
     */
    public static void init() {
        DEFERRED_REGISTRY_STRUCTURE.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModStructures::commonSetup);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, ModStructures::onBiomeLoad);
        MinecraftForge.EVENT_BUS.addListener(ModStructures::addDimensionalSpacing);
    }

    /**
     * Completes all work for setting up the better mineshaft structure and structure features.
     */
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Add mineshaft to the structure map
            Structure.field_236365_a_.put("Better Mineshaft".toLowerCase(Locale.ROOT), MINESHAFT_STRUCTURE.get());

            // Add structure + spacing settings to default dimension structures.
            // Note that we make a similar change in the WorldEvent.Load handler
            // as a safety for custom dimension support.
            DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                    .putAll(DimensionStructuresSettings.field_236191_b_)
                    .put(MINESHAFT_STRUCTURE.get(), new StructureSeparationSettings(1, 0, 593751784))
                    .build();

            // Register configured structure features
            Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_normal"), ModStructureFeatures.NORMAL_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_mesa"), ModStructureFeatures.MESA_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_jungle"), ModStructureFeatures.JUNGLE_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_mushroom"), ModStructureFeatures.MUSHROOM_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_savanna"), ModStructureFeatures.SAVANNA_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_desert"), ModStructureFeatures.DESERT_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_reddesert"), ModStructureFeatures.REDDESERT_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_ice"), ModStructureFeatures.ICE_MINESHAFT);
            Registry.register(registry, new ResourceLocation(BetterMineshafts.MOD_ID, "mineshaft_snow"), ModStructureFeatures.SNOW_MINESHAFT);

            // Add structure to this to prevent any issues if other mods' custom ChunkGenerators use FlatGenerationSettings.STRUCTURES.
            FlatGenerationSettings.STRUCTURES.put(ModStructures.MINESHAFT_STRUCTURE.get(), ModStructureFeatures.NORMAL_MINESHAFT);

            // Register pieces
            ModStructurePieces.init();
        });
    }

    /**
     * We must manually add the separation settings for our structure to spawn.
     */
    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();

            // Prevent spawning in superflat world
            if (
                serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator
                && serverWorld.getDimensionKey().equals(World.OVERWORLD)
            ) {
                return;
            }

            // We use a temp map because some mods handle immutable maps.
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            tempMap.put(ModStructures.MINESHAFT_STRUCTURE.get(), DimensionStructuresSettings.field_236191_b_.get(ModStructures.MINESHAFT_STRUCTURE.get()));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }

    /**
     * Adds the appropriate structure feature to each biome as it loads in.
     */
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        // Only operate on biomes that have mineshafts
        boolean found = false;
        for (Supplier<StructureFeature<?, ?>> supplier : event.getGeneration().getStructures()) {
            if (supplier.get().field_236268_b_ == Structure.field_236367_c_) {
                found = true;
                break;
            }
        }

        if (!found) return;

        // Remove vanilla mineshaft from biome generation settings.
        // This will prevent them from spawning, although the /locate entry will still exist.
        // I couldn't figure out how to remove that for now.
        event.getGeneration().getStructures().removeIf(supplier -> supplier.get().field_236268_b_ == Structure.field_236367_c_);

        // Determine mineshaft variant to add based on biome
        StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> structureFeature;
        String biomeName = event.getName().toString();
        try {
            biomeName = biomeName.split(":")[1];
        } catch (Exception ignored) {}

        // Determine mineshaft variant based on biome
        if (event.getCategory() == Biome.Category.MESA) {
            if (biomeName.equalsIgnoreCase("badlands")) {
                structureFeature = getVariant(ModStructureFeatures.REDDESERT_MINESHAFT);
            } else {
                structureFeature = getVariant(ModStructureFeatures.MESA_MINESHAFT);
            }
        } else if (event.getCategory() == Biome.Category.JUNGLE) {
            structureFeature = getVariant(ModStructureFeatures.JUNGLE_MINESHAFT);
        }
        else if (event.getCategory() == Biome.Category.ICY || event.getCategory() == Biome.Category.TAIGA) {
            if (biomeName.contains("ice")) {
                structureFeature = getVariant(ModStructureFeatures.ICE_MINESHAFT);
            } else {
                structureFeature = getVariant(ModStructureFeatures.SNOW_MINESHAFT);
            }
        }
        else if (event.getCategory() == Biome.Category.DESERT) {
            if (biomeName.equalsIgnoreCase("desert_lakes")) {
                structureFeature = getVariant(ModStructureFeatures.REDDESERT_MINESHAFT);
            } else {
                structureFeature = getVariant(ModStructureFeatures.DESERT_MINESHAFT);
            }                }
        else if (event.getCategory() == Biome.Category.MUSHROOM) {
            structureFeature = getVariant(ModStructureFeatures.MUSHROOM_MINESHAFT);
        }
        else if (event.getCategory() == Biome.Category.SAVANNA) {
            structureFeature = getVariant(ModStructureFeatures.SAVANNA_MINESHAFT);
        } else {
            structureFeature = getVariant(ModStructureFeatures.NORMAL_MINESHAFT);
        }

        // Add mineshaft to biome generation settings
        event.getGeneration().getStructures().add(() -> structureFeature);
    }

    /**
     * Returns the given type if its mineshaft variant is enabled.
     * Otherwise returns a backup type that is enabled.
     */
    private static StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> getVariant(
        StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> variant
    ) {
        if (variant == ModStructureFeatures.REDDESERT_MINESHAFT) {
            if (BMConfig.redDesertEnabled) return ModStructureFeatures.REDDESERT_MINESHAFT;
            if (BMConfig.desertEnabled) return ModStructureFeatures.DESERT_MINESHAFT;
        } else if (variant == ModStructureFeatures.DESERT_MINESHAFT) {
            if (BMConfig.desertEnabled) return ModStructureFeatures.DESERT_MINESHAFT;
        } else if (variant == ModStructureFeatures.MESA_MINESHAFT) {
            if (BMConfig.mesaEnabled) return ModStructureFeatures.MESA_MINESHAFT;
            if (BMConfig.redDesertEnabled) return ModStructureFeatures.REDDESERT_MINESHAFT;
        } else if (variant == ModStructureFeatures.ICE_MINESHAFT) {
            if (BMConfig.iceEnabled) return ModStructureFeatures.ICE_MINESHAFT;
            if (BMConfig.snowEnabled) return ModStructureFeatures.SNOW_MINESHAFT;
        } else if (variant == ModStructureFeatures.SNOW_MINESHAFT) {
            if (BMConfig.snowEnabled) return ModStructureFeatures.SNOW_MINESHAFT;
        } else if (variant == ModStructureFeatures.JUNGLE_MINESHAFT) {
            if (BMConfig.jungleEnabled) return ModStructureFeatures.JUNGLE_MINESHAFT;
        } else if (variant == ModStructureFeatures.SAVANNA_MINESHAFT) {
            if (BMConfig.savannaEnabled) return ModStructureFeatures.SAVANNA_MINESHAFT;
        } else if (variant == ModStructureFeatures.MUSHROOM_MINESHAFT) {
            if (BMConfig.mushroomEnabled) return ModStructureFeatures.MUSHROOM_MINESHAFT;
        }
        return ModStructureFeatures.NORMAL_MINESHAFT;
    }
}
