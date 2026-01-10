package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemKeyCollarMaster extends ItemKeyCollar {
   public ItemKeyCollarMaster(String name) {
      super(name);
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      tooltip.add(ChatFormatting.GOLD + "Master key");
   }

   public boolean func_77636_d(ItemStack stack) {
      return true;
   }

   public boolean canUseKey(ItemStack keyStack, ItemStack collarStack, EntityLivingBase holder) {
      return keyStack != null && keyStack.func_77973_b() instanceof ItemKeyCollar && collarStack != null && collarStack.func_77973_b() instanceof ItemCollar && holder != null;
   }

   public ItemStack setOwner(ItemStack stack, EntityPlayer owner) {
      return stack;
   }

   public ItemStack setTarget(ItemStack stack, EntityLivingBase owner) {
      return stack;
   }

   public boolean hasOwner(ItemStack stack) {
      return true;
   }

   public boolean hasTarget(ItemStack stack) {
      return true;
   }
}
