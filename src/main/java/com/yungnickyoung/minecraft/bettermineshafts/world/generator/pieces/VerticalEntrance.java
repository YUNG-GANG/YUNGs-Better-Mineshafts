package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class VerticalEntrance extends MineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 7,
        Y_AXIS_LEN = 30,
        MAIN_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public VerticalEntrance(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, compoundTag);
    }

    public VerticalEntrance(int i, int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, i, pieceChainLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    public VerticalEntrance(int i, int pieceChainLen, Random random, BlockPos pos, Direction direction, BetterMineshaftFeature.Type type) {
        this(i, pieceChainLen, random, determineInitialBoxPosition(random, pos.getX(), pos.getY(), pos.getZ(), direction), direction, type);
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
    }

    public static BlockBox determineInitialBoxPosition(Random random, int x, int y, int z, Direction direction) {
        return BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);
    }

    @Override
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        // Extend big tunnel in same direction
        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, this.method_14923(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, this.method_14923(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, this.method_14923(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, this.method_14923(), pieceChainLen);
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        // Fill with stone then clean out with air
        this.fillWithOutline(world, box, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), false);
        this.fillWithOutline(world, box, 2, 1, 1, LOCAL_X_END - 2, LOCAL_Y_END, LOCAL_Z_END - 1, AIR, AIR, false);

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END, getMainBlock(), getMainBlock(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END, AIR, AIR, true);

        // Add random blocks in floor
        this.randomFillWithOutline(world, box, random, .4f, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1, getMainBlock(), AIR, false);

        // Doorway
        this.fillWithOutline(world, box, 0, 0, LOCAL_Z_END, LOCAL_X_END, 6, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.fillWithOutline(world, box, 2, 1, LOCAL_Z_END, 4, 3, LOCAL_Z_END, AIR, AIR, true);

        return true;
    }
}
