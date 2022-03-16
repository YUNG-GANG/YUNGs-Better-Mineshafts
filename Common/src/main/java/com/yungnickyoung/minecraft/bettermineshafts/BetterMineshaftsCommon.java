package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.module.ConfigModule;
import com.yungnickyoung.minecraft.bettermineshafts.services.Services;
import io.netty.util.internal.ConcurrentSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

public class BetterMineshaftsCommon {
    public static final String MOD_ID = "bettermineshafts";
    public static final String MOD_NAME = "YUNG's Better Mineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final ConfigModule CONFIG = new ConfigModule();

    /** Debug info **/
    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);
    public static final boolean DEBUG_LOG = false;

    public static void init() {
        Services.MODULES.loadModules();
    }
}