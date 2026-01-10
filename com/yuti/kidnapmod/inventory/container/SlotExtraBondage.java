package com.yuti.kidnapmod.inventory.container;

import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItemCapabilities;
import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.SlotItemHandler;

public class SlotExtraBondage extends SlotItemHandler {
   int bondageSlot;
   EntityPlayer player;

   public SlotExtraBondage(EntityPlayer player, IExtraBondageItemHandler itemHandler, int slot, int par4, int par5) {
      super(itemHandler, slot, par4, par5);
      this.bondageSlot = slot;
      this.player = player;
   }

   public boolean func_75214_a(ItemStack stack) {
      return ((IExtraBondageItemHandler)this.getItemHandler()).isItemValidForSlot(this.bondageSlot, stack, this.player);
   }

   public boolean func_82869_a(EntityPlayer player) {
      ItemStack stack = this.func_75211_c();
      if (stack != null && !stack.func_190926_b()) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.isTiedUp()) {
            return false;
         } else if (EnchantmentHelper.func_190938_b(stack)) {
            return false;
         } else {
            IExtraBondageItem bondageItem = (IExtraBondageItem)stack.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null);
            return bondageItem.canUnequip(stack, player);
         }
      } else {
         return false;
      }
   }

   public ItemStack func_190901_a(EntityPlayer playerIn, ItemStack stack) {
      if (!this.func_75216_d() && !((IExtraBondageItemHandler)this.getItemHandler()).isEventBlocked() && stack.hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)) {
         ((IExtraBondageItem)stack.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)).onUnequipped(stack, playerIn);
      }

      super.func_190901_a(playerIn, stack);
      return stack;
   }

   public void func_75215_d(ItemStack stack) {
      if (this.func_75216_d() && !ItemStack.func_77989_b(stack, this.func_75211_c()) && !((IExtraBondageItemHandler)this.getItemHandler()).isEventBlocked() && this.func_75211_c().hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)) {
         ((IExtraBondageItem)this.func_75211_c().getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)).onUnequipped(this.func_75211_c(), this.player);
      }

      ItemStack oldstack = this.func_75211_c().func_77946_l();
      super.func_75215_d(stack);
      if (this.func_75216_d() && !ItemStack.func_77989_b(oldstack, this.func_75211_c()) && !((IExtraBondageItemHandler)this.getItemHandler()).isEventBlocked() && this.func_75211_c().hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)) {
         ((IExtraBondageItem)this.func_75211_c().getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)).onEquipped(this.func_75211_c(), this.player);
      }

   }

   public int func_75219_a() {
      return 1;
   }

   public int func_178170_b(ItemStack stack) {
      return 1;
   }
}
