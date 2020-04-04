package com.yungnickyoung.minecraft.bettermineshafts.world.generator;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.Direction;

public class BoxUtil {

    /**
     * Generates and rotates a block box.
     * The main axis is the longer of the x and z axes, in the direction
     * the structure should generate from the starting point.
     */
    public static BlockBox boxFromCoordsWithRotation(int x, int y, int z, int secondaryAxisLen, int yLen, int mainAxisLen, Direction mainAxis) {
        BlockBox blockBox = new BlockBox(x, y, z, x, y + yLen - 1, z);
        switch (mainAxis) {
            case NORTH:
            default:
                blockBox.maxX = x + (secondaryAxisLen - 1);
                blockBox.minZ = z - (mainAxisLen - 1);
                break;
            case SOUTH:
//                blockBox.maxX = x + (secondaryAxisLen - 1);
                blockBox.minX = x - (secondaryAxisLen - 1);
                blockBox.maxZ = z + (mainAxisLen - 1);
                break;
            case WEST:
                blockBox.minX = x - (mainAxisLen - 1);
//                blockBox.maxZ = z + (secondaryAxisLen - 1);
                blockBox.minZ = z - (secondaryAxisLen - 1);
                break;
            case EAST:
                blockBox.maxX = x + (mainAxisLen - 1);
                blockBox.maxZ = z + (secondaryAxisLen - 1);
        }
        return blockBox;
    }
}
