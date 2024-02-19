package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfigForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ConfigModuleForge {
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BMConfigForge.SPEC, "bettermineshafts-forge-1_20_4.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BMConfigForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        BetterMineshaftsCommon.CONFIG.disableVanillaMineshafts = BMConfigForge.disableVanillaMineshafts.get();
        BetterMineshaftsCommon.CONFIG.minY = BMConfigForge.minY.get();
        BetterMineshaftsCommon.CONFIG.maxY = BMConfigForge.maxY.get();
        BetterMineshaftsCommon.CONFIG.ores.enabled = BMConfigForge.ores.enabled.get();
        BetterMineshaftsCommon.CONFIG.ores.cobble = BMConfigForge.ores.cobble.get();
        BetterMineshaftsCommon.CONFIG.ores.coal = BMConfigForge.ores.coal.get();
        BetterMineshaftsCommon.CONFIG.ores.iron = BMConfigForge.ores.iron.get();
        BetterMineshaftsCommon.CONFIG.ores.redstone = BMConfigForge.ores.redstone.get();
        BetterMineshaftsCommon.CONFIG.ores.gold = BMConfigForge.ores.gold.get();
        BetterMineshaftsCommon.CONFIG.ores.lapis = BMConfigForge.ores.lapis.get();
        BetterMineshaftsCommon.CONFIG.ores.emerald = BMConfigForge.ores.emerald.get();
        BetterMineshaftsCommon.CONFIG.ores.diamond = BMConfigForge.ores.diamond.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.lanternSpawnRate = BMConfigForge.spawnRates.lanternSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.torchSpawnRate = BMConfigForge.spawnRates.torchSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.workstationSpawnRate = BMConfigForge.spawnRates.workstationSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.workstationDungeonSpawnRate = BMConfigForge.spawnRates.workstationDungeonSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftSpawnRate = BMConfigForge.spawnRates.smallShaftSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.cobwebSpawnRate = BMConfigForge.spawnRates.cobwebSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftChestMinecartSpawnRate = BMConfigForge.spawnRates.smallShaftChestMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftTntMinecartSpawnRate = BMConfigForge.spawnRates.smallShaftTntMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.mainShaftChestMinecartSpawnRate = BMConfigForge.spawnRates.mainShaftChestMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.mainShaftTntMinecartSpawnRate = BMConfigForge.spawnRates.mainShaftTntMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.zombieVillagerRoomSpawnRate = BMConfigForge.spawnRates.zombieVillagerRoomSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftPieceChainLength = BMConfigForge.spawnRates.smallShaftPieceChainLength.get();
    }
}
