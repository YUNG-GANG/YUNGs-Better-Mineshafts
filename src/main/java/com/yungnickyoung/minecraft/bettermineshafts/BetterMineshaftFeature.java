package com.yungnickyoung.minecraft.bettermineshafts;

import com.mojang.datafixers.Dynamic;
import net.minecraft.structure.MineshaftGenerator;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MineshaftFeature;
import net.minecraft.world.gen.feature.MineshaftFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Iterator;
import java.util.function.Function;

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

        public void initialize(ChunkGenerator<?> chunkGenerator, StructureManager structureManager, int x, int z, Biome biome) {
            MineshaftFeatureConfig mineshaftFeatureConfig = chunkGenerator.getStructureConfig(biome, BetterMineshafts.betterMineshaft);
            BetterMineshaftGenerator.MineshaftRoom mineshaftRoom = new BetterMineshaftGenerator.MineshaftRoom(0, this.random, (x << 4) + 2, (z << 4) + 2, mineshaftFeatureConfig.type);
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
}
