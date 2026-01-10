package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.init.ExtraRecipes;
import com.yuti.kidnapmod.recipes.RecipeBondageColored;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemGagColored extends ItemGag implements IHasColorVariants {
   private EnumDyeColor mainColor;
   protected int countItem = 16;

   public ItemGagColored(String name, ExtraBondageMaterial materialIn, EnumDyeColor mainColorIn) {
      super(name, materialIn);
      this.mainColor = mainColorIn;
      this.func_77627_a(true);
      ExtraRecipes.registerRecipe("recipe_bondage_colored_" + name, new RecipeBondageColored(this, this.getMaincolor()));
   }

   public EnumDyeColor getMaincolor() {
      return this.mainColor;
   }

   public void registerModels() {
      KidnapModMain.proxy.registerColoredItemRenderer(this, this.mainColor, "inventory");
   }

   public void registerVariants() {
      this.variants = ExtraBondageMaterial.registerVariants(this.getMaincolor(), this.getExtraBondageMaterial());
   }

   public void func_150895_a(CreativeTabs tab, NonNullList<ItemStack> items) {
      if (this.func_194125_a(tab)) {
         for(int i = 0; i < this.countItem; ++i) {
            items.add(new ItemStack(this, 1, i));
         }
      }

   }

   public String func_77667_c(ItemStack stack) {
      String name = this.func_77658_a();
      int meta = 0;
      if (stack != null) {
         meta = stack.func_77960_j();
      }

      return meta >= 0 && meta < 16 ? Utils.getUnlocalizedColorName(this, this.getMaincolor(), meta) : name;
   }

   public int getItemsCount() {
      return this.countItem;
   }
}
