package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.event.EventMineshaftGen;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMStructurePieces;
import com.yungnickyoung.minecraft.bettermineshafts.proxy.IProxy;
import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@Mod(modid = "bettermineshafts", useMetadata = true, acceptableRemoteVersions = "*")
public class BetterMineshafts {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @SidedProxy(clientSide = "com.yungnickyoung.minecraft.bettermineshafts.proxy.ClientProxy", serverSide = "com.yungnickyoung.minecraft.bettermineshafts.proxy.ServerProxy")
    public static IProxy proxy;

    // Debug variables used in development
    public static final boolean DEBUG_LOG = true;
    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.TERRAIN_GEN_BUS.register(new EventMineshaftGen());
        MapGenStructureIO.registerStructure(MapGenBetterMineshaft.Start.class, "Mineshaft");
        BMStructurePieces.init();
    }
}
