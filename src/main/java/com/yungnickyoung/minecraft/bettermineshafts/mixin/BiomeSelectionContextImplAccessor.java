package com.yungnickyoung.minecraft.bettermineshafts.mixin;

import net.fabricmc.fabric.impl.biome.modification.BiomeSelectionContextImpl;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BiomeSelectionContextImpl.class)
public interface BiomeSelectionContextImplAccessor {
    @Accessor
    RegistryAccess getDynamicRegistries();
}
