package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.variant.MineshaftVariants;
import com.yungnickyoung.minecraft.yungsapi.io.JSON;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class BMModConfig {
    public static void init() {
        initCustomFiles();
        // Register mod config with Forge
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BMConfig.SPEC, "bettermineshafts-forge-1_18.toml");
        // Refresh JSON config on world load so that user doesn't have to restart MC
        MinecraftForge.EVENT_BUS.addListener(BMModConfig::onWorldLoad);
    }

    private static void onWorldLoad(WorldEvent.Load event) {
        loadVariantsJSON();
    }

    private static void initCustomFiles() {
        createDirectory();
        createBaseReadMe();
        createJsonReadMe();
        createBiomeTagsTxt();
        loadVariantsJSON();
    }

    private static void createDirectory() {
        File parentDir = new File(FMLPaths.CONFIGDIR.get().toString(), BMSettings.CUSTOM_CONFIG_PATH);
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

    private static void createBaseReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BMSettings.CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            This directory is for adding YUNG's Better Mineshafts advanced options.
                            Options provided may vary by version.
                            This directory contains subdirectories for supported versions. The first time you run Better Mineshafts, a version subdirectory will be created if that version supports advanced options.
                            For example, the first time you use Better Mineshafts for Minecraft Forge 1.18, the 'forge-1_18 subdirectory will be created in this folder.
                            If no subdirectory for your version is created, then that version probably does not support advanced options.""";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create README file!");
            }
        }
    }


    private static void createJsonReadMe() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BMSettings.CUSTOM_CONFIG_PATH, BMSettings.VERSION_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    """
                            variants.json README

                            The variants.json file contains two properties:
                             - variants: a list of all the biome-dependent Variant Settings objects (see below)
                                  * NOTE - order is important! The list will be searched in order, and searching will be stopped at the first match.
                                  For example, if you have a rare mesa variant that uses the RARE and MESA biomeTags, as well as a normal mesa variant that only uses the MESA biomeTag,
                                  you will have to put the rare mesa variant BEFORE the normal mesa variant, or else all mesa biomes will match the normal mesa variant before they can check for the rare mesa variant.
                             - defaultVariant: a Variant Settings object to use for biomes that don't match the biomeTags for any of the Variant Settings in the "variants" list.
                                  This serves as the go-to/default mineshaft - think plain ol' oak planks mineshafts

                            Variant Settings: a single Variant Settings object is composed of the following properties.
                            ALL of these properties are required for ALL Variants Settings objects, with the exception of the biomeTags for the defaultVariant.
                             - biomeTags: a list of lists of BiomeDictionary tags required for this variant to spawn. Only one of the lists of tags must be matched.
                                  For example, by default we want the Red Desert mineshaft variant to spawn in rare desert AND rare mesa biomes.
                                  All desert biomes have the HOT, DRY, and SANDY tags; all mesa biomes have the MESA tag.
                                  Therefore, the biomeTags list for our Red Desert mineshaft in this example will look like the following:
                                  "biomeTags": [
                                    [
                                      "HOT",
                                      "DRY",
                                      "SANDY",
                                      "RARE"
                                    ],
                                    [
                                      "MESA",
                                      "RARE"
                                    ]
                                  ]
                                  NOTE that the biomeTags property is IGNORED for the defaultVariant, since the defaultVariant simply acts as the variant for all
                                     the biomes in the world that don't meet the criteria for any of the variants in the "variants" list.
                                  * SEE THE biomeTags.txt FILE FOR A LIST OF ALL BIOME TAGS *
                             - mainSelector: the BlockSelector (see below) used for generating the mineshaft's walls and ceiling
                             - floorSelector: the BlockSelector (see below) used for generating the mineshaft's floor
                             - brickSelector: the BlockSelector (see below) used for generating areas of the mineshaft where brick-like blocks would be more appropriate.
                                  This includes abandoned workstations, workstation cellars, and the doorway at the end of the main shaft for mineshafts containing surface openings.
                             - legSelector: the BlockSetSelector (see below) used for generating the 'legs' of the main mineshaft.
                                  These are the supports that form underneath the main mineshaft tunnel when the mineshaft spawns over a big opening.
                             - mainBlock: The main thematic block for the mineshaft. You will almost certainly want this to be the same as the defaultBlock in the mainSelector and floorSelector properties (see above).
                                  Used as the base and top of the small supports generated throughout the mineshaft.
                                  Also used as the floor for bridging gaps the mineshaft might spawn over.
                             - supportBlock: Used as the middle section of the small supports generated throughout the mineshaft.
                                  Also used as the supports in rooms with ladders found in the small shafts.
                                  Also used in Type 1 Leg Variants (see below).
                                  Usually this is a fence or wall block, but it's not required to be.
                             - slabBlock: The main slab block to use. Should be a block that matches your mainBlock well.
                             - gravelBlock: The block used for gravel deposits placed randomly throughout mineshafts.
                                  Usually gravel, sand, or snow.
                             - stoneWallBlock: The block used to frame the left and right sides of the doorway in the main shaft leading to the surface entrance, if present.
                                  This is a very minor piece and doesn't matter much. If you aren't sure, use your mainBlock or one of the blocks in your brickSelector.
                             - stoneSlabBlock: The block used to frame the top side of the doorway in the main shaft leading to the surface entrance, if present.
                                  This is a very minor piece and doesn't matter much. If you aren't sure, use your mainBlock or one of the blocks in your brickSelector.
                             - trapdoorBlock: The block used for trapdoors leading to workstation cellars.
                             - vineChance: chance of vines spawning in the mineshaft
                             - snowChance: chance of snow spawning on the floor of the mineshaft
                             - cactusChance: chance of cactus spawning in the mineshaft. Can only spawn on top of valid floor blocks (e.g. sand)
                             - deadBushChance: chance of dead bushes spawning the mineshaft. Can only spawn on top of valid floor blocks (sand, terracotta, dirt)
                             - mushroomChance: chance of mushrooms spawning in the mineshaft. Can only spawn on top of valid floor blocks (mycelium, dirt)
                             - legVariant: The ID of the leg variant to use. ACCEPTABLE VALUES: 1, 2
                                  1: The legs used for most mineshafts. Uses the legSelector and the supportBlock.
                                  2: The legs used for ice and mushroom variants by default. Uses only the legSelector.
                             - flammableLegs: Boolean value for whether the legs of this mineshaft are made of flammable material.
                                  If a mineshaft variant has this value set to true, it will use the brickSelector instead of the legSelector
                                  to generate legs that will spawn in lava. This helps to prevent mineshafts from catching on fire right after generation.
                             - lushDecorations: Boolean value for whether or not the mineshaft will have lush cave decorations throughout it.
                             - replacementRate: The percent of existing blocks the mainSelector and floorSelector should replace.
                                  For example, if the replacementRate is .6, then 60% of the already existing stone, andesite, etc in the floors/walls/ceiling
                                  will be replaced with blocks determined by the selectors.
                                  Lowering this value preserves more of the regular worldgen blocks in the mineshaft's floors/walls/ceiling.

                            BlockSelector: Describes a set of blocks and the probability of each block being chosen.
                             - entries: An object where each entry's key is a block, and each value is that block's probability of being chosen.
                                  The total sum of all probabilities SHOULD NOT exceed 1.0!
                             - defaultBlock: The block used for any leftover probability ranges.
                                  For example, if the total sum of all the probabilities of the entries is 0.6, then
                                  there is a 0.4 chance of the defaultBlock being selected.

                            Here's an example block selector:
                            "entries": {
                              "minecraft:cobblestone": 0.25,
                              "minecraft:air": 0.2,
                              "minecraft:stonebrick[variant=stonebrick]": 0.1
                            },
                            "defaultBlock": "minecraft:planks[variant=oak]"

                            For each block, this selector has a 25% chance of returning cobblestone, 20% chance of choosing air,
                            10% chance of choosing stone bricks, and a 100 - (25 + 20 + 10) = 45% chance of choosing oak planks (since it's the default block).
                            """;

            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create variants.json README file!");
            }
        }
    }

    private static void createBiomeTagsTxt() {
        Path path = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BMSettings.CUSTOM_CONFIG_PATH, BMSettings.VERSION_PATH, "biomeTags.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                    "Helper file showing all available BiomeDictionary biome tags.\n\n";
            try {
                Files.write(path, readmeText.getBytes());
                Files.write(path, JSON.toJsonString(BiomeDictionary.Type.getAll()).getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create variants.json README file!");
            }
        }
    }

    /**
     * If the variants JSON file already exists, it loads its contents into MineshaftVariants.
     * Otherwise, it creates a default JSON and from the default options in MineshaftVariants.
     */
    private static void loadVariantsJSON() {
        Path jsonPath = Paths.get(FMLPaths.CONFIGDIR.get().toString(), BMSettings.CUSTOM_CONFIG_PATH, BMSettings.VERSION_PATH, "variants.json");
        File jsonFile = new File(jsonPath.toString());

        if (!jsonFile.exists()) {
            // Create default file if JSON file doesn't already exist
            try {
                JSON.createJsonFileFromObject(jsonPath, MineshaftVariants.get());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create JSON file! - {}", e.toString());
            }
        } else {
            // If file already exists, load data into BlockSetSelectors' singleton instance
            if (!jsonFile.canRead()) {
                BetterMineshafts.LOGGER.error("Better Mineshafts variants.json file not readable! Using default configuration...");
                return;
            }

            try {
                MineshaftVariants.instance = JSON.loadObjectFromJsonFile(jsonPath, MineshaftVariants.class);
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Error loading Better Mineshafts variants.json file: {}", e.toString());
                BetterMineshafts.LOGGER.error("Using default configuration...");
            }
        }
    }
}
