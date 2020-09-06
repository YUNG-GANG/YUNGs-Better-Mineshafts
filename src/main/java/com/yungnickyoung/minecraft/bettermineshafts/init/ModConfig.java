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
        createBaseReadMe();
        createJsonReadMe();
        createBiomeTagsTxt();
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

    private static void createBaseReadMe() {
        Path path = Paths.get(Loader.instance().getConfigDir().toString(), BMSettings.CUSTOM_CONFIG_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                "This directory is for adding YUNG's Better Mineshafts advanced options.\n" +
                "Options provided may vary by version.\n" +
                "This directory contains subdirectories for supported versions. The first time you run Better Mineshafts, a version subdirectory will be created if that version supports advanced options.\n" +
                "For example, the first time you use Better Mineshafts v2.0+ for Minecraft 1.12.2, the '1_12_2' subdirectory will be created in this folder.\n" +
                "If no subdirectory for your version is created, then that version probably does not support advanced options.";
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create README file!");
            }
        }
    }

    private static void createJsonReadMe() {
        Path path = Paths.get(Loader.instance().getConfigDir().toString(), BMSettings.CUSTOM_CONFIG_PATH, BMSettings.VERSION_PATH, "README.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {
            String readmeText =
                "variants.json README\n" +
                "\n" +
                "The variants.json file contains two properties:\n" +
                " - variants: a list of all the biome-dependent Variant Settings objects (see below)\n" +
                "      * NOTE - order is important! The list will be searched in order, and searching will be stopped at the first match.\n" +
                "      For example, if you have a rare mesa variant that uses the RARE and MESA biomeTags, as well as a normal mesa variant that only uses the MESA biomeTag,\n" +
                "      you will have to put the rare mesa variant BEFORE the normal mesa variant, or else all mesa biomes will match the normal mesa variant before they can check for the rare mesa variant." +
                " - defaultVariant: a Variant Settings object to use for biomes that don't match the biomeTags for any of the Variant Settings in the \"variants\" list.\n" +
                "      This serves as the go-to/default mineshaft - think plain ol' oak planks mineshafts\n" +
                "\n" +
                "Variant Settings: a single Variant Settings object is composed of the following properties.\n" +
                "ALL of these properties are required for ALL Variants Settings objects, with the exception of the biomeTags for the defaultVariant.\n" +
                " - biomeTags: a list of lists of BiomeDictionary tags required for this variant to spawn. Only one of the lists of tags must be matched.\n" +
                "      For example, by default we want the Red Desert mineshaft variant to spawn in rare desert AND rare mesa biomes.\n" +
                "      All desert biomes have the HOT, DRY, and SANDY tags; all mesa biomes have the MESA tag.\n" +
                "      Therefore, the biomeTags list will look like the following:\n" +
                "      \"biomeTags\": [\n" +
                "        [\n" +
                "          \"HOT\",\n" +
                "          \"DRY\",\n" +
                "          \"SANDY\",\n" +
                "          \"RARE\"\n" +
                "        ],\n" +
                "        [\n" +
                "          \"MESA\",\n" +
                "          \"RARE\"\n" +
                "        ]\n" +
                "      ]\n" +
                "      NOTE that the biomeTags property is IGNORED for the defaultVariant, since the defaultVariant simply acts as the variant for all\n" +
                "         the biomes in the world that don't meet the criteria for any of the variants in the \"variants\" list.\n" +
                "      * SEE THE biomeTags.txt FILE FOR A LIST OF ALL BIOME TAGS *\n" +
                " - mainSelector: the BlockSelector (see below) used for generating the mineshaft's walls and ceiling\n" +
                " - floorSelector: the BlockSelector (see below) used for generating the mineshaft's floor\n" +
                " - brickSelector: the BlockSelector (see below) used for generating areas of the mineshaft where brick-like blocks would be more appropriate.\n" +
                "      This includes abandoned workstations, workstation cellars, and the doorway at the end of the main shafts for mineshafts containing surface openings.\n" +
                " - legSelector: the BlockSetSelector (see below) used for generating the 'legs' of the main mineshaft.\n" +
                "      These are the supports that form underneath the main mineshaft tunnel when the mineshaft spawns over a big opening.\n" +
                " - mainBlock: The main thematic block for the mineshaft. You will often want this to be the same as the defaultBlock in the mainSelector and floorSelector properties (see above).\n" +
                "      Used as the base and top of the small supports generated throughout the mineshaft.\n" +
                "      Also used as the floor for bridging gaps the mineshaft might spawn over.\n" +
                " - supportBlock: Used as the middle section of the small supports generated throughout the mineshaft.\n" +
                "      Also used as the supports in rooms with ladders found in the small shafts.\n" +
                "      Also used in Type 1 Leg Variants (see below).\n" +
                "      Usually this is a fence block, but it's not required to be.\n" +
                " - slabBlock: The main slab block to use. Should be a block that matches your mainBlock well.\n" +
                " - gravelBlock: The block used for gravel deposits placed randomly throughout mineshafts.\n" +
                "      Usually gravel, sand, or snow.\n" +
                " - stoneWallBlock: The block used to frame the left and right sides of the doorway in the main shaft leading to the surface entrance, if present.\n" +
                "      This is a very minor piece and doesn't matter much. If you aren't sure, use your mainBlock or one of the blocks in your brickSelector.\n" +
                " - stoneSlabBlock: The block used to frame the top side of the doorway in the main shaft leading to the surface entrance, if present.\n" +
                "      This is a very minor piece and doesn't matter much. If you aren't sure, use your mainBlock or one of the blocks in your brickSelector.\n" +
                " - vineChance: chance of vines spawning in the mineshaft\n" +
                " - snowChance: chance of snow spawning on the floor of the mineshaft\n" +
                " - cactusChance: chance of cactus spawning in the mineshaft. Can only spawn on top of valid floor blocks (e.g. sand)\n" +
                " - deadBushChance: chance of dead bushes spawning the mineshaft. Can only spawn on top of valid floor blocks (sand, hardened clay, dirt)\n" +
                " - mushroomChance: chance of mushrooms spawning in the mineshaft. Can only spawn on top of valid floor blocks (mycelium, dirt)\n" +
                " - legVariant: The ID of the leg variant to use. ACCEPTABLE VALUES: 1, 2\n" +
                "      1: The legs used for most mineshafts. Uses the legSelector and the supportBlock.\n" +
                "      2: The legs used for ice and mushroom variants by default. Uses only the legSelector.\n" +
                " - replacementRate: The percent of existing blocks the mainSelector and floorSelector should replace.\n" +
                "      For example, if the replacementRate is .6, then 60% of the already existing stone, andesite, etc in the floors/walls/ceiling\n" +
                "      will be replaced with blocks determined by the selectors.\n" +
                "      Lowering this value preserves more of the regular worldgen blocks in the mineshaft's floors/walls/ceiling.\n" +
                "\n" +
                "BlockSelector: Describes a set of blocks and the probability of each block being chosen.\n" +
                " - entries: An object where each entry's key is a block, and each value is that block's probability of being chosen.\n" +
                "      The total sum of all probabilities SHOULD NOT exceed 1.0!\n" +
                " - defaultBlock: The block used for any leftover probability ranges.\n" +
                "      For example, if the total sum of all the probabilities of the entries is 0.6, then\n" +
                "      there is a 0.4 chance of the defaultBlock being selected.\n" +
                "\n" +
                "Here's an example block selector:\n" +
                "\"entries\": {\n" +
                "  \"minecraft:cobblestone\": 0.25,\n" +
                "  \"minecraft:air\": 0.2,\n" +
                "  \"minecraft:stonebrick[variant=stonebrick]\": 0.1\n" +
                "},\n" +
                "\"defaultBlock\": \"minecraft:planks[variant=oak]\"\n" +
                "\n" +
                "For each block, this selector has a 25% chance of returning cobblestone, 20% chance of choosing air,\n" +
                "10% chance of choosing stone bricks, and a 100 - (25 + 20 + 10) = 45% chance of choosing oak planks (since it's the default block).\n";

            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create variants.json README file!");
            }
        }
    }

    private static void createBiomeTagsTxt() {
        Path path = Paths.get(Loader.instance().getConfigDir().toString(), BMSettings.CUSTOM_CONFIG_PATH, BMSettings.VERSION_PATH, "biomeTags.txt");
        File readme = new File(path.toString());
        if (!readme.exists()) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(BiomeDictionary.Type.class, new BiomeDictionaryTypeAdapter());
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.disableHtmlEscaping();
            Gson gson = gsonBuilder.create();

            String jsonString = gson.toJson(BiomeDictionary.Type.getAll());

            String readmeText =
                "Helper file showing all available BiomeDictionary biome tags.\n" +
                "\n" +
                jsonString;
            try {
                Files.write(path, readmeText.getBytes());
            } catch (IOException e) {
                BetterMineshafts.LOGGER.error("Unable to create variants.json README file!");
            }
        }
    }

    /**
     * If the variants JSON file already exists, it loads its contents into MineshaftVariants.
     * Otherwise, it creates a default JSON and from the default options in MineshaftVariants.
     */
    public static void loadVariantsJSON() {
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
