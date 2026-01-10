package com.yuti.kidnapmod.items;

import net.minecraft.item.ItemStack;

public interface IItemBondageItemHolder {
   ItemStack getBind(ItemStack var1);

   void setBind(ItemStack var1, ItemStack var2);

   ItemStack getGag(ItemStack var1);

   void setGag(ItemStack var1, ItemStack var2);

   ItemStack getBlindfold(ItemStack var1);

   void setBlindfold(ItemStack var1, ItemStack var2);

   ItemStack getEarplugs(ItemStack var1);

   void setEarplugs(ItemStack var1, ItemStack var2);

   ItemStack getClothes(ItemStack var1);

   void setClothes(ItemStack var1, ItemStack var2);

   ItemStack getCollar(ItemStack var1);

   void setCollar(ItemStack var1, ItemStack var2);
}
