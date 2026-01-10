package com.yuti.kidnapmod.blocks;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockBase extends Block implements IHasModel {
   public BlockBase(String name, Material material) {
      super(material);
      this.func_149663_c(name);
      this.setRegistryName(name);
      ModBlocks.BLOCKS.add(this);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(Item.func_150898_a(this), 0, "inventory");
   }
}
