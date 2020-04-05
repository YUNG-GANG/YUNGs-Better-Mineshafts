package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftFeature;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces.*;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.Random;

public class BetterMineshaftGenerator {
    public static MineshaftPart generateAndAddBigTunnelPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        if (pieceChainLen > 3) {
            return null;
        }

        int rand = random.nextInt(100);
        BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;

        BlockBox blockBox;
        if (rand >= 10) {
            blockBox = BigTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new BigTunnel(l, pieceChainLen, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }

        return null;
    }

    public static MineshaftPart generateAndAddSmallShaftPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        if (pieceChainLen > 8) {
            return null;
        }

        int rand = random.nextInt(100);
        BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;

        BlockBox blockBox;
        // TODO - check rand to spawn different pieces
        if (rand >= 0) {
            blockBox = SmallTunnel.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new SmallTunnel(l, pieceChainLen, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }

        return null;
    }

    public static MineshaftPart generateAndAddSideRoomPiece(StructurePiece structurePiece, List<StructurePiece> list, Random random, int x, int y, int z, Direction direction, int l, int pieceChainLen) {
        int rand = random.nextInt(100);
        BetterMineshaftFeature.Type type = ((MineshaftPart) structurePiece).mineshaftType;

        BlockBox blockBox;
        // TODO - check rand to spawn downstairs area
        if (rand >= 0) {
            blockBox = SideRoom.determineBoxPosition(list, random, x, y, z, direction);
            if (blockBox != null) {
                MineshaftPart newPiece = new SideRoom(l, pieceChainLen, random, blockBox, direction, type);
                list.add(newPiece);
                newPiece.method_14918(structurePiece, list, random); // buildComponent
                return newPiece;
            }
        }

        return null;
    }
}