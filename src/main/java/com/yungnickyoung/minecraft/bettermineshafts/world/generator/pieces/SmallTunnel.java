package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.item.minecart.ChestMinecartEntity;
import net.minecraft.entity.item.minecart.TNTMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class SmallTunnel extends MineshaftPiece {
    private final List<Integer> supports = Lists.newLinkedList(); // local z coords
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 5,
        MAIN_AXIS_LEN = 8;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SmallTunnel(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, compoundTag);
        ListNBT listTag1 = compoundTag.getList("Supports", 3);
        for (int i = 0; i < listTag1.size(); ++i) {
            this.supports.add(listTag1.getInt(i));
        }
    }

    public SmallTunnel(int i, int chunkPieceLen, Random random, MutableBoundingBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SMALL_TUNNEL, i, chunkPieceLen, type);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
        ListNBT listTag1 = new ListNBT();
        supports.forEach(z -> listTag1.add(IntNBT.valueOf(z)));
        tag.put("Supports", listTag1);
    }

    public static MutableBoundingBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        MutableBoundingBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        Direction direction = this.getCoordBaseMode();
        if (direction == null) {
            return;
        }

        switch (direction) {
            case NORTH:
            default:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, direction, this.getComponentType(), pieceChainLen);
                break;
            case SOUTH:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ + 1, direction, this.getComponentType(), pieceChainLen);
                break;
            case WEST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ, direction, this.getComponentType(), pieceChainLen);
                break;
            case EAST:
                BetterMineshaftGenerator.generateAndAddSmallTunnelPiece(structurePiece, list, random, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, direction, this.getComponentType(), pieceChainLen);
        }

        buildSupports(random);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean create(IWorld world, ChunkGenerator<?> generator, Random random, MutableBoundingBox box, ChunkPos pos) {
        // Don't spawn pieces in oceans
        if (world.getBiome(new BlockPos(pos.x, 1, pos.z)).getCategory() == Biome.Category.OCEAN) {
            return false;
        }

        // Don't spawn if liquid in this box
        if (this.isLiquidInStructureBoundingBox(world, box)) {
            return false;
        }

        // Randomize blocks
        float chance =
            this.mineshaftType == BetterMineshaftStructure.Type.ICE
                || this.mineshaftType == BetterMineshaftStructure.Type.MUSHROOM
            ? .95f
            : .6f;
        this.chanceReplaceNonAir(world, box, random, chance, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, CAVE_AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());
        // Special case - mushroom mineshafts get mycelium in floor
        if (this.mineshaftType == BetterMineshaftStructure.Type.MUSHROOM)
            this.chanceReplaceNonAir(world, box, random, .8f, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, Blocks.MYCELIUM.getDefaultState());

        // Decorations
        this.supports.forEach(z -> generateSupport(world, box, random, z));
        generateRails(world, box, random);
        generateCobwebs(world, box, random);
        generateChestCarts(world, box, random, LootTables.CHESTS_ABANDONED_MINESHAFT);
        generateTntCarts(world, box, random);
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, getVineChance(), 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }

    private void generateCobwebs(IWorld world, MutableBoundingBox box, Random random) {
        supports.forEach(z -> {
            this.chanceReplaceAir(world, box, random, .15f, 1, 3, z - 3, 1, 3, z + 3, Blocks.COBWEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, .15f, 3, 3, z - 3, 3, 3, z + 3, Blocks.COBWEB.getDefaultState());
        });
    }

    private void generateChestCarts(IWorld world, MutableBoundingBox box, Random random, ResourceLocation lootTableId) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextInt(800) == 0) {
                BlockPos blockPos = new BlockPos(this.getXWithOffset(LOCAL_X_END / 2, z), this.getYWithOffset(1), this.getZWithOffset(LOCAL_X_END / 2, z));
                if (box.isVecInside(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    ChestMinecartEntity chestMinecartEntity = new ChestMinecartEntity(world.getWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(lootTableId, random.nextLong());
                    world.addEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateSupport(IWorld world, MutableBoundingBox box, Random random, int z) {
        this.fill(world, box, 1, 1, z, 1, 2, z, getSupportBlock());
        this.fill(world, box, 3, 1, z, 3, 2, z, getSupportBlock());
        this.fill(world, box, 1, 3, z, 3, 3, z, getMainBlock());
        BlockState supportBlock = getSupportBlock();
        if (this.mineshaftType != BetterMineshaftStructure.Type.ICE)
            supportBlock = getSupportBlock().with(WallBlock.WEST, true).with(WallBlock.EAST, true);
        this.chanceReplaceNonAir(world, box, random, .25f, 1, 3, z, 3, 3, z, supportBlock);
    }

    private void generateRails(IWorld world, MutableBoundingBox box, Random random) {
        // Place rails in center
        this.chanceFill(world, box,  random, .5f, 2, 1, 0, 2, 1, LOCAL_Z_END, Blocks.RAIL.getDefaultState());
        // Place powered rails
        for (int n = 0; n <= LOCAL_Z_END; n++) {
            this.chanceAddBlock(world, random, .07f, Blocks.POWERED_RAIL.getDefaultState().with(PoweredRailBlock.POWERED, true), 2, 1, n, box);
        }
    }

    private void generateTntCarts(IWorld world, MutableBoundingBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextInt(400) == 0) {
                BlockPos blockPos = new BlockPos(this.getXWithOffset(LOCAL_X_END / 2, z), this.getYWithOffset(1), this.getZWithOffset(LOCAL_X_END / 2, z));
                if (box.isVecInside(blockPos) && !world.getBlockState(blockPos.down()).isAir()) {
                    TNTMinecartEntity tntMinecartEntity = new TNTMinecartEntity(world.getWorld(), ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    world.addEntity(tntMinecartEntity);
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
