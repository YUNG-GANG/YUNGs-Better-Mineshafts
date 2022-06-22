package com.yungnickyoung.minecraft.bettermineshafts.services;

import com.yungnickyoung.minecraft.bettermineshafts.module.ConfigModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureFeaturePieceModuleForge;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleForge.init();
        StructureModuleForge.init();
        StructureFeaturePieceModuleForge.init();
    }
}
