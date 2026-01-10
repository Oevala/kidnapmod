package com.yuti.kidnapmod.loaders;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class ItemTask {
   public final String item;
   public final int data;
   public final int amount;

   public ItemTask(String itemIn, int dataIn, int amountIn) {
      this.item = itemIn;
      this.data = dataIn;
      this.amount = amountIn;
   }

   public ItemStack getItemStack() {
      return new ItemStack(Item.func_111206_d(this.item), this.amount, this.data);
   }

   public String toString() {
      ItemStack stack = this.getItemStack();
      return stack != null && !stack.func_190926_b() ? TextFormatting.BLUE + "[" + stack.func_190916_E() + " x " + stack.func_82833_r() + "]" : null;
   }

   public static boolean doesStackCompleteTask(ItemStack stack, ItemStack taskStack) {
      if (stack != null && taskStack != null && !stack.func_190926_b() && !taskStack.func_190926_b()) {
         return stack.func_77960_j() == taskStack.func_77960_j() && stack.func_190916_E() >= taskStack.func_190916_E() && stack.func_77973_b().equals(taskStack.func_77973_b());
      } else {
         return false;
      }
   }
}
