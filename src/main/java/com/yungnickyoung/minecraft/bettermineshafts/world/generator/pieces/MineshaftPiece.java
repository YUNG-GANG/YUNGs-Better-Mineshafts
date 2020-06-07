package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import net.minecraft.block.*;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;

import java.util.List;
import java.util.Random;

public abstract class MineshaftPiece extends StructurePiece {
    public BetterMineshaftFeature.Type mineshaftType;
    protected int pieceChainLen;

    public MineshaftPiece(StructurePieceType structurePieceType, int i, int pieceChainLen, BetterMineshaftFeature.Type type) {
        super(structurePieceType, i);
        this.mineshaftType = type;
        this.pieceChainLen = pieceChainLen;
    }

    public MineshaftPiece(StructurePieceType structurePieceType, CompoundTag compoundTag) {
        super(structurePieceType, compoundTag);
        this.mineshaftType = BetterMineshaftFeature.Type.byIndex(compoundTag.getInt("MST"));
    }

    protected void toNbt(CompoundTag tag) {
        tag.putInt("MST", this.mineshaftType.ordinal());
    }

    public void setBoundingBox(BlockBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * buildComponent.
     * Adds new pieces to the list passed in.
     * Also resolves variables local to this piece like support positions.
     * Does not actually place any blocks.
     */
    @Override
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
    }

    /**
     * Returns true if box contains any liquid.
     */
    @Override
    protected boolean method_14937(BlockView blockView, BlockBox blockBox) {
        return super.method_14937(blockView, blockBox);
    }


    protected BlockState getMainBlock() {
        switch (this.mineshaftType) {
            case NORMAL:
            default:
                return Blocks.OAK_PLANKS.getDefaultState();
            case MESA:
                return Blocks.DARK_OAK_PLANKS.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_PLANKS.getDefaultState();
            case ICE:
                return Blocks.SNOW_BLOCK.getDefaultState();
        }
    }

    protected BlockState getMainSlab() {
        switch (this.mineshaftType) {
            case NORMAL:
            default:
                return Blocks.OAK_SLAB.getDefaultState();
            case MESA:
                return Blocks.DARK_OAK_SLAB.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_SLAB.getDefaultState();
            case ICE:
                return Blocks.PACKED_ICE.getDefaultState();
        }
    }

    protected BlockState getSupportBlock() {
        switch (this.mineshaftType) {
            case NORMAL:
            default:
                return Blocks.OAK_FENCE.getDefaultState();
            case MESA:
                return Blocks.DARK_OAK_FENCE.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_FENCE.getDefaultState();
            case ICE:
                return Blocks.BLUE_ICE.getDefaultState();
        }
    }

    protected BlockState getTrapdoor() {
        switch (this.mineshaftType) {
            case NORMAL:
            default:
                return Blocks.OAK_TRAPDOOR.getDefaultState();
            case MESA:
                return Blocks.DARK_OAK_TRAPDOOR.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_TRAPDOOR.getDefaultState();
            case ICE:
                return Blocks.BIRCH_TRAPDOOR.getDefaultState();
        }
    }

    protected float getVineChance() {
        switch (this.mineshaftType) {
            case JUNGLE:
                return .6f;
            default:
                return .25f;
        }
    }

