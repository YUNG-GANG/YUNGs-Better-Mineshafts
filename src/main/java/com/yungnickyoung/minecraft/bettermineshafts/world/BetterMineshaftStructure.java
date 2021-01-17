package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariants;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
public class BetterMineshaftStructure extends Structure<NoFeatureConfig> {
    public BetterMineshaftStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    /**
     * Returns true if the structure should generate.
     * The approach taken here is kind of legacy at this point, and should be avoided. Instead,
     * configure the structure's StructureSeparationSettings accordingly.
     * See {@link com.yungnickyoung.minecraft.bettermineshafts.init.ModStructures#commonSetup}
     */
    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int x, int z, Biome biome, ChunkPos chunkPos, NoFeatureConfig config) {
        random.setLargeFeatureSeed(seed, x, z);
        return random.nextDouble() < Configuration.mineshaftSpawnRate.get();
    }

    /**
     * Returns the Decoration stage for this structure.
     */
    @Override
    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    /**
     * Returns the name displayed when the locate command is used.
     * I believe (not 100% sure) that the lowercase form of this value must also match
     * the key of the entry added to Structure.field_236365_a_ during common setup.
     *
     * See {@link com.yungnickyoung.minecraft.bettermineshafts.init.ModStructures#commonSetup}
     */
    @Override
    public String getStructureName() {
        return "Better Mineshaft";
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureFeature, int chunkX, int chunkZ, MutableBoundingBox blockBox, int i, long l) {
            super(structureFeature, chunkX, chunkZ, blockBox, i, l);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator chunkGenerator, TemplateManager structureManager, int chunkX, int chunkZ, Biome biome, NoFeatureConfig config) {
            Direction direction = Direction.NORTH;
            // Separate rand is necessary bc for some reason otherwise r is 0 every time
            SharedSeedRandom rand = new SharedSeedRandom();
            rand.setBaseChunkSeed(chunkX, chunkZ);
            int r = rand.nextInt(4);

            // Determine starting position and direction
            switch (r) {
                case 0:
                    direction = Direction.NORTH;
                    break;
                case 1:
                    direction = Direction.SOUTH;
                    break;
                case 2:
                    direction = Direction.EAST;
                    break;
                case 3:
                    direction = Direction.WEST;
            }
            int y = this.rand.nextInt(Configuration.maxY.get() - Configuration.minY.get() + 1) + Configuration.minY.get();
            BlockPos.Mutable startingPos = new BlockPos.Mutable((chunkX << 4) + 2, y, (chunkZ << 4) + 2);

            // Determine mineshaft variant based on biome
            MineshaftVariantSettings settings = getSettingsForBiome(biome);

            // Entrypoint
            MineshaftPiece entryPoint = new VerticalEntrance(
                -1,
                this.rand,
                startingPos,
                direction,
                settings
            );

            this.components.add(entryPoint);

            // Build room component. This also populates the children list, effectively building the entire mineshaft.
            // Note that no blocks are actually placed yet.
            entryPoint.buildComponent(entryPoint, this.components, this.rand);

            // Expand bounding box to encompass all children
            this.recalculateStructureSize();
        }

        private MineshaftVariantSettings getSettingsForBiome(Biome biome) {
            RegistryKey<Biome> registryKey;

            // Ensure biome registry name isn't null. This should never happen.
            if (biome.getRegistryName() == null) {
                BetterMineshafts.LOGGER.error("Found null registry name for biome {}. This shouldn't happen!", biome);
                return MineshaftVariants.get().getDefault();
            }

            registryKey = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, biome.getRegistryName());

            // Search tag lists of variants top-down, short-circuiting if we find a matching tag list.
            boolean found;
            for (MineshaftVariantSettings variant : MineshaftVariants.get().getVariants()) {
                for (List<BiomeDictionary.Type> tagList : variant.biomeTags) {
                    found = true;
                    for (BiomeDictionary.Type tag : tagList) {
                        // Check tag
                        if (!BiomeDictionary.hasType(registryKey, tag)) {
                            found = false;
                            break;
                        }
                    }

                    if (found) {
                        return variant;
                    }
                }
            }

            // No match --> return default
            return MineshaftVariants.get().getDefault();
        }
    }
}
