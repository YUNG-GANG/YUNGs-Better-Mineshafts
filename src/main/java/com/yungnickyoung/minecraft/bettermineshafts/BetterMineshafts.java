package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.BMFeature;
import com.yungnickyoung.minecraft.bettermineshafts.init.BMModConfig;
import io.netty.util.internal.ConcurrentSet;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Entrypoint for YUNG's Better Mineshafts.
 *
 * Hey! If you're reading this, you may be looking for a reference for implementing complex structures.
 * Be warned that the approach this codebase takes is not recommended for very large and complex
 * randomly generated structures. In my code, I handle rotation and orientation manually, making the construction
 * of each piece very tedious. I only did this because I had no prior experience creating structures when I started
 * this mod, and so I used vanilla mineshaft code as a starting point. Turns out that code is awful.
 *
 * I recommend looking into Jigsaw or other structure API's that can abstract away logic dealing with rotation
 * and orientation. It will save you a LOT of headache and frustration down the line.
 */
@Mod("bettermineshafts")
public class BetterMineshafts {
    public static final String MOD_ID = "bettermineshafts";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    // Debug variables used in development
    public static final boolean DEBUG_LOG = false;
    public static ConcurrentSet<Integer> surfaceEntrances = new ConcurrentSet<>();
    public static AtomicInteger count = new AtomicInteger(0);

    public BetterMineshafts() {
        init();
    }

    private void init() {
        BMFeature.init();
        BMModConfig.init();
    }
}
