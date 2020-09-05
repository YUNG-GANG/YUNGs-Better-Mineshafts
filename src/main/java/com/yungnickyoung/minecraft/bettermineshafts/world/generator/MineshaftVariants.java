package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.util.BlockSetSelector;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
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
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), 0.1f)
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), 0.3f)
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), 0.1f)
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState(), .5f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), .1f)
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), .2f)
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), .2f))
            .setLegSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState(), .5f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), .1f)
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH), .2f)
                    .addBlock(Blocks.RED_SANDSTONE.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.CHISELED), .2f))
            .setMainBlock(Blocks.RED_SANDSTONE.getDefaultState())
            .setSupportBlock(Blocks.OAK_FENCE.getDefaultState())
            .setSlabBlock(Blocks.STONE_SLAB2.getDefaultState().withProperty(BlockStoneSlabNew.VARIANT, BlockStoneSlabNew.EnumType.RED_SANDSTONE))
            .setGravelBlock(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND))
            .setStoneWallBlock(Blocks.COBBLESTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_SLAB2.getDefaultState().withProperty(BlockStoneSlabNew.VARIANT, BlockStoneSlabNew.EnumType.RED_SANDSTONE).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
            .setVineChance(.1f)
            .setSnowChance(0)
            .setCactusChance(.1f)
            .setDeadBushChance(.1f)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setReplacementRate(.6f)
        );

        // SNOW (SPRUCE)
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.SNOWY, BiomeDictionary.Type.RARE)))))
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW.getDefaultState(), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.1f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW.getDefaultState(), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.1f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.PACKED_ICE.getDefaultState(), .9f)
                    .addBlock(Blocks.SNOW.getDefaultState(), .1f))
            .setLegSelector(
                new BlockSetSelector()
                .addBlock(Blocks.PACKED_ICE.getDefaultState(), .9f)
                .addBlock(Blocks.SNOW.getDefaultState(), .1f))
            .setMainBlock(Blocks.PACKED_ICE.getDefaultState())
            .setSupportBlock(Blocks.PACKED_ICE.getDefaultState())
            .setSlabBlock(Blocks.PACKED_ICE.getDefaultState())
            .setGravelBlock(Blocks.SNOW.getDefaultState())
            .setStoneWallBlock(Blocks.PACKED_ICE.getDefaultState())
            .setStoneSlabBlock(Blocks.PACKED_ICE.getDefaultState())
            .setVineChance(.05f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(2)
            .setReplacementRate(.95f)
        );

        // MESA
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.MESA)))))
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN), 0.1f)
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE), 0.1f)
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW), 0.1f)
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN), 0.05f)
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE), 0.05f)
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW), 0.05f)
                    .addBlock(Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.33333f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.33333f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.LOG2.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK)))
            .setMainBlock(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK))
            .setSupportBlock(Blocks.DARK_OAK_FENCE.getDefaultState())
            .setSlabBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK))
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.COBBLESTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
            .setVineChance(.15f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(.1f)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setReplacementRate(.6f)
        );

        // JUNGLE
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.JUNGLE)))))
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.2f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0.05f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.2f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), 0.05f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), .25f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), .25f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), .25f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CHISELED), .25f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE)))
            .setMainBlock(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE))
            .setSupportBlock(Blocks.JUNGLE_FENCE.getDefaultState())
            .setSlabBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE))
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.COBBLESTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
            .setVineChance(.6f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setReplacementRate(.6f)
        );

        // SNOW (SPRUCE)
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.SNOWY)))))
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW.getDefaultState(), .25f)
                    .addBlock(Blocks.PACKED_ICE.getDefaultState(), .2f)
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW.getDefaultState(), .25f)
                    .addBlock(Blocks.PACKED_ICE.getDefaultState(), .2f)
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SNOW.getDefaultState(), .5f)
                    .addBlock(Blocks.PACKED_ICE.getDefaultState(), .5f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE)))
            .setMainBlock(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE))
            .setSupportBlock(Blocks.SPRUCE_FENCE.getDefaultState())
            .setSlabBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE))
            .setGravelBlock(Blocks.SNOW.getDefaultState())
            .setStoneWallBlock(Blocks.SNOW.getDefaultState())
            .setStoneSlabBlock(Blocks.SNOW.getDefaultState())
            .setVineChance(.2f)
            .setSnowChance(1f)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setReplacementRate(.9f)
        );

        // DESERT
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY)))))
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), 0.1f)
                    .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SAND.getDefaultState(), 0.3f)
                    .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), 0.1f)
                    .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.05f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.05f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.SANDSTONE.getDefaultState(), .5f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), .1f)
                    .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), .2f)
                    .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), .2f))
            .setLegSelector(
                new BlockSetSelector()
                .addBlock(Blocks.SANDSTONE.getDefaultState(), .5f)
                .addBlock(Blocks.STONEBRICK.getDefaultState(), .1f)
                .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), .2f)
                .addBlock(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), .2f))
            .setMainBlock(Blocks.SANDSTONE.getDefaultState())
            .setSupportBlock(Blocks.OAK_FENCE.getDefaultState())
            .setSlabBlock(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND))
            .setGravelBlock(Blocks.SAND.getDefaultState())
            .setStoneWallBlock(Blocks.COBBLESTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
            .setVineChance(.1f)
            .setSnowChance(0)
            .setCactusChance(.1f)
            .setDeadBushChance(.1f)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setReplacementRate(.6f)
        );

        // SAVANNA (ACACIA)
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.SAVANNA)))))
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.33333f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.33333f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.LOG2.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA)))
            .setMainBlock(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))
            .setSupportBlock(Blocks.ACACIA_FENCE.getDefaultState())
            .setSlabBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA))
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.COBBLESTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
            .setVineChance(.25f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
            .setReplacementRate(.6f)
        );

        // MUSHROOM
        variants.add(new MineshaftVariantSettings()
            .setBiomeTags(new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(BiomeDictionary.Type.MUSHROOM)))))
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), .33333f)
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                    .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.MYCELIUM.getDefaultState(), 1))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), .33333f)
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                    .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f))
            .setLegSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM), .33333f)
                    .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                    .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f))
            .setMainBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState())
            .setSupportBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM))
            .setSlabBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState())
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM))
            .setStoneSlabBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM))
            .setVineChance(.25f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(.4f)
            .setLegVariant(2)
            .setReplacementRate(.95f)
        );

        // Set default for biomes that don't match any of the biome tag lists
        this.defaultVariant = new MineshaftVariantSettings()
            .setBiomeTags(Lists.newArrayList()) // TODO - ensure this list can only be empty if this variant is named default
            .setSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setFloorSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.1f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.1f)
                    .addBlock(Blocks.AIR.getDefaultState(), 0.2f))
            .setBrickSelector(
                new BlockSetSelector()
                    .addBlock(Blocks.STONEBRICK.getDefaultState(), 0.33333f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.MOSSY), 0.33333f)
                    .addBlock(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED), 0.33333f))
            .setLegSelector(BlockSetSelector.from(
                Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK)))
            .setMainBlock(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK))
            .setSupportBlock(Blocks.OAK_FENCE.getDefaultState())
            .setSlabBlock(Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK))
            .setGravelBlock(Blocks.GRAVEL.getDefaultState())
            .setStoneWallBlock(Blocks.COBBLESTONE_WALL.getDefaultState())
            .setStoneSlabBlock(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP))
            .setVineChance(.25f)
            .setSnowChance(0)
            .setCactusChance(0)
            .setDeadBushChance(0)
            .setMushroomChance(0)
            .setLegVariant(1)
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
