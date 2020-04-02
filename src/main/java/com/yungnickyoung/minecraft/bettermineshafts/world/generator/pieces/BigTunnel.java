package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import javafx.geometry.BoundingBox;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
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
    private final List<BlockBox> smallShaftEntrances = Lists.newLinkedList();
    private final List<BlockBox> sideRoomEntrances = Lists.newLinkedList();
    private static final int
        X_LEN = 9,
        Y_LEN = 8;
    private int Z_LEN;

    public BigTunnel(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, compoundTag);

        this.Z_LEN = compoundTag.getInt("Len");

        ListTag listTag1 = compoundTag.getList("SmallShaftEntrances", 11);
        ListTag listTag2 = compoundTag.getList("SideRoomEntrances", 11);

        for (int i = 0; i < listTag1.size(); ++i) {
            this.smallShaftEntrances.add(new BlockBox(listTag1.getIntArray(i)));
        }

        for (int i = 0; i < listTag2.size(); ++i) {
            this.sideRoomEntrances.add(new BlockBox(listTag2.getIntArray(i)));
        }
    }

    public BigTunnel(int i, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.BIG_TUNNEL, i, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
        if (this.getFacing().getAxis() == Direction.Axis.Z) {
            this.Z_LEN = blockBox.getBlockCountZ();
        }
        else {
            this.Z_LEN = blockBox.getBlockCountX();
        }
    }

    public BigTunnel(int i, Random random, BlockPos pos, Direction direction, BetterMineshaftFeature.Type type) {
        this(i, random, determineInitialBoxPosition(random, pos.getX(), pos.getY(), pos.getZ(), direction), direction, type);
    }

    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
        tag.putInt("Len", this.Z_LEN);
        ListTag listTag1 = new ListTag();
        ListTag listTag2 = new ListTag();
        smallShaftEntrances.forEach(blockBox -> listTag1.add(blockBox.toNbt()));
        sideRoomEntrances.forEach(blockBox -> listTag2.add(blockBox.toNbt()));
        tag.put("SmallShaftEntrances", listTag1);
        tag.put("SideRoomEntrances", listTag2);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = new BlockBox(x, y, z, x, y + Y_LEN, z);

        // Decrease by 8 until we can place, or until we determine not possible at all
        int n = (random.nextInt(3) + 6) * 8; // 48 to 64 blocks long max
        while (n > 0) {
            switch (direction) {
                case NORTH:
                default:
                    blockBox.maxX = x + X_LEN;
                    blockBox.minZ = z - (n - 1);
                    break;
                case SOUTH:
                    blockBox.maxX = x + X_LEN;
                    blockBox.maxZ = z + (n - 1);
                    break;
                case WEST:
                    blockBox.minX = x - (n - 1);
                    blockBox.maxZ = z + X_LEN;
                    break;
                case EAST:
                    blockBox.maxX = x + (n - 1);
                    blockBox.maxZ = z + X_LEN;
            }

            // If the blockBox does not intersect with any pieces, break from the loop
            if (StructurePiece.method_14932(list, blockBox) == null) { // findIntersecting
                break;
            }

            n -= 8;
        }

        // Return null if we were unable to get a box that doesn't intersect with other pieces.
        // Otherwise return the box.
        return n > 0 ? blockBox : null;
    }

    public static BlockBox determineInitialBoxPosition(Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = new BlockBox(x, y, z, x, y + Y_LEN, z);

        int n = 48 ; // Initial tunnel section is 48 blocks long
        switch (direction) {
            case NORTH:
            default:
                blockBox.maxX = x + X_LEN;
                blockBox.minZ = z - (n - 1);
                break;
            case SOUTH:
                blockBox.maxX = x + X_LEN;
                blockBox.maxZ = z + (n - 1);
                break;
            case WEST:
                blockBox.minX = x - (n - 1);
                blockBox.maxZ = z + X_LEN;
                break;
            case EAST:
                blockBox.maxX = x + (n - 1);
                blockBox.maxZ = z + X_LEN;
        }
        return blockBox;
    }

    /**
     * buildComponent
     */
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        int chainLen = this.method_14923(); // getComponentType
        Direction direction = this.getFacing();

        // Extend tunnel in same direction
        if (direction != null) {
            switch (direction) {
                case NORTH:
                default:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, chainLen);
                    break;
                case SOUTH:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, chainLen);
                    break;
                case WEST:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, direction, chainLen);
                    break;
                case EAST:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, direction, chainLen);
            }
        }

        // Add smaller side shafts and mark their entrance positions for generation
        for (int z = this.boundingBox.minZ; z <= this.boundingBox.maxZ - 2; z++) {
            float spawnChance = .1f;
            if (random.nextFloat() <= spawnChance) {
                // TODO - BetterMineshaftGenerator.generatePiece...
                smallShaftEntrances.add(new BlockBox(0, 1, z, 1, 3, z + 2));
                z += random.nextInt(7) + 5;
            }
        }
        for (int z = this.boundingBox.minZ; z <= this.boundingBox.maxZ - 2; z++) {
            float spawnChance = .1f;
            if (random.nextFloat() <= spawnChance) {
                // StructurePiece newPiece = BetterMineshaftGenerator.generatePiece...
//                if (newPiece != null) {
//                    newBox = newPiece.getBoundingBox();
//                    this.entrances.add(new BlockBox(newBox.minX, newBox.minY, this.boundingBox.minZ, newBox.maxX, newBox.maxY, this.boundingBox.minZ + 1));
//                    this.smallShaftEntrances.add(new BlockBox(X_LEN - 2, 1, z, X_LEN - 1, 3, z + 2)); this all needs to be fixed up
//                    z += random.nextInt(7) + 5;
//                }
            }
        }
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) { // check if box contains any liquid
//                return false;
        }
        int xEnd = X_LEN - 1,
            yEnd = Y_LEN - 1,
            zEnd = Z_LEN - 1;

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 0, xEnd - 1, yEnd, zEnd, AIR, AIR, false);

        // Add random blocks in floor
        this.randomFillWithOutline(world, box, random, .8f, 0, 0, 0, xEnd, 0, zEnd, Blocks.OAK_PLANKS.getDefaultState(), AIR, false);

        // Fill in any air in floor with planks
        this.replaceAirInBox(world, box, 0, 0, 0, xEnd, 0, zEnd, Blocks.OAK_PLANKS.getDefaultState());

        // Add cobble and bricks to walls
        this.randomFillWithOutline(world, box, random, .1f, 0, 0, 0, 0, yEnd, zEnd, Blocks.COBBLESTONE.getDefaultState(), AIR, true);
        this.randomFillWithOutline(world, box, random, .3f, 0, 0, 0, 0, yEnd, zEnd, Blocks.STONE_BRICKS.getDefaultState(), AIR, true);

        this.randomFillWithOutline(world, box, random, .1f, xEnd, 0, 0, xEnd, yEnd, zEnd, Blocks.COBBLESTONE.getDefaultState(), AIR, true);
        this.randomFillWithOutline(world, box, random, .3f, xEnd, 0, 0, xEnd, yEnd, zEnd, Blocks.STONE_BRICKS.getDefaultState(), AIR, true);

        // Small mineshaft entrances
        BetterMineshafts.LOGGER.info("WEFE");
        BetterMineshafts.LOGGER.info(smallShaftEntrances);
        this.smallShaftEntrances.forEach(smallBox ->
            this.fillWithOutline(world, box, smallBox.minX, smallBox.minY, smallBox.minZ, smallBox.maxX, smallBox.maxY, smallBox.maxZ, Blocks.REDSTONE_BLOCK.getDefaultState(), Blocks.REDSTONE_BLOCK.getDefaultState(), false)
        );

        return true;
    }
}