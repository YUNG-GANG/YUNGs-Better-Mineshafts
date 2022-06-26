package com.yungnickyoung.minecraft.bettermineshafts.module;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.Map;

public class StructurePieceTypeModuleForge {
    public static Map<ResourceLocation, StructurePieceType> STRUCTURE_PIECE_TYPES = new HashMap<>();

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructurePieceTypeModuleForge::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> STRUCTURE_PIECE_TYPES.forEach((name, structurePieceType) -> Registry.register(Registry.STRUCTURE_PIECE, name, structurePieceType)));
    }
}
