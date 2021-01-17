package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.ImmutableSet;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariants;
import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class MineshaftPiece extends StructurePiece {
    protected MineshaftVariantSettings settings;
    protected int pieceChainLen;

    protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    private static final Set<Material> LIQUIDS = ImmutableSet.of(Material.LAVA, Material.WATER);

    public MineshaftPiece(IStructurePieceType structurePieceType, int i, int pieceChainLen, MineshaftVariantSettings settings) {
        super(structurePieceType, i);
        this.settings = settings;
        this.pieceChainLen = pieceChainLen;
    }

    public MineshaftPiece(IStructurePieceType structurePieceType, CompoundNBT compoundTag) {
        super(structurePieceType, compoundTag);
        int index = compoundTag.getInt("MST");
        this.settings = index < MineshaftVariants.get().getVariants().size() && index >= 0
            ? MineshaftVariants.get().getVariants().get(index)
            : MineshaftVariants.get().getDefault();
    }

    protected void toNbt(CompoundNBT tag) {
        int index = MineshaftVariants.get().getVariants().indexOf(this.settings);
        tag.putInt("MST", index);
    }

    public void setBoundingBox(MutableBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public MineshaftVariantSettings getSettings() {
        return settings;
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
        return settings.mainSelector;
    }

    protected BlockSetSelector getFloorSelector() {
        return settings.floorSelector;
    }

    protected BlockSetSelector getBrickSelector() {
        return settings.brickSelector;
    }

    protected BlockSetSelector getLegSelector() {
        return settings.legSelector;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                BIOME VARIANT BLOCK GETTERS                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected BlockState getMainBlock() {
        return settings.mainBlock;
    }

    protected BlockState getSupportBlock() {
        return settings.supportBlock;
    }

    protected BlockState getMainSlab() {
        return settings.slabBlock;
    }

    protected BlockState getGravel() {
        return settings.gravelBlock;
    }

    protected BlockState getMainDoorwayWall() {
        return settings.stoneWallBlock;
    }

    protected BlockState getMainDoorwaySlab() {
        return settings.stoneSlabBlock;
    }

    protected BlockState getTrapdoor() {
        return settings.trapdoorBlock;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  GENERATION UTIL METHODS                                *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected void addBarrel(ISeedReader world, MutableBoundingBox boundingBox, Random random, BlockPos pos, ResourceLocation lootTableId) {
        if (boundingBox.isVecInside(pos) && world.getBlockState(pos).getBlock() != Blocks.BARREL) {
            world.setBlockState(pos, Blocks.BARREL.getDefaultState().with(BarrelBlock.PROPERTY_FACING, Direction.UP), 2);
            TileEntity blockEntity = world.getTileEntity(pos);
            if (blockEntity instanceof BarrelTileEntity) {
                ((BarrelTileEntity) blockEntity).setLootTable(lootTableId, random.nextLong());
            }
        }
    }

    protected void addBarrel(ISeedReader world, MutableBoundingBox boundingBox, Random random, int x, int y, int z, ResourceLocation lootTableId) {
        BlockPos blockPos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        this.addBarrel(world, boundingBox, random, blockPos, lootTableId);
    }

    /**
     * Randomly add vines with a given chance in a given area, facing the specified direction.
     */
    protected void addVines(ISeedReader world, MutableBoundingBox boundingBox, Direction facing, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.setPos(x, y, z);
                    // Wrap in try-catch to attempt to avoid issues related to accessing unloaded chunks
                    try {
                        BlockState nextBlock = this.getBlockStateFromPos(world, x + facing.getXOffset(), y + facing.getYOffset(), z + facing.getZOffset(), boundingBox);
                        if (
                            this.getBlockStateFromPos(world, x, y, z, boundingBox).isAir()
                                && Block.doesSideFillSquare(nextBlock.getCollisionShape(world, mutable), facing.getOpposite())
                                && nextBlock.getBlock().getDefaultState() != Blocks.LADDER.getDefaultState()
                                && random.nextFloat() < chance
                        ) {
                            this.setBlockState(world, Blocks.VINE.getDefaultState().with(VineBlock.getPropertyFor(facing.getAxis() == Direction.Axis.X ? facing : facing.getOpposite()), true), x, y, z, boundingBox);
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    /**
     * Randomly add vines with a given chance in a given area, doing passes for all four horizontal directions.
     */
    protected void addVines(ISeedReader world, MutableBoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.addVines(world, boundingBox, Direction.EAST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.WEST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.NORTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.SOUTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Add decorations specific to a biome variant, such as snow.
     */
    protected void addBiomeDecorations(ISeedReader world, MutableBoundingBox box, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos blockPos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
                    BlockState state = this.getBlockStateFromPos(world, x, y, z, box);
                    BlockState stateBelow = this.getBlockStateFromPos(world, x, y - 1, z, box);
                    Block blockBelow = stateBelow.getBlock();

                    // Snow layers
                    if (settings.snowChance > 0) {
                        if (random.nextFloat() < settings.snowChance && state == CAVE_AIR && blockBelow != Blocks.ICE && blockBelow != Blocks.PACKED_ICE && blockBelow != Blocks.BARRIER && stateBelow.isSolidSide(world, blockPos.down(), Direction.UP)) {
                            this.setBlockState(world, Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, random.nextInt(2) + 1), x, y, z, box);
                        }
                    }

                    // Cactus
                    if (settings.cactusChance > 0) {
                        if (random.nextFloat() < settings.cactusChance) {
                            if (state == CAVE_AIR && blockBelow == Blocks.SAND) {
                                this.setBlockState(world, Blocks.CACTUS.getDefaultState().with(CactusBlock.AGE, 0), x, y, z, box);
                                if (random.nextFloat() < .5f && this.getBlockStateFromPos(world, x, y + 1, z, box) == CAVE_AIR) {
                                    this.setBlockState(world, Blocks.CACTUS.getDefaultState().with(CactusBlock.AGE, 0), x, y + 1, z, box);
                                }
                            }
                        }
                    }

                    // Deadbush
                    if (settings.deadBushChance > 0) {
                        if (random.nextFloat() < settings.deadBushChance) {
                            if (state == CAVE_AIR && (blockBelow == Blocks.SAND || blockBelow == Blocks.RED_SAND || blockBelow == Blocks.TERRACOTTA || blockBelow == Blocks.WHITE_TERRACOTTA || blockBelow == Blocks.ORANGE_TERRACOTTA || blockBelow == Blocks.MAGENTA_TERRACOTTA || blockBelow == Blocks.LIGHT_BLUE_TERRACOTTA || blockBelow == Blocks.YELLOW_TERRACOTTA || blockBelow == Blocks.LIME_TERRACOTTA || blockBelow == Blocks.PINK_TERRACOTTA || blockBelow == Blocks.GRAY_TERRACOTTA || blockBelow == Blocks.LIGHT_GRAY_TERRACOTTA || blockBelow == Blocks.CYAN_TERRACOTTA || blockBelow == Blocks.PURPLE_TERRACOTTA || blockBelow == Blocks.BLUE_TERRACOTTA || blockBelow == Blocks.BROWN_TERRACOTTA || blockBelow == Blocks.GREEN_TERRACOTTA || blockBelow == Blocks.RED_TERRACOTTA || blockBelow == Blocks.BLACK_TERRACOTTA || blockBelow == Blocks.DIRT || blockBelow == Blocks.COARSE_DIRT || blockBelow == Blocks.PODZOL)) {
                                this.setBlockState(world, Blocks.DEAD_BUSH.getDefaultState(), x, y, z, box);
                            }
                        }
                    }

                    // Mushrooms
                    if (settings.mushroomChance > 0) {
                        if (state == CAVE_AIR && (blockBelow == Blocks.MYCELIUM || blockBelow == Blocks.DIRT)) {
                            float r = random.nextFloat();
                            if (r < settings.mushroomChance / 2) {
                                this.setBlockState(world, Blocks.RED_MUSHROOM.getDefaultState(), x, y, z, box);
                            } else if (r < settings.mushroomChance) {
                                this.setBlockState(world, Blocks.BROWN_MUSHROOM.getDefaultState(), x, y, z, box);
                            }
                        }
                    }
                }
            }
        }
    }

    private BlockSetSelector getLegSelectorForPosition(ISeedReader world, int x, int z) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(this.getXWithOffset(x, z), this.getYWithOffset(-1), this.getZWithOffset(x, z));
        BlockState currBlock = world.getBlockState(mutable);

        // If this variant's legs are marked as flammable, we check for lava below. If there's lava, we will use the brick selector for this leg
        if (settings.flammableLegs) {
            while (mutable.getY() > 0 && (currBlock == CAVE_AIR || LIQUIDS.contains(currBlock.getMaterial()))) {
                if (currBlock.getMaterial() == Material.LAVA) {
                    return getBrickSelector();
                }
                mutable.move(Direction.DOWN);
                currBlock = world.getBlockState(mutable);
            }
        }

        return getLegSelector();
    }

    protected void generateLeg(ISeedReader world, Random random, int x, int z) {
        BlockSetSelector selector = getLegSelectorForPosition(world, x, z);
        BlockPos.Mutable mutable = new BlockPos.Mutable(this.getXWithOffset(x, z), this.getYWithOffset(-1), this.getZWithOffset(x, z));
        BlockState currBlock = world.getBlockState(mutable);

        while (mutable.getY() > 0 && (currBlock == CAVE_AIR || LIQUIDS.contains(currBlock.getMaterial()))) {
            world.setBlockState(mutable, selector.get(random), 2);
            mutable.move(Direction.DOWN);
            currBlock = world.getBlockState(mutable);
        }
    }

    protected void generateLegWithSelector(ISeedReader world, Random random, int x, int z, BlockSetSelector selector) {
        BlockPos.Mutable mutable = new BlockPos.Mutable(this.getXWithOffset(x, z), this.getYWithOffset(-1), this.getZWithOffset(x, z));
        BlockState currBlock = world.getBlockState(mutable);

        while (mutable.getY() > 0 && (currBlock == CAVE_AIR || LIQUIDS.contains(currBlock.getMaterial()))) {
            world.setBlockState(mutable, selector.get(random), 2);
            mutable.move(Direction.DOWN);
            currBlock = world.getBlockState(mutable);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                       FILL METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Replaces each block in the provided area with the provided BlockState.
     */
    protected void fill(ISeedReader world, MutableBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
    protected void fill(ISeedReader world, MutableBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void replaceAir(ISeedReader world, MutableBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
    protected void replaceAir(ISeedReader world, MutableBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void replaceNonAir(ISeedReader world, MutableBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
    protected void replaceNonAir(ISeedReader world, MutableBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void chanceFill(ISeedReader world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
    protected void chanceFill(ISeedReader world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void chanceReplaceAir(ISeedReader world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
    protected void chanceReplaceAir(ISeedReader world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void chanceReplaceNonAir(ISeedReader world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
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
    protected void chanceReplaceNonAir(ISeedReader world, MutableBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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

    protected void chanceAddBlock(ISeedReader world, Random random, float chance, BlockState block, int x, int y, int z, MutableBoundingBox blockBox) {
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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  PLACEMENT METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected boolean isInOcean(ISeedReader world, int localX, int localZ) {
        BlockPos pos = new BlockPos(getXWithOffset(localX, localZ), 1, getZWithOffset(localX, localZ));
        Biome biome = world.getBiome(pos);

        // Ensure biome registry name isn't null. This should never happen, but we do it to prevent NPE crashes.
        if (biome.getRegistryName() == null) {
            BetterMineshafts.LOGGER.error("Found null registry name for biome {} during ocean check. This shouldn't happen!", biome);
            return false;
        }

        RegistryKey<Biome> registryKey = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());
        return BiomeDictionary.hasType(registryKey, BiomeDictionary.Type.OCEAN);
    }
}
