package com.yungnickyoung.minecraft.bettermineshafts.world.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.bettermineshafts.module.StructureProcessorTypeModule;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SuspiciousBlockProcessor extends StructureProcessor {
    public static final MapCodec<SuspiciousBlockProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    BlockState.CODEC.fieldOf("target_block").forGetter(config -> config.targetBlock),
                    BlockState.CODEC.fieldOf("output_block").forGetter(config -> config.outputBlock),
                    ResourceLocation.CODEC.fieldOf("loot_table").forGetter(config -> config.lootTable),
                    Codec.doubleRange(0.0, 1.0).fieldOf("probability").forGetter(config -> config.probability))
            .apply(instance, instance.stable(SuspiciousBlockProcessor::new)));

    private final BlockState targetBlock;
    private final BlockState outputBlock;
    private final ResourceLocation lootTable;
    private final double probability;

    private SuspiciousBlockProcessor(BlockState targetBlock, BlockState outputBlock, ResourceLocation lootTable, double probability) {
        this.targetBlock = targetBlock;
        this.outputBlock = outputBlock;
        this.lootTable = lootTable;
        this.probability = probability;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state() == this.targetBlock) {
            RandomSource randomSource = structurePlacementData.getRandom(blockInfoGlobal.pos());
            float f = randomSource.nextFloat();

            if (f < this.probability) {
                // Make block suspicious
                CompoundTag nbt = new CompoundTag();
                nbt.putString("LootTable", this.lootTable.toString());
                blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), outputBlock, nbt);
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return StructureProcessorTypeModule.SUSPICIOUS_BLOCK_PROCESSOR;
    }
}