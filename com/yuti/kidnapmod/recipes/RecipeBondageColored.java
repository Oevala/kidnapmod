package com.yuti.kidnapmod.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RecipeBondageColored extends Impl<IRecipe> implements IRecipe {
   private Item itemVariant;
   private EnumDyeColor mainColor;

   public RecipeBondageColored(Item item, EnumDyeColor color) {
      this.itemVariant = item;
      this.mainColor = color;
   }

   public boolean func_77569_a(InventoryCrafting inv, World worldIn) {
      if (this.itemVariant != null && this.mainColor != null) {
         boolean itemFound = false;
         boolean dyeFound = false;
         int colorFound = -1;
         int colorDyeFound = -1;

         for(int i = 0; i < inv.func_70302_i_(); ++i) {
            ItemStack currentStack = inv.func_70301_a(i);
            if (currentStack != null && !currentStack.func_190926_b()) {
               if (itemFound && dyeFound) {
                  return false;
               }

               Item item = currentStack.func_77973_b();
               if (item.equals(this.itemVariant)) {
                  itemFound = true;
                  colorFound = currentStack.func_77960_j();
               } else {
                  if (!(item instanceof ItemDye)) {
                     return false;
                  }

                  EnumDyeColor color = EnumDyeColor.func_176766_a(currentStack.func_77960_j());
                  if (color == null) {
                     return false;
                  }

                  dyeFound = true;
                  if (color == this.mainColor) {
                     colorDyeFound = 0;
                  } else if (color == EnumDyeColor.WHITE) {
                     colorDyeFound = this.mainColor.func_176765_a();
                  }
               }
            }
         }

         return itemFound && dyeFound && colorFound != colorDyeFound;
      } else {
         return false;
      }
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      if (this.itemVariant != null && this.mainColor != null) {
         int meta = -1;
         ItemStack itemStackColored = null;

         for(int i = 0; i < inv.func_70302_i_(); ++i) {
            ItemStack currentStack = inv.func_70301_a(i);
            if (currentStack != null && !currentStack.func_190926_b()) {
               if (meta != -1 && itemStackColored != null) {
                  return ItemStack.field_190927_a;
               }

               Item item = currentStack.func_77973_b();
               if (item.equals(this.itemVariant)) {
                  itemStackColored = currentStack.func_77946_l().func_77979_a(1);
               } else {
                  if (!(item instanceof ItemDye)) {
                     return ItemStack.field_190927_a;
                  }

                  EnumDyeColor color = EnumDyeColor.func_176766_a(currentStack.func_77960_j());
                  if (color == null) {
                     return ItemStack.field_190927_a;
                  }

                  if (color == this.mainColor) {
                     meta = 0;
                  } else if (color == EnumDyeColor.WHITE) {
                     meta = this.mainColor.func_176765_a();
                  } else {
                     meta = color.func_176765_a();
                  }
               }
            }
         }

         if (meta >= 0 && itemStackColored != null && itemStackColored.func_77960_j() != meta) {
            itemStackColored.func_77964_b(meta);
            return itemStackColored.func_77946_l();
         } else {
            return ItemStack.field_190927_a;
         }
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public ItemStack func_77571_b() {
      return ItemStack.field_190927_a;
   }

   public NonNullList<ItemStack> func_179532_b(InventoryCrafting inv) {
      NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(inv.func_70302_i_(), ItemStack.field_190927_a);

      for(int i = 0; i < nonnulllist.size(); ++i) {
         ItemStack itemstack = inv.func_70301_a(i);
         nonnulllist.set(i, ForgeHooks.getContainerItem(itemstack));
      }

      return nonnulllist;
   }

   public boolean func_192399_d() {
      return true;
   }

   public boolean func_194133_a(int width, int height) {
      return width * height >= 2;
   }
}
