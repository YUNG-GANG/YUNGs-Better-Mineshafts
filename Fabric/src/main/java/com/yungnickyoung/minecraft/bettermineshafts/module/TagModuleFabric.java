package com.yungnickyoung.minecraft.bettermineshafts.module;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagModuleFabric {
    public static void init() {
        TagModule.HAS_BETTER_MINESHAFT = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterMineshaftsCommon.MOD_ID, "has_better_mineshaft"));
    }
}
