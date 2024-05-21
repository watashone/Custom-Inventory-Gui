package com.example.examplemod.inventory.gui;

import com.example.examplemod.inventory.container.ContainerCustomInv;
import com.example.examplemod.inventory.caps.CAPCustomInventoryProvider;
import com.example.examplemod.inventory.caps.ICAPCustomInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    public static final int INVENTORY_GUI_ID = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ICAPCustomInventory inv = player.getCapability(CAPCustomInventoryProvider.INVENTORY_CAP, null);
        if(ID == INVENTORY_GUI_ID) {
            return new ContainerCustomInv(player.inventory, inv.getInventory(), player);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        ICAPCustomInventory inv = player.getCapability(CAPCustomInventoryProvider.INVENTORY_CAP, null);
        if(ID == INVENTORY_GUI_ID) {
            return new GUICustomInv(player, player.inventory, inv.getInventory());
        }
        return null;
    }

}
