package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.ModStructures;
import com.yungnickyoung.minecraft.bettermineshafts.init.ModConfig;
import io.netty.util.internal.ConcurrentSet;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@Mod("bettermineshafts")
public class BetterMineshafts {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // Debug variables used in development
    public static final boolean DEBUG_LOG = true;
    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);

    public BetterMineshafts() {
        init();
    }

    private void init() {
        ModStructures.init();
        ModConfig.init();
    }
}
