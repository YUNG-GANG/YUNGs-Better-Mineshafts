package com.yungnickyoung.minecraft.bettermineshafts.proxy;

import com.yungnickyoung.minecraft.bettermineshafts.event.EventConfigReload;
import net.minecraftforge.common.MinecraftForge;

/**
 * Proxy for client-only code.
 */
public class ClientProxy implements IProxy {
    @Override
    public void preInit() {
        MinecraftForge.EVENT_BUS.register(new EventConfigReload());
    }
}