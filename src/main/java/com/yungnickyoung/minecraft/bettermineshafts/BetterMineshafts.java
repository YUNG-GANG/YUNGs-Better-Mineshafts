package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMConfiguredStructureFeatures;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructureFeatures;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructureFeaturePieces;
import io.netty.util.internal.ConcurrentSet;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class BetterMineshafts implements ModInitializer {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final boolean DEBUG_LOG = false;

    /** Debug info **/
    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);

    /** Better Caves config. Uses AutoConfig. **/
    public static Configuration CONFIG;

    @Override
    public void onInitialize() {
        // Initialization & Registration
        BMConfig.init();
        BMStructureFeatures.init();
        BMConfiguredStructureFeatures.init();
        BMStructureFeaturePieces.init();
    }
}
