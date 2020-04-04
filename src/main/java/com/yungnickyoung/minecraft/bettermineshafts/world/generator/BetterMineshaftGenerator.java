package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.google.common.collect.Lists;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.CustomCrossing;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.BigTunnel;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPart;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.SideRoom;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.List;
import java.util.Random;

public class BetterMineshaftGenerator {
    public static MineshaftPart createRandomMineshaftPiece(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, BetterMineshaftFeature.Type type) {
        int rand = random.nextInt(100);
        BlockBox blockBox;

        if (rand >= 10) {
            blockBox = BigTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                return new BigTunnel(l, random, blockBox, direction, type);
            }
        }
        else {
            blockBox = CustomCrossing.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                return new CustomCrossing(l, random, blockBox, direction, type);
            }
        }

        return null;
    }

    public static MineshaftPart createMineshaftPiece(List<StructurePiece> list, StructurePieceType pieceType, Random random, int x, int y, int z, Direction direction, int l, BetterMineshaftFeature.Type type) {
        BlockBox blockBox;
        if (pieceType == BetterMineshaftStructurePieceType.BIG_TUNNEL) {
            blockBox = BigTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                return new BigTunnel(l, random, blockBox, direction, type);
            }
        }
        else if (pieceType == BetterMineshaftStructurePieceType.CUSTOM_CROSSING) {
            blockBox = CustomCrossing.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                return new CustomCrossing(l, random, blockBox, direction, type);
            }
        }
        else if (pieceType == BetterMineshaftStructurePieceType.SIDE_ROOM) {
            blockBox = SideRoom.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                return new SideRoom(l, random, blockBox, direction, type);
            }
        }
        return null;
    }

    /**
     * chainLen refers to the length of the chain of pieces leading up to this one, in terms of number of chunks the structure spans
     */
    public static MineshaftPart generateAndAddRandomPiece(
        StructurePiece structurePiece,
        List<StructurePiece> list,
        Random random,
        int x, int y, int z,
        Direction direction, int chainLen
    ) {
        if (chainLen > 8) {
            return null; // Quit out if chain is past max length
        }
//        else if (Math.abs(x - structurePiece.getBoundingBox().minX) <= 80 && Math.abs(z - structurePiece.getBoundingBox().minZ) <= 80) {
            BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;
            MineshaftPart mineshaftPart = createRandomMineshaftPiece(list, random, x, y, z, direction, chainLen + 1, type);
            if (mineshaftPart != null) {
                list.add(mineshaftPart);
                mineshaftPart.method_14918(structurePiece, list, random); // buildComponent
            }

            return mineshaftPart;
//        }
//        else {
//            return null;
//        }
    }

    public static MineshaftPart generateAndAddPiece(
        StructurePiece structurePiece,
        StructurePieceType pieceType,
        BetterMineshaftFeature.Type mineshaftType,
        List<StructurePiece> list,
        Random random,
        int x, int y, int z,
        Direction direction, int chainLen
    ) {
        if (chainLen > 8) {
            return null; // Quit out if chain is past max length
        }

        MineshaftPart mineshaftPart = createMineshaftPiece(list, pieceType, random, x, y, z, direction, chainLen + 1, mineshaftType);
        if (mineshaftPart != null) {
            list.add(mineshaftPart);
            mineshaftPart.method_14918(structurePiece, list, random); // buildComponent
        }

        return mineshaftPart;
    }

    public static class MineshaftStairs extends MineshaftPart {
        public MineshaftStairs(int i, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
            super(StructurePieceType.MINESHAFT_STAIRS, i, type);
            this.setOrientation(direction);
            this.boundingBox = blockBox;
        }

        public MineshaftStairs(StructureManager structureManager, CompoundTag compoundTag) {
            super(BetterMineshaftStructurePieceType.STAIRS, compoundTag);
        }

        public static BlockBox determineStairsBlockBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
            BlockBox blockBox = new BlockBox(x, y - 5, z, x, y + 2, z);
            // direction determines the maxX and maxZ
            switch (direction) {
                case NORTH:
                default:
                    blockBox.maxX = x + 2;
                    blockBox.minZ = z - 8;
                    break;
                case SOUTH:
                    blockBox.maxX = x + 2;
                    blockBox.maxZ = z + 8;
                    break;
                case WEST:
                    blockBox.minX = x - 8;
                    blockBox.maxZ = z + 2;
                    break;
                case EAST:
                    blockBox.maxX = x + 8;
                    blockBox.maxZ = z + 2;
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
            int i = this.method_14923();
            Direction direction = this.getFacing();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                        break;
                    case SOUTH:
                        BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        break;
                    case WEST:
                        BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.WEST, i);
                        break;
                    case EAST:
                        BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.EAST, i);
                }
            }

        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) { // checks if box contains liquid
                return false;
            } else {
                this.fillWithOutline(world, box, 0, 5, 0, 2, 7, 1, AIR, AIR, false);
                this.fillWithOutline(world, box, 0, 0, 7, 2, 2, 8, AIR, AIR, false);

                for (int i = 0; i < 5; ++i) {
                    this.fillWithOutline(world, box, 0, 5 - i - (i < 4 ? 1 : 0), 2 + i, 2, 7 - i, 2 + i, AIR, AIR, false);
                }

                return true;
            }
        }
    }

    public static class MineshaftCrossing extends MineshaftPart {
        private final Direction direction;
        private final boolean twoFloors;

        public MineshaftCrossing(StructureManager structureManager, CompoundTag compoundTag) {
            super(BetterMineshaftStructurePieceType.CROSSING, compoundTag);
            this.twoFloors = compoundTag.getBoolean("tf");
            this.direction = Direction.fromHorizontal(compoundTag.getInt("D"));
        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putBoolean("tf", this.twoFloors);
            tag.putInt("D", this.direction.getHorizontal());
        }

        public MineshaftCrossing(int i, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
            super(StructurePieceType.MINESHAFT_CROSSING, i, type);
            this.direction = direction;
            this.boundingBox = blockBox;
            this.twoFloors = blockBox.getBlockCountY() > 3;
        }

        public static BlockBox determineCrossingBlockBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction facing) {
            BlockBox blockBox = new BlockBox(x, y, z, x, y + 2, z);
            if (random.nextInt(4) == 0) {
                blockBox.maxY += 4;
            }

            switch (facing) {
                case NORTH:
                default:
                    blockBox.minX = x - 1;
                    blockBox.maxX = x + 3;
                    blockBox.minZ = z - 4;
                    break;
                case SOUTH:
                    blockBox.minX = x - 1;
                    blockBox.maxX = x + 3;
                    blockBox.maxZ = z + 4;
                    break;
                case WEST:
                    blockBox.minX = x - 4;
                    blockBox.minZ = z - 1;
                    blockBox.maxZ = z + 3;
                    break;
                case EAST:
                    blockBox.maxX = x + 4;
                    blockBox.minZ = z - 1;
                    blockBox.maxZ = z + 3;
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
            int i = this.method_14923(); // getComponentType
            switch (this.direction) {
                case NORTH:
                default:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
                    break;
                case SOUTH:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
                    break;
                case WEST:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    break;
                case EAST:
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
            }

            if (this.twoFloors) {
                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 4, this.boundingBox.minZ - 1, Direction.NORTH, i);
                }
                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 4, this.boundingBox.minZ + 1, Direction.WEST, i);
                }
                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 4, this.boundingBox.minZ + 1, Direction.EAST, i);
                }
                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 4, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                }
            }
        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) { // check if there is any liquid in box
                return false;
            } else {
                BlockState blockState = this.getMainBlock();
                if (this.twoFloors) {
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 2, this.boundingBox.maxZ, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 2, this.boundingBox.maxZ - 1, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, AIR, AIR, false);
                } else {
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, AIR, AIR, false);
                }

                this.placeSupportPillar(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.placeSupportPillar(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);

                for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i) {
                    for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j) {
                        if (this.getBlockAt(world, i, this.boundingBox.minY - 1, j, box).isAir() && this.isUnderSeaLevel(world, i, this.boundingBox.minY - 1, j, box)) {
                            this.addBlock(world, blockState, i, this.boundingBox.minY - 1, j, box);
                        }
                    }
                }

                return true;
            }
        }

        private void placeSupportPillar(IWorld iWorld, BlockBox blockBox, int x, int minY, int z, int maxY) {
            if (!this.getBlockAt(iWorld, x, maxY + 1, z, blockBox).isAir()) {
                this.fillWithOutline(iWorld, blockBox, x, minY, z, x, maxY, z, this.getMainBlock(), AIR, false);
            }

        }
    }

    public static class MineshaftCorridor extends MineshaftPart {
        private final boolean hasRails;
        private final boolean hasCobwebs;
        private boolean hasSpawner;
        private final int sectionCount;

        public MineshaftCorridor(StructureManager structureManager, CompoundTag compoundTag) {
            super(BetterMineshaftStructurePieceType.CORRIDOR, compoundTag);
            this.hasRails = compoundTag.getBoolean("hr");
            this.hasCobwebs = compoundTag.getBoolean("sc");
            this.hasSpawner = compoundTag.getBoolean("hps");
            this.sectionCount = compoundTag.getInt("Num");
        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putBoolean("hr", this.hasRails);
            tag.putBoolean("sc", this.hasCobwebs);
            tag.putBoolean("hps", this.hasSpawner);
            tag.putInt("Num", this.sectionCount);
        }

        public MineshaftCorridor(int i, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
            super(StructurePieceType.MINESHAFT_CORRIDOR, i, type);
            this.setOrientation(direction);
            this.boundingBox = blockBox;
            this.hasRails = random.nextInt(3) == 0;
            this.hasCobwebs = !this.hasRails && random.nextInt(23) == 0;
            if (this.getFacing().getAxis() == Direction.Axis.Z) {
                this.sectionCount = blockBox.getBlockCountZ() / 5;
            } else {
                this.sectionCount = blockBox.getBlockCountX() / 5;
            }

        }

        public static BlockBox determineCorridorBlockBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
            BlockBox blockBox = new BlockBox(x, y, z, x, y + 2, z);

            int i = random.nextInt(3) + 2; // i can be 2, 3, or 4
            while (i > 0) {
                int m = i * 5; // m can be 10, 15, or 20
                switch (direction) {
                    case NORTH:
                    default:
                        blockBox.maxX = x + 2;
                        blockBox.minZ = z - m - 1;
                        break;
                    case SOUTH:
                        blockBox.maxX = x + 2;
                        blockBox.maxZ = z + m - 1;
                        break;
                    case WEST:
                        blockBox.minX = x - m - 1;
                        blockBox.maxZ = z + 2;
                        break;
                    case EAST:
                        blockBox.maxX = x + m - 1;
                        blockBox.maxZ = z + 2;
                }

                // If the blockBox does not intersect with any pieces, break from the loop
                if (StructurePiece.method_14932(list, blockBox) == null) { // findIntersecting
                    break;
                }
                // Otherwise we will decrease i to try and see if a shorter corridor will avoid intersecting
                // existing pieces.
                i--;
            }

            // Return null if we were unable to get a box that doesn't intersect with other pieces.
            // Otherwise return the box.
            return i > 0 ? blockBox : null;
        }

        /**
         * buildComponent
         */
        public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
            int i = this.method_14923(); // getComponentType
            int j = random.nextInt(4);
            Direction direction = this.getFacing();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        if (j <= 1) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, Direction.WEST, i);
                        } else {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, Direction.EAST, i);
                        }
                        break;
                    case SOUTH:
                        if (j <= 1) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, Direction.WEST, i);
                        } else {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, Direction.EAST, i);
                        }
                        break;
                    case WEST:
                        if (j <= 1) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, Direction.NORTH, i);
                        } else {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        }
                        break;
                    case EAST:
                        if (j <= 1) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, Direction.NORTH, i);
                        } else {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        }
                }
            }

            if (i < 8) {
                int k;
                int l;
                if (direction != Direction.NORTH && direction != Direction.SOUTH) {
                    for (k = this.boundingBox.minX + 3; k + 3 <= this.boundingBox.maxX; k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, k, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i + 1);
                        } else if (l == 1) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, k, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, k, Direction.WEST, i + 1);
                        } else if (l == 1) {
                            BetterMineshaftGenerator.generateAndAddRandomPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, k, Direction.EAST, i + 1);
                        }
                    }
                }
            }

        }

        protected boolean addChest(IWorld world, BlockBox boundingBox, Random random, int x, int y, int z, Identifier lootTableId) {
            BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
            if (boundingBox.contains(blockPos) && world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos.down()).isAir()) {
                BlockState blockState = Blocks.RAIL.getDefaultState()
                    .with(RailBlock.SHAPE, random.nextBoolean()
                        ? RailShape.NORTH_SOUTH
                        : RailShape.EAST_WEST
                    );
                this.addBlock(world, blockState, x, y, z, boundingBox);
                ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(
                    world.getWorld(),
                    blockPos.getX() + 0.5f,
                    blockPos.getY() + 0.5f,
                    blockPos.getZ() + 0.5f
                );
                chestMinecartEntity.setLootTable(lootTableId, random.nextLong());
                world.spawnEntity(chestMinecartEntity);
                return true;
            } else {
                return false;
            }
        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) { // check if box contains any liquid
                return false;
            } else {
                int endOfShaftIThink = this.sectionCount * 5 - 1;
                BlockState blockState = this.getMainBlock();
                this.fillWithOutline(world, box, 0, 0, 0, 2, 1, endOfShaftIThink, AIR, AIR, false);
                this.fillWithOutlineUnderSealevel(world, box, random, 0.8F, 0, 2, 0, 2, 2, endOfShaftIThink, AIR, AIR, false, false);
                if (this.hasCobwebs) {
                    this.fillWithOutlineUnderSealevel(world, box, random, 0.6F, 0, 0, 0, 2, 1, endOfShaftIThink, Blocks.COBWEB.getDefaultState(), AIR, false, true);
                }

                int t;
                int n;
                for (t = 0; t < this.sectionCount; ++t) {
                    n = 2 + t * 5;
                    this.placeSupport(world, box, 0, 0, n, 2, 2, random);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.1F, 0, 2, n - 1);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.1F, 2, 2, n - 1);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.1F, 0, 2, n + 1);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.1F, 2, 2, n + 1);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.05F, 0, 2, n - 2);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.05F, 2, 2, n - 2);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.05F, 0, 2, n + 2);
                    this.randomlyPlaceCobwebIfBelowSeaLevel(world, box, random, 0.05F, 2, 2, n + 2);
                    if (random.nextInt(100) == 0) {
                        this.addChest(world, box, random, 2, 0, n - 1, LootTables.ABANDONED_MINESHAFT_CHEST);
                    }
                    if (random.nextInt(100) == 0) {
                        this.addChest(world, box, random, 0, 0, n + 1, LootTables.ABANDONED_MINESHAFT_CHEST);
                    }

                    if (this.hasCobwebs && !this.hasSpawner) {
                        int p = this.applyYTransform(0);
                        int q = n - 1 + random.nextInt(3);
                        int r = this.applyXTransform(1, q);
                        int s = this.applyZTransform(1, q);
                        BlockPos blockPos = new BlockPos(r, p, s);
                        if (box.contains(blockPos) && this.isUnderSeaLevel(world, 1, 0, q, box)) {
                            this.hasSpawner = true;
                            world.setBlockState(blockPos, Blocks.SPAWNER.getDefaultState(), 2);
                            BlockEntity blockEntity = world.getBlockEntity(blockPos);
                            if (blockEntity instanceof MobSpawnerBlockEntity) {
                                ((MobSpawnerBlockEntity) blockEntity).getLogic().setEntityId(EntityType.CAVE_SPIDER);
                            }
                        }
                    }
                }

                for (t = 0; t <= 2; ++t) {
                    for (n = 0; n <= endOfShaftIThink; ++n) {
                        BlockState blockState2 = this.getBlockAt(world, t, -1, n, box);
                        if (blockState2.isAir() && this.isUnderSeaLevel(world, t, -1, n, box)) {
                            this.addBlock(world, blockState, t, -1, n, box);
                        }
                    }
                }

                if (this.hasRails) {
                    BlockState blockState3 = Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.NORTH_SOUTH);

                    for (n = 0; n <= endOfShaftIThink; ++n) {
                        BlockState blockState4 = this.getBlockAt(world, 1, -1, n, box);
                        if (!blockState4.isAir() && blockState4.isFullOpaque(world, new BlockPos(this.applyXTransform(1, n), this.applyYTransform(-1), this.applyZTransform(1, n)))) {
                            float threshold = this.isUnderSeaLevel(world, 1, 0, n, box) ? 0.7F : 0.9F;
                            this.addBlockWithRandomThreshold(world, box, random, threshold, 1, 0, n, blockState3);
                        }
                    }
                }

                return true;
            }
        }

        private void placeSupport(IWorld iWorld, BlockBox blockBox, int x1, int y1, int z, int y2, int x2, Random random) {
            if (this.method_14719(iWorld, blockBox, x1, x2, y2, z)) { // for some reason makes sure theres no air above where the top 2 or 3 wood planks will go?
                BlockState mainBlock = this.getMainBlock();
                BlockState supportBlock = this.getSupportBlock();
                this.fillWithOutline(iWorld, blockBox, x1, y1, z, x1, y2 - 1, z, supportBlock.with(FenceBlock.WEST, true), AIR, false);
                this.fillWithOutline(iWorld, blockBox, x2, y1, z, x2, y2 - 1, z, supportBlock.with(FenceBlock.EAST, true), AIR, false);
                if (random.nextInt(4) == 0) {
                    this.fillWithOutline(iWorld, blockBox, x1, y2, z, x1, y2, z, mainBlock, AIR, false);
                    this.fillWithOutline(iWorld, blockBox, x2, y2, z, x2, y2, z, mainBlock, AIR, false);
                } else {
                    this.fillWithOutline(iWorld, blockBox, x1, y2, z, x2, y2, z, mainBlock, AIR, false);
                    this.addBlockWithRandomThreshold(iWorld, blockBox, random, 0.05F, x1 + 1, y2, z - 1, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.NORTH));
                    this.addBlockWithRandomThreshold(iWorld, blockBox, random, 0.05F, x1 + 1, y2, z + 1, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.SOUTH));
                }

            }
        }

        private void randomlyPlaceCobwebIfBelowSeaLevel(IWorld iWorld, BlockBox blockBox, Random random, float threshold, int x, int y, int z) {
            if (this.isUnderSeaLevel(iWorld, x, y, z, blockBox)) {
                this.addBlockWithRandomThreshold(iWorld, blockBox, random, threshold, x, y, z, Blocks.COBWEB.getDefaultState());
            }
        }
    }

    public static class MineshaftRoom extends MineshaftPart {
        private final List<BlockBox> entrances = Lists.newLinkedList();

        public MineshaftRoom(int i, Random random, int x, int z, BetterMineshaftFeature.Type type) {
            super(BetterMineshaftStructurePieceType.ROOM, i, type);
            this.mineshaftType = type;
            this.boundingBox = new BlockBox(x, 50, z, x + 7 + random.nextInt(6), 54 + random.nextInt(6), z + 7 + random.nextInt(6));
        }

        public MineshaftRoom(StructureManager structureManager, CompoundTag compoundTag) {
            super(StructurePieceType.MINESHAFT_ROOM, compoundTag);
            ListTag listTag = compoundTag.getList("Entrances", 11);

            for (int i = 0; i < listTag.size(); ++i) {
                this.entrances.add(new BlockBox(listTag.getIntArray(i)));
            }
        }

        /**
         * buildComponent
         */
        public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
            int i = this.method_14923();
            int k;

            BetterMineshaftFeature.Type mineshaftType = ((MineshaftPart)structurePiece).mineshaftType;
            MineshaftPart newPiece;
            BlockBox newBox;

            for (k = 0; k < this.boundingBox.getBlockCountX(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountX());
                if (k + 3 > this.boundingBox.getBlockCountX()) {
                    break;
                }

                newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.CORRIDOR, mineshaftType, list, random, this.boundingBox.minX + k, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                if (newPiece != null) {
                    newBox = newPiece.getBoundingBox();
                    this.entrances.add(new BlockBox(newBox.minX, newBox.minY, this.boundingBox.minZ, newBox.maxX, newBox.maxY, this.boundingBox.minZ + 1));
                }
            }

            for (k = 0; k < this.boundingBox.getBlockCountX(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountX());
                if (k + 3 > this.boundingBox.getBlockCountX()) {
                    break;
                }

                newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.CORRIDOR, mineshaftType, list, random, this.boundingBox.minX + k, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                if (newPiece != null) {
                    newBox = newPiece.getBoundingBox();
                    this.entrances.add(new BlockBox(newBox.minX, newBox.minY, this.boundingBox.maxZ - 1, newBox.maxX, newBox.maxY, this.boundingBox.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getBlockCountZ(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountZ());
                if (k + 3 > this.boundingBox.getBlockCountZ()) {
                    break;
                }

                newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.CORRIDOR, mineshaftType, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + k, Direction.WEST, i);
                if (newPiece != null) {
                    newBox = newPiece.getBoundingBox();
                    this.entrances.add(new BlockBox(this.boundingBox.minX, newBox.minY, newBox.minZ, this.boundingBox.minX + 1, newBox.maxY, newBox.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getBlockCountZ(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountZ());
                if (k + 3 > this.boundingBox.getBlockCountZ()) {
                    break;
                }

                newPiece = BetterMineshaftGenerator.generateAndAddPiece(structurePiece, BetterMineshaftStructurePieceType.CORRIDOR, mineshaftType, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + k, Direction.EAST, i);
                if (newPiece != null) {
                    newBox = newPiece.getBoundingBox();
                    this.entrances.add(new BlockBox(this.boundingBox.maxX - 1, newBox.minY, newBox.minZ, this.boundingBox.maxX, newBox.maxY, newBox.maxZ));
                }
            }
        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) { // check if any liquid in box
                return false;
            } else {
                this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.DIRT.getDefaultState(), AIR, true);
                this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AIR, AIR, false);

                entrances.forEach(blockBox ->
                    this.fillWithOutline(world, box, blockBox.minX, blockBox.minY, blockBox.minZ, blockBox.maxX, blockBox.maxY, blockBox.maxZ, AIR, Blocks.COBBLESTONE.getDefaultState(), false)
                );

                // I think the following function makes the round close-off at the top of the room
                this.method_14919(world, box, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AIR, false);
                return true;
            }
        }

        public void translate(int x, int y, int z) {
            super.translate(x, y, z);
            entrances.forEach(blockBox -> blockBox.offset(x, y, z));
        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            ListTag listTag = new ListTag();
            entrances.forEach(blockBox -> listTag.add(blockBox.toNbt()));
            tag.put("Entrances", listTag);
        }
    }
}