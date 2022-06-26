package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureTypeModule;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.BetterMineshaftConfiguration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.BetterMineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Optional;

public class BetterMineshaftStructure extends Structure {
    public static final Codec<BetterMineshaftStructure> CODEC = RecordCodecBuilder.create((builder) -> builder
            .group(
                    settingsCodec(builder),
                    BetterMineshaftConfiguration.CODEC.fieldOf("config").forGetter((structure) -> structure.config)
            ).apply(builder, BetterMineshaftStructure::new));

    private final BetterMineshaftConfiguration config;

    public BetterMineshaftStructure(Structure.StructureSettings settings, BetterMineshaftConfiguration config) {
        super(settings);
        this.config = config;
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        context.random().nextDouble();
        ChunkPos chunkPos = context.chunkPos();

        // Choose random y
        int y = context.random().nextInt(BetterMineshaftsCommon.CONFIG.maxY - BetterMineshaftsCommon.CONFIG.minY + 1) + BetterMineshaftsCommon.CONFIG.minY;
        BlockPos.MutableBlockPos startingPos = new BlockPos.MutableBlockPos(chunkPos.getBlockX(3), y, chunkPos.getBlockZ(3));

        StructurePiecesBuilder structurePiecesBuilder = new StructurePiecesBuilder();
        generatePieces(structurePiecesBuilder, context);
        return Optional.of(new Structure.GenerationStub(startingPos, Either.right(structurePiecesBuilder)));
    }

    @Override
    public StructureType<?> type() {
        return StructureTypeModule.BETTER_MINESHAFT;
    }

    private void generatePieces(StructurePiecesBuilder structurePiecesBuilder, Structure.GenerationContext context) {
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
                startingPos,
                direction,
                this.config,
                context.heightAccessor().getMaxBuildHeight()
        );

        structurePiecesBuilder.addPiece(entryPoint);

        // Build room component. This also populates the children list, effectively building the entire mineshaft.
        // Note that no blocks are actually placed yet.
        entryPoint.addChildren(entryPoint, structurePiecesBuilder, context.random());
    }
}
