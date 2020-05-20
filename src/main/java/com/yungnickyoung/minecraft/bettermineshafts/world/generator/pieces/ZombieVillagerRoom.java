package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.*;
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

public class ZombieVillagerRoom extends MineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 7,
        Y_AXIS_LEN = 5,
        MAIN_AXIS_LEN = 7;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public ZombieVillagerRoom(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.ZOMBIE_VILLAGER_ROOM, compoundTag);
    }

    public ZombieVillagerRoom(int i, int chunkPieceLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.ZOMBIE_VILLAGER_ROOM, i, chunkPieceLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.method_14932(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        // Outermost walls
        this.fillWithOutline(world, box, 1, 0, 0, 5, 2, 0, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), false);
        this.fillWithOutline(world, box, 0, 0, 1, 0, 2, 5, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), false);
        this.fillWithOutline(world, box, LOCAL_X_END, 0, 1, LOCAL_X_END, 2, 5, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), false);
        this.fillWithOutline(world, box, 1, 0, LOCAL_Z_END, 5, 2, LOCAL_Z_END, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), false);
        // Randomize
        // Cobble
        this.randomFillWithOutline(world, box, random, .4f, 1, 0, 0, 5, 2, 0, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .4f, 0, 0, 1, 0, 2, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .4f, LOCAL_X_END, 0, 1, LOCAL_X_END, 2, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .4f, 1, 0, LOCAL_Z_END, 5, 2, LOCAL_Z_END, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        // Stone brick
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, 5, 2, 0, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 1, 0, 2, 5, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, LOCAL_X_END, 0, 1, LOCAL_X_END, 2, 5, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, LOCAL_Z_END, 5, 2, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);

        // Slabs on top of outermost walls
        this.fillWithOutline(world, box, 2, 3, 0, 4, 3, 0, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
        this.fillWithOutline(world, box, 0, 3, 2, 0, 3, 4, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
        this.fillWithOutline(world, box, LOCAL_X_END, 3, 2, LOCAL_X_END, 3, 4, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
        this.fillWithOutline(world, box, 2, 3, LOCAL_Z_END, 4, 3, LOCAL_Z_END, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
        // Randomize
        this.randomFillWithOutline(world, box, random, .5f, 2, 3, 0, 4, 3, 0, Blocks.COBBLESTONE_SLAB.getDefaultState(), Blocks.COBBLESTONE_SLAB.getDefaultState(), false);
        this.randomFillWithOutline(world, box, random, .5f, 0, 3, 2, 0, 3, 4, Blocks.COBBLESTONE_SLAB.getDefaultState(), Blocks.COBBLESTONE_SLAB.getDefaultState(), false);
        this.randomFillWithOutline(world, box, random, .5f, LOCAL_X_END, 3, 2, LOCAL_X_END, 3, 4, Blocks.COBBLESTONE_SLAB.getDefaultState(), Blocks.COBBLESTONE_SLAB.getDefaultState(), false);
        this.randomFillWithOutline(world, box, random, .5f, 2, 3, LOCAL_Z_END, 4, 3, LOCAL_Z_END, Blocks.COBBLESTONE_SLAB.getDefaultState(), Blocks.COBBLESTONE_SLAB.getDefaultState(), false);

        // Second wall/ceiling layer, formed with upside-down stairs
        // Cardinal directions
        this.fillWithOutline(world, box, 2, 3, 1, 4, 3, 1, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, BlockHalf.TOP), Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, BlockHalf.TOP), false);
        this.fillWithOutline(world, box, 1, 3, 2, 1, 3, 4, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, BlockHalf.TOP), Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, BlockHalf.TOP), false);
        this.fillWithOutline(world, box, 2, 3, 5, 4, 3, 5, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, BlockHalf.TOP), Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, BlockHalf.TOP), false);
        this.fillWithOutline(world, box, 5, 3, 2, 5, 3, 4, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, BlockHalf.TOP), Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, BlockHalf.TOP), false);
        // Corners
        this.addBlock(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairShape.INNER_RIGHT).with(StairsBlock.HALF, BlockHalf.TOP).with(StairsBlock.FACING, Direction.SOUTH), 1, 3, 1, box);
        this.addBlock(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairShape.INNER_LEFT).with(StairsBlock.HALF, BlockHalf.TOP).with(StairsBlock.FACING, Direction.NORTH), 1, 3, 5, box);
        this.addBlock(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairShape.INNER_LEFT).with(StairsBlock.HALF, BlockHalf.TOP).with(StairsBlock.FACING, Direction.SOUTH), 5, 3, 1, box);
        this.addBlock(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairShape.INNER_RIGHT).with(StairsBlock.HALF, BlockHalf.TOP).with(StairsBlock.FACING, Direction.NORTH), 5, 3, 5, box);

        // Third ceiling layer, formed with bottom-half slabs
        this.fillWithOutline(world, box, 2, 4, 2, 4, 4, 4, Blocks.STONE_SLAB.getDefaultState(), Blocks.STONE_SLAB.getDefaultState(), false);
        this.randomFillWithOutline(world, box, random, .5f, 2, 4, 2, 4, 4, 4, Blocks.COBBLESTONE_SLAB.getDefaultState(), Blocks.COBBLESTONE_SLAB.getDefaultState(), false);
        this.addBlock(world, AIR, 3, 4, 3, box);

        // Top middle roof block
        this.addBlock(world, Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP), 3, 4,3, box);

        // Floor
        this.fillWithOutline(world, box, 1, 0, 1, 5, 0, 5, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), false);
        // Randomize
        this.randomFillWithOutline(world, box, random, .4f, 1, 0, 1, 5, 0, 5, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), false);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 1, 5, 0, 5, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), false);

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 1, 5, 2, 5, AIR, AIR, false);
        this.fillWithOutline(world, box, 2, 3, 2, 4, 3, 4, AIR, AIR, false);

        // Place door
        this.fillWithOutline(world, box, 3, 1, 0, 3, 2, 0, AIR, AIR, false);
        this.addBlock(world, Blocks.IRON_DOOR.getDefaultState().with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER), 3, 1, 0, box);
        this.addBlock(world, Blocks.IRON_DOOR.getDefaultState().with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3, 2, 0, box);

        // Window
        this.fillWithOutline(world, box, 6, 2, 2, 6, 2, 4, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);

        // Beds
        this.addBlock(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.FACING, Direction.NORTH).with(BedBlock.PART, BedPart.FOOT), 1, 1, 4, box);
        this.addBlock(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.FACING, Direction.NORTH).with(BedBlock.PART, BedPart.HEAD), 1, 1, 5, box);
        this.addBlock(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.FACING, Direction.NORTH).with(BedBlock.PART, BedPart.FOOT), 5, 1, 4, box);
        this.addBlock(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.FACING, Direction.NORTH).with(BedBlock.PART, BedPart.HEAD), 5, 1, 5, box);

        // Mob spawner
        BlockPos spawnerPos = new BlockPos(this.applyXTransform(3,3), this.applyYTransform(0), this.applyZTransform(3, 3));
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        BlockEntity blockEntity = world.getBlockEntity(spawnerPos);
        if (blockEntity instanceof MobSpawnerBlockEntity) {
            ((MobSpawnerBlockEntity)blockEntity).getLogic().setEntityId(EntityType.ZOMBIE_VILLAGER);
        }

        // Wall with redstone torch in corner
        this.addBlock(world, Blocks.COBBLESTONE_WALL.getDefaultState(), 1, 1, 1, box);
        this.addBlock(world, Blocks.REDSTONE_TORCH.getDefaultState(), 1, 2, 1, box);

        // Barrel
        this.addBarrel(world, box, random, 5, 1, 1, LootTables.ABANDONED_MINESHAFT_CHEST);

        // Button for door (inside)
        this.addBlock(world, Blocks.STONE_BUTTON.getDefaultState(), 2, 2, 1, box);

        // Anvil
        this.addBlock(world, Blocks.ANVIL.getDefaultState(), 5, 1, 2, box);

        // Decoration block (smithing table, crafting table, blast furnace)
        if (random.nextFloat() < .33f)
            this.addBlock(world, Blocks.SMITHING_TABLE.getDefaultState(), 2, 1, 5, box);
        else if (random.nextFloat() < .67f)
            this.addBlock(world, Blocks.CRAFTING_TABLE.getDefaultState(), 2, 1, 5, box);
        else
            this.addBlock(world, Blocks.BLAST_FURNACE.getDefaultState(), 2, 1, 5, box);

        // Cobwebs
        this.randomFillWithOutline(world, box, random, .3f, 2, 3, 2, 4, 3, 4, Blocks.COBWEB.getDefaultState(), Blocks.COBWEB.getDefaultState(), false);

        return true;
    }
}
