package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

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

    public SideRoom(CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, compoundTag);
        this.hasDownstairs = compoundTag.getBoolean("hasDownstairs");
    }

    public SideRoom(int pieceChainLen, Random random, BoundingBox blockBox, Direction direction, BetterMineshaftStructureFeature.Type type) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, pieceChainLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
        compoundTag.putBoolean("hasDownstairs", this.hasDownstairs);
    }

    public static BoundingBox determineBoxPosition(StructurePieceAccessor structurePiecesHolder, Random random, int x, int y, int z, Direction direction) {
        BoundingBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePiecesHolder.findCollisionPiece(blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random) {
        // Chance of generating side room dungeon downstairs
        if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.workstationDungeonSpawnRate) {
            Direction direction = this.getOrientation();
            if (direction == null) {
                return;
            }

            StructurePiece newDungeonPiece = null;
            switch (direction) {
                case NORTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + 6, this.boundingBox.minY() - 4, this.boundingBox.maxZ(), this.getOrientation(), this.genDepth);
                    break;
                case SOUTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + 6, this.boundingBox.minY() - 4, this.boundingBox.minZ(), this.getOrientation(), this.genDepth);
                    break;
                case WEST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX(), this.boundingBox.minY() - 4, this.boundingBox.minZ() + 6, this.getOrientation(), this.genDepth);
                    break;
                case EAST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY() - 4, this.boundingBox.minZ() + 6, this.getOrientation(), this.genDepth);
            }

            if (newDungeonPiece != null) {
                this.hasDownstairs = true;
            }
        }
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
//        if (this.isTouchingLiquid(world, box)) return;
//        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return;

        // Fill with stone then clean out with air. Track ceiling positions to see where we can place iron bar supports
        this.fill(world, box, random, 0, 0, 0, LOCAL_X_END, 1, LOCAL_Z_END, getBrickSelector()); // Floor
        this.replaceNonAir(world, box, random, 0, 2, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END, getBrickSelector()); // Fill w/ brick selector
        this.fill(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR); // Fill with air
        boolean[][] ceiling = new boolean[SECONDARY_AXIS_LEN][MAIN_AXIS_LEN]; // Ceiling
        for (int x = 0; x <= LOCAL_X_END; ++x) {
            for (int z = 0; z <= LOCAL_Z_END; ++z) {
                BlockState currState = this.getBlockAtFixed(world, x, LOCAL_Y_END, z, box);
                if (currState != null && currState != AIR && currState != Blocks.AIR.defaultBlockState()) {
                    this.placeBlock(world, getBrickSelector().get(random), x, LOCAL_Y_END, z, box);
                    ceiling[x][z] = true;
                }
            }
        }

        if (!hasDownstairs)
            generateLegs(world, random, box);

        // Furnace 1
        if (random.nextInt(2) == 0) {
            this.placeBlock(world, Blocks.FURNACE.defaultBlockState().setValue(FurnaceBlock.FACING, Direction.NORTH), 2, 1, 1, box);
            BlockEntity blockEntity = world.getBlockEntity(this.getWorldPos(2, 1, 1));
            if (blockEntity instanceof FurnaceBlockEntity) {
                ((FurnaceBlockEntity) blockEntity).setItem(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Furnace 2
        if (random.nextInt(2) == 0) {
            this.placeBlock(world, Blocks.FURNACE.defaultBlockState().setValue(FurnaceBlock.FACING, Direction.NORTH), 1, 1, 1, box);
            BlockEntity blockEntity = world.getBlockEntity(this.getWorldPos(1, 1, 1));
            if (blockEntity instanceof FurnaceBlockEntity) {
                ((FurnaceBlockEntity) blockEntity).setItem(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Crafting table
        this.chanceAddBlock(world, random, .5f, Blocks.CRAFTING_TABLE.defaultBlockState(), 3, 1, 1, box);

        // Barrel with loot
        if (random.nextInt(4) == 0) {
            this.addBarrel(world, box, random, LOCAL_X_END - 1, 1, 1, BuiltInLootTables.ABANDONED_MINESHAFT);
        }

        // Entrance to spider lair
        if (this.hasDownstairs) {
            this.placeBlock(world, Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH), 6, 0, 1, box);
            this.placeBlock(world, getTrapdoor().setValue(TrapDoorBlock.FACING, Direction.NORTH), 6, 1, 1, box);
        }

        // Decorations
        generateIronBarSupports(world, box, random, ceiling);
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
    }

    private void generateLegs(WorldGenLevel world, Random random, BoundingBox box) {
        generateLeg(world, random, box, 1, 1, getBrickSelector());
        generateLeg(world, random, box, 1, LOCAL_Z_END - 1, getBrickSelector());
        generateLeg(world, random, box, LOCAL_X_END - 1, 1, getBrickSelector());
        generateLeg(world, random, box, LOCAL_X_END - 1, LOCAL_Z_END - 1, getBrickSelector());
    }

    private void generateIronBarSupports(WorldGenLevel world, BoundingBox box, Random random, boolean[][] ceiling) {
        List<Integer> invalidXs = new ArrayList<>(); // Prevent columns of bars from spawning adjacent to eachother
        for (int z = 2; z <= 3; z++) {
            for (int x = 2; x <= 7; x++) {
                if (invalidXs.contains(x)) continue;
                if (!ceiling[x][z]) continue;
                if (random.nextInt(5) == 0) {
                    this.fill(world, box, x, 1, z, x, 3, z, Blocks.IRON_BARS.defaultBlockState());
                    invalidXs.add(x);
                    x++;
                }
            }
        }
    }
}
