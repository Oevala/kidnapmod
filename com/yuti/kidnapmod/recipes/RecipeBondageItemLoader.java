package com.yuti.kidnapmod.recipes;

import com.google.common.collect.Lists;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.items.ItemKidnapWearable;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public abstract class RecipeBondageItemLoader extends Impl<IRecipe> implements IRecipe {
   protected final Class<?> carrier;

   public RecipeBondageItemLoader(Class<?> carrierIn) {
      this.carrier = carrierIn;
   }

   public boolean func_77569_a(InventoryCrafting inv, World worldIn) {
      Object[] infos = this.getCraftInformation(inv);
      if (infos == null) {
         return false;
      } else if (infos.length != 2) {
         return false;
      } else if (!(infos[0] instanceof ItemStack)) {
         return false;
      } else if (!(infos[1] instanceof List)) {
         return false;
      } else {
         ItemStack stackToCheck = (ItemStack)infos[0];
         List<?> list = (List)infos[1];
         return infos != null && !stackToCheck.func_190926_b() && !list.isEmpty();
      }
   }

   protected Object[] getCraftInformation(InventoryCrafting inv) {
      Object[] infos = new Object[2];
      ItemStack stackReturned = ItemStack.field_190927_a;
      List<ItemStack> list = Lists.newArrayList();
      boolean[] statesItems = new boolean[5];

      int i;
      ItemStack currentStack;
      for(i = 0; i < inv.func_70302_i_(); ++i) {
         currentStack = inv.func_70301_a(i);
         if (!currentStack.func_190926_b() && this.carrier != null && this.carrier.isInstance(currentStack.func_77973_b())) {
            if (!stackReturned.func_190926_b()) {
               return null;
            }

            stackReturned = currentStack;
         }
      }

      if (stackReturned != null && !stackReturned.func_190926_b()) {
         for(i = 0; i < inv.func_70302_i_(); ++i) {
            currentStack = inv.func_70301_a(i);
            if (!currentStack.func_190926_b() && currentStack.func_77973_b() instanceof ItemKidnapWearable) {
               if (!stackReturned.func_190926_b() && !this.checkCurrentStackWithDataStoredInStack(currentStack, stackReturned)) {
                  return null;
               }

               if (currentStack.func_77973_b() instanceof ItemBind) {
                  if (statesItems[0]) {
                     return null;
                  }

                  statesItems[0] = true;
               }

               if (currentStack.func_77973_b() instanceof ItemGag) {
                  if (statesItems[1]) {
                     return null;
                  }

                  statesItems[1] = true;
               }

               if (currentStack.func_77973_b() instanceof ItemBlindfold) {
                  if (statesItems[2]) {
                     return null;
                  }

                  statesItems[2] = true;
               }

               if (currentStack.func_77973_b() instanceof ItemEarplugs) {
                  if (statesItems[3]) {
                     return null;
                  }

                  statesItems[3] = true;
               }

               if (currentStack.func_77973_b() instanceof ItemCollar) {
                  if (statesItems[4]) {
                     return null;
                  }

                  statesItems[4] = true;
               }

               list.add(currentStack);
            }
         }

         infos[0] = stackReturned;
         infos[1] = list;
         return infos;
      } else {
         return null;
      }
   }

   protected abstract boolean checkCurrentStackWithDataStoredInStack(ItemStack var1, ItemStack var2);

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
