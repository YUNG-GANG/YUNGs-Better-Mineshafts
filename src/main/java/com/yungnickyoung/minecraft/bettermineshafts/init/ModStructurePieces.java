package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.config.BMSettings;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureComponent;

public class ModStructurePieces {
    public static void init() {
        registerPieces();
    }

    private static void registerPieces() {
        register(VerticalEntrance.class, "BMSVerticalEntrance");
        register(BigTunnel.class, "BMSBigTunnel");
        register(SmallTunnel.class, "BMSSmallTunnel");
        register(SmallTunnelTurn.class, "BMSSmallTunnelTurn");
        register(SmallTunnelStairs.class, "BMSSmallTunnelStairs");
        register(SideRoom.class, "BMSSideRoom");
        register(SideRoomDungeon.class, "BMSSideRoomDungeon");
        register(OreDeposit.class, "BMSOreDeposit");
        register(LayeredIntersection4.class, "BMSLayeredIntersection4");
        register(LayeredIntersection5.class, "BMSLayeredIntersection5");
        register(ZombieVillagerRoom.class, "BMSZombieVillagerRoom");
    }

    private static void register(Class<? extends StructureComponent> clazz, String pieceName) {
        MapGenStructureIO.registerStructureComponent(clazz, new ResourceLocation(BMSettings.MOD_ID, pieceName).toString());
    }
}
