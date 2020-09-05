package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.ImmutableSet;
import com.yungnickyoung.minecraft.bettermineshafts.util.BlockSetSelector;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariants;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class MineshaftPiece extends StructureComponent {
    protected MineshaftVariantSettings settings;
    protected int pieceChainLen;

    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final Set<Material> LIQUIDS = ImmutableSet.of(Material.LAVA, Material.WATER);

    public MineshaftPiece() {}

    public MineshaftPiece(int i, int pieceChainLen, MineshaftVariantSettings settings) {
        super(i);
        this.settings = settings;
        this.pieceChainLen = pieceChainLen;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tag) {
        int index = MineshaftVariants.get().getVariants().indexOf(this.settings);
        tag.setInteger("MST", index);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
        int index = tagCompound.getInteger("MST");
        this.settings = index < MineshaftVariants.get().getVariants().size() && index >= 0
            ? MineshaftVariants.get().getVariants().get(index)
            : MineshaftVariants.get().getDefault();
    }

    public void setBoundingBox(StructureBoundingBox boundingBox) {
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
    public void buildComponent(StructureComponent structurePiece, List<StructureComponent> list, Random random) {
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                     BLOCK SELECTORS                                     *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected BlockSetSelector getMainSelector() {
        return settings.selector;
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

    protected IBlockState getMainBlock() {
        return settings.mainBlock;
    }

    protected IBlockState getSupportBlock() {
        return settings.supportBlock;
    }

    protected IBlockState getMainSlab() {
        return settings.slabBlock;
    }

    protected IBlockState getGravel() {
        return settings.gravelBlock;
    }

    protected IBlockState getMainDoorwayWall() {
        return settings.stoneWallBlock;
    }

    protected IBlockState getMainDoorwaySlab() {
        return settings.stoneSlabBlock;
    }

    protected IBlockState getTrapdoor() {
        return Blocks.TRAPDOOR.getDefaultState();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  GENERATION UTIL METHODS                                *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     * Randomly add vines with a given chance in a given area, facing the specified direction.
     */
    protected void addVines(World world, StructureBoundingBox boundingBox, EnumFacing facing, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    mutable.setPos(x, y, z);
                    EnumFacing dir = facing.getAxis() == EnumFacing.Axis.X ? facing : facing.getOpposite();
                    if (
                        this.getBlockStateFromPos(world, x, y, z, boundingBox).getBlock() == Blocks.AIR
                            && this.getBlockStateFromPos(world, x + facing.getXOffset(), y, z + facing.getZOffset(), boundingBox).getBlockFaceShape(world, new BlockPos(x + dir.getZOffset(), y, z + dir.getZOffset()), dir.getOpposite()) == BlockFaceShape.SOLID
                    ) {
                        if (random.nextFloat() < chance) {
                            this.setBlockState(world, Blocks.VINE.getDefaultState().withProperty(BlockVine.NORTH, dir == EnumFacing.NORTH).withProperty(BlockVine.EAST, dir == EnumFacing.EAST).withProperty(BlockVine.SOUTH, dir == EnumFacing.SOUTH).withProperty(BlockVine.WEST, dir == EnumFacing.WEST), x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Randomly add vines with a given chance in a given area, doing passes for all four horizontal directions.
     */
    protected void addVines(World world, StructureBoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        if (settings.vineChance <= 0) return;
        this.addVines(world, boundingBox, EnumFacing.EAST, random, settings.vineChance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, EnumFacing.WEST, random, settings.vineChance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, EnumFacing.NORTH, random, settings.vineChance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, EnumFacing.SOUTH, random, settings.vineChance, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Add decorations specific to a biome variant, such as snow.
     */
    protected void addBiomeDecorations(World world, StructureBoundingBox box, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos blockPos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
                    IBlockState state = this.getBlockStateFromPos(world, x, y, z, box);
                    IBlockState stateBelow = this.getBlockStateFromPos(world, x, y - 1, z, box);
                    Block blockBelow = stateBelow.getBlock();

                    // Snow layers
                    if (settings.snowChance > 0) {
                        if (random.nextFloat() < settings.snowChance && state == AIR && blockBelow != Blocks.ICE && blockBelow != Blocks.PACKED_ICE && blockBelow != Blocks.BARRIER && stateBelow.isSideSolid(world, blockPos.down(), EnumFacing.UP)) {
                            this.setBlockState(world, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, random.nextInt(2) + 1), x, y, z, box);
                        }
                    }

                    // Cactus
                    if (settings.cactusChance > 0) {
                        if (random.nextFloat() < settings.cactusChance) {
                            if (state == AIR && blockBelow == Blocks.SAND) {
                                this.setBlockState(world, Blocks.CACTUS.getDefaultState().withProperty(BlockCactus.AGE, 0), x, y, z, box);
                                if (random.nextFloat() < .5f && this.getBlockStateFromPos(world, x, y + 1, z, box) == AIR) {
                                    this.setBlockState(world, Blocks.CACTUS.getDefaultState().withProperty(BlockCactus.AGE, 0), x, y + 1, z, box);
                                }
                            }
                        }
                    }

                    // Deadbush
                    if (settings.deadBushChance > 0) {
                        if (random.nextFloat() < settings.deadBushChance) {
                            if (state == AIR && (blockBelow == Blocks.SAND || blockBelow == Blocks.STAINED_HARDENED_CLAY || blockBelow == Blocks.HARDENED_CLAY || blockBelow == Blocks.DIRT)) {
                                this.setBlockState(world, Blocks.DEADBUSH.getDefaultState(), x, y, z, box);
                            }
                        }
                    }

                    // Mushrooms
                    if (settings.mushroomChance > 0) {
                        if (state == AIR && (blockBelow == Blocks.MYCELIUM || (blockBelow == Blocks.DIRT))) {
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

    protected void generateLeg(World world, Random random, StructureBoundingBox box, int x, int z, BlockSetSelector selector) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, -1, z);
        IBlockState state = this.getBlockStateFromPos(world, mutable, box);

        while (getYWithOffset(mutable.getY()) > 0 && (state == AIR || LIQUIDS.contains(state.getMaterial()))) {
            this.setBlockState(world, selector.get(random), x, mutable.getY(), z, box);
            mutable.move(EnumFacing.DOWN);
            state = this.getBlockStateFromPos(world, mutable, box);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                       FILL METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Replaces each block in the provided area with the provided BlockState.
     */
    protected void fill(World world, StructureBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockState) {
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
    protected void fill(World world, StructureBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void replaceAir(World world, StructureBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && currState == AIR) {
                        this.setBlockState(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void replaceAir(World world, StructureBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && currState == AIR) {
                        this.setBlockState(world, selector.get(random), x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each non-air block in the provided area with the provided BlockState.
     */
    protected void replaceNonAir(World world, StructureBoundingBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && currState != AIR) {
                        this.setBlockState(world, blockState, x, y, z, blockBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each non-air block in the provided area with blocks determined by the provided BlockSelector.
     */
    protected void replaceNonAir(World world, StructureBoundingBox blockBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                    if (currState != null && currState != AIR) {
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
    protected void chanceFill(World world, StructureBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockState) {
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
    protected void chanceFill(World world, StructureBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
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
    protected void chanceReplaceAir(World world, StructureBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && currState == AIR) {
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
    protected void chanceReplaceAir(World world, StructureBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && currState == AIR) {
                            this.setBlockState(world, selector.get(random), x, y, z, blockBox);
                        }
                    }
                }
            }
        }    }

    /**
     * Has a chance of replacing each non-air block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceNonAir(World world, StructureBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, IBlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && currState != AIR) {
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
    protected void chanceReplaceNonAir(World world, StructureBoundingBox blockBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockSetSelector selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        IBlockState currState = this.getBlockStateFromPosFixed(world, x, y, z, blockBox);
                        if (currState != null && currState != AIR) {
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

    protected void chanceAddBlock(World world, Random random, float chance, IBlockState block, int x, int y, int z, StructureBoundingBox blockBox) {
        if (random.nextFloat() < chance) {
            this.setBlockState(world, block, x, y, z, blockBox);
        }
    }

    /**
     * My "fixed" version of getBlockAt that returns null instead of air if block is out of bounds
     *
     * @return the block at the given position, or null if it is outside of the blockBox
     */
    protected IBlockState getBlockStateFromPosFixed(World world, int x, int y, int z, StructureBoundingBox blockBox) {
        int i = this.getXWithOffset(x, z);
        int j = this.getYWithOffset(y);
        int k = this.getZWithOffset(x, z);
        BlockPos blockPos = new BlockPos(i, j, k);
        return !blockBox.isVecInside(blockPos) ? null : world.getBlockState(blockPos);
    }

    protected IBlockState getBlockStateFromPos(World worldIn, BlockPos pos, StructureBoundingBox boundingboxIn) {
        return super.getBlockStateFromPos(worldIn, pos.getX(), pos.getY(), pos.getZ(), boundingboxIn);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                  PLACEMENT METHODS                                      *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected boolean isInOcean(World world, int localX, int localZ) {
        BlockPos pos = new BlockPos.MutableBlockPos(getXWithOffset(localX, localZ), 1, getZWithOffset(localX, localZ));
        return BiomeDictionary.hasType(world.getBiome(pos), BiomeDictionary.Type.OCEAN);
    }
}
