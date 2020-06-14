package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructurePieces;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Mod("bettermineshafts")
public class BetterMineshafts {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final boolean DEBUG_LOG = false;

    public static final Structure<BetterMineshaftFeatureConfig> betterMineshaft = new BetterMineshaftStructure(BetterMineshaftFeatureConfig::deserialize);

    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);

    // TODO - replace hardcoded spawnrates w/ config option
    public static final double SPAWN_RATE = .003;

    public BetterMineshafts() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerFeature);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    /**
     * Listens for minecraft:feature registry Register event and replaces
     * normal mineshafts with better mineshafts.
     */
    public void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        if (event.getRegistry().getRegistryName().toString().equals("minecraft:feature")) {
            BMStructurePieces.init();
            betterMineshaft.setRegistryName("minecraft:mineshaft");
            event.getRegistry().register(betterMineshaft);

        }
    }

    /**
     * Adds better mineshafts structure to all biomes.
     */
    public void commonSetup(FMLCommonSetupEvent event) {
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

            config = new BetterMineshaftFeatureConfig(SPAWN_RATE, type);

            // No mineshafts in oceans
            if (biome.getCategory() == Biome.Category.OCEAN)
                continue;

            biome.addStructure(betterMineshaft.withConfiguration(config));
        }
    }
}
