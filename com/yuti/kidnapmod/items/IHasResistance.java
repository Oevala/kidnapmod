package com.yuti.kidnapmod.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IHasResistance {
   int getBaseResistance(World var1);

   int getMergePercentRule(World var1);

   String getResistanceId();

   boolean canBeStruggledOut(ItemStack var1);

   ItemStack setCanBeStruggledOut(ItemStack var1, boolean var2);

   void notifyStruggle(EntityPlayer var1);
}
