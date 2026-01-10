package com.yuti.kidnapmod.behaviors;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorDispenserPlaceBlock extends BehaviorDefaultDispenseItem {
   private Block createdBlock;

   public BehaviorDispenserPlaceBlock(Block block) {
      this.createdBlock = block;
   }

   protected ItemStack func_82487_b(IBlockSource source, ItemStack stack) {
      World worldIn = source.func_82618_k();
      EnumFacing enumfacing = (EnumFacing)source.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
      BlockPos pos = source.func_180699_d().func_177972_a(enumfacing);
      if (worldIn.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a) {
         stack.func_190918_g(1);
         worldIn.func_175656_a(source.func_180699_d().func_177972_a(enumfacing), this.createdBlock.func_176223_P());
         ItemBlock.func_179224_a(worldIn, (EntityPlayer)null, pos, stack);
         return stack;
      } else {
         return super.func_82487_b(source, stack);
      }
   }
}
