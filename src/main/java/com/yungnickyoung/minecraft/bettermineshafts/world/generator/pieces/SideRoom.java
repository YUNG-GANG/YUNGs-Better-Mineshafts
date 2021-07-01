package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

import java.util.ArrayList;
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

    public SideRoom(ServerWorld world, NbtCompound compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, compoundTag);
        this.hasDownstairs = compoundTag.getBoolean("hasDownstairs");
    }

    public SideRoom(int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, pieceChainLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void writeNbt(ServerWorld world, NbtCompound tag) {
        super.writeNbt(world, tag);
        tag.putBoolean("hasDownstairs", this.hasDownstairs);
    }

    public static BlockBox determineBoxPosition(StructurePiecesHolder structurePiecesHolder, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePiecesHolder.getIntersecting(blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void fillOpenings(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random) {
        // Chance of generating side room dungeon downstairs
        if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.workstationDungeonSpawnRate) {
            Direction direction = this.getFacing();
            if (direction == null) {
                return;
            }

            StructurePiece newDungeonPiece = null;
            switch (direction) {
                case NORTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() + 6, this.boundingBox.getMinY() - 4, this.boundingBox.getMaxZ(), this.getFacing(), chainLength);
                    break;
                case SOUTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() + 6, this.boundingBox.getMinY() - 4, this.boundingBox.getMinZ(), this.getFacing(), chainLength);
                    break;
                case WEST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX(), this.boundingBox.getMinY() - 4, this.boundingBox.getMinZ() + 6, this.getFacing(), chainLength);
                    break;
                case EAST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY() - 4, this.boundingBox.getMinZ() + 6, this.getFacing(), chainLength);
            }

            if (newDungeonPiece != null) {
                this.hasDownstairs = true;
            }
        }
    }

    @Override
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isTouchingLiquid(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Fill with stone then clean out with air. Track ceiling positions to see where we can place iron bar supports
        this.fill(world, box, random, 0, 0, 0, LOCAL_X_END, 1, LOCAL_Z_END, getBrickSelector()); // Floor
        this.replaceNonAir(world, box, random, 0, 2, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END, getBrickSelector()); // Fill w/ brick selector
        this.fill(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR); // Fill with air
        boolean[][] ceiling = new boolean[SECONDARY_AXIS_LEN][MAIN_AXIS_LEN]; // Ceiling
        for (int x = 0; x <= LOCAL_X_END; ++x) {
            for (int z = 0; z <= LOCAL_Z_END; ++z) {
                BlockState currState = this.getBlockAtFixed(world, x, LOCAL_Y_END, z, box);
                if (currState != null && currState != AIR && currState != Blocks.AIR.getDefaultState()) {
                    this.addBlock(world, getBrickSelector().get(random), x, LOCAL_Y_END, z, box);
                    ceiling[x][z] = true;
                }
            }
        }

        if (!hasDownstairs)
            generateLegs(world, random, box);

        // Furnace 1
        if (random.nextInt(2) == 0) {
            this.addBlock(world, Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, Direction.NORTH), 2, 1, 1, box);
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.applyXTransform(2, 1), this.applyYTransform(1), this.applyZTransform(2, 1)));
            if (blockEntity instanceof FurnaceBlockEntity) {
                ((FurnaceBlockEntity) blockEntity).setStack(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Furnace 2
        if (random.nextInt(2) == 0) {
            this.addBlock(world, Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, Direction.NORTH), 1, 1, 1, box);
            BlockEntity blockEntity = world.getBlockEntity(new BlockPos(this.applyXTransform(1, 1), this.applyYTransform(1), this.applyZTransform(1, 1)));
            if (blockEntity instanceof FurnaceBlockEntity) {
                ((FurnaceBlockEntity) blockEntity).setStack(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Crafting table
        this.chanceAddBlock(world, random, .5f, Blocks.CRAFTING_TABLE.getDefaultState(), 3, 1, 1, box);

        // Barrel with loot
        if (random.nextInt(4) == 0) {
            this.addBarrel(world, box, random, LOCAL_X_END - 1, 1, 1, LootTables.ABANDONED_MINESHAFT_CHEST);
        }

        // Entrance to spider lair
        if (this.hasDownstairs) {
            this.addBlock(world, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.NORTH), 6, 0, 1, box);
            this.addBlock(world, getTrapdoor().with(TrapdoorBlock.FACING, Direction.NORTH), 6, 1, 1, box);
        }

        // Decorations
        generateIronBarSupports(world, box, random, ceiling);
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }

    private void generateLegs(StructureWorldAccess world, Random random, BlockBox box) {
        generateLeg(world, random, box, 1, 1, getBrickSelector());
        generateLeg(world, random, box, 1, LOCAL_Z_END - 1, getBrickSelector());
        generateLeg(world, random, box, LOCAL_X_END - 1, 1, getBrickSelector());
        generateLeg(world, random, box, LOCAL_X_END - 1, LOCAL_Z_END - 1, getBrickSelector());
    }

    private void generateIronBarSupports(StructureWorldAccess world, BlockBox box, Random random, boolean[][] ceiling) {
        List<Integer> invalidXs = new ArrayList<>(); // Prevent columns of bars from spawning adjacent to eachother
        for (int z = 2; z <= 3; z++) {
            for (int x = 2; x <= 7; x++) {
                if (invalidXs.contains(x)) continue;
                if (!ceiling[x][z]) continue;
                if (random.nextInt(5) == 0) {
                    this.fill(world, box, x, 1, z, x, 3, z, Blocks.IRON_BARS.getDefaultState());
                    invalidXs.add(x);
                    x++;
                }
            }
        }
    }
}
