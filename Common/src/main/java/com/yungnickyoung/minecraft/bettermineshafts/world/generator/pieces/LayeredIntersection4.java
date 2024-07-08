package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.mixin.BoundingBoxAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.BetterMineshaftConfiguration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructurePieceTypeModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class LayeredIntersection4 extends BetterMineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 7,
        Y_AXIS_LEN = 9,
        MAIN_AXIS_LEN = 7;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public LayeredIntersection4(CompoundTag compoundTag) {
        super(StructurePieceTypeModule.LAYERED_INTERSECTION_4, compoundTag);
    }

    public LayeredIntersection4(int chainLength, BoundingBox blockBox, Direction direction, BetterMineshaftConfiguration config) {
        super(StructurePieceTypeModule.LAYERED_INTERSECTION_4, chainLength, config, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
    }

    public static BoundingBox determineBoxPosition(StructurePieceAccessor structurePieceAccessor, int x, int y, int z, Direction direction) {
        BoundingBox blockBox = new BoundingBox(x, y - 3, z, x, (y - 3) + Y_AXIS_LEN - 1, z);
        BoundingBoxAccessor blockBoxAccessor = (BoundingBoxAccessor) blockBox;
        switch (direction) {
            case NORTH:
            default:
                blockBoxAccessor.setMaxX(x + 5);
                blockBoxAccessor.setMinX(x - 1);
                blockBoxAccessor.setMinZ(z - (MAIN_AXIS_LEN - 1));
                break;
            case SOUTH:
                blockBoxAccessor.setMaxX(x + 1);
                blockBoxAccessor.setMinX(x - 5);
                blockBoxAccessor.setMaxZ(z + (MAIN_AXIS_LEN - 1));
                break;
            case WEST:
                blockBoxAccessor.setMinX(x - (MAIN_AXIS_LEN - 1));
                blockBoxAccessor.setMaxZ(z);
                blockBoxAccessor.setMinZ(z - 5);
                break;
            case EAST:
                blockBoxAccessor.setMaxX(x + (MAIN_AXIS_LEN - 1));
                blockBoxAccessor.setMaxZ(z + 4);
                blockBoxAccessor.setMinZ(z - 1);
        }

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePieceAccessor.findCollisionPiece(blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, RandomSource randomSource) {
        Direction direction = this.getOrientation();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 5, Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, Direction.WEST, this.genDepth);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 5, Direction.WEST, this.genDepth);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ(), Direction.WEST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.maxX() - 5, this.boundingBox.minY() + 3, this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, Direction.EAST, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.minX() + 5, this.boundingBox.minY() + 3, this.boundingBox.maxZ() + 1, Direction.SOUTH, this.genDepth);
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, randomSource, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() - 1, Direction.NORTH, this.genDepth);
        }
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Randomize blocks
        this.chanceReplaceNonAir(world, box, randomSource, config.replacementRate, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, config.blockStateRandomizers.mainRandomizer);

        // Randomize floor
        this.chanceReplaceNonAir(world, box, randomSource, config.replacementRate, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, config.blockStateRandomizers.floorRandomizer);

        // Fill with air
        this.fill(world, box, 2, 1, 2, 4, 1, 4, AIR);
        this.fill(world, box, 2, 2, 1, 4, 2, 5, AIR);
        this.fill(world, box, 2, 3, 0, 4, 6, 6, AIR);
        this.fill(world, box, 0, 3, 2, 6, 6, 4, AIR);

        // Sides for walking on
        this.fill(world, box, 2, 2, 0, 2, 3, 1, config.blockStates.mainBlockState);
        this.chanceReplaceNonAir(world, box, randomSource, .1f, 2, 2, 0, 2, 3, 1, config.blockStateRandomizers.brickRandomizer);
        this.fill(world, box, 4, 2, 0, 4, 3, 1, config.blockStates.mainBlockState);
        this.chanceReplaceNonAir(world, box, randomSource, .1f, 4, 2, 0, 4, 3, 1, config.blockStateRandomizers.brickRandomizer);
        this.fill(world, box, 2, 2, 5, 2, 3, 6, config.blockStates.mainBlockState);
        this.chanceReplaceNonAir(world, box, randomSource, .1f, 2, 2, 5, 2, 3, 6, config.blockStateRandomizers.brickRandomizer);
        this.fill(world, box, 4, 2, 5, 4, 3, 6, config.blockStates.mainBlockState);
        this.chanceReplaceNonAir(world, box, randomSource, .1f, 4, 2, 5, 4, 3, 6, config.blockStateRandomizers.brickRandomizer);

        // Ensure solid block is below each rail
        this.placeBlock(world, config.blockStates.mainBlockState, 3, 2, 0, box);
        this.placeBlock(world, config.blockStates.mainBlockState, 3, 1, 1, box);
        this.placeBlock(world, config.blockStates.mainBlockState, 3, 0, 2, box);
        this.placeBlock(world, config.blockStates.mainBlockState, 3, 0, 3, box);
        this.placeBlock(world, config.blockStates.mainBlockState, 3, 0, 4, box);
        this.placeBlock(world, config.blockStates.mainBlockState, 3, 1, 5, box);
        this.placeBlock(world, config.blockStates.mainBlockState, 3, 2, 6, box);

        // Bottom rails
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH), 3, 3, 0, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH), 3, 2, 1, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_SOUTH), 3, 1, 2, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.POWERED_RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE_STRAIGHT, RailShape.NORTH_SOUTH).setValue(BlockStateProperties.POWERED, true), 3, 1, 3, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH), 3, 1, 4, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH), 3, 2, 5, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_NORTH), 3, 3, 6, box);

        // Top wood
        this.fill(world, box, 0, 3, 2, 1, 3, 4, config.blockStates.mainBlockState);
        this.fill(world, box, 2, 4, 2, 4, 4, 4, config.blockStates.mainBlockState);
        this.fill(world, box, 5, 3, 2, 6, 3, 4, config.blockStates.mainBlockState);

        // Top rails
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.POWERED_RAIL.defaultBlockState().setValue(PoweredRailBlock.SHAPE, RailShape.EAST_WEST).setValue(PoweredRailBlock.POWERED, true), 0, 4, 3, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.ASCENDING_EAST), 1, 4, 3, box);
        this.chanceFill(world, box, randomSource, .5f, 2, 5, 3, 4, 5, 3, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST));
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.ASCENDING_WEST), 5, 4, 3, box);
        this.chanceReplaceAir(world, randomSource, .5f, Blocks.POWERED_RAIL.defaultBlockState().setValue(PoweredRailBlock.SHAPE, RailShape.EAST_WEST).setValue(PoweredRailBlock.POWERED, true), 6, 4, 3, box);

        // Decorations
        this.addBiomeDecorations(world, box, randomSource, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, randomSource, config.decorationChances.vineChance, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
    }
}
