package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class ItemDuctTapeBind extends ItemBindColored {
   public ItemDuctTapeBind(String name, ExtraBondageMaterial materialIn, int resistance, int mergePercent) {
      super(name, materialIn, resistance, mergePercent, EnumDyeColor.GRAY);
      this.countItem = 18;
   }

   public void registerModels() {
      super.registerModels();
      KidnapModMain.proxy.registerItemRenderer(this, this.getRegistryName().toString() + "_clear", 16, "inventory");
      KidnapModMain.proxy.registerItemRenderer(this, this.getRegistryName().toString() + "_caution", 17, "inventory");
   }

   public void registerVariants() {
      super.registerVariants();
      this.variants.add(ExtraBondageMaterial.TAPE_CLEAR);
      this.variants.add(ExtraBondageMaterial.TAPE_CAUTION);
   }

   public String func_77667_c(ItemStack stack) {
      String name = this.func_77658_a();
      int meta = 0;
      if (stack != null) {
         meta = stack.func_77960_j();
      }

      if (meta == 16) {
         return name + "_clear";
      } else {
         return meta == 17 ? name + "_caution" : super.func_77667_c(stack);
      }
   }
}
