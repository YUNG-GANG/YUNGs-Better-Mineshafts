package com.yungnickyoung.minecraft.bettermineshafts.event;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMSettings;
import com.yungnickyoung.minecraft.bettermineshafts.integration.Integrations;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Synchronize changes to the config.
 * Should be registered to the {@code EVENT_BUS} on the client side.
 */
public class EventConfigReload {
    /**
     * Keeps Better Mineshafts config settings synchronized
     */
    @SubscribeEvent
    public void onConfigReload(ConfigChangedEvent.OnConfigChangedEvent event) {
        // Only mess with config syncing if it is this mod being changed
        if (BMSettings.MOD_ID.equals(event.getModID())) {
            ConfigManager.sync(BMSettings.MOD_ID, Config.Type.INSTANCE);
            Integrations.update();
        }
    }
}
