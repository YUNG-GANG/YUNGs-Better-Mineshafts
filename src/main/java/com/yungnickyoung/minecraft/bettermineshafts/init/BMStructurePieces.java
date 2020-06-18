package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class BMStructurePieces {
    public static void init() {
        MapGenStructureIO.registerStructureComponent(VerticalEntrance.class, "BMSVerticalEntrance");
        MapGenStructureIO.registerStructureComponent(BigTunnel.class, "BMSBigTunnel");
        MapGenStructureIO.registerStructureComponent(SmallTunnel.class, "BMSSmallTunnel");
        MapGenStructureIO.registerStructureComponent(SmallTunnelTurn.class, "BMSSmallTunnelTurn");
        MapGenStructureIO.registerStructureComponent(SmallTunnelStairs.class, "BMSSmallTunnelStairs");
        MapGenStructureIO.registerStructureComponent(SideRoom.class, "BMSSideRoom");
        MapGenStructureIO.registerStructureComponent(SideRoomDungeon.class, "BMSSideRoomDungeon");
        MapGenStructureIO.registerStructureComponent(OreDeposit.class, "BMSOreDeposit");
        MapGenStructureIO.registerStructureComponent(LayeredIntersection4.class, "BMSLayeredIntersection4");
        MapGenStructureIO.registerStructureComponent(LayeredIntersection5.class, "BMSLayeredIntersection5");
        MapGenStructureIO.registerStructureComponent(ZombieVillagerRoom.class, "BMSZombieVillagerRoom");
    }
}
