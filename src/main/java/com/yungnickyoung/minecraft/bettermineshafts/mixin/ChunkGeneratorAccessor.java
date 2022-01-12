package com.yungnickyoung.minecraft.bettermineshafts.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.StructureSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ChunkGenerator.class)
public interface ChunkGeneratorAccessor {
    @Invoker("codec")
    Codec<ChunkGenerator> invokeCodec();

    @Mutable
    @Accessor("settings")
    void setSettings(StructureSettings settings);
}