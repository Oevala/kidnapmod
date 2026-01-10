package com.yuti.kidnapmod.blocks;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.util.IHasModel;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockPaddedStairs extends BlockStairs implements IHasModel {
   public BlockPaddedStairs(String name, IBlockState modelState) {
      super(modelState);
      this.func_149663_c(name);
      this.setRegistryName(name);
      ModBlocks.BLOCKS.add(this);
      ModItems.ITEMS.add((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
      this.func_149711_c(0.9F);
      this.func_149752_b(45.0F);
      this.func_149647_a(ModCreativeTabs.kidnapTab);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(Item.func_150898_a(this), 0, "inventory");
   }
}
