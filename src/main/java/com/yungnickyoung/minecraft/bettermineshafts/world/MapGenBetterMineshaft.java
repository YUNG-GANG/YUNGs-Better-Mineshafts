package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import mcp.MethodsReturnNonnullByDefault;
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
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA) || biome instanceof BiomeMesa) {
            type = Type.MESA;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE) || biome instanceof BiomeJungle) {
            type = Type.JUNGLE;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
            type = BiomeDictionary.hasType(biome, BiomeDictionary.Type.RARE) ? Type.ICE : Type.SNOW;
        } else if ((BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)) || biome instanceof BiomeDesert) {
            type = BiomeDictionary.hasType(biome, BiomeDictionary.Type.RARE) ? Type.RED_DESERT : Type.DESERT;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM) || biome instanceof BiomeMushroomIsland) {
            type = Type.MUSHROOM;
        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) || biome instanceof BiomeSavanna) {
            type = Type.SAVANNA;
        } else {
            type = Type.NORMAL;
        }

        return new Start(this.world, this.rand, chunkX, chunkZ, type);
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
            BlockPos.MutableBlockPos startingPos = new BlockPos.MutableBlockPos((chunkX << 4) + 2, 50, (chunkZ << 4) + 2);

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
