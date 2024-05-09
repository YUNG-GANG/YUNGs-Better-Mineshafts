package com.yungnickyoung.minecraft.bettermineshafts.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigSpawnRatesNeoForge {
    public final ModConfigSpec.ConfigValue<Double> lanternSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> torchSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> workstationSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> workstationDungeonSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> smallShaftSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> cobwebSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> smallShaftChestMinecartSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> smallShaftTntMinecartSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> mainShaftChestMinecartSpawnRate;
    public final ModConfigSpec.ConfigValue<Double> mainShaftTntMinecartSpawnRate;
    public final ModConfigSpec.ConfigValue<Integer> zombieVillagerRoomSpawnRate;
    public final ModConfigSpec.ConfigValue<Integer> smallShaftPieceChainLength;

    public ConfigSpawnRatesNeoForge(final ModConfigSpec.Builder BUILDER) {
        BUILDER
                .comment(
                        """
                                ##########################################################################################################
                                # Spawn rates for various mineshaft parts and decorations.
                                ##########################################################################################################""")
                .push("Spawn Rates & More");

        lanternSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for lanterns in the main shaft.\n" +
                        " Default: .0067")
                .defineInRange("Lantern Spawn Rate", 0.0067, 0 ,1);

        torchSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for torches in small shafts.\n" +
                        " Default: .02")
                .defineInRange("Torch Spawn Rate", 0.02, 0 ,1);

        workstationSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for workstation side rooms along the main shaft.\n" +
                        " Default: .025")
                .defineInRange("Workstation Spawn Rate", 0.025, 0 ,1);

        workstationDungeonSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for workstation cellars below workstations along the main shaft.\n" +
                        " Default: .25")
                .defineInRange("Workstation Cellar Spawn Rate", 0.25, 0 ,1);

        smallShaftSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for smaller tunnels that generate along the main shaft.\n" +
                        " Default: .07")
                .defineInRange("Small Shaft Spawn Rate", 0.07, 0 ,1);

        cobwebSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for cobwebs.\n" +
                        " Default: .15")
                .defineInRange("Cobweb Spawn Rate", 0.15, 0 ,1);

        smallShaftChestMinecartSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for minecarts holding chests in small shafts.\n" +
                        " Default: .00125")
                .defineInRange("Small Shaft Chest Minecart Spawn Rate", 0.00125, 0 ,1);

        smallShaftTntMinecartSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for minecarts holding TNT in small shafts.\n" +
                        " Default: .0025")
                .defineInRange("Small Shaft TNT Minecart Spawn Rate", 0.0025, 0 ,1);

        mainShaftChestMinecartSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for minecarts holding chests in the main shaft.\n" +
                                " Default: .01")
                .defineInRange("Main Shaft Chest Minecart Spawn Rate", 0.01, 0 ,1);

        mainShaftTntMinecartSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " The spawn rate for minecarts holding TNT in the main shaft.\n" +
                        " Default: .0025")
                .defineInRange("Main Shaft TNT Minecart Spawn Rate", 0.0025, 0 ,1);

        zombieVillagerRoomSpawnRate = BUILDER
                .worldRestart()
                .comment(
                        " Percent chance of an Abandoned Miners' Outpost to spawn at the end of a small mineshaft tunnel.\n" +
                        " Default: 2")
                .defineInRange("Abandoned Miners' Outpost Spawn Chance", 2, 0 ,100);

        smallShaftPieceChainLength = BUILDER
                .worldRestart()
                .comment(
                        """
                                The number of "pieces" (e.g. straight, turn, ladder, intersection, etc.) in a single small shaft.
                                This determines the overall length of small shafts.
                                Default: 9""".indent(1))
                .defineInRange("Small Shaft Piece Chain Length", 9, 0 ,1000);

        BUILDER.pop();
    }
}