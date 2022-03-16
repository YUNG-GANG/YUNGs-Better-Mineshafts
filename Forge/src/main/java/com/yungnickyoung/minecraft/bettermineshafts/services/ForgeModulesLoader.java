package com.yungnickyoung.minecraft.bettermineshafts.services;

import com.yungnickyoung.minecraft.bettermineshafts.module.ConfigModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureFeatureModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureFeaturePieceModuleForge;
import com.yungnickyoung.minecraft.bettermineshafts.module.TagModuleForge;

public class ForgeModulesLoader implements IModulesLoader {
    @Override
    public void loadModules() {
        ConfigModuleForge.init();
        TagModuleForge.init();
        StructureFeatureModuleForge.init();
        StructureFeaturePieceModuleForge.init();
    }
}
