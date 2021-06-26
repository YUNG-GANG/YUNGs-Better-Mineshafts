package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BetterMineshaftStructure extends StructureFeature<BetterMineshaftFeatureConfig> {
    public BetterMineshaftStructure(Codec<BetterMineshaftFeatureConfig> codec) {
        super(codec);
    }

    @Override
    protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long worldSeed, ChunkRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, BetterMineshaftFeatureConfig config, HeightLimitView world) {
        random.setCarverSeed(worldSeed, pos.getRegionRelativeX(), pos.getRegionRelativeZ());
        return random.nextDouble() < config.probability;
    }

    @Override
    public StructureStartFactory<BetterMineshaftFeatureConfig> getStructureStartFactory() {
        return Start::new;
    }

    @Override
    public GenerationStep.Feature getGenerationStep() {
        return GenerationStep.Feature.UNDERGROUND_STRUCTURES;
    }

    public static class Start extends StructureStart<BetterMineshaftFeatureConfig> {
        public Start(StructureFeature<BetterMineshaftFeatureConfig> feature, ChunkPos pos, int references, long seed) {
            super(feature, pos, references, seed);
        }

        public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos pos, Biome biome, BetterMineshaftFeatureConfig config, HeightLimitView world) {
            Direction direction = Direction.NORTH;

            // Randomly choose starting direction.
            // Separate rand is necessary bc for some reason otherwise r is 0 every time.
            ChunkRandom rand = new ChunkRandom();
            rand.setTerrainSeed(pos.getRegionRelativeX(), pos.getRegionRelativeZ());
            int r = rand.nextInt(4);
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
            int y = random.nextInt(BetterMineshafts.CONFIG.maxY - BetterMineshafts.CONFIG.minY + 1) + BetterMineshafts.CONFIG.minY;
            BlockPos.Mutable startingPos = new BlockPos.Mutable((pos.getRegionRelativeX() << 4) + 2, y, (pos.getRegionRelativeZ() << 4) + 2);

            // Entrypoint
            MineshaftPiece entryPoint = new VerticalEntrance(
                    -1,
                    this.random,
                    startingPos,
                    direction,
                    config.type
            );

            this.addPiece(entryPoint);

            // Build room component. This also populates the children list, effectively building the entire mineshaft.
            // Note that no blocks are actually placed yet.
            entryPoint.fillOpenings(entryPoint, this, this.random);

            // Expand bounding box to encompass all children
            this.setBoundingBoxFromChildren();
        }
    }

    public enum Type implements StringIdentifiable {
        NORMAL("normal"),
        MESA("mesa"),
        JUNGLE("jungle"),
        SNOW("snow"),
        ICE("ice"),
        DESERT("desert"),
        RED_DESERT("red_desert"),
        SAVANNA("savanna"),
        MUSHROOM("mushroom");

        public static final Codec<Type> CODEC = StringIdentifiable.createCodec(Type::values, Type::byName);
        private static final Map<String, Type> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Type::getName, type -> type));

        private final String name;

        Type(String name) {
            this.name = name;
        }

        private static Type byName(String name) {
            return BY_NAME.get(name);
        }

        public static Type byIndex(int index) {
            return index >= 0 && index < values().length ? values()[index] : NORMAL;
        }

        public String asString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
