package com.yungnickyoung.minecraft.bettermineshafts.services;

import com.yungnickyoung.minecraft.bettermineshafts.module.ConfigModuleFabric;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureModuleFabric;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureFeaturePieceModuleFabric;

public class FabricModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleFabric.init();
        StructureModuleFabric.init();
        StructureFeaturePieceModuleFabric.init();
    }
}
