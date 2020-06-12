package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.datafixers.Dynamic;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
public class BetterMineshaftStructure extends Structure<BetterMineshaftFeatureConfig> {
    public BetterMineshaftStructure(Function<Dynamic<?>, ? extends BetterMineshaftFeatureConfig> configFactory) {
        super(configFactory);
    }

    /**
     * shouldStartAt
     */
    @Override
    @ParametersAreNonnullByDefault
    public boolean func_225558_a_(BiomeManager biomeManager, ChunkGenerator<?> chunkGenerator, Random random, int chunkX, int chunkZ, Biome biome) {
        ((SharedSeedRandom) random).setLargeFeatureSeed(chunkGenerator.getSeed(), chunkX, chunkZ);
        if (chunkGenerator.hasStructure(biome, this)) {
            BetterMineshaftFeatureConfig featureConfig = chunkGenerator.getStructureConfig(biome, this);
            // Default to normal mineshaft in case we fail to load config for this biome
            if (featureConfig == null) {
                featureConfig = new BetterMineshaftFeatureConfig(BMStructure.SPAWN_RATE, Type.NORMAL);
            }
            return random.nextDouble() < featureConfig.probability;
        } else {
            return false;
        }
    }

    @Override
    public IStartFactory getStartFactory() {
        return Start::new;
    }

    @Override
    public String getStructureName() {
        return "Mineshaft";
    }

    @Override
    public int getSize() {
        return 12;
    }

    public static class Start extends StructureStart {
        public Start(Structure<?> structureFeature, int chunkX, int chunkZ, MutableBoundingBox blockBox, int i, long l) {
            super(structureFeature, chunkX, chunkZ, blockBox, i, l);
        }

        @ParametersAreNonnullByDefault
        public void init(
            ChunkGenerator<?> chunkGenerator,
            TemplateManager structureManager,
            int chunkX,
            int chunkZ,
            Biome biome
        ) {
            BetterMineshaftFeatureConfig featureConfig =
                chunkGenerator.getStructureConfig(biome, BMStructure.BETTER_MINESHAFT_FEATURE);
            if (featureConfig == null) { // Default to normal mineshaft in case we fail to load config for this biome
                featureConfig = new BetterMineshaftFeatureConfig(.003, Type.NORMAL);
            }
            Direction direction = Direction.NORTH;
            // Separate rand is necessary bc for some reason otherwise r is 0 every time
            SharedSeedRandom rand = new SharedSeedRandom();
            rand.setBaseChunkSeed(chunkX, chunkZ);
            int r = rand.nextInt(4);
//            BetterMineshafts.LOGGER.info(r);
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
//            direction = Direction.NORTH;
            BlockPos.Mutable startingPos = new BlockPos.Mutable((chunkX << 4) + 2, 50, (chunkZ << 4) + 2);

            // Entrypoint
            MineshaftPiece entryPoint = new VerticalEntrance(
                0,
                -1,
                this.rand,
                startingPos,
                direction,
                featureConfig.type
            );

            this.components.add(entryPoint);

            // Build room component. This also populates the children list, effectively building the entire mineshaft.
            // Note that no blocks are actually placed yet.
            entryPoint.buildComponent(entryPoint, this.components, this.rand);

            // Expand bounding box to encompass all children
            this.recalculateStructureSize();

            // y-coordinate adjustments
//            if (featureConfig.type == BetterMineshaftFeature.Type.MESA) {
//                // The following is logic to make mesa mineshafts higher up
//                int yOffset = chunkGenerator.getSeaLevel() - this.boundingBox.maxY + this.boundingBox.getBlockCountY() / 2 + 5;
//                this.boundingBox.offset(0, yOffset, 0);
//                children.forEach(structurePiece -> structurePiece.translate(0, yOffset, 0));
//            }
//            else {
//                // Method for adjusting y of all structure pieces (similar to above)
//                this.method_14978(chunkGenerator.getSeaLevel(), this.random, 10);
//            }
            // Method for adjusting y of all structure pieces
//            this.method_14978(chunkGenerator.getSeaLevel(), this.random, 10);
        }
    }

    public enum Type {
        NORMAL("normal"),
        MESA("mesa"),
        JUNGLE("jungle"),
        SNOW("snow"),
        ICE("ice"),
        DESERT("desert"),
        RED_DESERT("red_desert"),
        SAVANNA("savanna"),
        MUSHROOM("mushroom");

        private final String name;

        private Type(String name) {
            this.name = name;
        }

        private static final Map<String, Type> nameMap = Arrays.stream(values())
            .collect(Collectors.toMap(Type::getName, type -> type));

        public String getName() {
            return this.name;
        }

        public static Type byName(String name) {
            return nameMap.get(name);
        }

        public static Type byIndex(int index) {
            return index >= 0 && index < values().length ? values()[index] : NORMAL;
        }
    }
}
