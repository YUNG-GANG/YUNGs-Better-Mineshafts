package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.structure.StructurePiece;

import java.util.List;
import java.util.Random;

public class BetterMineshaftGenerator {
    public static MineshaftPiece generateAndAddBigTunnelPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        if (pieceChainLen > 3) { // will result in n + 2 max number of segments.
            return null;
        }

        int rand = random.nextInt(100);
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        if (rand >= 10 || pieceChainLen < 1) {
            MutableBoundingBox blockBox = BigTunnel.determineBoxPosition(x, y, z, direction);
            MineshaftPiece newPiece = new BigTunnel(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.buildComponent(structurePiece, list, random);
            return newPiece;
        }

        return null;
    }

    public static MineshaftPiece generateAndAddSmallTunnelPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        MutableBoundingBox blockBox;
        int rand = random.nextInt(100);
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        // End of chain - place ore deposit or zombie villager room
        if (pieceChainLen > 7) {
            if (rand < 2) {
                // Need to offset by 1 since room is wider than tunnel
                if (direction == Direction.NORTH) x -= 1;
                else if (direction == Direction.EAST) z -= 1;
                else if (direction == Direction.SOUTH) x += 1;
                else if (direction == Direction.WEST) z += 1;
                blockBox = ZombieVillagerRoom.determineBoxPosition(list, random, x, y, z, direction);
                if (blockBox != null) {
                    MineshaftPiece newPiece = new ZombieVillagerRoom(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                    list.add(newPiece);
                    newPiece.buildComponent(structurePiece, list, random);
                    return newPiece;
                }
            }
            else {
                blockBox = OreDeposit.determineBoxPosition(list, random, x, y, z, direction);
                if (blockBox != null) {
                    MineshaftPiece newPiece = new OreDeposit(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                    list.add(newPiece);
                    newPiece.buildComponent(structurePiece, list, random);
                    return newPiece;
                }
            }
            return null;
        }

        // Add new piece.
        if (rand >= 90 && pieceChainLen > 2 && pieceChainLen < 7) { // Intersection can't be placed early on or at the very end
            blockBox = LayeredIntersection4.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new LayeredIntersection4(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.buildComponent(structurePiece, list, random);
                return newPiece;
            }
        }
        else if (rand >= 80 && pieceChainLen < 7) { // Stairs can't be placed at the very end
            blockBox = SmallTunnelStairs.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new SmallTunnelStairs(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.buildComponent(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }
        else if (rand >= 70 && pieceChainLen > 2) { // Turns can't be placed early on
            blockBox = SmallTunnelTurn.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new SmallTunnelTurn(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.buildComponent(structurePiece, list, random);
                return newPiece;
            }
        }
        else if (rand >= 60 && pieceChainLen > 2 && pieceChainLen < 7) { // Intersection can't be placed early on or at the very end
            blockBox = LayeredIntersection5.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new LayeredIntersection5(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.buildComponent(structurePiece, list, random);
                return newPiece;
            }
        }
        else {
            blockBox = SmallTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPiece newPiece = new SmallTunnel(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.buildComponent(structurePiece, list, random);
                return newPiece;
            }
        }

        return null;
    }

    public static MineshaftPiece generateAndAddSideRoomPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        MutableBoundingBox blockBox = SideRoom.determineBoxPosition(list, random, x, y, z, direction);
        if (blockBox != null) {
            MineshaftPiece newPiece = new SideRoom(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.buildComponent(structurePiece, list, random);
            return newPiece;
        }

        return null;
    }

    public static MineshaftPiece generateAndAddSideRoomDungeonPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        BetterMineshaftStructure.Type type = ((MineshaftPiece) structurePiece).mineshaftType;

        MutableBoundingBox blockBox = SideRoomDungeon.determineBoxPosition(list, random, x, y, z, direction);
        if (blockBox != null) {
            MineshaftPiece newPiece = new SideRoomDungeon(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.buildComponent(structurePiece, list, random); // buildComponent
            return newPiece;
        }

        return null;
    }
}