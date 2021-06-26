package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.yungsapi.world.BoundingBoxHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
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

import java.util.Arrays;
import java.util.Random;

public class OreDeposit extends MineshaftPiece {
    public enum OreType {
        COBBLE(0, Blocks.COBBLESTONE.getDefaultState(), BetterMineshafts.CONFIG.ores.cobble),
        COAL(1, Blocks.COAL_ORE.getDefaultState(), BetterMineshafts.CONFIG.ores.coal + COBBLE.threshold),
        IRON(2, Blocks.IRON_ORE.getDefaultState(), BetterMineshafts.CONFIG.ores.iron + COAL.threshold),
        REDSTONE(3, Blocks.REDSTONE_ORE.getDefaultState(), BetterMineshafts.CONFIG.ores.redstone + IRON.threshold),
        GOLD(4, Blocks.GOLD_ORE.getDefaultState(), BetterMineshafts.CONFIG.ores.gold + REDSTONE.threshold),
        LAPIS(5, Blocks.LAPIS_ORE.getDefaultState(), BetterMineshafts.CONFIG.ores.lapis + GOLD.threshold),
        EMERALD(6, Blocks.EMERALD_ORE.getDefaultState(), BetterMineshafts.CONFIG.ores.emerald + LAPIS.threshold),
        DIAMOND(7, Blocks.DIAMOND_ORE.getDefaultState(), BetterMineshafts.CONFIG.ores.diamond + EMERALD.threshold);

        private final int value;
        private final BlockState block;
        private final int threshold;

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

    public OreDeposit(ServerWorld world, NbtCompound compoundTag) {
        super(BetterMineshaftStructurePieceType.ORE_DEPOSIT, compoundTag);
        this.oreType = OreType.valueOf(compoundTag.getInt("OreType"));
    }

    public OreDeposit(int chunkPieceLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.ORE_DEPOSIT, chunkPieceLen, type, blockBox);
        this.setOrientation(direction);
    }

    @Override
    protected void writeNbt(ServerWorld world, NbtCompound tag) {
        super.writeNbt(world, tag);
        tag.putInt("OreType", this.oreType.value);
    }

    public static BlockBox determineBoxPosition(StructurePiecesHolder structurePiecesHolder, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = BoundingBoxHelper.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = structurePiecesHolder.getIntersecting(blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void fillOpenings(StructurePiece structurePiece, StructurePiecesHolder structurePiecesHolder, Random random) {
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
    public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator generator, Random random, BlockBox box, ChunkPos pos, BlockPos blockPos) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isTouchingLiquid(world, box)) return false;
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
