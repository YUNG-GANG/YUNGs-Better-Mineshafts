package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class StructureModuleFabric {
    public static void init() {
        Registry.register(Registry.STRUCTURE_TYPES, new ResourceLocation(BetterMineshaftsCommon.MOD_ID, "mineshaft"), StructureModule.BETTER_MINESHAFT);
    }
}
