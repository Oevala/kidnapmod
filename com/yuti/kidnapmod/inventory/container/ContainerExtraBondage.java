package com.yuti.kidnapmod.inventory.container;

import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItemCapabilities;
import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ContainerExtraBondage extends Container {
   public final InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
   public final InventoryCraftResult craftResult = new InventoryCraftResult();
   public IExtraBondageItemHandler extraBondageItems;
   public boolean isLocalWorld;
   private final EntityPlayer thePlayer;
   private static final EntityEquipmentSlot[] equipmentSlots;

   public ContainerExtraBondage(InventoryPlayer playerInv, boolean par2, final EntityPlayer player) {
      this.isLocalWorld = par2;
      this.thePlayer = player;
      this.extraBondageItems = (IExtraBondageItemHandler)player.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_BONDAGE_ITEMS, (EnumFacing)null);
      this.func_75146_a(new SlotCrafting(playerInv.field_70458_d, this.craftMatrix, this.craftResult, 0, 154, 28));

      int i;
      int j;
      for(i = 0; i < 2; ++i) {
         for(j = 0; j < 2; ++j) {
            this.func_75146_a(new Slot(this.craftMatrix, j + i * 2, 116 + j * 18, 18 + i * 18));
         }
      }

      for(i = 0; i < 4; ++i) {
         final EntityEquipmentSlot slot = equipmentSlots[i];
         this.func_75146_a(new Slot(playerInv, 36 + (3 - i), 8, 8 + i * 18) {
            public int func_75219_a() {
               return 1;
            }

            public boolean func_75214_a(ItemStack stack) {
               return stack.func_77973_b().isValidArmor(stack, slot, player);
            }

            public boolean func_82869_a(EntityPlayer playerIn) {
               ItemStack itemstack = this.func_75211_c();
               return !itemstack.func_190926_b() && !playerIn.func_184812_l_() && EnchantmentHelper.func_190938_b(itemstack) ? false : super.func_82869_a(playerIn);
            }

            public String func_178171_c() {
               return ItemArmor.field_94603_a[slot.func_188454_b()];
            }
         });
      }

      this.func_75146_a(new SlotExtraBondage(player, this.extraBondageItems, 0, 77, 8));
      this.func_75146_a(new SlotExtraBondage(player, this.extraBondageItems, 1, 96, 8));
      this.func_75146_a(new SlotExtraBondage(player, this.extraBondageItems, 2, 77, 26));
      this.func_75146_a(new SlotExtraBondage(player, this.extraBondageItems, 3, 96, 26));
      this.func_75146_a(new SlotExtraBondage(player, this.extraBondageItems, 4, 77, 44));
      this.func_75146_a(new SlotExtraBondage(player, this.extraBondageItems, 5, 96, 44));

      for(i = 0; i < 3; ++i) {
         for(j = 0; j < 9; ++j) {
            this.func_75146_a(new Slot(playerInv, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
         }
      }

      for(i = 0; i < 9; ++i) {
         this.func_75146_a(new Slot(playerInv, i, 8 + i * 18, 142));
      }

      this.func_75146_a(new Slot(playerInv, 40, 96, 62) {
         public boolean func_75214_a(ItemStack stack) {
            return super.func_75214_a(stack);
         }

         public String func_178171_c() {
            return "minecraft:items/empty_armor_slot_shield";
         }
      });
      this.func_75130_a(this.craftMatrix);
   }

   public void func_75130_a(IInventory par1IInventory) {
      this.func_192389_a(this.thePlayer.func_130014_f_(), this.thePlayer, this.craftMatrix, this.craftResult);
   }

   public void func_75134_a(EntityPlayer player) {
      super.func_75134_a(player);
      this.craftResult.func_174888_l();
      if (!player.field_70170_p.field_72995_K) {
         this.func_193327_a(player, player.field_70170_p, this.craftMatrix);
      }

   }

   public boolean func_75145_c(EntityPlayer par1EntityPlayer) {
      return true;
   }

   public ItemStack func_82846_b(EntityPlayer playerIn, int index) {
      ItemStack itemstack = ItemStack.field_190927_a;
      Slot slot = (Slot)this.field_75151_b.get(index);
      if (slot != null && slot.func_75216_d()) {
         ItemStack itemstack1 = slot.func_75211_c();
         itemstack = itemstack1.func_77946_l();
         EntityEquipmentSlot entityequipmentslot = EntityLiving.func_184640_d(itemstack);
         int slotShift = this.extraBondageItems.getSlots();
         if (index == 0) {
            if (!this.func_75135_a(itemstack1, 9 + slotShift, 45 + slotShift, true)) {
               return ItemStack.field_190927_a;
            }

            slot.func_75220_a(itemstack1, itemstack);
         } else if (index >= 1 && index < 5) {
            if (!this.func_75135_a(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (index >= 5 && index < 9) {
            if (!this.func_75135_a(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (index >= 9 && index < 9 + slotShift) {
            if (!this.func_75135_a(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (entityequipmentslot.func_188453_a() == Type.ARMOR && !((Slot)this.field_75151_b.get(8 - entityequipmentslot.func_188454_b())).func_75216_d()) {
            int i = 8 - entityequipmentslot.func_188454_b();
            if (!this.func_75135_a(itemstack1, i, i + 1, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (entityequipmentslot == EntityEquipmentSlot.OFFHAND && !((Slot)this.field_75151_b.get(45 + slotShift)).func_75216_d()) {
            if (!this.func_75135_a(itemstack1, 45 + slotShift, 46 + slotShift, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (itemstack.hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)) {
            IExtraBondageItem bondageItem = (IExtraBondageItem)itemstack.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null);
            int[] var9 = bondageItem.getType(itemstack).getValidSlots();
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               int extraBondageSlot = var9[var11];
               if (bondageItem.canEquip(itemstack1, this.thePlayer) && !((Slot)this.field_75151_b.get(extraBondageSlot + 9)).func_75216_d() && !this.func_75135_a(itemstack1, extraBondageSlot + 9, extraBondageSlot + 10, false)) {
                  return ItemStack.field_190927_a;
               }

               if (itemstack1.func_190916_E() == 0) {
                  break;
               }
            }
         } else if (index >= 9 + slotShift && index < 36 + slotShift) {
            if (!this.func_75135_a(itemstack1, 36 + slotShift, 45 + slotShift, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (index >= 36 + slotShift && index < 45 + slotShift) {
            if (!this.func_75135_a(itemstack1, 9 + slotShift, 36 + slotShift, false)) {
               return ItemStack.field_190927_a;
            }
         } else if (!this.func_75135_a(itemstack1, 9 + slotShift, 45 + slotShift, false)) {
            return ItemStack.field_190927_a;
         }

         if (itemstack1.func_190926_b()) {
            slot.func_75215_d(ItemStack.field_190927_a);
         } else {
            slot.func_75218_e();
         }

         if (itemstack1.func_190916_E() == itemstack.func_190916_E()) {
            return ItemStack.field_190927_a;
         }

         if (itemstack1.func_190926_b() && !this.extraBondageItems.isEventBlocked() && slot instanceof SlotExtraBondage && itemstack.hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)) {
            ((IExtraBondageItem)itemstack.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)).onUnequipped(itemstack, playerIn);
         }

         ItemStack itemstack2 = slot.func_190901_a(playerIn, itemstack1);
         if (index == 0) {
            playerIn.func_71019_a(itemstack2, false);
         }
      }

      return itemstack;
   }

   public boolean func_94530_a(ItemStack stack, Slot slot) {
      return slot.field_75224_c != this.craftResult && super.func_94530_a(stack, slot);
   }

   static {
      equipmentSlots = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
   }
}
