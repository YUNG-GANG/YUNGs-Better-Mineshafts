package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OreDeposit extends MineshaftPart {
    public enum OreType {
        GOLD(0),
        IRON(1),
        COAL(2),
        LAPIS(3),
        REDSTONE(4),
        EMERALD(5),
        DIAMOND(6);

        private final int value;

        OreType(int value) {
            this.value = value;
        }

        public static OreType valueOf(int value) {
            return Arrays.stream(values())
                .filter(dir -> dir.value == value)
                .findFirst().get();
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

    public OreDeposit(StructureManager structureManager, CompoundTag compoundTag) {
        super(BetterMineshaftStructurePieceType.ORE_DEPOSIT, compoundTag);
        this.oreType = OreType.valueOf(compoundTag.getInt("OreType"));
    }

    public OreDeposit(int i, int chunkPieceLen, Random random, BlockBox blockBox, Direction direction, BetterMineshaftFeature.Type type) {
        super(BetterMineshaftStructurePieceType.ORE_DEPOSIT, i, chunkPieceLen, type);
        this.setOrientation(direction);
        this.boundingBox = blockBox;
    }

    @Override
    protected void toNbt(CompoundTag tag) {
        super.toNbt(tag);
        tag.putInt("OreType", this.oreType.value);
    }

    public static BlockBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        BlockBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.method_14932(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void method_14918(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
        int r = random.nextInt(100);
        if (r <= 50)
            this.oreType = OreType.COAL;
        else if (r <= 70)
            this.oreType = OreType.IRON;
        else if (r <= 80)
            this.oreType = OreType.REDSTONE;
        else if (r <= 90)
            this.oreType = OreType.GOLD;
        else if (r <= 95)
            this.oreType = OreType.LAPIS;
        else if (r <= 98)
            this.oreType = OreType.EMERALD;
        else
            this.oreType = OreType.DIAMOND;
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        BlockState ORE_BLOCK;
        if (this.oreType == OreType.GOLD)
            ORE_BLOCK = Blocks.GOLD_ORE.getDefaultState();
        else if (this.oreType == OreType.COAL)
            ORE_BLOCK = Blocks.COAL_ORE.getDefaultState();
        else if (this.oreType == OreType.IRON)
            ORE_BLOCK = Blocks.IRON_ORE.getDefaultState();
        else if (this.oreType == OreType.LAPIS)
            ORE_BLOCK = Blocks.LAPIS_ORE.getDefaultState();
        else if (this.oreType == OreType.REDSTONE)
            ORE_BLOCK = Blocks.REDSTONE_ORE.getDefaultState();
        else if (this.oreType == OreType.EMERALD)
            ORE_BLOCK = Blocks.EMERALD_ORE.getDefaultState();
        else
            ORE_BLOCK = Blocks.DIAMOND_ORE.getDefaultState();

        // Fill with air
        this.fillWithOutline(world, box, 1, 1, 0, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END, AIR, AIR, false);

        // Ore deposit
        this.randomFillWithOutline(world, box, random,.8f, 1, 1, 2, 3, 3, 3, ORE_BLOCK, ORE_BLOCK, false);
        this.fillWithOutline(world, box, 1, 1, 1, 1, 1 + random.nextInt(3), 1, ORE_BLOCK, ORE_BLOCK, false);
        this.fillWithOutline(world, box, 2, 1, 1, 2, 1 + random.nextInt(3), 1, ORE_BLOCK, ORE_BLOCK, false);
        this.fillWithOutline(world, box, 3, 1, 1, 3, 1 + random.nextInt(3), 1, ORE_BLOCK, ORE_BLOCK, false);
        this.addBlockWithRandomThreshold(world, box, random, .5f, 1, 1, 0, ORE_BLOCK);
        this.addBlockWithRandomThreshold(world, box, random, .5f, 2, 1, 0, ORE_BLOCK);
        this.addBlockWithRandomThreshold(world, box, random, .5f, 3, 1, 0, ORE_BLOCK);

        // Ceiling
        this.fillWithOutline(world, box, 1, 4, 2, 3, 4, 3, ORE_BLOCK, ORE_BLOCK, true);
        this.randomFillWithOutline(world, box, random, .5f,1, 4, 1, 3, 4, 1, ORE_BLOCK, ORE_BLOCK, true);

        return true;
    }
}
