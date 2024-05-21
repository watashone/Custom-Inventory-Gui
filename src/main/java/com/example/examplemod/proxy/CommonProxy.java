package com.example.examplemod.proxy;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.inventory.gui.GUICustomInv;
import com.example.examplemod.inventory.gui.GuiHandler;
import com.example.examplemod.inventory.KeyHandler;
import com.example.examplemod.inventory.NetworkHandler;
import com.example.examplemod.inventory.caps.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event)
    {
        NetworkHandler.init();
    }

    public void init(FMLInitializationEvent e) {

        CapabilityManager.INSTANCE.register(ICAPCustomInventory.class, new CAPCustomInventoryStorage(), CAPCustomInventory.class);
        CapabilityEventHandler.register();
        KeyHandler.register();
        NetworkRegistry.INSTANCE.registerGuiHandler(ExampleMod.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
