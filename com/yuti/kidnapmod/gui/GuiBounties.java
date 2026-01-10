package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.data.Bounty;
import com.yuti.kidnapmod.network.PacketDeleteBounty;
import com.yuti.kidnapmod.network.PacketHandler;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiBounties extends GuiScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private GuiButton deleteButton;
   protected String title = "Bounties";
   protected String noEntries = "There are no bounties for now";
   private GuiBountiesList selectionList;
   private final Minecraft client = Minecraft.func_71410_x();
   private List<Bounty> entries;
   private boolean isOpe;

   public GuiBounties(List<Bounty> bounties, boolean isOpe) {
      this.entries = bounties;
      this.isOpe = isOpe;
   }

   public void func_73866_w_() {
      this.title = I18n.func_135052_a("gui.bounties.title", new Object[0]);
      this.noEntries = I18n.func_135052_a("gui.bounties.noEntries", new Object[0]);
      this.selectionList = new GuiBountiesList(this, this.entries, this.field_146297_k, this.field_146294_l, this.field_146295_m, 32, this.field_146295_m - 32, 50);
      this.postInit();
   }

   public void postInit() {
      this.deleteButton = this.func_189646_b(new GuiButton(1, this.field_146294_l / 3, this.field_146295_m - 28, 150, 20, I18n.func_135052_a("gui.bounties.delete", new Object[0])));
      this.deleteButton.field_146124_l = false;
   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      this.selectionList.func_178039_p();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.selectionList.func_148128_a(mouseX, mouseY, partialTicks);
      this.func_73732_a(this.field_146289_q, this.title, this.field_146294_l / 2, 20, 16777215);
      if (this.selectionList.isEmpty()) {
         this.func_73732_a(this.field_146289_q, this.noEntries, this.field_146294_l / 2, this.field_146295_m / 2, 16777215);
         this.deleteButton.field_146124_l = false;
      }

      super.func_73863_a(mouseX, mouseY, partialTicks);
      GuiBountyEntry hoveredEntry = this.selectionList.getHoveredBounty();
      if (hoveredEntry != null) {
         Bounty bounty = hoveredEntry.getBounty();
         this.func_146283_a(bounty.getReward().func_82840_a(this.client.field_71439_g, TooltipFlags.NORMAL), mouseX, mouseY);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      this.selectionList.func_148179_a(mouseX, mouseY, mouseButton);
   }

   protected void func_146286_b(int mouseX, int mouseY, int state) {
      super.func_146286_b(mouseX, mouseY, state);
      this.selectionList.func_148181_b(mouseX, mouseY, state);
   }

   public void selectBounty(GuiBountyEntry selectedBounty) {
      if (selectedBounty == null) {
         this.deleteButton.field_146124_l = false;
      }

      if (this.isOpe) {
         this.deleteButton.field_146124_l = true;
      } else {
         Bounty bounty = selectedBounty.getBounty();
         this.deleteButton.field_146124_l = bounty.isClient(this.client.field_71439_g);
      }

   }

   protected void func_146284_a(GuiButton button) throws IOException {
      if (button.field_146124_l) {
         GuiBountyEntry selectedBounty = this.selectionList.getSelectedBounty();
         if (selectedBounty == null) {
            return;
         }

         Bounty bounty = selectedBounty.getBounty();
         if (button.field_146127_k == 1) {
            PacketHandler.INSTANCE.sendToServer(new PacketDeleteBounty(bounty.getId()));
            this.selectionList.removeEntry(selectedBounty);
         }
      }

   }
}
