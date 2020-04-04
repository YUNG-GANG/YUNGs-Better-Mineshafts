package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BoxUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class BigTunnel extends MineshaftPart {
    private final List<BlockPos> smallShaftLeftEntrances = Lists.newLinkedList();
    private final List<BlockPos> smallShaftRightEntrances = Lists.newLinkedList();
    private final List<BlockBox> sideRoomEntrances = Lists.newLinkedList();
    private static final int
        SECONDARY_AXIS_LEN = 9,
        Y_AXIS_LEN = 8,
        MAIN_AXIS_LEN = 32;

    public BigTunnel(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, compoundTag);

        ListTag listTag1 = compoundTag.getList("SmallShaftLeftEntrances", 11);
        ListTag listTag2 = compoundTag.getList("SmallShaftRightEntrances", 11);
        ListTag listTag3 = compoundTag.getList("SideRoomEntrances", 11);

        for (int i = 0; i < listTag1.size(); ++i) {
            this.smallShaftLeftEntrances.add(new BlockPos(listTag1.getIntArray(i)[0], listTag1.getIntArray(i)[1], listTag1.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag2.size(); ++i) {
            this.smallShaftRightEntrances.add(new BlockPos(listTag2.getIntArray(i)[0], listTag2.getIntArray(i)[1], listTag2.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag3.size(); ++i) {
            this.sideRoomEntrances.add(new BlockBox(listTag3.getIntArray(i)));
        }
    }

    public BigTunnel(int i, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, i, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    public BigTunnel(int i, Random random, BlockPos pos, Direction direction, BetterMineshaftFeature.Type type) {
        this(i, random, determineInitialBoxPosition(random, pos.getX(), pos.getY(), pos.getZ(), direction), direction, type);
    }

    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
        ListTag listTag1 = new ListTag();
        ListTag listTag2 = new ListTag();
        ListTag listTag3 = new ListTag();
        smallShaftLeftEntrances.forEach(pos -> listTag1.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        smallShaftRightEntrances.forEach(pos -> listTag2.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        sideRoomEntrances.forEach(blockBox -> listTag3.add(blockBox.toNbt()));
        tag.put("SmallShaftLeftEntrances", listTag1);
        tag.put("SmallShaftRightEntrances", listTag2);
        tag.put("SideRoomEntrances", listTag3);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.method_14932(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    public static BlockBox determineInitialBoxPosition(Random random, int x, int y, int z, Direction direction) {
        return BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);
    }

    /**
     * buildComponent
     */
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        float smallShaftSpawnChance = .07f;
        float sideRoomSpawnChance = .025f;

        BetterMineshaftFeature.Type mineshaftType = ((MineshaftPart)structurePiece).mineshaftType;
        int chainLen = this.method_14923(); // getComponentType

        Direction nextPieceDirection;
        StructurePiece newPiece;

        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        // Extend tunnel in same direction
        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, chainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, chainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, direction, chainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, direction, chainLen);
        }

        int pieceLen = this.getFacing().getAxis() == Direction.Axis.Z ? this.boundingBox.getBlockCountZ() : this.boundingBox.getBlockCountX();

        // Add side rooms on the left
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < sideRoomSpawnChance) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.minX - 5, this.boundingBox.minY, this.boundingBox.maxZ - n - 9, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.maxX + 5, this.boundingBox.minY, this.boundingBox.minZ + n + 9, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.maxX - n - 9, this.boundingBox.minY, this.boundingBox.maxZ + 5, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.minX + n + 9, this.boundingBox.minY, this.boundingBox.minZ - 5, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }

        // Add side rooms on the right
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < sideRoomSpawnChance) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.maxX + 5, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.minX - 5, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.minZ - 5, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SIDE_ROOM, mineshaftType, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.maxZ + 5, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }

        // Add smaller side shafts on the left, and record their entrances
        for (int n = 0; n < (pieceLen - 1) - 4; n++) {
            if (random.nextFloat() < smallShaftSpawnChance) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.minZ - 1, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }

        // Add smaller side shafts on the right, and record their entrances
        for (int n = 5; n < pieceLen; n++) {
            if (random.nextFloat() < smallShaftSpawnChance) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1,  n - 3));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.minZ - 1, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.SMALL_TUNNEL, mineshaftType, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextPieceDirection, this.method_14923());
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n - 3));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) { // check if box contains any liquid
//                return false;
        }
        int xEnd = SECONDARY_AXIS_LEN - 1,
            yEnd = Y_AXIS_LEN - 1,
            zEnd = MAIN_AXIS_LEN - 1;

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, xEnd, yEnd, zEnd, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, xEnd, yEnd, zEnd, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, xEnd, yEnd, zEnd, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, xEnd, yEnd, zEnd, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .2f, 0, 0, 0, xEnd, yEnd, zEnd, AIR, AIR, true);

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 0, xEnd - 1, yEnd - 3, zEnd, AIR, AIR, false);
        this.fillWithOutline(world, box, 2, yEnd - 3, 0, xEnd - 2, yEnd - 2, zEnd, AIR, AIR, false);
        this.fillWithOutline(world, box, 3, yEnd - 1, 0, xEnd - 3, yEnd - 1, zEnd, AIR, AIR, false);

        // Add random blocks in floor
        this.randomFillWithOutline(world, box, random, .4f, 0, 0, 0, xEnd, 0, zEnd, Blocks.OAK_PLANKS.getDefaultState(), AIR, false);

        // Fill in any air in floor with planks
        this.replaceAirInBox(world, box, 0, 0, 0, xEnd, 0, zEnd, Blocks.OAK_PLANKS.getDefaultState());

        // Small mineshaft entrances
        this.smallShaftLeftEntrances.forEach(entrancePos -> placeSmallShaftEntranceLeft(world, box, random, entrancePos.getX(), entrancePos.getY(), entrancePos.getZ()));
        this.smallShaftRightEntrances.forEach(entrancePos -> placeSmallShaftEntranceRight(world, box, random, entrancePos.getX(), entrancePos.getY(), entrancePos.getZ()));

        // Open up entrances to side rooms
        sideRoomEntrances.forEach(roomBox -> placeSideRoomOpening(world, box, roomBox, random));

        // Place rails in center
        this.fillWithOutline(world, box, xEnd / 2, 1, 0, xEnd / 2, 1, zEnd, Blocks.RAIL.getDefaultState(), Blocks.RAIL.getDefaultState(), false);

        return true;
    }

    private void placeSmallShaftEntranceRight(IWorld world, BlockBox box, Random random, int x, int y, int z) {
        BlockState mainBlock = this.getMainBlock();
        BlockState supportBlock = this.getSupportBlock();

        this.fillWithOutline(world, box, x + 1, y, z, x + 1, y, z + 2, AIR, AIR, false);
        this.fillWithOutline(world, box, x + 1, y + 1, z, x + 1, y + 1, z, supportBlock, AIR, false);
        this.fillWithOutline(world, box, x + 1, y + 1, z + 2, x + 1, y + 1, z + 2, supportBlock, AIR, false);
        this.fillWithOutline(world, box, x, y, z, x, y + 1, z, supportBlock, AIR, false);
        this.fillWithOutline(world, box, x, y, z + 2, x, y + 1, z + 2, supportBlock, AIR, false);
        this.randomFillWithOutline(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, mainBlock, AIR, false);
        this.fillWithOutline(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, AIR, AIR, false);
    }

    private void placeSmallShaftEntranceLeft(IWorld world, BlockBox box, Random random, int x, int y, int z) {
        BlockState mainBlock = this.getMainBlock();
        BlockState supportBlock = this.getSupportBlock();

        this.fillWithOutline(world, box, x, y, z, x, y, z + 2, AIR, AIR, false);
        this.fillWithOutline(world, box, x, y + 1, z, x, y + 1, z, supportBlock, AIR, false);
        this.fillWithOutline(world, box, x, y + 1, z + 2, x, y + 1, z + 2, supportBlock, AIR, false);
        this.fillWithOutline(world, box, x + 1, y, z, x + 1, y + 1, z, supportBlock, AIR, false);
        this.fillWithOutline(world, box, x + 1, y, z + 2, x + 1, y + 1, z + 2, supportBlock, AIR, false);
        this.randomFillWithOutline(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, mainBlock, AIR, false);
        this.fillWithOutline(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, AIR, AIR, false);
    }

    private void placeSideRoomOpening(IWorld world, BlockBox chunkBox, BlockBox entranceBox, Random random) {
        switch (random.nextInt(3)) {
            case 0:
                // Completely open
                this.fillWithOutline(world, chunkBox, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 2, entranceBox.maxX, entranceBox.maxY, entranceBox.maxZ - 2, AIR, AIR, false);
                return;
            case 1:
                // A few columns for openings
                this.fillWithOutline(world, chunkBox, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 2, entranceBox.maxX, entranceBox.maxY - 1, entranceBox.minZ + 2, AIR, AIR, false);
                this.fillWithOutline(world, chunkBox, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 4, entranceBox.maxX, entranceBox.maxY - 1, entranceBox.minZ + 5, AIR, AIR, false);
                this.fillWithOutline(world, chunkBox, entranceBox.minX, entranceBox.minY, entranceBox.minZ + 7, entranceBox.maxX, entranceBox.maxY - 1, entranceBox.minZ + 7, AIR, AIR, false);
                return;
            case 2:
                // No openings - random block removal will expose these, probably
                return;
        }
    }
}