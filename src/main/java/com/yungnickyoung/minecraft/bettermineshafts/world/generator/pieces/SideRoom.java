package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
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

    public SideRoom(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, compoundTag);
        this.hasDownstairs = compoundTag.getBoolean("hasDownstairs");
    }

    public SideRoom(int i, int pieceChainLen, Random random, MutableBoundingBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM, i, pieceChainLen, type);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
        tag.putBoolean("hasDownstairs", this.hasDownstairs);
    }

    public static MutableBoundingBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        MutableBoundingBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        // Chance of generating side room dungeon downstairs
        if (random.nextInt(4) == 0) {
            Direction direction = this.getCoordBaseMode();
            if (direction == null) {
                return;
            }

            StructurePiece newDungeonPiece = null;
            switch (direction) {
                case NORTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.minX + 6, this.boundingBox.minY - 4, this.boundingBox.maxZ, this.getCoordBaseMode(), this.getComponentType(), 0);
                    break;
                case SOUTH:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.minX + 6, this.boundingBox.minY - 4, this.boundingBox.minZ, this.getCoordBaseMode(), this.getComponentType(), 0);
                    break;
                case WEST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY - 4, this.boundingBox.minZ + 6, this.getCoordBaseMode(), this.getComponentType(), 0);
                    break;
                case EAST:
                    newDungeonPiece = BetterMineshaftGenerator.generateAndAddSideRoomDungeonPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 4, this.boundingBox.minZ + 6, this.getCoordBaseMode(), this.getComponentType(), 0);
            }

            if (newDungeonPiece != null) {
                this.hasDownstairs = true;
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random random, MutableBoundingBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box
        if (this.isLiquidInStructureBoundingBox(world, box)) {
            return false;
        }

        // Fill with stone then clean out with air. Track ceiling positions to see where we can place iron bar supports
        this.fill(world, box, random, 0, 0, 0, LOCAL_X_END, 1, LOCAL_Z_END, getBrickSelector());
        this.replaceNonAir(world, box, random, 0, 2, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END, getBrickSelector());
        this.fill(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, CAVE_AIR);
        boolean[][] ceiling = new boolean[SECONDARY_AXIS_LEN][MAIN_AXIS_LEN];
        for (int x = 0; x <= LOCAL_X_END; ++x) {
            for (int z = 0; z <= LOCAL_Z_END; ++z) {
                BlockState currState = this.getBlockStateFromPosFixed(world, x, LOCAL_Y_END, z, box);
                if (currState != null && !currState.isAir()) {
                    this.setBlockState(world, getBrickSelector().get(random), x, LOCAL_Y_END, z, box);
                    ceiling[x][z] = true;
                }
            }
        }

        if (!hasDownstairs)
            generateLegs(world, random);

        // Furnace 1
        if (random.nextInt(2) == 0) {
            this.setBlockState(world, Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, Direction.NORTH), 2, 1, 1, box);
            TileEntity blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(2, 1), this.getYWithOffset(1), this.getZWithOffset(2, 1)));
            if (blockEntity instanceof FurnaceTileEntity) {
                ((FurnaceTileEntity)blockEntity).setInventorySlotContents(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Furnace 2
        if (random.nextInt(2) == 0) {
            this.setBlockState(world, Blocks.FURNACE.getDefaultState().with(FurnaceBlock.FACING, Direction.NORTH), 1, 1, 1, box);
            TileEntity blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(1), this.getZWithOffset(1, 1)));
            if (blockEntity instanceof FurnaceTileEntity) {
                ((FurnaceTileEntity)blockEntity).setInventorySlotContents(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Crafting table
        this.chanceAddBlock(world, random, .5f, Blocks.CRAFTING_TABLE.getDefaultState(), 3, 1, 1, box);

        // Barrel with loot
        if (random.nextInt(4) == 0) {
            this.addBarrel(world, box, random, LOCAL_X_END - 1, 1, 1, LootTables.CHESTS_ABANDONED_MINESHAFT);
        }

        // Entrance to spider lair
        if (this.hasDownstairs) {
            this.setBlockState(world, Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.NORTH), 6, 0, 1, box);
            this.setBlockState(world, getTrapdoor().with(TrapDoorBlock.HORIZONTAL_FACING, Direction.NORTH), 6, 1, 1, box);
        }

        // Decorations
        generateIronBarSupports(world, box, random, ceiling);
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }

    private void generateLegs(IWorld world, Random random) {
        generateLeg(world, random, 1, 1, getBrickSelector());
        generateLeg(world, random, 1, LOCAL_Z_END - 1, getBrickSelector());
        generateLeg(world, random, LOCAL_X_END - 1, 1, getBrickSelector());
        generateLeg(world, random, LOCAL_X_END - 1, LOCAL_Z_END - 1, getBrickSelector());
    }

    private void generateIronBarSupports(IWorld world, MutableBoundingBox box, Random random, boolean[][] ceiling) {
        List<Integer> invalidXs = Lists.newLinkedList(); // Prevent columns of bars from spawning adjacent to eachother
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
