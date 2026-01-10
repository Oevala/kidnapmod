package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.data.Bounty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiBountyEntry implements IGuiListEntry {
   private Bounty bounty;
   private final Minecraft client;
   private GuiBountiesList guiList;
   private ResourceLocation bountyTexture = new ResourceLocation("knapm", "textures/special/bounty.png");
   private int x;
   private int y;
   private int width;
   private int height;

   public GuiBountyEntry(Bounty bounty, GuiBountiesList guiList) {
      this.bounty = bounty;
      this.guiList = guiList;
      this.client = Minecraft.func_71410_x();
   }

   public void func_192634_a(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected, float partialTicks) {
      this.x = x;
      this.y = y;
      this.client.field_71466_p.func_78276_b(I18n.func_135052_a("gui.bounties.client", new Object[]{this.bounty.getClientName()}), x + 32 + 3, y, 16777215);
      this.client.field_71466_p.func_78276_b(I18n.func_135052_a("gui.bounties.target", new Object[]{this.bounty.getTargetName()}), x + 32 + 3, y + this.client.field_71466_p.field_78288_b + 4, 16777215);
      this.client.field_71466_p.func_78276_b(I18n.func_135052_a("gui.bounties.reward", new Object[]{this.bounty.getRewardDescription()}), x + 32 + 3, y + this.client.field_71466_p.field_78288_b + this.client.field_71466_p.field_78288_b + 4, 16777215);
      int[] timeRemain = this.bounty.getRemainingTime();
      if (timeRemain.length == 2) {
         this.client.field_71466_p.func_78276_b(I18n.func_135052_a("gui.bounties.time", new Object[]{String.valueOf(timeRemain[0]), String.valueOf(timeRemain[1])}), x + 32 + 3, y + this.client.field_71466_p.field_78288_b + this.client.field_71466_p.field_78288_b + this.client.field_71466_p.field_78288_b + 4, 16777215);
      }

      this.width = listWidth;
      this.height = slotHeight;
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.client.func_110434_K().func_110577_a(this.bountyTexture);
      GlStateManager.func_179147_l();
      Gui.func_146110_a(x, y, 0.0F, 0.0F, 32, 32, 32.0F, 32.0F);
      GlStateManager.func_179084_k();
   }

   public boolean func_148278_a(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
      this.guiList.selectBounty(slotIndex);
      return false;
   }

   public void func_148277_b(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {
   }

   public void func_192633_a(int slotIndex, int x, int y, float partialTicks) {
   }

   public Bounty getBounty() {
      return this.bounty;
   }

   public boolean isHovered(int mouseX, int mouseY) {
      return mouseX >= this.x && mouseX <= this.x + this.width && mouseY >= this.y && mouseY <= this.y + this.height;
   }
}
