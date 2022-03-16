package com.yungnickyoung.minecraft.bettermineshafts.mixin;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class DisableVanillaMineshaftsMixinForge {
    @Inject(method = "tryGenerateStructure", at = @At(value = "HEAD"), cancellable = true)
    void disableVanillaStrongholds(StructureSet.StructureSelectionEntry structureSetEntry,
                                   StructureFeatureManager structureFeatureManager,
                                   RegistryAccess registryAccess,
                                   StructureManager structureManager,
                                   long seed,
                                   ChunkAccess chunkAccess,
                                   ChunkPos chunkPos,
                                   SectionPos sectionPos,
                                   CallbackInfoReturnable<Boolean> cir) {
        if (structureSetEntry.structure().value().feature.getRegistryName().equals(new ResourceLocation("mineshaft"))) {
            cir.setReturnValue(false);
        }
    }
}
