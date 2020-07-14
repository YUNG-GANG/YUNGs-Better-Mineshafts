package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.config.Configuration;
import com.yungnickyoung.minecraft.bettermineshafts.integration.rustic.RusticCompat;
import net.minecraftforge.fml.common.Loader;

public class ModCompat {
    public static void init() {
        if (Loader.isModLoaded("rustic")) {
            RusticCompat.init(Configuration.modCompat.rusticLanternsEnabled);
        }
    }
}
