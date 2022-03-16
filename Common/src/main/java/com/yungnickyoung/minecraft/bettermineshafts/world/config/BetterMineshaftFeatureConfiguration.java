package com.yungnickyoung.minecraft.bettermineshafts.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructureFeature;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class BetterMineshaftFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<BetterMineshaftFeatureConfiguration> CODEC = RecordCodecBuilder.create((instance) -> instance
            .group(
                    Codec.BOOL.fieldOf("flammableLegs").forGetter((config) -> config.flammableLegs),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("replacementRate").forGetter((config) -> config.replacementRate),
                    BetterMineshaftStructureFeature.LegVariant.CODEC.fieldOf("legVariant").forGetter((config) -> config.legVariant),
                    MineshaftDecorationChances.CODEC.fieldOf("decorationChances").forGetter((config) -> config.decorationChances),
                    MineshaftBlockStates.CODEC.fieldOf("blockStates").forGetter((config) -> config.blockStates),
                    MineshaftBlockstateRandomizers.CODEC.fieldOf("blockStateRandomizers").forGetter((config) -> config.blockStateRandomizers))
            .apply(instance, BetterMineshaftFeatureConfiguration::new));

    public boolean flammableLegs;
    public float replacementRate;
    public BetterMineshaftStructureFeature.LegVariant legVariant;
    public MineshaftDecorationChances decorationChances;
    public MineshaftBlockStates blockStates;
    public MineshaftBlockstateRandomizers blockStateRandomizers;

    public BetterMineshaftFeatureConfiguration(boolean flammableLegs, float replacementRate,
                                               BetterMineshaftStructureFeature.LegVariant legVariant,
                                               MineshaftDecorationChances decorationChances,
                                               MineshaftBlockStates blockStates,
                                               MineshaftBlockstateRandomizers blockStateRandomizers) {
        this.flammableLegs = flammableLegs;
        this.replacementRate = replacementRate;
        this.legVariant = legVariant;
        this.decorationChances = decorationChances;
        this.blockStates = blockStates;
        this.blockStateRandomizers = blockStateRandomizers;
    }

    public static class MineshaftDecorationChances {
        public static final Codec<MineshaftDecorationChances> CODEC = RecordCodecBuilder.create((instance) -> instance
                .group(
                        Codec.floatRange(0.0F, 1.0F).fieldOf("vineChance").forGetter((config) -> config.vineChance),
                        Codec.floatRange(0.0F, 1.0F).fieldOf("snowChance").forGetter((config) -> config.snowChance),
                        Codec.floatRange(0.0F, 1.0F).fieldOf("cactusChance").forGetter((config) -> config.cactusChance),
                        Codec.floatRange(0.0F, 1.0F).fieldOf("deadBushChance").forGetter((config) -> config.deadBushChance),
                        Codec.floatRange(0.0F, 1.0F).fieldOf("mushroomChance").forGetter((config) -> config.mushroomChance),
                        Codec.BOOL.fieldOf("lushDecorations").forGetter((config) -> config.lushDecorations))
                .apply(instance, MineshaftDecorationChances::new));

        public float vineChance;
        public float snowChance;
        public float cactusChance;
        public float deadBushChance;
        public float mushroomChance;
        public boolean lushDecorations;

        public MineshaftDecorationChances(float vineChance, float snowChance, float cactusChance, float deadBushChance,
                                          float mushroomChance, boolean lushDecorations) {
            this.vineChance = vineChance;
            this.snowChance = snowChance;
            this.cactusChance = cactusChance;
            this.deadBushChance = deadBushChance;
            this.mushroomChance = mushroomChance;
            this.lushDecorations = lushDecorations;
        }
    }

    public static class MineshaftBlockStates {
        public static final Codec<MineshaftBlockStates> CODEC = RecordCodecBuilder.create((instance) -> instance
                .group(
                        BlockState.CODEC.fieldOf("mainBlockState").forGetter((config) -> config.mainBlockState),
                        BlockState.CODEC.fieldOf("supportBlockState").forGetter((config) -> config.supportBlockState),
                        BlockState.CODEC.fieldOf("slabBlockState").forGetter((config) -> config.slabBlockState),
                        BlockState.CODEC.fieldOf("gravelBlockState").forGetter((config) -> config.gravelBlockState),
                        BlockState.CODEC.fieldOf("stoneWallBlockState").forGetter((config) -> config.stoneWallBlockState),
                        BlockState.CODEC.fieldOf("stoneSlabBlockState").forGetter((config) -> config.stoneSlabBlockState),
                        BlockState.CODEC.fieldOf("trapdoorBlockState").forGetter((config) -> config.trapdoorBlockState),
                        BlockState.CODEC.fieldOf("smallLegBlockState").forGetter((config) -> config.smallLegBlockState))
                .apply(instance, MineshaftBlockStates::new));

        public BlockState mainBlockState;
        public BlockState supportBlockState;
        public BlockState slabBlockState;
        public BlockState gravelBlockState;
        public BlockState stoneWallBlockState;
        public BlockState stoneSlabBlockState;
        public BlockState trapdoorBlockState;
        public BlockState smallLegBlockState;

        public MineshaftBlockStates(BlockState mainBlockState, BlockState supportBlockState, BlockState slabBlockState, BlockState gravelBlockState,
                                    BlockState stoneWallBlockState, BlockState stoneSlabBlockState, BlockState trapdoorBlockState, BlockState smallLegBlockState) {
            this.mainBlockState = mainBlockState;
            this.supportBlockState = supportBlockState;
            this.slabBlockState = slabBlockState;
            this.gravelBlockState = gravelBlockState;
            this.stoneWallBlockState = stoneWallBlockState;
            this.stoneSlabBlockState = stoneSlabBlockState;
            this.trapdoorBlockState = trapdoorBlockState;
            this.smallLegBlockState = smallLegBlockState;
        }
    }

    public static class MineshaftBlockstateRandomizers {
        public static final Codec<MineshaftBlockstateRandomizers> CODEC = RecordCodecBuilder.create((instance) -> instance
                .group(
                        BlockStateRandomizer.CODEC.fieldOf("mainRandomizer").forGetter((config) -> config.mainRandomizer),
                        BlockStateRandomizer.CODEC.fieldOf("floorRandomizer").forGetter((config) -> config.floorRandomizer),
                        BlockStateRandomizer.CODEC.fieldOf("brickRandomizer").forGetter((config) -> config.brickRandomizer),
                        BlockStateRandomizer.CODEC.fieldOf("legRandomizer").forGetter((config) -> config.legRandomizer))
                .apply(instance, MineshaftBlockstateRandomizers::new));

        public BlockStateRandomizer mainRandomizer;
        public BlockStateRandomizer floorRandomizer;
        public BlockStateRandomizer brickRandomizer;
        public BlockStateRandomizer legRandomizer;

        public MineshaftBlockstateRandomizers(BlockStateRandomizer mainRandomizer, BlockStateRandomizer floorRandomizer,
                                              BlockStateRandomizer brickRandomizer, BlockStateRandomizer legRandomizer) {
            this.mainRandomizer = mainRandomizer;
            this.floorRandomizer = floorRandomizer;
            this.brickRandomizer = brickRandomizer;
            this.legRandomizer = legRandomizer;
        }
    }
}
