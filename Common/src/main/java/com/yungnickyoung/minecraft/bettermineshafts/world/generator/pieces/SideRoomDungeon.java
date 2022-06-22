package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.mixin.BoundingBoxAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.BetterMineshaftConfiguration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class SideRoomDungeon extends BetterMineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 9,
        Y_AXIS_LEN = 4,
        MAIN_AXIS_LEN = 9;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SideRoomDungeon(CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, compoundTag);
    }

    public SideRoomDungeon(int pieceChainLen, BoundingBox blockBox, Direction direction, BetterMineshaftConfiguration config) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, pieceChainLen, config, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
    }

    public static BoundingBox determineBoxPosition(StructurePieceAccessor structurePieceAccessor, int x, int y, int z, Direction direction) {
        BoundingBox blockBox = new BoundingBox(x, y, z, x, y + Y_AXIS_LEN - 1, z);

        switch (direction) {
            case NORTH:
            default:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + 4);
                ((BoundingBoxAccessor) blockBox).setMinX(x - 4);
                ((BoundingBoxAccessor) blockBox).setMinZ(z - (MAIN_AXIS_LEN - 1));
                break;
            case SOUTH:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + 4);
                ((BoundingBoxAccessor) blockBox).setMinX(x - 4);
                ((BoundingBoxAccessor) blockBox).setMaxZ(z + (MAIN_AXIS_LEN - 1));
                break;
            case WEST:
                ((BoundingBoxAccessor) blockBox).setMinX(x - (MAIN_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMaxZ(z + 4);
                ((BoundingBoxAccessor) blockBox).setMinZ(z - 4);
                break;
            case EAST:
                ((BoundingBoxAccessor) blockBox).setMaxX(x + (MAIN_AXIS_LEN - 1));
                ((BoundingBoxAccessor) blockBox).setMaxZ(z + 4);
                ((BoundingBoxAccessor) blockBox).setMinZ(z - 4);
        }

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePieceAccessor.findCollisionPiece(blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, RandomSource randomSource) {
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Fill with stone then clean out with air
        this.fill(world, box, randomSource, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, config.blockStateRandomizers.brickRandomizer);
        this.fill(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR);

        generateLegs(world, randomSource, box);

        // Ladders
        BlockState LADDER = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH);
        this.fill(world, box, 4, 1, 1, 4, 3, 1, LADDER);

        // Spawner
        BlockPos spawnerPos = this.getWorldPos(4, 1, 5);
        world.setBlock(spawnerPos, Blocks.SPAWNER.defaultBlockState(), 2);
        BlockEntity blockEntity = world.getBlockEntity(spawnerPos);
        if (blockEntity instanceof SpawnerBlockEntity) {
            ((SpawnerBlockEntity) blockEntity).getSpawner().setEntityId(EntityType.CAVE_SPIDER);
        }

        // Cobwebs immediately surrounding chests
        this.chanceReplaceAir(world, box, randomSource, .9f, 3, 1, 4, 5, 2, 6, Blocks.COBWEB.defaultBlockState());

        // Fill room randomly with cobwebs
        this.chanceReplaceAir(world, box, randomSource, .1f, 1, 1, 1, LOCAL_X_END - 1, 2, LOCAL_Z_END, Blocks.COBWEB.defaultBlockState());

        // Chests
        this.createChest(world, box, randomSource, 1, 1, LOCAL_Z_END - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
        if (randomSource.nextInt(2) == 0) { // Chance of second chest
            this.createChest(world, box, randomSource, LOCAL_X_END - 1, 1, LOCAL_Z_END - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
        }

        // Decorations
        this.addBiomeDecorations(world, box, randomSource, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
    }

    private void generateLegs(WorldGenLevel world, RandomSource randomSource, BoundingBox box) {
        generateLeg(world, randomSource, box, 1, 1, config.blockStateRandomizers.brickRandomizer);
        generateLeg(world, randomSource, box, 1, LOCAL_Z_END - 1, config.blockStateRandomizers.brickRandomizer);
        generateLeg(world, randomSource, box, LOCAL_X_END - 1, 1, config.blockStateRandomizers.brickRandomizer);
        generateLeg(world, randomSource, box, LOCAL_X_END - 1, LOCAL_Z_END - 1, config.blockStateRandomizers.brickRandomizer);
    }
}
