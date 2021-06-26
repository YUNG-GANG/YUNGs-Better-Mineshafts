package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.mixin.BlockBoxAccessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
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

public class SideRoomDungeon extends MineshaftPiece {
    private static final int
            SECONDARY_AXIS_LEN = 9,
            Y_AXIS_LEN = 4,
            MAIN_AXIS_LEN = 9;
    private static final int
            LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
            LOCAL_Y_END = Y_AXIS_LEN - 1,
            LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SideRoomDungeon(ServerWorld world, NbtCompound compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, compoundTag);
    }

    public SideRoomDungeon(int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, pieceChainLen, type, blockBox);
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
                ((BlockBoxAccessor) blockBox).setMaxX(x + 4);
                ((BlockBoxAccessor) blockBox).setMinX(x - 4);
                ((BlockBoxAccessor) blockBox).setMinZ(z - (MAIN_AXIS_LEN - 1));
                break;
            case SOUTH:
                ((BlockBoxAccessor) blockBox).setMaxX(x + 4);
                ((BlockBoxAccessor) blockBox).setMinX(x - 4);
                ((BlockBoxAccessor) blockBox).setMaxZ(z + (MAIN_AXIS_LEN - 1));
                break;
            case WEST:
                ((BlockBoxAccessor) blockBox).setMinX(x - (MAIN_AXIS_LEN - 1));
                ((BlockBoxAccessor) blockBox).setMaxZ(z + 4);
                ((BlockBoxAccessor) blockBox).setMinZ(z - 4);
                break;
            case EAST:
                ((BlockBoxAccessor) blockBox).setMaxX(x + (MAIN_AXIS_LEN - 1));
                ((BlockBoxAccessor) blockBox).setMaxZ(z + 4);
                ((BlockBoxAccessor) blockBox).setMinZ(z - 4);
        }

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePiecesHolder.getIntersecting(blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void fillOpenings(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random) {
    }

    @Override
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isTouchingLiquid(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Fill with stone then clean out with air
        this.fill(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getBrickSelector());
        this.fill(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR);

        generateLegs(world, random, box);

        // Ladders
        BlockState LADDER = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.NORTH);
        this.fill(world, box, 4, 1, 1, 4, 3, 1, LADDER);

        // Spawner
        BlockPos spawnerPos = new BlockPos(this.applyXTransform(4, 5), this.applyYTransform(1), this.applyZTransform(4, 5));
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        BlockEntity blockEntity = world.getBlockEntity(spawnerPos);
        if (blockEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity) blockEntity).getLogic().setEntityId(EntityType.CAVE_SPIDER);
        }

        // Cobwebs immediately surrounding chests
        this.chanceReplaceAir(world, box, random, .9f, 3, 1, 4, 5, 2, 6, Blocks.COBWEB.getDefaultState());

        // Fill room randomly with cobwebs
        this.chanceReplaceAir(world, box, random, .1f, 1, 1, 1, LOCAL_X_END - 1, 2, LOCAL_Z_END, Blocks.COBWEB.getDefaultState());

        // Chests
        this.addChest(world, box, random, 1, 1, LOCAL_Z_END - 1, LootTables.ABANDONED_MINESHAFT_CHEST);
        if (random.nextInt(2) == 0) { // Chance of second chest
            this.addChest(world, box, random, LOCAL_X_END - 1, 1, LOCAL_Z_END - 1, LootTables.ABANDONED_MINESHAFT_CHEST);
        }

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);

        return true;
    }

    private void generateLegs(StructureWorldAccess world, Random random, BlockBox box) {
        generateLeg(world, random, box, 1, 1, getBrickSelector());
        generateLeg(world, random, box, 1, LOCAL_Z_END - 1, getBrickSelector());
        generateLeg(world, random, box, LOCAL_X_END - 1, 1, getBrickSelector());
        generateLeg(world, random, box, LOCAL_X_END - 1, LOCAL_Z_END - 1, getBrickSelector());
    }
}
