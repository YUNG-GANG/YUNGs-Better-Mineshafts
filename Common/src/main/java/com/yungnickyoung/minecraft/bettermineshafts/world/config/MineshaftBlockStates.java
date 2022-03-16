package com.yungnickyoung.minecraft.bettermineshafts.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

public class MineshaftBlockStates {
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
