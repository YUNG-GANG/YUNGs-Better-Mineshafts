package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmallTunnel extends MineshaftPiece {
    private final List<Integer> supports = new ArrayList<>(); // local z coords
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 5,
        MAIN_AXIS_LEN = 8;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SmallTunnel(CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, compoundTag);
        ListTag listTag1 = compoundTag.getList("Supports", 3);
        for (int i = 0; i < listTag1.size(); ++i) {
            this.supports.add(listTag1.getInt(i));
        }
    }

    public SmallTunnel(int chunkPieceLen, Random random, BoundingBox blockBox, Direction direction, BetterMineshaftStructureFeature.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, chunkPieceLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
        super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
        ListTag listTag1 = new ListTag();
        supports.forEach(z -> listTag1.add(IntTag.valueOf(z)));
        compoundTag.put("Supports", listTag1);
    }

    public static BoundingBox determineBoxPosition(StructurePieceAccessor structurePieceAccessor, Random random, int x, int y, int z, Direction direction) {
        BoundingBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePieceAccessor.findCollisionPiece(blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void addChildren(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, Random random) {
        Direction direction = this.getOrientation();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() - 1, direction, this.genDepth);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX(), this.boundingBox.minY(), this.boundingBox.maxZ() + 1, direction, this.genDepth);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.minX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ(), direction, this.genDepth);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePieceAccessor, random, this.boundingBox.maxX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), direction, this.genDepth);
        }

        buildSupports(random);
    }

    @Override
    public void postProcess(WorldGenLevel world, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        // Fill in any air in floor with main block
        this.replaceAirOrChains(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());

        // Decorations
        generateSupports(world, box, random);
        generateRails(world, box, random);
        generateChestCarts(world, box, random);
        generateTntCarts(world, box, random);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
        this.addBiomeDecorations(world, box, random, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1);
        generateTorches(world, box, random);
        generatePillarsOrChains(world, box, random);
    }

    private void generateChestCarts(WorldGenLevel world, BoundingBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftChestMinecartSpawnRate) {
                BlockPos blockPos = this.getWorldPos(LOCAL_X_END / 2, 1, z);
                if (box.isInside(blockPos) && !world.getBlockState(blockPos.below()).isAir()) {
                    MinecartChest chestMinecartEntity = new MinecartChest(world.getLevel(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(BuiltInLootTables.ABANDONED_MINESHAFT, random.nextLong());
                    world.addFreshEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateSupports(WorldGenLevel world, BoundingBox box, Random random) {
        float cobwebChance = (float) BetterMineshafts.CONFIG.spawnRates.cobwebSpawnRate;
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(BlockStateProperties.EAST_WALL) && supportBlock.getProperties().contains(BlockStateProperties.WEST_WALL)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.EAST_WALL, WallSide.TALL).setValue(BlockStateProperties.WEST_WALL, WallSide.TALL);
        } else if (supportBlock.getProperties().contains(BlockStateProperties.EAST) && supportBlock.getProperties().contains(BlockStateProperties.WEST)) {
            supportBlock = supportBlock.setValue(BlockStateProperties.EAST, true).setValue(BlockStateProperties.WEST, true);
        }

        for (int z : this.supports) {
            // Check if the area is covered. We only need to spawn supports in covered areas.
            int numCovered = 0; // We require at least 2 of 3 to be covered
            for (int x = 1; x <= 3; x++) {
                BlockState blockState = this.getBlock(world, x, 4, z, box);
                if (!blockState.isAir() && !blockState.is(Blocks.CHAIN)) {
                    numCovered++;
                }
            }

            if (numCovered < 2) continue;

            // Place the support
            this.fill(world, box, 1, 1, z, 1, 2, z, getSupportBlock());
            this.fill(world, box, 3, 1, z, 3, 2, z, getSupportBlock());
            this.fill(world, box, 1, 3, z, 3, 3, z, getMainBlock());
            this.chanceReplaceNonAir(world, box, random, .25f, 1, 3, z, 3, 3, z, supportBlock);

            // Spawn cobwebs around it
            this.chanceReplaceAir(world, box, random, cobwebChance, 1, 3, z - 1, 1, 3, z + 1, Blocks.COBWEB.defaultBlockState());
            this.chanceReplaceAir(world, box, random, cobwebChance, 3, 3, z - 1, 3, 3, z + 1, Blocks.COBWEB.defaultBlockState());
        }
    }

    private void generateRails(WorldGenLevel world, BoundingBox box, Random random) {
        // Place rails in center
        this.chanceFill(world, box, random, .5f, 2, 1, 0, 2, 1, LOCAL_Z_END, Blocks.RAIL.defaultBlockState());
        // Place powered rails
        for (int n = 0; n <= LOCAL_Z_END; n++) {
            this.chanceAddBlock(world, random, .07f, Blocks.POWERED_RAIL.defaultBlockState().setValue(PoweredRailBlock.POWERED, true), 2, 1, n, box);
        }
    }

    private void generateTntCarts(WorldGenLevel world, BoundingBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftTntMinecartSpawnRate) {
                BlockPos blockPos = this.getWorldPos(LOCAL_X_END / 2, 1, z);
                if (box.isInside(blockPos) && !world.getBlockState(blockPos.below()).isAir()) {
                    MinecartTNT tntMinecartEntity = new MinecartTNT(world.getLevel(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    world.addFreshEntity(tntMinecartEntity);
                }
            }
        }
    }

    private void generateTorches(WorldGenLevel world, BoundingBox box, Random random) {
        BlockState torchBlock = Blocks.WALL_TORCH.defaultBlockState();
        float r;
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (this.supports.contains(z)) continue;
            r = random.nextFloat();
            if (r < BetterMineshafts.CONFIG.spawnRates.torchSpawnRate / 2) {
                BlockPos pos = this.getWorldPos(1, 2, z);
                BlockPos adjPos = this.getWorldPos(0, 2, z);
                boolean canPlace = world.getBlockState(pos).isAir() && world.getBlockState(adjPos) != AIR;
                if (canPlace) {
                    this.replaceAirOrChains(world, box, 1, 2, z, 1, 2, z, torchBlock.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
                }
            } else if (r < BetterMineshafts.CONFIG.spawnRates.torchSpawnRate) {
                BlockPos pos = this.getWorldPos(LOCAL_X_END - 1, 2, z);
                BlockPos adjPos = this.getWorldPos(LOCAL_X_END, 2, z);
                boolean canPlace = world.getBlockState(pos).isAir() && world.getBlockState(adjPos) != AIR;
                if (canPlace) {
                    this.replaceAirOrChains(world, box, LOCAL_X_END - 1, 2, z, LOCAL_X_END - 1, 2, z, torchBlock.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
                }
            }
        }
    }

    private void generatePillarsOrChains(WorldGenLevel world, BoundingBox box, Random random) {
        generatePillarDownOrChainUp(world, random, box, 1, 0, 1);
        generatePillarDownOrChainUp(world, random, box, LOCAL_X_END - 1, 0, 1);
        generatePillarDownOrChainUp(world, random, box, 1, 0, LOCAL_Z_END - 1);
        generatePillarDownOrChainUp(world, random, box, LOCAL_X_END - 1, 0, LOCAL_Z_END - 1);
    }

    private void buildSupports(Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            int r = random.nextInt(7);
            if (r == 0) { // Big support
                supports.add(z);
                z += 5;
            }
        }
    }
}
