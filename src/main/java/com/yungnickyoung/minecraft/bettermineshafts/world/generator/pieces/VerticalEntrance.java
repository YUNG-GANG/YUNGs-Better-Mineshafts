package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.*;
import net.minecraft.world.Heightmap;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class VerticalEntrance extends MineshaftPiece {
    private BlockPos startPos;
    private BlockPos centerPos;
    private SurfaceTunnel surfaceTunnel; // reference to the surface tunnel attached to this
    private int  // height of vertical shaft depends on surface terrain
        yAxisLen,
        localYEnd;

    private static final int
        SECONDARY_AXIS_LEN = 7,
        MAIN_AXIS_LEN = 5;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;


    public VerticalEntrance(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.VERTICAL_ENTRANCE, compoundTag);
    }

    public VerticalEntrance(int i, int pieceChainLen, Direction direction, BlockBox blockBox, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.VERTICAL_ENTRANCE, i, pieceChainLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    public VerticalEntrance(int i, int pieceChainLen, BlockPos pos, Direction direction, BetterMineshaftFeature.Type type) {
        this(i, pieceChainLen, direction, determineInitialBoxPosition(pos.getX(), pos.getY(), pos.getZ(), direction), type);
        this.startPos = pos;
        this.centerPos = calcCenterBlockPos();
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
    }

    private BlockPos calcCenterBlockPos() {
        if (this.getFacing() == null) return startPos;

        int startX = startPos.getX(),
            startY = startPos.getY(),
            startZ = startPos.getZ();

        switch (this.getFacing()) {
            default:
            case NORTH:
                return new BlockPos(startX + 3, startY, startZ - 2);
            case SOUTH:
                return new BlockPos(startX - 3, startY, startZ + 2);
            case EAST:
                return new BlockPos(startX + 2, startY, startZ + 3);
            case WEST:
                return new BlockPos(startX - 2, startY, startZ - 3);
        }
    }

    public static BlockBox determineInitialBoxPosition(int x, int y, int z, Direction direction) {
        // Use yLen of 30 as placeholder estimation since we won't know the true Y_AXIS_LEN until we generate,
        // since the y-coordinate depends on surrounding surface terrain.
        return BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, 30, MAIN_AXIS_LEN, direction);
    }

    @Override
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        // Add surface tunnel (with placeholder values) and first big tunnel piece
        switch (direction) {
            case NORTH:
            default:
                surfaceTunnel = (SurfaceTunnel) BetterMineshaftGenerator.generateAndAddSurfaceTunnel(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minZ - 1, this.method_14923());
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, this.method_14923(), pieceChainLen);
                break;
            case SOUTH:
                surfaceTunnel = (SurfaceTunnel) BetterMineshaftGenerator.generateAndAddSurfaceTunnel(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.maxZ + 1, this.method_14923());
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, this.method_14923(), pieceChainLen);
                break;
            case WEST:
                surfaceTunnel = (SurfaceTunnel) BetterMineshaftGenerator.generateAndAddSurfaceTunnel(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.maxZ, this.method_14923());
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, this.method_14923(), pieceChainLen);
                break;
            case EAST:
                surfaceTunnel = (SurfaceTunnel) BetterMineshaftGenerator.generateAndAddSurfaceTunnel(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minZ, this.method_14923());
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, this.method_14923(), pieceChainLen);
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        DirInfo dirInfo = determineDirection(world);
        // Only generate vertical entrance if there is valid surrounding terrain
        if (dirInfo != null) {
            generateVerticalEntrance(world, random, box);
            // Build surface tunnel.
            // This must be done dynamically since its length depends on terrain.
            int floorAltitude = dirInfo.ceilingAltitude - 4;
            Direction surfaceTunnelDir = dirInfo.direction;
            int mainAxisLen = dirInfo.horizontalLen;

            surfaceTunnel.setOrientation(surfaceTunnelDir);
            surfaceTunnel.setFloorAltitude(floorAltitude);
            surfaceTunnel.setMainAxisLength(mainAxisLen);
            surfaceTunnel.updatePosition(centerPos, this.getFacing());
            surfaceTunnel.updateBoundingBox();

            return true;
        }

        return false;
    }

    private void generateVerticalEntrance(IWorld world, Random random, BlockBox box) {
        // Fill with stone then clean out with air
        this.fillWithOutline(world, box, 1, 0, 0, LOCAL_X_END - 1, localYEnd, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), false);
        this.fillWithOutline(world, box, 2, 1, 1, LOCAL_X_END - 2, localYEnd, LOCAL_Z_END - 1, AIR, AIR, false);

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, localYEnd, LOCAL_Z_END, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, localYEnd, LOCAL_Z_END, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, localYEnd, LOCAL_Z_END, getMainBlock(), getMainBlock(), true);
        this.randomFillWithOutline(world, box, random, .1f, 1, 0, 0, LOCAL_X_END - 1, localYEnd, LOCAL_Z_END, AIR, AIR, true);

        // Add random blocks in floor
        this.randomFillWithOutline(world, box, random, .4f, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1, getMainBlock(), AIR, false);

        // Doorway
        this.fillWithOutline(world, box, 0, 0, LOCAL_Z_END, LOCAL_X_END, 6, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.fillWithOutline(world, box, 2, 1, LOCAL_Z_END, 4, 3, LOCAL_Z_END, AIR, AIR, true);

        // Ladder
        this.fillWithOutline(world, box, 3, 1, 0, 3, localYEnd, 0, Blocks.STONE_BRICKS.getDefaultState(), AIR, false);
        this.fillWithOutline(world, box, 3, 1, 1, 3, localYEnd, 1, Blocks.LADDER.getDefaultState(), AIR, false);
    }

    /**
     * Determines the direction to spawn the structure in.
     * Tries to find a direction in which there is a drop-off, with the goal of creating an opening
     * in the face of a mountain or hill.
     *
     * @return The direction, or null if no drop-off nearby. If null, no vertical entrance/surface opening will be
     * made for this mineshaft.
     */
    private DirInfo determineDirection(IWorld world) {
        int minSurfaceHeight = 255;

        // Set height for this, equal to 2 below the min height in the 5x5 vertical shaft piece
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                int realX = centerPos.getX() + xOffset,
                    realZ = centerPos.getZ() + zOffset;
                int chunkX = realX >> 4,
                    chunkZ = realZ >> 4;
                int localX = realX & 0xF,
                    localZ = realZ & 0xF;

                try {
                    minSurfaceHeight = Math.min(minSurfaceHeight, world.getChunk(chunkX, chunkZ).getHeightmap(Heightmap.Type.WORLD_SURFACE_WG).get(localX, localZ));
                } catch (NullPointerException e) {
                    BetterMineshafts.LOGGER.error(e.getStackTrace());
                }
            }
        }

        // Require surface opening to be at high-ish altitude
        if (minSurfaceHeight < 70 || minSurfaceHeight == 255) return null;

        int ceilingHeight = minSurfaceHeight - 2;
        int floorHeight = ceilingHeight - 4;

        // Update
        yAxisLen = ceilingHeight - startPos.getY() + 1;
        localYEnd = yAxisLen - 1;

        BlockPos.Mutable mutable = new BlockPos.Mutable(centerPos);

        int radius = 8; // Number of blocks that constitutes one 'radius'
        int maxRadialDist = 3; // Number of radii to check in each direction. E.g. 3 radii * radius of 8 = 24 blocks
        int minDistToDropoff = 6; // Minimum required distance in a given direction before reaching a drop-off.
        // Prevents the vertical entrance itself from being partly on the edge of a drop-off,
        // and also ensures there will be at least a tiny bit of tunnel leading to the vertical entrance.

        for (int radialDist = 0; radialDist < maxRadialDist; radialDist++) {
            // Check each of the four cardinal directions
            for (Direction direction : Direction.values()) {
                if (direction == Direction.UP || direction == Direction.DOWN) continue;

                // Get start and end
                BlockPos startPos = mutable.offset(direction, 0);
                BlockPos endPos = startPos.offset(direction, 7);

                mutable.set(startPos);

                // Check altitude of each individual block along the direction.
                for (int i = radialDist * radius; i < radialDist * radius + radius; i++) {
                    int surfaceHeight = world.getChunk(mutable).getHeightmap(Heightmap.Type.WORLD_SURFACE).get(mutable.getX() & 15, mutable.getZ() & 15);

                    // Too early for a drop-off
                    if (i < minDistToDropoff && surfaceHeight < minSurfaceHeight) break;

                    if (surfaceHeight <= floorHeight) return new DirInfo(direction, i, ceilingHeight);

                    mutable.setOffset(direction);
                }
            }
        }
        return null;
        // NOTE -- this func is using the center of the vertical shaft as the starting point, so I will need to adjust the coordinates accordingly when
        // creating the vertical shaft itself
    }

    private static class DirInfo {
        public Direction direction;
        public int horizontalLen;
        public int ceilingAltitude;

        public DirInfo(Direction dir, int len, int ceilHeight) {
            this.direction = dir;
            this.horizontalLen = len;
            this.ceilingAltitude = ceilHeight;
        }
    }
}
