package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.google.common.collect.ImmutableMap;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
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
    public static final DeferredRegister<Structure<?>> DEFERRED_REGISTRY_STRUCTURE = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BMSettings.MOD_ID);

    // The mineshaft Structure
    public static final RegistryObject<Structure<NoFeatureConfig>> MINESHAFT_STRUCTURE = DEFERRED_REGISTRY_STRUCTURE
        .register("mineshaft", () -> new BetterMineshaftStructure(NoFeatureConfig.field_236558_a_));

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

            // Register configured structure feature
            Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
            Registry.register(registry, new ResourceLocation(BMSettings.MOD_ID, "mineshaft"), ModStructureFeatures.MINESHAFT_STRUCTURE_FEATURE);

            // Add structurefeature to this to prevent any issues if other mods' custom ChunkGenerators use FlatGenerationSettings.STRUCTURES.
            FlatGenerationSettings.STRUCTURES.put(ModStructures.MINESHAFT_STRUCTURE.get(), ModStructureFeatures.MINESHAFT_STRUCTURE_FEATURE);

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
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator && serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
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

        // Add mineshaft to biome generation settings. We defer biome-specific logic to actual world generation later on.
        event.getGeneration().getStructures().add(() -> ModStructureFeatures.MINESHAFT_STRUCTURE_FEATURE);
    }
}
