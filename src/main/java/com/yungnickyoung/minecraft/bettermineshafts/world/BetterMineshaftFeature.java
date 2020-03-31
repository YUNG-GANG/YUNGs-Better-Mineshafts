package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.datafixers.Dynamic;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.MineshaftFeature;
import net.minecraft.world.gen.feature.MineshaftFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BetterMineshaftFeature extends StructureFeature<BetterMineshaftFeatureConfig> {
    public BetterMineshaftFeature(Function<Dynamic<?>, ? extends BetterMineshaftFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public boolean shouldStartAt(BiomeAccess biomeAccess, ChunkGenerator<?> chunkGenerator, Random random, int chunkX, int chunkZ, Biome biome) {
        ((ChunkRandom)random).setStructureSeed(chunkGenerator.getSeed(), chunkX, chunkZ);
        if (chunkGenerator.hasStructure(biome, this)) {
            BetterMineshaftFeatureConfig featureConfig = chunkGenerator.getStructureConfig(biome, this);
            // Default to normal mineshaft in case we fail to load config for this biome
            if (featureConfig == null) {
                featureConfig = new BetterMineshaftFeatureConfig(.004, Type.NORMAL);
            }
            return random.nextDouble() < featureConfig.probability;
        } else {
            return false;
        }
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return Start::new;
    }

    @Override
    public String getName() {
        return "Better Mineshaft";
    }

    @Override
    public int getRadius() {
        return 8;
    }

    public static class Start extends StructureStart {
        public Start(StructureFeature<?> structureFeature, int chunkX, int chunkZ, BlockBox blockBox, int i, long l) {
            super(structureFeature, chunkX, chunkZ, blockBox, i, l);
        }

        public void initialize(
            ChunkGenerator<?> chunkGenerator,
            StructureManager structureManager,
            int chunkX,
            int chunkZ,
            Biome biome
        ) {
            BetterMineshaftFeatureConfig mineshaftFeatureConfig =
                chunkGenerator.getStructureConfig(biome, BetterMineshafts.BETTER_MINESHAFT_FEATURE);
            BetterMineshaftGenerator.MineshaftRoom mineshaftRoom = new BetterMineshaftGenerator.MineshaftRoom(
                0,
                this.random,
                (chunkX << 4) + 2,
                (chunkZ << 4) + 2,
                mineshaftFeatureConfig.type
            );
            this.children.add(mineshaftRoom);
            mineshaftRoom.method_14918(mineshaftRoom, this.children, this.random);
            this.setBoundingBoxFromChildren();
            if (mineshaftFeatureConfig.type == BetterMineshaftFeature.Type.MESA) {
                int j = chunkGenerator.getSeaLevel() - this.boundingBox.maxY + this.boundingBox.getBlockCountY() / 2 + 5;
                this.boundingBox.offset(0, j, 0);
                children.forEach(structurePiece -> structurePiece.translate(0, j, 0));
            } else {
                this.method_14978(chunkGenerator.getSeaLevel(), this.random, 10);
            }
        }
    }

    public enum Type {
        NORMAL("normal"),
        MESA("mesa");

        private final String name;

        private Type(String name) {
            this.name = name;
        }

        private static final Map<String, Type> nameMap = Arrays.stream(values())
            .collect(Collectors.toMap(Type::getName, type -> type)
            );

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
