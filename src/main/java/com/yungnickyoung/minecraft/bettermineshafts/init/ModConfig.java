package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMForgeConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ModConfig {
    public static void init() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, BMForgeConfig.SPEC, "bettermineshafts-forge-1_16.toml");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModConfig::configChanged);
        BMConfig.init();
    }

    public static void configChanged(net.minecraftforge.fml.config.ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == BMForgeConfig.SPEC) {
            BMConfig.bake();
            BetterMineshafts.LOGGER.debug("Baked configuration changes.");
        }
    }
}