    protected boolean addBarrel(IWorld world, BlockBox boundingBox, Random random, BlockPos pos, Identifier lootTableId) {
        if (boundingBox.contains(pos) && world.getBlockState(pos).getBlock() != Blocks.BARREL) {
            world.setBlockState(pos, Blocks.BARREL.getDefaultState().with(BarrelBlock.FACING, Direction.UP), 2);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BarrelBlockEntity) {
                ((BarrelBlockEntity) blockEntity).setLootTable(lootTableId, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean addBarrel(IWorld world, BlockBox boundingBox, Random random, int x, int y, int z, Identifier lootTableId) {
        BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
        return this.addBarrel(world, boundingBox, random, blockPos, lootTableId);
    }

    /**
     * Randomly add vines with a given chance in a given area, facing the specified direction.
     */
    protected void addVines(IWorld world, BlockBox boundingBox, Direction facing, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.set(x, y, z);
                    BlockState nextBlock = this.getBlockAt(world, x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ(), boundingBox);
                    if (
                        this.getBlockAt(world, x, y, z, boundingBox).isAir()
                            && Block.isFaceFullSquare(nextBlock.getCollisionShape(world, mutable), facing.getOpposite())
                            && nextBlock.getBlock().getDefaultState() != Blocks.LADDER.getDefaultState()
                    ) {
                        if (random.nextFloat() < chance) {
                            this.addBlock(world, Blocks.VINE.getDefaultState().with(VineBlock.getFacingProperty(facing.getAxis() == Direction.Axis.X ? facing : facing.getOpposite()), true), x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Randomly add vines with a given chance in a given area, doing passes for all four horizontal directions.
     */
    protected void addVines(IWorld world, BlockBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.addVines(world, boundingBox, Direction.EAST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.WEST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.NORTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.SOUTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                       FILL METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Fills up the given bounded area with provided block.
     */
    protected void fill(IWorld world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.addBlock(world, blockState, x, y, z, blockBox);
                }
            }
        }
    }

    /**
     * Fills up the given bounded area with provided inside and edge blocks.
     *
     * @param edgeBlock BlockState for blocks along the edges of the enclosed area
     * @param insideBlock BlockState for blocks inside the enclosed area (not along an edge or corner)
     */
    protected void fillWithOutline(IWorld world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState edgeBlock, BlockState insideBlock, boolean onlyReplaceNonAir) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (!onlyReplaceNonAir || !this.getBlockAt(world, x, y, z, blockBox).isAir()) {
                        if (y != minY && y != maxY && x != minX && x != maxX && z != minZ && z != maxZ) {
                            this.addBlock(world, insideBlock, x, y, z, blockBox);
                        } else {
                            this.addBlock(world, edgeBlock, x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Replaces all air blocks in the given bounded area with provided block.
     */
    protected void replaceAir(IWorld world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                    if (currState != null && currState.isAir()) {
                        this.addBlock(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces all non-air blocks in the given bounded area with provided block.
     */
    protected void replaceNonAir(IWorld world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                    if (currState != null && !currState.isAir()) {
                        this.addBlock(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                   RANDOM FILL METHODS                                   *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Replaces each block with a given chance.
     */
    protected void randomFill(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        this.addBlock(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Fills up the given bounded area with provided inside and edge blocks.
     *
     * @param edgeBlock BlockState for blocks along the edges of the enclosed area
     * @param insideBlock BlockState for blocks inside the enclosed area (not along an edge or corner)
     */
    protected void randomFillWithOutline(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState edgeBlock, BlockState insideBlock) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        if (y != minY && y != maxY && x != minX && x != maxX && z != minZ && z != maxZ) {
                            this.addBlock(world, insideBlock, x, y, z, blockBox);
                        } else {
                            this.addBlock(world, edgeBlock, x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Replaces each air block with a given chance.
     */
    protected void randomReplaceAir(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                        if (currState != null && currState.isAir()) {
                            this.addBlock(world, blockState, x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Replaces all non-air blocks in the given bounded area with provided block.
     */
    protected void randomReplaceNonAir(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                        if (currState != null && !currState.isAir()) {
                            this.addBlock(world, blockState, x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                      BLOCK SET/GET                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected void randomAddBlock(IWorld world, Random random, float chance, BlockState block, int x, int y, int z, BlockBox blockBox) {
        if (random.nextFloat() < chance) {
            this.addBlock(world, block, x, y, z, blockBox);
        }
    }

    /**
     * My "fixed" version of getBlockAt that returns null instead of air if block is out of bounds
     *
     * @return the block at the given position, or null if it is outside of the blockBox
     */
    protected BlockState getBlockAtFixed(BlockView blockView, int x, int y, int z, BlockBox blockBox) {
        int i = this.applyXTransform(x, z);
        int j = this.applyYTransform(y);
        int k = this.applyZTransform(x, z);
        BlockPos blockPos = new BlockPos(i, j, k);
        return !blockBox.contains(blockPos) ? null : blockView.getBlockState(blockPos);
    }

    /**
     * Seems to check for air within a BlockBox at (xMin to xMax, y + 1, z).
     * Note that getBlockAt() also returns air if the coordinate passed in is not within the blockBox passed in.
     * Returns false if any air is found
     */
    protected boolean method_14719(BlockView blockView, BlockBox blockBox, int xMin, int xMax, int y, int z) {
        for (int x = xMin; x <= xMax; ++x) {
            if (this.getBlockAt(blockView, x, y + 1, z, blockBox).isAir()) {
                return false;
            }
        }

        return true;
    }
}
