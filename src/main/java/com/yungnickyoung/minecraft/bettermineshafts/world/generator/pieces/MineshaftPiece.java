package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.ImmutableSet;
import com.yungnickyoung.minecraft.bettermineshafts.util.BlockSetSelector;
import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
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

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class MineshaftPiece extends StructureComponent {
    public MapGenBetterMineshaft.Type mineshaftType;
    protected int pieceChainLen;

    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private static final Set<Material> LIQUIDS = ImmutableSet.of(Material.LAVA, Material.WATER);

    public MineshaftPiece() {}

    public MineshaftPiece(int i, int pieceChainLen, MapGenBetterMineshaft.Type type) {
        super(i);
        this.mineshaftType = type;
        this.pieceChainLen = pieceChainLen;
    }

    @Override
    protected void writeStructureToNBT(NBTTagCompound tag) {
        tag.setInteger("MST", this.mineshaftType.ordinal());
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readStructureFromNBT(NBTTagCompound tagCompound, TemplateManager p_143011_2_) {
        this.mineshaftType = MapGenBetterMineshaft.Type.byId(tagCompound.getInteger("MST"));
    }

    public void setBoundingBox(StructureBoundingBox boundingBox) {
        this.boundingBox = boundingBox;
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

    protected BlockSetSelector getFloorSelector() {
        switch (this.mineshaftType) {
            case DESERT:
                return BlockSetSelector.FLOOR_DESERT;
            case RED_DESERT:
                return BlockSetSelector.FLOOR_RED_DESERT;
            default:
                return getMainSelector();
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
                return BlockSetSelector.from(Blocks.LOG2.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK));
            case JUNGLE:
                return BlockSetSelector.from(Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE));
            case SNOW:
                return BlockSetSelector.from(Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE));
            case ICE:
                return BlockSetSelector.STONE_BRICK_ICE;
            case DESERT:
                return BlockSetSelector.STONE_BRICK_DESERT;
            case RED_DESERT:
                return BlockSetSelector.STONE_BRICK_RED_DESERT;
            case MUSHROOM:
                return BlockSetSelector.STONE_BRICK_MUSHROOM;
            case SAVANNA:
                return BlockSetSelector.from(Blocks.LOG2.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA));
            default:
                return BlockSetSelector.from(Blocks.LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK));
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                BIOME VARIANT BLOCK GETTERS                              *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected IBlockState getMainBlock() {
        switch (this.mineshaftType) {
            case MESA:
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK);
            case JUNGLE:
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE);
            case SNOW:
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE);
            case ICE:
                return Blocks.PACKED_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SANDSTONE.getDefaultState();
            case MUSHROOM:
                return Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
            case SAVANNA:
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA);
            default:
                return Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK);
        }
    }

    protected IBlockState getSupportBlock() {
        switch (this.mineshaftType) {
            case MESA:
                return Blocks.DARK_OAK_FENCE.getDefaultState();
            case JUNGLE:
                return Blocks.JUNGLE_FENCE.getDefaultState();
            case SNOW:
                return Blocks.SPRUCE_FENCE.getDefaultState();
            case ICE:
                return Blocks.PACKED_ICE.getDefaultState();
            case MUSHROOM:
                return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
            case SAVANNA:
                return Blocks.ACACIA_FENCE.getDefaultState();
            default:
                return Blocks.OAK_FENCE.getDefaultState();
        }
    }

    protected IBlockState getMainSlab() {
        switch (this.mineshaftType) {
            case MESA:
                return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.DARK_OAK);
            case JUNGLE:
                return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.JUNGLE);
            case SNOW:
                return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE);
            case ICE:
                return Blocks.PACKED_ICE.getDefaultState();
            case DESERT:
                return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND);
            case RED_DESERT:
                return Blocks.STONE_SLAB2.getDefaultState().withProperty(BlockStoneSlabNew.VARIANT, BlockStoneSlabNew.EnumType.RED_SANDSTONE);
            case MUSHROOM:
                return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState();
            case SAVANNA:
                return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA);
            default:
                return Blocks.WOODEN_SLAB.getDefaultState().withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.OAK);
        }
    }

    protected IBlockState getBrickBlock() {
        switch (this.mineshaftType) {
            case SNOW:
                return Blocks.SNOW.getDefaultState();
            case ICE:
                return Blocks.PACKED_ICE.getDefaultState();
            case DESERT:
                return Blocks.SANDSTONE.getDefaultState();
            case RED_DESERT:
                return Blocks.RED_SANDSTONE.getDefaultState();
            case MUSHROOM:
                return Blocks.RED_MUSHROOM_BLOCK.getDefaultState();
            default:
                return Blocks.STONEBRICK.getDefaultState();
        }
    }

    protected IBlockState getGravel() {
        switch (this.mineshaftType) {
            case DESERT:
                return Blocks.SAND.getDefaultState();
            case RED_DESERT:
                return Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
            case SNOW:
            case ICE:
                return Blocks.SNOW.getDefaultState();
            default:
                return Blocks.GRAVEL.getDefaultState();
        }
    }

    protected IBlockState getMainDoorwayWall() {
        switch (this.mineshaftType) {
            case SNOW:
                return Blocks.SNOW.getDefaultState();
            case ICE:
                return Blocks.PACKED_ICE.getDefaultState();
            case RED_DESERT:
                return Blocks.DARK_OAK_FENCE.getDefaultState();
            case MUSHROOM:
                return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
            default:
                return Blocks.COBBLESTONE_WALL.getDefaultState();
        }
    }

    protected IBlockState getMainDoorwaySlab() {
        switch (this.mineshaftType) {
            case SNOW:
                return Blocks.SNOW.getDefaultState();
            case ICE:
                return Blocks.PACKED_ICE.getDefaultState();
            case DESERT:
                return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
            case RED_DESERT:
                return Blocks.STONE_SLAB2.getDefaultState().withProperty(BlockStoneSlabNew.VARIANT, BlockStoneSlabNew.EnumType.RED_SANDSTONE).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
            case MUSHROOM:
                return Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
            default:
                return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SMOOTHBRICK).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
        }
    }

    protected IBlockState getTrapdoor() {
        return Blocks.TRAPDOOR.getDefaultState();
    }

    protected float getVineChance() {
        switch (this.mineshaftType) {
            case JUNGLE:
                return .6f;
            default:
                return .25f;
        }
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
    protected void addVines(World world, StructureBoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.addVines(world, boundingBox, EnumFacing.EAST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, EnumFacing.WEST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, EnumFacing.NORTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, EnumFacing.SOUTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
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

                    // Snow variant
                    if (mineshaftType == MapGenBetterMineshaft.Type.SNOW) {
                        if (state == AIR && blockBelow != Blocks.ICE && blockBelow != Blocks.PACKED_ICE && blockBelow != Blocks.BARRIER && stateBelow.isSideSolid(world, blockPos.down(), EnumFacing.UP)) {
                            this.setBlockState(world, Blocks.SNOW_LAYER.getDefaultState().withProperty(BlockSnow.LAYERS, random.nextInt(2) + 1), x, y, z, box);
                        }
                    }
                    // Desert and Red Desert variants
                    else if (mineshaftType == MapGenBetterMineshaft.Type.DESERT || mineshaftType == MapGenBetterMineshaft.Type.RED_DESERT) {
                        float r = random.nextFloat();
                        if (r < .1f) {
                            if (state == AIR && blockBelow == Blocks.SAND) {
                                this.setBlockState(world, Blocks.CACTUS.getDefaultState().withProperty(BlockCactus.AGE, 0), x, y, z, box);
                                if (random.nextFloat() < .5f && this.getBlockStateFromPos(world, x, y + 1, z, box) == AIR) {
                                    this.setBlockState(world, Blocks.CACTUS.getDefaultState().withProperty(BlockCactus.AGE, 0), x, y + 1, z, box);
                                }
                            }
                        } else if (r < .2f) {
                            if (state == AIR && (blockBelow == Blocks.SAND || blockBelow == Blocks.STAINED_HARDENED_CLAY || blockBelow == Blocks.HARDENED_CLAY || blockBelow == Blocks.DIRT)) {
                                this.setBlockState(world, Blocks.DEADBUSH.getDefaultState(), x, y, z, box);
                            }
                        }

                    }
                    // Mesa variant
                    else if (mineshaftType == MapGenBetterMineshaft.Type.MESA) {
                        if (random.nextFloat() < .1f) {
                            if (state == AIR && (blockBelow == Blocks.SAND || blockBelow == Blocks.STAINED_HARDENED_CLAY || blockBelow == Blocks.HARDENED_CLAY || blockBelow == Blocks.DIRT)) {
                                this.setBlockState(world, Blocks.DEADBUSH.getDefaultState(), x, y, z, box);
                            }
                        }
                    }
                    // Mushroom variant
                    else if (mineshaftType == MapGenBetterMineshaft.Type.MUSHROOM) {
                        if (state == AIR && (blockBelow == Blocks.MYCELIUM || (blockBelow == Blocks.DIRT))) {
                            float r = random.nextFloat();
                            if (r < .2f) {
                                this.setBlockState(world, Blocks.RED_MUSHROOM.getDefaultState(), x, y, z, box);
                            } else if (r < .4f) {
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
}
