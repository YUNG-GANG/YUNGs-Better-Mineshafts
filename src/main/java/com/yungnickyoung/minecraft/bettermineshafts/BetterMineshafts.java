package com.yungnickyoung.minecraft.bettermineshafts;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMModConfig;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMModStructureFeaturePieces;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMModStructureFeatures;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Mod(BetterMineshafts.MOD_ID)
public class BetterMineshafts {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    /**
     * Lists of whitelisted dimensions and blacklisted biomes.
     * Will be reinitialized later w/ values from config.
     */
    public static List<String> whitelistedDimensions = Lists.newArrayList("minecraft:overworld");
    public static List<String> blacklistedBiomes = Lists.newArrayList(
            "minecraft:ocean", "minecraft:frozen_ocean", "minecraft:deep_ocean",
            "minecraft:warm_ocean", "minecraft:lukewarm_ocean", "minecraft:cold_ocean",
            "minecraft:deep_lukewarm_ocean", "minecraft:deep_cold_ocean", "minecraft:deep_frozen_ocean",
            "minecraft:beach", "minecraft:snowy_beach",
            "minecraft:river", "minecraft:frozen_river"
    );

    /** Debug info **/
    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);
    public static final boolean DEBUG_LOG = false;

    public BetterMineshafts() {
        init();
    }

    private void init() {
        BiomeDictionary.addTypes(Biomes.LUSH_CAVES, BiomeDictionary.Type.LUSH);
        BMModConfig.init();
        BMModStructureFeatures.init();
        BMModStructureFeaturePieces.init();
    }
}
