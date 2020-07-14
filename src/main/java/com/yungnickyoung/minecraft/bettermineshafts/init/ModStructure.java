package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import com.yungnickyoung.minecraft.bettermineshafts.event.EventMineshaftGen;
import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;

public class ModStructure {
    public static void init() {
        MinecraftForge.TERRAIN_GEN_BUS.register(new EventMineshaftGen());
        MapGenStructureIO.registerStructure(MapGenBetterMineshaft.Start.class, new ResourceLocation(BetterMineshafts.MOD_ID,"BetterMineshaft").toString());
    }
}
