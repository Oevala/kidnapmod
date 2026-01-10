package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.tileentity.TileEntityKidnapBomb;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemKidnapBomb extends ItemBlock {
   public ItemKidnapBomb(Block block) {
      super(block);
   }

   public boolean func_77636_d(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      TileEntityKidnapBomb tileTrap = new TileEntityKidnapBomb(true);
      if (nbt != null && nbt.func_74764_b("BlockEntityTag")) {
         tileTrap.func_145839_a(nbt.func_74775_l("BlockEntityTag"));
         ItemStack bind = tileTrap.getBind();
         ItemStack gag = tileTrap.getGag();
         ItemStack blindfold = tileTrap.getBlindfold();
         ItemStack earplugs = tileTrap.getEarplugs();
         ItemStack collar = tileTrap.getCollar();
         ItemStack clothes = tileTrap.getClothes();
         return bind != null || gag != null || blindfold != null || earplugs != null || collar != null || clothes != null || super.func_77636_d(stack);
      } else {
         return super.func_77636_d(stack);
      }
   }
}
