package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.world.processor.StructureVoidProcessor;
import com.yungnickyoung.minecraft.bettermineshafts.world.processor.SuspiciousBlockProcessor;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

@AutoRegister(BetterMineshaftsCommon.MOD_ID)
public class StructureProcessorTypeModule {
    @AutoRegister("structure_void_processor")
    public static StructureProcessorType<StructureVoidProcessor> STRUCTURE_VOID_PROCESSOR = () -> StructureVoidProcessor.CODEC;

    @AutoRegister("suspicious_block_processor")
    public static StructureProcessorType<SuspiciousBlockProcessor> SUSPICIOUS_BLOCK_PROCESSOR = () -> SuspiciousBlockProcessor.CODEC;
}
