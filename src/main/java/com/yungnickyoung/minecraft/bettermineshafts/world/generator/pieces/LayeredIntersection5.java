package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.mixin.BlockBoxAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

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

    public LayeredIntersection5(ServerWorld world, NbtCompound compoundTag) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5, compoundTag);
    }

    public LayeredIntersection5(int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5, pieceChainLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void writeNbt(ServerWorld world, NbtCompound tag) {
        super.writeNbt(world, tag);
    }

    public static BlockBox determineBoxPosition(StructurePiecesHolder structurePiecesHolder, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = new BlockBox(x, y, z, x, y + Y_AXIS_LEN - 1, z);

        switch (direction) {
            case NORTH:
            default:
                ((BlockBoxAccessor) blockBox).setMinZ(x + (SECONDARY_AXIS_LEN - 1));
                ((BlockBoxAccessor) blockBox).setMinY(z - (MAIN_AXIS_LEN - 1));
                break;
            case SOUTH:
                ((BlockBoxAccessor) blockBox).setMaxZ(x - (SECONDARY_AXIS_LEN - 1));
                ((BlockBoxAccessor) blockBox).setMaxY(z + (MAIN_AXIS_LEN - 1));
                break;
            case WEST:
                ((BlockBoxAccessor) blockBox).setMaxZ(x - (MAIN_AXIS_LEN - 1));
                ((BlockBoxAccessor) blockBox).setMinY(z - (SECONDARY_AXIS_LEN - 1));
                break;
            case EAST:
                ((BlockBoxAccessor) blockBox).setMinZ(x + (MAIN_AXIS_LEN - 1));
                ((BlockBoxAccessor) blockBox).setMaxY(z + (SECONDARY_AXIS_LEN - 1));
        }

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePiecesHolder.getIntersecting(blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void fillOpenings(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random) {
        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            case SOUTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinZ() + 1, this.boundingBox.getMinX(), this.boundingBox.getMinY(), Direction.EAST, chainLength);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxZ() - 1, this.boundingBox.getMinX(), this.boundingBox.getMaxY(), Direction.WEST, chainLength);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinZ() + 1, this.boundingBox.getMinX() + 5, this.boundingBox.getMinY(), Direction.EAST, chainLength);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxZ() - 1, this.boundingBox.getMinX() + 5, this.boundingBox.getMaxY(), Direction.WEST, chainLength);
                break;
            case EAST:
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinZ(), this.boundingBox.getMinX(), this.boundingBox.getMaxY() + 1, Direction.SOUTH, chainLength);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxZ(), this.boundingBox.getMinX(), this.boundingBox.getMinY() - 1, Direction.NORTH, chainLength);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinZ(), this.boundingBox.getMinX() + 5, this.boundingBox.getMaxY() + 1, Direction.SOUTH, chainLength);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxZ(), this.boundingBox.getMinX() + 5, this.boundingBox.getMinY() - 1, Direction.NORTH, chainLength);
                break;
        }
    }

    @Override
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isTouchingLiquid(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

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
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.NORTH_SOUTH), 2, 1, 0, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.NORTH_SOUTH), 2, 1, 1, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), 0, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), 1, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), 3, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), 4, 1, 2, box);

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
        BlockState LADDER = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.SOUTH);
        this.fill(world, box, 2, 1, 3, 2, 5, 3, LADDER);

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }
}
