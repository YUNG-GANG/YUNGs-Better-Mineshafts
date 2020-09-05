package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.google.common.collect.Lists;
import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.integration.Integrations;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftGenerator;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

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

    public SmallTunnel() {}

    public SmallTunnel(int i, int chunkPieceLen, Random random, StructureBoundingBox blockBox, EnumFacing direction, MineshaftVariantSettings settings) {
        super(i, chunkPieceLen, settings);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void writeStructureToNBT(NBTTagCompound tag) {
        super.writeStructureToNBT(tag);
        NBTTagList listTag1 = new NBTTagList();
        supports.forEach(z -> listTag1.appendTag(new NBTTagInt(z)));
        tag.setTag("Supports", listTag1);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readStructureFromNBT(NBTTagCompound tag, TemplateManager templateManager) {
        super.readStructureFromNBT(tag, templateManager);
        NBTTagList listTag1 = tag.getTagList("Supports", 3);
        for (int i = 0; i < listTag1.tagCount(); ++i) {
            this.supports.add(listTag1.getIntAt(i));
        }
    }

    public static StructureBoundingBox determineBoxPosition(List<StructureComponent> list, Random random, int x, int y, int z, EnumFacing direction) {
        StructureBoundingBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructureComponent intersectingPiece = StructureComponent.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void buildComponent(StructureComponent structurePiece, List<StructureComponent> list, Random random) {
        EnumFacing direction = this.getCoordBaseMode();
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
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isLiquidInStructureBoundingBox(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        // Randomize blocks
        this.chanceReplaceNonAir(world, box, random, settings.replacementRate, 0, 1, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getMainSelector());

        // Randomize floor
        this.chanceReplaceNonAir(world, box, random, settings.replacementRate, 0, 0, 0, LOCAL_X_END, 0, LOCAL_Z_END, getFloorSelector());

        // Fill with air
        this.fill(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR);

        // Fill in any air in floor with main block
        this.replaceAir(world, box, 1, 0, 0, LOCAL_X_END - 1, 0, LOCAL_Z_END, getMainBlock());

        // Decorations
        generateSupports(world, box, random);
        generateRails(world, box, random);
        generateCobwebs(world, box, random);
        generateChestCarts(world, box, random, LootTableList.CHESTS_ABANDONED_MINESHAFT);
        generateTntCarts(world, box, random);
        generateTorches(world, box, random);
        this.addBiomeDecorations(world, box, random, 1, 0, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END);
        this.addVines(world, box, random, 1, 0, 1, LOCAL_X_END - 1, LOCAL_Y_END, LOCAL_Z_END - 1);

        return true;
    }

    private void generateCobwebs(World world, StructureBoundingBox box, Random random) {
        supports.forEach(z -> {
            this.chanceReplaceAir(world, box, random, Configuration.spawnRates.cobwebSpawnRate, 1, 3, z - 1, 1, 3, z + 1, Blocks.WEB.getDefaultState());
            this.chanceReplaceAir(world, box, random, Configuration.spawnRates.cobwebSpawnRate, 3, 3, z - 1, 3, 3, z + 1, Blocks.WEB.getDefaultState());
        });
    }

    private void generateChestCarts(World world, StructureBoundingBox box, Random random, ResourceLocation lootTableId) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < Configuration.spawnRates.smallShaftChestMinecartSpawnRate) {
                BlockPos blockPos = new BlockPos(this.getXWithOffset(LOCAL_X_END / 2, z), this.getYWithOffset(1), this.getZWithOffset(LOCAL_X_END / 2, z));
                if (box.isVecInside(blockPos) && world.getBlockState(blockPos.down()) != AIR) {
                    EntityMinecartChest chestMinecartEntity = new EntityMinecartChest(world, ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    chestMinecartEntity.setLootTable(lootTableId, random.nextLong());
                    world.spawnEntity(chestMinecartEntity);
                }
            }
        }
    }

    private void generateSupports(World world, StructureBoundingBox box, Random random) {
        IBlockState supportBlock = getSupportBlock();
        if (supportBlock.getBlock() instanceof BlockFence) {
            supportBlock = supportBlock.withProperty(BlockFence.WEST, true).withProperty(BlockFence.EAST, true);
        } else if (supportBlock.getBlock() instanceof BlockWall) {
            supportBlock = supportBlock.withProperty(BlockWall.WEST, true).withProperty(BlockWall.EAST, true);
        }

        for (int z : this.supports) {
            this.fill(world, box, 1, 1, z, 1, 2, z, getSupportBlock());
            this.fill(world, box, 3, 1, z, 3, 2, z, getSupportBlock());
            this.fill(world, box, 1, 3, z, 3, 3, z, getMainBlock());
            this.chanceReplaceNonAir(world, box, random, .25f, 1, 3, z, 3, 3, z, supportBlock);
        }
    }

    private void generateRails(World world, StructureBoundingBox box, Random random) {
        // Place rails in center
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < .3f) {
                this.fill(world, box, LOCAL_X_END / 2, 1, z, LOCAL_X_END / 2, 1, z + 1, Blocks.RAIL.getDefaultState());
                z++;
            }
        }
    }

    private void generateTntCarts(World world, StructureBoundingBox box, Random random) {
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (random.nextFloat() < Configuration.spawnRates.smallShaftTntMinecartSpawnRate) {
                BlockPos blockPos = new BlockPos(this.getXWithOffset(LOCAL_X_END / 2, z), this.getYWithOffset(1), this.getZWithOffset(LOCAL_X_END / 2, z));
                if (box.isVecInside(blockPos) && world.getBlockState(blockPos.down()) != AIR) {
                    EntityMinecartTNT tntMinecartEntity = new EntityMinecartTNT(world, ((float) blockPos.getX() + 0.5F), ((float) blockPos.getY() + 0.5F), ((float) blockPos.getZ() + 0.5F));
                    world.spawnEntity(tntMinecartEntity);
                }
            }
        }
    }

    private void generateTorches(World world, StructureBoundingBox box, Random random) {
        IBlockState leftTorch = Integrations.getLeftTorch(random);
        IBlockState rightTorch = Integrations.getRightTorch(random);
        float r;
        for (int z = 0; z <= LOCAL_Z_END; z++) {
            if (this.supports.contains(z)) continue;
            r = random.nextFloat();
            if (r < Configuration.spawnRates.torchSpawnRate / 2) {
                if (this.getBlockStateFromPos(world, 0, 2, z, box) != Blocks.AIR.getDefaultState()) {
                    this.replaceAir(world, box, 1, 2, z, 1, 2, z, leftTorch);
                }
            } else if (r < Configuration.spawnRates.torchSpawnRate) {
                if (this.getBlockStateFromPos(world, LOCAL_X_END, 2, z, box) != Blocks.AIR.getDefaultState()) {
                    this.replaceAir(world, box, LOCAL_X_END - 1, 2, z, LOCAL_X_END - 1, 2, z, rightTorch);
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
