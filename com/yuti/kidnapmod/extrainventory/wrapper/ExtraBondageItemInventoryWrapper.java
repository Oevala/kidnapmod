package com.yuti.kidnapmod.extrainventory.wrapper;

import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ExtraBondageItemInventoryWrapper implements IInventory {
   final IExtraBondageItemHandler handler;
   final EntityPlayer player;

   public ExtraBondageItemInventoryWrapper(IExtraBondageItemHandler handler) {
      this.handler = handler;
      this.player = null;
   }

   public ExtraBondageItemInventoryWrapper(IExtraBondageItemHandler handler, EntityPlayer player) {
      this.handler = handler;
      this.player = player;
   }

   public String func_70005_c_() {
      return "KidnapModExtraInventory";
   }

   public boolean func_145818_k_() {
      return false;
   }

   public ITextComponent func_145748_c_() {
      return new TextComponentString(this.func_70005_c_());
   }

   public int func_70302_i_() {
      return this.handler.getSlots();
   }

   public boolean func_191420_l() {
      return false;
   }

   public ItemStack func_70301_a(int index) {
      return this.handler.getStackInSlot(index);
   }

   public ItemStack func_70298_a(int index, int count) {
      return this.handler.extractItem(index, count, false);
   }

   public ItemStack func_70304_b(int index) {
      ItemStack out = this.func_70301_a(index);
      this.handler.setStackInSlot(index, ItemStack.field_190927_a);
      return out;
   }

   public void func_70299_a(int index, ItemStack stack) {
      this.handler.setStackInSlot(index, stack);
   }

   public int func_70297_j_() {
      return 64;
   }

   public void func_70296_d() {
   }

   public boolean func_70300_a(EntityPlayer player) {
      return true;
   }

   public void func_174889_b(EntityPlayer player) {
   }

   public void func_174886_c(EntityPlayer player) {
   }

   public boolean func_94041_b(int index, ItemStack stack) {
      return this.handler.isItemValidForSlot(index, stack, this.player);
   }

   public int func_174887_a_(int id) {
      return 0;
   }

   public void func_174885_b(int id, int value) {
   }

   public int func_174890_g() {
      return 0;
   }

   public void func_174888_l() {
      for(int i = 0; i < this.func_70302_i_(); ++i) {
         this.func_70299_a(i, ItemStack.field_190927_a);
      }

   }
}
