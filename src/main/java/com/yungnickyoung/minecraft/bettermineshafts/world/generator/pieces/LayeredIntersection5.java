package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockRail;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

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

    public LayeredIntersection5() {}

    public LayeredIntersection5(int i, int pieceChainLen, Random random, StructureBoundingBox blockBox, EnumFacing direction, MapGenBetterMineshaft.Type type) {
        super(i, pieceChainLen, type);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    public static StructureBoundingBox determineBoxPosition(List<StructureComponent> list, Random random, int x, int y, int z, EnumFacing direction) {
        StructureBoundingBox blockBox = new StructureBoundingBox(x, y, z, x, y + Y_AXIS_LEN - 1, z);

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
        StructureComponent intersectingPiece = StructureComponent.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructureComponent structurePiece, List<StructureComponent> list, Random random) {
        EnumFacing direction = this.getCoordBaseMode();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            case SOUTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, EnumFacing.WEST, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 5, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 5, this.boundingBox.maxZ, EnumFacing.WEST, this.getComponentType(), pieceChainLen);
                break;
            case EAST:
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY + 5, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType(), pieceChainLen);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY + 5, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType(), pieceChainLen);
                break;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        // Don't spawn if liquid in this box
        if (this.isLiquidInStructureBoundingBox(world, box)) {
            return false;
        }

        // Randomize blocks
        float chance =
            this.mineshaftType == MapGenBetterMineshaft.Type.ICE
                || this.mineshaftType == MapGenBetterMineshaft.Type.MUSHROOM
            ? .95f
            : .6f;
        this.chanceReplaceNonAir(world, box, random, chance, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, chance, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR);
        this.fill(world, box, 0, 5, LOCAL_Z_END, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        // First floor Bottom - fill in any air in floor with main block
        this.fill(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1, getMainBlock());

        // First floor rails
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH), 2, 1, 0, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH), 2, 1, 1, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST), 0, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST), 1, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST), 3, 1, 2, box);
        this.chanceAddBlock(world, random, .5f, Blocks.RAIL.getDefaultState().withProperty(BlockRail.SHAPE, BlockRailBase.EnumRailDirection.EAST_WEST), 4, 1, 2, box);

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
        IBlockState LADDER = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.SOUTH);
        this.fill(world, box, 2, 1, 3, 2, 5, 3, LADDER);

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }
}
