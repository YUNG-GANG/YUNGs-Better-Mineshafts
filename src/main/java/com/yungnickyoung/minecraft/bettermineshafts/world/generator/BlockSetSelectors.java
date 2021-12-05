package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A collection of static maps of mineshaft type to the corresponding BlockSetSelector.
 * It's ugly, but it works.
 * I'm too lazy to reimplement the sexy architecture from 1.12.2, so this is what you get instead.
 *
 * Sorry.
 */
public class BlockSetSelectors {
    /**
     * Main block selectors.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockSetSelector> MAIN_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            new BlockSetSelector(Blocks.OAK_PLANKS.defaultBlockState())
                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f)
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            new BlockSetSelector(Blocks.DARK_OAK_PLANKS.defaultBlockState())
                .addBlock(Blocks.BROWN_TERRACOTTA.defaultBlockState(), 0.1f)
                .addBlock(Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 0.1f)
                .addBlock(Blocks.YELLOW_TERRACOTTA.defaultBlockState(), 0.1f)
                .addBlock(Blocks.WHITE_TERRACOTTA.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f)
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            new BlockSetSelector(Blocks.JUNGLE_PLANKS.defaultBlockState())
                .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.2f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 0.05f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f)
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), .25f)
                .addBlock(Blocks.PACKED_ICE.defaultBlockState(), .1f)
                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .1f)
                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f)
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .4f)
                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.1f)
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                .addBlock(Blocks.SAND.defaultBlockState(), 0.3f)
                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f)
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                .addBlock(Blocks.RED_SAND.defaultBlockState(), 0.3f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.05f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f)
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.defaultBlockState())
                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.defaultBlockState(), 0.2f)
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            new BlockSetSelector(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                .addBlock(Blocks.MUSHROOM_STEM.defaultBlockState(), .33333f)
                .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState(), .33333f)
                .addBlock(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), .33333f)
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Floor block selectors.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockSetSelector> FLOOR_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            new BlockSetSelector(Blocks.OAK_PLANKS.defaultBlockState())
                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            new BlockSetSelector(Blocks.DARK_OAK_PLANKS.defaultBlockState())
                .addBlock(Blocks.BROWN_TERRACOTTA.defaultBlockState(), 0.05f)
                .addBlock(Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 0.05f)
                .addBlock(Blocks.YELLOW_TERRACOTTA.defaultBlockState(), 0.05f)
                .addBlock(Blocks.WHITE_TERRACOTTA.defaultBlockState(), 0.05f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 0.1f)
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            new BlockSetSelector(Blocks.JUNGLE_PLANKS.defaultBlockState())
                .addBlock(Blocks.MOSSY_COBBLESTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .05f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), .2f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), .05f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), .05f),
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), .25f)
                .addBlock(Blocks.PACKED_ICE.defaultBlockState(), .2f)
                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), .05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), .1f),
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                .addBlock(Blocks.PACKED_ICE.defaultBlockState(), .45f)
                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .45f)
                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), .1f)
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                .addBlock(Blocks.SAND.defaultBlockState(), .3f)
                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), .05f)
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                .addBlock(Blocks.RED_SAND.defaultBlockState(), .3f)
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), .05f)
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.defaultBlockState())
                .addBlock(Blocks.COBBLESTONE.defaultBlockState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.1f)
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            BlockSetSelector.from(Blocks.MYCELIUM.defaultBlockState())
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Brick selectors.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockSetSelector> BRICK_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            new BlockSetSelector(Blocks.OAK_PLANKS.defaultBlockState())
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.33333f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.33333f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.33333f)
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            new BlockSetSelector(Blocks.DARK_OAK_PLANKS.defaultBlockState())
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.33333f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.33333f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.33333f)
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            new BlockSetSelector(Blocks.JUNGLE_PLANKS.defaultBlockState())
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .25f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), .25f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), .25f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), .25f)
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            new BlockSetSelector(Blocks.SPRUCE_PLANKS.defaultBlockState())
                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), .5f)
                .addBlock(Blocks.PACKED_ICE.defaultBlockState(), .25f)
                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .25f)
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                .addBlock(Blocks.PACKED_ICE.defaultBlockState(), .45f)
                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .45f)
                .addBlock(Blocks.SNOW_BLOCK.defaultBlockState(), .1f)
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .1f)
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), .1f)
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.defaultBlockState())
                .addBlock(Blocks.STONE_BRICKS.defaultBlockState(), 0.33333f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.33333f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.33333f)
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            new BlockSetSelector(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState())
                .addBlock(Blocks.MUSHROOM_STEM.defaultBlockState(), .33333f)
                .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState(), .33333f)
                .addBlock(Blocks.RED_MUSHROOM_BLOCK.defaultBlockState(), .33333f)        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Leg selectors.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockSetSelector> LEG_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            BlockSetSelector.from(Blocks.STRIPPED_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            BlockSetSelector.from(Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            BlockSetSelector.from(Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            BlockSetSelector.from(Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.defaultBlockState())
                .addBlock(Blocks.BLUE_ICE.defaultBlockState(), .5f)
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.defaultBlockState())
                .addBlock(Blocks.SMOOTH_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CUT_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CHISELED_SANDSTONE.defaultBlockState(), .1f)
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.defaultBlockState())
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.defaultBlockState(), .15f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.defaultBlockState(), .1f)
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            BlockSetSelector.from(Blocks.STRIPPED_ACACIA_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            BlockSetSelector.from(Blocks.MUSHROOM_STEM.defaultBlockState())
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Main Blocks.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockState> MAIN_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            Blocks.OAK_PLANKS.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            Blocks.DARK_OAK_PLANKS.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            Blocks.JUNGLE_PLANKS.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            Blocks.SPRUCE_PLANKS.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            Blocks.PACKED_ICE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            Blocks.SANDSTONE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            Blocks.RED_SANDSTONE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            Blocks.ACACIA_PLANKS.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            Blocks.RED_MUSHROOM_BLOCK.defaultBlockState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Support Blocks.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockState> SUPPORT_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            Blocks.OAK_FENCE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            Blocks.DARK_OAK_FENCE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            Blocks.JUNGLE_FENCE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            Blocks.SPRUCE_FENCE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            Blocks.PACKED_ICE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            Blocks.SANDSTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            Blocks.ACACIA_FENCE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            Blocks.MUSHROOM_STEM.defaultBlockState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Slab Blocks.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockState> SLAB_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            Blocks.OAK_SLAB.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            Blocks.DARK_OAK_SLAB.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            Blocks.JUNGLE_SLAB.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            Blocks.SPRUCE_SLAB.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            Blocks.PACKED_ICE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            Blocks.SANDSTONE_SLAB.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_SLAB.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            Blocks.ACACIA_SLAB.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            Blocks.BROWN_MUSHROOM.defaultBlockState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Gravel Blocks.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockState> GRAVEL_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            Blocks.GRAVEL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            Blocks.GRAVEL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            Blocks.GRAVEL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            Blocks.SNOW_BLOCK.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            Blocks.SNOW_BLOCK.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            Blocks.SAND.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            Blocks.RED_SAND.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            Blocks.GRAVEL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            Blocks.GRAVEL.defaultBlockState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Stone Wall Blocks.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockState> STONE_WALL_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            Blocks.COBBLESTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            Blocks.COBBLESTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            Blocks.COBBLESTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            Blocks.SNOW_BLOCK.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            Blocks.PACKED_ICE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            Blocks.SANDSTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            Blocks.COBBLESTONE_WALL.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Stone Slab Blocks.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockState> STONE_SLAB_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructureFeature.Type.NORMAL,
            Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructureFeature.Type.MESA,
            Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructureFeature.Type.JUNGLE,
            Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructureFeature.Type.SNOW,
            Blocks.SNOW_BLOCK.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.ICE,
            Blocks.PACKED_ICE.defaultBlockState()
        },
        {
            BetterMineshaftStructureFeature.Type.DESERT,
            Blocks.SANDSTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructureFeature.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructureFeature.Type.SAVANNA,
            Blocks.STONE_BRICK_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructureFeature.Type.MUSHROOM,
            Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Main Blocks.
     */
    public static Map<BetterMineshaftStructureFeature.Type, BlockState> SMALL_LEG_BLOCK = Stream.of (new Object[][] {
        {
                BetterMineshaftStructureFeature.Type.NORMAL,
                Blocks.STRIPPED_OAK_LOG.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.MESA,
                Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.JUNGLE,
                Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.SNOW,
                Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.ICE,
                Blocks.PACKED_ICE.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.DESERT,
                Blocks.SMOOTH_SANDSTONE.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.RED_DESERT,
                Blocks.SMOOTH_RED_SANDSTONE.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.SAVANNA,
                Blocks.STRIPPED_ACACIA_LOG.defaultBlockState()
        },
        {
                BetterMineshaftStructureFeature.Type.MUSHROOM,
                Blocks.MUSHROOM_STEM.defaultBlockState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructureFeature.Type) data[0], data -> (BlockState) data[1]));
}
