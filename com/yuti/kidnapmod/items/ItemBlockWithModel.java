package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockWithModel extends ItemBlock implements IHasModel {
   public ItemBlockWithModel(Block block) {
      super(block);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }
}
