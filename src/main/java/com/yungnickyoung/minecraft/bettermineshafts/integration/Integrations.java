package com.yungnickyoung.minecraft.bettermineshafts.integration;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class Integrations {
    // List of supported modules
    public static RusticModule RUSTIC = new RusticModule();
    public static CharmModule CHARM = new CharmModule();

    public static List<CompatModule> MODULES = Lists.newArrayList(
        RUSTIC,
        CHARM
    );

    /** Collection of all lanterns from enabled and successfully loaded mods **/
    private static List<IBlockState> LANTERNS = Lists.newArrayList();

    /**
     * Update all modules' status and all internal lists (e.g. lanterns).
     */
    public static void update() {
        // Update mods enabled/disabled status in case config settings have changed
        MODULES.forEach(module -> {
            if (module.shouldBeEnabled() && !module.isEnabled()) module.enable();  // Enable
            if (!module.shouldBeEnabled() && module.isEnabled()) module.disable(); // Disable
        });

        // Add lanterns for enabled mods
        MODULES.stream().filter(CompatModule::isEnabled).forEach(module -> addIfAbsent(LANTERNS, module.lanterns));

        // Remove lanterns for disabled mods
        MODULES.stream().filter(module -> !module.isEnabled()).forEach(module -> LANTERNS.removeAll(module.lanterns));
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

    private static <T> void addIfAbsent(List<T> list, T item) {
        if (!list.contains(item)) list.add(item);
    }

    private static <T> void addIfAbsent(List<T> list, Collection<T> items) {
        items.forEach(item -> addIfAbsent(list, item));
    }
}
