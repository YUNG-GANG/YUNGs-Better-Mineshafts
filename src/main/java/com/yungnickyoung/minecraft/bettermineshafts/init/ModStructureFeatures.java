package com.yungnickyoung.minecraft.bettermineshafts.init;

import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;

public class ModStructureFeatures {
    // All structure features. These are configured variants of the mineshaft structure.
    public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> NORMAL_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
        .func_236391_a_(IFeatureConfig.NO_FEATURE_CONFIG);

//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> MESA_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.MESA));
//
//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> JUNGLE_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.JUNGLE));
//
//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> SNOW_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.SNOW));
//
//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> MUSHROOM_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.MUSHROOM));
//
//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> SAVANNA_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.SAVANNA));
//
//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> DESERT_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.DESERT));
//
//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> REDDESERT_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.RED_DESERT));
//
//    public static final StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>> ICE_MINESHAFT = ModStructures.MINESHAFT_STRUCTURE.get()
//        .func_236391_a_(new BetterMineshaftConfig(BMConfig.mineshaftSpawnRate, BetterMineshaftStructure.Type.ICE));

    // List of all structure features to make updating spawn rate easy
//    public static final List<StructureFeature<BetterMineshaftConfig, ? extends Structure<BetterMineshaftConfig>>> STRUCTURE_FEATURE_LIST =
//        Arrays.asList(
//            NORMAL_MINESHAFT, MESA_MINESHAFT, JUNGLE_MINESHAFT,
//            SNOW_MINESHAFT, MUSHROOM_MINESHAFT, SAVANNA_MINESHAFT,
//            DESERT_MINESHAFT, REDDESERT_MINESHAFT, ICE_MINESHAFT
//        );
}
