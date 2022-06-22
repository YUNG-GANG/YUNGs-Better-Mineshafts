package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.BetterMineshaftsCommon;
import com.yungnickyoung.minecraft.bettermineshafts.world.config.BetterMineshaftConfiguration;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;

public class BetterMineshaftGenerator {
    public static void generateAndAddBigTunnelPiece(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, RandomSource randomSource, int x, int y, int z, Direction direction, int chainLength) {
        if (chainLength > 3) { // will result in n + 2 max number of segments.
            return;
        }

        int rand = randomSource.nextInt(100);
        BetterMineshaftConfiguration config = ((BetterMineshaftPiece) structurePiece).config;

        if (rand >= 10 || chainLength < 2) {
            BoundingBox boundingBox = BigTunnel.determineBoxPosition(x, y, z, direction);
            BetterMineshaftPiece newPiece = new BigTunnel(chainLength + 1, boundingBox, direction, config);
            structurePieceAccessor.addPiece(newPiece);
            newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
        }
    }

    public static BetterMineshaftPiece generateAndAddSmallTunnelPiece(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, RandomSource randomSource, int x, int y, int z, Direction direction, int chainLength) {
        BoundingBox boundingBox;
        int rand = randomSource.nextInt(100);
        BetterMineshaftConfiguration config = ((BetterMineshaftPiece) structurePiece).config;

        // End of chain - place ore deposit or zombie villager room
        if (chainLength > BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftPieceChainLength - 2) {
            if (rand < BetterMineshaftsCommon.CONFIG.spawnRates.zombieVillagerRoomSpawnRate) {
                // Need to offset by 1 since room is wider than tunnel
                if (direction == Direction.NORTH) x -= 1;
                else if (direction == Direction.EAST) z -= 1;
                else if (direction == Direction.SOUTH) x += 1;
                else if (direction == Direction.WEST) z += 1;
                boundingBox = ZombieVillagerRoom.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
                if (boundingBox != null) {
                    BetterMineshaftPiece newPiece = new ZombieVillagerRoom(chainLength + 1, boundingBox, direction, config);
                    structurePieceAccessor.addPiece(newPiece);
                    newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
                    return newPiece;
                }
            } else {
                if (!BetterMineshaftsCommon.CONFIG.ores.enabled) return null;
                boundingBox = OreDeposit.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
                if (boundingBox != null) {
                    BetterMineshaftPiece newPiece = new OreDeposit(chainLength + 1, boundingBox, direction, config);
                    structurePieceAccessor.addPiece(newPiece);
                    newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
                    return newPiece;
                }
            }
            return null;
        }

        // Add new piece.
        if (rand >= 90 && chainLength > 2 && chainLength < BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftPieceChainLength - 2) { // Intersection can't be placed early on or at the very end
            boundingBox = LayeredIntersection4.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
            if (boundingBox != null) {
                BetterMineshaftPiece newPiece = new LayeredIntersection4(chainLength + 1, boundingBox, direction, config);
                structurePieceAccessor.addPiece(newPiece);
                newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
                return newPiece;
            }
        } else if (rand >= 80 && chainLength < BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftPieceChainLength - 2) { // Stairs can't be placed at the very end
            boundingBox = SmallTunnelStairs.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
            if (boundingBox != null) {
                BetterMineshaftPiece newPiece = new SmallTunnelStairs(chainLength + 1, boundingBox, direction, config);
                structurePieceAccessor.addPiece(newPiece);
                newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
                return newPiece;
            }
        } else if (rand >= 70 && chainLength > 2) { // Turns can't be placed early on
            boundingBox = SmallTunnelTurn.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
            if (boundingBox != null) {
                BetterMineshaftPiece newPiece = new SmallTunnelTurn(chainLength + 1, randomSource, boundingBox, direction, config);
                structurePieceAccessor.addPiece(newPiece);
                newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
                return newPiece;
            }
        } else if (rand >= 60 && chainLength > 2 && chainLength < BetterMineshaftsCommon.CONFIG.spawnRates.smallShaftPieceChainLength - 2) { // Intersection can't be placed early on or at the very end
            boundingBox = LayeredIntersection5.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
            if (boundingBox != null) {
                BetterMineshaftPiece newPiece = new LayeredIntersection5(chainLength + 1, boundingBox, direction, config);
                structurePieceAccessor.addPiece(newPiece);
                newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
                return newPiece;
            }
        } else {
            boundingBox = SmallTunnel.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
            if (boundingBox != null) {
                BetterMineshaftPiece newPiece = new SmallTunnel(chainLength + 1, boundingBox, direction, config);
                structurePieceAccessor.addPiece(newPiece);
                newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
                return newPiece;
            }
        }

        return null;
    }

    public static BetterMineshaftPiece generateAndAddSideRoomPiece(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, RandomSource randomSource, int x, int y, int z, Direction direction, int chainLength) {
        BetterMineshaftConfiguration config = ((BetterMineshaftPiece) structurePiece).config;

        BoundingBox boundingBox = SideRoom.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
        if (boundingBox != null) {
            BetterMineshaftPiece newPiece = new SideRoom(chainLength + 1, boundingBox, direction, config);
            structurePieceAccessor.addPiece(newPiece);
            newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
            return newPiece;
        }

        return null;
    }

    public static BetterMineshaftPiece generateAndAddSideRoomDungeonPiece(StructurePiece structurePiece, StructurePieceAccessor structurePieceAccessor, RandomSource randomSource, int x, int y, int z, Direction direction, int chainLength) {
        BetterMineshaftConfiguration config = ((BetterMineshaftPiece) structurePiece).config;

        BoundingBox boundingBox = SideRoomDungeon.determineBoxPosition(structurePieceAccessor, x, y, z, direction);
        if (boundingBox != null) {
            BetterMineshaftPiece newPiece = new SideRoomDungeon(chainLength + 1, boundingBox, direction, config);
            structurePieceAccessor.addPiece(newPiece);
            newPiece.addChildren(structurePiece, structurePieceAccessor, randomSource);
            return newPiece;
        }

        return null;
    }
}