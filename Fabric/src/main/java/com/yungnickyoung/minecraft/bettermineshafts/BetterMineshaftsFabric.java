package com.yungnickyoung.minecraft.bettermineshafts;

import net.fabricmc.api.ModInitializer;

public class BetterMineshaftsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        BetterMineshaftsCommon.init();
    }
}
