package com.example.examplemod.inventory.caps;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class CAPCustomInventoryStorage implements Capability.IStorage<ICAPCustomInventory> {

    @Override
    public NBTBase writeNBT(Capability<ICAPCustomInventory> capability, ICAPCustomInventory instance, EnumFacing side) {
        NBTTagCompound properties = new NBTTagCompound();
        instance.getInventory().writeToNBT(properties);
        return properties;
    }

    @Override
    public void readNBT(Capability<ICAPCustomInventory> capability, ICAPCustomInventory instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound properties = (NBTTagCompound)nbt;
        instance.getInventory().readFromNBT(properties);
    }

}
