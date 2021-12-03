package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.Random;

public class SmallTunnelStairs extends MineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 9,
        MAIN_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SmallTunnelStairs(CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_STAIRS, compoundTag);
    }

    public SmallTunnelStairs(int chunkPieceLen, Random random, BoundingBox blockBox, Direction direction, BetterMineshaftStructureFeature.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_STAIRS, chunkPieceLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
    }

    public static BoundingBox determineBoxPosition(StructurePieceAccessor structurePieceAccessor, Random random, int x, int y, int z, Direction direction) {
        BoundingBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

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
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ() - 1, direction, this.genDepth);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX(), this.boundingBox.minY() + 4, this.boundingBox.maxZ() + 1, direction, this.genDepth);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY() + 4, this.boundingBox.maxZ(), direction, this.genDepth);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 4, this.boundingBox.minZ(), direction, this.genDepth);
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

        for (int i = 0; i < 5; i++) {
            // Fill with air
            this.fill(world, box, 1, i + 1, i, LOCAL_X_END - 1, i + 1 + 2, i, AIR);

            // Place floor blocks
            this.chanceReplaceNonAir(world, box, random, .5f, 1, i, i, LOCAL_X_END - 1, i, i, getMainSelector());
            this.replaceAir(world, box, 1, i, i, LOCAL_X_END - 1, i, i, getMainBlock());

            // Place rails
            if (i == 0) {
                this.placeBlock(world, Blocks.POWERED_RAIL.defaultBlockState().setValue(PoweredRailBlock.POWERED, true).setValue(PoweredRailBlock.SHAPE, RailShape.ASCENDING_NORTH), 2, i + 1, i, box);
            } else if (i == 4) {
                this.placeBlock(world, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH), 2, i + 1, i, box);
            } else {
                this.placeBlock(world, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.ASCENDING_NORTH), 2, i + 1, i, box);
            }
        }

        // Decorations
        this.addBiomeDecorations(world, box, random, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
    }
}
