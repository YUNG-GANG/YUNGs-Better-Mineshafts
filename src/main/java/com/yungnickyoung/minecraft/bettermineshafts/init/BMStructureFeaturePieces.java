package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class BMStructureFeaturePieces {
    public static void init() {
        BetterMineshaftStructurePieceType.VERTICAL_ENTRANCE      = registerPiece("BMSVerticalEntrance", VerticalEntrance::new);
        BetterMineshaftStructurePieceType.BIG_TUNNEL             = registerPiece("BMSBigTunnel", BigTunnel::new);
        BetterMineshaftStructurePieceType.SMALL_TUNNEL           = registerPiece("BMSSmallTunnel", SmallTunnel::new);
        BetterMineshaftStructurePieceType.SMALL_TUNNEL_TURN      = registerPiece("BMSSmallTunnelTurn", SmallTunnelTurn::new);
        BetterMineshaftStructurePieceType.SMALL_TUNNEL_STAIRS    = registerPiece("BMSSmallTunnelStairs", SmallTunnelStairs::new);
        BetterMineshaftStructurePieceType.SIDE_ROOM              = registerPiece("BMSSideRoom", SideRoom::new);
        BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON      = registerPiece("BMSSideRoomDungeon", SideRoomDungeon::new);
        BetterMineshaftStructurePieceType.ORE_DEPOSIT            = registerPiece("BMSOreDeposit", OreDeposit::new);
        BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_4 = registerPiece("BMSLayeredIntersection4", LayeredIntersection4::new);
        BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5 = registerPiece("BMSLayeredIntersection5", LayeredIntersection5::new);
        BetterMineshaftStructurePieceType.ZOMBIE_VILLAGER_ROOM   = registerPiece("BMSZombieVillagerRoom", ZombieVillagerRoom::new);
    }

    private static StructurePieceType registerPiece(String name, StructurePieceType piece) {
        return Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, name.toLowerCase(Locale.ROOT)), piece);
    }
}
