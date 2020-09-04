package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.google.gson.*;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMSettings;
import com.yungnickyoung.minecraft.bettermineshafts.event.EventConfigReload;
import com.yungnickyoung.minecraft.bettermineshafts.util.BlockSetSelectors;
import com.yungnickyoung.minecraft.bettermineshafts.json.BlockStateAdapter;
import com.yungnickyoung.minecraft.bettermineshafts.json.BlockStateContainerAdapter;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {
    public static void preInit() {
        createDirectory();
        createReadMe();
        loadBlockJSON();
        MinecraftForge.EVENT_BUS.register(new EventConfigReload());
    }

    private static void createDirectory() {
        File parentDir = new File(Loader.instance().getConfigDir(), BMSettings.CUSTOM_CONFIG_PATH);
        File customConfigDir = new File(parentDir, BMSettings.VERSION_PATH);
        try {
            String filePath = customConfigDir.getCanonicalPath();
            if (customConfigDir.mkdirs()) {
                BetterMineshafts.LOGGER.info("Creating directory for advanced Better Mineshafts configs at {}", filePath);
            }
        } catch (IOException e) {
            BetterMineshafts.LOGGER.error("ERROR creating Better Mineshafts config directory: {}", e.toString());
        }
    }

    private static void createReadMe() {
        Path path = Paths.get(Loader.instance().getConfigDir().toString(), BMSettings.CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                "This directory is for adding YUNG's Better Mineshafts advanced options.\n" +
                "These options may not be supported for all versions.\n" +
                "If you're reading this, you almost certainly don't need to use this folder. All the important stuff can be found in the main config file.\n\n" +
                "This directory contains subdirectories for supported versions. The first time you run Better Mineshafts, a version subdirectory will be created if that version supports this feature.\n" +
                "For example, the first time you use Better Mineshafts v1.4+ for Minecraft 1.12.2, the '1_12_2' subdirectory will be created in this folder.\n" +
                "If no subdirectory for your version is created, then that version probably does not support this feature.";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create README file!");
            }
        }
    }

    /**
     * If the block selector JSON file already exists, it loads its contents into BlockSetSelectors.
     * Otherwise, it creates a default JSON and uses it for the BlockSetSelectors.
     */
    private static void loadBlockJSON() {
        Path jsonPath = Paths.get(Loader.instance().getConfigDir().toString(), BMSettings.CUSTOM_CONFIG_PATH, BMSettings.VERSION_PATH, "blockSetSelectors.json");
        File jsonFile = new File(jsonPath.toString());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BlockStateContainer.StateImplementation.class, new BlockStateContainerAdapter());
        gsonBuilder.registerTypeAdapter(IBlockState.class, new BlockStateAdapter());
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.disableHtmlEscaping();
        Gson gson = gsonBuilder.create();

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            String jsonString = gson.toJson(BlockSetSelectors.get());

            try {
                Files.write(jsonPath, jsonString.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create JSON file!");
            }
        } else {
            // If file already exists, load data into BlockSetSelectors' singleton instance
            if (!jsonFile.canRead()) {
                BetterMineshafts.LOGGER.error("Better Mineshafts block selectors JSON file not readable! Using default configuration...");
                return;
            }

            try (Reader reader = Files.newBufferedReader(jsonPath)) {
                BlockSetSelectors.instance = gson.fromJson(reader, BlockSetSelectors.class);
            } catch (Exception e) {
                BetterMineshafts.LOGGER.error("Error loading Better Mineshafts block selectors JSON file: {}", e.toString());
                BetterMineshafts.LOGGER.error("Using default configuration...");
            }

            BetterMineshafts.LOGGER.error(BlockSetSelectors.instance);

        }
    }
}
