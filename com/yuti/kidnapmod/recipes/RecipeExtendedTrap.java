package com.yuti.kidnapmod.recipes;

import com.yuti.kidnapmod.items.ItemRopesTrap;
import com.yuti.kidnapmod.tileentity.ITileEntityBondageItemHolder;
import com.yuti.kidnapmod.tileentity.TileEntityTrap;

public class RecipeExtendedTrap extends RecipeBondageItemLoaderBlock {
   public RecipeExtendedTrap() {
      super(ItemRopesTrap.class);
   }

   public ITileEntityBondageItemHolder getEntityHolder() {
      return new TileEntityTrap(true);
   }
}
