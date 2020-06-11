package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.util.BlockSelector;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import net.minecraft.block.*;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.SlabType;
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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                     BLOCK SELECTORS                                     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected BlockSelector getMainSelector() {
        switch (this.mineshaftType) {
            case MESA:
                return BlockSelector.MESA;
            case JUNGLE:
                return BlockSelector.JUNGLE;
            case SNOW:
                return BlockSelector.SNOW;
            case ICE:
                return BlockSelector.ICE;
            case DESERT:
                return BlockSelector.DESERT;
            case RED_DESERT:
                return BlockSelector.RED_DESERT;
            case MUSHROOM:
                return BlockSelector.MUSHROOM;
            default:
                return BlockSelector.NORMAL;
        }
    }

    protected BlockSelector getBrickSelector() {
        switch (this.mineshaftType) {
            case JUNGLE:
                return BlockSelector.STONE_BRICK_JUNGLE;
            case SNOW:
                return BlockSelector.STONE_BRICK_SNOW;
            case ICE:
                return BlockSelector.STONE_BRICK_ICE;
            case DESERT:
                return BlockSelector.STONE_BRICK_DESERT;
            case RED_DESERT:
                return BlockSelector.STONE_BRICK_RED_DESERT;
            case MUSHROOM:
                return BlockSelector.STONE_BRICK_MUSHROOM;
            default:
                return BlockSelector.STONE_BRICK_NORMAL;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                BIOME VARIANT BLOCK GETTERS                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected BlockState getMainBlock() {
        switch (this.mineshaftType) {
            case MESA:
                return Blocks.DARK_OAK_PLANKS.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_PLANKS.getDefaultState();
            case SNOW:
                return Blocks.SPRUCE_PLANKS.getDefaultState();
            case ICE:
                return Blocks.BLUE_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SANDSTONE.getDefaultState();
            case MUSHROOM:
                return Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
            default:
                return Blocks.OAK_PLANKS.getDefaultState();
        }
    }

    protected BlockState getSupportBlock() {
        switch (this.mineshaftType) {
            case MESA:
                return Blocks.DARK_OAK_FENCE.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_FENCE.getDefaultState();
            case SNOW:
                return Blocks.SPRUCE_FENCE.getDefaultState();
            case ICE:
                return Blocks.BLUE_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE_WALL.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SANDSTONE_WALL.getDefaultState();
            case MUSHROOM:
                return Blocks.MUSHROOM_STEM.getDefaultState();
            default:
                return Blocks.OAK_FENCE.getDefaultState();
        }
    }

    protected BlockState getMainSlab() {
        switch (this.mineshaftType) {
            case MESA:
                return Blocks.DARK_OAK_SLAB.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_SLAB.getDefaultState();
            case SNOW:
                return Blocks.SPRUCE_SLAB.getDefaultState();
            case ICE:
                return Blocks.BLUE_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE_SLAB.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SANDSTONE_SLAB.getDefaultState();
            case MUSHROOM:
                return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState();
            default:
                return Blocks.OAK_SLAB.getDefaultState();
        }
    }

    protected BlockState getBrickBlock() {
        switch (this.mineshaftType) {
            case SNOW:
                return Blocks.SNOW_BLOCK.getDefaultState();
            case ICE:
                return Blocks.BLUE_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SANDSTONE.getDefaultState();
            case MUSHROOM:
                return Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
            default:
                return Blocks.STONE_BRICKS.getDefaultState();
        }
    }

    protected BlockState getGravel() {
        switch (this.mineshaftType) {
            case DESERT:
                return Blocks.SAND.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SAND.getDefaultState();
            case SNOW:
            case ICE:
                return Blocks.SNOW_BLOCK.getDefaultState();
            default:
                return Blocks.GRAVEL.getDefaultState();
        }
    }

    protected BlockState getMainDoorwayWall() {
        switch (this.mineshaftType) {
            case SNOW:
                return Blocks.SNOW_BLOCK.getDefaultState();
            case ICE:
                return Blocks.BLUE_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE_WALL.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SANDSTONE_WALL.getDefaultState();
            case MUSHROOM:
                return Blocks.MUSHROOM_STEM.getDefaultState();
            default:
                return Blocks.STONE_BRICK_WALL.getDefaultState();
        }
    }

    protected BlockState getMainDoorwaySlab() {
        switch (this.mineshaftType) {
            case SNOW:
                return Blocks.SNOW_BLOCK.getDefaultState();
            case ICE:
                return Blocks.BLUE_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP);
            case RED_DESERT:
                return Blocks.RED_SANDSTONE_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP);
            case MUSHROOM:
                return Blocks.MUSHROOM_STEM.getDefaultState();
            default:
                return Blocks.STONE_BRICK_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP);
        }
    }

    protected BlockState getTrapdoor() {
        switch (this.mineshaftType) {
            case MESA:
            case RED_DESERT:
                return Blocks.DARK_OAK_TRAPDOOR.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_TRAPDOOR.getDefaultState();
            case SNOW:
            case ICE:
                return Blocks.SPRUCE_TRAPDOOR.getDefaultState();
            default:
                return Blocks.OAK_TRAPDOOR.getDefaultState();
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

    /**
     * Add decorations specific to a biome variant, such as snow.
     */
    protected void addBiomeDecorations(IWorld world, BlockBox box, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
                    // Snow layers
                    if (mineshaftType == BetterMineshaftFeature.Type.SNOW) {
                        if (this.getBlockAt(world, x, y, z, box) == AIR && Blocks.SNOW.canPlaceAt(AIR, world, blockPos)) {
                            this.addBlock(world, Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, random.nextInt(2) + 1), x, y, z, box);
                        }
                    }
                    // Cacti and dead bushes
                    else if (mineshaftType == BetterMineshaftFeature.Type.DESERT) {
                        float r = random.nextFloat();
                        if (r < .1f) {
                            if (this.getBlockAt(world, x, y, z, box) == AIR && Blocks.CACTUS.canPlaceAt(AIR, world, blockPos)) {
                                this.addBlock(world, Blocks.CACTUS.getDefaultState().with(CactusBlock.AGE, 0), x, y, z, box);
                            }
                        } else if (r < .2f) {
                            Block floor = this.getBlockAt(world, x, y - 1, z, box).getBlock();
                            if (this.getBlockAt(world, x, y, z, box) == AIR && (floor == Blocks.SAND || floor == Blocks.RED_SAND || floor == Blocks.TERRACOTTA || floor == Blocks.WHITE_TERRACOTTA || floor == Blocks.ORANGE_TERRACOTTA || floor == Blocks.YELLOW_TERRACOTTA || floor == Blocks.BROWN_TERRACOTTA || floor == Blocks.DIRT)) {
                                this.addBlock(world, Blocks.DEAD_BUSH.getDefaultState(), x, y, z, box);
                            }
                        }

                    }
                    // Dead bushes
                    else if (mineshaftType == BetterMineshaftFeature.Type.MESA) {
                        if (random.nextFloat() < .1f) {
                            Block floor = this.getBlockAt(world, x, y - 1, z, box).getBlock();
                            if (this.getBlockAt(world, x, y, z, box) == AIR && (floor == Blocks.SAND || floor == Blocks.RED_SAND || floor == Blocks.TERRACOTTA || floor == Blocks.WHITE_TERRACOTTA || floor == Blocks.ORANGE_TERRACOTTA || floor == Blocks.YELLOW_TERRACOTTA || floor == Blocks.BROWN_TERRACOTTA || floor == Blocks.DIRT)) {
                                this.addBlock(world, Blocks.DEAD_BUSH.getDefaultState(), x, y, z, box);
                            }
                        }
                    }
                    // Mushrooms
                    else if (mineshaftType == BetterMineshaftFeature.Type.MUSHROOM) {
                        float r = random.nextFloat();
                        if (r < .2f) {
                            if (this.getBlockAt(world, x, y, z, box) == AIR && Blocks.RED_MUSHROOM.canPlaceAt(AIR, world, blockPos)) {
                                this.addBlock(world, Blocks.RED_MUSHROOM.getDefaultState(), x, y, z, box);
                            }
                        } else if (r < .4f) {
                            if (this.getBlockAt(world, x, y, z, box) == AIR && Blocks.BROWN_MUSHROOM.canPlaceAt(AIR, world, blockPos)) {
                                this.addBlock(world, Blocks.BROWN_MUSHROOM.getDefaultState(), x, y, z, box);
                            }
                        }
                    }
                }
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                       FILL METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Replaces each block in the provided area with the provided BlockState.
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
     * Replaces each block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void fill(IWorld world, BlockBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.addBlock(world, selector.get(random), x, y, z, blockBox);
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with the provided BlockState.
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
     * Replaces each air block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void replaceAir(IWorld world, BlockBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                    if (currState != null && currState.isAir()) {
                        this.addBlock(world, selector.get(random), x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each non-air block in the provided area with the provided BlockState.
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

    /**
     * Replaces each non-air block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void replaceNonAir(IWorld world, BlockBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                    if (currState != null && !currState.isAir()) {
                        this.addBlock(world, selector.get(random), x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                   RANDOM FILL METHODS                                   *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Has a chance of replacing each block in the provided area with the provided BlockState.
     */
    protected void chanceFill(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
     * Has a chance of replacing each block in the provided area with a block determined by the provided BlockSelector.
     */
    protected void chanceFill(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        this.addBlock(world, selector.get(random), x, y, z, blockBox);
                    }
                }
            }
        }    }

    /**
     * Has a chance of replacing each air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceAir(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
     * Has a chance of replacing each air block in the provided area with a block determined by the provided BlockSelector.
     */
    protected void chanceReplaceAir(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                        if (currState != null && currState.isAir()) {
                            this.addBlock(world, selector.get(random), x, y, z, blockBox);
                        }
                    }
                }
            }
        }    }

    /**
     * Has a chance of replacing each non-air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceNonAir(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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

    /**
     * Has a chance of replacing each non-air block in the provided area with a block determined by the provided BlockSelector.
     */
    protected void chanceReplaceNonAir(IWorld world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, blockBox);
                        if (currState != null && !currState.isAir()) {
                            this.addBlock(world, selector.get(random), x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                      BLOCK SET/GET                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected void chanceAddBlock(IWorld world, Random random, float chance, BlockState block, int x, int y, int z, BlockBox blockBox) {
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
