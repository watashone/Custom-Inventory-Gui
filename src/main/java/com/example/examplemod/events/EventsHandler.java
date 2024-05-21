package com.example.examplemod.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = "examplemod")
public class EventsHandler {
    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent e) {
        if (!e.getWorld().isRemote && e.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntity();
            player.sendMessage(new TextComponentString("Hello, %p!".replace("%p", player.getName())));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e.getEntity();

            if (player.getName().equals("_Ivasik_"))
                player.dropItem(new ItemStack(Items.GOLDEN_APPLE, 1, 1), false);
        }
    }
}
