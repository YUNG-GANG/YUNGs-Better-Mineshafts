package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;

import java.util.Locale;

public class BMModStructureFeaturePieces {
    public static void init() {
        BetterMineshaftStructurePieceType.VERTICAL_ENTRANCE      = register("BMSVerticalEntrance", VerticalEntrance::new);
        BetterMineshaftStructurePieceType.BIG_TUNNEL             = register("BMSBigTunnel", BigTunnel::new);
        BetterMineshaftStructurePieceType.SMALL_TUNNEL           = register("BMSSmallTunnel", SmallTunnel::new);
        BetterMineshaftStructurePieceType.SMALL_TUNNEL_TURN      = register("BMSSmallTunnelTurn", SmallTunnelTurn::new);
        BetterMineshaftStructurePieceType.SMALL_TUNNEL_STAIRS    = register("BMSSmallTunnelStairs", SmallTunnelStairs::new);
        BetterMineshaftStructurePieceType.SIDE_ROOM              = register("BMSSideRoom", SideRoom::new);
        BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON      = register("BMSSideRoomDungeon", SideRoomDungeon::new);
        BetterMineshaftStructurePieceType.ORE_DEPOSIT            = register("BMSOreDeposit", OreDeposit::new);
        BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_4 = register("BMSLayeredIntersection4", LayeredIntersection4::new);
        BetterMineshaftStructurePieceType.LAYERED_INTERSECTION_5 = register("BMSLayeredIntersection5", LayeredIntersection5::new);
        BetterMineshaftStructurePieceType.ZOMBIE_VILLAGER_ROOM   = register("BMSZombieVillagerRoom", ZombieVillagerRoom::new);
    }
    
    private static StructurePieceType register(String name, StructurePieceType.ContextlessType structurePieceType) {
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation(BetterMineshafts.MOD_ID, name.toLowerCase(Locale.ROOT)), structurePieceType);
    }
}
