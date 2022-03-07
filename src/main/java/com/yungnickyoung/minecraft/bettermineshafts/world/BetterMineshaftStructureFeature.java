package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMConfig;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import com.yungnickyoung.minecraft.bettermineshafts.world.variant.MineshaftVariantSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.variant.MineshaftVariants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraftforge.common.BiomeDictionary;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class BetterMineshaftStructureFeature extends StructureFeature<NoneFeatureConfiguration> {
    public BetterMineshaftStructureFeature() {
        super(NoneFeatureConfiguration.CODEC, context -> {
            // Only generate if location is valid
            if (!checkLocation(context)) {
                return Optional.empty();
            }

            return Optional.of(BetterMineshaftStructureFeature::generatePieces);
        });
    }

    @Override
    public GenerationStep.@NotNull Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<NoneFeatureConfiguration> context) {
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenRandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        return worldgenRandom.nextDouble() < BMConfig.mineshaftSpawnRate.get() &&
                context.validBiome().test(context.chunkGenerator().getNoiseBiome(
                        QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()),
                        QuartPos.fromBlock(50),
                        QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ()))
                );
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<NoneFeatureConfiguration> context) {
        Direction direction = Direction.NORTH;

        // Randomly choose starting direction.
        // Separate rand is necessary bc for some reason otherwise r is 0 every time.
        WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(0));
        rand.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        int r = rand.nextInt(4);
        switch (r) {
            case 0 -> direction = Direction.NORTH;
            case 1 -> direction = Direction.SOUTH;
            case 2 -> direction = Direction.EAST;
            case 3 -> direction = Direction.WEST;
        }
        int x = context.chunkPos().getMiddleBlockX();
        int z = context.chunkPos().getMiddleBlockZ();
        int y = context.random().nextInt(BMConfig.maxY.get() - BMConfig.minY.get() + 1) + BMConfig.minY.get();
        BlockPos.MutableBlockPos startingPos = new BlockPos.MutableBlockPos(context.chunkPos().getBlockX(3), y, context.chunkPos().getBlockZ(3));

        // Determine mineshaft variant based on biome
        Biome biome = context.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(x), QuartPos.fromBlock(y), QuartPos.fromBlock(z)).value();
        MineshaftVariantSettings settings = getSettingsForBiome(biome);

        // Entrypoint
        MineshaftPiece entryPoint = new VerticalEntrance(
                -1,
                context.random(),
                startingPos,
                direction,
                settings
        );

        structurePiecesBuilder.addPiece(entryPoint);

        // Build room component. This also populates the children list, effectively building the entire mineshaft.
        // Note that no blocks are actually placed yet.
        entryPoint.addChildren(entryPoint, structurePiecesBuilder, context.random());
    }

    private static MineshaftVariantSettings getSettingsForBiome(Biome biome) {
        ResourceKey<Biome> registryKey;

        // Ensure biome registry name isn't null. This should never happen.
        if (biome.getRegistryName() == null) {
            BetterMineshafts.LOGGER.error("Found null registry name for biome {}. This shouldn't happen!", biome);
            return MineshaftVariants.get().getDefault();
        }

        registryKey = ResourceKey.create(Registry.BIOME_REGISTRY, biome.getRegistryName());

        // Search tag lists of variants top-down, short-circuiting if we find a matching tag list.
        // Each variant has can have multiple tag lists. In order for a variant to match,
        // a biome must contain ALL of the tags in ANY SINGLE ONE of a variant's tag lists.
        boolean found;
        for (MineshaftVariantSettings variant : MineshaftVariants.get().getVariants()) {
            for (List<BiomeDictionary.Type> tagList : variant.biomeTags) {
                found = true;
                for (BiomeDictionary.Type tag : tagList) {
                    // Check tag
                    if (!BiomeDictionary.hasType(registryKey, tag)) {
                        found = false;
                        break;
                    }
                }

                if (found) {
                    return variant;
                }
            }
        }

        // No match --> return default
        return MineshaftVariants.get().getDefault();
    }
}
