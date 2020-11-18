package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Arrays;
import java.util.List;

public class BMConfiguredStructureFeatures {
    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> NORMAL_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.NORMAL));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> MESA_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.MESA));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> JUNGLE_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.JUNGLE));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> SNOW_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.SNOW));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> MUSHROOM_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.MUSHROOM));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> SAVANNA_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.SAVANNA));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> DESERT_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.DESERT));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> REDDESERT_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.RED_DESERT));

    public static ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>> ICE_MINESHAFT = BMStructureFeatures.MINESHAFT_STRUCTURE
        .configure(new BetterMineshaftConfig(.003, BetterMineshaftStructure.Type.ICE));

    // List of all structure features to make updating spawn rate easy
    public static final List<ConfiguredStructureFeature<BetterMineshaftConfig, ? extends StructureFeature<BetterMineshaftConfig>>> CONFIGURED_STRUCTURE_FEATURES =
        Arrays.asList(
            NORMAL_MINESHAFT, MESA_MINESHAFT, JUNGLE_MINESHAFT,
            SNOW_MINESHAFT, MUSHROOM_MINESHAFT, SAVANNA_MINESHAFT,
            DESERT_MINESHAFT, REDDESERT_MINESHAFT, ICE_MINESHAFT
        );

    /**
     * Registers configured structure features.
     */
    public static void init() {
        Registry<ConfiguredStructureFeature<?, ?>> registry = BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_normal"), NORMAL_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_mesa"), MESA_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_jungle"), JUNGLE_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_mushroom"), MUSHROOM_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_savanna"), SAVANNA_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_desert"), DESERT_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_reddesert"), REDDESERT_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_ice"), ICE_MINESHAFT);
        Registry.register(registry, new Identifier(BetterMineshafts.MOD_ID, "mineshaft_snow"), SNOW_MINESHAFT);
    }
}
