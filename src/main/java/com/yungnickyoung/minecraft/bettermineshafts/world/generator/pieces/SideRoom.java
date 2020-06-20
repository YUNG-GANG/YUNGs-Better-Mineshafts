package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

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

    public SideRoom(int i, int pieceChainLen, Random random, StructureBoundingBox blockBox, EnumFacing direction, MapGenBetterMineshaft.Type type) {
        super(i, pieceChainLen, type);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void writeStructureToNBT(NBTTagCompound tag) {
        super.writeStructureToNBT(tag);
        tag.setBoolean("hasDownstairs", this.hasDownstairs);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readStructureFromNBT(NBTTagCompound tag, TemplateManager templateManager) {
        super.readStructureFromNBT(tag, templateManager);
        this.hasDownstairs = tag.getBoolean("hasDownstairs");
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
        // Chance of generating side room dungeon downstairs
        if (random.nextInt(4) == 0) {
            EnumFacing direction = this.getCoordBaseMode();
            if (direction == null) {
                return;
            }

            StructureComponent newDungeonPiece = null;
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
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        // Don't spawn if liquid in this box
        if (this.isLiquidInStructureBoundingBox(world, box)) {
            return false;
        }

        // Fill with stone then clean out with air
        this.fill(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getBrickSelector());
        this.fill(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        if (!hasDownstairs)
            generateLegs(world, random);

        // Furnace 1
        if (random.nextInt(2) == 0) {
            this.setBlockState(world, Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH), 2, 1, 1, box);
            TileEntity blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(2, 1), this.getYWithOffset(1), this.getZWithOffset(2, 1)));
            if (blockEntity instanceof TileEntityFurnace) {
                ((TileEntityFurnace)blockEntity).setInventorySlotContents(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Furnace 2
        if (random.nextInt(2) == 0) {
            this.setBlockState(world, Blocks.FURNACE.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH), 1, 1, 1, box);
            TileEntity blockEntity = world.getTileEntity(new BlockPos(this.getXWithOffset(1, 1), this.getYWithOffset(1), this.getZWithOffset(1, 1)));
            if (blockEntity instanceof TileEntityFurnace) {
                ((TileEntityFurnace)blockEntity).setInventorySlotContents(1, new ItemStack(Items.COAL, random.nextInt(33)));
            }
        }

        // Crafting table
        this.chanceAddBlock(world, random, .5f, Blocks.CRAFTING_TABLE.getDefaultState(), 3, 1, 1, box);

        // Chest with loot
        if (random.nextInt(4) == 0) {
            this.generateChest(world, box, random, LOCAL_X_END - 1, 1, 1, LootTableList.CHESTS_ABANDONED_MINESHAFT);
        }

        // Entrance to spider lair
        if (this.hasDownstairs) {
            this.setBlockState(world, Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.NORTH), 6, 0, 1, box);
            this.setBlockState(world, getTrapdoor().withProperty(BlockTrapDoor.FACING, EnumFacing.NORTH), 6, 1, 1, box);
        }

        // Decorations
        generateIronBarSupports(world, box, random);
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }

    private void generateLegs(World world, Random random) {
        generateLeg(world, random, 1, 1, getBrickSelector());
        generateLeg(world, random, 1, LOCAL_Z_END - 1, getBrickSelector());
        generateLeg(world, random, LOCAL_X_END - 1, 1, getBrickSelector());
        generateLeg(world, random, LOCAL_X_END - 1, LOCAL_Z_END - 1, getBrickSelector());
    }

    private void generateIronBarSupports(World world, StructureBoundingBox box, Random random) {
        List<Integer> invalidXs = Lists.newLinkedList(); // Prevent columns of bars from spawning adjacent to eachother
        for (int z = 2; z <= 3; z++) {
            for (int x = 2; x <= 7; x++) {
                if (invalidXs.contains(x)) {
                    continue;
                }
                if (random.nextInt(5) == 0) {
                    this.fill(world, box, x, 1, z, x, 3, z, Blocks.IRON_BARS.getDefaultState());
                    invalidXs.add(x);
                    x++;
                }
            }
        }
    }
}
