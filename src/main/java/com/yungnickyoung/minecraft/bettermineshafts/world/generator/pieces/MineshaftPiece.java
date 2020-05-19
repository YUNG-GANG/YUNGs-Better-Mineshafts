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
        }
    }

    protected BlockState getMainSlab() {
        switch (this.mineshaftType) {
            case NORMAL:
            default:
                return Blocks.OAK_SLAB.getDefaultState();
            case MESA:
                return Blocks.DARK_OAK_SLAB.getDefaultState();
        }
    }

    protected BlockState getSupportBlock() {
        switch (this.mineshaftType) {
            case NORMAL:
            default:
                return Blocks.OAK_FENCE.getDefaultState();
            case MESA:
                return Blocks.DARK_OAK_FENCE.getDefaultState();
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
                    if (
                        this.getBlockAt(world, x, y, z, boundingBox).isAir()
                            && Block.isFaceFullSquare(this.getBlockAt(world, x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ(), boundingBox).getCollisionShape(world, mutable), facing.getOpposite())
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

    /**
     * Replaces each block with a given chance.
     */
    protected void randomFillWithOutline(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState, BlockState inside, boolean bl) {
        for (int o = minY; o <= maxY; ++o) {
            for (int p = minX; p <= maxX; ++p) {
                for (int q = minZ; q <= maxZ; ++q) {
                    if (random.nextFloat() < chance) {
                        if (!bl || !this.getBlockAt(world, p, o, q, blockBox).isAir()) {
                            if (o != minY && o != maxY && p != minX && p != maxX && q != minZ && q != maxZ) {
                                this.addBlock(world, inside, p, o, q, blockBox);
                            } else {
                                this.addBlock(world, blockState, p, o, q, blockBox);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void replaceAirInBox(IWorld world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int o = minY; o <= maxY; ++o) {
            for (int p = minX; p <= maxX; ++p) {
                for (int q = minZ; q <= maxZ; ++q) {
                    BlockState currState = this.getBlockAtFixed(world, p, o, q, blockBox);
                    if (currState != null && currState.isAir()) {
                        this.addBlock(world, blockState, p, o, q, blockBox);
                    }
                }
            }
        }
    }

    protected void randomlyReplaceAirInBox(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int o = minY; o <= maxY; ++o) {
            for (int p = minX; p <= maxX; ++p) {
                for (int q = minZ; q <= maxZ; ++q) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, p, o, q, blockBox);
                        if (currState != null && currState.isAir()) {
                            this.addBlock(world, blockState, p, o, q, blockBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Sets the block state, disregarding any blockbox shenanigans
     */
    protected void setBlockState(IWorld world, BlockState block, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
        world.setBlockState(blockPos, block, 2);
        FluidState fluidState = world.getFluidState(blockPos);
        if (!fluidState.isEmpty()) {
            world.getFluidTickScheduler().schedule(blockPos, fluidState.getFluid(), 0);
        }
    }

    /**
     * Returns null instead of air if block is out of bounds
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
