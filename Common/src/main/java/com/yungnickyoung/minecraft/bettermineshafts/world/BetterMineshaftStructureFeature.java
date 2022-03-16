package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.BetterMineshaftFeatureConfiguration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.BetterMineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BetterMineshaftStructureFeature extends StructureFeature<BetterMineshaftFeatureConfiguration> {
    public BetterMineshaftStructureFeature() {
        super(BetterMineshaftFeatureConfiguration.CODEC, context -> {
            // Only generate if location is valid
            if (!checkLocation(context)) {
                return Optional.empty();
            }

            return Optional.of(BetterMineshaftStructureFeature::generatePieces);
        });
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<BetterMineshaftFeatureConfiguration> context) {
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenRandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        return worldgenRandom.nextDouble() < BetterMineshaftsCommon.CONFIG.mineshaftSpawnRate &&
                context.validBiome().test(context.chunkGenerator().getNoiseBiome(
                        QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()),
                        QuartPos.fromBlock(50),
                        QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ()))
                );
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<BetterMineshaftFeatureConfiguration> context) {
        // Randomly choose starting direction.
        WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(0));
        rand.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(rand);

        // Choose random y
        int y = context.random().nextInt(BetterMineshaftsCommon.CONFIG.maxY - BetterMineshaftsCommon.CONFIG.minY + 1) + BetterMineshaftsCommon.CONFIG.minY;
        BlockPos.MutableBlockPos startingPos = new BlockPos.MutableBlockPos(context.chunkPos().getBlockX(3), y, context.chunkPos().getBlockZ(3));

        // Entrypoint
        BetterMineshaftPiece entryPoint = new VerticalEntrance(
                -1,
                context.random(),
                startingPos,
                direction,
                context.config()
        );

        structurePiecesBuilder.addPiece(entryPoint);

        // Build room component. This also populates the children list, effectively building the entire mineshaft.
        // Note that no blocks are actually placed yet.
        entryPoint.addChildren(entryPoint, structurePiecesBuilder, context.random());
    }

    public enum LegVariant implements StringRepresentable {
        EDGE("edge"),
        INNER("inner");

        public static final Codec<LegVariant> CODEC = StringRepresentable.fromEnum(LegVariant::values, LegVariant::byName);
        private static final Map<String, LegVariant> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(LegVariant::getName, (variant) -> variant));

        private final String name;

        LegVariant(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        private static LegVariant byName(String name) {
            return BY_NAME.get(name);
        }

        public static LegVariant byId(int id) {
            return id >= 0 && id < values().length ? values()[id] : EDGE;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}
