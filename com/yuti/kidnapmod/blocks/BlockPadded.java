package com.yuti.kidnapmod.blocks;

import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;

public class BlockPadded extends BlockBase {
   public BlockPadded(String name, Material material) {
      super(name, material);
      ModItems.ITEMS.add((new ItemBlock(this)).setRegistryName(this.getRegistryName()));
      this.func_149711_c(0.9F);
      this.func_149752_b(45.0F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149713_g(0);
      this.func_149647_a(ModCreativeTabs.kidnapTab);
   }

   public boolean func_149662_c(IBlockState state) {
      return true;
   }
}
