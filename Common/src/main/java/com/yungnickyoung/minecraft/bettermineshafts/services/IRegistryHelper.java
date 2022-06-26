package com.yungnickyoung.minecraft.bettermineshafts.services;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public interface IRegistryHelper {
    void registerStructureType(ResourceLocation resourceLocation, StructureType<? extends Structure> structureType);

    void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType);
}
