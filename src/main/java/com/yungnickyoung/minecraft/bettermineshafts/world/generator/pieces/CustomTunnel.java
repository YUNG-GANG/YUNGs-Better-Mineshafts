package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class CustomTunnel extends MineshaftPart {
    private static final int X_LEN = 5, Y_LEN = 4;
    private int zSectionCount;

    public CustomTunnel(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.CUSTOM_TUNNEL, compoundTag);
        this.zSectionCount = compoundTag.getInt("Num");
    }

    public CustomTunnel(int i, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.CUSTOM_TUNNEL, i, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
        if (this.getFacing().getAxis() == Direction.Axis.Z) {
            this.zSectionCount = blockBox.getBlockCountZ() / 8;
        } else {
            this.zSectionCount = blockBox.getBlockCountX() / 8;
        }
    }

    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
        tag.putInt("Num", this.zSectionCount);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = new BlockBox(x, y, z, x, y + Y_LEN, z);

        // Test to see if we can make it length 32 blocks. Decrease by 8 until we can, or until we determine not possible at all
        int n;
        for (n = 32; n > 0; n -= 8) {
            switch (direction) {
                case NORTH:
                default:
                    blockBox.maxX = x + X_LEN;
                    blockBox.minZ = z - (n - 1);
                    break;
                case SOUTH:
                    blockBox.maxX = x + X_LEN;
                    blockBox.maxZ = z + (n - 1);
                    break;
                case WEST:
                    blockBox.minX = x - (n - 1);
                    blockBox.maxZ = z + X_LEN;
                    break;
                case EAST:
                    blockBox.maxX = x + (n - 1);
                    blockBox.maxZ = z + X_LEN;
            }

            // If the blockBox does not intersect with any pieces, break from the loop
            if (StructurePiece.method_14932(list, blockBox) == null) { // findIntersecting
                break;
            }
        }

        // Return null if we were unable to get a box that doesn't intersect with other pieces.
        // Otherwise return the box.
        return n > 0 ? blockBox : null;
    }

    /**
     * buildComponent
     */
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        int chainLen = this.method_14923(); // getComponentType
        Direction direction = this.getFacing();
        if (direction != null) {
            switch (direction) {
                case NORTH:
                default:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, chainLen);
                    break;
                case SOUTH:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, chainLen);
                    break;
                case WEST:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, direction, chainLen);
                    break;
                case EAST:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, direction, chainLen);
            }
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) { // check if box contains any liquid
//                return false;
        }
        int xEnd = X_LEN - 1,
            yEnd = Y_LEN - 1,
            zEnd = zSectionCount * 8 - 1;

        this.fillWithOutline(world, box, 0, 0, 0, xEnd, yEnd, zEnd, AIR, AIR, false);
        this.fillWithOutline(world, box, 0, 0, 0, xEnd, 0, zEnd, Blocks.DARK_OAK_PLANKS.getDefaultState(), AIR, false);

        // mark the corners
        // Coords are [x, z]
        // [0, 0] -> GOLD BLOCK
        this.fillWithOutline(world, box, 0, 0, 0, 0, 0, 0, Blocks.GOLD_BLOCK.getDefaultState(), AIR, false);
        // [0, endZ] -> REDSTONE BLOCK
        this.fillWithOutline(world, box, 0, 0, zEnd, 0, 0, zEnd, Blocks.REDSTONE_BLOCK.getDefaultState(), AIR, false);
        // [endX, 0] -> BLUE WOOL BLOCK
        this.fillWithOutline(world, box, xEnd, 0, 0, xEnd, 0, 0, Blocks.BLUE_WOOL.getDefaultState(), AIR, false);
        // [endX, endZ] -> DIAMOND BLOCK
        this.fillWithOutline(world, box, xEnd, 0, zEnd, xEnd, 0, zEnd, Blocks.DIAMOND_BLOCK.getDefaultState(), AIR, false);

        return true;
    }
}