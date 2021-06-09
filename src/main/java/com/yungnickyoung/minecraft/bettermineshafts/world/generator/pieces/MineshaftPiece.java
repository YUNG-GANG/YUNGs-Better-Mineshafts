package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.ImmutableSet;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BlockSetSelectors;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.*;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class MineshaftPiece extends StructurePiece {
    public BetterMineshaftStructure.Type mineshaftType;

    private static final Set<Material> LIQUIDS = ImmutableSet.of(Material.LAVA, Material.WATER);

    public MineshaftPiece(StructurePieceType structurePieceType, int chainLength, BetterMineshaftStructure.Type type) {
        super(structurePieceType, chainLength);
        this.mineshaftType = type;
    }

    public MineshaftPiece(StructurePieceType structurePieceType, CompoundTag compoundTag) {
        super(structurePieceType, compoundTag);
        this.mineshaftType = BetterMineshaftStructure.Type.byIndex(compoundTag.getInt("MST"));
    }

    protected void toNbt(CompoundTag tag) {
        tag.putInt("MST", this.mineshaftType.ordinal());
    }

    public void setBoundingBox(BlockBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * Adds new pieces to the list passed in.
     * Also resolves variables local to this piece like support positions.
     * Does not actually place any blocks.
     */
    @Override
    public void fillOpenings(StructurePiece structurePiece, List<StructurePiece> list, Random random) {}

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
            case DESERT:
            case RED_DESERT:
                return .1f;
            case JUNGLE:
                return .6f;
            case ICE:
            case SNOW:
                return .05f;
            default:
                return .25f;
        }
    }

    protected float getReplacementRate() {
        return
            this.mineshaftType == BetterMineshaftStructure.Type.SNOW
                || this.mineshaftType == BetterMineshaftStructure.Type.ICE
                || this.mineshaftType == BetterMineshaftStructure.Type.MUSHROOM
                ? .95f
                : .6f;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  GENERATION UTIL METHODS                                *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected boolean addBarrel(StructureWorldAccess world, BlockBox boundingBox, Random random, BlockPos pos, Identifier lootTableId) {
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

    protected boolean addBarrel(StructureWorldAccess world, BlockBox boundingBox, Random random, int x, int y, int z, Identifier lootTableId) {
        BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
        return this.addBarrel(world, boundingBox, random, blockPos, lootTableId);
    }

    /**
     * Randomly add vines with a given chance in a given area, facing the specified direction.
     */
    protected void addVines(StructureWorldAccess world, BlockBox boundingBox, Direction facing, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.set(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z)).move(facing);
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
    protected void addVines(StructureWorldAccess world, BlockBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        float chance = getVineChance();
        this.addVines(world, boundingBox, Direction.EAST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.WEST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.NORTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.SOUTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Add decorations specific to a biome variant, such as snow.
     */
    protected void addBiomeDecorations(StructureWorldAccess world, BlockBox box, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos blockPos = new BlockPos(this.applyXTransform(x, z), this.applyYTransform(y), this.applyZTransform(x, z));
                    BlockState state = this.getBlockAt(world, x, y, z, box);
                    BlockState stateBelow = this.getBlockAt(world, x, y - 1, z, box);
                    Block blockBelow = stateBelow.getBlock();

                    // Snow layers
                    if (mineshaftType == BetterMineshaftStructure.Type.SNOW) {
                        if (state == AIR && Blocks.SNOW.canPlaceAt(AIR, world, blockPos)) {
                            this.addBlock(world, Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, random.nextInt(2) + 1), x, y, z, box);
                        }
                    }

                    // Cacti
                    if (mineshaftType == BetterMineshaftStructure.Type.DESERT || mineshaftType == BetterMineshaftStructure.Type.RED_DESERT) {
                        if (random.nextFloat() < .1f) {
                            if (state == AIR && Blocks.CACTUS.canPlaceAt(AIR, world, blockPos)) {
                                this.addBlock(world, Blocks.CACTUS.getDefaultState().with(CactusBlock.AGE, 0), x, y, z, box);
                                if (random.nextFloat() < .5f && this.getBlockAt(world, x, y + 1, z, box) == AIR) {
                                    this.addBlock(world, Blocks.CACTUS.getDefaultState().with(CactusBlock.AGE, 0), x, y + 1, z, box);
                                }
                            }
                        }
                    }

                    // Dead bushes
                    if (mineshaftType == BetterMineshaftStructure.Type.MESA || mineshaftType == BetterMineshaftStructure.Type.DESERT || mineshaftType == BetterMineshaftStructure.Type.RED_DESERT) {
                        if (random.nextFloat() < .1f) {
                            if (state == AIR && (blockBelow == Blocks.SAND || blockBelow == Blocks.RED_SAND || blockBelow == Blocks.TERRACOTTA || blockBelow == Blocks.WHITE_TERRACOTTA || blockBelow == Blocks.ORANGE_TERRACOTTA || blockBelow == Blocks.YELLOW_TERRACOTTA || blockBelow == Blocks.BROWN_TERRACOTTA || blockBelow == Blocks.DIRT)) {
                                this.addBlock(world, Blocks.DEAD_BUSH.getDefaultState(), x, y, z, box);
                            }
                        }
                    }

                    // Mushrooms
                    if (mineshaftType == BetterMineshaftStructure.Type.MUSHROOM) {
                        if (state == AIR && (blockBelow == Blocks.MYCELIUM || blockBelow == Blocks.DIRT)) {
                            float r = random.nextFloat();
                            if (r < .2f) {
                                this.addBlock(world, Blocks.RED_MUSHROOM.getDefaultState(), x, y, z, box);
                            } else if (r < .4f) {
                                this.addBlock(world, Blocks.BROWN_MUSHROOM.getDefaultState(), x, y, z, box);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void generateLeg(StructureWorldAccess world, Random random, BlockBox box, int x, int z, BlockSetSelector selector) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(x, -1, z);
        BlockState state = this.getBlockAt(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);

        while (applyYTransform(mutable.getY()) > 0 && (state == AIR || state == Blocks.AIR.getDefaultState() || LIQUIDS.contains(state.getMaterial()))) {
            this.addBlock(world,selector.get(random), x, mutable.getY(), z, box);
            mutable.move(Direction.DOWN);
            state = this.getBlockAt(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                       FILL METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Replaces each block in the provided area with the provided BlockState.
     */
    protected void fill(StructureWorldAccess world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    this.addBlock(world, blockState, x, y, z, blockBox);
                }
            }
        }
    }

    /**
     * Replaces each block in the provided area with blocks determined by the provided BlockSetSelector.
     */
    protected void fill(StructureWorldAccess world, BlockBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void replaceAir(StructureWorldAccess world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
     * Replaces each air block in the provided area with blocks determined by the provided BlockSetSelector.
     */
    protected void replaceAir(StructureWorldAccess world, BlockBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void replaceNonAir(StructureWorldAccess world, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
     * Replaces each non-air block in the provided area with blocks determined by the provided BlockSetSelector.
     */
    protected void replaceNonAir(StructureWorldAccess world, BlockBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void chanceFill(StructureWorldAccess world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
     * Has a chance of replacing each block in the provided area with a block determined by the provided BlockSetSelector.
     */
    protected void chanceFill(StructureWorldAccess world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void chanceReplaceAir(StructureWorldAccess world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
     * Has a chance of replacing each air block in the provided area with a block determined by the provided BlockSetSelector.
     */
    protected void chanceReplaceAir(StructureWorldAccess world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
        }
    }

    /**
     * Has a chance of replacing each non-air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceNonAir(StructureWorldAccess world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
     * Has a chance of replacing each non-air block in the provided area with a block determined by the provided BlockSetSelector.
     */
    protected void chanceReplaceNonAir(StructureWorldAccess world, BlockBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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

    protected void chanceAddBlock(StructureWorldAccess world, Random random, float chance, BlockState block, int x, int y, int z, BlockBox blockBox) {
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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  PLACEMENT METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected boolean isInOcean(StructureWorldAccess world, int localX, int localZ) {
        BlockPos pos = new BlockPos.Mutable(applyXTransform(localX, localZ), 1, applyZTransform(localX, localZ));
        return world.getBiome(pos).getCategory() == Biome.Category.OCEAN;
    }
}
