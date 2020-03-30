package com.yungnickyoung.minecraft.bettermineshafts;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.gen.feature.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;

public class BetterMineshafts implements ModInitializer {
    public static final String MOD_ID = "bettermineshafts";
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	public static BetterMineshafts INSTANCE;
	public MineshaftFeature betterMineshaft = new BetterMineshaftFeature(MineshaftFeatureConfig::deserialize);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
        INSTANCE = this;

        // Register Better Mineshaft to replace default mineshaft in registry
		SimpleRegistry<StructureFeature<?>> registry = (SimpleRegistry<StructureFeature<?>>)Registry.STRUCTURE_FEATURE;
		Identifier id = new Identifier("minecraft:mineshaft");
		StructureFeature<?> vanillaMineshaft = registry.get(id);
		int rawID = registry.getRawId(vanillaMineshaft);
		MineshaftFeature FEATURE = registry.set(rawID, id, betterMineshaft);

		// Add Better Mineshaft to biomes
		Registry.BIOME.forEach(biome -> {
			if (biome.hasStructureFeature(vanillaMineshaft)) {
				try {
					Field field = biome.getClass().getDeclaredField("structureFeatures");
					field.setAccessible(true);
					Map<StructureFeature<?>, FeatureConfig> structureFeatures = (Map<StructureFeature<?>, FeatureConfig>)field.get(biome);
					structureFeatures.keySet().removeIf(feature -> feature == vanillaMineshaft);
					field.set(biome, structureFeatures);
				} catch (Exception e) {

				}
				biome.addStructureFeature(betterMineshaft.configure(new MineshaftFeatureConfig(0.004D, MineshaftFeature.Type.NORMAL)));
			}
		});

		// This is for making /locate work
		Feature.STRUCTURES.put("better_mineshaft", FEATURE);
	}
}
