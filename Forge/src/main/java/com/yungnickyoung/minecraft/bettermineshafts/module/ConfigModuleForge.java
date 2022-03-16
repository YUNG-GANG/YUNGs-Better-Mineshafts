package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfigForge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigModuleForge {
    public static final String CUSTOM_CONFIG_PATH = "bettermineshafts";
    public static final String VERSION_PATH = "forge-1_18_2";

    public static void init() {
        initCustomFiles();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BMConfigForge.SPEC, "bettermineshafts-forge-1_18.toml");
        MinecraftForge.EVENT_BUS.addListener(ConfigModuleForge::onWorldLoad);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigModuleForge::onConfigChange);
    }

    private static void onWorldLoad(WorldEvent.Load event) {
        bakeConfig();
    }

    private static void onConfigChange(ModConfigEvent event) {
        if (event.getConfig().getSpec() == BMConfigForge.SPEC) {
            bakeConfig();
        }
    }

    private static void initCustomFiles() {
        createDirectory();
        createBaseReadMe();
        createJsonReadMe();
    }

    private static void createDirectory() {
        File parentDir = new File(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH);
        File customConfigDir = new File(parentDir, VERSION_PATH);
        try {
            String filePath = customConfigDir.getCanonicalPath();
            if (customConfigDir.mkdirs()) {
                BetterMineshaftsCommon.LOGGER.info("Creating directory for advanced Better Mineshafts configs at {}", filePath);
            }
        } catch (IOException e) {
            BetterMineshaftsCommon.LOGGER.error("ERROR creating Better Mineshafts config directory: {}", e.toString());
        }
    }

    private static void createBaseReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            This directory is for adding YUNG's Better Mineshafts advanced options.
                            Options provided may vary by version.

                            NOTE - AS OF 1.18.2, MINESHAFT CUSTOMIZATION IS NOW DONE VIA DATA PACK!
                            
                            THE FOLLOWING INSTRUCTIONS APPLY TO VERSIONS BEFORE 1.18.2:
                            
                            This directory contains subdirectories for supported versions. The first time you run Better Mineshafts, a version subdirectory will be created if that version supports advanced options.
                            For example, the first time you use Better Mineshafts for Minecraft Forge 1.18, the 'forge-1_18 subdirectory will be created in this folder.
                            If no subdirectory for your version is created, then that version probably does not support advanced options.""";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshaftsCommon.LOGGER.error("Unable to create README file!");
            }
        }
    }

    private static void createJsonReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), CUSTOM_CONFIG_PATH, VERSION_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            YUNG's Better Mineshafts for 1.18.2 no longer uses its own JSON files for creating custom mineshaft variants.
                            Minecraft 1.18.2 has introduced the ability to add custom structures via data pack,
                            so any mineshaft customization should be done by adding/modifying Better Mineshafts' configured_features via data pack.
                            
                            If you need help, join the Discord!
                            
                            discord.gg/rns3beq
                            """;

            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshaftsCommon.LOGGER.error("Unable to create README file!");
            }
        }
    }

    private static void bakeConfig() {
        BetterMineshaftsCommon.CONFIG.mineshaftSpawnRate = BMConfigForge.mineshaftSpawnRate.get();
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
