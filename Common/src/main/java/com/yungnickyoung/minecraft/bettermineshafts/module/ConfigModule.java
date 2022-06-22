package com.yungnickyoung.minecraft.bettermineshafts.module;

public class ConfigModule {
    public int minY = -55;
    public int maxY = 30;
    public final Ores ores = new Ores();
    public final SpawnRates spawnRates = new SpawnRates();

    public static class Ores {
        public boolean enabled = true;
        public int cobble = 50;
        public int coal = 20;
        public int iron = 9;
        public int redstone = 7;
        public int gold = 7;
        public int lapis = 3;
        public int emerald = 3;
        public int diamond = 1;

        public Ores() {}
    }

    public static class SpawnRates {
        public double lanternSpawnRate = 0.0067;
        public double torchSpawnRate = 0.02;
        public double workstationSpawnRate = 0.025;
        public double workstationDungeonSpawnRate = 0.25;
        public double smallShaftSpawnRate = 0.07;
        public double cobwebSpawnRate = 0.15;
        public double smallShaftChestMinecartSpawnRate = 0.00125;
        public double smallShaftTntMinecartSpawnRate = 0.0025;
        public double mainShaftChestMinecartSpawnRate = 0.01;
        public double mainShaftTntMinecartSpawnRate = 0.0025;
        public int zombieVillagerRoomSpawnRate = 2;
        public int smallShaftPieceChainLength = 9;

        public SpawnRates() {}
    }
}
