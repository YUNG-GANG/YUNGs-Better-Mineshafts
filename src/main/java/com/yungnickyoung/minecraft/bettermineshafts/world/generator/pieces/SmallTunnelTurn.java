package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.RailBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
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

    public SmallTunnelTurn(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_TURN, compoundTag);
        this.turnDirection = TurnDirection.valueOf(compoundTag.getInt("TurnDirection"));
    }

    public SmallTunnelTurn(int i, int chunkPieceLen, Random random, MutableBoundingBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_TURN, i, chunkPieceLen, type);
        this.setCoordBaseMode(direction);
        this.turnDirection = random.nextBoolean() ? TurnDirection.LEFT : TurnDirection.RIGHT;
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
        tag.putInt("TurnDirection", this.turnDirection.value);
    }

    public static MutableBoundingBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        MutableBoundingBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        Direction direction = this.getCoordBaseMode();
        if (direction == null) {
            return;
        }

        Direction nextDirection = this.turnDirection == TurnDirection.LEFT ? direction.rotateYCCW() : direction.rotateY();
        switch (nextDirection) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, nextDirection, this.getComponentType(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextDirection, this.getComponentType(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, nextDirection, this.getComponentType(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, nextDirection, this.getComponentType(), pieceChainLen);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean create(IWorld world, ChunkGenerator<?> generator, Random random, MutableBoundingBox box, ChunkPos pos) {
        if (this.isLiquidInStructureBoundingBox(world, box)) {
            return false;
        }

        Direction direction = this.getCoordBaseMode();

        // Randomize blocks
        float chance =
            this.mineshaftType == BetterMineshaftStructure.Type.ICE
                || this.mineshaftType == BetterMineshaftStructure.Type.MUSHROOM
            ? .95f
            : .6f;
        this.chanceReplaceNonAir(world, box, random, chance, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, CAVE_AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());
        // Special case - mushroom mineshafts get mycelium in floor
        if (this.mineshaftType == BetterMineshaftStructure.Type.MUSHROOM)
            this.chanceReplaceNonAir(world, box, random, .8f, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, Blocks.MYCELIUM.getDefaultState());

        // Rails
        this.fill(world, box, 2, 1, 0, 2, 1, 1, Blocks.RAIL.getDefaultState());

        if (this.turnDirection == TurnDirection.LEFT) {
            if (direction == Direction.NORTH || direction == Direction.EAST) {
                generateLeftTurn(world, box);
            }
            else {
                generateRightTurn(world, box);
            }
        }
        else {
            if (direction == Direction.NORTH || direction == Direction.EAST) {
                generateRightTurn(world, box);
            }
            else {
                generateLeftTurn(world, box);
            }
        }

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }

    private void generateLeftTurn(IWorld world, MutableBoundingBox box) {
        this.fill(world, box, 0, 1, 1, 0, LOCAL_Y_END - 1, LOCAL_Z_END - 1, CAVE_AIR);
        this.fill(world, box, 0, 0, 0, 0, 0, LOCAL_Z_END - 1, getMainBlock());
        this.fill(world, box, 2, 1, 2, 2, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.SOUTH_WEST));
        this.fill(world, box, 0, 1, 2, 1, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST));
    }

    private void generateRightTurn(IWorld world, MutableBoundingBox box) {
        this.fill(world, box, LOCAL_X_END, 1, 1, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END - 1, CAVE_AIR);
        this.fill(world, box, LOCAL_X_END, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END - 1, getMainBlock());
        this.fill(world, box, 2, 1, 2, 2, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.SOUTH_EAST));
        this.fill(world, box, LOCAL_X_END - 1, 1, 2, LOCAL_X_END, 1, 2, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST));
    }
}
