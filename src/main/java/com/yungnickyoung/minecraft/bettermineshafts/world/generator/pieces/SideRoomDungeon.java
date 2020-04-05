package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BoxUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
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

public class SideRoomDungeon extends MineshaftPart {
    private static final int
        SECONDARY_AXIS_LEN = 9,
        Y_AXIS_LEN = 4,
        MAIN_AXIS_LEN = 9;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SideRoomDungeon(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, compoundTag);
    }

    public SideRoomDungeon(int i, int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, i, pieceChainLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = new BlockBox(x, y, z, x, y + Y_AXIS_LEN - 1, z);

        switch (direction) {
            case NORTH:
            default:
                blockBox.maxX = x + 4;
                blockBox.minX = x - 4;
                blockBox.minZ = z - (MAIN_AXIS_LEN - 1);
                break;
            case SOUTH:
                blockBox.maxX = x + 4;
                blockBox.minX = x - 4;
                blockBox.maxZ = z + (MAIN_AXIS_LEN - 1);
                break;
            case WEST:
                blockBox.minX = x - (MAIN_AXIS_LEN - 1);
                blockBox.maxZ = z + 4;
                blockBox.minZ = z - 4;
                break;
            case EAST:
                blockBox.maxX = x + (MAIN_AXIS_LEN - 1);
                blockBox.maxZ = z + 4;
                blockBox.minZ = z - 4;
        }

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.method_14932(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    /**
     * buildComponent
     */
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) { // check if box contains any liquid
//                return false;
        }

        // Fill with stone then clean out with air
        this.fillWithOutline(world, box, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), false);
        this.fillWithOutline(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, AIR, AIR, false);

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .4f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .2f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);

        // Ladders
        BlockState LADDER = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.NORTH);
        this.fillWithOutline(world, box, 4, 1, 1, 4, 3, 1, LADDER, LADDER, false);

        // Spawner
        BlockPos spawnerPos = new BlockPos(this.applyXTransform(4,5), this.applyYTransform(1), this.applyZTransform(4, 5));
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        BlockEntity blockEntity = world.getBlockEntity(spawnerPos);
        if (blockEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(EntityType.CAVE_SPIDER);
        }

        // Cobwebs immediately surrounding chests
        for (int x = 3; x <= 5; x++) {
            for (int y = 1; y <= 2; y++) {
                for (int z = 4; z <= 6; z++) {
                    if (this.getBlockAt(world, x, y, z, box).isAir()) {
                        this.addBlockWithRandomThreshold(world, box, random, .9f, x, y, z, Blocks.COBWEB.getDefaultState());
                    }
                }
            }
        }

        // Fill room randomly with cobwebs
        for (int x = 1; x <= LOCAL_X_END - 1; x++) {
            for (int y = 1; y <= 2; y++) {
                for (int z = 1; z <= LOCAL_Z_END - 1; z++) {
                    if (this.getBlockAt(world, x, y, z, box).isAir()) {
                        this.addBlockWithRandomThreshold(world, box, random, .1f, x, y, z, Blocks.COBWEB.getDefaultState());
                    }
                }
            }
        }

        // Chests
        this.addChest(world, box, random, 1, 1, LOCAL_Z_END - 1, LootTables.ABANDONED_MINESHAFT_CHEST);
        if (random.nextInt(2) == 0)
            this.addChest(world, box, random, LOCAL_X_END - 1, 1, LOCAL_Z_END - 1, LootTables.STRONGHOLD_CORRIDOR_CHEST);

        return true;
    }
}
