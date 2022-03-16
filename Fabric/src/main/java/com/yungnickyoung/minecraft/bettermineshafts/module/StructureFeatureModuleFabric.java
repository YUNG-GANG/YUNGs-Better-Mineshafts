package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class StructureFeatureModuleFabric {
    public static void init() {
        Registry.register(Registry.STRUCTURE_FEATURE, new ResourceLocation(BetterMineshaftsCommon.MOD_ID, "mineshaft"), StructureFeatureModule.BETTER_MINESHAFT);
    }
}
