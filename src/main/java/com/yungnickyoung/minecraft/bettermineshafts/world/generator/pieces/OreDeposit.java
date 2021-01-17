package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OreDeposit extends MineshaftPiece {
    public enum OreType {
        COBBLE(0, Blocks.COBBLESTONE.getDefaultState(), Configuration.ores.cobble.get()),
        COAL(1, Blocks.COAL_ORE.getDefaultState(), Configuration.ores.coal.get() + COBBLE.threshold),
        IRON(2, Blocks.IRON_ORE.getDefaultState(), Configuration.ores.iron.get() + COAL.threshold),
        REDSTONE(3, Blocks.REDSTONE_ORE.getDefaultState(), Configuration.ores.redstone.get() + IRON.threshold),
        GOLD(4, Blocks.GOLD_ORE.getDefaultState(), Configuration.ores.gold.get() + REDSTONE.threshold),
        LAPIS(5, Blocks.LAPIS_ORE.getDefaultState(), Configuration.ores.lapis.get() + GOLD.threshold),
        EMERALD(6, Blocks.EMERALD_ORE.getDefaultState(), Configuration.ores.emerald.get() + LAPIS.threshold),
        DIAMOND(7, Blocks.DIAMOND_ORE.getDefaultState(), Configuration.ores.diamond.get() + EMERALD.threshold);

        private final int value;
        private final BlockState block;
        private int threshold;

        OreType(int value, BlockState block, int threshold) {
            this.value = value;
            this.block = block;
            this.threshold = threshold;
        }

        public static OreType valueOf(int value) {
            return Arrays.stream(values())
                .filter(oreType -> oreType.value == value)
                .findFirst().get();
        }

        public BlockState getBlock() {
            return this.block;
        }
    }

    private OreType oreType;
    private static final int
        SECONDARY_AXIS_LEN = 5,
        Y_AXIS_LEN = 5,
        MAIN_AXIS_LEN = 4;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public OreDeposit(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.ORE_DEPOSIT, compoundTag);
        this.oreType = OreType.valueOf(compoundTag.getInt("OreType"));
    }

    public OreDeposit(int i, int chunkPieceLen, Random random, MutableBoundingBox blockBox, Direction direction, MineshaftVariantSettings settings) {
        super(BetterMineshaftStructurePieceType.ORE_DEPOSIT, i, chunkPieceLen, settings);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
        tag.putInt("OreType", this.oreType.value);
    }

    public static MutableBoundingBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        MutableBoundingBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.findIntersecting(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        int r = random.nextInt(100);

        // Determine ore type
        for (OreType oreType : OreType.values()) {
            if (r < oreType.threshold) {
                this.oreType = oreType;
                break;
            }
        }

        // Double check sum to see if user messed up spawn chances
        if (OreType.DIAMOND.threshold != 100)
            BetterMineshafts.LOGGER.error("Your ore spawn chances don't add up to 100! Ores won't spawn as you intend!");
        if (this.oreType == null)
            this.oreType = OreType.COBBLE;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean func_230383_a_(ISeedReader world, StructureManager structureManager, ChunkGenerator generator, Random random, MutableBoundingBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isLiquidInStructureBoundingBox(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

        BlockState COBBLE = Blocks.COBBLESTONE.getDefaultState();
        BlockState ORE_BLOCK = this.oreType.getBlock();

        // Fill with cobble
        this.chanceReplaceNonAir(world, box, random, .9f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, COBBLE);

        // Ore deposit. Ore is more dense in center than edges
        this.chanceReplaceNonAir(world, box, random, .65f, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, ORE_BLOCK);
        this.chanceReplaceNonAir(world, box, random, .15f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, ORE_BLOCK);

        return true;
    }
}
