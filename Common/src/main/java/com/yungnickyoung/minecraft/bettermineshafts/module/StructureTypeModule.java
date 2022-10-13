package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.StructureType;

@AutoRegister(BetterMineshaftsCommon.MOD_ID)
public class StructureTypeModule {
    @AutoRegister("mineshaft")
    public static StructureType<BetterMineshaftStructure> BETTER_MINESHAFT = () -> BetterMineshaftStructure.CODEC;
}
