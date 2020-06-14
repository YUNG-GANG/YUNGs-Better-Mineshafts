package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.Set;

public class BMFeature {
    public static final Structure<BetterMineshaftFeatureConfig> betterMineshaft = new BetterMineshaftStructure(BetterMineshaftFeatureConfig::deserialize);

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMFeature::registerFeature);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMFeature::commonSetup);
    }

    /**
     * Listens for minecraft:feature registry Register event and replaces
     * normal mineshafts with better mineshafts.
     */
    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        if (event.getRegistry().getRegistryName().toString().equals("minecraft:feature")) {
            betterMineshaft.setRegistryName("minecraft:mineshaft");
            event.getRegistry().register(betterMineshaft);
        }
    }

    /**
     * Adds better mineshafts structure to all applicable biomes.
     */
    public static void commonSetup(FMLCommonSetupEvent event) {
        BetterMineshaftFeatureConfig config;
        BetterMineshaftStructure.Type type;

        // Set config based on biome
        Set<Map.Entry<ResourceLocation, Biome>> biomesList = ForgeRegistries.BIOMES.getEntries();
        for (Map.Entry<ResourceLocation, Biome> e : biomesList) {
            Biome biome = e.getValue();
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
            }

            config = new BetterMineshaftFeatureConfig(BMConfig.mineshaftSpawnRate, type);

            // No mineshafts in oceans
            if (biome.getCategory() == Biome.Category.OCEAN)
                continue;

            biome.addStructure(betterMineshaft.withConfiguration(config));
        }
    }
}
