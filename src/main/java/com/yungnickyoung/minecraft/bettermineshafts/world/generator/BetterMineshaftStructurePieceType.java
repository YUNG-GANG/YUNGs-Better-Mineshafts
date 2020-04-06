package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.BigTunnel;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.OreDeposit;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.SideRoom;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.SideRoomDungeon;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public interface BetterMineshaftStructurePieceType extends StructurePieceType {
    StructurePieceType BIG_TUNNEL = register(BigTunnel::new, "BMSBigTunnel");
    StructurePieceType SMALL_TUNNEL = register(SideRoom::new, "BMSSmallTunnel");
    StructurePieceType SMALL_TUNNEL_TURN = register(SideRoom::new, "BMSSmallTunnelTurn");
    StructurePieceType SIDE_ROOM = register(SideRoom::new, "BMSSideRoom");
    StructurePieceType SIDE_ROOM_DUNGEON = register(SideRoomDungeon::new, "BMSSideRoomDungeon");
    StructurePieceType ORE_DEPOSIT = register(OreDeposit::new, "BMSOreDeposit");

    static BetterMineshaftStructurePieceType register(BetterMineshaftStructurePieceType pieceType, String id) {
        return Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, id.toLowerCase(Locale.ROOT)), pieceType);
    }
}
