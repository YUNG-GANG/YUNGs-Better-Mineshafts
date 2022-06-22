package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import net.minecraft.world.level.levelgen.structure.StructureType;

public class StructureModule {
    public static StructureType<BetterMineshaftStructure> BETTER_MINESHAFT = () -> BetterMineshaftStructure.CODEC;
}
