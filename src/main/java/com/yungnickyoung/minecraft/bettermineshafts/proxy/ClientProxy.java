package com.yungnickyoung.minecraft.bettermineshafts.proxy;

import com.yungnickyoung.minecraft.bettermineshafts.init.ModConfig;

/**
 * Proxy for client-only code.
 */
public class ClientProxy implements IProxy {
    @Override
    public void preInit() {
        ModConfig.preInit();
    }
}