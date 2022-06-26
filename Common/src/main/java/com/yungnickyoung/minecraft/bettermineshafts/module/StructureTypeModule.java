package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.services.Services;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class StructureTypeModule {
    public static StructureType<BetterMineshaftStructure> BETTER_MINESHAFT = () -> BetterMineshaftStructure.CODEC;

    public static void init() {
        Services.REGISTRY.registerStructureType(new ResourceLocation(BetterMineshaftsCommon.MOD_ID, "mineshaft"), BETTER_MINESHAFT);
    }
}
