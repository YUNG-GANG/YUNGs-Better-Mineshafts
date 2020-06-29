package com.yungnickyoung.minecraft.bettermineshafts.event;

import com.yungnickyoung.minecraft.bettermineshafts.world.MapGenBetterMineshaft;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventMineshaftGen {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onMineshaftGen(InitMapGenEvent event) {
        if (event.getType() == InitMapGenEvent.EventType.MINESHAFT) {
            event.setNewGen(new MapGenBetterMineshaft());
        }
    }
}
