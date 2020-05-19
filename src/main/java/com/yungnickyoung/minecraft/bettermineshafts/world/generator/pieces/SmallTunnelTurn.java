package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.RailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SmallTunnelTurn extends MineshaftPiece {
    public enum TurnDirection {
        LEFT(0), RIGHT(1);

        private final int value;

        TurnDirection(int value) {
            this.value = value;
        }

        public static TurnDirection valueOf(int value) {
            return Arrays.stream(values())
                .filter(dir -> dir.value == value)
                .findFirst().get();
        }
    }

    private TurnDirection turnDirection;
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 5,
        MAIN_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SmallTunnelTurn(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_TURN, compoundTag);
        this.turnDirection = TurnDirection.valueOf(compoundTag.getInt("TurnDirection"));
    }

    public SmallTunnelTurn(int i, int chunkPieceLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_TURN, i, chunkPieceLen, type);
        this.setOrientation(direction);
        this.turnDirection = random.nextBoolean() ? TurnDirection.LEFT : TurnDirection.RIGHT;
        this.boundingBox = blockBox;
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
        tag.putInt("TurnDirection", this.turnDirection.value);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.method_14932(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        Direction nextDirection = this.turnDirection == TurnDirection.LEFT ? rotate90Left(direction) : rotate90Right(direction);
        switch (nextDirection) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, nextDirection, this.method_14923(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextDirection, this.method_14923(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, nextDirection, this.method_14923(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, nextDirection, this.method_14923(), pieceChainLen);
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        Direction direction = this.getFacing();

        // Place floor
        this.fillWithOutline(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1, getMainBlock(), getMainBlock(), false);
        this.randomFillWithOutline(world, box, random, .5f, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .2f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, AIR, AIR, true);

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR, AIR, false);

        // Rails
        this.fillWithOutline(world, box, 2, 1, 0, 2, 1, 1, Blocks.RAIL.getDefaultState(), Blocks.RAIL.getDefaultState(), false);

        if (this.turnDirection == TurnDirection.LEFT) {
            if (direction == Direction.NORTH || direction == Direction.EAST) {
                generateLeftTurn(world, random, box);
            }
            else {
                generateRightTurn(world, random, box);
            }
        }
        else {
            if (direction == Direction.NORTH || direction == Direction.EAST) {
                generateRightTurn(world, random, box);
            }
            else {
                generateLeftTurn(world, random, box);
            }
        }

        return true;
    }

    private void generateLeftTurn(IWorld world, Random random, BlockBox box) {
        this.fillWithOutline(world, box, 0, 1, 1, 0, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR, AIR, false);
        this.fillWithOutline(world, box, 0, 0, 0, 0, 0, LOCAL_Z_END - 1, getMainBlock(), getMainBlock(), false);
        this.fillWithOutline(world, box, 2, 1, 2, 2, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.SOUTH_WEST), Blocks.RAIL.getDefaultState(), false);
        this.fillWithOutline(world, box, 0, 1, 2, 1, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), false);
    }

    private void generateRightTurn(IWorld world, Random random, BlockBox box) {
        this.fillWithOutline(world, box, LOCAL_X_END, 1, 1, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR, AIR, false);
        this.fillWithOutline(world, box, LOCAL_X_END, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END - 1, getMainBlock(), getMainBlock(), false);
        this.fillWithOutline(world, box, 2, 1, 2, 2, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.SOUTH_EAST), Blocks.RAIL.getDefaultState(), false);
        this.fillWithOutline(world, box, LOCAL_X_END - 1, 1, 2, LOCAL_X_END, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST), false);
    }

    private static Direction rotate90Right(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.EAST;
            case EAST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.WEST;
            case WEST:
            default:
                return Direction.NORTH;
        }
    }

    private static Direction rotate90Left(Direction direction) {
        switch (direction) {
            case NORTH:
                return Direction.WEST;
            case WEST:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.EAST;
            case EAST:
            default:
                return Direction.NORTH;
        }
    }
}
