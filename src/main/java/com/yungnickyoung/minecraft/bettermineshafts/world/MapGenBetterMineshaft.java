package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

@MethodsReturnNonnullByDefault
public class MapGenBetterMineshaft extends MapGenMineshaft {
    private static final double SPAWN_RATE = 0.003;

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        // No mineshafts in oceans
        Biome biome = this.world.getBiomeForCoordsBody(new BlockPos(chunkX << 4, 64, chunkZ << 4));
        if (biome instanceof BiomeOcean || biome.getTempCategory() == Biome.TempCategory.OCEAN)
            return false;

        return this.rand.nextDouble() < SPAWN_RATE && this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ));
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        Biome biome = this.world.getBiome(new BlockPos((chunkX << 4) + 8, 64, (chunkZ << 4) + 8));
        float r = this.rand.nextFloat();

        // Determine mineshaft type based on biome
        Type type;
        if (biome instanceof BiomeMesa) {
            type = r > .25f ? Type.MESA : Type.RED_DESERT;
        } else if (biome instanceof BiomeJungle) {
            type = Type.JUNGLE;
        } else if (biome instanceof BiomeTaiga || biome instanceof BiomeSnow) {
            type = r > .25f ? Type.SNOW : Type.ICE;
        } else if (biome instanceof BiomeDesert) {
            type = r > .25f ? Type.DESERT : Type.RED_DESERT;
        } else if (biome instanceof BiomeMushroomIsland) {
            type = Type.MUSHROOM;
        } else if (biome instanceof BiomeSavanna) {
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
            // Separate rand is necessary bc for some reason otherwise r is 0 every time
            Random rand = new Random();
            rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
            int r = rand.nextInt(4);
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
