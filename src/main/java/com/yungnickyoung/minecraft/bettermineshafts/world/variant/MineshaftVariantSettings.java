package com.yungnickyoung.minecraft.bettermineshafts.world.variant;

import com.yungnickyoung.minecraft.yungsapi.world.BlockSetSelector;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;

public class MineshaftVariantSettings {
    public MineshaftVariantSettings() {}

    public List<List<BiomeDictionary.Type>> biomeTags;
    public BlockSetSelector mainSelector;
    public BlockSetSelector floorSelector;
    public BlockSetSelector brickSelector;
    public BlockSetSelector legSelector;
    public BlockState mainBlock;
    public BlockState supportBlock;
    public BlockState slabBlock;
    public BlockState gravelBlock;
    public BlockState stoneWallBlock;
    public BlockState stoneSlabBlock;
    public BlockState trapdoorBlock;
    public BlockState smallLegBlock;
    public float vineChance;
    public float snowChance;
    public float cactusChance;
    public float deadBushChance;
    public float mushroomChance;
    public int legVariant;
    public boolean flammableLegs;
    public boolean lushDecorations;
    public float replacementRate;

    /** Builder-style setters to make it more obvious which settings are being set when creating a new object **/

    public MineshaftVariantSettings setBiomeTags(List<List<BiomeDictionary.Type>> biomeTags) {
        this.biomeTags = biomeTags;
        return this;
    }

    public MineshaftVariantSettings setMainSelector(BlockSetSelector mainSelector) {
        this.mainSelector = mainSelector;
        if (this.mainBlock != null) this.mainSelector.setDefaultBlockState(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setFloorSelector(BlockSetSelector floorSelector) {
        this.floorSelector = floorSelector;
        if (this.mainBlock != null) this.floorSelector.setDefaultBlockState(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setBrickSelector(BlockSetSelector brickSelector) {
        this.brickSelector = brickSelector;
        if (this.mainBlock != null) this.brickSelector.setDefaultBlockState(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setLegSelector(BlockSetSelector legSelector) {
        this.legSelector = legSelector;
        if (this.mainBlock != null) this.legSelector.setDefaultBlockState(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setMainBlock(BlockState mainBlock) {
        this.mainBlock = mainBlock;
        if (this.mainSelector != null) this.mainSelector.setDefaultBlockState(mainBlock);
        if (this.floorSelector != null) this.floorSelector.setDefaultBlockState(mainBlock);
        if (this.brickSelector != null) this.brickSelector.setDefaultBlockState(mainBlock);
        if (this.legSelector != null) this.legSelector.setDefaultBlockState(mainBlock);
        return this;
    }

    public MineshaftVariantSettings setSupportBlock(BlockState supportBlock) {
        this.supportBlock = supportBlock;
        return this;
    }

    public MineshaftVariantSettings setSlabBlock(BlockState slabBlock) {
        this.slabBlock = slabBlock;
        return this;
    }

    public MineshaftVariantSettings setGravelBlock(BlockState gravelBlock) {
        this.gravelBlock = gravelBlock;
        return this;
    }

    public MineshaftVariantSettings setStoneWallBlock(BlockState stoneWallBlock) {
        this.stoneWallBlock = stoneWallBlock;
        return this;
    }

    public MineshaftVariantSettings setStoneSlabBlock(BlockState stoneSlabBlock) {
        this.stoneSlabBlock = stoneSlabBlock;
        return this;
    }

    public MineshaftVariantSettings setTrapdoorBlock(BlockState trapdoorBlock) {
        this.trapdoorBlock = trapdoorBlock;
        return this;
    }

    public MineshaftVariantSettings setSmallLegBlock(BlockState smallLegBlock) {
        this.smallLegBlock = smallLegBlock;
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

    public MineshaftVariantSettings setLegVariant(int legVariant) {
        this.legVariant = legVariant;
        return this;
    }

    public MineshaftVariantSettings setFlammableLegs(boolean flammableLegs) {
        this.flammableLegs = flammableLegs;
        return this;
    }

    public MineshaftVariantSettings setLushDecorations(boolean lushDecorations) {
        this.lushDecorations = lushDecorations;
        return this;
    }

    public MineshaftVariantSettings setReplacementRate(float replacementRate) {
        this.replacementRate = replacementRate;
        return this;
    }
}
