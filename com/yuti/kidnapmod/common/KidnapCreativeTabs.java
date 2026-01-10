package com.yuti.kidnapmod.common;

import com.yuti.kidnapmod.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class KidnapCreativeTabs extends CreativeTabs {
   public KidnapCreativeTabs(String label) {
      super(label);
   }

   public ItemStack func_78016_d() {
      return new ItemStack(ModItems.ROPES);
   }
}
