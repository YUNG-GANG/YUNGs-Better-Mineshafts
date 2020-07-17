package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.ImmutableSet;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.util.BlockSetSelector;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class MineshaftPiece extends StructurePiece {
    public BetterMineshaftStructure.Type mineshaftType;
    protected int pieceChainLen;

    private static final Set<Material> LIQUIDS = ImmutableSet.of(Material.LAVA, Material.WATER);

    public MineshaftPiece(IStructurePieceType structurePieceType, int i, int pieceChainLen, BetterMineshaftStructure.Type type) {
        super(structurePieceType, i);
        this.mineshaftType = type;
        this.pieceChainLen = pieceChainLen;
    }

    public MineshaftPiece(IStructurePieceType structurePieceType, CompoundNBT compoundTag) {
        super(structurePieceType, compoundTag);
        this.mineshaftType = BetterMineshaftStructure.Type.byId(compoundTag.getInt("MST"));
    }

    protected void toNbt(CompoundNBT tag) {
        tag.putInt("MST", this.mineshaftType.ordinal());
    }

    public void setBoundingBox(MutableBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Adds new pieces to the list passed in.
     * Also resolves variables local to this piece like support positions.
     * Does not actually place any blocks.
     */
    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                     BLOCK SELECTORS                                     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected BlockSetSelector getMainSelector() {
        switch (this.mineshaftType) {
            case MESA:
                return BlockSetSelector.MESA;
            case JUNGLE:
                return BlockSetSelector.JUNGLE;
            case SNOW:
                return BlockSetSelector.SNOW;
            case ICE:
                return BlockSetSelector.ICE;
            case DESERT:
                return BlockSetSelector.DESERT;
            case RED_DESERT:
                return BlockSetSelector.RED_DESERT;
            case MUSHROOM:
                return BlockSetSelector.MUSHROOM;
            case SAVANNA:
                return BlockSetSelector.ACACIA;
            default:
                return BlockSetSelector.NORMAL;
        }
    }

    protected BlockSetSelector getBrickSelector() {
        switch (this.mineshaftType) {
            case JUNGLE:
                return BlockSetSelector.STONE_BRICK_JUNGLE;
            case SNOW:
                return BlockSetSelector.STONE_BRICK_SNOW;
            case ICE:
                return BlockSetSelector.STONE_BRICK_ICE;
            case DESERT:
                return BlockSetSelector.STONE_BRICK_DESERT;
            case RED_DESERT:
                return BlockSetSelector.STONE_BRICK_RED_DESERT;
            case MUSHROOM:
                return BlockSetSelector.STONE_BRICK_MUSHROOM;
            default:
                return BlockSetSelector.STONE_BRICK_NORMAL;
        }
    }

    protected BlockSetSelector getLegSelector() {
        switch (this.mineshaftType) {
            case MESA:
                return BlockSetSelector.from(Blocks.STRIPPED_DARK_OAK_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            case JUNGLE:
                return BlockSetSelector.from(Blocks.STRIPPED_JUNGLE_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            case SNOW:
                return BlockSetSelector.from(Blocks.STRIPPED_SPRUCE_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            case ICE:
                return BlockSetSelector.STONE_BRICK_ICE;
            case DESERT:
                return BlockSetSelector.STONE_BRICK_DESERT;
            case RED_DESERT:
                return BlockSetSelector.STONE_BRICK_RED_DESERT;
            case MUSHROOM:
                return BlockSetSelector.STONE_BRICK_MUSHROOM;
            case SAVANNA:
                return BlockSetSelector.from(Blocks.STRIPPED_ACACIA_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
            default:
                return BlockSetSelector.from(Blocks.STRIPPED_OAK_LOG.getDefaultState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y));
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
            case SAVANNA:
                return Blocks.ACACIA_PLANKS.getDefaultState();
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
            case SAVANNA:
                return Blocks.ACACIA_FENCE.getDefaultState();
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
            case SAVANNA:
                return Blocks.ACACIA_SLAB.getDefaultState();
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
            case SAVANNA:
                return Blocks.ACACIA_TRAPDOOR.getDefaultState();
            default:
                return Blocks.OAK_TRAPDOOR.getDefaultState();
        }
    }

    protected float getVineChance() {
        switch (this.mineshaftType) {
            case JUNGLE:
                return (float) BMConfig.vineFreqJungle;
            default:
                return (float) BMConfig.vineFreq;
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  GENERATION UTIL METHODS                                *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected boolean addBarrel(IWorld world, MutableBoundingBox boundingBox, Random random, BlockPos pos, ResourceLocation lootTableId) {
        if (boundingBox.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.BARREL) {
            world.setBlockState(pos, Blocks.BARREL.getDefaultState().with(BarrelBlock.PROPERTY_FACING, Direction.UP), 2);
            TileEntity blockEntity = world.getTileEntity(pos);
            if (blockEntity instanceof BarrelTileEntity) {
                ((BarrelTileEntity) blockEntity).setLootTable(lootTableId, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean addBarrel(IWorld world, MutableBoundingBox boundingBox, Random random, int x, int y, int z, ResourceLocation lootTableId) {
        BlockPos blockPos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        return this.addBarrel(world, boundingBox, random, blockPos, lootTableId);
    }

    /**
     * Randomly add vines with a given chance in a given area, facing the specified direction.
     */
    protected void addVines(IWorld world, MutableBoundingBox boundingBox, Direction facing, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.setPos(x, y, z);
                    BlockState nextBlock = this.getBlockStateFromPos(world, x + facing.getXOffset(), y + facing.getYOffset(), z + facing.getZOffset(), boundingBox);
                    if (
                        this.getBlockStateFromPos(world, x, y, z, boundingBox).isAir()
                            && Block.doesSideFillSquare(nextBlock.getCollisionShape(world, mutable), facing.getOpposite())
                            && nextBlock.getBlock().getDefaultState() != Blocks.LADDER.getDefaultState()
                    ) {
                        if (random.nextFloat() < chance) {
                            this.setBlockState(world, Blocks.VINE.getDefaultState().with(VineBlock.getPropertyFor(facing.getAxis() == Direction.Axis.X ? facing : facing.getOpposite()), true), x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Randomly add vines with a given chance in a given area, doing passes for all four horizontal directions.
     */
    protected void addVines(IWorld world, MutableBoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.addVines(world, boundingBox, Direction.EAST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.WEST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.NORTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.SOUTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Add decorations specific to a biome variant, such as snow.
     */
    protected void addBiomeDecorations(IWorld world, MutableBoundingBox box, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos blockPos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
                    // Snow layers
                    if (mineshaftType == BetterMineshaftStructure.Type.SNOW) {
                        if (this.getBlockStateFromPos(world, x, y, z, box) == CAVE_AIR && Blocks.SNOW.isValidPosition(CAVE_AIR, world, blockPos)) {
                            this.setBlockState(world, Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, random.nextInt(2) + 1), x, y, z, box);
                        }
                    }
                    // Cacti and dead bushes
                    else if (mineshaftType == BetterMineshaftStructure.Type.DESERT || mineshaftType == BetterMineshaftStructure.Type.RED_DESERT) {
                        float r = random.nextFloat();
                        if (r < .1f) {
                            if (this.getBlockStateFromPos(world, x, y, z, box) == CAVE_AIR && Blocks.CACTUS.isValidPosition(CAVE_AIR, world, blockPos)) {
                                this.setBlockState(world, Blocks.CACTUS.getDefaultState().with(CactusBlock.AGE, 0), x, y, z, box);
                            }
                        } else if (r < .2f) {
                            Block floor = this.getBlockStateFromPos(world, x, y - 1, z, box).getBlock();
                            if (this.getBlockStateFromPos(world, x, y, z, box) == CAVE_AIR && (floor == Blocks.SAND || floor == Blocks.RED_SAND || floor == Blocks.TERRACOTTA || floor == Blocks.WHITE_TERRACOTTA || floor == Blocks.ORANGE_TERRACOTTA || floor == Blocks.YELLOW_TERRACOTTA || floor == Blocks.BROWN_TERRACOTTA || floor == Blocks.DIRT)) {
                                this.setBlockState(world, Blocks.DEAD_BUSH.getDefaultState(), x, y, z, box);
                            }
                        }

                    }
                    // Dead bushes
                    else if (mineshaftType == BetterMineshaftStructure.Type.MESA) {
                        if (random.nextFloat() < .1f) {
                            Block floor = this.getBlockStateFromPos(world, x, y - 1, z, box).getBlock();
                            if (this.getBlockStateFromPos(world, x, y, z, box) == CAVE_AIR && (floor == Blocks.SAND || floor == Blocks.RED_SAND || floor == Blocks.TERRACOTTA || floor == Blocks.WHITE_TERRACOTTA || floor == Blocks.ORANGE_TERRACOTTA || floor == Blocks.YELLOW_TERRACOTTA || floor == Blocks.BROWN_TERRACOTTA || floor == Blocks.DIRT)) {
                                this.setBlockState(world, Blocks.DEAD_BUSH.getDefaultState(), x, y, z, box);
                            }
                        }
                    }
                    // Mushrooms
                    else if (mineshaftType == BetterMineshaftStructure.Type.MUSHROOM) {
                        float r = random.nextFloat();
                        if (r < .2f) {
                            if (this.getBlockStateFromPos(world, x, y, z, box) == CAVE_AIR && Blocks.RED_MUSHROOM.isValidPosition(CAVE_AIR, world, blockPos)) {
                                this.setBlockState(world, Blocks.RED_MUSHROOM.getDefaultState(), x, y, z, box);
                            }
                        } else if (r < .4f) {
                            if (this.getBlockStateFromPos(world, x, y, z, box) == CAVE_AIR && Blocks.BROWN_MUSHROOM.isValidPosition(CAVE_AIR, world, blockPos)) {
                                this.setBlockState(world, Blocks.BROWN_MUSHROOM.getDefaultState(), x, y, z, box);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void generateLeg(IWorld world, Random random, int x, int z, BlockSetSelector selector) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(this.getXWithOffset(x, z), this.getYWithOffset(-1), this.getZWithOffset(x, z));

        while (mutable.getY() > 0 && (world.getBlockState(mutable) == CAVE_AIR || LIQUIDS.contains(world.getBlockState(mutable).getMaterial()))) {
            world.setBlockState(mutable, selector.get(random), 2);
            mutable.move(Direction.DOWN);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                       FILL METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Replaces each block in the provided area with the provided BlockState.
     */
    protected void fill(IWorld world, MutableBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.setBlockState(world, blockState, x, y, z, blockBox);
                }
            }
        }
    }

    /**
     * Replaces each block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void fill(IWorld world, MutableBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.setBlockState(world, selector.get(random), x, y, z, blockBox);
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with the provided BlockState.
     */
    protected void replaceAir(IWorld world, MutableBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && currState.isAir()) {
                        this.setBlockState(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void replaceAir(IWorld world, MutableBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && currState.isAir()) {
                        this.setBlockState(world, selector.get(random), x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each non-air block in the provided area with the provided BlockState.
     */
    protected void replaceNonAir(IWorld world, MutableBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && !currState.isAir()) {
                        this.setBlockState(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each non-air block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void replaceNonAir(IWorld world, MutableBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && !currState.isAir()) {
                        this.setBlockState(world, selector.get(random), x, y, z, blockBox);
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
    protected void chanceFill(IWorld world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        this.setBlockState(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each block in the provided area with a block determined by the provided BlockSelector.
     */
    protected void chanceFill(IWorld world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        this.setBlockState(world, selector.get(random), x, y, z, blockBox);
                    }
                }
            }
        }    }

    /**
     * Has a chance of replacing each air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceAir(IWorld world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && currState.isAir()) {
                            this.setBlockState(world, blockState, x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each air block in the provided area with a block determined by the provided BlockSelector.
     */
    protected void chanceReplaceAir(IWorld world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && currState.isAir()) {
                            this.setBlockState(world, selector.get(random), x, y, z, blockBox);
                        }
                    }
                }
            }
        }    }

    /**
     * Has a chance of replacing each non-air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceNonAir(IWorld world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && !currState.isAir()) {
                            this.setBlockState(world, blockState, x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each non-air block in the provided area with a block determined by the provided BlockSelector.
     */
    protected void chanceReplaceNonAir(IWorld world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && !currState.isAir()) {
                            this.setBlockState(world, selector.get(random), x, y, z, blockBox);
                        }
                    }
                }
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                      BLOCK SET/GET                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected void chanceAddBlock(IWorld world, Random random, float chance, BlockState block, int x, int y, int z, MutableBoundingBox blockBox) {
        if (random.nextFloat() < chance) {
            this.setBlockState(world, block, x, y, z, blockBox);
        }
    }

    /**
     * My "fixed" version of getBlockAt that returns null instead of air if block is out of bounds
     *
     * @return the block at the given position, or null if it is outside of the blockBox
     */
    protected BlockState getBlockStateFromPosFixed(IBlockReader blockView, int x, int y, int z, MutableBoundingBox blockBox) {
        int i = this.getXWithOffset(x, z);
        int j = this.getYWithOffset(y);
        int k = this.getZWithOffset(x, z);
        BlockPos blockPos = new BlockPos(i, j, k);
        return !blockBox.isVecInside(blockPos) ? null : blockView.getBlockState(blockPos);
    }

    /**
     * Seems to check for air within a MutableBoundingBox at (xMin to xMax, y + 1, z).
     * Note that getBlockAt() also returns air if the coordinate passed in is not within the blockBox passed in.
     * Returns false if any air is found
     */
    protected boolean method_14719(IBlockReader blockView, MutableBoundingBox blockBox, int xMin, int xMax, int y, int z) {
        for (int x = xMin; x <= xMax; ++x) {
            if (this.getBlockStateFromPos(blockView, x, y + 1, z, blockBox).isAir()) {
                return false;
            }
        }

        return true;
    }
}
