package com.example.examplemod.inventory.container;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ArmorSlot extends Slot {
    EntityEquipmentSlot entityequipmentslot;
    EntityPlayer thePlayer;
    public ArmorSlot(IInventory inventoryIn, int index, int xPosition, int yPosition,
                     EntityEquipmentSlot entityequipmentslot, EntityPlayer player) {
        super(inventoryIn, index, xPosition, yPosition);
        this.thePlayer = player;
        this.entityequipmentslot = entityequipmentslot;
    }

    public int getSlotStackLimit(){
        return 1;
    }

    public boolean isItemValid(ItemStack stack){
        return stack.getItem().isValidArmor(stack, entityequipmentslot, thePlayer);
    }

    public boolean canTakeStack(EntityPlayer playerIn){
        ItemStack itemstack = this.getStack();
        return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
    }
    @Nullable
    @SideOnly(Side.CLIENT)
    public String getSlotTexture(){
        return backgroundName;
    }
}
