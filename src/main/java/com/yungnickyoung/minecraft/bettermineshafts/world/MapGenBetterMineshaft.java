package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.MineshaftVariants;
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

import java.util.List;
import java.util.Random;

@MethodsReturnNonnullByDefault
public class MapGenBetterMineshaft extends MapGenMineshaft {
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        Biome biome = this.world.getBiome(new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8));
        // No mineshafts in or near oceans
        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH)) {
            return false;
        }
        return this.rand.nextDouble() < Configuration.mineshaftSpawnRate;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        Biome biome = this.world.getBiome(new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8));

        // Determine mineshaft type based on biome
//        Type type;
//        if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN) || BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH)) {
//            type = Type.NULL; // No mineshafts in or near oceans
//        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
//            if (biome == Biomes.MESA || isRare(biome)) {
//                type = getType(Type.RED_DESERT);
//            } else {
//                type = getType(Type.MESA);
//            }
//        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
//            type = getType(Type.JUNGLE);
//        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
//            if (biome == Biomes.MUTATED_ICE_FLATS || isRare(biome)) {
//                type = getType(Type.ICE);
//            } else {
//                type = getType(Type.SNOW);
//            }
//        } else if ((BiomeDictionary.hasType(biome, BiomeDictionary.Type.HOT) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)) || biome instanceof BiomeDesert) {
//            if (biome == Biomes.MUTATED_DESERT || isRare(biome)) {
//                type = getType(Type.RED_DESERT);
//            } else {
//                type = getType(Type.DESERT);
//            }
//        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.MUSHROOM) || biome instanceof BiomeMushroomIsland) {
//            type = getType(Type.MUSHROOM);
//        } else if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) || biome instanceof BiomeSavanna) {
//            type = getType(Type.SAVANNA);
//        } else {
//            type = Type.NORMAL;
//        }
//
//        return new Start(this.world, this.rand, chunkX, chunkZ, type);

        MineshaftVariantSettings settings = MineshaftVariants.get().getDefault();
        boolean found = true;
        for (MineshaftVariantSettings variant : MineshaftVariants.get().getVariants()) {
            for (List<BiomeDictionary.Type> tagList : variant.biomeTags) {
                found = true;
                for (BiomeDictionary.Type tag : tagList) {
                    if (!BiomeDictionary.hasType(biome, tag)) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    settings = variant;
                    break;
                }
            }
            if (found) break;
        }

        return new Start(this.world, this.rand, chunkX, chunkZ, settings);
    }

    /**
     * Returns the given type if its mineshaft variant is enabled.
     * Otherwise returns a backup type that is enabled.
     */
//    private static Type getType(Type type) {
//        if (type == Type.RED_DESERT) {
//            if (Configuration.biomeVariants.redDesertEnabled) return Type.RED_DESERT;
//            if (Configuration.biomeVariants.desertEnabled) return Type.DESERT;
//        } else if (type == Type.DESERT) {
//            if (Configuration.biomeVariants.desertEnabled) return Type.DESERT;
//        } else if (type == Type.MESA) {
//            if (Configuration.biomeVariants.mesaEnabled) return Type.MESA;
//            if (Configuration.biomeVariants.redDesertEnabled) return Type.RED_DESERT;
//        } else if (type == Type.ICE) {
//            if (Configuration.biomeVariants.iceEnabled) return Type.ICE;
//            if (Configuration.biomeVariants.snowEnabled) return Type.SNOW;
//        } else if (type == Type.SNOW) {
//            if (Configuration.biomeVariants.snowEnabled) return Type.SNOW;
//        } else if (type == Type.JUNGLE) {
//            if (Configuration.biomeVariants.jungleEnabled) return Type.JUNGLE;
//        } else if (type == Type.SAVANNA) {
//            if (Configuration.biomeVariants.savannaEnabled) return Type.SAVANNA;
//        } else if (type == Type.MUSHROOM) {
//            if (Configuration.biomeVariants.mushroomEnabled) return Type.MUSHROOM;
//        }
//        return Type.NORMAL;
//    }
//
//    private static boolean isRare(Biome biome) {
//        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.RARE);
//    }

    public static class Start extends StructureStart {
//        private Type mineshaftType;
        private MineshaftVariantSettings settings;

        public Start() {
        }

        public Start(World world, Random random, int chunkX, int chunkZ, MineshaftVariantSettings settings) {
            super(chunkX, chunkZ);
            this.settings = settings;

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
                settings
            );

            this.components.add(entryPoint);

            // Build room component. This also populates the children list, effectively building the entire mineshaft.
            // Note that no blocks are actually placed yet.
            entryPoint.buildComponent(entryPoint, this.components, random);

            // Expand bounding box to encompass all children
            this.updateBoundingBox();
        }
    }

//    public enum Type {
//        NULL,
//        NORMAL,
//        MESA,
//        JUNGLE,
//        SNOW,
//        ICE,
//        DESERT,
//        RED_DESERT,
//        SAVANNA,
//        MUSHROOM;
//
//        public static Type byId(int id) {
//            return id >= 0 && id < values().length ? values()[id] : NORMAL;
//        }
//    }
}
