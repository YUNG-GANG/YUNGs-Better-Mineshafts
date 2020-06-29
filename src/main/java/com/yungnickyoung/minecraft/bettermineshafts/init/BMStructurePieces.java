package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;

public class BMStructurePieces {
    public static void register() {
        registerPiece(VerticalEntrance.class, "BMSVerticalEntrance");
        registerPiece(BigTunnel.class, "BMSBigTunnel");
        registerPiece(SmallTunnel.class, "BMSSmallTunnel");
        registerPiece(SmallTunnelTurn.class, "BMSSmallTunnelTurn");
        registerPiece(SmallTunnelStairs.class, "BMSSmallTunnelStairs");
        registerPiece(SideRoom.class, "BMSSideRoom");
        registerPiece(SideRoomDungeon.class, "BMSSideRoomDungeon");
        registerPiece(OreDeposit.class, "BMSOreDeposit");
        registerPiece(LayeredIntersection4.class, "BMSLayeredIntersection4");
        registerPiece(LayeredIntersection5.class, "BMSLayeredIntersection5");
        registerPiece(ZombieVillagerRoom.class, "BMSZombieVillagerRoom");
    }

    private static void registerPiece(Class<? extends StructureComponent> clazz, String pieceName) {
        MapGenStructureIO.registerStructureComponent(clazz, new ResourceLocation(BetterMineshafts.MOD_ID, pieceName).toString());
    }
}
