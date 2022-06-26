package com.yungnickyoung.minecraft.bettermineshafts.services;

import com.yungnickyoung.minecraft.bettermineshafts.module.ConfigModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureTypeModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructurePieceTypeModuleForge;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        IModulesLoader.super.loadModules(); // Load common modules
        ConfigModuleForge.init();
        StructureTypeModuleForge.init();
        StructurePieceTypeModuleForge.init();
    }
}
