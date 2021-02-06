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

        // Determine mineshaft settings
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

    public static class Start extends StructureStart {

        public Start() {
        }

        public Start(World world, Random random, int chunkX, int chunkZ, MineshaftVariantSettings settings) {
            super(chunkX, chunkZ);

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
            int minY = settings.minY == 0 ? 17 : settings.minY;
            int maxY = settings.maxY == 0 ? 37 : settings.maxY;
            int y = random.nextInt(maxY - minY + 1) + minY;
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
}
