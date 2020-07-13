package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.init.Biomes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

@MethodsReturnNonnullByDefault
public class MapGenBetterMineshaft extends MapGenMineshaft {
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return this.rand.nextDouble() < Configuration.mineshaftSpawnRate;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        Biome biome = this.world.getBiome(new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8));

        // Determine mineshaft type based on biome
        Type type;
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH)) {
            type = Type.NULL; // No mineshafts in or near oceans
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
            if (biome == Biomes.MESA || isRare(biome)) {
                type = getType(Type.RED_DESERT);
            } else {
                type = getType(Type.MESA);
            }
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
            type = getType(Type.JUNGLE);
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
            if (biome == Biomes.MUTATED_ICE_FLATS || isRare(biome)) {
                type = getType(Type.ICE);
            } else {
                type = getType(Type.SNOW);
            }
        } else if ((BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)) || biome instanceof BiomeDesert) {
            if (biome == Biomes.MUTATED_DESERT || isRare(biome)) {
                type = getType(Type.RED_DESERT);
            } else {
                type = getType(Type.DESERT);
            }
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM) || biome instanceof BiomeMushroomIsland) {
            type = getType(Type.MUSHROOM);
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) || biome instanceof BiomeSavanna) {
            type = getType(Type.SAVANNA);
        } else {
            type = Type.NORMAL;
        }

        return new Start(this.world, this.rand, chunkX, chunkZ, type);
    }

    /**
     * Returns the given type if its mineshaft variant is enabled.
     * Otherwise returns a backup type that is enabled.
     */
    private static Type getType(Type type) {
        if (type == Type.RED_DESERT) {
            if (Configuration.redDesertEnabled) return Type.RED_DESERT;
            if (Configuration.desertEnabled) return Type.DESERT;
        } else if (type == Type.DESERT) {
            if (Configuration.desertEnabled) return Type.DESERT;
        } else if (type == Type.MESA) {
            if (Configuration.mesaEnabled) return Type.MESA;
            if (Configuration.redDesertEnabled) return Type.RED_DESERT;
        } else if (type == Type.ICE) {
            if (Configuration.iceEnabled) return Type.ICE;
            if (Configuration.snowEnabled) return Type.SNOW;
        } else if (type == Type.SNOW) {
            if (Configuration.snowEnabled) return Type.SNOW;
        } else if (type == Type.JUNGLE) {
            if (Configuration.jungleEnabled) return Type.JUNGLE;
        } else if (type == Type.SAVANNA) {
            if (Configuration.savannaEnabled) return Type.SAVANNA;
        } else if (type == Type.MUSHROOM) {
            if (Configuration.mushroomEnabled) return Type.MUSHROOM;
        }
        return Type.NORMAL;
    }

    private static boolean isRare(Biome biome) {
        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.RARE);
    }

    public static class Start extends StructureStart {
        private Type mineshaftType;

        public Start() {
        }

        public Start(World world, Random random, int chunkX, int chunkZ, Type type) {
            super(chunkX, chunkZ);
            this.mineshaftType = type;

            EnumFacing direction = EnumFacing.NORTH;
            int r = random.nextInt(4);
            switch (r) {
                case 0:
                    direction = EnumFacing.NORTH;
                    break;
                case 1:
                    direction = EnumFacing.SOUTH;
                    break;
                case 2:
                    direction = EnumFacing.EAST;
                    break;
                case 3:
                    direction = EnumFacing.WEST;
            }
            int y = random.nextInt(Configuration.maxY - Configuration.minY + 1) + Configuration.minY;
            BlockPos.MutableBlockPos startingPos = new BlockPos.MutableBlockPos((chunkX << 4) + 2, y, (chunkZ << 4) + 2);

            // Entrypoint
            MineshaftPiece entryPoint = new VerticalEntrance(
                0,
                -1,
                random,
                startingPos,
                direction,
                mineshaftType
            );

            this.components.add(entryPoint);

            // Build room component. This also populates the children list, effectively building the entire mineshaft.
            // Note that no blocks are actually placed yet.
            entryPoint.buildComponent(entryPoint, this.components, random);

            // Expand bounding box to encompass all children
            this.updateBoundingBox();
        }
    }

    public enum Type {
        NULL,
        NORMAL,
        MESA,
        JUNGLE,
        SNOW,
        ICE,
        DESERT,
        RED_DESERT,
        SAVANNA,
        MUSHROOM;

        public static Type byId(int id) {
            return id >= 0 && id < values().length ? values()[id] : NORMAL;
        }
    }
}
