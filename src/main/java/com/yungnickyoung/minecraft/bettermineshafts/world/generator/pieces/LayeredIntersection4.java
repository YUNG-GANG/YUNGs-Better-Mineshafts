package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.mixin.BoundingBoxAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.Random;

public class LayeredIntersection4 extends MineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 7,
        Y_AXIS_LEN = 9,
        MAIN_AXIS_LEN = 7;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public LayeredIntersection4(CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_4, compoundTag);
    }

    public LayeredIntersection4(int chainLength, Random random, BoundingBox blockBox, Direction direction, BetterMineshaftStructureFeature.Type type) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_4, chainLength, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
    }

    public static BoundingBox determineBoxPosition(StructurePieceAccessor structurePieceAccessor, Random random, int x, int y, int z, Direction direction) {
        BoundingBox blockBox = new BoundingBox(x, y - 3, z, x, (y - 3) + Y_AXIS_LEN - 1, z);
        switch (direction) {
            case NORTH:
            default:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + 5);
                ((BoundingBoxAccessor) blockBox).setMinX(x - 1);
                ((BoundingBoxAccessor) blockBox).setMinZ(z - (MAIN_AXIS_LEN - 1));
                break;
            case SOUTH:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + 1);
                ((BoundingBoxAccessor) blockBox).setMinX(x - 5);
                ((BoundingBoxAccessor) blockBox).setMaxZ(z + (MAIN_AXIS_LEN - 1));
                break;
            case WEST:
                ((BoundingBoxAccessor) blockBox).setMinX(x - (MAIN_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMaxZ(z);
                ((BoundingBoxAccessor) blockBox).setMinZ(z - 5);
                break;
            case EAST:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + (MAIN_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMaxZ(z + 4);
                ((BoundingBoxAccessor) blockBox).setMinZ(z - 1);
        }

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePieceAccessor.findCollisionPiece(blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random) {
        Direction direction = this.getOrientation();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 5, Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, Direction.WEST, this.genDepth);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 5, Direction.WEST, this.genDepth);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ(), Direction.WEST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() - 5, this.boundingBox.minY() + 3, this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + 5, this.boundingBox.minY() + 3, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
        }
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isTouchingLiquid(world, box)) return;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return;

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 2, 1, 2, 4, 1, 4, AIR);
        this.fill(world, box, 2, 2, 1, 4, 2, 5, AIR);
        this.fill(world, box, 2, 3, 0, 4, 6, 6, AIR);
        this.fill(world, box, 0, 3, 2, 6, 6, 4, AIR);

        // Fill in any air in floor with main block
        this.replaceNonAir(world, box, 2, 0, 0, 4, 2, LOCAL_Z_END, getMainBlock());

        // Sides for walking on
        this.fill(world, box, 2, 2, 0, 2, 3, 1, getMainBlock());
        this.chanceReplaceNonAir(world, box, random, .1f, 2, 2, 0, 2, 3, 1, getBrickSelector());
        this.fill(world, box, 4, 2, 0, 4, 3, 1, getMainBlock());
        this.chanceReplaceNonAir(world, box, random, .1f, 4, 2, 0, 4, 3, 1, getBrickSelector());
        this.fill(world, box, 2, 2, 5, 2, 3, 6, getMainBlock());
        this.chanceReplaceNonAir(world, box, random, .1f, 2, 2, 5, 2, 3, 6, getBrickSelector());
        this.fill(world, box, 4, 2, 5, 4, 3, 6, getMainBlock());
        this.chanceReplaceNonAir(world, box, random, .1f, 4, 2, 5, 4, 3, 6, getBrickSelector());

        // Bottom rails
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH), 3, 3, 0, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH), 3, 2, 1, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH), 3, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.POWERED_RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH).setValue(BlockStateProperties.POWERED, true), 3, 1, 3, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH), 3, 1, 4, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH), 3, 2, 5, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH), 3, 3, 6, box);

        // Ensure solid block is below each rail
        this.placeBlock(world, getMainBlock(), 3, 2, 0, box);
        this.placeBlock(world, getMainBlock(), 3, 1, 1, box);
        this.placeBlock(world, getMainBlock(), 3, 0, 2, box);
        this.placeBlock(world, getMainBlock(), 3, 0, 3, box);
        this.placeBlock(world, getMainBlock(), 3, 0, 4, box);
        this.placeBlock(world, getMainBlock(), 3, 1, 5, box);
        this.placeBlock(world, getMainBlock(), 3, 2, 6, box);

        // Top wood
        this.fill(world, box, 0, 3, 2, 1, 3, 4, getMainBlock());
        this.fill(world, box, 2, 4, 2, 4, 4, 4, getMainBlock());
        this.fill(world, box, 5, 3, 2, 6, 3, 4, getMainBlock());

        // Top rails
        this.chanceAddBlock(world, random, .5f, Blocks.POWERED_RAIL.defaultBlockState().setValue(PoweredRailBlock.SHAPE, RailShape.EAST_WEST).setValue(PoweredRailBlock.POWERED, true), 0, 4, 3, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.ASCENDING_EAST), 1, 4, 3, box);
        this.chanceFill(world, box, random, .5f, 2, 5, 3, 4, 5, 3, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST));
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.ASCENDING_WEST), 5, 4, 3, box);
        this.chanceAddBlock(world, random, .5f, Blocks.POWERED_RAIL.defaultBlockState().setValue(PoweredRailBlock.SHAPE, RailShape.EAST_WEST).setValue(PoweredRailBlock.POWERED, true), 6, 4, 3, box);

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
    }
}
