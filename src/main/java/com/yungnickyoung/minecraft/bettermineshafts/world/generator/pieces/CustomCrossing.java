package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
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

public class CustomCrossing extends MineshaftPart {

    public CustomCrossing(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.CUSTOM_CROSSING, compoundTag);
    }

    public CustomCrossing(int i, int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.CUSTOM_CROSSING, i, pieceChainLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = new BlockBox(x, y, z, x, y + 6, z);
        switch (direction) {
            case NORTH:
            default:
                blockBox.minX = x - 3;
                blockBox.maxX = x + 6;
                blockBox.minZ = z - 8;
                break;
            case SOUTH:
                blockBox.minX = x - 3;
                blockBox.maxX = x + 6;
                blockBox.maxZ = z + 8;
                break;
            case EAST:
                blockBox.maxX = x + 8;
                blockBox.minZ = z - 3;
                blockBox.maxZ = z + 6;
                break;
            case WEST:
                blockBox.minX = x - 8;
                blockBox.minZ = z - 3;
                blockBox.maxZ = z + 6;
        }

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

    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        int xEnd = 11 - 1,
            yEnd = 7 - 1,
            zEnd = 11 - 1;
        this.fillWithOutline(world, box, 0, 0, 0, xEnd, yEnd, zEnd, Blocks.STONE_BRICKS.getDefaultState(), AIR, false);
        this.fillWithOutline(world, box, 1, 1, 1, xEnd - 1, yEnd - 1, zEnd - 1, AIR, AIR, false);
        this.fillWithOutline(world, box, 4, 1, 0, 6, yEnd - 3, 0, AIR, AIR, false);
        return true;
    }
}