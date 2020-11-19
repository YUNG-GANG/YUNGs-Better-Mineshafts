package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Random;

public class BetterMineshaftGenerator {
    public static void generateAndAddBigTunnelPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int chainLength) {
        if (chainLength > 3) { // will result in n + 2 max number of segments.
            return;
        }

        int rand = random.nextInt(100);
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        if (rand >= 10 || chainLength < 1) {
            BlockBox blockBox = BigTunnel.determineBoxPosition(x, y, z, direction);
            MineshaftPiece newPiece = new BigTunnel(chainLength + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.fillOpenings(structurePiece, list, random);
        }
    }

    public static MineshaftPiece generateAndAddSmallTunnelPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int chainLength) {
        BlockBox blockBox;
        int rand = random.nextInt(100);
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        // End of chain - place ore deposit or zombie villager room
        if (chainLength > 9 - 2) { // TODO - config option
            if (rand < 2) { // TODO - config option (zombie room)
                // Need to offset by 1 since room is wider than tunnel
                if (direction == Direction.NORTH) x -= 1;
                else if (direction == Direction.EAST) z -= 1;
                else if (direction == Direction.SOUTH) x += 1;
                else if (direction == Direction.WEST) z += 1;
                blockBox = ZombieVillagerRoom.determineBoxPosition(list, random, x, y, z, direction);
                if (blockBox != null) {
                    MineshaftPiece newPiece = new ZombieVillagerRoom(chainLength + 1, random, blockBox, direction, type);
                    list.add(newPiece);
                    newPiece.fillOpenings(structurePiece, list, random); // buildComponent
                    return newPiece;
                }
            } else {
                // TODO - insert oreEnabled config option check here
                blockBox = OreDeposit.determineBoxPosition(list, random, x, y, z, direction);
                if (blockBox != null) {
                    MineshaftPiece newPiece = new OreDeposit(chainLength + 1, random, blockBox, direction, type);
                    list.add(newPiece);
                    newPiece.fillOpenings(structurePiece, list, random); // buildComponent
                    return newPiece;
                }
            }
            return null;
        }

        // Add new piece.
        if (rand >= 90 && chainLength > 2 && chainLength < 9 - 2) { // TODO: config option // Intersection can't be placed early on or at the very end
            blockBox = LayeredIntersection4.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new LayeredIntersection4(chainLength + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.fillOpenings(structurePiece, list, random);
                return newPiece;
            }
        } else if (rand >= 80 && chainLength < 9 - 2) { // TODO: config option // Stairs can't be placed at the very end
            blockBox = SmallTunnelStairs.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new SmallTunnelStairs(chainLength + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.fillOpenings(structurePiece, list, random);
                return newPiece;
            }
        } else if (rand >= 70 && chainLength > 2) { // Turns can't be placed early on
            blockBox = SmallTunnelTurn.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new SmallTunnelTurn(chainLength + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.fillOpenings(structurePiece, list, random);
                return newPiece;
            }
        } else if (rand >= 60 && chainLength > 2 && chainLength < 9 - 2) { // TODO: config option // Intersection can't be placed early on or at the very end
            blockBox = LayeredIntersection5.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new LayeredIntersection5(chainLength + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.fillOpenings(structurePiece, list, random);
                return newPiece;
            }
        } else {
            blockBox = SmallTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new SmallTunnel(chainLength + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.fillOpenings(structurePiece, list, random);
                return newPiece;
            }
        }

        return null;
    }

    public static MineshaftPiece generateAndAddSideRoomPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int chainLength) {
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        BlockBox blockBox = SideRoom.determineBoxPosition(list, random, x, y, z, direction);
        if (blockBox != null) {
            MineshaftPiece newPiece = new SideRoom(chainLength + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.fillOpenings(structurePiece, list, random);
            return newPiece;
        }

        return null;
    }

    public static MineshaftPiece generateAndAddSideRoomDungeonPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int chainLength) {
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        BlockBox blockBox = SideRoomDungeon.determineBoxPosition(list, random, x, y, z, direction);
        if (blockBox != null) {
            MineshaftPiece newPiece = new SideRoomDungeon(chainLength + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.fillOpenings(structurePiece, list, random);
            return newPiece;
        }

        return null;
    }
}