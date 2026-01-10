package com.yuti.kidnapmod.recipes;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RecipeShockCollar extends Impl<IRecipe> implements IRecipe {
   private World world;

   public boolean func_77569_a(InventoryCrafting inv, World worldIn) {
      this.world = worldIn;

      for(int i = 0; i < inv.func_70302_i_(); ++i) {
         ItemStack stack = inv.func_70301_a(i);
         boolean flagStack = stack != null && !stack.func_190926_b() && stack.func_77973_b() != null;
         if (i == 4) {
            if (!flagStack || !stack.func_77973_b().equals(ModItems.CLASSIC_COLLAR)) {
               return false;
            }
         } else if (i != 1 && i != 3 && i != 5 && i != 7) {
            if (flagStack) {
               return false;
            }
         } else if (!flagStack || !stack.func_77973_b().equals(Item.func_150898_a(Blocks.field_150451_bX))) {
            return false;
         }
      }

      return true;
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      if (this.world == null) {
         return ItemStack.field_190927_a;
      } else {
         ItemStack stackCollar = ItemStack.field_190927_a;

         ItemStack stackShockCollar;
         for(int i = 0; i < inv.func_70302_i_(); ++i) {
            stackShockCollar = inv.func_70301_a(i);
            boolean flagStack = stackShockCollar != null && !stackShockCollar.func_190926_b() && stackShockCollar.func_77973_b() != null;
            if (i == 4) {
               if (!flagStack || !stackShockCollar.func_77973_b().equals(ModItems.CLASSIC_COLLAR)) {
                  return ItemStack.field_190927_a;
               }

               stackCollar = stackShockCollar;
            } else if (i != 1 && i != 3 && i != 5 && i != 7) {
               if (flagStack) {
                  return ItemStack.field_190927_a;
               }
            } else if (!flagStack || !stackShockCollar.func_77973_b().equals(Item.func_150898_a(Blocks.field_150451_bX))) {
               return ItemStack.field_190927_a;
            }
         }

         if (stackCollar != null && !stackCollar.func_190926_b()) {
            ItemCollar shockCollar = ModItems.SHOCK_COLLAR;
            stackShockCollar = new ItemStack(shockCollar);
            NBTTagCompound tagCollar = Utils.getTagComponent(stackCollar);
            if (tagCollar != null) {
               stackShockCollar.func_77982_d(tagCollar);
            }

            int resistanceCurrent = Utils.getResistance(stackCollar, this.world);
            int resistanceNew = Utils.getResistance(stackShockCollar, this.world);
            if (resistanceCurrent > resistanceNew) {
               Utils.setResistance(stackShockCollar, resistanceCurrent);
            }

            return stackShockCollar;
         } else {
            return ItemStack.field_190927_a;
         }
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
      return width * height == 9;
   }
}
