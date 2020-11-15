package com.yungnickyoung.minecraft.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.ModStructures;
import com.yungnickyoung.minecraft.bettermineshafts.init.ModConfig;
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
 * this mod, and so I used vanilla mineshaft code as a starting point. Turns out that was a bad idea.
 *
 * I recommend looking into Jigsaw or other structure APIs that can abstract away logic dealing with rotation
 * and orientation. It will save you a LOT of headache and frustration down the line.
 * Or just don't rotate your structure. That will make it way easier to code manually.
 *
 * Consider using structure blocks to build and save your structure, rather than coding it from scratch, if you don't
 * need extremely fine-grained control over the randomization of the structure.
 *
 * For a good tutorial on structures in 1.16, see TelepathicGrunt's lovely tutorial mod:
 * https://github.com/TelepathicGrunt/StructureTutorialMod
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
        ModStructures.init();
        ModConfig.init();
    }
}
