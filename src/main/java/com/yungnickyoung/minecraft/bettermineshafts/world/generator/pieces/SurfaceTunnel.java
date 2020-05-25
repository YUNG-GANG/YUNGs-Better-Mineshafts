package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.*;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class SurfaceTunnel extends MineshaftPiece {
    private int // These vars will be defined later during generation, based on the surrounding terrain
        mainAxisLength = 0,
        localZEnd      = 0,
        floorAltitude  = 0;
    public ColumnPos startPos;

    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1;

    public SurfaceTunnel(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SURFACE_TUNNEL, compoundTag);
    }

    public SurfaceTunnel(int i, BetterMineshaftFeature.Type type, ColumnPos startPos) {
        super(BetterMineshaftStructurePieceType.SURFACE_TUNNEL, i, 0, type);
        this.startPos = startPos;
        // Initialize null bounding box to ensure this piece is added to the list.
        // The actual bounding box will be set during generation.
        this.boundingBox = BoxUtil.boxFromCoordsWithRotation(startPos.x, 0, startPos.z, 0, 0, 0, Direction.NORTH);
    }

    public void setMainAxisLength(int len) {
        this.mainAxisLength = len;
        this.localZEnd = mainAxisLength - 1;
    }

    public void setFloorAltitude(int altitude) {
        this.floorAltitude = altitude;
    }

    public void updatePosition(BlockPos shaftCenterPos, Direction verticalShaftDir) {
        int centerX = shaftCenterPos.getX(),
            centerZ = shaftCenterPos.getZ();

        switch (this.getFacing()) {
            default:
            case NORTH:
                if (verticalShaftDir.getAxis() == Direction.Axis.X)
                    this.startPos = new ColumnPos(centerX - 2, centerZ - 2);
                else
                    this.startPos = new ColumnPos(centerX - 2, centerZ - 2);
                break;
            case SOUTH:
                if (verticalShaftDir.getAxis() == Direction.Axis.X)
                    this.startPos = new ColumnPos(centerX + 2, centerZ + 2);
                else
                    this.startPos = new ColumnPos(centerX + 2, centerZ + 2);
                break;
            case EAST:
                if (verticalShaftDir.getAxis() == Direction.Axis.X)
                    this.startPos = new ColumnPos(centerX + 2, centerZ - 2);
                else
                    this.startPos = new ColumnPos(centerX + 2, centerZ - 2);
                break;
            case WEST:
                if (verticalShaftDir.getAxis() == Direction.Axis.X)
                    this.startPos = new ColumnPos(centerX - 2, centerZ + 2);
                else
                    this.startPos = new ColumnPos(centerX - 2, centerZ + 2);
                break;
        }
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
    }

    public void updateBoundingBox() {
        if (this.getFacing() == null) return;
        int x = startPos.x,
            z = startPos.z;

        setBoundingBox(BoxUtil.boxFromCoordsWithRotation(x, floorAltitude, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, mainAxisLength, this.getFacing()));
    }

    public BlockBox calcBoxPosition(List<StructurePiece> list, int x, int y, int z) {
        if (this.getFacing() == null) return null;
        BlockBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, mainAxisLength, this.getFacing());

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.method_14932(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        // Abort if fields haven't been defined
        if (floorAltitude == 0 || mainAxisLength == 0 || localZEnd == 0) {
            return false;
        }

        // Place floor
        this.fillWithOutline(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, localZEnd, getMainBlock(), getMainBlock(), true);
        this.randomFillWithOutline(world, box, random, .5f, 1, 0, 0, LOCAL_X_END - 1, 0, localZEnd, Blocks.STONE.getDefaultState(), Blocks.STONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, 0, localZEnd, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, localZEnd, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, localZEnd, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, localZEnd, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, localZEnd, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .2f, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, localZEnd, AIR, AIR, true);

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, localZEnd, AIR, AIR, false);

        // Decorations
        // Note that while normally, building (i.e. determining placement) of decorations is done before generation,
        // that is not the case here since localZEnd is not known until generation.
        generateSupports(world, box, random);
        generateRails(world, box, random);

        return true;
    }

    private void generateSupports(IWorld world, BlockBox box, Random random) {
        for (int z = 0; z <= localZEnd; z++) {
            int r = random.nextInt(7);

            if (r == 0) {
                // Support
                this.fillWithOutline(world, box, 1, 1, z, 1, 2, z, getSupportBlock(), getSupportBlock(), false);
                this.fillWithOutline(world, box, 3, 1, z, 3, 2, z, getSupportBlock(), getSupportBlock(), false);
                this.fillWithOutline(world, box, 1, 3, z, 3, 3, z, getMainBlock(), getMainBlock(), false);
                this.randomFillWithOutline(world, box, random, .25f, 1, 3, z, 3, 3, z, getSupportBlock(), getSupportBlock(), true);

                // Cobwebs
                this.randomlyReplaceAirInBox(world, box, random, .15f, 1, 3, z - 3, 1, 3, z + 3, Blocks.COBWEB.getDefaultState());
                this.randomlyReplaceAirInBox(world, box, random, .15f, 3, 3, z - 3, 3, 3, z + 3, Blocks.COBWEB.getDefaultState());
                z += 3;
            }
        }
    }

    private void generateRails(IWorld world, BlockBox box, Random random) {
        this.randomFillWithOutline(world, box, random, .5f, 2, 1, 0, 2, 1, localZEnd, Blocks.RAIL.getDefaultState(), Blocks.RAIL.getDefaultState(), false);
    }
}
