package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.BMModConfig;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMModStructureFeaturePieces;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMModStructureFeatures;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMModTags;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@Mod(BetterMineshafts.MOD_ID)
public class BetterMineshafts {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

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
        BMModTags.init();
        BMModStructureFeatures.init();
        BMModStructureFeaturePieces.init();
    }
}
