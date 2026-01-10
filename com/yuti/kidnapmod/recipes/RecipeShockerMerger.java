package com.yuti.kidnapmod.recipes;

import com.google.common.collect.Lists;
import com.yuti.kidnapmod.items.ItemShockerController;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class RecipeShockerMerger extends Impl<IRecipe> implements IRecipe {
   private World world;

   @Nullable
   protected List<ItemStack> getCraftInformation(InventoryCrafting inv) {
      List<ItemStack> shockersList = Lists.newArrayList();

      for(int i = 0; i < inv.func_70302_i_(); ++i) {
         ItemStack currentStack = inv.func_70301_a(i);
         if (currentStack != null && !currentStack.func_190926_b() && currentStack.func_77973_b() instanceof ItemShockerController) {
            shockersList.add(currentStack);
         } else if (!currentStack.func_190926_b()) {
            return null;
         }
      }

      return shockersList;
   }

   public boolean func_77569_a(InventoryCrafting inv, World worldIn) {
      this.world = worldIn;
      List<ItemStack> shockersList = this.getCraftInformation(inv);
      return shockersList != null && !shockersList.isEmpty() && shockersList.size() > 1;
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      if (this.world == null) {
         return ItemStack.field_190927_a;
      } else {
         List<ItemStack> shockersList = this.getCraftInformation(inv);
         return shockersList != null && !shockersList.isEmpty() && shockersList.size() > 1 ? ItemShockerController.mergeShockers(shockersList, this.world) : ItemStack.field_190927_a;
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
