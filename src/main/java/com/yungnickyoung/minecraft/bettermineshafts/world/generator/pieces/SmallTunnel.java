package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.WallShape;
import net.minecraft.entity.vehicle.ChestMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesHolder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;

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

    public SmallTunnel(ServerWorld world, NbtCompound compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, compoundTag);
        NbtList listTag1 = compoundTag.getList("Supports", 3);
        for (int i = 0; i < listTag1.size(); ++i) {
            this.supports.add(listTag1.getInt(i));
        }
    }

    public SmallTunnel(int chunkPieceLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, chunkPieceLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void writeNbt(ServerWorld world, NbtCompound tag) {
        super.writeNbt(world, tag);
        NbtList listTag1 = new NbtList();
        supports.forEach(z -> listTag1.add(NbtInt.of(z)));
        tag.put("Supports", listTag1);
    }

    public static BlockBox determineBoxPosition(StructurePiecesHolder structurePiecesHolder, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePiecesHolder.getIntersecting(blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void fillOpenings(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random) {
        Direction direction = this.getFacing();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxZ(), this.boundingBox.getMinX(), this.boundingBox.getMinY() - 1, direction, chainLength);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinZ(), this.boundingBox.getMinX(), this.boundingBox.getMaxY() + 1, direction, chainLength);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMaxZ() - 1, this.boundingBox.getMinX(), this.boundingBox.getMaxY(), direction, chainLength);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, structurePiecesHolder, random, this.boundingBox.getMinZ() + 1, this.boundingBox.getMinX(), this.boundingBox.getMinY(), direction, chainLength);
        }

        buildSupports(random);
    }

    @Override
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isTouchingLiquid(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, this.getReplacementRate(), 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());

        // Fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());

        // Decorations
        generateSupports(world, box, random);
        generateRails(world, box, random);
        generateCobwebs(world, box, random);
        generateChestCarts(world, box, random);
        generateTntCarts(world, box, random);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);
        this.addBiomeDecorations(world, box, random, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1);
        generateTorches(world, box, random);

        return true;
    }

    private void generateCobwebs(StructureWorldAccess world, BlockBox box, Random random) {
        float chance = (float) BetterMineshafts.CONFIG.spawnRates.cobwebSpawnRate;
        supports.forEach(z -> {
            this.chanceReplaceAir(world, box, random, chance, 1, 3, z - 1, 1, 3, z + 1, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, chance, 3, 3, z - 1, 3, 3, z + 1, Blocks.COBWEB.getDefaultState());
        });
    }

    private void generateChestCarts(StructureWorldAccess world, BlockBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftChestMinecartSpawnRate) {
                BlockPos blockPos = new BlockPos(this.applyXTransform(LOCAL_X_END / 2, z), applyYTransform(1), this.applyZTransform(LOCAL_X_END / 2, z));
                if (box.contains(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(world.toServerWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(LootTables.ABANDONED_MINESHAFT_CHEST, random.nextLong());
                    world.spawnEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateSupports(StructureWorldAccess world, BlockBox box, Random random) {
        BlockState supportBlock = getSupportBlock();
        if (supportBlock.getProperties().contains(Properties.EAST_WALL_SHAPE) && supportBlock.getProperties().contains(Properties.WEST_WALL_SHAPE)) {
            supportBlock = supportBlock.with(Properties.EAST_WALL_SHAPE, WallShape.TALL).with(Properties.WEST_WALL_SHAPE, WallShape.TALL);
        } else if (supportBlock.getProperties().contains(Properties.EAST) && supportBlock.getProperties().contains(Properties.WEST)) {
            supportBlock = supportBlock.with(Properties.EAST, true).with(Properties.WEST, true);
        }

        for (int z : this.supports) {
            this.fill(world, box, 1, 1, z, 1, 2, z, getSupportBlock());
            this.fill(world, box, 3, 1, z, 3, 2, z, getSupportBlock());
            this.fill(world, box, 1, 3, z, 3, 3, z, getMainBlock());
            this.chanceReplaceNonAir(world, box, random, .25f, 1, 3, z, 3, 3, z, supportBlock);
        }
    }

    private void generateRails(StructureWorldAccess world, BlockBox box, Random random) {
        // Place rails in center
        this.chanceFill(world, box, random, .5f, 2, 1, 0, 2, 1, LOCAL_Z_END, Blocks.RAIL.getDefaultState());
        // Place powered rails
        for (int n = 0; n <= LOCAL_Z_END; n++) {
            this.chanceAddBlock(world, random, .07f, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.POWERED, true), 2, 1, n, box);
        }
    }

    private void generateTntCarts(StructureWorldAccess world, BlockBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < BetterMineshafts.CONFIG.spawnRates.smallShaftTntMinecartSpawnRate) {
                BlockPos blockPos = new BlockPos(this.applyXTransform(LOCAL_X_END / 2, z), applyYTransform(1), this.applyZTransform(LOCAL_X_END / 2, z));
                if (box.contains(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    TntMinecartEntity tntMinecartEntity = new TntMinecartEntity(world.toServerWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    world.spawnEntity(tntMinecartEntity);
                }
            }
        }
    }

    private void generateTorches(StructureWorldAccess world, BlockBox box, Random random) {
        BlockState torchBlock = Blocks.WALL_TORCH.getDefaultState();
        float r;
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (this.supports.contains(z)) continue;
            r = random.nextFloat();
            if (r < BetterMineshafts.CONFIG.spawnRates.torchSpawnRate / 2) {
                BlockPos pos = new BlockPos(applyXTransform(1, z), applyYTransform(2), applyZTransform(1, z));
                BlockPos adjPos = new BlockPos(applyXTransform(0, z), applyYTransform(2), applyZTransform(0, z));
                boolean canPlace = world.getBlockState(pos).isAir() && world.getBlockState(adjPos) != AIR;
                if (canPlace) {
                    this.replaceAir(world, box, 1, 2, z, 1, 2, z, torchBlock.with(Properties.HORIZONTAL_FACING, Direction.EAST));
                }
            } else if (r < BetterMineshafts.CONFIG.spawnRates.torchSpawnRate) {
                BlockPos pos = new BlockPos(applyXTransform(LOCAL_X_END - 1, z), applyYTransform(2), applyZTransform(LOCAL_X_END - 1, z));
                BlockPos adjPos = new BlockPos(applyXTransform(LOCAL_X_END, z), applyYTransform(2), applyZTransform(LOCAL_X_END, z));
                boolean canPlace = world.getBlockState(pos).isAir() && world.getBlockState(adjPos) != AIR;
                if (canPlace) {
                    this.replaceAir(world, box, LOCAL_X_END - 1, 2, z, LOCAL_X_END - 1, 2, z, torchBlock.with(Properties.HORIZONTAL_FACING, Direction.WEST));
                }
            }
        }
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
