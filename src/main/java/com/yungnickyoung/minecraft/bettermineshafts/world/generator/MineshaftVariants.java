package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
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
                new BlockSetSelector()
                    .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.RED_SAND.getDefaultState(), 0.3f)
                    .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState(), .5f)
                    .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), .1f))
            .setLegSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState(), .5f)
                    .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), .1f))
            .setMainBlock(Blocks.RED_SANDSTONE.getDefaultState())
            .setSupportBlock(Blocks.RED_SANDSTONE_WALL.getDefaultState())
            .setSlabBlock(Blocks.RED_SANDSTONE_SLAB.getDefaultState())
            .setGravelBlock(Blocks.RED_SAND.getDefaultState())
            .setStoneWallBlock(Blocks.RED_SANDSTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.RED_SANDSTONE_SLAB.getDefaultState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
            .setTrapdoorBlock(Blocks.DARK_OAK_TRAPDOOR.getDefaultState())
            .setVineChance(.1f)
            .setSnowChance(0)
            .setCactusChance(.1f)
            .setDeadBushChance(.1f)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(false)
            .setReplacementRate(.6f)
        );

        // ICE
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.RARE)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.1f)
                    .addBlock(Blocks.BLUE_ICE.getDefaultState(), .4f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), 0.1f)
                    .addBlock(Blocks.BLUE_ICE.getDefaultState(), .2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.BLUE_ICE.getDefaultState(), .5f))
            .setLegSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.BLUE_ICE.getDefaultState(), .5f))
            .setMainBlock(Blocks.BLUE_ICE.getDefaultState())
            .setSupportBlock(Blocks.BLUE_ICE.getDefaultState())
            .setSlabBlock(Blocks.BLUE_ICE.getDefaultState())
            .setGravelBlock(Blocks.SNOW_BLOCK.getDefaultState())
            .setStoneWallBlock(Blocks.BLUE_ICE.getDefaultState())
            .setStoneSlabBlock(Blocks.BLUE_ICE.getDefaultState())
            .setTrapdoorBlock(Blocks.SPRUCE_TRAPDOOR.getDefaultState())
            .setVineChance(.05f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(2)
            .setFlammableLegs(false)
            .setReplacementRate(.95f)
        );

        // MESA
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.MESA)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.BROWN_TERRACOTTA.getDefaultState(), 0.05f)
                    .addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 0.05f)
                    .addBlock(Blocks.YELLOW_TERRACOTTA.getDefaultState(), 0.05f)
                    .addBlock(Blocks.WHITE_TERRACOTTA.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.BROWN_TERRACOTTA.getDefaultState(), 0.1f)
                    .addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 0.1f)
                    .addBlock(Blocks.YELLOW_TERRACOTTA.getDefaultState(), 0.1f)
                    .addBlock(Blocks.WHITE_TERRACOTTA.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 0.05f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.STRIPPED_DARK_OAK_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
            .setMainBlock(Blocks.DARK_OAK_PLANKS.getDefaultState())
            .setSupportBlock(Blocks.DARK_OAK_FENCE.getDefaultState())
            .setSlabBlock(Blocks.DARK_OAK_SLAB.getDefaultState())
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.STONE_BRICK_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.getDefaultState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
            .setTrapdoorBlock(Blocks.DARK_OAK_TRAPDOOR.getDefaultState())
            .setVineChance(.15f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(.1f)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(true)
            .setReplacementRate(.9f)
        );

        // JUNGLE
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.JUNGLE)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.2f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.2f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 0.05f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .25f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), .25f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), .25f)
                    .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), .25f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.STRIPPED_JUNGLE_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
            .setMainBlock(Blocks.JUNGLE_PLANKS.getDefaultState())
            .setSupportBlock(Blocks.JUNGLE_FENCE.getDefaultState())
            .setSlabBlock(Blocks.JUNGLE_SLAB.getDefaultState())
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.STONE_BRICK_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.getDefaultState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
            .setTrapdoorBlock(Blocks.JUNGLE_TRAPDOOR.getDefaultState())
            .setVineChance(.6f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(true)
            .setReplacementRate(.6f)
        );

        // SNOWY SPRUCE
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.SNOWY)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .25f)
                    .addBlock(Blocks.PACKED_ICE.getDefaultState(), .2f)
                    .addBlock(Blocks.BLUE_ICE.getDefaultState(), .1f)
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .25f)
                    .addBlock(Blocks.PACKED_ICE.getDefaultState(), .2f)
                    .addBlock(Blocks.BLUE_ICE.getDefaultState(), .1f)
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .5f)
                    .addBlock(Blocks.PACKED_ICE.getDefaultState(), .25f)
                    .addBlock(Blocks.BLUE_ICE.getDefaultState(), .25f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.STRIPPED_SPRUCE_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
            .setMainBlock(Blocks.SPRUCE_PLANKS.getDefaultState())
            .setSupportBlock(Blocks.SPRUCE_FENCE.getDefaultState())
            .setSlabBlock(Blocks.SPRUCE_SLAB.getDefaultState())
            .setGravelBlock(Blocks.SNOW_BLOCK.getDefaultState())
            .setStoneWallBlock(Blocks.SNOW_BLOCK.getDefaultState())
            .setStoneSlabBlock(Blocks.SNOW_BLOCK.getDefaultState())
            .setTrapdoorBlock(Blocks.SPRUCE_TRAPDOOR.getDefaultState())
            .setVineChance(.2f)
            .setSnowChance(1f)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(true)
            .setReplacementRate(.9f)
        );

        // SPRUCE (NO SNOW)
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.COLD, BiomeDictionary.Type.CONIFEROUS, BiomeDictionary.Type.FOREST)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.STRIPPED_SPRUCE_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
            .setMainBlock(Blocks.SPRUCE_PLANKS.getDefaultState())
            .setSupportBlock(Blocks.SPRUCE_FENCE.getDefaultState())
            .setSlabBlock(Blocks.SPRUCE_SLAB.getDefaultState())
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.STONE_BRICK_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.getDefaultState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
            .setTrapdoorBlock(Blocks.SPRUCE_TRAPDOOR.getDefaultState())
            .setVineChance(.25f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(true)
            .setReplacementRate(.6f)
        );

        // DESERT
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SAND.getDefaultState(), 0.3f)
                    .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SANDSTONE.getDefaultState(), .5f)
                    .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), .1f))
            .setLegSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SANDSTONE.getDefaultState(), .5f)
                    .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), .2f)
                    .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), .1f))
            .setMainBlock(Blocks.SANDSTONE.getDefaultState())
            .setSupportBlock(Blocks.SANDSTONE_WALL.getDefaultState())
            .setSlabBlock(Blocks.SANDSTONE_SLAB.getDefaultState())
            .setGravelBlock(Blocks.SAND.getDefaultState())
            .setStoneWallBlock(Blocks.SANDSTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.SANDSTONE_SLAB.getDefaultState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
            .setTrapdoorBlock(Blocks.OAK_TRAPDOOR.getDefaultState())
            .setVineChance(.1f)
            .setSnowChance(0)
            .setCactusChance(.1f)
            .setDeadBushChance(.1f)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(false)
            .setReplacementRate(.6f)
        );

        // SAVANNA (ACACIA)
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.SAVANNA)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.STRIPPED_ACACIA_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
            .setMainBlock(Blocks.ACACIA_PLANKS.getDefaultState())
            .setSupportBlock(Blocks.ACACIA_FENCE.getDefaultState())
            .setSlabBlock(Blocks.ACACIA_SLAB.getDefaultState())
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.STONE_BRICK_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.getDefaultState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
            .setTrapdoorBlock(Blocks.ACACIA_TRAPDOOR.getDefaultState())
            .setVineChance(.25f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(true)
            .setReplacementRate(.6f)
        );

        // MUSHROOM
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.MUSHROOM)))))
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MUSHROOM_STEM.getDefaultState(), .33333f)
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                    .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MYCELIUM.getDefaultState(), 1))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MUSHROOM_STEM.getDefaultState(), .33333f)
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                    .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f))
            .setLegSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MUSHROOM_STEM.getDefaultState(), .33333f)
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                    .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f))
            .setMainBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState())
            .setSupportBlock(Blocks.MUSHROOM_STEM.getDefaultState())
            .setSlabBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState())
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState())
            .setStoneSlabBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState())
            .setTrapdoorBlock(Blocks.OAK_TRAPDOOR.getDefaultState())
            .setVineChance(.25f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(.4f)
            .setLegVariant(2)
            .setFlammableLegs(true)
            .setReplacementRate(.95f)
        );

        // Set default for biomes that don't match any of the biome tag lists
        this.defaultVariant = new MineshaftVariantSettings()
            .setBiomeTags(Lists.newArrayList()) // TODO - ensure this list can only be empty if this variant is named default
            .setMainSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.STRIPPED_OAK_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)))
            .setMainBlock(Blocks.OAK_PLANKS.getDefaultState())
            .setSupportBlock(Blocks.OAK_FENCE.getDefaultState())
            .setSlabBlock(Blocks.OAK_SLAB.getDefaultState())
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.STONE_BRICK_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_BRICK_SLAB.getDefaultState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP))
            .setTrapdoorBlock(Blocks.OAK_TRAPDOOR.getDefaultState())
            .setVineChance(.25f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setFlammableLegs(true)
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