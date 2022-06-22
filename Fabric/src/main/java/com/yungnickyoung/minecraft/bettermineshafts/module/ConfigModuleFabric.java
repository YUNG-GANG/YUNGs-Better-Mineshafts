package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.world.InteractionResult;

public class ConfigModuleFabric {
    public static void init() {
        AutoConfig.register(BMConfigFabric.class, Toml4jConfigSerializer::new);
        AutoConfig.getConfigHolder(BMConfigFabric.class).registerSaveListener(ConfigModuleFabric::bakeConfig);
        AutoConfig.getConfigHolder(BMConfigFabric.class).registerLoadListener(ConfigModuleFabric::bakeConfig);
        bakeConfig(AutoConfig.getConfigHolder(BMConfigFabric.class).get());
    }

    private static InteractionResult bakeConfig(ConfigHolder<BMConfigFabric> configHolder, BMConfigFabric configFabric) {
        bakeConfig(configFabric);
        return InteractionResult.SUCCESS;
    }

    private static void bakeConfig(BMConfigFabric configFabric) {
        BetterMineshaftsCommon.CONFIG.minY = configFabric.minY;
        BetterMineshaftsCommon.CONFIG.maxY = configFabric.maxY;
        BetterMineshaftsCommon.CONFIG.ores.enabled = configFabric.ores.enabled;
        BetterMineshaftsCommon.CONFIG.ores.cobble = configFabric.ores.cobble;
        BetterMineshaftsCommon.CONFIG.ores.coal = configFabric.ores.coal;
        BetterMineshaftsCommon.CONFIG.ores.iron = configFabric.ores.iron;
        BetterMineshaftsCommon.CONFIG.ores.redstone = configFabric.ores.redstone;
        BetterMineshaftsCommon.CONFIG.ores.gold = configFabric.ores.gold;
        BetterMineshaftsCommon.CONFIG.ores.lapis = configFabric.ores.lapis;
        BetterMineshaftsCommon.CONFIG.ores.emerald = configFabric.ores.emerald;
        BetterMineshaftsCommon.CONFIG.ores.diamond = configFabric.ores.diamond;
        BetterMineshaftsCommon.CONFIG.spawnRates.lanternSpawnRate = configFabric.spawnRates.lanternSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.torchSpawnRate = configFabric.spawnRates.torchSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.workstationSpawnRate = configFabric.spawnRates.workstationSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.workstationDungeonSpawnRate = configFabric.spawnRates.workstationDungeonSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftSpawnRate = configFabric.spawnRates.smallShaftSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.cobwebSpawnRate = configFabric.spawnRates.cobwebSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftChestMinecartSpawnRate = configFabric.spawnRates.smallShaftChestMinecartSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftTntMinecartSpawnRate = configFabric.spawnRates.smallShaftTntMinecartSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.mainShaftChestMinecartSpawnRate = configFabric.spawnRates.mainShaftChestMinecartSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.mainShaftTntMinecartSpawnRate = configFabric.spawnRates.mainShaftTntMinecartSpawnRate;
        BetterMineshaftsCommon.CONFIG.spawnRates.zombieVillagerRoomSpawnRate = configFabric.spawnRates.zombieVillagerRoomSpawnChance;
        BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftPieceChainLength = configFabric.spawnRates.smallShaftPieceChainLength;
    }
}
