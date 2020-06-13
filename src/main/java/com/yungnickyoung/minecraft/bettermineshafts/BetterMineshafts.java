package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructureFeaturePieces;
import io.netty.util.internal.ConcurrentSet;
import net.fabricmc.api.ModInitializer;
import net.minecraft.world.gen.feature.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class BetterMineshafts implements ModInitializer {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final boolean DEBUG_LOG = true;

    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);

    // TODO - replace hardcoded spawnrates w/ config option
    public static final double SPAWN_RATE = .01; // .003

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Initialization
        BMStructureFeaturePieces.init(); // Register pieces
        BMStructureFeature.init(); // Add structure/feature to applicable biomes

        // Not sure exactly what this is for but it's necessary to make work.
        Feature.STRUCTURES.put("bettermineshaft", BMStructureFeature.BETTER_MINESHAFT_FEATURE);
    }
}
