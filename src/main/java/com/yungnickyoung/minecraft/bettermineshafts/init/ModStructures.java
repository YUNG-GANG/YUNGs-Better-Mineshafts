package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.common.BiomeDictionary;
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

            BetterMineshaftConfig config;
            BetterMineshaftStructure.Type type;

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

                // Determine mineshaft variant based on biome
                if (biome.getCategory() == Biome.Category.MESA || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
                    if (biome instanceof BadlandsBiome || isRare(biome)) {
                        type = getType(BetterMineshaftStructure.Type.RED_DESERT);
                    } else {
                        type = getType(BetterMineshaftStructure.Type.MESA);
                    }
                }
                else if (biome.getCategory() == Biome.Category.JUNGLE || BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                    type = getType(BetterMineshaftStructure.Type.JUNGLE);
                }
                else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY) || biome.getCategory() == Biome.Category.ICY || biome.getCategory() == Biome.Category.TAIGA) {
                    if (biome instanceof IceSpikesBiome || isRare(biome)) {
                        type = getType(BetterMineshaftStructure.Type.ICE);
                    } else {
                        type = getType(BetterMineshaftStructure.Type.SNOW);
                    }
                }
                else if (biome.getCategory() == Biome.Category.DESERT ||
                    (BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) &&
                        BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) &&
                        BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY))
                ) {
                    if (biome instanceof DesertLakesBiome || isRare(biome)) {
                        type = getType(BetterMineshaftStructure.Type.RED_DESERT);
                    } else {
                        type = getType(BetterMineshaftStructure.Type.DESERT);
                    }
                }
                else if (biome.getCategory() == Biome.Category.MUSHROOM || BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM)) {
                    type = getType(BetterMineshaftStructure.Type.MUSHROOM);
                }
                else if (biome.getCategory() == Biome.Category.SAVANNA || BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA)) {
                    type = getType(BetterMineshaftStructure.Type.SAVANNA);
                } else {
                    type = BetterMineshaftStructure.Type.NORMAL;
                }

                config = new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, type);
                biome.func_235063_a_(BETTERMINESHAFT.get().func_236391_a_(config));
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

    private static boolean isRare(Biome biome) {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.RARE);
    }
}
