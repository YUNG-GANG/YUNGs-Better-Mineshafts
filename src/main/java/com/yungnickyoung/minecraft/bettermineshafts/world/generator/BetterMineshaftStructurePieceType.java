package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class BetterMineshaftStructurePieceType {
    public static StructurePieceType BIG_TUNNEL;
    public static StructurePieceType SMALL_TUNNEL;
    public static StructurePieceType SMALL_TUNNEL_TURN;
    public static StructurePieceType SIDE_ROOM;
    public static StructurePieceType SIDE_ROOM_DUNGEON;
    public static StructurePieceType ORE_DEPOSIT;

    public static void init() {
        BIG_TUNNEL = Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, "BMSBigTunnel".toLowerCase(Locale.ROOT)), BigTunnel::new);
        SMALL_TUNNEL = Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, "BMSSmallTunnel".toLowerCase(Locale.ROOT)), SmallTunnel::new);
        SMALL_TUNNEL_TURN = Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, "BMSSmallTunnelTurn".toLowerCase(Locale.ROOT)), SmallTunnelTurn::new);
        SIDE_ROOM = Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, "BMSSideRoom".toLowerCase(Locale.ROOT)), SideRoom::new);
        SIDE_ROOM_DUNGEON = Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, "BMSSideRoomDungeon".toLowerCase(Locale.ROOT)), SideRoomDungeon::new);
        ORE_DEPOSIT = Registry.register(Registry.STRUCTURE_PIECE, new Identifier(BetterMineshafts.MOD_ID, "BMSOreDeposit".toLowerCase(Locale.ROOT)), OreDeposit::new);
    }
}
