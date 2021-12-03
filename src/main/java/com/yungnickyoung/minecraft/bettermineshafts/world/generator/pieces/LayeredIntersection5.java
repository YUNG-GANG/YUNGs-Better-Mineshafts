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
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.Random;

public class LayeredIntersection5 extends MineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 10,
        MAIN_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public LayeredIntersection5(CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5, compoundTag);
    }

    public LayeredIntersection5(int pieceChainLen, Random random, BoundingBox blockBox, Direction direction, BetterMineshaftStructureFeature.Type type) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5, pieceChainLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
    }

    public static BoundingBox determineBoxPosition(StructurePieceAccessor structurePieceAccessor, Random random, int x, int y, int z, Direction direction) {
        BoundingBox blockBox = new BoundingBox(x, y, z, x, y + Y_AXIS_LEN - 1, z);

        switch (direction) {
            case NORTH:
            default:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + (SECONDARY_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMinZ(z - (MAIN_AXIS_LEN - 1));
                break;
            case SOUTH:
                ((BoundingBoxAccessor) blockBox).setMinX(x - (SECONDARY_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMaxZ(z + (MAIN_AXIS_LEN - 1));
                break;
            case WEST:
                ((BoundingBoxAccessor) blockBox).setMinX(x - (MAIN_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMinZ(z - (SECONDARY_AXIS_LEN - 1));
                break;
            case EAST:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + (MAIN_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMaxZ(z + (SECONDARY_AXIS_LEN - 1));
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
            case SOUTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ(), Direction.WEST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 5, this.boundingBox.minZ(), Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 5, this.boundingBox.maxZ(), Direction.WEST, this.genDepth);
                break;
            case EAST:
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX(), this.boundingBox.minY() + 5, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() + 5, this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
                break;
        }
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
//        if (this.isTouchingLiquid(world, box)) return;
//        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return;

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR);
        this.fill(world, box, 0, 5, LOCAL_Z_END, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        // First floor Bottom - fill in any air in floor with main block
        this.replaceAir(world, box, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END - 1, getMainBlock());

        // First floor rails
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH), 2, 1, 0, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH), 2, 1, 1, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST), 0, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST), 1, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST), 3, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST), 4, 1, 2, box);

        // Second floor bottom
        this.fill(world, box, 0, 5, 0, LOCAL_X_END, 5, LOCAL_Z_END, getMainBlock());
        this.chanceReplaceNonAir(world, box, random, .5f, 0, 5, 0, LOCAL_X_END, 5, LOCAL_Z_END, getMainSelector());
        this.fill(world, box, 1, 5, 1, LOCAL_X_END - 1, 5, LOCAL_Z_END - 1, AIR);

        // Supports
        this.fill(world, box, 1, 1, 1, 1, LOCAL_Y_END - 1, 1, getSupportBlock());
        this.fill(world, box, 3, 1, 1, 3, LOCAL_Y_END - 1, 1, getSupportBlock());
        this.fill(world, box, 1, 1, 3, 1, LOCAL_Y_END - 1, 3, getSupportBlock());
        this.fill(world, box, 3, 1, 3, 3, LOCAL_Y_END - 1, 3, getSupportBlock());

        // Ladders
        BlockState LADDER = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.SOUTH);
        this.fill(world, box, 2, 1, 3, 2, 5, 3, LADDER);

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
    }
}
