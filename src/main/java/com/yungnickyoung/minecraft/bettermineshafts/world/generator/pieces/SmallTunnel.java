package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BoxUtil;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class SmallTunnel extends MineshaftPart {
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 5,
        MAIN_AXIS_LEN = 16;

    public SmallTunnel(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, compoundTag);
    }

    public SmallTunnel(int i, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, i, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

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

        int xEnd = SECONDARY_AXIS_LEN - 1,
            yEnd = Y_AXIS_LEN - 1,
            zEnd = MAIN_AXIS_LEN - 1;

        // Place floor
        this.fillWithOutline(world, box, 1, 0, 0, xEnd - 1, 0, zEnd, Blocks.OAK_PLANKS.getDefaultState(), Blocks.OAK_PLANKS.getDefaultState(), false);
        // Randomize floor blocks
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, xEnd - 1, 0, zEnd, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, xEnd, yEnd, zEnd, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, xEnd, yEnd, zEnd, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, xEnd, yEnd, zEnd, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, xEnd, yEnd, zEnd, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .2f, 0, 1, 0, xEnd, yEnd, zEnd, AIR, AIR, true);

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 0, xEnd - 1, yEnd - 1, zEnd, AIR, AIR, false);

        return true;
    }
}
