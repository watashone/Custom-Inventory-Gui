package com.example.examplemod.inventory.container;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


public class ContainerCustomInv extends Container {

    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    private final EntityPlayer thePlayer;

    public ContainerCustomInv(InventoryPlayer playerInventory, CustomInventory cInventory, EntityPlayer player) {

        this.thePlayer = player;
//        this.addSlotToContainer(new StandartSlot(player, cInventory, 0, 250, 8));
//        this.addSlotToContainer(new StandartSlot(player, cInventory, 1, 250, 44));

        EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[0]; // 74 52
        this.addSlotToContainer(new ArmorSlot(playerInventory, 39, 96, 82, entityequipmentslot, thePlayer));
        entityequipmentslot = VALID_EQUIPMENT_SLOTS[1];
        this.addSlotToContainer(new ArmorSlot(playerInventory, 38, 133, 82, entityequipmentslot, thePlayer));
        entityequipmentslot = VALID_EQUIPMENT_SLOTS[2];
        this.addSlotToContainer(new ArmorSlot(playerInventory, 37, 95, 193, entityequipmentslot, thePlayer));
        entityequipmentslot = VALID_EQUIPMENT_SLOTS[3];
        this.addSlotToContainer(new ArmorSlot(playerInventory, 36, 132, 193, entityequipmentslot, thePlayer));


        for (int l = 0; l < 3; ++l) {
            int yPos = 100 + l * 27;
            for (int j1 = 0; j1 < 9; ++j1) {
                int xPos = 238 + j1 * 25;
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, xPos, yPos));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 236 + i1 * 25, 201));
        }

    }


    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 4) {
                if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }

            if (index > 3) {
                if (itemstack1.getItem() instanceof ItemArmor) {
                    if (!this.mergeItemStack(itemstack1, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}
