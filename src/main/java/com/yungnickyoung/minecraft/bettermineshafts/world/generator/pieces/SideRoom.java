package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.Blocks;
import net.minecraft.block.FurnaceBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

public class SideRoom extends MineshaftPiece {
    private boolean hasDownstairs;
    private static final int
        SECONDARY_AXIS_LEN = 10,
        Y_AXIS_LEN = 5,
        MAIN_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SideRoom(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, compoundTag);
        this.hasDownstairs = compoundTag.getBoolean("hasDownstairs");
    }

    public SideRoom(int i, int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, i, pieceChainLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
        tag.putBoolean("hasDownstairs", this.hasDownstairs);
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
        // Chance of generating side room dungeon downstairs
        if (random.nextInt(4) == 0) {
            Direction direction = this.getFacing();
            if (direction == null) {
                return;
            }

            StructurePiece newDungeonPiece = null;
            switch (direction) {
                case NORTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.minX + 6, this.boundingBox.minY - 4, this.boundingBox.maxZ, this.getFacing(), this.method_14923(), 0);
                    break;
                case SOUTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.minX + 6, this.boundingBox.minY - 4, this.boundingBox.minZ, this.getFacing(), this.method_14923(), 0);
                    break;
                case WEST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY - 4, this.boundingBox.minZ + 6, this.getFacing(), this.method_14923(), 0);
                    break;
                case EAST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 4, this.boundingBox.minZ + 6, this.getFacing(), this.method_14923(), 0);
            }

            if (newDungeonPiece != null) {
                this.hasDownstairs = true;
            }
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        // Fill with stone then clean out with air
        this.fillWithOutline(world, box, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), false);
        this.fillWithOutline(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR, AIR, false);

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), true);

        // Furnace 1
        if (random.nextInt(2) == 0) {
            this.addBlock(world, Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, Direction.NORTH), 2, 1, 1, box);
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.applyXTransform(2, 1), this.applyYTransform(1), this.applyZTransform(2, 1)));
            if (blockEntity instanceof FurnaceBlockEntity) {
                ((FurnaceBlockEntity)blockEntity).setInvStack(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Furnace 2
        if (random.nextInt(2) == 0) {
            this.addBlock(world, Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, Direction.NORTH), 1, 1, 1, box);
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.applyXTransform(1, 1), this.applyYTransform(1), this.applyZTransform(1, 1)));
            if (blockEntity instanceof FurnaceBlockEntity) {
                ((FurnaceBlockEntity)blockEntity).setInvStack(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Crafting table
        if (random.nextInt(2) == 0) {
            this.addBlock(world, Blocks.CRAFTING_TABLE.getDefaultState(), 3, 1, 1, box);
        }

        // Barrel with loot
        if (random.nextInt(4) == 0)
            this.addBarrel(world, box, random, LOCAL_X_END - 1, 1, 1, LootTables.ABANDONED_MINESHAFT_CHEST);

        // Entrance to spider lair
        if (this.hasDownstairs) {
            this.addBlock(world, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.NORTH), 6, 0, 1, box);
            this.addBlock(world, Blocks.OAK_TRAPDOOR.getDefaultState().with(TrapdoorBlock.FACING, Direction.NORTH), 6, 1, 1, box);
        }

        // Decorations
        generateIronBarSupports(world, box, random);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }

    private void generateIronBarSupports(IWorld world, BlockBox box, Random random) {
        List<Integer> invalidXs = Lists.newLinkedList(); // Prevent columns of bars from spawning adjacent to eachother
        for (int z = 2; z <= 3; z++) {
            for (int x = 2; x <= 7; x++) {
                if (invalidXs.contains(x)) {
                    continue;
                }
                if (random.nextInt(5) == 0) {
                    this.fillWithOutline(world, box, x, 1, z, x, 3, z, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);
                    invalidXs.add(x);
                    x++;
                }
            }
        }
    }
}
