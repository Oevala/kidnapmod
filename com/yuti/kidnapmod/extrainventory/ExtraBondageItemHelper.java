package com.yuti.kidnapmod.extrainventory;

import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItemCapabilities;
import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import com.yuti.kidnapmod.items.IHasBlindingEffect;
import com.yuti.kidnapmod.items.ItemGaggingEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ExtraBondageItemHelper {
   public static IExtraBondageItemHandler getExtraBondageItemHandler(EntityPlayer player) {
      IExtraBondageItemHandler handler = (IExtraBondageItemHandler)player.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_BONDAGE_ITEMS, (EnumFacing)null);
      handler.setPlayer(player);
      return handler;
   }

   public static int isExtraBondageItemEquiped(EntityPlayer player, Item bondageItem) {
      IExtraBondageItemHandler handler = getExtraBondageItemHandler(player);

      for(int a = 0; a < handler.getSlots(); ++a) {
         if (!handler.getStackInSlot(a).func_190926_b() && handler.getStackInSlot(a).func_77973_b() == bondageItem) {
            return a;
         }
      }

      return -1;
   }

   public static boolean isTypeEquiped(EntityPlayer player, ExtraBondageItemType type) {
      IExtraBondageItemHandler handler = getExtraBondageItemHandler(player);
      int[] var3 = type.getValidSlots();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int a = var3[var5];
         ItemStack stack = handler.getStackInSlot(a);
         if (stack != null && !stack.func_190926_b()) {
            Item item = stack.func_77973_b();
            if (item != null && item instanceof IExtraBondageItem) {
               IExtraBondageItem bondageItem = (IExtraBondageItem)item;
               if (bondageItem.getType(stack) == type) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public static void setStackInSlot(EntityPlayer player, ExtraBondageItemType slotIn, ItemStack stackTo) {
      if (player != null && slotIn != null && stackTo != null) {
         IExtraBondageItemHandler handler = getExtraBondageItemHandler(player);
         ItemStack stackFrom = handler.getStackInSlot(slotIn.getMainSlot());
         handler.setStackInSlot(slotIn.getMainSlot(), stackTo);
         IExtraBondageItem bondageItemTo;
         if (stackFrom != null && !stackFrom.func_190926_b() && stackFrom.func_77973_b() instanceof IExtraBondageItem) {
            bondageItemTo = (IExtraBondageItem)stackFrom.func_77973_b();
            bondageItemTo.onUnequipped(stackFrom, player);
         }

         if (stackTo.func_77973_b() instanceof IExtraBondageItem) {
            bondageItemTo = (IExtraBondageItem)stackTo.func_77973_b();
            bondageItemTo.onEquipped(stackTo, player);
         }
      }

   }

   public static ItemStack getItemStackFromSlot(EntityPlayer player, ExtraBondageItemType slotIn) {
      if (player != null && slotIn != null) {
         IExtraBondageItemHandler handler = getExtraBondageItemHandler(player);
         return handler.getStackInSlot(slotIn.getMainSlot());
      } else {
         return null;
      }
   }

   public static boolean hasGaggingEffect(EntityPlayer player) {
      IExtraBondageItemHandler handler = getExtraBondageItemHandler(player);

      for(int a = 0; a < handler.getSlots(); ++a) {
         ItemStack stack = handler.getStackInSlot(a);
         if (stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemGaggingEffect) {
            return true;
         }
      }

      return false;
   }

   public static boolean hasBlindingEffect(EntityPlayer player) {
      IExtraBondageItemHandler handler = getExtraBondageItemHandler(player);

      for(int a = 0; a < handler.getSlots(); ++a) {
         ItemStack stack = handler.getStackInSlot(a);
         if (stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof IHasBlindingEffect) {
            return true;
         }
      }

      return false;
   }
}
