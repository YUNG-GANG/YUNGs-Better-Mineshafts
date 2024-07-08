package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.module.ConfigModuleNeoForge;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(BetterMineshaftsCommon.MOD_ID)
public class BetterMineshaftsNeoForge {
    public static IEventBus loadingContextEventBus;

    public BetterMineshaftsNeoForge(IEventBus eventBus, ModContainer container) {
        BetterMineshaftsNeoForge.loadingContextEventBus = eventBus;

        BetterMineshaftsCommon.init();
        ConfigModuleNeoForge.init(container);
    }
}
