package com.yungnickyoung.minecraft.bettermineshafts.world.variant;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Singleton class holding list of all {@link MineshaftVariantSettings}.
 * The class is a singleton so that it may be synchronized with the JSON file as a single source of truth.
 * If no JSON exists, this class will be populated with the default values shown below
 * (and a JSON with the default values created)
 */
public class MineshaftVariants {

    /** Singleton stuff **/

    public static MineshaftVariants instance; // This technically shouldn't be public, but it is necessary for loading data from JSON
    public static MineshaftVariants get() {
        if (instance == null) {
            instance = new MineshaftVariants();
        }
        return instance;
    }

    private MineshaftVariants() {
        // Populate with default settings
        variants = new ArrayList<>();

        // RED DESERT
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(Arrays.asList(
                        new ArrayList<>(Arrays.asList(BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.RARE)),
                        new ArrayList<>(Arrays.asList(BiomeDictionary.Type.MESA, BiomeDictionary.Type.RARE)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.RED_SAND.defaultBlockState(), 0.3f)
                                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), .1f))
                .setLegSelector(
                        new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), .1f))
                .setMainBlock(Blocks.RED_SANDSTONE.defaultBlockState())
                .setSupportBlock(Blocks.RED_SANDSTONE_WALL.defaultBlockState())
                .setSlabBlock(Blocks.RED_SANDSTONE_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.RED_SAND.defaultBlockState())
                .setStoneWallBlock(Blocks.RED_SANDSTONE_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.RED_SANDSTONE_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
                .setSmallLegBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState())
                .setTrapdoorBlock(Blocks.DARK_OAK_TRAPDOOR.defaultBlockState())
                .setVineChance(.1f)
                .setSnowChance(0)
                .setCactusChance(.1f)
                .setDeadBushChance(.1f)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(false)
                .setLushDecorations(false)
                .setReplacementRate(.6f)
        );

        // ICE
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(Arrays.asList(BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.RARE)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .4f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .2f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .5f))
                .setLegSelector(
                        new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .5f))
                .setMainBlock(Blocks.PACKED_ICE.defaultBlockState())
                .setSupportBlock(Blocks.PACKED_ICE.defaultBlockState())
                .setSlabBlock(Blocks.PACKED_ICE.defaultBlockState())
                .setGravelBlock(Blocks.SNOW_BLOCK.defaultBlockState())
                .setStoneWallBlock(Blocks.PACKED_ICE.defaultBlockState())
                .setStoneSlabBlock(Blocks.PACKED_ICE.defaultBlockState())
                .setSmallLegBlock(Blocks.PACKED_ICE.defaultBlockState())
                .setTrapdoorBlock(Blocks.SPRUCE_TRAPDOOR.defaultBlockState())
                .setVineChance(0)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(0)
                .setLegVariant(2)
                .setFlammableLegs(false)
                .setLushDecorations(false)
                .setReplacementRate(.95f)
        );

        // MESA
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(List.of(BiomeDictionary.Type.MESA)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.DARK_OAK_PLANKS.defaultBlockState())
                                .addBlock(Blocks.BROWN_TERRACOTTA.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.YELLOW_TERRACOTTA.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.WHITE_TERRACOTTA.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.DARK_OAK_PLANKS.defaultBlockState())
                                .addBlock(Blocks.BROWN_TERRACOTTA.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.YELLOW_TERRACOTTA.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.WHITE_TERRACOTTA.defaultBlockState(), 0.1f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.DARK_OAK_PLANKS.defaultBlockState())
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.33333f))
                .setLegSelector(BlockSetSelector.from(
                        Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
                .setMainBlock(Blocks.DARK_OAK_PLANKS.defaultBlockState())
                .setSupportBlock(Blocks.DARK_OAK_FENCE.defaultBlockState())
                .setSlabBlock(Blocks.DARK_OAK_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.GRAVEL.defaultBlockState())
                .setStoneWallBlock(Blocks.COBBLESTONE_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.COBBLESTONE_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
                .setSmallLegBlock(Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState())
                .setTrapdoorBlock(Blocks.DARK_OAK_TRAPDOOR.defaultBlockState())
                .setVineChance(.1f)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(.1f)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(true)
                .setLushDecorations(false)
                .setReplacementRate(.9f)
        );

        // JUNGLE
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(List.of(BiomeDictionary.Type.JUNGLE)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.JUNGLE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.2f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.JUNGLE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.2f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 0.05f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.JUNGLE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .25f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), .25f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), .25f)
                                .addBlock(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), .25f))
                .setLegSelector(BlockSetSelector.from(
                        Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
                .setMainBlock(Blocks.JUNGLE_PLANKS.defaultBlockState())
                .setSupportBlock(Blocks.JUNGLE_FENCE.defaultBlockState())
                .setSlabBlock(Blocks.JUNGLE_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.GRAVEL.defaultBlockState())
                .setStoneWallBlock(Blocks.COBBLESTONE_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.COBBLESTONE_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
                .setSmallLegBlock(Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState())
                .setTrapdoorBlock(Blocks.JUNGLE_TRAPDOOR.defaultBlockState())
                .setVineChance(.5f)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(true)
                .setLushDecorations(false)
                .setReplacementRate(.6f)
        );

        // SNOWY SPRUCE
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(List.of(BiomeDictionary.Type.SNOWY)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), .25f)
                                .addBlock(Blocks.PACKED_ICE.defaultBlockState(), .2f)
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), .25f)
                                .addBlock(Blocks.PACKED_ICE.defaultBlockState(), .2f)
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .33333f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), .33333f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), .33333f))
                .setLegSelector(BlockSetSelector.from(
                        Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
                .setMainBlock(Blocks.SPRUCE_PLANKS.defaultBlockState())
                .setSupportBlock(Blocks.SPRUCE_FENCE.defaultBlockState())
                .setSlabBlock(Blocks.SPRUCE_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.SNOW_BLOCK.defaultBlockState())
                .setStoneWallBlock(Blocks.STONE_BRICK_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.defaultBlockState())
                .setSmallLegBlock(Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState())
                .setTrapdoorBlock(Blocks.SPRUCE_TRAPDOOR.defaultBlockState())
                .setVineChance(0)
                .setSnowChance(1f)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(true)
                .setLushDecorations(false)
                .setReplacementRate(.9f)
        );

        // SPRUCE (NO SNOW)
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(Arrays.asList(BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.33333f))
                .setLegSelector(BlockSetSelector.from(
                        Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
                .setMainBlock(Blocks.SPRUCE_PLANKS.defaultBlockState())
                .setSupportBlock(Blocks.SPRUCE_FENCE.defaultBlockState())
                .setSlabBlock(Blocks.SPRUCE_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.GRAVEL.defaultBlockState())
                .setStoneWallBlock(Blocks.STONE_BRICK_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
                .setSmallLegBlock(Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState())
                .setTrapdoorBlock(Blocks.SPRUCE_TRAPDOOR.defaultBlockState())
                .setVineChance(.05f)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(true)
                .setLushDecorations(false)
                .setReplacementRate(.6f)
        );

        // DESERT
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(Arrays.asList(BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.SAND.defaultBlockState(), 0.3f)
                                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), .1f))
                .setLegSelector(
                        new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), .2f)
                                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), .1f))
                .setMainBlock(Blocks.SANDSTONE.defaultBlockState())
                .setSupportBlock(Blocks.SANDSTONE_WALL.defaultBlockState())
                .setSlabBlock(Blocks.SANDSTONE_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.SAND.defaultBlockState())
                .setStoneWallBlock(Blocks.SANDSTONE_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.SANDSTONE_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
                .setSmallLegBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState())
                .setTrapdoorBlock(Blocks.OAK_TRAPDOOR.defaultBlockState())
                .setVineChance(0)
                .setSnowChance(0)
                .setCactusChance(.1f)
                .setDeadBushChance(.1f)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(false)
                .setLushDecorations(false)
                .setReplacementRate(.6f)
        );

        // SAVANNA (ACACIA)
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(List.of(BiomeDictionary.Type.SAVANNA)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.ACACIA_PLANKS.defaultBlockState())
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.ACACIA_PLANKS.defaultBlockState())
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.ACACIA_PLANKS.defaultBlockState())
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.33333f))
                .setLegSelector(BlockSetSelector.from(
                        Blocks.STRIPPED_ACACIA_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
                .setMainBlock(Blocks.ACACIA_PLANKS.defaultBlockState())
                .setSupportBlock(Blocks.ACACIA_FENCE.defaultBlockState())
                .setSlabBlock(Blocks.ACACIA_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.GRAVEL.defaultBlockState())
                .setStoneWallBlock(Blocks.STONE_BRICK_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
                .setSmallLegBlock(Blocks.STRIPPED_ACACIA_LOG.defaultBlockState())
                .setTrapdoorBlock(Blocks.ACACIA_TRAPDOOR.defaultBlockState())
                .setVineChance(.1f)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(true)
                .setLushDecorations(false)
                .setReplacementRate(.6f)
        );

        // MUSHROOM
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(List.of(BiomeDictionary.Type.MUSHROOM)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                                .addBlock(Blocks.MUSHROOM_STEM.defaultBlockState(), .33333f)
                                .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState(), .33333f)
                                .addBlock(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), .33333f))
                .setFloorSelector(
                        new BlockSetSelector()
                                .addBlock(Blocks.MYCELIUM.defaultBlockState(), 1))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                                .addBlock(Blocks.MUSHROOM_STEM.defaultBlockState(), .33333f)
                                .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState(), .33333f)
                                .addBlock(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), .33333f))
                .setLegSelector(
                        BlockSetSelector.from(Blocks.MUSHROOM_STEM.defaultBlockState()))
                .setMainBlock(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                .setSupportBlock(Blocks.MUSHROOM_STEM.defaultBlockState())
                .setSlabBlock(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState())
                .setGravelBlock(Blocks.GRAVEL.defaultBlockState())
                .setStoneWallBlock(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                .setStoneSlabBlock(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                .setSmallLegBlock(Blocks.MUSHROOM_STEM.defaultBlockState())
                .setTrapdoorBlock(Blocks.OAK_TRAPDOOR.defaultBlockState())
                .setVineChance(.1f)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(.4f)
                .setLegVariant(2)
                .setFlammableLegs(true)
                .setLushDecorations(false)
                .setReplacementRate(.95f)
        );

        // LUSH
        variants.add(new MineshaftVariantSettings()
                .setBiomeTags(new ArrayList<>(List.of(
                        new ArrayList<>(List.of(BiomeDictionary.Type.LUSH)))))
                .setMainSelector(
                        new BlockSetSelector(Blocks.OAK_PLANKS.defaultBlockState())
                                .addBlock(Blocks.MOSS_BLOCK.defaultBlockState(), 0.7f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.15f))
                .setFloorSelector(
                        new BlockSetSelector(Blocks.OAK_PLANKS.defaultBlockState())
                                .addBlock(Blocks.MOSS_BLOCK.defaultBlockState(), 0.75f))
                .setBrickSelector(
                        new BlockSetSelector(Blocks.OAK_PLANKS.defaultBlockState())
                                .addBlock(Blocks.MOSS_BLOCK.defaultBlockState(), 0.75f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.15f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f))
                .setLegSelector(
                        BlockSetSelector.from(Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
                .setMainBlock(Blocks.OAK_PLANKS.defaultBlockState())
                .setSupportBlock(Blocks.OAK_FENCE.defaultBlockState())
                .setSlabBlock(Blocks.OAK_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.GRAVEL.defaultBlockState())
                .setStoneWallBlock(Blocks.MOSSY_COBBLESTONE_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState())
                .setSmallLegBlock(Blocks.STRIPPED_OAK_LOG.defaultBlockState())
                .setTrapdoorBlock(Blocks.OAK_TRAPDOOR.defaultBlockState())
                .setVineChance(0)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(true)
                .setLushDecorations(true)
                .setReplacementRate(.95f)
        );

        // Set default for biomes that don't match any of the biome tag lists
        this.defaultVariant = new MineshaftVariantSettings()
                .setBiomeTags(Lists.newArrayList()) // TODO - ensure this list can only be empty if this variant is named default
                .setMainSelector(
                        new BlockSetSelector()
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f))
                .setFloorSelector(
                        new BlockSetSelector()
                                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f))
                .setBrickSelector(
                        new BlockSetSelector()
                                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.33333f)
                                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.33333f))
                .setLegSelector(BlockSetSelector.from(
                        Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
                .setMainBlock(Blocks.OAK_PLANKS.defaultBlockState())
                .setSupportBlock(Blocks.OAK_FENCE.defaultBlockState())
                .setSlabBlock(Blocks.OAK_SLAB.defaultBlockState())
                .setGravelBlock(Blocks.GRAVEL.defaultBlockState())
                .setStoneWallBlock(Blocks.STONE_BRICK_WALL.defaultBlockState())
                .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
                .setSmallLegBlock(Blocks.STRIPPED_OAK_LOG.defaultBlockState())
                .setTrapdoorBlock(Blocks.OAK_TRAPDOOR.defaultBlockState())
                .setVineChance(.1f)
                .setSnowChance(0)
                .setCactusChance(0)
                .setDeadBushChance(0)
                .setMushroomChance(0)
                .setLegVariant(1)
                .setFlammableLegs(true)
                .setLushDecorations(false)
                .setReplacementRate(.6f);
    }

    /** Instance variables and methods **/

    private List<MineshaftVariantSettings> variants;
    private MineshaftVariantSettings defaultVariant;

    public List<MineshaftVariantSettings> getVariants() {
        return variants;
    }

    public MineshaftVariantSettings getDefault() {
        return defaultVariant;
    }
}
