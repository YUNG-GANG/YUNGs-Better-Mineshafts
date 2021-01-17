package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
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

public class LayeredIntersection5 extends MineshaftPiece{
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 10,
        MAIN_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public LayeredIntersection5(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5, compoundTag);
    }

    public LayeredIntersection5(int i, int pieceChainLen, Random random, MutableBoundingBox blockBox, Direction direction, MineshaftVariantSettings settings) {
        super(BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5, i, pieceChainLen, settings);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
    }

    public static MutableBoundingBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        MutableBoundingBox blockBox = new MutableBoundingBox(x, y, z, x, y + Y_AXIS_LEN - 1, z);

        switch (direction) {
            case NORTH:
            default:
                blockBox.maxX = x + (SECONDARY_AXIS_LEN - 1);
                blockBox.minZ = z - (MAIN_AXIS_LEN - 1);
                break;
            case SOUTH:
                blockBox.minX = x - (SECONDARY_AXIS_LEN - 1);
                blockBox.maxZ = z + (MAIN_AXIS_LEN - 1);
                break;
            case WEST:
                blockBox.minX = x - (MAIN_AXIS_LEN - 1);
                blockBox.minZ = z - (SECONDARY_AXIS_LEN - 1);
                break;
            case EAST:
                blockBox.maxX = x + (MAIN_AXIS_LEN - 1);
                blockBox.maxZ = z + (SECONDARY_AXIS_LEN - 1);
        }

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
            case SOUTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.EAST, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, Direction.WEST, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 5, this.boundingBox.minZ, Direction.EAST, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 5, this.boundingBox.maxZ, Direction.WEST, this.getComponentType(), pieceChainLen);
                break;
            case EAST:
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY + 5, this.boundingBox.maxZ + 1, Direction.SOUTH, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY + 5, this.boundingBox.minZ - 1, Direction.NORTH, this.getComponentType(), pieceChainLen);
                break;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random random, MutableBoundingBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isLiquidInStructureBoundingBox(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, settings.replacementRate, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, settings.replacementRate, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END - 1, CAVE_AIR);
        this.fill(world, box, 0, 5, LOCAL_Z_END, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END, CAVE_AIR);

        // First floor Bottom - fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1, getMainBlock());

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
        this.fill(world, box, 1, 5, 1, LOCAL_X_END - 1, 5, LOCAL_Z_END - 1, CAVE_AIR);

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
        this.addVines(world, box, random, settings.vineChance, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }
}
