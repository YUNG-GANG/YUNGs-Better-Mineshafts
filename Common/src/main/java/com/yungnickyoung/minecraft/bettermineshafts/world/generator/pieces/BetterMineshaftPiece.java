package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.BetterMineshaftFeatureConfiguration;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.MineshaftBlockStates;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.MineshaftBlockstateRandomizers;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.MineshaftDecorationChances;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.material.Material;

import java.util.Random;
import java.util.Set;

public abstract class BetterMineshaftPiece extends StructurePiece {
    public BetterMineshaftFeatureConfiguration config;

    protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final Set<Material> NON_SOLID_MATERIALS = Set.of(Material.AIR, Material.WATER, Material.LAVA, Material.WATER_PLANT);

    public BetterMineshaftPiece(StructurePieceType structurePieceType, int chainLength, BetterMineshaftFeatureConfiguration config, BoundingBox boundingBox) {
        super(structurePieceType, chainLength, boundingBox);
        this.config = config;
    }

    public BetterMineshaftPiece(StructurePieceType structurePieceType, CompoundTag compoundTag) {
        super(structurePieceType, compoundTag);
//        int index = compoundTag.getInt("MST");
//        this.config = legVariantIndex < MineshaftVariants.get().getVariants().size() && index >= 0
//                ? MineshaftVariants.get().getVariants().get(index)
//                : MineshaftVariants.get().getDefault();

        this.config = new BetterMineshaftFeatureConfiguration(
                compoundTag.getBoolean("flammableLegs"),
                compoundTag.getFloat("replacementRate"),
                BetterMineshaftStructureFeature.LegVariant.byId(compoundTag.getInt("legVariantIndex")),
                new MineshaftDecorationChances(
                        compoundTag.getFloat("vineChance"),
                        compoundTag.getFloat("snowChance"),
                        compoundTag.getFloat("cactusChance"),
                        compoundTag.getFloat("deadBushChance"),
                        compoundTag.getFloat("mushroomChance"),
                        compoundTag.getBoolean("lushDecorations")),
                new MineshaftBlockStates(
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("mainBlockId")),
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("supportBlockId")),
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("slabBlockId")),
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("gravelBlockId")),
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("stoneWallBlockId")),
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("stoneSlabBlockId")),
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("trapdoorBlockId")),
                        Block.BLOCK_STATE_REGISTRY.byId(compoundTag.getInt("smallLegBlockId"))),
                new MineshaftBlockstateRandomizers(
                        new BlockStateRandomizer(compoundTag.getCompound("mainSelector")),
                        new BlockStateRandomizer(compoundTag.getCompound("floorSelector")),
                        new BlockStateRandomizer(compoundTag.getCompound("brickSelector")),
                        new BlockStateRandomizer(compoundTag.getCompound("legSelector"))));
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
//        int index = MineshaftVariants.get().getVariants().indexOf(this.config);
//        compoundTag.putInt("MST", index);

        compoundTag.putBoolean("flammableLegs", this.config.flammableLegs);
        compoundTag.putFloat("replacementRate", this.config.replacementRate);
        compoundTag.putInt("legVariantIndex", config.legVariant.ordinal());
        compoundTag.putFloat("vineChance", this.config.decorationChances.vineChance);
        compoundTag.putFloat("snowChance", this.config.decorationChances.snowChance);
        compoundTag.putFloat("cactusChance", this.config.decorationChances.cactusChance);
        compoundTag.putFloat("deadBushChance", this.config.decorationChances.deadBushChance);
        compoundTag.putFloat("mushroomChance", this.config.decorationChances.mushroomChance);
        compoundTag.putBoolean("lushDecorations", this.config.decorationChances.lushDecorations);
        compoundTag.putInt("mainBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.mainBlockState));
        compoundTag.putInt("supportBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.supportBlockState));
        compoundTag.putInt("slabBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.slabBlockState));
        compoundTag.putInt("gravelBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.gravelBlockState));
        compoundTag.putInt("stoneWallBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.stoneWallBlockState));
        compoundTag.putInt("stoneSlabBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.stoneSlabBlockState));
        compoundTag.putInt("trapdoorBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.trapdoorBlockState));
        compoundTag.putInt("smallLegBlockId", Block.BLOCK_STATE_REGISTRY.getId(this.config.blockStates.smallLegBlockState));
        compoundTag.put("mainSelector", this.config.blockStateRandomizers.mainRandomizer.saveTag());
        compoundTag.put("floorSelector", this.config.blockStateRandomizers.floorRandomizer.saveTag());
        compoundTag.put("brickSelector", this.config.blockStateRandomizers.brickRandomizer.saveTag());
        compoundTag.put("legSelector", this.config.blockStateRandomizers.legRandomizer.saveTag());
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
     *                                  GENERATION UTIL METHODS                                *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    protected boolean addBarrel(WorldGenLevel world, BoundingBox boundingBox, Random random, BlockPos pos, ResourceLocation lootTableId) {
        if (boundingBox.isInside(pos) && world.getBlockState(pos).getBlock() != Blocks.BARREL) {
            world.setBlock(pos, Blocks.BARREL.defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP), 2);
            RandomizableContainerBlockEntity.setLootTable(world, random, pos, lootTableId);
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
    protected void addVines(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.addVines(world, boundingBox, Direction.EAST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.WEST, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.NORTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
        this.addVines(world, boundingBox, Direction.SOUTH, random, chance, minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Add decorations specific to a biome variant, such as snow.
     */
    protected void addBiomeDecorations(WorldGenLevel world, BoundingBox box, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos blockPos = getWorldPos(x, y, z);
                    BlockState state = this.getBlock(world, x, y, z, box);
                    BlockState stateBelow = this.getBlock(world, x, y - 1, z, box);

                    // Snow layers
                    if (config.decorationChances.snowChance > 0) {
                        if (random.nextFloat() < config.decorationChances.snowChance && state.isAir() && Blocks.SNOW.canSurvive(AIR, world, blockPos)) {
                            this.placeBlock(world, Blocks.SNOW.defaultBlockState().setValue(BlockStateProperties.LAYERS, random.nextInt(2) + 1), x, y, z, box);
                        }
                    }

                    if (config.decorationChances.lushDecorations) {
                        // Moss & ground plants
                        if (box.isInside(blockPos) && random.nextFloat() < .005f) {
                            CaveFeatures.MOSS_PATCH.value().place(world, world.getLevel().getChunkSource().getGenerator(), random, blockPos);
                        }

                        // Clay, water, dripleaf
                        if (box.isInside(blockPos) && random.nextFloat() < .005f) {
                            CaveFeatures.LUSH_CAVES_CLAY.value().place(world, world.getLevel().getChunkSource().getGenerator(), random, blockPos);
                        }

                        // Moss ceiling & cave vines
                        if (box.isInside(blockPos) && random.nextFloat() < .005f) {
                            CaveFeatures.MOSS_PATCH_CEILING.value().place(world, world.getLevel().getChunkSource().getGenerator(), random, blockPos);
                        }

                        // Moss layers
                        if (stateBelow.is(config.blockStates.mainBlockState.getBlock()) && state.isAir() && stateBelow.isFaceSturdy(world, blockPos.below(), Direction.UP)) {
                            this.placeBlock(world, Blocks.MOSS_CARPET.defaultBlockState(), x, y, z, box);
                        }
                    }

                    // Cacti
                    if (config.decorationChances.cactusChance > 0 && random.nextFloat() < config.decorationChances.cactusChance) {
                        if (state.isAir() && Blocks.CACTUS.canSurvive(AIR, world, blockPos)) {
                            this.placeBlock(world, Blocks.CACTUS.defaultBlockState().setValue(BlockStateProperties.AGE_15, 0), x, y, z, box);
                            if (random.nextFloat() < .5f && this.getBlock(world, x, y + 1, z, box).is(Blocks.AIR)) {
                                this.placeBlock(world, Blocks.CACTUS.defaultBlockState().setValue(BlockStateProperties.AGE_15, 0), x, y + 1, z, box);
                            }
                        }
                    }

                    // Dead bushes
                    if (config.decorationChances.deadBushChance > 0 && random.nextFloat() < config.decorationChances.deadBushChance) {
                        if (state.isAir() && (stateBelow.is(Blocks.SAND) || stateBelow.is(Blocks.RED_SAND) || stateBelow.is(Blocks.TERRACOTTA) || stateBelow.is(Blocks.WHITE_TERRACOTTA) || stateBelow.is(Blocks.ORANGE_TERRACOTTA) || stateBelow.is(Blocks.YELLOW_TERRACOTTA) || stateBelow.is(Blocks.BROWN_TERRACOTTA) || stateBelow.is(Blocks.DIRT))) {
                            this.placeBlock(world, Blocks.DEAD_BUSH.defaultBlockState(), x, y, z, box);
                        }
                    }

                    // Mushrooms
                    if (config.decorationChances.mushroomChance > 0) {
                        if (state.isAir() && Blocks.RED_MUSHROOM.canSurvive(AIR, world, blockPos)) {
                            float r = random.nextFloat();
                            if (r < config.decorationChances.mushroomChance / 2) {
                                this.placeBlock(world, Blocks.RED_MUSHROOM.defaultBlockState(), x, y, z, box);
                            } else if (r < config.decorationChances.mushroomChance) {
                                this.placeBlock(world, Blocks.BROWN_MUSHROOM.defaultBlockState(), x, y, z, box);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void generateLeg(WorldGenLevel world, Random random, BoundingBox box, int x, int z, BlockStateRandomizer selector) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, -1, z);
        BlockState state = this.getBlock(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);

        while (getWorldY(mutable.getY()) > world.getMinBuildHeight() + 1 && isReplaceableByStructures(state)) {
            this.placeBlock(world, selector.get(random), x, mutable.getY(), z, box);
            mutable.move(Direction.DOWN);
            state = this.getBlock(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);
        }
    }

    protected boolean generateLegOrChain(WorldGenLevel world, Random random, BoundingBox box, int x, int z, BlockStateRandomizer selector) {
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(x, -1, z);
        BlockState state = this.getBlock(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);
        boolean lavaBelow = false;
        while (getWorldY(mutable.getY()) > world.getMinBuildHeight() + 1 && isReplaceableByStructures(state)) {
            if (state.is(Blocks.LAVA)) {
                lavaBelow = true;
                break;
            }
            mutable.move(Direction.DOWN);
            state = this.getBlock(world, mutable.getX(), mutable.getY(), mutable.getZ(), box);
        }

        // If lava below, generate chain up. Else, generate leg as normal.
        if (lavaBelow) {
            mutable = this.getWorldPos(x, 0, z);
            if (!boundingBox.isInside(mutable)) return false;

            int realChainY = this.getWorldY(0);
            int length = 1;
            boolean canGenerateChain = true;

            while (canGenerateChain) {
                boolean currBlockCanBeReplaced;
                if (canGenerateChain) {
                    mutable.setY(realChainY + length);
                    BlockState currBlock = world.getBlockState(mutable);
                    currBlockCanBeReplaced = this.isReplaceableByStructures(currBlock);
                    if (!currBlockCanBeReplaced && this.canHangChainBelow(world, mutable, currBlock)) {
                        world.setBlock(mutable.setY(realChainY + 1), this.config.blockStates.supportBlockState, 2);
                        fillColumnBetween(world, Blocks.CHAIN.defaultBlockState(), mutable, realChainY + 2, realChainY + length);
                        return false;
                    }
                    canGenerateChain = length <= 50 && currBlockCanBeReplaced && mutable.getY() < world.getMaxBuildHeight() - 1;
                }
                ++length;
            }
        } else {
            generateLeg(world, random, box, x, z, selector);
            return true;
        }

        return false; // Return true if leg generated, false otherwise
    }

    protected void generatePillarDownOrChainUp(WorldGenLevel world, Random random, BoundingBox boundingBox, int x, int z, int pillarStartY, int chainStartY, BlockState chainBlock) {
        BlockPos.MutableBlockPos mutable = this.getWorldPos(x, pillarStartY, z);
        if (!boundingBox.isInside(mutable)) return;

        int realPillarY = this.getWorldY(pillarStartY);
        int realChainY = this.getWorldY(chainStartY);
        int length = 1;
        boolean canGenerateLeg = true;
        boolean canGenerateChain = true;

        while (canGenerateLeg || canGenerateChain) {
            boolean currBlockCanBeReplaced;

            if (canGenerateLeg) {
                mutable.setY(realPillarY - length);
                BlockState currBlock = world.getBlockState(mutable);
                currBlockCanBeReplaced = this.isReplaceableByStructures(currBlock) && !currBlock.is(Blocks.LAVA);
                if (!currBlockCanBeReplaced && this.canPlaceColumnOnTopOf(currBlock)) { // if we've hit solid or lava, and we can place a column at this position
                    fillColumnBetween(world, this.config.blockStates.smallLegBlockState, mutable, realPillarY - length + 1, realPillarY);
                    return;
                }
                canGenerateLeg = length <= 20 && currBlockCanBeReplaced && mutable.getY() > world.getMinBuildHeight() + 1;
            }

            if (canGenerateChain) {
                mutable.setY(realChainY + length);
                BlockState currBlock = world.getBlockState(mutable);
                currBlockCanBeReplaced = this.isReplaceableByStructures(currBlock);
                if (!currBlockCanBeReplaced && this.canHangChainBelow(world, mutable, currBlock)) {
                    world.setBlock(mutable.setY(realChainY + 1), chainBlock, 2);
                    fillColumnBetween(world, Blocks.CHAIN.defaultBlockState(), mutable, realChainY + 2, realChainY + length);
                    return;
                }
                canGenerateChain = length <= 50 && currBlockCanBeReplaced && mutable.getY() < world.getMaxBuildHeight() - 1;
            }
            ++length;
        }
    }

    protected void generatePillarDownOrChainUp(WorldGenLevel world, Random random, BoundingBox boundingBox, int x, int y, int z) {
        this.generatePillarDownOrChainUp(world, random, boundingBox, x, z, y, y, this.config.blockStates.supportBlockState);
    }

    private boolean canPlaceColumnOnTopOf(BlockState blockState) {
        return !blockState.is(Blocks.RAIL) && !blockState.is(Blocks.LAVA);
    }

    private boolean canHangChainBelow(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
        return Block.canSupportCenter(levelReader, blockPos, Direction.DOWN) && !(blockState.getBlock() instanceof FallingBlock);
    }

    protected boolean isReplaceableByStructures(BlockState blockState) {
        return blockState.isAir() ||
                blockState.getMaterial().isLiquid() ||
                blockState.is(Blocks.GLOW_LICHEN) ||
                blockState.is(Blocks.SEAGRASS) ||
                blockState.is(Blocks.TALL_SEAGRASS) ||
                blockState.is(Blocks.POINTED_DRIPSTONE) ||
                blockState.is(Blocks.CAVE_VINES) ||
                blockState.is(Blocks.CAVE_VINES_PLANT) ||
                blockState.is(Blocks.MOSS_CARPET) ||
                blockState.is(Blocks.SNOW);
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
                    // Don't allow overwriting placed chains
                    if (this.getBlock(world, x, y, z, boundingBox) == Blocks.CHAIN.defaultBlockState()) continue;
                    this.placeBlock(world, blockState, x, y, z, boundingBox);
                }
            }
        }
    }

    /**
     * Replaces each block in the provided area with blocks determined by the provided BlockStateRandomizer.
     */
    protected void fill(WorldGenLevel world, BoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockStateRandomizer selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    // Don't allow overwriting placed chains
                    if (this.getBlock(world, x, y, z, boundingBox) == Blocks.CHAIN.defaultBlockState()) continue;
                    this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with the provided BlockState.
     */
    protected void replaceAirOrChains(WorldGenLevel world, BoundingBox boundingBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && (currState.isAir() || currState == Blocks.CHAIN.defaultBlockState())) {
                        this.placeBlock(world, blockState, x, y, z, boundingBox);
                    }
                }
            }
        }
    }

    /**
     * Replaces each air block in the provided area with blocks determined by the provided BlockStateRandomizer.
     */
    protected void replaceAirOrChains(WorldGenLevel world, BoundingBox boundingBox, Random random, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockStateRandomizer selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && (currState.isAir() || currState == Blocks.CHAIN.defaultBlockState())) {
                        this.placeBlock(world, selector.get(random), x, y, z, boundingBox);
                    }
                }
            }
        }
    }

    protected static void fillColumnBetween(WorldGenLevel worldGenLevel, BlockState blockState, BlockPos.MutableBlockPos mutableBlockPos, int minY, int maxY) {
        for (int y = minY; y < maxY; ++y) {
            worldGenLevel.setBlock(mutableBlockPos.setY(y), blockState, 2);
        }
    }

    protected static void fillColumnBetween(WorldGenLevel worldGenLevel, Random random, BlockStateRandomizer selector, BlockPos.MutableBlockPos mutableBlockPos, int minY, int maxY) {
        for (int y = minY; y < maxY; ++y) {
            worldGenLevel.setBlock(mutableBlockPos.setY(y), selector.get(random), 2);
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
     * Has a chance of replacing each block in the provided area with a block determined by the provided BlockStateRandomizer.
     */
    protected void chanceFill(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockStateRandomizer selector) {
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
     * Has a chance of replacing each non-air block in the provided area with the provided BlockState.
     * Guaranteed to always replace liquid.
     */
    protected void chanceReplaceNonAir(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && currState != Blocks.CHAIN.defaultBlockState()) {
                        if ((currState.getMaterial() == Material.WATER || currState.getMaterial() == Material.LAVA) || (random.nextFloat() < chance && !currState.isAir())) {
                            this.placeBlock(world, blockState, x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each non-air block in the provided area with a block determined by the provided BlockStateRandomizer.
     * Guaranteed to always replace liquid.
     */
    protected void chanceReplaceNonAir(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockStateRandomizer selector) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                    if (currState != null && currState != Blocks.CHAIN.defaultBlockState()) {
                        if ((currState.getMaterial() == Material.WATER || currState.getMaterial() == Material.LAVA) || (random.nextFloat() < chance && !currState.isAir())) {
                            // Select random block state
                            BlockState blockState = selector.get(random);

                            // Don't place air where liquid was. This helps to avoid floating water.
                            if (currState.getMaterial() == Material.WATER || currState.getMaterial() == Material.LAVA) {
                                int numAttempts = 0;
                                while ((blockState == Blocks.AIR.defaultBlockState() || blockState == Blocks.CAVE_AIR.defaultBlockState()) && numAttempts < 10) {
                                    blockState = selector.get(random);
                                    numAttempts++;
                                }
                            }
                            this.placeBlock(world, blockState, x, y, z, boundingBox);
                        }
                    }
                }
            }
        }
    }

    /**
     * Has a chance of replacing each solid block in the provided area with the provided BlockState.
     */
    protected void chanceReplaceSolid(WorldGenLevel world, BoundingBox boundingBox, Random random, float chance, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState blockState) {
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    if (random.nextFloat() < chance) {
                        BlockState currState = this.getBlockAtFixed(world, x, y, z, boundingBox);
                        if (currState != null && currState != Blocks.CHAIN.defaultBlockState() && !NON_SOLID_MATERIALS.contains(currState.getMaterial())) {
                            this.placeBlock(world, blockState, x, y, z, boundingBox);
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
        BlockPos blockPos = this.getWorldPos(x, y, z);
        return !boundingBox.isInside(blockPos) ? null : blockGetter.getBlockState(blockPos);
    }
}
