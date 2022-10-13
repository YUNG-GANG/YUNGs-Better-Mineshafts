package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import com.yungnickyoung.minecraft.yungsapi.api.autoregister.AutoRegister;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

@AutoRegister(BetterMineshaftsCommon.MOD_ID)
public class StructurePieceTypeModule {
    @AutoRegister("bmsverticalentrance")
    public static StructurePieceType VERTICAL_ENTRANCE = (StructurePieceType.ContextlessType) VerticalEntrance::new;

    @AutoRegister("bmsbigtunnel")
    public static StructurePieceType BIG_TUNNEL = (StructurePieceType.ContextlessType) BigTunnel::new;

    @AutoRegister("bmssmalltunnel")
    public static StructurePieceType SMALL_TUNNEL = (StructurePieceType.ContextlessType) SmallTunnel::new;

    @AutoRegister("bmssmalltunnelturn")
    public static StructurePieceType SMALL_TUNNEL_TURN = (StructurePieceType.ContextlessType) SmallTunnelTurn::new;

    @AutoRegister("bmssmalltunnelstairs")
    public static StructurePieceType SMALL_TUNNEL_STAIRS = (StructurePieceType.ContextlessType) SmallTunnelStairs::new;

    @AutoRegister("bmssideroom")
    public static StructurePieceType SIDE_ROOM = (StructurePieceType.ContextlessType) SideRoom::new;

    @AutoRegister("bmssideroomdungeon")
    public static StructurePieceType SIDE_ROOM_DUNGEON = (StructurePieceType.ContextlessType) SideRoomDungeon::new;

    @AutoRegister("bmsoredeposit")
    public static StructurePieceType ORE_DEPOSIT = (StructurePieceType.ContextlessType) OreDeposit::new;

    @AutoRegister("bmslayeredintersection4")
    public static StructurePieceType LAYERED_INTERSECTION_4 = (StructurePieceType.ContextlessType) LayeredIntersection4::new;

    @AutoRegister("bmslayeredintersection5")
    public static StructurePieceType LAYERED_INTERSECTION_5 = (StructurePieceType.ContextlessType) LayeredIntersection5::new;

    @AutoRegister("bmszombievillagerroom")
    public static StructurePieceType ZOMBIE_VILLAGER_ROOM = (StructurePieceType.ContextlessType) ZombieVillagerRoom::new;
}
