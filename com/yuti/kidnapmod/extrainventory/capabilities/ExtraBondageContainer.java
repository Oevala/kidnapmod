package com.yuti.kidnapmod.extrainventory.capabilities;

import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.ItemStackHandler;

public class ExtraBondageContainer extends ItemStackHandler implements IExtraBondageItemHandler {
   private static final int BONDAGE_SLOTS = 6;
   private boolean[] changed = new boolean[6];
   private boolean blockEvents = false;
   private EntityLivingBase player;

   public ExtraBondageContainer() {
      super(6);
   }

   public void setSize(int size) {
      if (size < 6) {
         size = 6;
      }

      super.setSize(size);
      boolean[] old = this.changed;
      this.changed = new boolean[size];

      for(int i = 0; i < old.length && i < this.changed.length; ++i) {
         this.changed[i] = old[i];
      }

   }

   public boolean isItemValidForSlot(int slot, ItemStack stack, EntityLivingBase player) {
      if (stack != null && !stack.func_190926_b() && stack.hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)) {
         IExtraBondageItem boondageItem = (IExtraBondageItem)stack.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null);
         return boondageItem.canEquip(stack, player) && boondageItem.getType(stack).hasSlot(slot);
      } else {
         return false;
      }
   }

   public void setStackInSlot(int slot, ItemStack stack) {
      if (stack == null || stack.func_190926_b() || this.isItemValidForSlot(slot, stack, this.player)) {
         super.setStackInSlot(slot, stack);
      }

   }

   public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
      return !this.isItemValidForSlot(slot, stack, this.player) ? stack : super.insertItem(slot, stack, simulate);
   }

   public boolean isEventBlocked() {
      return this.blockEvents;
   }

   public void setEventBlock(boolean blockEvents) {
      this.blockEvents = blockEvents;
   }

   protected void onContentsChanged(int slot) {
      this.setChanged(slot, true);
   }

   public boolean isChanged(int slot) {
      if (this.changed == null) {
         this.changed = new boolean[this.getSlots()];
      }

      return this.changed[slot];
   }

   public void setChanged(int slot, boolean change) {
      if (this.changed == null) {
         this.changed = new boolean[this.getSlots()];
      }

      this.changed[slot] = change;
   }

   public void setPlayer(EntityLivingBase player) {
      this.player = player;
   }
}
