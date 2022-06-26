package com.yungnickyoung.minecraft.bettermineshafts.services;

import com.yungnickyoung.minecraft.bettermineshafts.module.StructurePieceTypeModule;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureTypeModule;

public interface IModulesLoader {
    default void loadModules() {
        StructureTypeModule.init();
        StructurePieceTypeModule.init();
    }
}
