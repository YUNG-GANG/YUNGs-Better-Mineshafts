package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

public class StructureModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(StructureModuleForge::registerStructures);
    }

    private static void registerStructures(RegisterEvent event) {
        event.register(Registry.STRUCTURE_TYPE_REGISTRY, helper -> {
            helper.register(new ResourceLocation(BetterMineshaftsCommon.MOD_ID, "mineshaft"), StructureModule.BETTER_MINESHAFT);
        });
    }
}
