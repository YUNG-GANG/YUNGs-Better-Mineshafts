package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.google.gson.*;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMSettings;
import com.yungnickyoung.minecraft.bettermineshafts.event.EventConfigReload;
import com.yungnickyoung.minecraft.bettermineshafts.json.BiomeDictionaryTypeAdapter;
import com.yungnickyoung.minecraft.bettermineshafts.json.BlockStateAdapter;
import com.yungnickyoung.minecraft.bettermineshafts.json.BlockStateContainerAdapter;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariants;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.BiomeDictionary;
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
        loadVariantsJSON();
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
     * If the variants JSON file already exists, it loads its contents into MineshaftVariants.
     * Otherwise, it creates a default JSON and from the default options in MineshaftVariants.
     */
    private static void loadVariantsJSON() {
        Path jsonPath = Paths.get(Loader.instance().getConfigDir().toString(), BMSettings.CUSTOM_CONFIG_PATH, BMSettings.VERSION_PATH, "variants.json");
        File jsonFile = new File(jsonPath.toString());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BlockStateContainer.StateImplementation.class, new BlockStateContainerAdapter());
        gsonBuilder.registerTypeAdapter(IBlockState.class, new BlockStateAdapter());
        gsonBuilder.registerTypeAdapter(BiomeDictionary.Type.class, new BiomeDictionaryTypeAdapter());
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.disableHtmlEscaping();
        Gson gson = gsonBuilder.create();

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            String jsonString = gson.toJson(MineshaftVariants.get());

            try {
                Files.write(jsonPath, jsonString.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create JSON file!");
            }
        } else {
            // If file already exists, load data into BlockSetSelectors' singleton instance
            if (!jsonFile.canRead()) {
                BetterMineshafts.LOGGER.error("Better Mineshafts variants.json file not readable! Using default configuration...");
                return;
            }

            try (Reader reader = Files.newBufferedReader(jsonPath)) {
                MineshaftVariants.instance = gson.fromJson(reader, MineshaftVariants.class);
            } catch (Exception e) {
                BetterMineshafts.LOGGER.error("Error loading Better Mineshafts variants.json file: {}", e.toString());
                BetterMineshafts.LOGGER.error("Using default configuration...");
            }
        }
    }
}
