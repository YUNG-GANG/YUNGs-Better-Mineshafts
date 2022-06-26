package com.yungnickyoung.minecraft.bettermineshafts.services;

import com.yungnickyoung.minecraft.bettermineshafts.module.StructurePieceTypeModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureTypeModuleForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ForgeRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructureType(ResourceLocation resourceLocation, StructureType<? extends Structure> structureType) {
        StructureTypeModuleForge.STRUCTURE_TYPES.put(resourceLocation, structureType);
    }

    @Override
    public void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType) {
        StructurePieceTypeModuleForge.STRUCTURE_PIECE_TYPES.put(resourceLocation, structurePieceType);
    }
}
