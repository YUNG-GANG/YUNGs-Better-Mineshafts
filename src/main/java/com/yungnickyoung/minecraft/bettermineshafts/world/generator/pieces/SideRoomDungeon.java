package com.yungnickyoung.minecraft.bettermineshafts.world.generator.pieces;

import com.yungnickyoung.minecraft.bettermineshafts.world.BetterMineshaftStructure;
import com.yungnickyoung.minecraft.bettermineshafts.world.generator.BetterMineshaftStructurePieceType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTables;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class SideRoomDungeon extends MineshaftPiece {
    private static final int
        SECONDARY_AXIS_LEN = 9,
        Y_AXIS_LEN = 4,
        MAIN_AXIS_LEN = 9;
    private static final int
        LOCAL_X_END = SECONDARY_AXIS_LEN - 1,
        LOCAL_Y_END = Y_AXIS_LEN - 1,
        LOCAL_Z_END = MAIN_AXIS_LEN - 1;

    public SideRoomDungeon(TemplateManager structureManager, CompoundNBT compoundTag) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, compoundTag);
    }

    public SideRoomDungeon(int i, int pieceChainLen, Random random, MutableBoundingBox blockBox, Direction direction, BetterMineshaftStructure.Type type) {
        super(BetterMineshaftStructurePieceType.SIDE_ROOM_DUNGEON, i, pieceChainLen, type);
        this.setCoordBaseMode(direction);
        this.boundingBox = blockBox;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void readAdditional(CompoundNBT tag) {
        super.toNbt(tag);
    }

    public static MutableBoundingBox determineBoxPosition(List<StructurePiece> list, Random random, int x, int y, int z, Direction direction) {
        MutableBoundingBox blockBox = new MutableBoundingBox(x, y, z, x, y + Y_AXIS_LEN - 1, z);

        switch (direction) {
            case NORTH:
            default:
                blockBox.maxX = x + 4;
                blockBox.minX = x - 4;
                blockBox.minZ = z - (MAIN_AXIS_LEN - 1);
                break;
            case SOUTH:
                blockBox.maxX = x + 4;
                blockBox.minX = x - 4;
                blockBox.maxZ = z + (MAIN_AXIS_LEN - 1);
                break;
            case WEST:
                blockBox.minX = x - (MAIN_AXIS_LEN - 1);
                blockBox.maxZ = z + 4;
                blockBox.minZ = z - 4;
                break;
            case EAST:
                blockBox.maxX = x + (MAIN_AXIS_LEN - 1);
                blockBox.maxZ = z + 4;
                blockBox.minZ = z - 4;
        }

        // The following func call returns null if this new blockbox does not intersect with any pieces in the list.
        // If there is an intersection, the following func call returns the piece that intersects.
        StructurePiece intersectingPiece = StructurePiece.findIntersecting(list, blockBox);

        // Thus, this function returns null if blackBox intersects with an existing piece. Otherwise, we return blackbox
        return intersectingPiece != null ? null : blockBox;
    }

    @Override
    public void buildComponent(StructurePiece structurePiece, List<StructurePiece> list, Random random) {
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean create(IWorld world, ChunkGenerator<?> generator, Random random, MutableBoundingBox box, ChunkPos pos) {
         // Fill with stone then clean out with air
        this.fill(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END, LOCAL_Z_END, getBrickSelector());
        this.fill(world, box, 1, 1, 1, LOCAL_X_END - 1, LOCAL_Y_END - 1, LOCAL_Z_END - 1, CAVE_AIR);

        generateLegs(world, random);

        // Ladders
        BlockState LADDER = Blocks.LADDER.getDefaultState().with(LadderBlock.FACING, Direction.NORTH);
        this.fill(world, box, 4, 1, 1, 4, 3, 1, LADDER);

        // Spawner
        BlockPos spawnerPos = new BlockPos(this.getXWithOffset(4,5), this.getYWithOffset(1), this.getZWithOffset(4, 5));
        world.setBlockState(spawnerPos, Blocks.SPAWNER.getDefaultState(), 2);
        TileEntity blockEntity = world.getTileEntity(spawnerPos);
        if (blockEntity instanceof MobSpawnerTileEntity) {
            ((MobSpawnerTileEntity)blockEntity).getSpawnerBaseLogic().setEntityType(EntityType.CAVE_SPIDER);
        }

        // Cobwebs immediately surrounding chests
        this.chanceReplaceAir(world, box, random, .9f, 3, 1, 4, 5, 2, 6, Blocks.COBWEB.getDefaultState());

        // Fill room randomly with cobwebs
        this.chanceReplaceAir(world, box, random, .1f, 1, 1, 1, LOCAL_X_END - 1, 2, LOCAL_Z_END, Blocks.COBWEB.getDefaultState());

        // Chests
        this.generateChest(world, box, random, 1, 1, LOCAL_Z_END - 1, LootTables.CHESTS_ABANDONED_MINESHAFT);
        if (random.nextInt(2) == 0) { // Chance of second chest
            this.generateChest(world, box, random, LOCAL_X_END - 1, 1, LOCAL_Z_END - 1, LootTables.CHESTS_STRONGHOLD_CORRIDOR);
        }

        // Decorations
        this.addBiomeDecorations(world, box, random, 0, 0, 0, LOCAL_X_END, LOCAL_Y_END - 1, LOCAL_Z_END);

        return true;
    }

    private void generateLegs(IWorld world, Random random) {
        generateLeg(world, random, 1, 1, getBrickSelector());
        generateLeg(world, random, 1, LOCAL_Z_END - 1, getBrickSelector());
        generateLeg(world, random, LOCAL_X_END - 1, 1, getBrickSelector());
        generateLeg(world, random, LOCAL_X_END - 1, LOCAL_Z_END - 1, getBrickSelector());
    }
}
