package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
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

    public LayeredIntersection4(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_4, compoundTag);
    }

    public LayeredIntersection4(int i, int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_4, i, pieceChainLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = new BlockBox(x, y - 3, z, x, (y - 3) + Y_AXIS_LEN - 1, z);

        switch (direction) {
            case NORTH:
            default:
                blockBox.maxX = x + 5;
                blockBox.minX = x - 1;
                blockBox.minZ = z - (MAIN_AXIS_LEN - 1);
                break;
            case SOUTH:
                blockBox.maxX = x + 1;
                blockBox.minX = x - 5;
                blockBox.maxZ = z + (MAIN_AXIS_LEN - 1);
                break;
            case WEST:
                blockBox.minX = x - (MAIN_AXIS_LEN - 1);
                blockBox.maxZ = z;
                blockBox.minZ = z - 5;
                break;
            case EAST:
                blockBox.maxX = x + (MAIN_AXIS_LEN - 1);
                blockBox.maxZ = z + 4;
                blockBox.minZ = z - 1;
        }

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

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ - 1, Direction.NORTH, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 5, Direction.EAST, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, Direction.WEST, this.method_14923(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ + 1, Direction.SOUTH, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, Direction.EAST, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 5, Direction.WEST, this.method_14923(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ, Direction.WEST, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX - 5, this.boundingBox.minY + 3, this.boundingBox.minZ - 1, Direction.NORTH, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ + 1, Direction.SOUTH, this.method_14923(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, Direction.EAST, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX + 5, this.boundingBox.minY + 3, this.boundingBox.maxZ + 1, Direction.SOUTH, this.method_14923(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ - 1, Direction.NORTH, this.method_14923(), pieceChainLen);
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        // Randomize blocks
        float chance =
            this.mineshaftType == BetterMineshaftFeature.Type.ICE
                || this.mineshaftType == BetterMineshaftFeature.Type.MUSHROOM
            ? .95f
            : .6f;
        this.chanceReplaceNonAir(world, box, random, chance, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

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
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_SOUTH), 3, 3, 0, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_SOUTH), 3, 2, 1, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_SOUTH), 3, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.SHAPE, RailShape.NORTH_SOUTH).with(PoweredRailBlock.POWERED, true), 3, 1, 3, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_NORTH), 3, 1, 4, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_NORTH), 3, 2, 5, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_NORTH), 3, 3, 6, box);

        // Ensure solid block is below each rail
        this.addBlock(world, getMainBlock(), 3, 2, 0, box);
        this.addBlock(world, getMainBlock(), 3, 1, 1, box);
        this.addBlock(world, getMainBlock(), 3, 0, 2, box);
        this.addBlock(world, getMainBlock(), 3, 0, 3, box);
        this.addBlock(world, getMainBlock(), 3, 0, 4, box);
        this.addBlock(world, getMainBlock(), 3, 1, 5, box);
        this.addBlock(world, getMainBlock(), 3, 2, 6, box);

        // Top wood
        this.fill(world, box, 0, 3, 2, 1, 3, 4, getMainBlock());
        this.fill(world, box, 2, 4, 2, 4, 4, 4, getMainBlock());
        this.fill(world, box, 5, 3, 2, 6, 3, 4, getMainBlock());

        // Top rails
        this.chanceAddBlock(world, random, .5f, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.SHAPE, RailShape.EAST_WEST).with(PoweredRailBlock.POWERED, true), 0, 4, 3, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_EAST), 1, 4, 3, box);
        this.chanceFill(world, box, random, .5f, 2, 5, 3, 4, 5, 3, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.EAST_WEST));
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_WEST), 5, 4, 3, box);
        this.chanceAddBlock(world, random, .5f, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.SHAPE, RailShape.EAST_WEST).with(PoweredRailBlock.POWERED, true), 6, 4, 3, box);

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }
}
