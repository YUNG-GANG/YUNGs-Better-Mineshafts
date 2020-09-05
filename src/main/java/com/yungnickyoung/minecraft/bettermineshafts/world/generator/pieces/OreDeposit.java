package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
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
        COBBLE(0, Blocks.COBBLESTONE.getDefaultState(), Configuration.ores.cobble),
        COAL(1, Blocks.COAL_ORE.getDefaultState(), Configuration.ores.coal + COBBLE.threshold),
        IRON(2, Blocks.IRON_ORE.getDefaultState(), Configuration.ores.iron + COAL.threshold),
        REDSTONE(3, Blocks.REDSTONE_ORE.getDefaultState(), Configuration.ores.redstone + IRON.threshold),
        GOLD(4, Blocks.GOLD_ORE.getDefaultState(), Configuration.ores.gold + REDSTONE.threshold),
        LAPIS(5, Blocks.LAPIS_ORE.getDefaultState(), Configuration.ores.lapis + GOLD.threshold),
        EMERALD(6, Blocks.EMERALD_ORE.getDefaultState(), Configuration.ores.emerald + LAPIS.threshold),
        DIAMOND(7, Blocks.DIAMOND_ORE.getDefaultState(), Configuration.ores.diamond + EMERALD.threshold);

        private final int value;
        private final IBlockState block;
        private int threshold;

        OreType(int value, IBlockState block, int threshold) {
            this.value = value;
            this.block = block;
            this.threshold = threshold;
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

    public OreDeposit() {
    }

    public OreDeposit(int i, int chunkPieceLen, Random random, StructureBoundingBox blockBox, EnumFacing direction, MineshaftVariantSettings settings) {
        super(i, chunkPieceLen, settings);
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
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        // Don't spawn if liquid in this box or if in ocean biome
        if (this.isLiquidInStructureBoundingBox(world, box)) return false;
        if (this.isInOcean(world, 0, 0) || this.isInOcean(world, LOCAL_X_END, LOCAL_Z_END)) return false;

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
