package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class BigTunnel extends MineshaftPiece {
    private final List<BlockPos> smallShaftLeftEntrances = Lists.newLinkedList();
    private final List<BlockPos> smallShaftRightEntrances = Lists.newLinkedList();
    private final List<BlockBox> sideRoomEntrances = Lists.newLinkedList();
    private final List<Integer> bigSupports = Lists.newLinkedList(); // local z coords
    private final List<Integer> smallSupports = Lists.newLinkedList(); // local z coords
    private final List<Pair<Integer, Integer>> gravelDeposits = Lists.newLinkedList(); // Pair<z coordinate, side> where side 0 = left, 1 = right
    private static final int
            SECONDARY_AXIS_LEN = 9,
            Y_AXIS_LEN = 8,
            MAIN_AXIS_LEN = 24;
    private static final int
            LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
            LOCAL_Y_END = Y_AXIS_LEN - 1,
            LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public BigTunnel(ServerWorld world, NbtCompound compoundTag) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, compoundTag);

        NbtList listTag1 = compoundTag.getList("SmallShaftLeftEntrances", 11);
        NbtList listTag2 = compoundTag.getList("SmallShaftRightEntrances", 11);
        NbtList listTag3 = compoundTag.getList("SideRoomEntrances", 11);
        NbtList listTag4 = compoundTag.getList("BigSupports", 3);
        NbtList listTag5 = compoundTag.getList("SmallSupports", 3);
        NbtList listTag6 = compoundTag.getList("GravelDeposits", 11);

        for (int i = 0; i < listTag1.size(); ++i) {
            this.smallShaftLeftEntrances.add(new BlockPos(listTag1.getIntArray(i)[0], listTag1.getIntArray(i)[1], listTag1.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag2.size(); ++i) {
            this.smallShaftRightEntrances.add(new BlockPos(listTag2.getIntArray(i)[0], listTag2.getIntArray(i)[1], listTag2.getIntArray(i)[2]));
        }

        for (int i = 0; i < listTag3.size(); ++i) {
            this.sideRoomEntrances.add(new BlockBox(listTag3.getIntArray(i)[0], listTag3.getIntArray(i)[1],
                    listTag3.getIntArray(i)[2], listTag3.getIntArray(i)[3],
                    listTag3.getIntArray(i)[4], listTag3.getIntArray(i)[5]));
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

    public BigTunnel(int chainLength, Random random, BlockBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, chainLength, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void writeNbt(ServerWorld world, NbtCompound tag) {
        super.writeNbt(world, tag);
        NbtList listTag1 = new NbtList();
        NbtList listTag2 = new NbtList();
        NbtList listTag3 = new NbtList();
        NbtList listTag4 = new NbtList();
        NbtList listTag5 = new NbtList();
        NbtList listTag6 = new NbtList();
        smallShaftLeftEntrances.forEach(pos -> listTag1.add(new NbtIntArray(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        smallShaftRightEntrances.forEach(pos -> listTag2.add(new NbtIntArray(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        sideRoomEntrances.forEach(blockBox -> listTag3.add(new NbtIntArray(new int[]{blockBox.getMinY(), blockBox.getMinZ(), blockBox.getMaxX(), blockBox.getMaxY(), blockBox.getMaxZ(), blockBox.getMinX()})));
        bigSupports.forEach(z -> listTag4.add(NbtInt.of(z)));
        smallSupports.forEach(z -> listTag5.add(NbtInt.of(z)));
        gravelDeposits.forEach(pair -> listTag6.add(new NbtIntArray(new int[]{pair.getLeft(), pair.getRight()})));
        tag.put("SmallShaftLeftEntrances", listTag1);
        tag.put("SmallShaftRightEntrances", listTag2);
        tag.put("SideRoomEntrances", listTag3);
        tag.put("BigSupports", listTag4);
        tag.put("SmallSupports", listTag5);
        tag.put("GravelDeposits", listTag6);
    }

    public static BlockBox determineBoxPosition(int x, int y, int z, Direction direction) {
        return BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);
    }

    @Override
    public void fillOpenings(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random) {
        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        // Extend big tunnel in same direction
        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX(), this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, direction, chainLength);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX(), this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, direction, chainLength);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ(), direction, chainLength);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ(), direction, chainLength);
        }

        // Get the length of the main axis. This SHOULD be equal to the MAIN_AXIS_LEN variable.
        int pieceLen = this.getFacing().getAxis() == Direction.Axis.Z ? this.boundingBox.getBlockCountZ() : this.boundingBox.getBlockCountY();

        // Build side rooms
        buildSideRoomsLeft(structurePiece, structurePiecesHolder, random, direction, pieceLen);
        buildSideRoomsRight(structurePiece, structurePiecesHolder, random, direction, pieceLen);

        // Build small shafts and mark their entrances
        buildSmallShaftsLeft(structurePiece, structurePiecesHolder, random, direction, pieceLen);
        buildSmallShaftsRight(structurePiece, structurePiecesHolder, random, direction, pieceLen);

        // Decorations
        buildSupports(random);
        buildGravelDeposits(random);
    }

    @Override
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if in ocean biome
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 3, LOCAL_Z_END, AIR);
        this.fill(world, box, 2, LOCAL_Y_END - 3, 0, LOCAL_X_END - 2, LOCAL_Y_END - 2, LOCAL_Z_END, AIR);
        this.fill(world, box, 3, LOCAL_Y_END - 1, 0, LOCAL_X_END - 3, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());

        // Core structure
        generateSmallShaftEntrances(world, box, random);
        generateSideRoomOpenings(world, box, random);
        generateLegs(world, box, random);
        generateBigSupports(world, box, random);
        generateSmallSupports(world, box, random);

        // Decorations
        generateRails(world, box, random);
        generateChestCarts(world, box, random);
        generateTntCarts(world, box, random);
        generateGravelDeposits(world, box, random);
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
        generateLanterns(world, box, random);
        generateCobwebs(world, box, random);

        return true;
    }

    private void generateSmallShaftEntrances(StructureWorldAccess world, BlockBox box, Random random) {
        smallShaftLeftEntrances.forEach(entrancePos -> {
            int x = entrancePos.getX();
            int y = entrancePos.getY();
            int z = entrancePos.getZ();

            this.fill(world, box, x, y, z, x, y, z + 2, AIR);
            this.addBlock(world, getSupportBlock(), x, y + 1, z, box);
            this.addBlock(world, getSupportBlock(), x, y + 1, z + 2, box);
            this.fill(world, box, x + 1, y, z, x + 1, y + 1, z, getSupportBlock());
            this.fill(world, box, x + 1, y, z + 2, x + 1, y + 1, z + 2, getSupportBlock());
            this.chanceFill(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, getMainBlock());
            this.fill(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, AIR);
            this.replaceAir(world, box, x, y - 1, z, x + 1, y - 1, z + 2, getMainBlock());
        });

        smallShaftRightEntrances.forEach(entrancePos -> {
            int x = entrancePos.getX();
            int y = entrancePos.getY();
            int z = entrancePos.getZ();

            this.replaceAir(world, box, x, y - 1, z, x + 1, y - 1, z + 2, getMainBlock());
            this.fill(world, box, x + 1, y, z, x + 1, y, z + 2, AIR);
            this.addBlock(world, getSupportBlock(), x + 1, y + 1, z, box);
            this.addBlock(world, getSupportBlock(), x + 1, y + 1, z + 2, box);
            this.fill(world, box, x, y, z, x, y + 1, z, getSupportBlock());
            this.fill(world, box, x, y, z + 2, x, y + 1, z + 2, getSupportBlock());
            this.chanceFill(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, getMainBlock());
            this.fill(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, AIR);
        });
    }

    private void generateLegs(StructureWorldAccess world, BlockBox box, Random random) {
        // Ice and mushroom biome variants have different legs
        if (this.mineshaftType == BetterMineshaftStructure.Type.ICE || this.mineshaftType == BetterMineshaftStructure.Type.MUSHROOM) {
            generateLegsVariant(world, box, random);
            return;
        }

        // Configure support block
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(Properties.NORTH_WALL_SHAPE) && supportBlock.getProperties().contains(Properties.SOUTH_WALL_SHAPE)) {
            supportBlock = supportBlock.with(Properties.NORTH_WALL_SHAPE, WallShape.TALL).with(Properties.SOUTH_WALL_SHAPE, WallShape.TALL);
        } else if (supportBlock.getProperties().contains(Properties.NORTH) && supportBlock.getProperties().contains(Properties.SOUTH)) {
            supportBlock = supportBlock.with(Properties.NORTH, true).with(Properties.SOUTH, true);
        }

        // Get leg selector
        BlockSetSelector legSelector = getLegSelector();

        // Left side
        generateLeg(world, random, box, 1, 0, legSelector);
        this.replaceAir(world, box, 1, -1, 1, 1, -1, 5, supportBlock);
        this.replaceAir(world, box, 1, -2, 1, 1, -2, 3, supportBlock);
        this.replaceAir(world, box, 1, -3, 1, 1, -3, 2, supportBlock);
        this.replaceAir(world, box, 1, -5, 1, 1, -4, 1, supportBlock);
        this.replaceAir(world, box, 1, -1, 6, 1, -1, 10, supportBlock);
        this.replaceAir(world, box, 1, -2, 8, 1, -2, 10, supportBlock);
        this.replaceAir(world, box, 1, -3, 9, 1, -3, 10, supportBlock);
        this.replaceAir(world, box, 1, -5, 10, 1, -4, 10, supportBlock);
        generateLeg(world, random, box, 1, 11, legSelector);
        generateLeg(world, random, box, 1, 12, legSelector);
        this.replaceAir(world, box, 1, -1, 13, 1, -1, 17, supportBlock);
        this.replaceAir(world, box, 1, -2, 13, 1, -2, 15, supportBlock);
        this.replaceAir(world, box, 1, -3, 13, 1, -3, 14, supportBlock);
        this.replaceAir(world, box, 1, -5, 13, 1, -4, 13, supportBlock);
        this.replaceAir(world, box, 1, -1, 18, 1, -1, 22, supportBlock);
        this.replaceAir(world, box, 1, -2, 20, 1, -2, 22, supportBlock);
        this.replaceAir(world, box, 1, -3, 21, 1, -3, 22, supportBlock);
        this.replaceAir(world, box, 1, -5, 22, 1, -4, 22, supportBlock);
        generateLeg(world, random, box, 1, LOCAL_Z_END, legSelector);

        // Right side
        generateLeg(world, random, box, LOCAL_X_END - 1, 0, legSelector);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 1, LOCAL_X_END - 1, -1, 5, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 1, LOCAL_X_END - 1, -2, 3, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 1, LOCAL_X_END - 1, -3, 2, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 1, LOCAL_X_END - 1, -4, 1, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 6, LOCAL_X_END - 1, -1, 10, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 8, LOCAL_X_END - 1, -2, 10, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 9, LOCAL_X_END - 1, -3, 10, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 10, LOCAL_X_END - 1, -4, 10, supportBlock);
        generateLeg(world, random, box, LOCAL_X_END - 1, 11, legSelector);
        generateLeg(world, random, box, LOCAL_X_END - 1, 12, legSelector);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 13, LOCAL_X_END - 1, -1, 17, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 13, LOCAL_X_END - 1, -2, 15, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 13, LOCAL_X_END - 1, -3, 14, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 13, LOCAL_X_END - 1, -4, 13, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -1, 18, LOCAL_X_END - 1, -1, 22, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -2, 20, LOCAL_X_END - 1, -2, 22, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -3, 21, LOCAL_X_END - 1, -3, 22, supportBlock);
        this.replaceAir(world, box, LOCAL_X_END - 1, -5, 22, LOCAL_X_END - 1, -4, 22, supportBlock);
        generateLeg(world, random, box, LOCAL_X_END - 1, LOCAL_Z_END, legSelector);
    }

    private void generateLegsVariant(StructureWorldAccess world, BlockBox box, Random random) {
        BlockSetSelector legSelector = getLegSelector();
        for (int z = 0; z <= LOCAL_Z_END; z += 7) {
            generateLeg(world, random, box, 2, z + 1, legSelector);
            generateLeg(world, random, box, LOCAL_X_END - 2, z + 1, legSelector);

            this.replaceAir(world, box, random, 1, -1, z, LOCAL_X_END - 1, -1, z + 2, legSelector);

            this.replaceAir(world, box, random, 2, -1, z + 3, 2, -1, z + 3, legSelector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -1, z + 3, LOCAL_X_END - 2, -1, z + 3, legSelector);

            this.replaceAir(world, box, random, 3, -1, z + 3, LOCAL_X_END - 3, -1, z + 6, legSelector);

            this.replaceAir(world, box, random, 2, -1, z + 6, 2, -1, z + 6, legSelector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -1, z + 6, LOCAL_X_END - 2, -1, z + 6, legSelector);

            this.replaceAir(world, box, random, 2, -2, z, 2, -2, z, legSelector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -2, z, LOCAL_X_END - 2, -2, z, legSelector);

            this.replaceAir(world, box, random, 2, -2, z + 2, 2, -2, z + 2, legSelector);
            this.replaceAir(world, box, random, LOCAL_X_END - 2, -2, z + 2, LOCAL_X_END - 2, -2, z + 2, legSelector);

            this.replaceAir(world, box, random, 1, -2, z + 1, 1, -2, z + 1, legSelector);
            this.replaceAir(world, box, random, LOCAL_X_END - 1, -2, z + 1, LOCAL_X_END - 1, -2, z + 1, legSelector);

            this.replaceAir(world, box, random, 3, -2, z + 1, 3, -2, z + 1, legSelector);
            this.replaceAir(world, box, random, LOCAL_X_END - 3, -2, z + 1, LOCAL_X_END - 3, -2, z + 1, legSelector);
        }
    }

    private void generateGravelDeposits(StructureWorldAccess world, BlockBox box, Random random) {
        gravelDeposits.forEach(pair -> {
            int z = pair.getLeft();
            int side = pair.getRight();
            switch (side) {
                case 0: // Left side
                default:
                    // Row closest to wall
                    this.replaceAir(world, box, 1, 1, z, 1, 2, z + 2, getGravel());
                    this.replaceAir(world, box, 1, 3, z + 1, 1, 3 + random.nextInt(2), z + 1, getGravel());
                    this.chanceReplaceAir(world, box, random, .5f, 1, 3, z, 1, 3, z + 2, getGravel());
                    // Middle row
                    this.replaceAir(world, box, 2, 1, z + 1, 2, 2 + random.nextInt(2), z + 1, getGravel());
                    this.replaceAir(world, box, 2, 1, z, 2, 1 + random.nextInt(2), z + 2, getGravel());
                    // Innermost row
                    this.chanceReplaceAir(world, box, random, .5f, 3, 1, z, 3, 1, z + 2, getGravel());
                    break;
                case 1: // Right side
                    // Row closest to wall
                    this.replaceAir(world, box, LOCAL_X_END - 1, 1, z, LOCAL_X_END - 1, 2, z + 2, getGravel());
                    this.replaceAir(world, box, LOCAL_X_END - 1, 3, z + 1, LOCAL_X_END - 1, 3 + random.nextInt(2), z + 1, getGravel());
                    this.chanceReplaceAir(world, box, random, .5f, LOCAL_X_END - 1, 3, z, LOCAL_X_END - 1, 3, z + 2, getGravel());
                    // Middle row
                    this.replaceAir(world, box, LOCAL_X_END - 2, 1, z + 1, LOCAL_X_END - 2, 2 + random.nextInt(2), z + 1, getGravel());
                    this.replaceAir(world, box, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 2, 1 + random.nextInt(2), z + 2, getGravel());
                    // Innermost row
                    this.chanceReplaceAir(world, box, random, .5f, LOCAL_X_END - 3, 1, z, LOCAL_X_END - 3, 1, z + 2, getGravel());
            }
        });
    }

    private void generateCobwebs(StructureWorldAccess world, BlockBox box, Random random) {
        float chance = (float) BetterMineshafts.CONFIG.spawnRates.cobwebSpawnRate;
        smallSupports.forEach(z -> {
            this.chanceReplaceAir(world, box, random, chance, 2, 3, z - 1, LOCAL_X_END - 2, 4, z + 1, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 3, 5, z, LOCAL_X_END - 3, 5, z, Blocks.COBWEB.getDefaultState());
        });

        bigSupports.forEach(z -> {
            this.chanceReplaceAir(world, box, random, chance, 1, 1, z, 1, 4, z + 2, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, LOCAL_X_END - 1, 1, z, LOCAL_X_END - 1, 4, z + 2, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 2, 5, z, LOCAL_X_END - 2, 5, z + 2, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 2, 4, z + 1, LOCAL_X_END - 2, 4, z + 1, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 3, 6, z + 1, LOCAL_X_END - 3, 6, z + 1, Blocks.COBWEB.getDefaultState());
        });
    }

    private void generateChestCarts(StructureWorldAccess world, BlockBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.mainShaftChestMinecartSpawnRate) {
                BlockPos blockPos = new BlockPos(this.applyXTransform(LOCAL_X_END / 2, z), applyYTransform(1), this.applyZTransform(LOCAL_X_END / 2, z));
                if (box.contains(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(world.toServerWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(LootTables.ABANDONED_MINESHAFT_CHEST, random.nextLong());
                    world.spawnEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateTntCarts(StructureWorldAccess world, BlockBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.mainShaftTntMinecartSpawnRate) {
                BlockPos blockPos = new BlockPos(this.applyXTransform(LOCAL_X_END / 2, z), applyYTransform(1), this.applyZTransform(LOCAL_X_END / 2, z));
                if (box.contains(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    TntMinecartEntity tntMinecartEntity = new TntMinecartEntity(world.toServerWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    world.spawnEntity(tntMinecartEntity);
                }
            }
        }
    }

    private void generateBigSupports(StructureWorldAccess world, BlockBox box, Random random) {
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(Properties.EAST_WALL_SHAPE) && supportBlock.getProperties().contains(Properties.WEST_WALL_SHAPE)) {
            supportBlock = supportBlock.with(Properties.EAST_WALL_SHAPE, WallShape.TALL).with(Properties.WEST_WALL_SHAPE, WallShape.TALL);
        } else if (supportBlock.getProperties().contains(Properties.EAST) && supportBlock.getProperties().contains(Properties.WEST)) {
            supportBlock = supportBlock.with(Properties.EAST, true).with(Properties.WEST, true);
        }

        for (int z : bigSupports) {
            // Bottom slabs
            this.chanceFill(world, box, random, .6f, 1, 1, z, 2, 1, z + 2, getMainSlab());
            this.chanceFill(world, box, random, .6f, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 1, 1, z + 2, getMainSlab());
            // Main blocks
            this.addBlock(world, getMainBlock(), 1, 1, z + 1, box);
            this.addBlock(world, getMainBlock(), LOCAL_X_END - 1, 1, z + 1, box);
            this.addBlock(world, getMainBlock(), 1, 4, z + 1, box);
            this.addBlock(world, getMainBlock(), LOCAL_X_END - 1, 4, z + 1, box);
            this.fill(world, box, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, getMainBlock());
            // Supports
            this.fill(world, box, 1, 2, z + 1, 1, 3, z + 1, getSupportBlock());
            this.fill(world, box, LOCAL_X_END - 1, 2, z + 1, LOCAL_X_END - 1, 3, z + 1, getSupportBlock());
            this.chanceReplaceNonAir(world, box, random, .4f, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, supportBlock);
        }
    }

    private void generateSmallSupports(StructureWorldAccess world, BlockBox box, Random random) {
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(Properties.EAST_WALL_SHAPE) && supportBlock.getProperties().contains(Properties.WEST_WALL_SHAPE)) {
            supportBlock = supportBlock.with(Properties.EAST_WALL_SHAPE, WallShape.TALL).with(Properties.WEST_WALL_SHAPE, WallShape.TALL);
        } else if (supportBlock.getProperties().contains(Properties.EAST) && supportBlock.getProperties().contains(Properties.WEST)) {
            supportBlock = supportBlock.with(Properties.EAST, true).with(Properties.WEST, true);
        }

        for (int z : smallSupports) {
            this.addBlock(world, getMainBlock(), 2, 1, z, box);
            this.addBlock(world, getMainBlock(), LOCAL_X_END - 2, 1, z, box);
            this.addBlock(world, getSupportBlock(), 2, 2, z, box);
            this.addBlock(world, getSupportBlock(), LOCAL_X_END - 2, 2, z, box);
            this.addBlock(world, getMainBlock(), 2, 3, z, box);
            this.addBlock(world, getMainBlock(), LOCAL_X_END - 2, 3, z, box);
            this.fill(world, box, 3, 4, z, LOCAL_X_END - 3, 4, z, getMainBlock());
            this.chanceReplaceNonAir(world, box, random, .5f, 3, 4, z, LOCAL_X_END - 3, 4, z, supportBlock);
            this.chanceFill(world, box, random, .4f, 2, 3, z, LOCAL_X_END - 2, 3, z, supportBlock);
            this.addBlock(world, supportBlock, 3, 3, z, box);
            this.addBlock(world, supportBlock, LOCAL_X_END - 3, 3, z, box);
        }
    }

    private void generateLanterns(StructureWorldAccess world, BlockBox box, Random random) {
        BlockState LANTERN = Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true);
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            for (int x = 3; x <= LOCAL_X_END - 3; x++) {
                // Check rate * 3 because this used to spawn in any of the 3 middle blocks, so the * 3 matches the spawn rate for the previous logic
                if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.lanternSpawnRate * 3) {
                    if (!this.getBlockAt(world, x, LOCAL_Y_END, z, box).isAir()) {
                        this.addBlock(world, LANTERN, x, LOCAL_Y_END - 1, z, box);
                        z += 20;
                    }
                }
            }
        }
    }

    private void generateRails(StructureWorldAccess world, BlockBox box, Random random) {
        // Place rails in center
        this.chanceFill(world, box, random, .5f, LOCAL_X_END / 2, 1, 0, LOCAL_X_END / 2, 1, LOCAL_Z_END, Blocks.RAIL.getDefaultState());
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

    private void generateSideRoomOpenings(StructureWorldAccess world, BlockBox chunkBox, Random random) {
        sideRoomEntrances.forEach(entranceBox -> {
            // Ensure floor in gap between tunnel and room
            this.replaceAir(world, chunkBox, random, entranceBox.getMinX(), 0, entranceBox.getMinZ(), entranceBox.getMaxX(), 0, entranceBox.getMaxZ(), getBrickSelector());
            switch (random.nextInt(3)) {
                case 0:
                    // Completely open
                    this.fill(world, chunkBox, entranceBox.getMinX(), entranceBox.getMinY(), entranceBox.getMinZ() + 2, entranceBox.getMaxX(), entranceBox.getMaxY(), entranceBox.getMaxZ() - 2, AIR);
                    return;
                case 1:
                    // A few columns for openings
                    this.fill(world, chunkBox, entranceBox.getMinX(), entranceBox.getMinY(), entranceBox.getMinZ() + 2, entranceBox.getMaxX(), entranceBox.getMaxY() - 1, entranceBox.getMinZ() + 2, AIR);
                    this.fill(world, chunkBox, entranceBox.getMinX(), entranceBox.getMinY(), entranceBox.getMinZ() + 4, entranceBox.getMaxX(), entranceBox.getMaxY() - 1, entranceBox.getMinZ() + 5, AIR);
                    this.fill(world, chunkBox, entranceBox.getMinX(), entranceBox.getMinY(), entranceBox.getMinZ() + 7, entranceBox.getMaxX(), entranceBox.getMaxY() - 1, entranceBox.getMinZ() + 7, AIR);
                    return;
                case 2:
                    // No openings - random block removal will expose these, probably
            }
        });
    }

    private void buildGravelDeposits(Random random) {
        for (int z = 0; z <= LOCAL_Z_END - 2; z++) {
            int r = random.nextInt(20);
            int currPos = z;
            if (r == 0) { // Left side
                gravelDeposits.add(new Pair<>(currPos, 0));
                z += 5;
            } else if (r == 1) { // Right side
                gravelDeposits.add(new Pair<>(currPos, 1));
                z += 5;
            }
        }
    }

    private void buildSupports(Random random) {
        int counter = 0;
        final int MAX_COUNT = 10; // max number of blocks before force spawning a support

        for (int z = 0; z <= LOCAL_Z_END - 2; z++) {
            counter++;

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

            int r = random.nextInt(8);
            if (r == 0 || counter >= MAX_COUNT) { // Big support
                bigSupports.add(z);
                counter = 0;
                z += 3;
            } else if (r == 1) { // Small support
                smallSupports.add(z);
                counter = 0;
                z += 3;
            }
        }
    }

    private void buildSideRoomsLeft(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.workstationSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() - 5, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() - n - 9, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() + 5, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + n + 9, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() - n - 9, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() + n + 9, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSideRoomsRight(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.workstationSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() + 5, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() - n, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() - 5, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + n, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() - n, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() + n, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 5, nextPieceDirection, chainLength);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BlockBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSmallShaftsLeft(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 4; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() - n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() - n, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() + n, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }
    }

    private void buildSmallShaftsRight(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 5; n < pieceLen; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() + 1, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() - n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n - 3));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() - 1, this.boundingBox.getMinY(), this.boundingBox.getMinZ() + n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxX() - n, this.boundingBox.getMinY(), this.boundingBox.getMinZ() - 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinX() + n, this.boundingBox.getMinY(), this.boundingBox.getMaxZ() + 1, nextPieceDirection, 0);
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