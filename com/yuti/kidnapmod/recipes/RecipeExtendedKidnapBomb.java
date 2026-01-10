package com.yuti.kidnapmod.recipes;

import com.yuti.kidnapmod.items.ItemKidnapBomb;
import com.yuti.kidnapmod.tileentity.ITileEntityBondageItemHolder;
import com.yuti.kidnapmod.tileentity.TileEntityKidnapBomb;

public class RecipeExtendedKidnapBomb extends RecipeBondageItemLoaderBlock {
   public RecipeExtendedKidnapBomb() {
      super(ItemKidnapBomb.class);
   }

   public ITileEntityBondageItemHolder getEntityHolder() {
      return new TileEntityKidnapBomb(true);
   }
}
