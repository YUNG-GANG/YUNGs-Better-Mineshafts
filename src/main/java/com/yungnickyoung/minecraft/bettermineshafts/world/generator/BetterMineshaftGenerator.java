package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Random;

public class BetterMineshaftGenerator {
    public static MineshaftPart generateAndAddBigTunnelPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        if (pieceChainLen > 3) { // will result in n + 2 max number of segments.
            return null;
        }

        int rand = random.nextInt(100);
        BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;

        BlockBox blockBox;
        if (rand >= 10) {
            blockBox = BigTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new BigTunnel(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }

        return null;
    }

    public static MineshaftPart generateAndAddSmallTunnelPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        BlockBox blockBox;
        int rand = random.nextInt(100);
        BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;

        // End of chain - chance of placing ore deposit
        if (pieceChainLen > 7) {
            if (rand > 50) {
                blockBox = OreDeposit.determineBoxPosition(list, random, x, y, z, direction);
                if (blockBox != null) {
                    MineshaftPart newPiece = new OreDeposit(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                    list.add(newPiece);
                    newPiece.method_14918(structurePiece, list, random); // buildComponent
                    return newPiece;
                }
            }
            return null;
        }

        // Add new piece.
        if (rand >= 90 && pieceChainLen > 2) { // Turns can't be placed early on
            blockBox = LayeredIntersection4.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new LayeredIntersection4(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }
        else if (rand >= 80 && pieceChainLen < 7) { // Stairs can't be placed at the very end
            blockBox = SmallTunnelStairs.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new SmallTunnelStairs(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }
        else if (rand >= 70 && pieceChainLen > 2) { // Turns can't be placed early on
            blockBox = SmallTunnelTurn.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new SmallTunnelTurn(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }
        else {
            blockBox = SmallTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new SmallTunnel(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }

        return null;
    }

    public static MineshaftPart generateAndAddSideRoomPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;

        BlockBox blockBox = SideRoom.determineBoxPosition(list, random, x, y, z, direction);
        if (blockBox != null) {
            MineshaftPart newPiece = new SideRoom(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.method_14918(structurePiece, list, random); // buildComponent
            return newPiece;
        }

        return null;
    }

    public static MineshaftPart generateAndAddSideRoomDungeonPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;

        BlockBox blockBox = SideRoomDungeon.determineBoxPosition(list, random, x, y, z, direction);
        if (blockBox != null) {
            MineshaftPart newPiece = new SideRoomDungeon(l + 1, pieceChainLen + 1, random, blockBox, direction, type);
            list.add(newPiece);
            newPiece.method_14918(structurePiece, list, random); // buildComponent
            return newPiece;
        }

        return null;
    }
}