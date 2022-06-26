package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.services.Services;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

public class StructurePieceTypeModule {
    public static StructurePieceType VERTICAL_ENTRANCE = (StructurePieceType.ContextlessType) VerticalEntrance::new;
    public static StructurePieceType BIG_TUNNEL = (StructurePieceType.ContextlessType) BigTunnel::new;
    public static StructurePieceType SMALL_TUNNEL = (StructurePieceType.ContextlessType) SmallTunnel::new;
    public static StructurePieceType SMALL_TUNNEL_TURN = (StructurePieceType.ContextlessType) SmallTunnelTurn::new;
    public static StructurePieceType SMALL_TUNNEL_STAIRS = (StructurePieceType.ContextlessType) SmallTunnelStairs::new;
    public static StructurePieceType SIDE_ROOM = (StructurePieceType.ContextlessType) SideRoom::new;
    public static StructurePieceType SIDE_ROOM_DUNGEON = (StructurePieceType.ContextlessType) SideRoomDungeon::new;
    public static StructurePieceType ORE_DEPOSIT = (StructurePieceType.ContextlessType) OreDeposit::new;
    public static StructurePieceType LAYERED_INTERSECTION_4 = (StructurePieceType.ContextlessType) LayeredIntersection4::new;
    public static StructurePieceType LAYERED_INTERSECTION_5 = (StructurePieceType.ContextlessType) LayeredIntersection5::new;
    public static StructurePieceType ZOMBIE_VILLAGER_ROOM = (StructurePieceType.ContextlessType) ZombieVillagerRoom::new;

    public static void init() {
        register("BMSVerticalEntrance", VERTICAL_ENTRANCE);
        register("BMSBigTunnel", BIG_TUNNEL);
        register("BMSSmallTunnel", SMALL_TUNNEL);
        register("BMSSmallTunnelTurn", SMALL_TUNNEL_TURN);
        register("BMSSmallTunnelStairs", SMALL_TUNNEL_STAIRS);
        register("BMSSideRoom", SIDE_ROOM);
        register("BMSSideRoomDungeon", SIDE_ROOM_DUNGEON);
        register("BMSOreDeposit", ORE_DEPOSIT);
        register("BMSLayeredIntersection4", LAYERED_INTERSECTION_4);
        register("BMSLayeredIntersection5", LAYERED_INTERSECTION_5);
        register("BMSZombieVillagerRoom", ZOMBIE_VILLAGER_ROOM);
    }

    private static void register(String name, StructurePieceType structurePieceType) {
        Services.REGISTRY.registerStructurePieceType(new ResourceLocation(BetterMineshaftsCommon.MOD_ID, name.toLowerCase(Locale.ROOT)), structurePieceType);
    }
}
