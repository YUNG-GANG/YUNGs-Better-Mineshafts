package com.yungnickyoung.minecraft.bettermineshafts.module;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.HashMap;
import java.util.Map;

public class StructureTypeModuleForge {
    public static Map<ResourceLocation, StructureType<? extends Structure>> STRUCTURE_TYPES = new HashMap<>();

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureTypeModuleForge::registerStructures);
    }

    private static void registerStructures(RegisterEvent event) {
        event.register(Registry.STRUCTURE_TYPE_REGISTRY, helper -> STRUCTURE_TYPES.forEach(helper::register));
    }
}
