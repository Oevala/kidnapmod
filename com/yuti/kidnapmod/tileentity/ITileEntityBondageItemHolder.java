package com.yuti.kidnapmod.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface ITileEntityBondageItemHolder {
   ItemStack getBind();

   void setBind(ItemStack var1);

   ItemStack getGag();

   void setGag(ItemStack var1);

   ItemStack getBlindfold();

   void setBlindfold(ItemStack var1);

   ItemStack getEarplugs();

   void setEarplugs(ItemStack var1);

   ItemStack getClothes();

   void setClothes(ItemStack var1);

   ItemStack getCollar();

   void setCollar(ItemStack var1);

   void readDataHolder(NBTTagCompound var1);

   NBTTagCompound writeDataHolder(NBTTagCompound var1);
}
