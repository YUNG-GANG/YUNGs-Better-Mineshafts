package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.datafixers.Dynamic;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.MineshaftFeature;
import net.minecraft.world.gen.feature.MineshaftFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BetterMineshaftFeature extends MineshaftFeature {
    public BetterMineshaftFeature(Function<Dynamic<?>, ? extends MineshaftFeatureConfig> configFactory) {
        super(configFactory);
    }

    @Override
    public String getName() {
        return "Better Mineshaft";
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return Start::new;
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
            MineshaftFeatureConfig mineshaftFeatureConfig =
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
            if (mineshaftFeatureConfig.type == MineshaftFeature.Type.MESA) {
                int j = chunkGenerator.getSeaLevel() - this.boundingBox.maxY + this.boundingBox.getBlockCountY() / 2 - -5;
                this.boundingBox.offset(0, j, 0);
                Iterator<StructurePiece> var10 = this.children.iterator();

                while (var10.hasNext()) {
                    StructurePiece structurePiece = var10.next();
                    structurePiece.translate(0, j, 0);
                }
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
