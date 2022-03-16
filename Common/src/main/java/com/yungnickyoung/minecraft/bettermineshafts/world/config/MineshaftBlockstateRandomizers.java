package com.yungnickyoung.minecraft.bettermineshafts.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.world.BlockStateRandomizer;

public class MineshaftBlockstateRandomizers {
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
