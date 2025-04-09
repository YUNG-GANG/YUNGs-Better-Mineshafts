package com.yungnickyoung.minecraft.bettermineshafts.world.processor;

import com.mojang.serialization.MapCodec;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureProcessorTypeModule;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class StructureVoidProcessor extends StructureProcessor {
    public static final StructureVoidProcessor INSTANCE = new StructureVoidProcessor();
    public static final MapCodec<StructureVoidProcessor> CODEC = MapCodec.unit(() -> INSTANCE);

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().is(Blocks.BROWN_WOOL)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos()))) {
                BlockState blockState = levelReader.getBlockState(blockInfoGlobal.pos());
                if (blockState.is(Blocks.WATER) || blockState.is(Blocks.LAVA)) {
                    blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), Blocks.OAK_PLANKS.defaultBlockState(), null);
                } else {
                    return null;
                }
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.STRUCTURE_VOID_PROCESSOR;
    }
}