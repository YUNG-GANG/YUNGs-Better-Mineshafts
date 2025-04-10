package com.yungnickyoung.minecraft.bettermineshafts.mixin;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGenerator.class)
public class DisableVanillaMineshaftsMixin {
    @Inject(method = "tryGenerateStructure", at = @At(value = "HEAD"), cancellable = true)
    void bettermineshafts_disableVanillaMineshafts(
            StructureSet.StructureSelectionEntry structureSetEntry,
            StructureManager $$1,
            RegistryAccess $$2,
            RandomState $$3,
            StructureTemplateManager $$4,
            long $$5,
            ChunkAccess $$6,
            ChunkPos $$7,
            SectionPos $$8,
            ResourceKey<Level> $$9,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (BetterMineshaftsCommon.CONFIG.disableVanillaMineshafts && structureSetEntry.structure().value().type() == StructureType.MINESHAFT) {
            cir.setReturnValue(false);
        }
    }
}
