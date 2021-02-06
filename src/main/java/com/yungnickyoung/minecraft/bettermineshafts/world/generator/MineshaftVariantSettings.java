package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.util.BlockSetSelector;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;

public class MineshaftVariantSettings {
    public MineshaftVariantSettings() {}

    public List<List<BiomeDictionary.Type>> biomeTags;
    public BlockSetSelector mainSelector;
    public BlockSetSelector floorSelector;
    public BlockSetSelector brickSelector;
    public BlockSetSelector legSelector;
    public IBlockState mainBlock;
    public IBlockState supportBlock;
    public IBlockState slabBlock;
    public IBlockState gravelBlock;
    public IBlockState stoneWallBlock;
    public IBlockState stoneSlabBlock;
    public int minY;
    public int maxY;
    public float vineChance;
    public float snowChance;
    public float cactusChance;
    public float deadBushChance;
    public float mushroomChance;
    public int legVariant;
    public float replacementRate;

    /** Builder-style setters to make it more obvious which settings are being set when creating a new object **/

    public MineshaftVariantSettings setBiomeTags(List<List<BiomeDictionary.Type>> biomeTags) {
        this.biomeTags = biomeTags;
        return this;
    }

    public MineshaftVariantSettings setMainSelector(BlockSetSelector mainSelector) {
        this.mainSelector = mainSelector;
        if (this.mainBlock != null) this.mainSelector.setDefaultBlock(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setFloorSelector(BlockSetSelector floorSelector) {
        this.floorSelector = floorSelector;
        if (this.mainBlock != null) this.floorSelector.setDefaultBlock(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setBrickSelector(BlockSetSelector brickSelector) {
        this.brickSelector = brickSelector;
        if (this.mainBlock != null) this.brickSelector.setDefaultBlock(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setLegSelector(BlockSetSelector legSelector) {
        this.legSelector = legSelector;
        if (this.mainBlock != null) this.legSelector.setDefaultBlock(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setMainBlock(IBlockState mainBlock) {
        this.mainBlock = mainBlock;
        if (this.mainSelector != null) this.mainSelector.setDefaultBlock(mainBlock);
        if (this.floorSelector != null) this.floorSelector.setDefaultBlock(mainBlock);
        if (this.brickSelector != null) this.brickSelector.setDefaultBlock(mainBlock);
        if (this.legSelector != null) this.legSelector.setDefaultBlock(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setSupportBlock(IBlockState supportBlock) {
        this.supportBlock = supportBlock;
        return this;
    }

    public MineshaftVariantSettings setSlabBlock(IBlockState slabBlock) {
        this.slabBlock = slabBlock;
        return this;
    }

    public MineshaftVariantSettings setGravelBlock(IBlockState gravelBlock) {
        this.gravelBlock = gravelBlock;
        return this;
    }

    public MineshaftVariantSettings setStoneWallBlock(IBlockState stoneWallBlock) {
        this.stoneWallBlock = stoneWallBlock;
        return this;
    }

    public MineshaftVariantSettings setStoneSlabBlock(IBlockState stoneSlabBlock) {
        this.stoneSlabBlock = stoneSlabBlock;
        return this;
    }

    public MineshaftVariantSettings setMinY(int minY) {
        this.minY = minY;
        return this;
    }

    public MineshaftVariantSettings setMaxY(int maxY) {
        this.maxY = maxY;
        return this;
    }

    public MineshaftVariantSettings setLegVariant(int legVariant) {
        this.legVariant = legVariant;
        return this;
    }

    public MineshaftVariantSettings setVineChance(float vineChance) {
        this.vineChance = vineChance;
        return this;
    }

    public MineshaftVariantSettings setSnowChance(float snowChance) {
        this.snowChance = snowChance;
        return this;
    }

    public MineshaftVariantSettings setCactusChance(float cactusChance) {
        this.cactusChance = cactusChance;
        return this;
    }

    public MineshaftVariantSettings setDeadBushChance(float deadBushChance) {
        this.deadBushChance = deadBushChance;
        return this;
    }

    public MineshaftVariantSettings setMushroomChance(float mushroomChance) {
        this.mushroomChance = mushroomChance;
        return this;
    }

    public MineshaftVariantSettings setReplacementRate(float replacementRate) {
        this.replacementRate = replacementRate;
        return this;
    }
}
