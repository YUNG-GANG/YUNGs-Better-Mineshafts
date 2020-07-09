package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.RailBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
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

    public SmallTunnelStairs(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_STAIRS, compoundTag);
    }

    public SmallTunnelStairs(int i, int chunkPieceLen, Random random, MutableBoundingBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL_STAIRS, i, chunkPieceLen, type);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
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

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ - 1, direction, this.getComponentType(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY + 4, this.boundingBox.maxZ + 1, direction, this.getComponentType(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 4, this.boundingBox.maxZ, direction, this.getComponentType(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 4, this.boundingBox.minZ, direction, this.getComponentType(), pieceChainLen);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random random, MutableBoundingBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box
        if (this.isLiquidInStructureBoundingBox(world, box)) {
            return false;
        }

        // Randomize blocks
        float chance =
            this.mineshaftType == BetterMineshaftStructure.Type.ICE
                || this.mineshaftType == BetterMineshaftStructure.Type.MUSHROOM
            ? .95f
            : .6f;
        this.chanceReplaceNonAir(world, box, random, chance, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        for (int i = 0; i < 5 ; i++) {
            // Fill with air
            this.fill(world, box, 1, i + 1, i, LOCAL_X_END - 1, i + 1 + 2, i, CAVE_AIR);

            // Place floor blocks
            this.chanceReplaceNonAir(world, box, random, .5f, 1, i, i, LOCAL_X_END - 1, i, i, getMainSelector());

            // Place rails
            if (i == 0) {
                this.setBlockState(world, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.POWERED, true).with(PoweredRailBlock.SHAPE, RailShape.ASCENDING_NORTH), 2, i + 1, i, box);
            } else if (i == 4) {
                this.setBlockState(world, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.NORTH_SOUTH), 2, i + 1, i, box);
            } else {
                this.setBlockState(world, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.ASCENDING_NORTH), 2, i + 1, i, box);
            }

            // Ensure solid block is below each rail
            this.setBlockState(world, getMainBlock(), 2, i, i, box);
        }

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }
}
