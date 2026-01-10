package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.data.Bounty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;

public class GuiBountiesList extends GuiListExtended {
   private GuiBounties guiBounties;
   private final List<GuiBountyEntry> entries = new ArrayList();
   private int selectedIdx = -1;

   public GuiBountiesList(GuiBounties guiBounties, List<Bounty> bounties, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
      super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
      this.guiBounties = guiBounties;
      Iterator var9 = bounties.iterator();

      while(var9.hasNext()) {
         Bounty bounty = (Bounty)var9.next();
         this.entries.add(new GuiBountyEntry(bounty, this));
      }

   }

   public GuiBountyEntry getListEntry(int index) {
      return (GuiBountyEntry)this.entries.get(index);
   }

   protected int func_148127_b() {
      return this.entries.size();
   }

   protected boolean func_148131_a(int slotIndex) {
      return slotIndex == this.selectedIdx;
   }

   public void selectBounty(int slotIndex) {
      this.selectedIdx = slotIndex;
      GuiBountyEntry entry = this.getSelectedBounty();
      this.guiBounties.selectBounty(entry);
   }

   @Nullable
   public GuiBountyEntry getSelectedBounty() {
      return this.selectedIdx >= 0 && this.selectedIdx < this.func_148127_b() ? this.getListEntry(this.selectedIdx) : null;
   }

   public void removeEntry(GuiBountyEntry entry) {
      this.entries.remove(entry);
   }

   @Nullable
   public GuiBountyEntry getHoveredBounty() {
      Iterator var1 = this.entries.iterator();

      GuiBountyEntry entry;
      do {
         if (!var1.hasNext()) {
            return null;
         }

         entry = (GuiBountyEntry)var1.next();
      } while(!entry.isHovered(this.field_148150_g, this.field_148162_h));

      return entry;
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }
}
