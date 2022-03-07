package com.yungnickyoung.minecraft.bettermineshafts.init;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshafts;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class BMModTags {
    public static TagKey<Biome> HAS_BETTER_MINESHAFT;

    public static void init() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMModTags::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            HAS_BETTER_MINESHAFT = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BetterMineshafts.MOD_ID, "has_better_mineshaft"));
        });
    }
}
