package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.gen.feature.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BetterMineshafts implements ModInitializer {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static StructureFeature<BetterMineshaftFeatureConfig> BETTER_MINESHAFT_FEATURE;
    public static StructureFeature<?> VANILLA_MINESHAFT_FEATURE;

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Register feature and structure
        BETTER_MINESHAFT_FEATURE = Registry.register(
            Registry.FEATURE,
            new Identifier(MOD_ID, "mineshaft"),
            new BetterMineshaftFeature(BetterMineshaftFeatureConfig::deserialize)
        );

        Registry.register(
            Registry.STRUCTURE_FEATURE,
            new Identifier(MOD_ID, "mineshaft"),
            BETTER_MINESHAFT_FEATURE
        );

        // Register pieces
        BetterMineshaftStructurePieceType.init();

        // Get vanilla mineshaft, used to check which biomes should be modified
        VANILLA_MINESHAFT_FEATURE = Registry.STRUCTURE_FEATURE.get(new Identifier("minecraft:mineshaft"));

        // Add Better Mineshaft to applicable biomes, replacing vanilla mineshafts
        Registry.BIOME.forEach(biome -> {
            if (biome.hasStructureFeature(VANILLA_MINESHAFT_FEATURE)) {
                FeatureAdder.addBetterMineshafts(biome);
            }
        });

        // This is for making /locate work (not sure if necessary?)
        Feature.STRUCTURES.put("bettermineshaft", BETTER_MINESHAFT_FEATURE);
    }
}
