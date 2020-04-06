package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BoxUtil;
import net.minecraft.block.*;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
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
    private final List<Integer> bigSupports = Lists.newLinkedList(); // local z coords
    private final List<Integer> smallSupports = Lists.newLinkedList(); // local z coords
    private final List<Pair<Integer, Integer>> gravelDeposits = Lists.newLinkedList(); // Pair<z coordinate, side> where side 0 = left, 1 = right
    private static final int
        SECONDARY_AXIS_LEN = 9,
        Y_AXIS_LEN = 8,
        MAIN_AXIS_LEN = 64;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;
    private static final float
        SMALL_SHAFT_SPAWN_CHANCE = .07f,
        SIDE_ROOM_SPAWN_CHANCE = .025f;

    public BigTunnel(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, compoundTag);

        ListTag listTag1 = compoundTag.getList("SmallShaftLeftEntrances", 11);
        ListTag listTag2 = compoundTag.getList("SmallShaftRightEntrances", 11);
        ListTag listTag3 = compoundTag.getList("SideRoomEntrances", 11);
        ListTag listTag4 = compoundTag.getList("BigSupports", 3);
        ListTag listTag5 = compoundTag.getList("SmallSupports", 3);
        ListTag listTag6 = compoundTag.getList("GravelDeposits", 11);

        for (int i = 0; i < listTag1.size(); ++i) {
            this.smallShaftLeftEntrances.add(new BlockPos(listTag1.getIntArray(i)[0], listTag1.getIntArray(i)[1], listTag1.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag2.size(); ++i) {
            this.smallShaftRightEntrances.add(new BlockPos(listTag2.getIntArray(i)[0], listTag2.getIntArray(i)[1], listTag2.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag3.size(); ++i) {
            this.sideRoomEntrances.add(new BlockBox(listTag3.getIntArray(i)));
        }

        for (int i = 0; i < listTag4.size(); ++i) {
            this.bigSupports.add(listTag4.getInt(i));
        }

        for (int i = 0; i < listTag5.size(); ++i) {
            this.smallSupports.add(listTag5.getInt(i));
        }

        for (int i = 0; i < listTag6.size(); ++i) {
            this.gravelDeposits.add(new Pair<>(listTag6.getIntArray(i)[0], listTag6.getIntArray(i)[1]));
        }
    }

    public BigTunnel(int i, int pieceChainLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, i, pieceChainLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    public BigTunnel(int i, int pieceChainLen, Random random, BlockPos pos, Direction direction, BetterMineshaftFeature.Type type) {
        this(i, pieceChainLen, random, determineInitialBoxPosition(random, pos.getX(), pos.getY(), pos.getZ(), direction), direction, type);
    }

    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
        ListTag listTag1 = new ListTag();
        ListTag listTag2 = new ListTag();
        ListTag listTag3 = new ListTag();
        ListTag listTag4 = new ListTag();
        ListTag listTag5 = new ListTag();
        ListTag listTag6 = new ListTag();
        smallShaftLeftEntrances.forEach(pos -> listTag1.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        smallShaftRightEntrances.forEach(pos -> listTag2.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        sideRoomEntrances.forEach(blockBox -> listTag3.add(blockBox.toNbt()));
        bigSupports.forEach(z -> listTag4.add(IntTag.of(z)));
        smallSupports.forEach(z -> listTag5.add(IntTag.of(z)));
        gravelDeposits.forEach(pair -> listTag6.add(new IntArrayTag(new int[]{pair.getLeft(), pair.getRight()})));
        tag.put("SmallShaftLeftEntrances", listTag1);
        tag.put("SmallShaftRightEntrances", listTag2);
        tag.put("SideRoomEntrances", listTag3);
        tag.put("BigSupports", listTag4);
        tag.put("SmallSupports", listTag5);
        tag.put("GravelDeposits", listTag6);
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
        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        // Extend big tunnel in same direction
        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, this.method_14923(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, this.method_14923(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, direction, this.method_14923(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, direction, this.method_14923(), pieceChainLen);
        }

        // Get the length of the main axis. This SHOULD be equal to the MAIN_AXIS_LEN variable.
        int pieceLen = this.getFacing().getAxis() == Direction.Axis.Z ? this.boundingBox.getBlockCountZ() : this.boundingBox.getBlockCountX();

        // Build side rooms
        buildSideRoomsLeft(structurePiece, list, random, direction, pieceLen);
        buildSideRoomsRight(structurePiece, list, random, direction, pieceLen);

        // Build small shafts and mark their entrances
        buildSmallShaftsLeft(structurePiece, list, random, direction, pieceLen);
        buildSmallShaftsRight(structurePiece, list, random, direction, pieceLen);

        // Decorations
        buildSupports(random);
        buildGravelDeposits(random);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) { // check if box contains any liquid
                return false;
        }

        // Randomize blocks
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.COBBLESTONE.getDefaultState(), Blocks.COBBLESTONE.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.STONE_BRICKS.getDefaultState(), Blocks.STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.MOSSY_STONE_BRICKS.getDefaultState(), Blocks.MOSSY_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, Blocks.CRACKED_STONE_BRICKS.getDefaultState(), Blocks.CRACKED_STONE_BRICKS.getDefaultState(), true);
        this.randomFillWithOutline(world, box, random, .2f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, AIR, AIR, true);

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 3, LOCAL_Z_END, AIR, AIR, false);
        this.fillWithOutline(world, box, 2, LOCAL_Y_END - 3, 0, LOCAL_X_END - 2, LOCAL_Y_END - 2, LOCAL_Z_END, AIR, AIR, false);
        this.fillWithOutline(world, box, 3, LOCAL_Y_END - 1, 0, LOCAL_X_END - 3, LOCAL_Y_END - 1, LOCAL_Z_END, AIR, AIR, false);

        // Add random blocks in floor
        this.randomFillWithOutline(world, box, random, .4f, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getMainBlock(), AIR, false);

        // Fill in any air in floor with planks
        this.replaceAirInBox(world, box, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getMainBlock());

        // Small mineshaft entrances
        smallShaftLeftEntrances.forEach(entrancePos -> generateSmallShaftEntranceLeft(world, box, random, entrancePos.getX(), entrancePos.getY(), entrancePos.getZ()));
        smallShaftRightEntrances.forEach(entrancePos -> generateSmallShaftEntranceRight(world, box, random, entrancePos.getX(), entrancePos.getY(), entrancePos.getZ()));

        // Open up entrances to side rooms
        sideRoomEntrances.forEach(roomBox -> generateSideRoomOpening(world, box, roomBox, random));

        generateRails(world, box, random);
        generateLanterns(world, box, random);
        generateChestCarts(world, box, random, LootTables.ABANDONED_MINESHAFT_CHEST);

        bigSupports.forEach(z -> generateBigSupport(world, box, random, z));
        smallSupports.forEach(z -> generateSmallSupport(world, box, random, z));

        gravelDeposits.forEach(pair -> generateGravelDeposit(world, box, random, pair.getLeft(), pair.getRight()));

        return true;
    }

    private void generateGravelDeposit(IWorld world, BlockBox box, Random random, int z, int side) {
        BlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
        switch (side) {
            case 0: // Left side
            default:
                // Row closest to wall
                this.replaceAirInBox(world, box, 1, 1, z, 1, 2, z + 2, GRAVEL);
                this.replaceAirInBox(world, box, 1, 3, z + 1, 1, 3 + random.nextInt(2), z + 1, GRAVEL);
                this.randomlyReplaceAirInBox(world, box, random, .5f, 1, 3, z, 1, 3, z + 2, GRAVEL);
                // Middle row
                this.replaceAirInBox(world, box, 2, 1, z + 1, 2, 2 + random.nextInt(2), z + 1, GRAVEL);
                this.replaceAirInBox(world, box, 2, 1, z, 2, 1 + random.nextInt(2), z + 2, GRAVEL);
                // Innermost row
                this.randomlyReplaceAirInBox(world, box, random, .5f, 3, 1, z, 3, 1, z + 2, GRAVEL);
                break;
            case 1: // Right side
                // Row closest to wall
                this.replaceAirInBox(world, box, LOCAL_X_END - 1, 1, z, LOCAL_X_END - 1, 2, z + 2, GRAVEL);
                this.replaceAirInBox(world, box, LOCAL_X_END - 1, 3, z + 1, LOCAL_X_END - 1, 3 + random.nextInt(2), z + 1, GRAVEL);
                this.randomlyReplaceAirInBox(world, box, random, .5f, LOCAL_X_END - 1, 3, z, LOCAL_X_END - 1, 3, z + 2, GRAVEL);
                // Middle row
                this.replaceAirInBox(world, box, LOCAL_X_END - 2, 1, z + 1, LOCAL_X_END - 2, 2 + random.nextInt(2), z + 1, GRAVEL);
                this.replaceAirInBox(world, box, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 2, 1 + random.nextInt(2), z + 2, GRAVEL);
                // Innermost row
                this.randomlyReplaceAirInBox(world, box, random, .5f, LOCAL_X_END - 3, 1, z, LOCAL_X_END - 3, 1, z + 2, GRAVEL);
        }
    }

    private void generateChestCarts(IWorld world, BlockBox box, Random random, Identifier lootTableId) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextInt(100) == 0) {
                BlockPos blockPos = new BlockPos(this.applyXTransform(LOCAL_X_END / 2, z), applyYTransform(1), this.applyZTransform(LOCAL_X_END / 2, z));
                if (box.contains(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(world.getWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(lootTableId, random.nextLong());
                    world.spawnEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateBigSupport(IWorld world, BlockBox box, Random random, int z) {
        // Bottom slabs
        this.randomFillWithOutline(world, box, random, .6f, 1, 1, z, 2, 1, z + 2, Blocks.OAK_SLAB.getDefaultState(), Blocks.OAK_SLAB.getDefaultState(), false);
        this.randomFillWithOutline(world, box, random, .6f, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 1, 1, z + 2, Blocks.OAK_SLAB.getDefaultState(), Blocks.OAK_SLAB.getDefaultState(), false);
        // Main blocks
        this.addBlock(world, getMainBlock(), 1, 1, z + 1, box);
        this.addBlock(world, getMainBlock(), LOCAL_X_END - 1, 1, z + 1, box);
        this.addBlock(world, getMainBlock(), 1, 4, z + 1, box);
        this.addBlock(world, getMainBlock(), LOCAL_X_END - 1, 4, z + 1, box);
        this.fillWithOutline(world, box, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, getMainBlock(), getMainBlock(), false);
        this.randomFillWithOutline(world, box, random, .4f, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, getSupportBlock(), getSupportBlock(), true);
        // Supports
        this.fillWithOutline(world, box, 1, 2, z + 1, 1, 3, z + 1, getSupportBlock(), getSupportBlock(), false);
        this.fillWithOutline(world, box, LOCAL_X_END - 1, 2, z + 1, LOCAL_X_END - 1, 3, z + 1, getSupportBlock(), getSupportBlock(), false);
        this.randomFillWithOutline(world, box, random, .7f,2, 3, z + 1, 2, 3, z + 1, getSupportBlock(), getSupportBlock(), true);
        this.randomFillWithOutline(world, box, random, .7f,LOCAL_X_END - 2, 3, z + 1, 2, 3, z + 1, getSupportBlock(), getSupportBlock(), true);
        this.randomFillWithOutline(world, box, random, .5f, 2, 4, z + 1, LOCAL_X_END - 2, 4, z + 1, getSupportBlock(), getSupportBlock(), false);
    }

    private void generateSmallSupport(IWorld world, BlockBox box, Random random, int z) {
        this.addBlock(world, getMainBlock(), 2, 1, z, box);
        this.addBlock(world, getMainBlock(), LOCAL_X_END - 2, 1, z, box);
        this.addBlock(world, getSupportBlock(), 2, 2, z, box);
        this.addBlock(world, getSupportBlock(), LOCAL_X_END - 2, 2, z, box);
        this.addBlock(world, getMainBlock(), 2, 3, z, box);
        this.addBlock(world, getMainBlock(), LOCAL_X_END - 2, 3, z, box);
        this.fillWithOutline(world, box, 3, 4, z, LOCAL_X_END - 3, 4, z, getMainBlock(), getMainBlock(), false);
        this.randomFillWithOutline(world, box, random, .5f, 3, 4, z, LOCAL_X_END - 3, 4, z, getSupportBlock(), getSupportBlock(), true);
        this.randomFillWithOutline(world, box, random, .4f, 2, 3, z, LOCAL_X_END - 2, 3, z, getSupportBlock(), getSupportBlock(), false);
        this.addBlock(world, getSupportBlock(), 3, 3, z, box);
        this.addBlock(world, getSupportBlock(), LOCAL_X_END - 3, 3, z, box);
    }

    private void generateLanterns(IWorld world, BlockBox box, Random random) {
        BlockState LANTERN = Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true);
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            for (int x = 3; x <= LOCAL_X_END - 3; x++) {
                if (random.nextInt(150) == 0) {
                    if (!this.getBlockAt(world, x, LOCAL_Y_END, z, box).isAir()) {
                        this.addBlock(world, LANTERN, x, LOCAL_Y_END - 1, z, box);
                        z += 20;
                    }
                }
            }
        }
    }

    private void generateRails(IWorld world, BlockBox box, Random random) {
        // Place rails in center
        this.fillWithOutline(world, box, LOCAL_X_END / 2, 1, 0, LOCAL_X_END / 2, 1, LOCAL_Z_END, Blocks.RAIL.getDefaultState(), Blocks.RAIL.getDefaultState(), false);
        // Place powered rails
        int blocksSinceLastRail = 0;
        for (int n = 0; n <= LOCAL_Z_END; n++) {
            blocksSinceLastRail++;
            if (random.nextInt(20) == 0 || blocksSinceLastRail > 25) {
                this.addBlock(world, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.POWERED, true), LOCAL_X_END / 2, 1, n, box);
                blocksSinceLastRail = 0; // reset counter
            }
        }
    }

    private void generateSmallShaftEntranceRight(IWorld world, BlockBox box, Random random, int x, int y, int z) {
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

    private void generateSmallShaftEntranceLeft(IWorld world, BlockBox box, Random random, int x, int y, int z) {
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

    private void generateSideRoomOpening(IWorld world, BlockBox chunkBox, BlockBox entranceBox, Random random) {
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

    private void buildGravelDeposits(Random random) {
        for (int z = 0; z <= LOCAL_Z_END - 2; z++) {
            int r = random.nextInt(20);
            int currPos = z;
            if (r == 0) { // Left side
                gravelDeposits.add(new Pair<>(currPos, 0));
                z += 5;
            }
            else if (r == 1) { // Right side
                gravelDeposits.add(new Pair<>(currPos, 1));
                z += 5;
            }
        }
    }

    private void buildSupports(Random random) {
        for (int z = 0; z <= LOCAL_Z_END - 2; z++) {
            // Make sure we arent overlapping with small shaft entrances
            boolean blockingEntrance = false;
            for (BlockPos entrancePos : smallShaftLeftEntrances) {
                if (entrancePos.getZ() <= z + 2 && z <= entrancePos.getZ() + 2) {
                    blockingEntrance = true;
                    break;
                }
            }
            for (BlockPos entrancePos : smallShaftRightEntrances) {
                if (entrancePos.getZ() <= z + 2 && z <= entrancePos.getZ() + 2) {
                    blockingEntrance = true;
                    break;
                }
            }
            if (blockingEntrance) continue;

            int r = random.nextInt(10);
            if (r == 0) { // Big support
                bigSupports.add(z);
                z += 5;
            }
            else if (r == 1) { // Small support
                smallSupports.add(z);
                z += 5;
            }
        }
    }

    private void buildSideRoomsLeft(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < SIDE_ROOM_SPAWN_CHANCE) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX - 5, this.boundingBox.minY, this.boundingBox.maxZ - n - 9, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX + 5, this.boundingBox.minY, this.boundingBox.minZ + n + 9, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX - n - 9, this.boundingBox.minY, this.boundingBox.maxZ + 5, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX + n + 9, this.boundingBox.minY, this.boundingBox.minZ - 5, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSideRoomsRight(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < SIDE_ROOM_SPAWN_CHANCE) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX + 5, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX - 5, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.minZ - 5, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.maxZ + 5, nextPieceDirection, this.method_14923(), pieceChainLen);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSmallShaftsLeft(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 4; n++) {
            if (random.nextFloat() < SMALL_SHAFT_SPAWN_CHANCE) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.minZ - 1, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }
    }

    private void buildSmallShaftsRight(StructurePiece structurePiece, List<StructurePiece> list, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 5; n < pieceLen; n++) {
            if (random.nextFloat() < SMALL_SHAFT_SPAWN_CHANCE) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - n, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n - 3));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + n, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX - n, this.boundingBox.minY, this.boundingBox.minZ - 1, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX + n, this.boundingBox.minY, this.boundingBox.maxZ + 1, nextPieceDirection, this.method_14923(), 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n - 3));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }
    }
}