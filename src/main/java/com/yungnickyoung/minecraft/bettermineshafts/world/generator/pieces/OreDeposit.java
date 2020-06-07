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

public class OreDeposit extends MineshaftPiece {
    public enum OreType {
        GOLD(0, Blocks.GOLD_ORE.getDefaultState()),
        IRON(1, Blocks.IRON_ORE.getDefaultState()),
        COAL(2, Blocks.COAL_ORE.getDefaultState()),
        LAPIS(3, Blocks.LAPIS_ORE.getDefaultState()),
        REDSTONE(4, Blocks.REDSTONE_ORE.getDefaultState()),
        EMERALD(5, Blocks.EMERALD_ORE.getDefaultState()),
        DIAMOND(6, Blocks.DIAMOND_ORE.getDefaultState()),
        COBBLE(7, Blocks.COBBLESTONE.getDefaultState());

        private final int value;
        private final BlockState block;

        OreType(int value, BlockState block) {
            this.value = value;
            this.block = block;
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
        float r = random.nextFloat();

        if (r < .3f) {
            this.oreType = OreType.COBBLE; // Chance of cobble instead of ore
        } else if (r <= .65f)
            this.oreType = OreType.COAL;
        else if (r <= .79f)
            this.oreType = OreType.IRON;
        else if (r <= .86f)
            this.oreType = OreType.REDSTONE;
        else if (r <= .93f)
            this.oreType = OreType.GOLD;
        else if (r <= .965f)
            this.oreType = OreType.LAPIS;
        else if (r <= .986f)
            this.oreType = OreType.EMERALD;
        else
            this.oreType = OreType.DIAMOND;
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<?> generator, Random random, BlockBox box, ChunkPos pos) {
        if (this.method_14937(world, box)) {
            return false;
        }

        BlockState COBBLE = Blocks.COBBLESTONE.getDefaultState();
        BlockState ORE_BLOCK = this.oreType.getBlock();

        // Fill with cobble
        this.randomReplaceNonAir(world, box, random, .9f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, COBBLE);

        // Ore deposit. Ore is more dense in center than edges
        this.randomReplaceNonAir(world, box, random, .75f, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, ORE_BLOCK);
        this.randomReplaceNonAir(world, box, random, .25f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, ORE_BLOCK);

        return true;
    }
}
