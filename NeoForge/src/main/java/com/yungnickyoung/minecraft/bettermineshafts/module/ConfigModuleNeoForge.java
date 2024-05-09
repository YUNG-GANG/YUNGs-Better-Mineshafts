package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsNeoForge;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfigNeoForge;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.LevelEvent;

public class ConfigModuleNeoForge {
    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BMConfigNeoForge.SPEC, "bettermineshafts-neoforge-1_20_4.toml");
        NeoForge.EVENT_BUS.addListener(ConfigModuleNeoForge::onWorldLoad);
        BetterMineshaftsNeoForge.loadingContextEventBus.addListener(ConfigModuleNeoForge::onConfigChange);
    }

    private static void onWorldLoad(LevelEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BMConfigNeoForge.SPEC) {
            bakeConfig();
        }
    }

    private static void bakeConfig() {
        BetterMineshaftsCommon.CONFIG.disableVanillaMineshafts = BMConfigNeoForge.disableVanillaMineshafts.get();
        BetterMineshaftsCommon.CONFIG.minY = BMConfigNeoForge.minY.get();
        BetterMineshaftsCommon.CONFIG.maxY = BMConfigNeoForge.maxY.get();
        BetterMineshaftsCommon.CONFIG.ores.enabled = BMConfigNeoForge.ores.enabled.get();
        BetterMineshaftsCommon.CONFIG.ores.cobble = BMConfigNeoForge.ores.cobble.get();
        BetterMineshaftsCommon.CONFIG.ores.coal = BMConfigNeoForge.ores.coal.get();
        BetterMineshaftsCommon.CONFIG.ores.iron = BMConfigNeoForge.ores.iron.get();
        BetterMineshaftsCommon.CONFIG.ores.redstone = BMConfigNeoForge.ores.redstone.get();
        BetterMineshaftsCommon.CONFIG.ores.gold = BMConfigNeoForge.ores.gold.get();
        BetterMineshaftsCommon.CONFIG.ores.lapis = BMConfigNeoForge.ores.lapis.get();
        BetterMineshaftsCommon.CONFIG.ores.emerald = BMConfigNeoForge.ores.emerald.get();
        BetterMineshaftsCommon.CONFIG.ores.diamond = BMConfigNeoForge.ores.diamond.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.lanternSpawnRate = BMConfigNeoForge.spawnRates.lanternSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.torchSpawnRate = BMConfigNeoForge.spawnRates.torchSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.workstationSpawnRate = BMConfigNeoForge.spawnRates.workstationSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.workstationDungeonSpawnRate = BMConfigNeoForge.spawnRates.workstationDungeonSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftSpawnRate = BMConfigNeoForge.spawnRates.smallShaftSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.cobwebSpawnRate = BMConfigNeoForge.spawnRates.cobwebSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftChestMinecartSpawnRate = BMConfigNeoForge.spawnRates.smallShaftChestMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftTntMinecartSpawnRate = BMConfigNeoForge.spawnRates.smallShaftTntMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.mainShaftChestMinecartSpawnRate = BMConfigNeoForge.spawnRates.mainShaftChestMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.mainShaftTntMinecartSpawnRate = BMConfigNeoForge.spawnRates.mainShaftTntMinecartSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.zombieVillagerRoomSpawnRate = BMConfigNeoForge.spawnRates.zombieVillagerRoomSpawnRate.get();
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftPieceChainLength = BMConfigNeoForge.spawnRates.smallShaftPieceChainLength.get();
    }
}
