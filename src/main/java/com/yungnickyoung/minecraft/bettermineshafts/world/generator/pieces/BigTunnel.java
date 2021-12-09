package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.List;
import java.util.Random;

public class BigTunnel extends MineshaftPiece {
    private final List<BlockPos> smallShaftLeftEntrances = Lists.newLinkedList();
    private final List<BlockPos> smallShaftRightEntrances = Lists.newLinkedList();
    private final List<BoundingBox> sideRoomEntrances = Lists.newLinkedList();
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

    public BigTunnel(CompoundTag compoundTag) {
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
            this.sideRoomEntrances.add(new BoundingBox(listTag3.getIntArray(i)[0], listTag3.getIntArray(i)[1],
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

    public BigTunnel(int chainLength, Random random, BoundingBox blockBox, Direction direction, BetterMineshaftStructureFeature.Type type) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, chainLength, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
        ListTag listTag1 = new ListTag();
        ListTag listTag2 = new ListTag();
        ListTag listTag3 = new ListTag();
        ListTag listTag4 = new ListTag();
        ListTag listTag5 = new ListTag();
        ListTag listTag6 = new ListTag();
        smallShaftLeftEntrances.forEach(pos -> listTag1.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        smallShaftRightEntrances.forEach(pos -> listTag2.add(new IntArrayTag(new int[]{pos.getX(), pos.getY(), pos.getZ()})));
        sideRoomEntrances.forEach(blockBox -> listTag3.add(new IntArrayTag(new int[]{blockBox.minX(), blockBox.minY(), blockBox.minZ(), blockBox.maxX(), blockBox.maxY(), blockBox.maxZ()})));
        bigSupports.forEach(z -> listTag4.add(IntTag.valueOf(z)));
        smallSupports.forEach(z -> listTag5.add(IntTag.valueOf(z)));
        gravelDeposits.forEach(pair -> listTag6.add(new IntArrayTag(new int[]{pair.getFirst(), pair.getSecond()})));
        compoundTag.put("SmallShaftLeftEntrances", listTag1);
        compoundTag.put("SmallShaftRightEntrances", listTag2);
        compoundTag.put("SideRoomEntrances", listTag3);
        compoundTag.put("BigSupports", listTag4);
        compoundTag.put("SmallSupports", listTag5);
        compoundTag.put("GravelDeposits", listTag6);
    }

    public static BoundingBox determineBoxPosition(int x, int y, int z, Direction direction) {
        return BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);
    }

    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random) {
        Direction direction = this.getOrientation();
        if (direction == null) {
            return;
        }

        // Extend big tunnel in same direction
        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, direction, this.genDepth);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, direction, this.genDepth);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ(), direction, this.genDepth);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddBigTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), direction, this.genDepth);
        }

        // Get the length of the main axis. This SHOULD be equal to the MAIN_AXIS_LEN variable.
        int pieceLen = this.getOrientation().getAxis() == Direction.Axis.Z ? this.boundingBox.getZSpan() : this.boundingBox.getXSpan();

        // Build side rooms
        buildSideRoomsLeft(structurePiece, structurePieceAccessor, random, direction, pieceLen);
        buildSideRoomsRight(structurePiece, structurePieceAccessor, random, direction, pieceLen);

        // Build small shafts and mark their entrances
        buildSmallShaftsLeft(structurePiece, structurePieceAccessor, random, direction, pieceLen);
        buildSmallShaftsRight(structurePiece, structurePieceAccessor, random, direction, pieceLen);

        // Decorations
        buildSupports(random);
        buildGravelDeposits(random);
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 3, LOCAL_Z_END, AIR);
        this.fill(world, box, 2, LOCAL_Y_END - 3, 0, LOCAL_X_END - 2, LOCAL_Y_END - 2, LOCAL_Z_END, AIR);
        this.fill(world, box, 3, LOCAL_Y_END - 1, 0, LOCAL_X_END - 3, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        // Fill in any air in floor with main block
        this.replaceAirOrChains(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());

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
    }

    private void generateSmallShaftEntrances(WorldGenLevel world, BoundingBox box, Random random) {
        for (BlockPos entrancePos : smallShaftLeftEntrances) {
            int x = entrancePos.getX();
            int y = entrancePos.getY();
            int z = entrancePos.getZ();

            // Check if the area is covered. We only need to spawn supports in covered areas.
            int numCovered = 0; // We require at least 2 blocks to be covered
            for (int i = z; i <= z + 2; i++) {
                for (int j = x; j <= x + 1; j++) {
                    BlockState blockState = this.getBlock(world, j, y + 3, i, box);
                    if (!blockState.isAir() && !blockState.is(Blocks.CHAIN)) {
                        numCovered++;
                    }
                }
            }


            this.fill(world, box, x, y, z, x + 1, y + 2, z + 2, AIR);
            this.replaceAirOrChains(world, box, x, y - 1, z, x + 1, y - 1, z + 2, getMainBlock()); // Floor connecting big shaft to small shaft
            if (numCovered < 2) continue;

            this.placeBlock(world, getSupportBlock(), x, y + 1, z, box);
            this.placeBlock(world, getSupportBlock(), x, y + 1, z + 2, box);
            this.fill(world, box, x + 1, y, z, x + 1, y + 1, z, getSupportBlock());
            this.fill(world, box, x + 1, y, z + 2, x + 1, y + 1, z + 2, getSupportBlock());
            this.chanceFill(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, getMainBlock());
            this.fill(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, AIR);
        }

        for (BlockPos entrancePos : smallShaftRightEntrances) {
            int x = entrancePos.getX();
            int y = entrancePos.getY();
            int z = entrancePos.getZ();

            // Check if the area is covered. We only need to spawn supports in covered areas.
            int numCovered = 0; // We require at least 2 blocks to be covered
            for (int i = z; i <= z + 2; i++) {
                for (int j = x; j <= x + 1; j++) {
                    BlockState blockState = this.getBlock(world, j, y + 3, i, box);
                    if (!blockState.isAir() && !blockState.is(Blocks.CHAIN)) {
                        numCovered++;
                    }
                }
            }

            this.fill(world, box, x, y, z, x + 1, y + 2, z + 2, AIR);
            this.replaceAirOrChains(world, box, x, y - 1, z, x + 1, y - 1, z + 2, this.getMainBlock()); // Floor connecting big shaft to small shaft
            if (numCovered < 2) continue;

            this.placeBlock(world, getSupportBlock(), x + 1, y + 1, z, box);
            this.placeBlock(world, getSupportBlock(), x + 1, y + 1, z + 2, box);
            this.fill(world, box, x, y, z, x, y + 1, z, getSupportBlock());
            this.fill(world, box, x, y, z + 2, x, y + 1, z + 2, getSupportBlock());
            this.chanceFill(world, box, random, .75f, x, y + 2, z, x + 1, y + 2, z + 2, getMainBlock());
            this.fill(world, box, x, y + 1, z + 1, x + 1, y + 1, z + 1, AIR);
        }
    }

    private void generateLegs(WorldGenLevel world, BoundingBox box, Random random) {
        // Ice and mushroom biome variants have different legs
        if (this.mineshaftType == BetterMineshaftStructureFeature.Type.ICE || this.mineshaftType == BetterMineshaftStructureFeature.Type.MUSHROOM) {
            generateLegsVariant(world, box, random);
            return;
        }

        // Configure support block
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(BlockStateProperties.NORTH_WALL) && supportBlock.getProperties().contains(BlockStateProperties.SOUTH_WALL)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.NORTH_WALL, WallSide.TALL).setValue(BlockStateProperties.SOUTH_WALL, WallSide.TALL);
        } else if (supportBlock.getProperties().contains(BlockStateProperties.NORTH) && supportBlock.getProperties().contains(BlockStateProperties.SOUTH)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.NORTH, true).setValue(BlockStateProperties.SOUTH, true);
        }

        // Get leg selector
        BlockSetSelector legSelector = getLegSelector();

        // Begin generating legs. For each leg, if it successfully generates then we generate
        // Some supporting blocks around it.

        // Left side
        boolean generatedLeg = generateLegOrChain(world, random, box, 1, 0, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, 1, -1, 1, 1, -1, 5, supportBlock);
            this.replaceAirOrChains(world, box, 1, -2, 1, 1, -2, 3, supportBlock);
            this.replaceAirOrChains(world, box, 1, -3, 1, 1, -3, 2, supportBlock);
            this.replaceAirOrChains(world, box, 1, -5, 1, 1, -4, 1, supportBlock);
        }
        generatedLeg = generateLegOrChain(world, random, box, 1, 11, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, 1, -1, 6, 1, -1, 10, supportBlock);
            this.replaceAirOrChains(world, box, 1, -2, 8, 1, -2, 10, supportBlock);
            this.replaceAirOrChains(world, box, 1, -3, 9, 1, -3, 10, supportBlock);
            this.replaceAirOrChains(world, box, 1, -5, 10, 1, -4, 10, supportBlock);
        }
        generatedLeg = generateLegOrChain(world, random, box, 1, 12, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, 1, -1, 13, 1, -1, 17, supportBlock);
            this.replaceAirOrChains(world, box, 1, -2, 13, 1, -2, 15, supportBlock);
            this.replaceAirOrChains(world, box, 1, -3, 13, 1, -3, 14, supportBlock);
            this.replaceAirOrChains(world, box, 1, -5, 13, 1, -4, 13, supportBlock);
        }
        generatedLeg = generateLegOrChain(world, random, box, 1, LOCAL_Z_END, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, 1, -1, 18, 1, -1, 22, supportBlock);
            this.replaceAirOrChains(world, box, 1, -2, 20, 1, -2, 22, supportBlock);
            this.replaceAirOrChains(world, box, 1, -3, 21, 1, -3, 22, supportBlock);
            this.replaceAirOrChains(world, box, 1, -5, 22, 1, -4, 22, supportBlock);
        }

        // Right side
        generatedLeg = generateLegOrChain(world, random, box, LOCAL_X_END - 1, 0, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -1, 1, LOCAL_X_END - 1, -1, 5, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -2, 1, LOCAL_X_END - 1, -2, 3, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -3, 1, LOCAL_X_END - 1, -3, 2, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -5, 1, LOCAL_X_END - 1, -4, 1, supportBlock);
        }
        generatedLeg = generateLegOrChain(world, random, box, LOCAL_X_END - 1, 11, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -1, 6, LOCAL_X_END - 1, -1, 10, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -2, 8, LOCAL_X_END - 1, -2, 10, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -3, 9, LOCAL_X_END - 1, -3, 10, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -5, 10, LOCAL_X_END - 1, -4, 10, supportBlock);
        }
        generatedLeg = generateLegOrChain(world, random, box, LOCAL_X_END - 1, 12, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -1, 13, LOCAL_X_END - 1, -1, 17, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -2, 13, LOCAL_X_END - 1, -2, 15, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -3, 13, LOCAL_X_END - 1, -3, 14, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -5, 13, LOCAL_X_END - 1, -4, 13, supportBlock);
        }
        generatedLeg = generateLegOrChain(world, random, box, LOCAL_X_END - 1, LOCAL_Z_END, legSelector);
        if (generatedLeg) {
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -1, 18, LOCAL_X_END - 1, -1, 22, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -2, 20, LOCAL_X_END - 1, -2, 22, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -3, 21, LOCAL_X_END - 1, -3, 22, supportBlock);
            this.replaceAirOrChains(world, box, LOCAL_X_END - 1, -5, 22, LOCAL_X_END - 1, -4, 22, supportBlock);
        }
    }

    private void generateLegsVariant(WorldGenLevel world, BoundingBox box, Random random) {
        BlockSetSelector legSelector = getLegSelector();
        for (int z = 0; z <= LOCAL_Z_END; z += 7) {
            generateLeg(world, random, box, 2, z + 1, legSelector);
            generateLeg(world, random, box, LOCAL_X_END - 2, z + 1, legSelector);

            this.replaceAirOrChains(world, box, random, 1, -1, z, LOCAL_X_END - 1, -1, z + 2, legSelector);

            this.replaceAirOrChains(world, box, random, 2, -1, z + 3, 2, -1, z + 3, legSelector);
            this.replaceAirOrChains(world, box, random, LOCAL_X_END - 2, -1, z + 3, LOCAL_X_END - 2, -1, z + 3, legSelector);

            this.replaceAirOrChains(world, box, random, 3, -1, z + 3, LOCAL_X_END - 3, -1, z + 6, legSelector);

            this.replaceAirOrChains(world, box, random, 2, -1, z + 6, 2, -1, z + 6, legSelector);
            this.replaceAirOrChains(world, box, random, LOCAL_X_END - 2, -1, z + 6, LOCAL_X_END - 2, -1, z + 6, legSelector);

            this.replaceAirOrChains(world, box, random, 2, -2, z, 2, -2, z, legSelector);
            this.replaceAirOrChains(world, box, random, LOCAL_X_END - 2, -2, z, LOCAL_X_END - 2, -2, z, legSelector);

            this.replaceAirOrChains(world, box, random, 2, -2, z + 2, 2, -2, z + 2, legSelector);
            this.replaceAirOrChains(world, box, random, LOCAL_X_END - 2, -2, z + 2, LOCAL_X_END - 2, -2, z + 2, legSelector);

            this.replaceAirOrChains(world, box, random, 1, -2, z + 1, 1, -2, z + 1, legSelector);
            this.replaceAirOrChains(world, box, random, LOCAL_X_END - 1, -2, z + 1, LOCAL_X_END - 1, -2, z + 1, legSelector);

            this.replaceAirOrChains(world, box, random, 3, -2, z + 1, 3, -2, z + 1, legSelector);
            this.replaceAirOrChains(world, box, random, LOCAL_X_END - 3, -2, z + 1, LOCAL_X_END - 3, -2, z + 1, legSelector);
        }
    }

    private void generateGravelDeposits(WorldGenLevel world, BoundingBox box, Random random) {
        gravelDeposits.forEach(pair -> {
            int z = pair.getFirst();
            int side = pair.getSecond();
            switch (side) {
                case 0: // Left side
                default:
                    // Row closest to wall
                    this.replaceAirOrChains(world, box, 1, 1, z, 1, 2, z + 2, getGravel());
                    this.replaceAirOrChains(world, box, 1, 3, z + 1, 1, 3 + random.nextInt(2), z + 1, getGravel());
                    this.chanceReplaceAir(world, box, random, .5f, 1, 3, z, 1, 3, z + 2, getGravel());
                    // Middle row
                    this.replaceAirOrChains(world, box, 2, 1, z + 1, 2, 2 + random.nextInt(2), z + 1, getGravel());
                    this.replaceAirOrChains(world, box, 2, 1, z, 2, 1 + random.nextInt(2), z + 2, getGravel());
                    // Innermost row
                    this.chanceReplaceAir(world, box, random, .5f, 3, 1, z, 3, 1, z + 2, getGravel());
                    break;
                case 1: // Right side
                    // Row closest to wall
                    this.replaceAirOrChains(world, box, LOCAL_X_END - 1, 1, z, LOCAL_X_END - 1, 2, z + 2, getGravel());
                    this.replaceAirOrChains(world, box, LOCAL_X_END - 1, 3, z + 1, LOCAL_X_END - 1, 3 + random.nextInt(2), z + 1, getGravel());
                    this.chanceReplaceAir(world, box, random, .5f, LOCAL_X_END - 1, 3, z, LOCAL_X_END - 1, 3, z + 2, getGravel());
                    // Middle row
                    this.replaceAirOrChains(world, box, LOCAL_X_END - 2, 1, z + 1, LOCAL_X_END - 2, 2 + random.nextInt(2), z + 1, getGravel());
                    this.replaceAirOrChains(world, box, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 2, 1 + random.nextInt(2), z + 2, getGravel());
                    // Innermost row
                    this.chanceReplaceAir(world, box, random, .5f, LOCAL_X_END - 3, 1, z, LOCAL_X_END - 3, 1, z + 2, getGravel());
            }
        });
    }

    private void generateChestCarts(WorldGenLevel world, BoundingBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.mainShaftChestMinecartSpawnRate) {
                BlockPos blockPos = this.getWorldPos(LOCAL_X_END / 2, 1, z);
                if (box.isInside(blockPos) && !world.getBlockState(blockPos.below()).isAir()) {
                    MinecartChest chestMinecartEntity = new MinecartChest(world.getLevel(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(BuiltInLootTables.ABANDONED_MINESHAFT, random.nextLong());
                    world.addFreshEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateTntCarts(WorldGenLevel world, BoundingBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.mainShaftTntMinecartSpawnRate) {
                BlockPos blockPos = this.getWorldPos(LOCAL_X_END / 2, 1, z);
                if (box.isInside(blockPos) && !world.getBlockState(blockPos.below()).isAir()) {
                    MinecartTNT tntMinecartEntity = new MinecartTNT(world.getLevel(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    world.addFreshEntity(tntMinecartEntity);
                }
            }
        }
    }

    private void generateBigSupports(WorldGenLevel world, BoundingBox box, Random random) {
        float cobwebChance = (float) BetterMineshafts.CONFIG.spawnRates.cobwebSpawnRate;
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(BlockStateProperties.EAST_WALL) && supportBlock.getProperties().contains(BlockStateProperties.WEST_WALL)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.EAST_WALL, WallSide.TALL).setValue(BlockStateProperties.WEST_WALL, WallSide.TALL);
        } else if (supportBlock.getProperties().contains(BlockStateProperties.EAST) && supportBlock.getProperties().contains(BlockStateProperties.WEST)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.EAST, true).setValue(BlockStateProperties.WEST, true);
        }

        for (int z : bigSupports) {
            // Check if the area is covered. We only need to spawn supports in covered areas.
            int numCovered = 0; // We require at least 2 blocks to be covered
            for (int x = 2; x <= LOCAL_X_END - 2; x++) {
                BlockState blockState = this.getBlock(world, x, 7, z, box);
                if (!blockState.isAir() && !blockState.is(Blocks.CHAIN)) {
                    numCovered++;
                }
            }

            if (numCovered < 2) continue;

            // Bottom slabs
            this.chanceFill(world, box, random, .6f, 1, 1, z, 2, 1, z + 2, getMainSlab());
            this.chanceFill(world, box, random, .6f, LOCAL_X_END - 2, 1, z, LOCAL_X_END - 1, 1, z + 2, getMainSlab());
            // Main blocks
            this.placeBlock(world, getMainBlock(), 1, 1, z + 1, box);
            this.placeBlock(world, getMainBlock(), LOCAL_X_END - 1, 1, z + 1, box);
            this.placeBlock(world, getMainBlock(), 1, 4, z + 1, box);
            this.placeBlock(world, getMainBlock(), LOCAL_X_END - 1, 4, z + 1, box);
            this.fill(world, box, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, getMainBlock());
            // Supports
            this.fill(world, box, 1, 2, z + 1, 1, 3, z + 1, getSupportBlock());
            this.fill(world, box, LOCAL_X_END - 1, 2, z + 1, LOCAL_X_END - 1, 3, z + 1, getSupportBlock());
            this.chanceReplaceNonAir(world, box, random, .4f, 2, 5, z + 1, LOCAL_X_END - 2, 5, z + 1, supportBlock);

            // Place cobwebs around support
            this.chanceReplaceAir(world, box, random, cobwebChance, 1, 1, z, 1, 4, z + 2, Blocks.COBWEB.defaultBlockState());
            this.chanceReplaceAir(world, box, random, cobwebChance, LOCAL_X_END - 1, 1, z, LOCAL_X_END - 1, 4, z + 2, Blocks.COBWEB.defaultBlockState());
            this.chanceReplaceAir(world, box, random, cobwebChance, 2, 5, z, LOCAL_X_END - 2, 5, z + 2, Blocks.COBWEB.defaultBlockState());
            this.chanceReplaceAir(world, box, random, cobwebChance, 2, 4, z + 1, LOCAL_X_END - 2, 4, z + 1, Blocks.COBWEB.defaultBlockState());
            this.chanceReplaceAir(world, box, random, cobwebChance, 3, 6, z + 1, LOCAL_X_END - 3, 6, z + 1, Blocks.COBWEB.defaultBlockState());
        }
    }

    private void generateSmallSupports(WorldGenLevel world, BoundingBox box, Random random) {
        float cobwebChance = (float) BetterMineshafts.CONFIG.spawnRates.cobwebSpawnRate;
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(BlockStateProperties.EAST_WALL) && supportBlock.getProperties().contains(BlockStateProperties.WEST_WALL)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.EAST_WALL, WallSide.TALL).setValue(BlockStateProperties.WEST_WALL, WallSide.TALL);
        } else if (supportBlock.getProperties().contains(BlockStateProperties.EAST) && supportBlock.getProperties().contains(BlockStateProperties.WEST)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.EAST, true).setValue(BlockStateProperties.WEST, true);
        }

        for (int z : smallSupports) {
            // Check if the area is covered. We only need to spawn supports in covered areas.
            int numCovered = 0; // We require at least 2 blocks to be covered
            for (int x = 2; x <= LOCAL_X_END - 2; x++) {
                BlockState blockState = this.getBlock(world, x, 7, z, box);
                if (!blockState.isAir() && !blockState.is(Blocks.CHAIN)) {
                    numCovered++;
                }
            }

            if (numCovered < 2) continue;

            // Place support
            this.placeBlock(world, getMainBlock(), 2, 1, z, box);
            this.placeBlock(world, getMainBlock(), LOCAL_X_END - 2, 1, z, box);
            this.placeBlock(world, getSupportBlock(), 2, 2, z, box);
            this.placeBlock(world, getSupportBlock(), LOCAL_X_END - 2, 2, z, box);
            this.placeBlock(world, getMainBlock(), 2, 3, z, box);
            this.placeBlock(world, getMainBlock(), LOCAL_X_END - 2, 3, z, box);
            this.fill(world, box, 3, 4, z, LOCAL_X_END - 3, 4, z, getMainBlock());
            this.chanceReplaceNonAir(world, box, random, .5f, 3, 4, z, LOCAL_X_END - 3, 4, z, supportBlock);
            this.chanceFill(world, box, random, .4f, 2, 3, z, LOCAL_X_END - 2, 3, z, supportBlock);
            this.placeBlock(world, supportBlock, 3, 3, z, box);
            this.placeBlock(world, supportBlock, LOCAL_X_END - 3, 3, z, box);

            // Place cobwebs around support
            this.chanceReplaceAir(world, box, random, cobwebChance, 2, 3, z - 1, LOCAL_X_END - 2, 4, z + 1, Blocks.COBWEB.defaultBlockState());
            this.chanceReplaceAir(world, box, random, cobwebChance, 3, 5, z, LOCAL_X_END - 3, 5, z, Blocks.COBWEB.defaultBlockState());
        }
    }

    private void generateLanterns(WorldGenLevel world, BoundingBox box, Random random) {
        BlockState LANTERN = Blocks.LANTERN.defaultBlockState().setValue(BlockStateProperties.HANGING, true);
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            for (int x = 3; x <= LOCAL_X_END - 3; x++) {
                // Check rate * 3 because this used to spawn in any of the 3 middle blocks, so the * 3 matches the spawn rate for the previous logic
                if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.lanternSpawnRate * 3) {
                    if (!this.getBlock(world, x, LOCAL_Y_END, z, box).isAir()) {
                        this.placeBlock(world, LANTERN, x, LOCAL_Y_END - 1, z, box);
                        z += 20;
                    }
                }
            }
        }
    }

    private void generateRails(WorldGenLevel world, BoundingBox box, Random random) {
        // Place rails in center
        this.chanceFill(world, box, random, .5f, LOCAL_X_END / 2, 1, 0, LOCAL_X_END / 2, 1, LOCAL_Z_END, Blocks.RAIL.defaultBlockState());
        // Place powered rails
        int blocksSinceLastRail = 0;
        for (int n = 0; n <= LOCAL_Z_END; n++) {
            blocksSinceLastRail++;
            if (random.nextInt(20) == 0 || blocksSinceLastRail > 25) {
                this.placeBlock(world, Blocks.POWERED_RAIL.defaultBlockState().setValue(BlockStateProperties.POWERED, true), LOCAL_X_END / 2, 1, n, box);
                blocksSinceLastRail = 0; // reset counter
            }
        }
    }

    private void generateSideRoomOpenings(WorldGenLevel world, BoundingBox chunkBox, Random random) {
        sideRoomEntrances.forEach(entranceBox -> {
            // Ensure floor in gap between tunnel and room
            this.replaceAirOrChains(world, chunkBox, random, entranceBox.minX(), 0, entranceBox.minZ(), entranceBox.maxX(), 0, entranceBox.maxZ(), getBrickSelector());
            switch (random.nextInt(3)) {
                case 0:
                    // Completely open
                    this.fill(world, chunkBox, entranceBox.minX(), entranceBox.minY(), entranceBox.minZ() + 2, entranceBox.maxX(), entranceBox.maxY(), entranceBox.maxZ() - 2, AIR);
                    return;
                case 1:
                    // A few columns for openings
                    this.fill(world, chunkBox, entranceBox.minX(), entranceBox.minY(), entranceBox.minZ() + 2, entranceBox.maxX(), entranceBox.maxY() - 1, entranceBox.minZ() + 2, AIR);
                    this.fill(world, chunkBox, entranceBox.minX(), entranceBox.minY(), entranceBox.minZ() + 4, entranceBox.maxX(), entranceBox.maxY() - 1, entranceBox.minZ() + 5, AIR);
                    this.fill(world, chunkBox, entranceBox.minX(), entranceBox.minY(), entranceBox.minZ() + 7, entranceBox.maxX(), entranceBox.maxY() - 1, entranceBox.minZ() + 7, AIR);
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

    private void buildSideRoomsLeft(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.workstationSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 5, this.boundingBox.minY(), this.boundingBox.maxZ() - n - 9, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 5, this.boundingBox.minY(), this.boundingBox.minZ() + n + 9, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() - n - 9, this.boundingBox.minY(), this.boundingBox.maxZ() + 5, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + n + 9, this.boundingBox.minY(), this.boundingBox.minZ() - 5, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSideRoomsRight(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 10; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.workstationSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 5, this.boundingBox.minY(), this.boundingBox.maxZ() - n, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 5, this.boundingBox.minY(), this.boundingBox.minZ() + n, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() - n, this.boundingBox.minY(), this.boundingBox.minZ() - 5, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(0, 1, n, 0, 3, n + 9));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSideRoomPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + n, this.boundingBox.minY(), this.boundingBox.maxZ() + 5, nextPieceDirection, this.genDepth);
                        if (newPiece != null) {
                            sideRoomEntrances.add(new BoundingBox(SECONDARY_AXIS_LEN - 1, 1, n, SECONDARY_AXIS_LEN - 1, 3, n + 9));
                        }
                        break;
                }
                n += 10;
            }
        }
    }

    private void buildSmallShaftsLeft(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 0; n < (pieceLen - 1) - 4; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() - n, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n + 1));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + n, this.boundingBox.minY(), this.boundingBox.minZ() - 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n + 1));
                        }
                        break;
                }

                n += random.nextInt(7) + 5;
            }
        }
    }

    private void buildSmallShaftsRight(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random, Direction direction, int pieceLen) {
        Direction nextPieceDirection;
        StructurePiece newPiece;
        for (int n = 5; n < pieceLen; n++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftSpawnRate) {
                switch (direction) {
                    case NORTH:
                    default:
                        nextPieceDirection = Direction.EAST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftRightEntrances.add(new BlockPos(SECONDARY_AXIS_LEN - 2, 1, n - 3));
                        }
                        break;
                    case SOUTH:
                        nextPieceDirection = Direction.WEST;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + n, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case WEST:
                        nextPieceDirection = Direction.NORTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() - n, this.boundingBox.minY(), this.boundingBox.minZ() - 1, nextPieceDirection, 0);
                        if (newPiece != null) {
                            this.smallShaftLeftEntrances.add(new BlockPos(0, 1, n - 3));
                        }
                        break;
                    case EAST:
                        nextPieceDirection = Direction.SOUTH;
                        newPiece = BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() + n, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, nextPieceDirection, 0);
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