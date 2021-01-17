package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.*;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
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

    public ZombieVillagerRoom(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.ZOMBIE_VILLAGER_ROOM, compoundTag);
    }

    public ZombieVillagerRoom(int chainLength, Random random, MutableBoundingBox blockBox, Direction direction, MineshaftVariantSettings settings) {
        super(BetterMineshaftStructurePieceType.ZOMBIE_VILLAGER_ROOM, chainLength, settings);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
    }

    public static MutableBoundingBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        MutableBoundingBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random random, MutableBoundingBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isLiquidInStructureBoundingBox(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Outermost walls
        this.fill(world, box, 1, 0, 0, 5, 2, 0, Blocks.STONE.getDefaultState());
        this.fill(world, box, 0, 0, 1, 0, 2, 5, Blocks.STONE.getDefaultState());
        this.fill(world, box, LOCAL_X_END, 0, 1, LOCAL_X_END, 2, 5, Blocks.STONE.getDefaultState());
        this.fill(world, box, 1, 0, LOCAL_Z_END, 5, 2, LOCAL_Z_END, Blocks.STONE.getDefaultState());
        // Randomize
        // Cobble
        this.chanceReplaceNonAir(world, box, random, .4f, 1, 0, 0, 5, 2, 0, Blocks.COBBLESTONE.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .4f, 0, 0, 1, 0, 2, 5, Blocks.COBBLESTONE.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .4f, LOCAL_X_END, 0, 1, LOCAL_X_END, 2, 5, Blocks.COBBLESTONE.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .4f, 1, 0, LOCAL_Z_END, 5, 2, LOCAL_Z_END, Blocks.COBBLESTONE.getDefaultState());
        // Stone brick
        this.chanceReplaceNonAir(world, box, random, .1f, 1, 0, 0, 5, 2, 0, Blocks.STONE_BRICKS.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .1f, 0, 0, 1, 0, 2, 5, Blocks.STONE_BRICKS.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .1f, LOCAL_X_END, 0, 1, LOCAL_X_END, 2, 5, Blocks.STONE_BRICKS.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .1f, 1, 0, LOCAL_Z_END, 5, 2, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState());

        // Slabs on top of outermost walls
        this.fill(world, box, 2, 3, 0, 4, 3, 0, Blocks.STONE_SLAB.getDefaultState());
        this.fill(world, box, 0, 3, 2, 0, 3, 4, Blocks.STONE_SLAB.getDefaultState());
        this.fill(world, box, LOCAL_X_END, 3, 2, LOCAL_X_END, 3, 4, Blocks.STONE_SLAB.getDefaultState());
        this.fill(world, box, 2, 3, LOCAL_Z_END, 4, 3, LOCAL_Z_END, Blocks.STONE_SLAB.getDefaultState());
        // Randomize
        this.chanceFill(world, box, random, .5f, 2, 3, 0, 4, 3, 0, Blocks.COBBLESTONE_SLAB.getDefaultState());
        this.chanceFill(world, box, random, .5f, 0, 3, 2, 0, 3, 4, Blocks.COBBLESTONE_SLAB.getDefaultState());
        this.chanceFill(world, box, random, .5f, LOCAL_X_END, 3, 2, LOCAL_X_END, 3, 4, Blocks.COBBLESTONE_SLAB.getDefaultState());
        this.chanceFill(world, box, random, .5f, 2, 3, LOCAL_Z_END, 4, 3, LOCAL_Z_END, Blocks.COBBLESTONE_SLAB.getDefaultState());

        // Second wall/ceiling layer, formed with upside-down stairs
        // Cardinal directions
        this.fill(world, box, 2, 3, 1, 4, 3, 1, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.SOUTH).with(StairsBlock.HALF, Half.TOP));
        this.fill(world, box, 1, 3, 2, 1, 3, 4, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.WEST).with(StairsBlock.HALF, Half.TOP));
        this.fill(world, box, 2, 3, 5, 4, 3, 5, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.NORTH).with(StairsBlock.HALF, Half.TOP));
        this.fill(world, box, 5, 3, 2, 5, 3, 4, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.FACING, Direction.EAST).with(StairsBlock.HALF, Half.TOP));
        // Corners
        this.setBlockState(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairsShape.INNER_RIGHT).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.SOUTH), 1, 3, 1, box);
        this.setBlockState(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairsShape.INNER_LEFT).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.NORTH), 1, 3, 5, box);
        this.setBlockState(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairsShape.INNER_LEFT).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.SOUTH), 5, 3, 1, box);
        this.setBlockState(world, Blocks.COBBLESTONE_STAIRS.getDefaultState().with(StairsBlock.SHAPE, StairsShape.INNER_RIGHT).with(StairsBlock.HALF, Half.TOP).with(StairsBlock.FACING, Direction.NORTH), 5, 3, 5, box);

        // Third ceiling layer, formed with bottom-half slabs
        this.fill(world, box, 2, 4, 2, 4, 4, 4, Blocks.STONE_SLAB.getDefaultState());
        this.chanceFill(world, box, random, .5f, 2, 4, 2, 4, 4, 4, Blocks.COBBLESTONE_SLAB.getDefaultState());
        this.setBlockState(world, CAVE_AIR, 3, 4, 3, box);

        // Top middle roof block
        this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP), 3, 4,3, box);

        // Floor
        this.fill(world, box, 1, 0, 1, 5, 0, 5, Blocks.STONE.getDefaultState());
        // Randomize
        this.chanceFill(world, box, random, .4f, 1, 0, 1, 5, 0, 5, Blocks.COBBLESTONE.getDefaultState());
        this.chanceFill(world, box, random, .1f, 1, 0, 1, 5, 0, 5, Blocks.STONE_BRICKS.getDefaultState());

        // Fill with air
        this.fill(world, box, 1, 1, 1, 5, 2, 5, CAVE_AIR);
        this.fill(world, box, 2, 3, 2, 4, 3, 4, CAVE_AIR);

        // Place door
        this.fill(world, box, 3, 1, 0, 3, 2, 0, CAVE_AIR);
        this.setBlockState(world, Blocks.IRON_DOOR.getDefaultState().with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.LOWER), 3, 1, 0, box);
        this.setBlockState(world, Blocks.IRON_DOOR.getDefaultState().with(DoorBlock.FACING, Direction.NORTH).with(DoorBlock.HALF, DoubleBlockHalf.UPPER), 3, 2, 0, box);

        // Window
        this.fill(world, box, 6, 2, 2, 6, 2, 4, Blocks.IRON_BARS.getDefaultState());

        // Beds
        this.setBlockState(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.HORIZONTAL_FACING, Direction.NORTH).with(BedBlock.PART, BedPart.FOOT), 1, 1, 4, box);
        this.setBlockState(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.HORIZONTAL_FACING, Direction.NORTH).with(BedBlock.PART, BedPart.HEAD), 1, 1, 5, box);
        this.setBlockState(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.HORIZONTAL_FACING, Direction.NORTH).with(BedBlock.PART, BedPart.FOOT), 5, 1, 4, box);
        this.setBlockState(world, Blocks.BLACK_BED.getDefaultState().with(BedBlock.HORIZONTAL_FACING, Direction.NORTH).with(BedBlock.PART, BedPart.HEAD), 5, 1, 5, box);

        // Mob spawner
        BlockPos spawnerPos = new BlockPos(this.getXWithOffset(3,3), this.getYWithOffset(0), this.getZWithOffset(3, 3));
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        TileEntity blockEntity = world.getTileEntity(spawnerPos);
        if (blockEntity instanceof MobSpawnerTileEntity) {
            ((MobSpawnerTileEntity)blockEntity).getSpawnerBaseLogic().setEntityType(EntityType.ZOMBIE_VILLAGER);
        }

        // Wall with redstone torch in corner
        this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), 1, 1, 1, box);
        this.setBlockState(world, Blocks.REDSTONE_TORCH.getDefaultState(), 1, 2, 1, box);

        // Barrel
        this.addBarrel(world, box, random, 5, 1, 1, LootTables.CHESTS_ABANDONED_MINESHAFT);

        // Button for door (inside)
        this.setBlockState(world, Blocks.STONE_BUTTON.getDefaultState(), 2, 2, 1, box);

        // Anvil
        this.setBlockState(world, Blocks.ANVIL.getDefaultState(), 5, 1, 2, box);

        // Decoration block (smithing table, crafting table, blast furnace)
        if (random.nextFloat() < .33f)
            this.setBlockState(world, Blocks.SMITHING_TABLE.getDefaultState(), 2, 1, 5, box);
        else if (random.nextFloat() < .67f)
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 2, 1, 5, box);
        else
            this.setBlockState(world, Blocks.BLAST_FURNACE.getDefaultState(), 2, 1, 5, box);

        // Cobwebs
        this.chanceFill(world, box, random, .3f, 2, 3, 2, 4, 3, 4, Blocks.COBWEB.getDefaultState());

        return true;
    }
}
