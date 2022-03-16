package com.yungnickyoung.minecraft.bettermineshafts.services;

import com.yungnickyoung.minecraft.bettermineshafts.module.ConfigModuleFabric;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureFeatureModuleFabric;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureFeaturePieceModuleFabric;
import com.yungnickyoung.minecraft.bettermineshafts.module.TagModuleFabric;

public class FabricModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleFabric.init();
        TagModuleFabric.init();
        StructureFeatureModuleFabric.init();
        StructureFeaturePieceModuleFabric.init();
    }
}
