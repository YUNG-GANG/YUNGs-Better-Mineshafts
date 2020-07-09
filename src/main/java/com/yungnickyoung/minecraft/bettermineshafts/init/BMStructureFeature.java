package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

public class BMStructureFeature {
    private static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterMineshafts.MOD_ID);
    public static final BetterMineshaftStructure STRUCTURE = new BetterMineshaftStructure(BetterMineshaftConfig.field_236541_a_);
    public static final RegistryObject<Structure<?>> BETTERCAVES_CARVER_FEATURE = STRUCTURES.register("bettermineshaft", () -> STRUCTURE);

    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_NORMAL = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.NORMAL));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_MESA = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.MESA));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_JUNGLE = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.JUNGLE));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_SNOW = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.SNOW));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_ICE = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.ICE));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_DESERT = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.DESERT));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_REDDESERT = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.RED_DESERT));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_SAVANNA = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.SAVANNA));
    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> STRUCTURE_FEATURE_MUSHROOM = STRUCTURE.func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.MUSHROOM));

//    public static final Structure<BetterMineshaftConfig> betterMineshaft = new BetterMineshaftStructure(BetterMineshaftConfig::deserialize);

    public static void init() {
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMFeature::registerFeature);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMStructureFeature::commonSetup);
    }

    /**
     * Listens for minecraft:feature registry Register event and replaces
     * normal mineshafts with better mineshafts.
     */
//    public static void registerFeature(RegistryEvent.Register<Structure<?>> event) {
//        if (event.getRegistry().getRegistryName().toString().equals("minecraft:feature")) {
//            betterMineshaft.setRegistryName("Mineshaft".toLowerCase(Locale.ROOT));
//            event.getRegistry().register(betterMineshaft);
//        }
//    }

    /**
     * Adds better mineshafts structure to all applicable biomes.
     */
    public static void commonSetup(FMLCommonSetupEvent event) {
        DeferredWorkQueue.runLater(() -> {
            BMStructurePieces.init();

            // Set config based on biome
            Set<Map.Entry<ResourceLocation, Biome>> biomesList = ForgeRegistries.BIOMES.getEntries();
            for (Map.Entry<ResourceLocation, Biome> entry : biomesList) {
                Biome biome = entry.getValue();

                // Only operate on biomes that have mineshafts
                if (!biome.hasStructure(Structure.field_236367_c_)) {
                    continue;
                }

                // No mineshafts in oceans
                if (biome.getCategory() == Biome.Category.OCEAN)
                    continue;

                switch (biome.getCategory()) {
                    case MESA:
                        if (biome instanceof BadlandsBiome) {
                            biome.func_235063_a_(STRUCTURE_FEATURE_REDDESERT);
                        } else {
                            biome.func_235063_a_(STRUCTURE_FEATURE_MESA);
                        }
                        break;
                    case JUNGLE:
                        biome.func_235063_a_(STRUCTURE_FEATURE_JUNGLE);
                        break;
                    case ICY:
                        if (biome instanceof IceSpikesBiome) {
                            biome.func_235063_a_(STRUCTURE_FEATURE_ICE);
                        } else {
                            biome.func_235063_a_(STRUCTURE_FEATURE_SNOW);
                        }
                        break;
                    case TAIGA:
                        if (biome instanceof SnowyTaigaBiome || biome instanceof SnowyTaigaHillsBiome || biome instanceof SnowyTaigaMountainsBiome) {
                            biome.func_235063_a_(STRUCTURE_FEATURE_ICE);
                        } else {
                            biome.func_235063_a_(STRUCTURE_FEATURE_SNOW);
                        }
                        break;
                    case DESERT:
                        if (biome instanceof DesertBiome) {
                            biome.func_235063_a_(STRUCTURE_FEATURE_REDDESERT);
                        } else {
                            biome.func_235063_a_(STRUCTURE_FEATURE_DESERT);
                        }
                        break;
                    case MUSHROOM:
                        biome.func_235063_a_(STRUCTURE_FEATURE_MUSHROOM);
                        break;
                    case SAVANNA:
                        biome.func_235063_a_(STRUCTURE_FEATURE_SAVANNA);
                        break;
                    default:
                        biome.func_235063_a_(STRUCTURE_FEATURE_NORMAL);
                }
            }
        });
    }
}
