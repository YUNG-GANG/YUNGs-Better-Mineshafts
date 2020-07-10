package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ModStructures {
    // We use "minecraft" as the mod ID to override vanilla
    public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, "minecraft");
    public static final RegistryObject<Structure<BetterMineshaftConfig>> BETTERMINESHAFT = register("Mineshaft", new BetterMineshaftStructure(BetterMineshaftConfig.field_236541_a_), GenerationStage.Decoration.UNDERGROUND_STRUCTURES);

    private static <T extends Structure<?>> RegistryObject<T> register(String name, T structure, GenerationStage.Decoration stage) {
        Structure.field_236385_u_.put(structure, stage);
        Structure.field_236365_a_.put(name, structure);
        return STRUCTURES.register(name.toLowerCase(Locale.ROOT), () -> structure);
    }

    public static void init() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModStructures::commonSetup);
    }

    /**
     * Adds better mineshafts structure to all applicable biomes.
     */
    public static void commonSetup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ModStructurePieces.init();

            BetterMineshaftStructure.Type type;
            double spawnRate = BMConfig.mineshaftSpawnRate;

            // Set config based on biome
            Set<Map.Entry<ResourceLocation, Biome>> biomesList = ForgeRegistries.BIOMES.getEntries();
            for (Map.Entry<ResourceLocation, Biome> entry : biomesList) {
                Biome biome = entry.getValue();

                // Only operate on biomes that have mineshafts
                if (!biome.hasStructure(Structure.field_236367_c_)) {
                    continue;
                }

                // Remove vanilla mineshaft
                biome.structures.remove(Structure.field_236367_c_);

                // Cannot generate in oceans or beaches
                if (biome.getCategory() == Biome.Category.OCEAN || biome.getCategory() == Biome.Category.BEACH)
                    continue;

                switch (biome.getCategory()) {
                    case MESA:
                        if (biome instanceof BadlandsBiome) {
                            type = BetterMineshaftStructure.Type.RED_DESERT;
                        } else {
                            type = BetterMineshaftStructure.Type.MESA;
                        }
                        break;
                    case JUNGLE:
                        type = BetterMineshaftStructure.Type.JUNGLE;
                        break;
                    case ICY:
                        if (biome instanceof IceSpikesBiome) {
                            type = BetterMineshaftStructure.Type.ICE;
                        } else {
                            type = BetterMineshaftStructure.Type.SNOW;
                        }
                        break;
                    case TAIGA:
                        if (biome instanceof SnowyTaigaBiome || biome instanceof SnowyTaigaHillsBiome || biome instanceof SnowyTaigaMountainsBiome) {
                            type = BetterMineshaftStructure.Type.ICE;
                        } else {
                            type = BetterMineshaftStructure.Type.SNOW;
                        }
                        break;
                    case DESERT:
                        if (biome instanceof DesertBiome) {
                            type = BetterMineshaftStructure.Type.RED_DESERT;
                        } else {
                            type = BetterMineshaftStructure.Type.DESERT;
                        }
                        break;
                    case MUSHROOM:
                        type = BetterMineshaftStructure.Type.MUSHROOM;
                        break;
                    case SAVANNA:
                        type = BetterMineshaftStructure.Type.SAVANNA;
                        break;
                    default:
                        type = BetterMineshaftStructure.Type.NORMAL;
                        break;
                }

                biome.func_235063_a_(BETTERMINESHAFT.get().func_236391_a_(new BetterMineshaftConfig(spawnRate, type)));
            }
        });
    }
}
