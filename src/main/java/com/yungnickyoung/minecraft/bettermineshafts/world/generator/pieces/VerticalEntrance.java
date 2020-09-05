package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.util.ColPos;
import com.yungnickyoung.minecraft.bettermineshafts.util.SurfaceUtil;
import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;

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
    private EnumFacing tunnelEnumFacing = EnumFacing.NORTH;
    private boolean hasTunnel = false;

    public VerticalEntrance() {}

    public VerticalEntrance(int i, int pieceChainLen, Random random, BlockPos.MutableBlockPos centerPos, EnumFacing direction, MineshaftVariantSettings settings) {
        super(i, pieceChainLen, settings);
        this.setCoordBaseMode(direction);
        this.centerPos = new BlockPos(centerPos); // position passed in is center of shaft piece (unlike all other pieces, where it is a corner)
        this.boundingBox = getInitialBoundingBox(centerPos);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void writeStructureToNBT(NBTTagCompound tag) {
        super.writeStructureToNBT(tag);
        tag.setIntArray("centerPos", new int[]{centerPos.getX(), centerPos.getY(), centerPos.getZ()});
        tag.setInteger("yAxisLen", yAxisLen);
        tag.setInteger("tunnelLen", tunnelLength);
        tag.setInteger("floorAltitude", tunnelFloorAltitude);
        tag.setInteger("tunnelDir", tunnelEnumFacing.getHorizontalIndex());
        tag.setBoolean("hasTunnel", hasTunnel);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readStructureFromNBT(NBTTagCompound tag, TemplateManager templateManager) {
        super.readStructureFromNBT(tag, templateManager);
        int centerPosX = tag.getIntArray("centerPos")[0];
        int centerPosY = tag.getIntArray("centerPos")[1];
        int centerPosZ = tag.getIntArray("centerPos")[2];
        this.centerPos = new BlockPos(centerPosX, centerPosY, centerPosZ);

        this.yAxisLen = tag.getInteger("yAxisLen");
        this.localYEnd = this.yAxisLen - 1;
        this.tunnelLength = tag.getInteger("tunnelLen");
        this.tunnelFloorAltitude = tag.getInteger("floorAltitude");

        int tunnelDirInt = tag.getInteger("tunnelDir");
        this.tunnelEnumFacing = tunnelDirInt == -1 ? null : EnumFacing.byHorizontalIndex(tunnelDirInt);

        this.hasTunnel = tag.getBoolean("hasTunnel");
    }

    private static StructureBoundingBox getInitialBoundingBox(BlockPos centerPos) {
        return new StructureBoundingBox(centerPos.getX() - 2, centerPos.getY(), centerPos.getZ() - 2, centerPos.getX() + 12, 256, centerPos.getZ() + 12);
    }

    @Override
    public void buildComponent(StructureComponent structurePiece, List<StructureComponent> list, Random random) {
        EnumFacing direction = this.getCoordBaseMode();
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
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        if (BetterMineshafts.DEBUG_LOG) {
            BetterMineshafts.count.incrementAndGet();
        }

        // Only generate vertical entrance if there is valid surrounding terrain
        if (!this.hasTunnel) {
            determineEnumFacing(world);

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
    private void generateVerticalShaft(World world, Random random, StructureBoundingBox box) {
        int shaftStartX = 0,
            shaftStartZ = 0,
            shaftEndX = 4,
            shaftEndZ = 4;
        EnumFacing facing = this.getCoordBaseMode();

        // Adjust shaft position
        if (facing == EnumFacing.NORTH || facing == EnumFacing.WEST) {
            shaftStartZ = 10;
            shaftEndZ = 14;
        }

        // Randomize blocks
        this.fill(world, box, random, shaftStartX, 0, shaftStartZ, shaftEndX, localYEnd, shaftEndZ, getMainSelector());

        // Fill with air
        this.fill(world, box, shaftStartX + 1, 1, shaftStartZ + 1, shaftEndX - 1, localYEnd - 1, shaftEndZ - 1, AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, shaftStartX + 1, 0, shaftStartZ + 1, shaftEndX - 1, 0, shaftEndZ - 1, getMainBlock());

        // Ladder
        this.replaceAir(world, box, shaftStartX + 2, 1, shaftStartZ, shaftStartX + 2, localYEnd - 4, shaftStartZ, getMainBlock());
        this.fill(world, box, shaftStartX + 2, 1, shaftStartZ + 1, shaftStartX + 2, localYEnd - 4, shaftStartZ + 1, Blocks.LADDER.getDefaultState());

        // Doorway
        this.fill(world, box, shaftStartX + 1, 1, shaftStartZ + 4, shaftStartX + 3, 2, shaftStartZ + 4, getMainDoorwayWall());
        this.fill(world, box, shaftStartX + 2, 3, shaftStartZ + 4, shaftStartX + 2, 3, shaftStartZ + 4, getMainDoorwaySlab());
        this.fill(world, box, shaftStartX + 2, 1, shaftStartZ + 4, shaftStartX + 2, 2, shaftStartZ + 4, AIR);

        // Decorations
        this.addBiomeDecorations(world, box, random, shaftStartX + 1, 0, shaftStartZ + 1, shaftEndX - 1, 1, shaftEndZ - 1);
        this.addVines(world, box, random, shaftStartX + 1, 0, shaftStartZ + 1, shaftEndX - 1, localYEnd - 4, shaftEndZ - 1);
    }

    /**
     * Generates tunnel from top of vertical shaft to nearby open cliff-side.
     * The code for this is quite ugly as a result of rotation being handled manually, rather than internally
     * as is normally the case. Rotation logic must be handled manually for this piece because the piece's orientation
     * relies on the surrounding terrain, which can't be determined until generation time.
     */
    private void generateSurfaceTunnel(World world, Random random, StructureBoundingBox box) {
        int tunnelStartX = 0,
            tunnelStartZ = 0,
            tunnelEndX = 0,
            tunnelEndZ = 0;
        EnumFacing facing = this.getCoordBaseMode();

        // We have to account for this piece's rotation.
        // This is normally handled internally, but must be tweaked manually for surface tunnels
        // since their orientation is not determined until generation time.
        float rotationDifference = facing.getHorizontalAngle() - tunnelEnumFacing.getHorizontalAngle();
        EnumFacing relativeTunnelDir = EnumFacing.fromAngle(EnumFacing.NORTH.getHorizontalAngle() - rotationDifference);
        if (relativeTunnelDir == EnumFacing.NORTH) {
            tunnelStartX = 0;
            tunnelStartZ = 4;
            tunnelEndX = 4;
            tunnelEndZ = 4 + tunnelLength;
        }
        else if (
            (relativeTunnelDir == EnumFacing.WEST && !(facing == EnumFacing.SOUTH || facing == EnumFacing.WEST)) ||
            (relativeTunnelDir == EnumFacing.EAST && (facing == EnumFacing.SOUTH || facing == EnumFacing.WEST))
        ) {
            tunnelStartX = 10 - tunnelLength;
            tunnelStartZ = 0;
            tunnelEndX = 10;
            tunnelEndZ = 4;
        }
        else if (relativeTunnelDir == EnumFacing.SOUTH) {
            tunnelStartX = 0;
            tunnelStartZ = 10 - tunnelLength;
            tunnelEndX = 4;
            tunnelEndZ = 10;
        }
        else if (
            relativeTunnelDir == EnumFacing.EAST || relativeTunnelDir == EnumFacing.WEST
        ) {
            tunnelStartX = 4;
            tunnelStartZ = 0;
            tunnelEndX = 4 + tunnelLength;
            tunnelEndZ = 4;
            // Adjust position
            if (facing == EnumFacing.NORTH || facing == EnumFacing.WEST) {
                tunnelStartZ = 10;
                tunnelEndZ = 14;
            }
        }

        // ################################################################
        // #                            Tunnel                            #
        // ################################################################

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, .6f, tunnelStartX, tunnelFloorAltitude, tunnelStartZ, tunnelEndX, tunnelFloorAltitude + 4, tunnelEndZ, getMainSelector());

        if (facing.getAxis() == tunnelEnumFacing.getAxis()) {
            // Fill in any air in floor with main block
            this.replaceAir(world, box, tunnelStartX + 1, tunnelFloorAltitude, tunnelStartZ, tunnelEndX - 1, tunnelFloorAltitude, tunnelEndZ, getMainBlock());

            // Fill with air
            this.fill(world, box, tunnelStartX + 1, tunnelFloorAltitude + 1, tunnelStartZ, tunnelEndX - 1, tunnelFloorAltitude + 3, tunnelEndZ, AIR);
        }
        else {
            // Fill in any air in floor with main block
            this.replaceAir(world, box, tunnelStartX, tunnelFloorAltitude, tunnelStartZ + 1, tunnelEndX, tunnelFloorAltitude, tunnelEndZ - 1, getMainBlock());

            // Fill with air
            this.fill(world, box, tunnelStartX, tunnelFloorAltitude + 1, tunnelStartZ + 1, tunnelEndX, tunnelFloorAltitude + 3, tunnelEndZ - 1, AIR);
        }

        // Vines
        this.addVines(world, box, random, tunnelStartX + 1, tunnelFloorAltitude, tunnelStartZ + 1, tunnelEndX - 1, tunnelFloorAltitude + 4, tunnelEndZ - 1);

        // ################################################################
        // #                       Supports & Rails                       #
        // ################################################################
        // Note that while normally, building (i.e. determining placement) of things like supports is done before generation,
        // that is not the case here since localZEnd is not known until generation.

        // Mark positions where center floor block is solid
        boolean[] validPositions;
        if (facing.getAxis() == tunnelEnumFacing.getAxis()) {
            validPositions = new boolean[tunnelEndZ - tunnelStartZ + 1];
            for (int z = 0; z < validPositions.length; z++) {
                IBlockState floorBlock = this.getBlockStateFromPos(world, tunnelStartX + 2, tunnelFloorAltitude, tunnelStartZ + z, box);
                if (floorBlock.getMaterial().isSolid()) {
                    validPositions[z] = true;
                }
            }
        } else {
            validPositions = new boolean[tunnelEndX - tunnelStartX + 1];
            for (int x = 0; x < validPositions.length; x++) {
                IBlockState floorBlock = this.getBlockStateFromPos(world, tunnelStartX + x, tunnelFloorAltitude, tunnelStartZ + 2, box);
                if (floorBlock.getMaterial().isSolid()) {
                    validPositions[x] = true;
                }
            }
        }

        // Generate supports.
        if (facing.getAxis() == tunnelEnumFacing.getAxis()) {
            for (int z = tunnelStartZ; z <= tunnelEndZ; z++) {
                int r = random.nextInt(4);
                if (r == 0 && validPositions[z - tunnelStartZ]) {
                    // Support
                    this.fill(world, box, tunnelStartX + 1, tunnelFloorAltitude + 1, z, tunnelStartX + 1, tunnelFloorAltitude + 2, z, getSupportBlock());
                    this.fill(world, box, tunnelStartX + 3, tunnelFloorAltitude + 1, z, tunnelStartX + 3, tunnelFloorAltitude + 2, z, getSupportBlock());
                    this.fill(world, box, tunnelStartX + 1, tunnelFloorAltitude + 3, z, tunnelStartX + 3, tunnelFloorAltitude + 3, z, getMainBlock());
                    this.chanceReplaceNonAir(world, box, random, .25f, tunnelStartX + 1, tunnelFloorAltitude + 3, z, tunnelStartX + 3, tunnelFloorAltitude + 3, z, getSupportBlock());

                    // Cobwebs
                    this.chanceReplaceAir(world, box, random, .15f, tunnelStartX + 1, tunnelFloorAltitude + 3, z - 1, tunnelStartX + 1, tunnelFloorAltitude + 3, z + 1, Blocks.WEB.getDefaultState());
                    this.chanceReplaceAir(world, box, random, .15f, tunnelStartX + 3, tunnelFloorAltitude + 3, z - 1, tunnelStartX + 3, tunnelFloorAltitude + 3, z + 1, Blocks.WEB.getDefaultState());
                    z += 3;
                }
            }
        } else {
            for (int x = tunnelStartX; x <= tunnelEndX; x++) {
                int r = random.nextInt(4);
                if (r == 0 && validPositions[x - tunnelStartX]) {
                    // Support
                    this.fill(world, box, x, tunnelFloorAltitude + 1, tunnelStartZ + 1, x, tunnelFloorAltitude + 2, tunnelStartZ + 1, getSupportBlock());
                    this.fill(world, box, x, tunnelFloorAltitude + 1, tunnelStartZ + 3, x, tunnelFloorAltitude + 2, tunnelStartZ + 3, getSupportBlock());
                    this.fill(world, box, x, tunnelFloorAltitude + 3, tunnelStartZ + 1, x, tunnelFloorAltitude + 3, tunnelStartZ + 3, getMainBlock());
                    this.chanceReplaceNonAir(world, box, random, .25f, x, tunnelFloorAltitude + 3, tunnelStartZ + 1, x, tunnelFloorAltitude + 3, tunnelStartZ + 3, getSupportBlock());

                    // Cobwebs
                    this.chanceReplaceAir(world, box, random, .15f, x - 1, tunnelFloorAltitude + 3, tunnelStartZ + 1, x + 1, tunnelFloorAltitude + 3, tunnelStartZ + 1, Blocks.WEB.getDefaultState());
                    this.chanceReplaceAir(world, box, random, .15f, x - 1, tunnelFloorAltitude + 3, tunnelStartZ + 3, x + 1, tunnelFloorAltitude + 3, tunnelStartZ + 3, Blocks.WEB.getDefaultState());
                    x += 3;
                }
            }
        }

        // Generate rails.
        if (facing.getAxis() == tunnelEnumFacing.getAxis()) {
            for (int z = 0; z < tunnelEndZ - tunnelStartZ + 1; z++) {
                if (validPositions[z] && random.nextFloat() < .3f) {
                    this.fill(world, box, tunnelStartX + 2, tunnelFloorAltitude + 1, tunnelStartZ + z, tunnelStartX + 2, tunnelFloorAltitude + 1, tunnelStartZ + z + 1, Blocks.RAIL.getDefaultState());
                    z++;
                }
            }
        } else {
            for (int x = 0; x < tunnelEndX - tunnelStartX + 1; x++) {
                if (validPositions[x] && random.nextFloat() < .3f) {
                    this.fill(world, box, tunnelStartX + x, tunnelFloorAltitude + 1, tunnelStartZ + 2, tunnelStartX + x + 1, tunnelFloorAltitude + 1, tunnelStartZ + 2, Blocks.RAIL.getDefaultState());
                    x++;
                }
            }
        }
    }

    /**
     * Determines the direction to spawn the surface tunnel in.
     * Tries to find a direction in which there is a drop-off, with the goal of creating an opening
     * in the face of a mountain or hill.
     */
    private void determineEnumFacing(World world) {
        int minSurfaceHeight = 255;

        // Set height for this, equal to 2 below the min height in the 5x5 vertical shaft piece
        for (int xOffset = -2; xOffset <= 2; xOffset++) {
            for (int zOffset = -2; zOffset <= 2; zOffset++) {
                try {
                    int realX = centerPos.getX() + xOffset,
                        realZ = centerPos.getZ() + zOffset;
                    int surfaceHeight = SurfaceUtil.getSurfaceHeight(world, new ColPos(realX, realZ));
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

        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(centerPos);

        int radius = 5; // Number of blocks that constitutes one 'radius'
        int maxRadialDist = 2; // Number of radii to check in each direction. E.g. 3 radii * radius of 8 = 24 blocks

        for (int radialDist = 0; radialDist < maxRadialDist; radialDist++) {
            // Check each of the four cardinal directions
            for (EnumFacing direction : EnumFacing.values()) {
                if (direction != EnumFacing.EAST && direction != EnumFacing.SOUTH) continue;

                mutable.setPos(centerPos.offset(direction, radius * radialDist + 2));

                // Check altitude of each individual block along the direction.
                for (int i = radialDist * radius; i < radialDist * radius + radius; i++) {
                    int surfaceHeight = SurfaceUtil.getSurfaceHeight(world, new ColPos(mutable.getX(), mutable.getZ()));

                    if (surfaceHeight <= floorHeight && surfaceHeight > 1) {
                        this.hasTunnel = true;
                        this.tunnelEnumFacing = direction;
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
