package com.yungnickyoung.minecraft.bettermineshafts;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(BetterMineshaftsCommon.MOD_ID)
public class BetterMineshaftsNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterMineshaftsNeoForge(IEventBus eventBus) {
        BetterMineshaftsNeoForge.loadingContextEventBus = eventBus;

        BetterMineshaftsCommon.init();
    }
}
