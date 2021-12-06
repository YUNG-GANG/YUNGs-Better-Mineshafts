package com.yungnickyoung.minecraft.bettermineshafts.world;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.MineshaftPiece;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.VerticalEntrance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.QuartPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BetterMineshaftStructureFeature extends StructureFeature<BetterMineshaftFeatureConfig> {
    public BetterMineshaftStructureFeature(Codec<BetterMineshaftFeatureConfig> codec) {
        super(codec, PieceGeneratorSupplier.simple(BetterMineshaftStructureFeature::checkLocation, BetterMineshaftStructureFeature::generatePieces));
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<BetterMineshaftFeatureConfig> context) {
        WorldgenRandom worldgenRandom = new WorldgenRandom(new LegacyRandomSource(0L));
        worldgenRandom.setLargeFeatureSeed(context.seed(), context.chunkPos().x, context.chunkPos().z);
        return worldgenRandom.nextDouble() < context.config().probability &&
                context.validBiome().test(context.chunkGenerator().getNoiseBiome(
                        QuartPos.fromBlock(context.chunkPos().getMiddleBlockX()),
                        QuartPos.fromBlock(50),
                        QuartPos.fromBlock(context.chunkPos().getMiddleBlockZ()))
                );
    }

    private static void generatePieces(StructurePiecesBuilder structurePiecesBuilder, PieceGenerator.Context<BetterMineshaftFeatureConfig> context) {
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
        int y = context.random().nextInt(BetterMineshafts.CONFIG.maxY - BetterMineshafts.CONFIG.minY + 1) + BetterMineshafts.CONFIG.minY;
        BlockPos.MutableBlockPos startingPos = new BlockPos.MutableBlockPos(context.chunkPos().getBlockX(2), y, context.chunkPos().getBlockZ(2));

        // Entrypoint
        MineshaftPiece entryPoint = new VerticalEntrance(
                -1,
                context.random(),
                startingPos,
                direction,
                context.config().type
        );

        structurePiecesBuilder.addPiece(entryPoint);

        // Build room component. This also populates the children list, effectively building the entire mineshaft.
        // Note that no blocks are actually placed yet.
        entryPoint.addChildren(entryPoint, structurePiecesBuilder, context.random());
    }

    public enum Type implements StringRepresentable {
        NORMAL("normal"),
        MESA("mesa"),
        JUNGLE("jungle"),
        SNOW("snow"),
        ICE("ice"),
        DESERT("desert"),
        RED_DESERT("red_desert"),
        SAVANNA("savanna"),
        MUSHROOM("mushroom"),
        LUSH("lush");

        public static final Codec<Type> CODEC = StringRepresentable.fromEnum(Type::values, Type::byName);
        private static final Map<String, Type> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Type::getName, type -> type));

        private final String name;

        Type(String name) {
            this.name = name;
        }

        private static Type byName(String name) {
            return BY_NAME.get(name);
        }

        public static Type byId(int index) {
            return index >= 0 && index < values().length ? values()[index] : NORMAL;
        }

        public String getName() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
