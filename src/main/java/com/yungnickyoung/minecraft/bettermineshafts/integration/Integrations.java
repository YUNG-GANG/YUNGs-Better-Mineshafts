package com.yungnickyoung.minecraft.bettermineshafts.integration;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Integrations {
    // List of supported modules
    public static RusticModule RUSTIC = new RusticModule();
    public static CharmModule CHARM = new CharmModule();
    public static VariedCommoditiesModule VARIED_COMMODITIES = new VariedCommoditiesModule();

    private static final List<CompatModule> MODULES_LIST = Lists.newArrayList(
        RUSTIC,
        CHARM,
        VARIED_COMMODITIES
    );

    /** Collections of items from enabled and successfully loaded mods **/
    private static List<IBlockState> LANTERNS = Lists.newArrayList();
    private static List<IBlockState> LEFT_TORCHES = Lists.newArrayList(Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST)); // Includes vanilla torch no matter what
    private static List<IBlockState> RIGHT_TORCHES = Lists.newArrayList(Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST)); // Includes vanilla torch no matter what

    /**
     * Update all modules' status and all internal lists (e.g. lanterns).
     */
    public static void update() {
        // Update mods enabled/disabled status in case config settings have changed
        MODULES_LIST.forEach(module -> {
            if (module.shouldBeEnabled() && !module.isEnabled()) module.enable();  // Enable
            if (!module.shouldBeEnabled() && module.isEnabled()) module.disable(); // Disable
        });

        // Add items for enabled mods
        MODULES_LIST.stream().filter(CompatModule::isEnabled).forEach(module -> addIfAbsent(LANTERNS, module.lanterns));
        MODULES_LIST.stream().filter(CompatModule::isEnabled).forEach(module -> addIfAbsent(LEFT_TORCHES, module.leftTorches));
        MODULES_LIST.stream().filter(CompatModule::isEnabled).forEach(module -> addIfAbsent(RIGHT_TORCHES, module.rightTorches));

        // Remove items for disabled mods
        MODULES_LIST.stream().filter(module -> !module.isEnabled()).forEach(module -> LANTERNS.removeAll(module.lanterns));
        MODULES_LIST.stream().filter(module -> !module.isEnabled()).forEach(module -> LEFT_TORCHES.removeAll(module.leftTorches));
        MODULES_LIST.stream().filter(module -> !module.isEnabled()).forEach(module -> RIGHT_TORCHES.removeAll(module.rightTorches));
    }

    /**
     * @return A random lantern from the list of all loaded lanterns. null if no lanterns enabled.
     */
    public static IBlockState getLantern(Random random) {
        if (LANTERNS.size() == 0) return null;

        float delta = 1f / LANTERNS.size();
        float currBottom = 0f;

        float r = random.nextFloat();
        for (IBlockState lantern : LANTERNS) {
            if (r >= currBottom && r < currBottom + delta) return lantern;
            currBottom += delta;
        }

        return LANTERNS.get(LANTERNS.size() - 1);
    }

    /**
     * @return A random torch from the list of all loaded lanterns. null if no lanterns enabled.
     */
    public static IBlockState getLeftTorch(Random random) {
        if (LEFT_TORCHES.size() == 0) return null;

        float delta = 1f / LEFT_TORCHES.size();
        float currBottom = 0f;

        float r = random.nextFloat();
        for (IBlockState torch : LEFT_TORCHES) {
            if (r >= currBottom && r < currBottom + delta) return torch;
            currBottom += delta;
        }

        return LEFT_TORCHES.get(LEFT_TORCHES.size() - 1);
    }

    /**
     * @return A random torch from the list of all loaded lanterns. null if no lanterns enabled.
     */
    public static IBlockState getRightTorch(Random random) {
        if (RIGHT_TORCHES.size() == 0) return null;

        float delta = 1f / RIGHT_TORCHES.size();
        float currBottom = 0f;

        float r = random.nextFloat();
        for (IBlockState torch : RIGHT_TORCHES) {
            if (r >= currBottom && r < currBottom + delta) return torch;
            currBottom += delta;
        }

        return RIGHT_TORCHES.get(RIGHT_TORCHES.size() - 1);
    }

    private static <T> void addIfAbsent(List<T> list, T item) {
        if (!list.contains(item)) list.add(item);
    }

    private static <T> void addIfAbsent(List<T> list, Collection<T> items) {
        items.forEach(item -> addIfAbsent(list, item));
    }
}
