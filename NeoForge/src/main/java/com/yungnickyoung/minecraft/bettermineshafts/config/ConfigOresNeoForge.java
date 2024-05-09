package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigOresNeoForge {
    public final ModConfigSpec.ConfigValue<Boolean> enabled;
    public final ModConfigSpec.ConfigValue<Integer> cobble;
    public final ModConfigSpec.ConfigValue<Integer> coal;
    public final ModConfigSpec.ConfigValue<Integer> iron;
    public final ModConfigSpec.ConfigValue<Integer> redstone;
    public final ModConfigSpec.ConfigValue<Integer> gold;
    public final ModConfigSpec.ConfigValue<Integer> lapis;
    public final ModConfigSpec.ConfigValue<Integer> emerald;
    public final ModConfigSpec.ConfigValue<Integer> diamond;

    public ConfigOresNeoForge(final ModConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Ore deposit settings.
                                ##########################################################################################################""")
                .push("Ore Deposits");

        enabled = BUILDER
                .worldRestart()
                .define("Enable Ore Deposits", true);

        cobble = BUILDER
                .comment(
                        " Chance of an ore deposit being cobblestone only.\n" +
                        " Default: 50")
                .worldRestart()
                .defineInRange("Cobble Spawn Chance (Empty Deposit)", 50, 0, 100);

        coal = BUILDER
                .comment(
                        " Chance of an ore deposit containing coal.\n" +
                        " Default: 20")
                .worldRestart()
                .defineInRange("Coal Spawn Chance", 20, 0, 100);

        iron = BUILDER
                .comment(
                        " Chance of an ore deposit containing iron.\n" +
                        " Default: 9")
                .worldRestart()
                .defineInRange("Iron Spawn Chance", 9, 0, 100);

        redstone = BUILDER
                .comment(
                        " Chance of an ore deposit containing redstone.\n" +
                        " Default: 7")
                .worldRestart()
                .defineInRange("Redstone Spawn Chance", 7, 0, 100);

        gold = BUILDER
                .comment(
                        " Chance of an ore deposit containing gold.\n" +
                        " Default: 7")
                .worldRestart()
                .defineInRange("Gold Spawn Chance", 7, 0, 100);

        lapis = BUILDER
                .comment(
                        " Chance of an ore deposit containing lapis lazuli.\n" +
                        " Default: 3")
                .worldRestart()
                .defineInRange("Lapis Spawn Chance", 3, 0, 100);

        emerald = BUILDER
                .comment(
                        " Chance of an ore deposit containing emerald.\n" +
                        " Default: 3")
                .worldRestart()
                .defineInRange("Emerald Spawn Chance", 3, 0, 100);

        diamond = BUILDER
                .comment(
                        " Chance of an ore deposit containing diamond.\n" +
                        " Default: 1")
                .worldRestart()
                .defineInRange("Diamond Spawn Chance", 1, 0, 100);

        BUILDER.pop();
    }
}