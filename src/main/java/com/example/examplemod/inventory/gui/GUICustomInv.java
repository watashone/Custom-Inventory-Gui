package com.example.examplemod.inventory.gui;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.inventory.container.ContainerCustomInv;
import com.example.examplemod.inventory.container.CustomInventory;
import com.example.examplemod.inventory.container.StandartSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;


public class GUICustomInv extends GuiContainer {

    private float oldMouseX;
    private float oldMouseY;
    CustomGuiHelper customGuiHelper = new CustomGuiHelper();
    private static final ResourceLocation INVENTORY_GUI_TEXTURE = new ResourceLocation(ExampleMod.MODID + ":textures/gui/inventory_gui.png");
    private static final ResourceLocation INVENTORY_SLOTS_TEXTURE = new ResourceLocation(ExampleMod.MODID + ":textures/gui/inventory_slots.png");
    private static final ResourceLocation INVENTORY_HOTBAR_TEXTURE = new ResourceLocation(ExampleMod.MODID + ":textures/gui/inventory_hotbar.png");

    public GUICustomInv(EntityPlayer player, InventoryPlayer inventoryPlayer, CustomInventory cInventory) {
        super(new ContainerCustomInv(inventoryPlayer, cInventory, player));
        this.allowUserInput = false;
        this.xSize = 0;
        this.ySize = 0;
    }

    @Override
    public void initGui() {
        this.mc.player.openContainer = this.inventorySlots;
        this.guiLeft = 0;
        this.guiTop = 0;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int i = this.guiLeft;
        int j = this.guiTop;
        this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) i, (float) j, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableRescaleNormal();
        this.hoveredSlot = null;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
            Slot slot = this.inventorySlots.inventorySlots.get(i1);

            if (slot.isEnabled()) {
                this.drawSlot(slot);
            }

            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
                this.hoveredSlot = slot;
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                double j1 = slot.xPos - 0.25;
                double k1 = slot.yPos - 0.25;
                if (slot.getSlotIndex() >= 0 && slot.getSlotIndex() <= 8) j1 += 0.25;
                if (slot.getSlotIndex() == 39 || slot.getSlotIndex() == 37) j1 += 0.5;
                int slotSize = getSlotSize(slot);
                GlStateManager.colorMask(true, true, true, false);
                CustomGuiHelper.drawGradientRect(j1, k1, j1 + slotSize, k1 + slotSize, -2130706433, -2130706433);
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
            }
        }

        RenderHelper.disableStandardItemLighting();
        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        RenderHelper.enableGUIStandardItemLighting();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, mouseX, mouseY));
        InventoryPlayer inventoryplayer = this.mc.player.inventory;
        ItemStack itemstack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;

        if (!itemstack.isEmpty()) {
            int j2 = 8;
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
                itemstack = itemstack.copy();
                itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
            } else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
                itemstack = itemstack.copy();
                itemstack.setCount(this.dragSplittingRemnant);

                if (itemstack.isEmpty()) {
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            super.drawItemStack(itemstack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!this.returningStack.isEmpty()) {
            float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F) {
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            int l1 = this.touchUpX + (int) ((float) l2 * f);
            int i2 = this.touchUpY + (int) ((float) i3 * f);
            super.drawItemStack(this.returningStack, l1, i2, (String) null);
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
    }

    /**
     * Рисуем задний фон(т.е. все, что позади предметов)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(INVENTORY_GUI_TEXTURE);
        this.xSize = 0;
        this.ySize = 0;
        customGuiHelper.drawTexturedModalRect(22, 30, 0, 0, 203, 250);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(INVENTORY_SLOTS_TEXTURE);
        customGuiHelper.drawTexturedModalRect(230, 91, 0, 0, 250, 125);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(INVENTORY_HOTBAR_TEXTURE);
        customGuiHelper.drawTexturedModalRect(230, 195, 0, 0, 250, 221);

    }

    @Override
    public void drawSlot(Slot slotIn) {
        int slotSize = getSlotSize(slotIn);
        float glScale = slotSize / 16.0F;
        int i = MathHelper.floor(slotIn.xPos / glScale);
        int j = MathHelper.floor(slotIn.yPos / glScale);
        ItemStack itemstack = slotIn.getStack();
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
        String s = null;

        if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty()) {
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        } else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty()) {
            if (this.dragSplittingSlots.size() == 1) {
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k) {
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            } else {
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }

        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        GL11.glPushMatrix();
        GL11.glScalef(glScale, glScale, 1.0F);

        if (itemstack.isEmpty() && slotIn.isEnabled()) {
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();
            if (textureatlassprite != null) {
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                customGuiHelper.drawTexturedModalRect(slotIn.xPos, slotIn.yPos, textureatlassprite, slotSize, slotSize);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1) {
            if (flag) {
                double tempX = slotIn.xPos / glScale - 0.25;
                double tempY = slotIn.yPos / glScale - 0.25;
                if (slotIn.getSlotIndex() >= 0 && slotIn.getSlotIndex() <= 8) tempX += 0.25;
                if (slotIn.getSlotIndex() == 39 || slotIn.getSlotIndex() == 37) tempX += 0.5;
                CustomGuiHelper.drawRect(tempX, tempY, tempX + 16, tempY + 16, -2130706433);
            }

            GlStateManager.enableDepth();
            if (slotIn.getSlotIndex() >= 9 && slotIn.getSlotIndex() < 18) j += 1;
            if (slotIn.getSlotIndex() == 39 || slotIn.getSlotIndex() == 37) i += 1;
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
        }

        GL11.glPopMatrix();
        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }

    private int getSlotSize(Slot slot) {
        int slotId = slot.getSlotIndex();
        if (slotId >= 0 && slotId <= 1 && slot.getClass() == StandartSlot.class) {
            return 32;
        } else if (slotId >= 36 && slotId <= 39) {
            return 19;
        } else if (slotId >= 0 && slotId <= 35) {
            return 22;
        }
        return 16;
    }

    @Override
    public boolean isMouseOverSlot(Slot slotIn, int mouseX, int mouseY) {
        int slotSize = getSlotSize(slotIn);
        return CustomGuiHelper.isPointInRegion(slotIn.xPos, slotIn.yPos, slotSize, slotSize, mouseX, mouseY, this.guiLeft, this.guiTop);
    }

    @SubscribeEvent
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent event) {
        if (event.isCancelable() && event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            event.setCanceled(true);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (isMouseWithinBounds(mouseX, mouseY)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (isMouseWithinBounds(mouseX, mouseY)) {
            super.mouseReleased(mouseX, mouseY, state);
        }
    }

    private boolean isMouseWithinBounds(int mouseX, int mouseY) {
        if (getSlotUnderMouse(mouseX, mouseY)) return true;
        return !((mouseX >= 225 && mouseX <= 460 && mouseY >= 91 && mouseY <= 190)
                || (mouseX >= 230 && mouseX <= 464 && mouseY >= 195 && mouseY <= 229)
                || (mouseX >= 22 && mouseX <= 225 && mouseY >= 30 && mouseY <= 280));
    }

    private boolean getSlotUnderMouse(int mouseX, int mouseY) {
        for (int slotId = 0; slotId < inventorySlots.inventorySlots.size(); slotId++) {
            Slot slot = inventorySlots.inventorySlots.get(slotId);
            if (isMouseOverSlot(slot, mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }


}