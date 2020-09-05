package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import net.minecraft.block.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.storage.loot.LootTableList;

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

    public ZombieVillagerRoom() {}

    public ZombieVillagerRoom(int i, int chunkPieceLen, Random random, StructureBoundingBox blockBox, EnumFacing direction, MineshaftVariantSettings settings) {
        super(i, chunkPieceLen, settings);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    public static StructureBoundingBox determineBoxPosition(List<StructureComponent> list, Random random, int x, int y, int z, EnumFacing direction) {
        StructureBoundingBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructureComponent intersectingPiece = StructureComponent.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructureComponent structurePiece, List<StructureComponent> list, Random random) {
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
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
        this.chanceReplaceNonAir(world, box, random, .1f, 1, 0, 0, 5, 2, 0, Blocks.STONEBRICK.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .1f, 0, 0, 1, 0, 2, 5, Blocks.STONEBRICK.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .1f, LOCAL_X_END, 0, 1, LOCAL_X_END, 2, 5, Blocks.STONEBRICK.getDefaultState());
        this.chanceReplaceNonAir(world, box, random, .1f, 1, 0, LOCAL_Z_END, 5, 2, LOCAL_Z_END, Blocks.STONEBRICK.getDefaultState());

        // Slabs on top of outermost walls
        this.fill(world, box, 2, 3, 0, 4, 3, 0, Blocks.STONE_SLAB.getDefaultState());
        this.fill(world, box, 0, 3, 2, 0, 3, 4, Blocks.STONE_SLAB.getDefaultState());
        this.fill(world, box, LOCAL_X_END, 3, 2, LOCAL_X_END, 3, 4, Blocks.STONE_SLAB.getDefaultState());
        this.fill(world, box, 2, 3, LOCAL_Z_END, 4, 3, LOCAL_Z_END, Blocks.STONE_SLAB.getDefaultState());
        // Randomize
        this.chanceFill(world, box, random, .5f, 2, 3, 0, 4, 3, 0, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));
        this.chanceFill(world, box, random, .5f, 0, 3, 2, 0, 3, 4, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));
        this.chanceFill(world, box, random, .5f, LOCAL_X_END, 3, 2, LOCAL_X_END, 3, 4, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));
        this.chanceFill(world, box, random, .5f, 2, 3, LOCAL_Z_END, 4, 3, LOCAL_Z_END, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));

        // Second wall/ceiling layer, formed with upside-down stairs
        // Cardinal directions
        this.fill(world, box, 2, 3, 1, 4, 3, 1, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        this.fill(world, box, 1, 3, 2, 1, 3, 4, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        this.fill(world, box, 2, 3, 5, 4, 3, 5, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        this.fill(world, box, 5, 3, 2, 5, 3, 4, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP));
        // Corners
        this.setBlockState(world, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 1, 3, 1, box);
        this.setBlockState(world, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, EnumFacing.NORTH), 1, 3, 5, box);
        this.setBlockState(world, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_LEFT).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 5, 3, 1, box);
        this.setBlockState(world, Blocks.STONE_STAIRS.getDefaultState().withProperty(BlockStairs.SHAPE, BlockStairs.EnumShape.INNER_RIGHT).withProperty(BlockStairs.HALF, BlockStairs.EnumHalf.TOP).withProperty(BlockStairs.FACING, EnumFacing.NORTH), 5, 3, 5, box);

        // Third ceiling layer, formed with bottom-half slabs
        this.fill(world, box, 2, 4, 2, 4, 4, 4, Blocks.STONE_SLAB.getDefaultState());
        this.chanceFill(world, box, random, .5f, 2, 4, 2, 4, 4, 4, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));
        this.setBlockState(world, AIR, 3, 4, 3, box);

        // Top middle roof block
        this.setBlockState(world, Blocks.STONE_SLAB.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP), 3, 4,3, box);

        // Floor
        this.fill(world, box, 1, 0, 1, 5, 0, 5, Blocks.STONE.getDefaultState());
        // Randomize
        this.chanceFill(world, box, random, .4f, 1, 0, 1, 5, 0, 5, Blocks.COBBLESTONE.getDefaultState());
        this.chanceFill(world, box, random, .1f, 1, 0, 1, 5, 0, 5, Blocks.STONEBRICK.getDefaultState());

        // Fill with air
        this.fill(world, box, 1, 1, 1, 5, 2, 5, AIR);
        this.fill(world, box, 2, 3, 2, 4, 3, 4, AIR);

        // Place door
        this.fill(world, box, 3, 1, 0, 3, 2, 0, AIR);
        this.setBlockState(world, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER), 3, 1, 0, box);
        this.setBlockState(world, Blocks.IRON_DOOR.getDefaultState().withProperty(BlockDoor.FACING, EnumFacing.NORTH).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER), 3, 2, 0, box);

        // Window
        this.fill(world, box, 6, 2, 2, 6, 2, 4, Blocks.IRON_BARS.getDefaultState());

        // Beds
        this.setBlockState(world, Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.NORTH).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT), 1, 1, 4, box);
        this.setBlockState(world, Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.NORTH).withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 1, 1, 5, box);
        TileEntity blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(1, 4), this.getYWithOffset(1), this.getZWithOffset(1, 4)));
        if (blockEntity instanceof TileEntityBed) {
            ((TileEntityBed)blockEntity).setColor(EnumDyeColor.BLACK);
        }
        blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(1, 5), this.getYWithOffset(1), this.getZWithOffset(1, 5)));
        if (blockEntity instanceof TileEntityBed) {
            ((TileEntityBed)blockEntity).setColor(EnumDyeColor.BLACK);
        }

        this.setBlockState(world, Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.NORTH).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT), 5, 1, 4, box);
        this.setBlockState(world, Blocks.BED.getDefaultState().withProperty(BlockBed.FACING, EnumFacing.NORTH).withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD), 5, 1, 5, box);
        blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(5, 4), this.getYWithOffset(1), this.getZWithOffset(5, 4)));
        if (blockEntity instanceof TileEntityBed) {
            ((TileEntityBed)blockEntity).setColor(EnumDyeColor.BLACK);
        }
        blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5)));
        if (blockEntity instanceof TileEntityBed) {
            ((TileEntityBed)blockEntity).setColor(EnumDyeColor.BLACK);
        }

        // Mob spawner
        BlockPos spawnerPos = new BlockPos(this.getXWithOffset(3,3), this.getYWithOffset(0), this.getZWithOffset(3, 3));
        world.setBlockState(spawnerPos, Blocks.MOB_SPAWNER.getDefaultState(), 2);
        blockEntity = world.getTileEntity(spawnerPos);
        if (blockEntity instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)blockEntity).getSpawnerBaseLogic().setEntityId(new ResourceLocation("zombie_villager"));
        }

        // Wall with redstone torch in corner
        this.setBlockState(world, Blocks.COBBLESTONE_WALL.getDefaultState(), 1, 1, 1, box);
        this.setBlockState(world, Blocks.REDSTONE_TORCH.getDefaultState(), 1, 2, 1, box);

        // Chest
        this.generateChest(world, box, random, 5, 1, 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);

        // Button for door (inside)
        this.setBlockState(world, Blocks.STONE_BUTTON.getDefaultState(), 2, 2, 1, box);

        // Anvil
        this.setBlockState(world, Blocks.ANVIL.getDefaultState(), 5, 1, 2, box);

        // Decoration block (smithing table, crafting table, blast furnace)
        if (random.nextFloat() < .33f)
            this.setBlockState(world, Blocks.BOOKSHELF.getDefaultState(), 2, 1, 5, box);
        else if (random.nextFloat() < .67f)
            this.setBlockState(world, Blocks.CRAFTING_TABLE.getDefaultState(), 2, 1, 5, box);
        else
            this.setBlockState(world, Blocks.FURNACE.getDefaultState(), 2, 1, 5, box);

        // Cobwebs
        this.chanceFill(world, box, random, .3f, 2, 3, 2, 4, 3, 4, Blocks.WEB.getDefaultState());

        return true;
    }
}
