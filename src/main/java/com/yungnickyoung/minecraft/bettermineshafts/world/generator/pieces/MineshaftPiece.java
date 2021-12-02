package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.ImmutableSet;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BlockSetSelectors;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.Material;

import java.util.Random;
import java.util.Set;

public abstract class MineshaftPiece extends StructurePiece {
    public BetterMineshaftStructureFeature.Type mineshaftType;
    protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final Set<Material> LIQUIDS = ImmutableSet.of(Material.LAVA, Material.WATER);

    public MineshaftPiece(StructurePieceType structurePieceType, int chainLength, BetterMineshaftStructureFeature.Type type, BoundingBox boundingBox) {
        super(structurePieceType, chainLength, boundingBox);
        this.mineshaftType = type;
    }

    public MineshaftPiece(StructurePieceType structurePieceType, CompoundTag compoundTag) {
        super(structurePieceType, compoundTag);
        this.mineshaftType = BetterMineshaftStructureFeature.Type.byId(compoundTag.getInt("MST"));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        compoundTag.putInt("MST", this.mineshaftType.ordinal());
    }

    /**
     * Adds new pieces to the list passed in.
     * Also resolves variables local to this piece like support positions.
     * Does not actually place any blocks.
     */
    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random) {
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                     BLOCK SELECTORS                                     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected BlockSetSelector getMainSelector() {
        return BlockSetSelectors.MAIN_SELECTOR.get(this.mineshaftType);
    }

    protected BlockSetSelector getFloorSelector() {
        return BlockSetSelectors.FLOOR_SELECTOR.get(this.mineshaftType);
    }

    protected BlockSetSelector getBrickSelector() {
        return BlockSetSelectors.BRICK_SELECTOR.get(this.mineshaftType);
    }

    protected BlockSetSelector getLegSelector() {
        return BlockSetSelectors.LEG_SELECTOR.get(this.mineshaftType);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                BIOME VARIANT BLOCK GETTERS                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected BlockState getMainBlock() {
        return BlockSetSelectors.MAIN_BLOCK.get(this.mineshaftType);
    }

    protected BlockState getSupportBlock() {
        return BlockSetSelectors.SUPPORT_BLOCK.get(this.mineshaftType);
    }

    protected BlockState getMainSlab() {
        return BlockSetSelectors.SLAB_BLOCK.get(this.mineshaftType);
    }

    protected BlockState getGravel() {
        return BlockSetSelectors.GRAVEL_BLOCK.get(this.mineshaftType);
    }

    protected BlockState getMainDoorwayWall() {
        return BlockSetSelectors.STONE_WALL_BLOCK.get(this.mineshaftType);
    }

    protected BlockState getMainDoorwaySlab() {
        return BlockSetSelectors.STONE_SLAB_BLOCK.get(this.mineshaftType);
    }

    protected BlockState getTrapdoor() {
        return switch (this.mineshaftType) {
            case MESA, RED_DESERT -> Blocks.DARK_OAK_TRAPDOOR.defaultBlockState();
            case JUNGLE -> Blocks.JUNGLE_TRAPDOOR.defaultBlockState();
            case SNOW, ICE -> Blocks.SPRUCE_TRAPDOOR.defaultBlockState();
            case SAVANNA -> Blocks.ACACIA_TRAPDOOR.defaultBlockState();
            default -> Blocks.OAK_TRAPDOOR.defaultBlockState();
        };
    }

    protected float getVineChance() {
        return switch (this.mineshaftType) {
            case DESERT, RED_DESERT -> .1f;
            case JUNGLE -> .6f;
            case ICE, SNOW -> .05f;
            default -> .25f;
        };
    }

    protected float getReplacementRate() {
        return
            this.mineshaftType == BetterMineshaftStructureFeature.Type.SNOW
                || this.mineshaftType == BetterMineshaftStructureFeature.Type.ICE
                || this.mineshaftType == BetterMineshaftStructureFeature.Type.MUSHROOM
                ? .95f
                : .6f;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  GENERATION UTIL METHODS                                *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected boolean addBarrel(WorldGenLevel world, BoundingBox boundingBox, Random random, BlockPos pos, ResourceLocation lootTableId) {
        if (boundingBox.isInside(pos) && world.getBlockState(pos).getBlock() != Blocks.BARREL) {
            world.setBlock(pos, Blocks.BARREL.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP), 2);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BarrelBlockEntity) {
                ((BarrelBlockEntity) blockEntity).setLootTable(lootTableId, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean addBarrel(WorldGenLevel world, BoundingBox boundingBox, Random random, int x, int y, int z, ResourceLocation lootTableId) {
        return this.addBarrel(world, boundingBox, random, this.getWorldPos(x, y, z), lootTableId);
    }

    /**
     * Randomly add vines with a given chance in a given area, facing the specified direction.
     */
    protected void addVines(WorldGenLevel world, BoundingBox boundingBox, Direction facing, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.set(this.getWorldX(x, z), this.getWorldY(y), this.getWorldZ(x, z)).move(facing);
                    BlockState nextBlock = this.getBlock(world, x + facing.getStepX(), y + facing.getStepY(), z + facing.getStepZ(), boundingBox);
                    if (
                        this.getBlock(world, x, y, z, boundingBox).isAir()
                            && Block.isFaceFull(nextBlock.getCollisionShape(world, mutable), facing.getOpposite())
                            && nextBlock.getBlock().defaultBlockState() != Blocks.LADDER.defaultBlockState()
                    ) {
                        if (random.nextFloat() < chance) {
                            this.placeBlock(world, Blocks.VINE.defaultBlockState().setValue(VineBlock.getPropertyForFace(facing.getAxis() == Direction.Axis.X ? facing : facing.getOpposite()), true), x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Randomly add vines with a given chance in a given area, doing passes for all four horizontal directions.
     */
    protected void addVines(WorldGenLevel world, BoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        float chance = getVineChance();
        this.addVines(world, boundingBox, Direction.EAST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.WEST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.NORTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.SOUTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Add decorations specific to a biome variant, such as snow.
     */
    protected void addBiomeDecorations(WorldGenLevel world, BoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos blockPos = getWorldPos(x, y, z);
                    BlockState state = this.getBlock(world, x, y, z, boundingBox);
                    BlockState stateBelow = this.getBlock(world, x, y - 1, z, boundingBox);
                    Block blockBelow = stateBelow.getBlock();

                    // Snow layers
                    if (mineshaftType == BetterMineshaftStructureFeature.Type.SNOW) {
                        if (state.isAir() && Blocks.SNOW.canSurvive(AIR, world, blockPos)) {
                            this.placeBlock(world, Blocks.SNOW.defaultBlockState().setValue(BlockStateProperties.LAYERS, random.nextInt(2) + 1), x, y, z, boundingBox);
                        }
                    }

                    // Cacti
                    if (mineshaftType == BetterMineshaftStructureFeature.Type.DESERT || mineshaftType == BetterMineshaftStructureFeature.Type.RED_DESERT) {
                        if (random.nextFloat() < .1f) {
                            if (state.isAir() && Blocks.CACTUS.canSurvive(AIR, world, blockPos)) {
                                this.placeBlock(world, Blocks.CACTUS.defaultBlockState().setValue(BlockStateProperties.AGE_15, 0), x, y, z, boundingBox);
                                if (random.nextFloat() < .5f && this.getBlock(world, x, y + 1, z, boundingBox).is(Blocks.AIR)) {
                                    this.placeBlock(world, Blocks.CACTUS.defaultBlockState().setValue(BlockStateProperties.AGE_15, 0), x, y + 1, z, boundingBox);
                                }
                            }
                        }
                    }

                    // Dead bushes
                    if (mineshaftType == BetterMineshaftStructureFeature.Type.MESA || mineshaftType == BetterMineshaftStructureFeature.Type.DESERT || mineshaftType == BetterMineshaftStructureFeature.Type.RED_DESERT) {
                        if (random.nextFloat() < .1f) {
                            if (state.isAir() && (blockBelow == Blocks.SAND || blockBelow == Blocks.RED_SAND || blockBelow == Blocks.TERRACOTTA || blockBelow == Blocks.WHITE_TERRACOTTA || blockBelow == Blocks.ORANGE_TERRACOTTA || blockBelow == Blocks.YELLOW_TERRACOTTA || blockBelow == Blocks.BROWN_TERRACOTTA || blockBelow == Blocks.DIRT)) {
                                this.placeBlock(world, Blocks.DEAD_BUSH.defaultBlockState(), x, y, z, boundingBox);
                            }
                        }
                    }

                    // Mushrooms
                    if (mineshaftType == BetterMineshaftStructureFeature.Type.MUSHROOM) {
                        if (state.isAir() && Blocks.RED_MUSHROOM.canSurvive(AIR, world, blockPos)) {
                            float r = random.nextFloat();
                            if (r < .2f) {
                                this.placeBlock(world, Blocks.RED_MUSHROOM.defaultBlockState(), x, y, z, boundingBox);
                            } else if (r < .4f) {
                                this.placeBlock(world, Blocks.BROWN_MUSHROOM.defaultBlockState(), x, y, z, boundingBox);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void generateLeg(WorldGenLevel world, Random random, BoundingBox box, int x, int z, BlockSetSelector selector) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, -1, z);
        BlockState state = this.getBlock(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);

        while (getWorldY(mutable.getY()) > 0 && (state.isAir() || LIQUIDS.contains(state.getMaterial()))) {
            this.placeBlock(world, selector.get(random), x, mutable.getY(), z, box);
            mutable.move(Direction.DOWN);
            state = this.getBlock(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                       FILL METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Replaces each block in the provided area with the provided BlockState.
     */
    protected void fill(WorldGenLevel world, BoundingBox boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.placeBlock(world, blockState, x, y, z, boundingBox);
                }
            }
        }
    }

    /**
     * Replaces each block in the provided area with blocks determined by the provided BlockSetSelector.
     */
    protected void fill(WorldGenLevel world, BoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with the provided BlockState.
     */
    protected void replaceAir(WorldGenLevel world, BoundingBox boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && currState.isAir()) {
                        this.placeBlock(world, blockState, x, y, z, boundingBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with blocks determined by the provided BlockSetSelector.
     */
    protected void replaceAir(WorldGenLevel world, BoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && currState.isAir()) {
                        this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each non-air block in the provided area with the provided BlockState.
     */
    protected void replaceNonAir(WorldGenLevel world, BoundingBox boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && !currState.isAir()) {
                        this.placeBlock(world, blockState, x, y, z, boundingBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each non-air block in the provided area with blocks determined by the provided BlockSetSelector.
     */
    protected void replaceNonAir(WorldGenLevel world, BoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && !currState.isAir()) {
                        this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
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
    protected void chanceFill(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        this.placeBlock(world, blockState, x, y, z, boundingBox);
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each block in the provided area with a block determined by the provided BlockSetSelector.
     */
    protected void chanceFill(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceAir(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                        if (currState != null && currState.isAir()) {
                            this.placeBlock(world, blockState, x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each air block in the provided area with a block determined by the provided BlockSetSelector.
     */
    protected void chanceReplaceAir(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                        if (currState != null && currState.isAir()) {
                            this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each non-air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceNonAir(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                        if (currState != null && !currState.isAir()) {
                            this.placeBlock(world, blockState, x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each non-air block in the provided area with a block determined by the provided BlockSetSelector.
     */
    protected void chanceReplaceNonAir(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                        if (currState != null && !currState.isAir()) {
                            this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                      BLOCK SET/GET                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected void chanceAddBlock(WorldGenLevel world, Random random, float chance, BlockState block, int x, int y, int z, BoundingBox boundingBox) {
        if (random.nextFloat() < chance) {
            this.placeBlock(world, block, x, y, z, boundingBox);
        }
    }

    /**
     * My "fixed" version of getBlock that returns null instead of air if block is out of bounds
     *
     * @return the block at the given position, or null if it is outside of the BoundingBox
     */
    protected BlockState getBlockAtFixed(BlockGetter blockGetter, int x, int y, int z, BoundingBox boundingBox) {
        int i = this.getWorldX(x, z);
        int j = this.getWorldY(y);
        int k = this.getWorldZ(x, z);
        BlockPos blockPos = new BlockPos(i, j, k);
        return !boundingBox.isInside(blockPos) ? null : blockGetter.getBlockState(blockPos);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  PLACEMENT METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Checks if there is liquid within a given box
     **/
    protected boolean isTouchingLiquid(BlockGetter blockGetter, BoundingBox box) {
        int minX = Math.max(this.boundingBox.minX() - 1, box.minX());
        int minY = Math.max(this.boundingBox.minY() - 1, box.minY());
        int minZ = Math.max(this.boundingBox.minZ() - 1, box.minZ());
        int maxX = Math.min(this.boundingBox.maxX() + 1, box.maxX());
        int maxY = Math.min(this.boundingBox.maxY() + 1, box.maxY());
        int maxZ = Math.min(this.boundingBox.maxZ() + 1, box.maxZ());
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                if (blockGetter.getBlockState(mutable.set(x, minY, z)).getMaterial().isLiquid()) {
                    return true;
                }

                if (blockGetter.getBlockState(mutable.set(x, maxY, z)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }

        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                if (blockGetter.getBlockState(mutable.set(x, y, minZ)).getMaterial().isLiquid()) {
                    return true;
                }

                if (blockGetter.getBlockState(mutable.set(x, y, maxZ)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }

        for (int z = minZ; z <= maxZ; ++z) {
            for (int y = minY; y <= maxY; ++y) {
                if (blockGetter.getBlockState(mutable.set(minX, y, z)).getMaterial().isLiquid()) {
                    return true;
                }

                if (blockGetter.getBlockState(mutable.set(maxX, y, z)).getMaterial().isLiquid()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean isInOcean(WorldGenLevel world, int localX, int localZ) {
        BlockPos pos = new BlockPos.MutableBlockPos(getWorldX(localX, localZ), 1, getWorldZ(localX, localZ));
        return world.getBiome(pos).getBiomeCategory() == Biome.BiomeCategory.OCEAN;
    }
}
