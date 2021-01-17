package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.yungsapi.world.SurfaceHelper;
import net.minecraft.block.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ColumnPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class VerticalEntrance extends MineshaftPiece {
    private BlockPos centerPos;
    private int  // height of vertical shaft depends on surface terrain
        yAxisLen  = 0,
        localYEnd = 0;
    private int // Surface tunnel vars
        tunnelLength        = 0,
        tunnelFloorAltitude = 0;
    private Direction tunnelDirection = Direction.NORTH;
    private boolean hasTunnel = false;

    // Vertical shaft static vars
    private static final int
        SHAFT_LOCAL_XZ_START = 22,
        SHAFT_LOCAL_XZ_END = 26;

    public VerticalEntrance(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.VERTICAL_ENTRANCE, compoundTag);
        int centerPosX = compoundTag.getIntArray("centerPos")[0];
        int centerPosY = compoundTag.getIntArray("centerPos")[1];
        int centerPosZ = compoundTag.getIntArray("centerPos")[2];
        this.centerPos = new BlockPos(centerPosX, centerPosY, centerPosZ);

        this.yAxisLen = compoundTag.getInt("yAxisLen");
        this.localYEnd = this.yAxisLen - 1;
        this.tunnelLength = compoundTag.getInt("tunnelLen");
        this.tunnelFloorAltitude = compoundTag.getInt("floorAltitude");

        int tunnelDirInt = compoundTag.getInt("tunnelDir");
        this.tunnelDirection = tunnelDirInt == -1 ? null : Direction.byHorizontalIndex(tunnelDirInt);

        this.hasTunnel = compoundTag.getBoolean("hasTunnel");
    }

    public VerticalEntrance(int i, int pieceChainLen, Random random, BlockPos.Mutable centerPos, Direction direction, MineshaftVariantSettings settings) {
        super(BetterMineshaftStructurePieceType.VERTICAL_ENTRANCE, i, pieceChainLen, settings);
        this.setCoordBaseMode(direction);
        this.centerPos = new BlockPos(centerPos); // position passed in is center of shaft piece (unlike all other pieces, where it is a corner)
        this.boundingBox = getInitialMutableBoundingBox(centerPos);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
        tag.putIntArray("centerPos", new int[]{centerPos.getX(), centerPos.getY(), centerPos.getZ()});
        tag.putInt("yAxisLen", yAxisLen);
        tag.putInt("tunnelLen", tunnelLength);
        tag.putInt("floorAltitude", tunnelFloorAltitude);
        tag.putInt("tunnelDir", tunnelDirection.getHorizontalIndex());
        tag.putBoolean("hasTunnel", hasTunnel);
    }

    private static MutableBoundingBox getInitialMutableBoundingBox(BlockPos centerPos) {
        return new MutableBoundingBox(centerPos.getX() - 24, centerPos.getY(), centerPos.getZ() - 24, centerPos.getX() + 24, 256, centerPos.getZ() + 24);
    }

    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        Direction direction = this.getCoordBaseMode();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.centerPos.getX() - 4, this.centerPos.getY(), this.centerPos.getZ() - 3, direction, this.getComponentType(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.centerPos.getX() + 4, this.centerPos.getY(), this.centerPos.getZ() + 3, direction, this.getComponentType(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.centerPos.getX() - 3, this.centerPos.getY(), this.centerPos.getZ() + 4, direction, this.getComponentType(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.centerPos.getX() + 3, this.centerPos.getY(), this.centerPos.getZ() - 4, direction, this.getComponentType(), pieceChainLen);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random random, MutableBoundingBox box, ChunkPos pos, BlockPos blockPos) {
        if (BetterMineshafts.DEBUG_LOG) {
            BetterMineshafts.count.incrementAndGet();
        }

        // Only generate vertical entrance if there is valid surrounding terrain
        if (!this.hasTunnel) {
            determineDirection(world);

            if (BetterMineshafts.DEBUG_LOG && this.hasTunnel) {
                BetterMineshafts.surfaceEntrances.add(this.centerPos.hashCode());
                BetterMineshafts.LOGGER.info(String.format("(%d, %d) --- %d / %d  (%f%%)", centerPos.getX(), centerPos.getZ(), BetterMineshafts.surfaceEntrances.size(), BetterMineshafts.count.get(), (float) BetterMineshafts.surfaceEntrances.size() * 100 / BetterMineshafts.count.get()));
            }
        }

        if (this.hasTunnel) {
            generateVerticalShaft(world, random, box);
            // Build surface tunnel.
            // This must be done dynamically since its length depends on terrain.
            generateSurfaceTunnel(world, random, box);

            return true;
        }

        return false;
    }

    /**
     * Generates the vertical shaft with a ladder.
     */
    private void generateVerticalShaft(ISeedReader world, Random random, MutableBoundingBox box) {
        // Randomize blocks
        this.fill(world, box, random, SHAFT_LOCAL_XZ_START, 0, SHAFT_LOCAL_XZ_START, SHAFT_LOCAL_XZ_END, localYEnd, SHAFT_LOCAL_XZ_END, getMainSelector());

        // Fill with air
        this.fill(world, box, SHAFT_LOCAL_XZ_START + 1, 1, SHAFT_LOCAL_XZ_START + 1, SHAFT_LOCAL_XZ_END - 1, localYEnd - 1, SHAFT_LOCAL_XZ_END - 1, CAVE_AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, SHAFT_LOCAL_XZ_START + 1, 0, SHAFT_LOCAL_XZ_START + 1, SHAFT_LOCAL_XZ_END - 1, 0, SHAFT_LOCAL_XZ_END - 1, getMainBlock());

        // Ladder
        this.replaceAir(world, box, SHAFT_LOCAL_XZ_START + 2, 1, SHAFT_LOCAL_XZ_START, SHAFT_LOCAL_XZ_START + 2, localYEnd - 4, SHAFT_LOCAL_XZ_START, getMainBlock());
        this.fill(world, box, SHAFT_LOCAL_XZ_START + 2, 1, SHAFT_LOCAL_XZ_START + 1, SHAFT_LOCAL_XZ_START + 2, localYEnd - 4, SHAFT_LOCAL_XZ_START + 1, Blocks.LADDER.getDefaultState());

        // Doorway
        this.fill(world, box, SHAFT_LOCAL_XZ_START + 1, 1, SHAFT_LOCAL_XZ_START + 4, SHAFT_LOCAL_XZ_START + 3, 2, SHAFT_LOCAL_XZ_START + 4, getMainDoorwayWall());
        this.fill(world, box, SHAFT_LOCAL_XZ_START + 2, 3, SHAFT_LOCAL_XZ_START + 4, SHAFT_LOCAL_XZ_START + 2, 3, SHAFT_LOCAL_XZ_START + 4, getMainDoorwaySlab());
        this.fill(world, box, SHAFT_LOCAL_XZ_START + 2, 1, SHAFT_LOCAL_XZ_START + 4, SHAFT_LOCAL_XZ_START + 2, 2, SHAFT_LOCAL_XZ_START + 4, CAVE_AIR);

        // Decorations
        this.addBiomeDecorations(world, box, random, SHAFT_LOCAL_XZ_START + 1, 0, SHAFT_LOCAL_XZ_START + 1, SHAFT_LOCAL_XZ_END - 1, 1, SHAFT_LOCAL_XZ_END - 1);
        this.addVines(world, box, random, settings.replacementRate, SHAFT_LOCAL_XZ_START + 1, 0, SHAFT_LOCAL_XZ_START + 1, SHAFT_LOCAL_XZ_END - 1, localYEnd - 4, SHAFT_LOCAL_XZ_END - 1);
    }

    /**
     * Generates tunnel from top of vertical shaft to nearby open cliff-side.
     * The code for this is quite ugly as a result of rotation being handled manually, rather than internally
     * as is normally the case. Rotation logic must be handled manually for this piece because the piece's orientation
     * relies on the surrounding terrain, which can't be determined until generation time.
     */
    private void generateSurfaceTunnel(ISeedReader world, Random random, MutableBoundingBox box) {
        int tunnelStartX = 0,
            tunnelStartZ = 0,
            tunnelEndX = 0,
            tunnelEndZ = 0;
        Direction facing = this.getCoordBaseMode();

        // We have to account for this piece's rotation.
        // This is normally handled internally, but must be tweaked manually for surface tunnels
        // since their orientation is not determined until generation time.
        float rotationDifference = facing.getHorizontalAngle() - tunnelDirection.getHorizontalAngle();
        Direction relativeTunnelDir = Direction.fromAngle(Direction.NORTH.getHorizontalAngle() - rotationDifference);
        if (relativeTunnelDir == Direction.NORTH) {
            tunnelStartX = 22;
            tunnelStartZ = 26;
            tunnelEndX = 26;
            tunnelEndZ = 26 + tunnelLength;
        }
        else if (
            (relativeTunnelDir == Direction.WEST && !(facing == Direction.SOUTH || facing == Direction.WEST)) ||
            (relativeTunnelDir == Direction.EAST && (facing == Direction.SOUTH || facing == Direction.WEST))
        ) {
            tunnelStartX = 22 - tunnelLength;
            tunnelStartZ = 22;
            tunnelEndX = 22;
            tunnelEndZ = 26;
        }
        else if (relativeTunnelDir == Direction.SOUTH) {
            tunnelStartX = 22;
            tunnelStartZ = 22 - tunnelLength;
            tunnelEndX = 26;
            tunnelEndZ = 22;
        }
        else if (
            relativeTunnelDir == Direction.EAST || relativeTunnelDir == Direction.WEST
        ) {
            tunnelStartX = 26;
            tunnelStartZ = 22;
            tunnelEndX = 26 + tunnelLength;
            tunnelEndZ = 26;
        }

        // ################################################################
        // #                            Tunnel                            #
        // ################################################################

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, settings.replacementRate, tunnelStartX, tunnelFloorAltitude, tunnelStartZ, tunnelEndX, tunnelFloorAltitude + 4, tunnelEndZ, getMainSelector());

        if (facing.getAxis() == tunnelDirection.getAxis()) {
            // Fill in any air in floor with main block
            this.replaceAir(world, box, tunnelStartX + 1, tunnelFloorAltitude, tunnelStartZ, tunnelEndX - 1, tunnelFloorAltitude, tunnelEndZ, getMainBlock());

            // Fill with air
            this.fill(world, box, tunnelStartX + 1, tunnelFloorAltitude + 1, tunnelStartZ, tunnelEndX - 1, tunnelFloorAltitude + 3, tunnelEndZ, CAVE_AIR);
        } else {
            // Fill in any air in floor with main block
            this.replaceAir(world, box, tunnelStartX, tunnelFloorAltitude, tunnelStartZ + 1, tunnelEndX, tunnelFloorAltitude, tunnelEndZ - 1, getMainBlock());

            // Fill with air
            this.fill(world, box, tunnelStartX, tunnelFloorAltitude + 1, tunnelStartZ + 1, tunnelEndX, tunnelFloorAltitude + 3, tunnelEndZ - 1, CAVE_AIR);
        }

        // Vines
        this.addVines(world, box, random, settings.vineChance, tunnelStartX + 1, tunnelFloorAltitude, tunnelStartZ + 1, tunnelEndX - 1, tunnelFloorAltitude + 4, tunnelEndZ - 1);

        // ################################################################
        // #                       Supports & Rails                       #
        // ################################################################
        // Note that while normally, building (i.e. determining placement) of things like supports is done before generation,
        // that is not the case here since localZEnd is not known until generation.

        // Mark positions where center floor block is solid
        boolean[] validPositions;
        if (facing.getAxis() == tunnelDirection.getAxis()) {
            validPositions = new boolean[tunnelEndZ - tunnelStartZ + 1];
            for (int z = 0; z < validPositions.length; z++) {
                BlockState floorBlock = this.getBlockStateFromPos(world, tunnelStartX + 2, tunnelFloorAltitude, tunnelStartZ + z, box);
                if (floorBlock.getMaterial().isSolid()) {
                    validPositions[z] = true;
                }
            }
        } else {
            validPositions = new boolean[tunnelEndX - tunnelStartX + 1];
            for (int x = 0; x < validPositions.length; x++) {
                BlockState floorBlock = this.getBlockStateFromPos(world, tunnelStartX + x, tunnelFloorAltitude, tunnelStartZ + 2, box);
                if (floorBlock.getMaterial().isSolid()) {
                    validPositions[x] = true;
                }
            }
        }

        // Generate supports.
        if (facing.getAxis() == tunnelDirection.getAxis()) {
            BlockState supportBlock = getSupportBlock();
            if (supportBlock.getBlock() instanceof WallBlock) {
                supportBlock = supportBlock.with(WallBlock.WALL_HEIGHT_EAST, WallHeight.TALL).with(WallBlock.WALL_HEIGHT_WEST, WallHeight.TALL);
            } else if (supportBlock.getBlock() instanceof FourWayBlock) {
                supportBlock = supportBlock.with(FourWayBlock.EAST, true).with(FourWayBlock.WEST, true);
            }
            for (int z = tunnelStartZ; z <= tunnelEndZ; z++) {
                int r = random.nextInt(4);
                if (r == 0 && validPositions[z - tunnelStartZ]) {
                    // Support
                    this.fill(world, box, tunnelStartX + 1, tunnelFloorAltitude + 1, z, tunnelStartX + 1, tunnelFloorAltitude + 2, z, getSupportBlock());
                    this.fill(world, box, tunnelStartX + 3, tunnelFloorAltitude + 1, z, tunnelStartX + 3, tunnelFloorAltitude + 2, z, getSupportBlock());
                    this.fill(world, box, tunnelStartX + 1, tunnelFloorAltitude + 3, z, tunnelStartX + 3, tunnelFloorAltitude + 3, z, getMainBlock());
                    this.chanceReplaceNonAir(world, box, random, .25f, tunnelStartX + 1, tunnelFloorAltitude + 3, z, tunnelStartX + 3, tunnelFloorAltitude + 3, z, supportBlock);

                    // Cobwebs
                    this.chanceReplaceAir(world, box, random, .15f, tunnelStartX + 1, tunnelFloorAltitude + 3, z - 1, tunnelStartX + 1, tunnelFloorAltitude + 3, z + 1, Blocks.COBWEB.getDefaultState());
                    this.chanceReplaceAir(world, box, random, .15f, tunnelStartX + 3, tunnelFloorAltitude + 3, z - 1, tunnelStartX + 3, tunnelFloorAltitude + 3, z + 1, Blocks.COBWEB.getDefaultState());
                    z += 3;
                }
            }
        } else {
            BlockState supportBlock = getSupportBlock();
            if (supportBlock.getBlock() instanceof WallBlock) {
                supportBlock = supportBlock.with(WallBlock.WALL_HEIGHT_NORTH, WallHeight.TALL).with(WallBlock.WALL_HEIGHT_SOUTH, WallHeight.TALL);
            } else if (supportBlock.getBlock() instanceof FourWayBlock) {
                supportBlock = supportBlock.with(FourWayBlock.NORTH, true).with(FourWayBlock.SOUTH, true);
            }
            for (int x = tunnelStartX; x <= tunnelEndX; x++) {
                int r = random.nextInt(4);
                // TOOD - fix z to account for direction of shaft
                if (r == 0 && validPositions[x - tunnelStartX]) {
                    // Support
                    this.fill(world, box, x, tunnelFloorAltitude + 1, tunnelStartZ + 1, x, tunnelFloorAltitude + 2, tunnelStartZ + 1, getSupportBlock());
                    this.fill(world, box, x, tunnelFloorAltitude + 1, tunnelStartZ + 3, x, tunnelFloorAltitude + 2, tunnelStartZ + 3, getSupportBlock());
                    this.fill(world, box, x, tunnelFloorAltitude + 3, tunnelStartZ + 1, x, tunnelFloorAltitude + 3, tunnelStartZ + 3, getMainBlock());
                    this.chanceReplaceNonAir(world, box, random, .25f, x, tunnelFloorAltitude + 3, tunnelStartZ + 1, x, tunnelFloorAltitude + 3, tunnelStartZ + 3, supportBlock);

                    // Cobwebs
                    this.chanceReplaceAir(world, box, random, .15f, x - 1, tunnelFloorAltitude + 3, tunnelStartZ + 1, x + 1, tunnelFloorAltitude + 3, tunnelStartZ + 1, Blocks.COBWEB.getDefaultState());
                    this.chanceReplaceAir(world, box, random, .15f, x - 1, tunnelFloorAltitude + 3, tunnelStartZ + 3, x + 1, tunnelFloorAltitude + 3, tunnelStartZ + 3, Blocks.COBWEB.getDefaultState());
                    x += 3;
                }
            }
        }

        // Generate rails.
        RailShape railShape = relativeTunnelDir.getAxis() == Direction.Axis.Z ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST;
        if (facing.getAxis() == tunnelDirection.getAxis()) {
            for (int z = 0; z < tunnelEndZ - tunnelStartZ + 1; z++) {
                if (validPositions[z] && random.nextFloat() < .5) {
                    this.setBlockState(world, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, railShape), tunnelStartX + 2, tunnelFloorAltitude + 1, tunnelStartZ + z, box);
                }
            }
        } else {
            for (int x = 0; x < tunnelEndX - tunnelStartX + 1; x++) {
                if (validPositions[x] && random.nextFloat() < .5) {
                    this.setBlockState(world, Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, railShape), tunnelStartX + x, tunnelFloorAltitude + 1, tunnelStartZ + 2, box);
                }
            }
        }
    }

    /**
     * Determines the direction to spawn the surface tunnel in.
     * Tries to find a direction in which there is a drop-off, with the goal of creating an opening
     * in the face of a mountain or hill.
     */
    private void determineDirection(ISeedReader world) {
        int minSurfaceHeight = 255;

        // Set height for this, equal to 2 below the min height in the 5x5 vertical shaft piece
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                try {
                    int realX = centerPos.getX() + xOffset,
                        realZ = centerPos.getZ() + zOffset;
                    int chunkX = realX >> 4,
                        chunkZ = realZ >> 4;
                    int surfaceHeight = SurfaceHelper.getSurfaceHeight(world.getChunk(chunkX, chunkZ), new ColumnPos(realX, realZ));
                    if (surfaceHeight > 1) {
                        minSurfaceHeight = Math.min(minSurfaceHeight, surfaceHeight);
                    }
                } catch (NullPointerException e) {
                    BetterMineshafts.LOGGER.error("Unexpected YUNG's Better Mineshafts error. Please report this!");
                    BetterMineshafts.LOGGER.error(e.toString());
                    BetterMineshafts.LOGGER.error(e.getMessage());
                }
            }
        }

        // Require surface opening to be at high-ish altitude
        if (minSurfaceHeight < 60 || minSurfaceHeight == 255) return;

        int ceilingHeight = minSurfaceHeight - 2;
        int floorHeight = ceilingHeight - 4;

        // Update
        yAxisLen = ceilingHeight - centerPos.getY() + 1;
        localYEnd = yAxisLen - 1;

        BlockPos.Mutable mutable = centerPos.toMutable();

        int radius = 8; // Number of blocks that constitutes one 'radius'
        int maxRadialDist = 3; // Number of radii to check in each direction. E.g. 3 radii * radius of 8 = 24 blocks

        for (int radialDist = 0; radialDist < maxRadialDist; radialDist++) {
            // Check each of the four cardinal directions
            for (Direction direction : Direction.values()) {
                if (direction == Direction.UP || direction == Direction.DOWN) continue;

                mutable.setPos(centerPos.offset(direction, radius * radialDist + 2));

                // Check altitude of each individual block along the direction.
                for (int i = radialDist * radius; i < radialDist * radius + radius; i++) {
                    int surfaceHeight = SurfaceHelper.getSurfaceHeight(world.getChunk(mutable), new ColumnPos(mutable.getX(), mutable.getZ()));

                    if (surfaceHeight <= floorHeight && surfaceHeight > 1) {
                        this.hasTunnel = true;
                        this.tunnelDirection = direction;
                        this.tunnelFloorAltitude = ceilingHeight - 4 - this.boundingBox.minY;
                        this.tunnelLength = i;
                        return;
                    }

                    mutable.move(direction);
                }
            }
        }
    }
}
