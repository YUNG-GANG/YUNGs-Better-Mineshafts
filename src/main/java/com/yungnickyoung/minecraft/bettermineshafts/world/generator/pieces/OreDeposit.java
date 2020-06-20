package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import com.yungnickyoung.minecraft.bettermineshafts.util.BoxUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
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
        private final IBlockState block;

        OreType(int value, IBlockState block) {
            this.value = value;
            this.block = block;
        }

        public static OreType valueOf(int value) {
            return Arrays.stream(values())
                .filter(oreType -> oreType.value == value)
                .findFirst().get();
        }

        public IBlockState getBlock() {
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

    public OreDeposit(int i, int chunkPieceLen, Random random, StructureBoundingBox blockBox, EnumFacing direction, MapGenBetterMineshaft.Type type) {
        super(i, chunkPieceLen, type);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void writeStructureToNBT(NBTTagCompound tag) {
        super.writeStructureToNBT(tag);
        tag.setInteger("OreType", this.oreType.value);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readStructureFromNBT(NBTTagCompound tag, TemplateManager templateManager) {
        super.readStructureFromNBT(tag, templateManager);
        this.oreType = OreType.valueOf(tag.getInteger("OreType"));
    }

    public static StructureBoundingBox determineBoxPosition(List<StructureComponent> list, Random random, int x, int y, int z, EnumFacing direction) {
        StructureBoundingBox blockBox = BoxUtil.boxFromCoordsWithRotation(x, y, z, SECONDARY_AXIS_LEN, Y_AXIS_LEN, MAIN_AXIS_LEN, direction);

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructureComponent intersectingPiece = StructureComponent.findIntersecting(list, blockBox); // findIntersecting

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructureComponent structurePiece, List<StructureComponent> list, Random random) {
        float r = random.nextFloat();

        if (r < .5f) {
            this.oreType = OreType.COBBLE; // Chance of cobble instead of ore
        } else if (r <= .7f)
            this.oreType = OreType.COAL;
        else if (r <= .79f)
            this.oreType = OreType.IRON;
        else if (r <= .86f)
            this.oreType = OreType.REDSTONE;
        else if (r <= .93f)
            this.oreType = OreType.GOLD;
        else if (r <= .965f)
            this.oreType = OreType.LAPIS;
        else if (r <= .99f)
            this.oreType = OreType.EMERALD;
        else
            this.oreType = OreType.DIAMOND;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        // Don't spawn if liquid in this box
        if (this.isLiquidInStructureBoundingBox(world, box)) {
            return false;
        }

        IBlockState COBBLE = Blocks.COBBLESTONE.getDefaultState();
        IBlockState ORE_BLOCK = this.oreType.getBlock();

        // Fill with cobble
        this.chanceReplaceNonAir(world, box, random, .9f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, COBBLE);

        // Ore deposit. Ore is more dense in center than edges
        this.chanceReplaceNonAir(world, box, random, .65f, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, ORE_BLOCK);
        this.chanceReplaceNonAir(world, box, random, .15f, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, ORE_BLOCK);

        return true;
    }
}
