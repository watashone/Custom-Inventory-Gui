package com.example.examplemod.inventory;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class KeyHandler {


    public static KeyBinding openKey = new KeyBinding("Open Inventory", Keyboard.KEY_H, "Custom Inventory Keys");

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        ClientRegistry.registerKeyBinding(openKey);
    }

    @SubscribeEvent
    public void onKey(KeyInputEvent event) {
        if (openKey.isPressed()) {
            NetworkHandler.network.sendToServer(new OpenInventoryMessage());
        }
    }

}
