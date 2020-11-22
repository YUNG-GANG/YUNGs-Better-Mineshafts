package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

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
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> MAIN_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            new BlockSetSelector(Blocks.OAK_PLANKS.getDefaultState())
                .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            new BlockSetSelector(Blocks.DARK_OAK_PLANKS.getDefaultState())
                .addBlock(Blocks.BROWN_TERRACOTTA.getDefaultState(), 0.1f)
                .addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 0.1f)
                .addBlock(Blocks.YELLOW_TERRACOTTA.getDefaultState(), 0.1f)
                .addBlock(Blocks.WHITE_TERRACOTTA.getDefaultState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            new BlockSetSelector(Blocks.JUNGLE_PLANKS.getDefaultState())
                .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.2f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 0.05f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            new BlockSetSelector(Blocks.SPRUCE_PLANKS.getDefaultState())
                .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .25f)
                .addBlock(Blocks.PACKED_ICE.getDefaultState(), .1f)
                .addBlock(Blocks.BLUE_ICE.getDefaultState(), .1f)
                .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.getDefaultState())
                .addBlock(Blocks.BLUE_ICE.getDefaultState(), .4f)
                .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.1f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.getDefaultState())
                .addBlock(Blocks.SAND.getDefaultState(), 0.3f)
                .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.getDefaultState())
                .addBlock(Blocks.RED_SAND.getDefaultState(), 0.3f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.05f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.getDefaultState())
                .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CAVE_AIR.getDefaultState(), 0.2f)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            new BlockSetSelector(Blocks.RED_MUSHROOM_BLOCK.getDefaultState())
                .addBlock(Blocks.MUSHROOM_STEM.getDefaultState(), .33333f)
                .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f)
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Floor block selectors.
     */
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> FLOOR_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            new BlockSetSelector(Blocks.OAK_PLANKS.getDefaultState())
                .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            new BlockSetSelector(Blocks.DARK_OAK_PLANKS.getDefaultState())
                .addBlock(Blocks.BROWN_TERRACOTTA.getDefaultState(), 0.05f)
                .addBlock(Blocks.ORANGE_TERRACOTTA.getDefaultState(), 0.05f)
                .addBlock(Blocks.YELLOW_TERRACOTTA.getDefaultState(), 0.05f)
                .addBlock(Blocks.WHITE_TERRACOTTA.getDefaultState(), 0.05f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), 0.1f)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            new BlockSetSelector(Blocks.JUNGLE_PLANKS.getDefaultState())
                .addBlock(Blocks.MOSSY_COBBLESTONE.getDefaultState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .05f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), .2f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), .05f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), .05f),
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            new BlockSetSelector(Blocks.SPRUCE_PLANKS.getDefaultState())
                .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .25f)
                .addBlock(Blocks.PACKED_ICE.getDefaultState(), .2f)
                .addBlock(Blocks.COBBLESTONE.getDefaultState(), .05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), .1f),
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.getDefaultState())
                .addBlock(Blocks.PACKED_ICE.getDefaultState(), .45f)
                .addBlock(Blocks.BLUE_ICE.getDefaultState(), .45f)
                .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.getDefaultState())
                .addBlock(Blocks.SAND.getDefaultState(), .3f)
                .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), .05f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.getDefaultState())
                .addBlock(Blocks.RED_SAND.getDefaultState(), .3f)
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .05f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), .05f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.getDefaultState())
                .addBlock(Blocks.COBBLESTONE.getDefaultState(), 0.1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.1f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.1f)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            BlockSetSelector.from(Blocks.MYCELIUM.getDefaultState())
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Brick selectors.
     */
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> BRICK_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            new BlockSetSelector(Blocks.OAK_PLANKS.getDefaultState())
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            new BlockSetSelector(Blocks.DARK_OAK_PLANKS.getDefaultState())
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            new BlockSetSelector(Blocks.JUNGLE_PLANKS.getDefaultState())
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .25f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), .25f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), .25f)
                .addBlock(Blocks.CHISELED_STONE_BRICKS.getDefaultState(), .25f)
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            new BlockSetSelector(Blocks.SPRUCE_PLANKS.getDefaultState())
                .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .5f)
                .addBlock(Blocks.PACKED_ICE.getDefaultState(), .25f)
                .addBlock(Blocks.BLUE_ICE.getDefaultState(), .25f)
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.getDefaultState())
                .addBlock(Blocks.PACKED_ICE.getDefaultState(), .45f)
                .addBlock(Blocks.BLUE_ICE.getDefaultState(), .45f)
                .addBlock(Blocks.SNOW_BLOCK.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.getDefaultState())
                .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.getDefaultState())
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), .1f)
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            new BlockSetSelector(Blocks.ACACIA_PLANKS.getDefaultState())
                .addBlock(Blocks.STONE_BRICKS.getDefaultState(), 0.33333f)
                .addBlock(Blocks.MOSSY_STONE_BRICKS.getDefaultState(), 0.33333f)
                .addBlock(Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 0.33333f)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            new BlockSetSelector(Blocks.RED_MUSHROOM_BLOCK.getDefaultState())
                .addBlock(Blocks.MUSHROOM_STEM.getDefaultState(), .33333f)
                .addBlock(Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState(), .33333f)
                .addBlock(Blocks.RED_MUSHROOM_BLOCK.getDefaultState(), .33333f)        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Leg selectors.
     */
    public static Map<BetterMineshaftStructure.Type, BlockSetSelector> LEG_SELECTOR = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            BlockSetSelector.from(Blocks.STRIPPED_OAK_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.MESA,
            BlockSetSelector.from(Blocks.STRIPPED_DARK_OAK_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            BlockSetSelector.from(Blocks.STRIPPED_JUNGLE_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            BlockSetSelector.from(Blocks.STRIPPED_SPRUCE_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.ICE,
            new BlockSetSelector(Blocks.PACKED_ICE.getDefaultState())
                .addBlock(Blocks.BLUE_ICE.getDefaultState(), .5f)
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            new BlockSetSelector(Blocks.SANDSTONE.getDefaultState())
                .addBlock(Blocks.SMOOTH_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CUT_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CHISELED_SANDSTONE.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            new BlockSetSelector(Blocks.RED_SANDSTONE.getDefaultState())
                .addBlock(Blocks.SMOOTH_RED_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CUT_RED_SANDSTONE.getDefaultState(), .15f)
                .addBlock(Blocks.CHISELED_RED_SANDSTONE.getDefaultState(), .1f)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            BlockSetSelector.from(Blocks.STRIPPED_ACACIA_LOG.getDefaultState().with(PillarBlock.AXIS, Direction.Axis.Y))
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            BlockSetSelector.from(Blocks.MUSHROOM_STEM.getDefaultState())
        }
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockSetSelector) data[1]));

    /**
     * Main Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> MAIN_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.OAK_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.DARK_OAK_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.JUNGLE_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SPRUCE_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.PACKED_ICE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.SANDSTONE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.RED_SANDSTONE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.ACACIA_PLANKS.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.RED_MUSHROOM_BLOCK.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Support Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> SUPPORT_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.OAK_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.DARK_OAK_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.JUNGLE_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SPRUCE_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.PACKED_ICE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.SANDSTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.ACACIA_FENCE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.MUSHROOM_STEM.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Slab Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> SLAB_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.OAK_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.DARK_OAK_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.JUNGLE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SPRUCE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.PACKED_ICE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.SANDSTONE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.ACACIA_SLAB.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.BROWN_MUSHROOM.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Gravel Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> GRAVEL_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SNOW_BLOCK.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.SNOW_BLOCK.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.SAND.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.RED_SAND.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.GRAVEL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.GRAVEL.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Stone Wall Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> STONE_WALL_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SNOW_BLOCK.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.PACKED_ICE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.SANDSTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.COBBLESTONE_WALL.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));

    /**
     * Stone Slab Blocks.
     */
    public static Map<BetterMineshaftStructure.Type, BlockState> STONE_SLAB_BLOCK = Stream.of (new Object[][] {
        {
            BetterMineshaftStructure.Type.NORMAL,
            Blocks.STONE_BRICK_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.MESA,
            Blocks.STONE_BRICK_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.JUNGLE,
            Blocks.STONE_BRICK_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.SNOW,
            Blocks.SNOW_BLOCK.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.ICE,
            Blocks.PACKED_ICE.getDefaultState()
        },
        {
            BetterMineshaftStructure.Type.DESERT,
            Blocks.SANDSTONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.RED_DESERT,
            Blocks.RED_SANDSTONE_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.SAVANNA,
            Blocks.STONE_BRICK_SLAB.getDefaultState().with(Properties.SLAB_TYPE, SlabType.TOP)
        },
        {
            BetterMineshaftStructure.Type.MUSHROOM,
            Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState()}
    }).collect(Collectors.toMap(data -> (BetterMineshaftStructure.Type) data[0], data -> (BlockState) data[1]));
}
