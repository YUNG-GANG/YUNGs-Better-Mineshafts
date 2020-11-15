package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
public class BetterMineshaftStructure extends Structure<BetterMineshaftConfig> {
    public BetterMineshaftStructure(Codec<BetterMineshaftConfig> codec) {
        super(codec);
    }

    /**
     * canBeGenerated
     */
    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider, long seed, SharedSeedRandom random, int x, int z, Biome biome, ChunkPos chunkPos, BetterMineshaftConfig config) {
        random.setLargeFeatureSeed(seed, x, z);
        return random.nextDouble() < config.probability;
    }

    @Override
    public GenerationStage.Decoration func_236396_f_() {
        return GenerationStage.Decoration.UNDERGROUND_STRUCTURES;
    }

    @Override
    public IStartFactory<BetterMineshaftConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    public String getStructureName() {
        return "Better Mineshaft";
    }

    public static class Start extends StructureStart<BetterMineshaftConfig> {
        public Start(Structure<BetterMineshaftConfig> structureFeature, int chunkX, int chunkZ, MutableBoundingBox blockBox, int i, long l) {
            super(structureFeature, chunkX, chunkZ, blockBox, i, l);
        }

        @ParametersAreNonnullByDefault
        public void func_230364_a_(
            DynamicRegistries p_230364_1_,
            ChunkGenerator chunkGenerator,
            TemplateManager structureManager,
            int chunkX,
            int chunkZ,
            Biome biome,
            BetterMineshaftConfig config
        ) {
            Direction direction = Direction.NORTH;
            // Separate rand is necessary bc for some reason otherwise r is 0 every time
            SharedSeedRandom rand = new SharedSeedRandom();
            rand.setBaseChunkSeed(chunkX, chunkZ);
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
            int y = this.rand.nextInt(BMConfig.maxY - BMConfig.minY + 1) + BMConfig.minY;
            BlockPos.Mutable startingPos = new BlockPos.Mutable((chunkX << 4) + 2, y, (chunkZ << 4) + 2);

            // Entrypoint
            MineshaftPiece entryPoint = new VerticalEntrance(
                0,
                -1,
                this.rand,
                startingPos,
                direction,
                config.type
            );

            this.components.add(entryPoint);

            // Build room component. This also populates the children list, effectively building the entire mineshaft.
            // Note that no blocks are actually placed yet.
            entryPoint.buildComponent(entryPoint, this.components, this.rand);

            // Expand bounding box to encompass all children
            this.recalculateStructureSize();
        }
    }

    public enum Type implements IStringSerializable {
        NORMAL("normal"),
        MESA("mesa"),
        JUNGLE("jungle"),
        SNOW("snow"),
        ICE("ice"),
        DESERT("desert"),
        RED_DESERT("red_desert"),
        SAVANNA("savanna"),
        MUSHROOM("mushroom");

        public static final Codec<BetterMineshaftStructure.Type> field_236324_c_ = IStringSerializable.createEnumCodec(BetterMineshaftStructure.Type::values, BetterMineshaftStructure.Type::byName);
        private static final Map<String, BetterMineshaftStructure.Type> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(BetterMineshaftStructure.Type::getName, type -> type));

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        private static BetterMineshaftStructure.Type byName(String p_214715_0_) {
            return BY_NAME.get(p_214715_0_);
        }

        public static BetterMineshaftStructure.Type byId(int id) {
            return id >= 0 && id < values().length ? values()[id] : NORMAL;
        }

        public String getString() {
            return this.name;
        }
    }
}
