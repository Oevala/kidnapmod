package com.yuti.kidnapmod.gui.extrainventory;

import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.extrainventory.PacketOpenExtraBondageInventory;
import com.yuti.kidnapmod.network.extrainventory.PacketOpenNormalInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;

public class GuiExtraBondageButton extends GuiButton {
   private final GuiContainer parentGui;

   public GuiExtraBondageButton(int buttonId, GuiContainer parentGui, int x, int y, int width, int height, String buttonText) {
      super(buttonId, x, parentGui.getGuiTop() + y, width, height, buttonText);
      this.parentGui = parentGui;
   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      boolean pressed = super.func_146116_c(mc, mouseX - this.parentGui.getGuiLeft(), mouseY);
      if (pressed) {
         if (this.parentGui instanceof GuiInventory) {
            PacketHandler.INSTANCE.sendToServer(new PacketOpenExtraBondageInventory());
         } else {
            ((GuiBondageExtended)this.parentGui).displayNormalInventory();
            PacketHandler.INSTANCE.sendToServer(new PacketOpenNormalInventory());
         }
      }

      return pressed;
   }

   public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
      if (this.field_146125_m) {
         int x = this.field_146128_h + this.parentGui.getGuiLeft();
         mc.func_110434_K().func_110577_a(GuiBondageExtended.background);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_146123_n = mouseX >= x && mouseY >= this.field_146129_i && mouseX < x + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
         int k = this.func_146114_a(this.field_146123_n);
         GlStateManager.func_179147_l();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b(0.0F, 0.0F, 200.0F);
         if (k == 1) {
            this.func_73729_b(x, this.field_146129_i, 192, 50, 16, 16);
         } else {
            this.func_73729_b(x, this.field_146129_i, 209, 51, 16, 16);
         }

         GlStateManager.func_179121_F();
         this.func_146119_b(mc, mouseX, mouseY);
      }

   }
}
