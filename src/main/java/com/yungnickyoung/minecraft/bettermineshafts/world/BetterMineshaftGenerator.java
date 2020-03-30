package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.google.common.collect.Lists;

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
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.MineshaftFeature;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BetterMineshaftGenerator {
    private static BetterMineshaftGenerator.MineshaftPart method_14712(List<StructurePiece> list, Random random, int i, int j, int k, Direction direction, int l, MineshaftFeature.Type type) {
        int m = random.nextInt(100);
        BlockBox blockBox2;
        if (m >= 80) {
            blockBox2 = BetterMineshaftGenerator.MineshaftCrossing.method_14717(list, random, i, j, k, direction);
            if (blockBox2 != null) {
                return new MineshaftCrossing(l, blockBox2, direction, type);
            }
        } else if (m >= 70) {
            blockBox2 = BetterMineshaftGenerator.MineshaftStairs.method_14720(list, random, i, j, k, direction);
            if (blockBox2 != null) {
                return new MineshaftStairs(l, blockBox2, direction, type);
            }
        } else {
            blockBox2 = BetterMineshaftGenerator.MineshaftCorridor.method_14714(list, random, i, j, k, direction);
            if (blockBox2 != null) {
                return new MineshaftCorridor(l, random, blockBox2, direction, type);
            }
        }

        return null;
    }

    private static BetterMineshaftGenerator.MineshaftPart method_14711(StructurePiece structurePiece, List<StructurePiece> list, Random random, int i, int j, int k, Direction direction, int l) {
        if (l > 8) {
            return null;
        } else if (Math.abs(i - structurePiece.getBoundingBox().minX) <= 80 && Math.abs(k - structurePiece.getBoundingBox().minZ) <= 80) {
            MineshaftFeature.Type type = ((BetterMineshaftGenerator.MineshaftPart) structurePiece).mineshaftType;
            BetterMineshaftGenerator.MineshaftPart mineshaftPart = method_14712(list, random, i, j, k, direction, l + 1, type);
            if (mineshaftPart != null) {
                list.add(mineshaftPart);
                mineshaftPart.method_14918(structurePiece, list, random);
            }

            return mineshaftPart;
        } else {
            return null;
        }
    }

    public static class MineshaftStairs extends BetterMineshaftGenerator.MineshaftPart {
        public MineshaftStairs(int i, BlockBox blockBox, Direction direction, MineshaftFeature.Type type) {
            super(StructurePieceType.MINESHAFT_STAIRS, i, type);
            this.setOrientation(direction);
            this.boundingBox = blockBox;
        }

        public MineshaftStairs(StructureManager structureManager, CompoundTag compoundTag) {
            super(StructurePieceType.MINESHAFT_STAIRS, compoundTag);
        }

        public static BlockBox method_14720(List<StructurePiece> list, Random random, int i, int j, int k, Direction direction) {
            BlockBox blockBox = new BlockBox(i, j - 5, k, i, j + 3 - 1, k);
            switch (direction) {
                case NORTH:
                default:
                    blockBox.maxX = i + 3 - 1;
                    blockBox.minZ = k - 8;
                    break;
                case SOUTH:
                    blockBox.maxX = i + 3 - 1;
                    blockBox.maxZ = k + 8;
                    break;
                case WEST:
                    blockBox.minX = i - 8;
                    blockBox.maxZ = k + 3 - 1;
                    break;
                case EAST:
                    blockBox.maxX = i + 8;
                    blockBox.maxZ = k + 3 - 1;
            }

            return StructurePiece.method_14932(list, blockBox) != null ? null : blockBox;
        }

        public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
            int i = this.method_14923();
            Direction direction = this.getFacing();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                        break;
                    case SOUTH:
                        BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        break;
                    case WEST:
                        BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.WEST, i);
                        break;
                    case EAST:
                        BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, Direction.EAST, i);
                }
            }

        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) {
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

    public static class MineshaftCrossing extends BetterMineshaftGenerator.MineshaftPart {
        private final Direction direction;
        private final boolean twoFloors;

        public MineshaftCrossing(StructureManager structureManager, CompoundTag compoundTag) {
            super(StructurePieceType.MINESHAFT_CROSSING, compoundTag);
            this.twoFloors = compoundTag.getBoolean("tf");
            this.direction = Direction.fromHorizontal(compoundTag.getInt("D"));
        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putBoolean("tf", this.twoFloors);
            tag.putInt("D", this.direction.getHorizontal());
        }

        public MineshaftCrossing(int i, BlockBox blockBox, Direction direction, MineshaftFeature.Type type) {
            super(StructurePieceType.MINESHAFT_CROSSING, i, type);
            this.direction = direction;
            this.boundingBox = blockBox;
            this.twoFloors = blockBox.getBlockCountY() > 3;
        }

        public static BlockBox method_14717(List<StructurePiece> list, Random random, int i, int j, int k, Direction facing) {
            BlockBox blockBox = new BlockBox(i, j, k, i, j + 3 - 1, k);
            if (random.nextInt(4) == 0) {
                blockBox.maxY += 4;
            }

            switch (facing) {
                case NORTH:
                default:
                    blockBox.minX = i - 1;
                    blockBox.maxX = i + 3;
                    blockBox.minZ = k - 4;
                    break;
                case SOUTH:
                    blockBox.minX = i - 1;
                    blockBox.maxX = i + 3;
                    blockBox.maxZ = k + 3 + 1;
                    break;
                case WEST:
                    blockBox.minX = i - 4;
                    blockBox.minZ = k - 1;
                    blockBox.maxZ = k + 3;
                    break;
                case EAST:
                    blockBox.maxX = i + 3 + 1;
                    blockBox.minZ = k - 1;
                    blockBox.maxZ = k + 3;
            }

            return StructurePiece.method_14932(list, blockBox) != null ? null : blockBox;
        }

        public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
            int i = this.method_14923();
            switch (this.direction) {
                case NORTH:
                default:
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
                    break;
                case SOUTH:
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
                    break;
                case WEST:
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.WEST, i);
                    break;
                case EAST:
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, Direction.EAST, i);
            }

            if (this.twoFloors) {
                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ - 1, Direction.NORTH, i);
                }

                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, Direction.WEST, i);
                }

                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.minZ + 1, Direction.EAST, i);
                }

                if (random.nextBoolean()) {
                    BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + 1, this.boundingBox.minY + 3 + 1, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                }
            }

        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) {
                return false;
            } else {
                BlockState blockState = this.method_16443();
                if (this.twoFloors) {
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.minY + 3 - 1, this.boundingBox.maxZ - 1, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.maxY - 2, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.maxY - 2, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.minY + 3, this.boundingBox.minZ + 1, this.boundingBox.maxX - 1, this.boundingBox.minY + 3, this.boundingBox.maxZ - 1, AIR, AIR, false);
                } else {
                    this.fillWithOutline(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX - 1, this.boundingBox.maxY, this.boundingBox.maxZ, AIR, AIR, false);
                    this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ - 1, AIR, AIR, false);
                }

                this.method_14716(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.method_14716(world, box, this.boundingBox.minX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);
                this.method_14716(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.minZ + 1, this.boundingBox.maxY);
                this.method_14716(world, box, this.boundingBox.maxX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 1, this.boundingBox.maxY);

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

        private void method_14716(IWorld iWorld, BlockBox blockBox, int i, int j, int k, int l) {
            if (!this.getBlockAt(iWorld, i, l + 1, k, blockBox).isAir()) {
                this.fillWithOutline(iWorld, blockBox, i, j, k, i, l, k, this.method_16443(), AIR, false);
            }

        }
    }

    public static class MineshaftCorridor extends BetterMineshaftGenerator.MineshaftPart {
        private final boolean hasRails;
        private final boolean hasCobwebs;
        private boolean hasSpawner;
        private final int length;

        public MineshaftCorridor(StructureManager structureManager, CompoundTag compoundTag) {
            super(StructurePieceType.MINESHAFT_CORRIDOR, compoundTag);
            this.hasRails = compoundTag.getBoolean("hr");
            this.hasCobwebs = compoundTag.getBoolean("sc");
            this.hasSpawner = compoundTag.getBoolean("hps");
            this.length = compoundTag.getInt("Num");
        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            tag.putBoolean("hr", this.hasRails);
            tag.putBoolean("sc", this.hasCobwebs);
            tag.putBoolean("hps", this.hasSpawner);
            tag.putInt("Num", this.length);
        }

        public MineshaftCorridor(int i, Random random, BlockBox blockBox, Direction direction, MineshaftFeature.Type type) {
            super(StructurePieceType.MINESHAFT_CORRIDOR, i, type);
            this.setOrientation(direction);
            this.boundingBox = blockBox;
            this.hasRails = random.nextInt(3) == 0;
            this.hasCobwebs = !this.hasRails && random.nextInt(23) == 0;
            if (this.getFacing().getAxis() == Direction.Axis.Z) {
                this.length = blockBox.getBlockCountZ() / 5;
            } else {
                this.length = blockBox.getBlockCountX() / 5;
            }

        }

        public static BlockBox method_14714(List<StructurePiece> list, Random random, int i, int j, int k, Direction direction) {
            BlockBox blockBox = new BlockBox(i, j, k, i, j + 3 - 1, k);

            int l;
            for (l = random.nextInt(3) + 2; l > 0; --l) {
                int m = l * 5;
                switch (direction) {
                    case NORTH:
                    default:
                        blockBox.maxX = i + 3 - 1;
                        blockBox.minZ = k - (m - 1);
                        break;
                    case SOUTH:
                        blockBox.maxX = i + 3 - 1;
                        blockBox.maxZ = k + m - 1;
                        break;
                    case WEST:
                        blockBox.minX = i - (m - 1);
                        blockBox.maxZ = k + 3 - 1;
                        break;
                    case EAST:
                        blockBox.maxX = i + m - 1;
                        blockBox.maxZ = k + 3 - 1;
                }

                if (StructurePiece.method_14932(list, blockBox) == null) {
                    break;
                }
            }

            return l > 0 ? blockBox : null;
        }

        public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
            int i = this.method_14923();
            int j = random.nextInt(4);
            Direction direction = this.getFacing();
            if (direction != null) {
                switch (direction) {
                    case NORTH:
                    default:
                        if (j <= 1) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, Direction.WEST, i);
                        } else {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, Direction.EAST, i);
                        }
                        break;
                    case SOUTH:
                        if (j <= 1) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, Direction.WEST, i);
                        } else {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ - 3, Direction.EAST, i);
                        }
                        break;
                    case WEST:
                        if (j <= 1) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, Direction.NORTH, i);
                        } else {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                        }
                        break;
                    case EAST:
                        if (j <= 1) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ, direction, i);
                        } else if (j == 2) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.minZ - 1, Direction.NORTH, i);
                        } else {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX - 3, this.boundingBox.minY - 1 + random.nextInt(3), this.boundingBox.maxZ + 1, Direction.SOUTH, i);
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
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, k, this.boundingBox.minY, this.boundingBox.minZ - 1, Direction.NORTH, i + 1);
                        } else if (l == 1) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, k, this.boundingBox.minY, this.boundingBox.maxZ + 1, Direction.SOUTH, i + 1);
                        }
                    }
                } else {
                    for (k = this.boundingBox.minZ + 3; k + 3 <= this.boundingBox.maxZ; k += 5) {
                        l = random.nextInt(5);
                        if (l == 0) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, k, Direction.WEST, i + 1);
                        } else if (l == 1) {
                            BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, k, Direction.EAST, i + 1);
                        }
                    }
                }
            }

        }

        protected boolean addChest(IWorld world, BlockBox boundingBox, Random random, int x, int y, int z, Identifier lootTableId) {
            BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
            if (boundingBox.contains(blockPos) && world.getBlockState(blockPos).isAir() && !world.getBlockState(blockPos.down()).isAir()) {
                BlockState blockState = (BlockState) Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                this.addBlock(world, blockState, x, y, z, boundingBox);
                ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(world.getWorld(), (double) ((float) blockPos.getX() + 0.5F), (double) ((float) blockPos.getY() + 0.5F), (double) ((float) blockPos.getZ() + 0.5F));
                chestMinecartEntity.setLootTable(lootTableId, random.nextLong());
                world.spawnEntity(chestMinecartEntity);
                return true;
            } else {
                return false;
            }
        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) {
                return false;
            } else {
                int m = this.length * 5 - 1;
                BlockState blockState = this.method_16443();
                this.fillWithOutline(world, box, 0, 0, 0, 2, 1, m, AIR, AIR, false);
                this.fillWithOutlineUnderSealevel(world, box, random, 0.8F, 0, 2, 0, 2, 2, m, AIR, AIR, false, false);
                if (this.hasCobwebs) {
                    this.fillWithOutlineUnderSealevel(world, box, random, 0.6F, 0, 0, 0, 2, 1, m, Blocks.COBWEB.getDefaultState(), AIR, false, true);
                }

                int t;
                int x;
                for (t = 0; t < this.length; ++t) {
                    x = 2 + t * 5;
                    this.method_14713(world, box, 0, 0, x, 2, 2, random);
                    this.method_14715(world, box, random, 0.1F, 0, 2, x - 1);
                    this.method_14715(world, box, random, 0.1F, 2, 2, x - 1);
                    this.method_14715(world, box, random, 0.1F, 0, 2, x + 1);
                    this.method_14715(world, box, random, 0.1F, 2, 2, x + 1);
                    this.method_14715(world, box, random, 0.05F, 0, 2, x - 2);
                    this.method_14715(world, box, random, 0.05F, 2, 2, x - 2);
                    this.method_14715(world, box, random, 0.05F, 0, 2, x + 2);
                    this.method_14715(world, box, random, 0.05F, 2, 2, x + 2);
                    if (random.nextInt(100) == 0) {
                        this.addChest(world, box, random, 2, 0, x - 1, LootTables.ABANDONED_MINESHAFT_CHEST);
                    }

                    if (random.nextInt(100) == 0) {
                        this.addChest(world, box, random, 0, 0, x + 1, LootTables.ABANDONED_MINESHAFT_CHEST);
                    }

                    if (this.hasCobwebs && !this.hasSpawner) {
                        int p = this.applyYTransform(0);
                        int q = x - 1 + random.nextInt(3);
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
                    for (x = 0; x <= m; ++x) {
                        BlockState blockState2 = this.getBlockAt(world, t, -1, x, box);
                        if (blockState2.isAir() && this.isUnderSeaLevel(world, t, -1, x, box)) {
                            this.addBlock(world, blockState, t, -1, x, box);
                        }
                    }
                }

                if (this.hasRails) {
                    BlockState blockState3 = Blocks.RAIL.getDefaultState().with(RailBlock.SHAPE, RailShape.NORTH_SOUTH);

                    for (x = 0; x <= m; ++x) {
                        BlockState blockState4 = this.getBlockAt(world, 1, -1, x, box);
                        if (!blockState4.isAir() && blockState4.isFullOpaque(world, new BlockPos(this.applyXTransform(1, x), this.applyYTransform(-1), this.applyZTransform(1, x)))) {
                            float f = this.isUnderSeaLevel(world, 1, 0, x, box) ? 0.7F : 0.9F;
                            this.addBlockWithRandomThreshold(world, box, random, f, 1, 0, x, blockState3);
                        }
                    }
                }

                return true;
            }
        }

        private void method_14713(IWorld iWorld, BlockBox blockBox, int i, int j, int k, int l, int m, Random random) {
            if (this.method_14719(iWorld, blockBox, i, m, l, k)) {
                BlockState blockState = this.method_16443();
                BlockState blockState2 = this.method_14718();
                this.fillWithOutline(iWorld, blockBox, i, j, k, i, l - 1, k, blockState2.with(FenceBlock.WEST, true), AIR, false);
                this.fillWithOutline(iWorld, blockBox, m, j, k, m, l - 1, k, blockState2.with(FenceBlock.EAST, true), AIR, false);
                if (random.nextInt(4) == 0) {
                    this.fillWithOutline(iWorld, blockBox, i, l, k, i, l, k, blockState, AIR, false);
                    this.fillWithOutline(iWorld, blockBox, m, l, k, m, l, k, blockState, AIR, false);
                } else {
                    this.fillWithOutline(iWorld, blockBox, i, l, k, m, l, k, blockState, AIR, false);
                    this.addBlockWithRandomThreshold(iWorld, blockBox, random, 0.05F, i + 1, l, k - 1, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.NORTH));
                    this.addBlockWithRandomThreshold(iWorld, blockBox, random, 0.05F, i + 1, l, k + 1, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.FACING, Direction.SOUTH));
                }

            }
        }

        private void method_14715(IWorld iWorld, BlockBox blockBox, Random random, float f, int i, int j, int k) {
            if (this.isUnderSeaLevel(iWorld, i, j, k, blockBox)) {
                this.addBlockWithRandomThreshold(iWorld, blockBox, random, f, i, j, k, Blocks.COBWEB.getDefaultState());
            }

        }
    }

    public static class MineshaftRoom extends BetterMineshaftGenerator.MineshaftPart {
        private final List<BlockBox> entrances = Lists.newLinkedList();

        public MineshaftRoom(int i, Random random, int j, int k, MineshaftFeature.Type type) {
            super(StructurePieceType.MINESHAFT_ROOM, i, type);
            this.mineshaftType = type;
            this.boundingBox = new BlockBox(j, 50, k, j + 7 + random.nextInt(6), 54 + random.nextInt(6), k + 7 + random.nextInt(6));
        }

        public MineshaftRoom(StructureManager structureManager, CompoundTag compoundTag) {
            super(StructurePieceType.MINESHAFT_ROOM, compoundTag);
            ListTag listTag = compoundTag.getList("Entrances", 11);

            for (int i = 0; i < listTag.size(); ++i) {
                this.entrances.add(new BlockBox(listTag.getIntArray(i)));
            }

        }

        public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
            int i = this.method_14923();
            int j = this.boundingBox.getBlockCountY() - 3 - 1;
            if (j <= 0) {
                j = 1;
            }

            int k;
            BetterMineshaftGenerator.MineshaftPart structurePiece2;
            BlockBox blockBox4;
            for (k = 0; k < this.boundingBox.getBlockCountX(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountX());
                if (k + 3 > this.boundingBox.getBlockCountX()) {
                    break;
                }

                structurePiece2 = BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ - 1, Direction.NORTH, i);
                if (structurePiece2 != null) {
                    blockBox4 = structurePiece2.getBoundingBox();
                    this.entrances.add(new BlockBox(blockBox4.minX, blockBox4.minY, this.boundingBox.minZ, blockBox4.maxX, blockBox4.maxY, this.boundingBox.minZ + 1));
                }
            }

            for (k = 0; k < this.boundingBox.getBlockCountX(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountX());
                if (k + 3 > this.boundingBox.getBlockCountX()) {
                    break;
                }

                structurePiece2 = BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX + k, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.maxZ + 1, Direction.SOUTH, i);
                if (structurePiece2 != null) {
                    blockBox4 = structurePiece2.getBoundingBox();
                    this.entrances.add(new BlockBox(blockBox4.minX, blockBox4.minY, this.boundingBox.maxZ - 1, blockBox4.maxX, blockBox4.maxY, this.boundingBox.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getBlockCountZ(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountZ());
                if (k + 3 > this.boundingBox.getBlockCountZ()) {
                    break;
                }

                structurePiece2 = BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ + k, Direction.WEST, i);
                if (structurePiece2 != null) {
                    blockBox4 = structurePiece2.getBoundingBox();
                    this.entrances.add(new BlockBox(this.boundingBox.minX, blockBox4.minY, blockBox4.minZ, this.boundingBox.minX + 1, blockBox4.maxY, blockBox4.maxZ));
                }
            }

            for (k = 0; k < this.boundingBox.getBlockCountZ(); k += 4) {
                k += random.nextInt(this.boundingBox.getBlockCountZ());
                if (k + 3 > this.boundingBox.getBlockCountZ()) {
                    break;
                }

                structurePiece2 = BetterMineshaftGenerator.method_14711(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY + random.nextInt(j) + 1, this.boundingBox.minZ + k, Direction.EAST, i);
                if (structurePiece2 != null) {
                    blockBox4 = structurePiece2.getBoundingBox();
                    this.entrances.add(new BlockBox(this.boundingBox.maxX - 1, blockBox4.minY, blockBox4.minZ, this.boundingBox.maxX, blockBox4.maxY, blockBox4.maxZ));
                }
            }

        }

        public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
            if (this.method_14937(world, box)) {
                return false;
            } else {
                this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.DIRT.getDefaultState(), AIR, true);
                this.fillWithOutline(world, box, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min(this.boundingBox.minY + 3, this.boundingBox.maxY), this.boundingBox.maxZ, AIR, AIR, false);
                Iterator<BlockBox> var6 = this.entrances.iterator();

                while (var6.hasNext()) {
                    BlockBox blockBox = var6.next();
                    this.fillWithOutline(world, box, blockBox.minX, blockBox.maxY - 2, blockBox.minZ, blockBox.maxX, blockBox.maxY, blockBox.maxZ, AIR, AIR, false);
                }

                this.method_14919(world, box, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, AIR, false);
                return true;
            }
        }

        public void translate(int x, int y, int z) {
            super.translate(x, y, z);
            Iterator<BlockBox> var4 = this.entrances.iterator();

            while (var4.hasNext()) {
                BlockBox blockBox = var4.next();
                blockBox.offset(x, y, z);
            }

        }

        protected void toNbt(CompoundTag tag) {
            super.toNbt(tag);
            ListTag listTag = new ListTag();
            Iterator<BlockBox> var3 = this.entrances.iterator();

            while (var3.hasNext()) {
                BlockBox blockBox = var3.next();
                listTag.add(blockBox.toNbt());
            }

            tag.put("Entrances", listTag);
        }
    }

    abstract static class MineshaftPart extends StructurePiece {
        protected MineshaftFeature.Type mineshaftType;

        public MineshaftPart(StructurePieceType structurePieceType, int i, MineshaftFeature.Type type) {
            super(structurePieceType, i);
            this.mineshaftType = type;
        }

        public MineshaftPart(StructurePieceType structurePieceType, CompoundTag compoundTag) {
            super(structurePieceType, compoundTag);
            this.mineshaftType = MineshaftFeature.Type.byIndex(compoundTag.getInt("MST"));
        }

        protected void toNbt(CompoundTag tag) {
            tag.putInt("MST", this.mineshaftType.ordinal());
        }

        protected BlockState method_16443() {
            switch (this.mineshaftType) {
                case NORMAL:
                default:
                    return Blocks.REDSTONE_BLOCK.getDefaultState();
                case MESA:
                    return Blocks.DARK_OAK_PLANKS.getDefaultState();
            }
        }

        protected BlockState method_14718() {
            switch (this.mineshaftType) {
                case NORMAL:
                default:
                    return Blocks.OAK_FENCE.getDefaultState();
                case MESA:
                    return Blocks.DARK_OAK_FENCE.getDefaultState();
            }
        }

        protected boolean method_14719(BlockView blockView, BlockBox blockBox, int i, int j, int k, int l) {
            for (int m = i; m <= j; ++m) {
                if (this.getBlockAt(blockView, m, k + 1, l, blockBox).isAir()) {
                    return false;
                }
            }

            return true;
        }
    }
}