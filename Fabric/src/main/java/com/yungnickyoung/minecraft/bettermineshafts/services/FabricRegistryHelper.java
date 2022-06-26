package com.yungnickyoung.minecraft.bettermineshafts.services;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class FabricRegistryHelper implements IRegistryHelper {
    @Override
    public void registerStructureType(ResourceLocation resourceLocation, StructureType<? extends Structure> structureType) {
        Registry.register(Registry.STRUCTURE_TYPES, resourceLocation, structureType);
    }

    @Override
    public void registerStructurePieceType(ResourceLocation resourceLocation, StructurePieceType structurePieceType) {
        Registry.register(Registry.STRUCTURE_PIECE, resourceLocation, structurePieceType);
    }
}
