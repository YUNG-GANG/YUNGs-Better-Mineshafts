package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeatureConfig;
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
    public static final BetterMineshaftFeature BETTER_MINESHAFT_FEATURE = new BetterMineshaftFeature(BetterMineshaftFeatureConfig::deserialize);
    public static StructureFeature<?> VANILLA_MINESHAFT_FEATURE;

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

		// Register Better Mineshaft to replace default mineshaft in registry
		SimpleRegistry<StructureFeature<?>> registry = (SimpleRegistry<StructureFeature<?>>) Registry.STRUCTURE_FEATURE;
		VANILLA_MINESHAFT_FEATURE = getVanillaMineshaftFromRegistry(registry);
		registerBetterMineshafts(registry);

        // Add Better Mineshaft to applicable biomes, replacing vanilla mineshafts
        Registry.BIOME.forEach(biome -> {
            if (biome.hasStructureFeature(VANILLA_MINESHAFT_FEATURE)) {
                FeatureAdder.addBetterMineshafts(biome);
            }
        });

        // This is for making /locate work
        Feature.STRUCTURES.put("better_mineshaft", BETTER_MINESHAFT_FEATURE);
    }

    private static StructureFeature<?> getVanillaMineshaftFromRegistry(SimpleRegistry<StructureFeature<?>> registry) {
		Identifier id = new Identifier("minecraft:mineshaft");
		return registry.get(id);
	}

    private static void registerBetterMineshafts(SimpleRegistry<StructureFeature<?>> registry) {
		Identifier id = new Identifier("minecraft:mineshaft");
		int rawID = registry.getRawId(VANILLA_MINESHAFT_FEATURE);
		registry.set(rawID, id, BETTER_MINESHAFT_FEATURE);
    }
}
