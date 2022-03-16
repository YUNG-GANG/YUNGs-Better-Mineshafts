package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class StructureFeatureModuleForge {
    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(StructureFeature.class, StructureFeatureModuleForge::registerStructures);
    }

    private static void registerStructures(RegistryEvent.Register<StructureFeature<?>> event) {
        StructureFeatureModule.BETTER_MINESHAFT.setRegistryName(new ResourceLocation(BetterMineshaftsCommon.MOD_ID, "mineshaft"));
        event.getRegistry().register(StructureFeatureModule.BETTER_MINESHAFT);
    }
}
