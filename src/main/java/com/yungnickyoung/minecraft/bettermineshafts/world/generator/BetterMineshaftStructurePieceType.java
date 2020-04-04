package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.CustomCrossing;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.BigTunnel;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.SideRoom;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public interface BetterMineshaftStructurePieceType extends StructurePieceType {
    StructurePieceType CORRIDOR = register(BetterMineshaftGenerator.MineshaftCorridor::new, "BMSCorridor");
    StructurePieceType CROSSING = register(BetterMineshaftGenerator.MineshaftCrossing::new, "BMSCrossing");
    StructurePieceType ROOM = register(BetterMineshaftGenerator.MineshaftRoom::new, "BMSRoom");
    StructurePieceType STAIRS = register(BetterMineshaftGenerator.MineshaftStairs::new, "BMSStairs");

    StructurePieceType BIG_TUNNEL = register(BigTunnel::new, "BMSBigTunnel");
    StructurePieceType SMALL_TUNNEL = register(SideRoom::new, "BMSSmallTunnel");
    StructurePieceType SIDE_ROOM = register(SideRoom::new, "BMSSideRoom");

    StructurePieceType CUSTOM_CROSSING = register(CustomCrossing::new, "BMSCustomCrossing");

    static BetterMineshaftStructurePieceType register(BetterMineshaftStructurePieceType pieceType, String id) {
        return Registry.register(Registry.STRUCTURE_PIECE, id.toLowerCase(Locale.ROOT), pieceType);
    }
}
