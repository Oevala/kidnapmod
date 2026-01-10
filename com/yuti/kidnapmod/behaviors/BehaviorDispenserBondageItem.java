package com.yuti.kidnapmod.behaviors;

import com.yuti.kidnapmod.blocks.ICanBeLoaded;
import com.yuti.kidnapmod.tileentity.ITileEntityBondageItemHolder;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BehaviorDispenserBondageItem extends BehaviorDefaultDispenseItem {
   protected ItemStack func_82487_b(IBlockSource source, ItemStack stack) {
      World worldIn = source.func_82618_k();
      EnumFacing enumfacing = (EnumFacing)source.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
      BlockPos pos = source.func_180699_d().func_177972_a(enumfacing);
      IBlockState state = worldIn.func_180495_p(pos);
      if (state != null && state.func_177230_c() instanceof ICanBeLoaded) {
         ItemStack stackToDispense = Utils.extractValidStack(stack);
         if (state.func_177230_c() instanceof ICanBeLoaded) {
            TileEntity tileEntity = worldIn.func_175625_s(pos);
            if (tileEntity != null && tileEntity instanceof ITileEntityBondageItemHolder) {
               Utils.loadItemStack(worldIn, pos, (ITileEntityBondageItemHolder)tileEntity, stack, stackToDispense, (EntityPlayer)null);
               return stack;
            }
         }
      }

      return super.func_82487_b(source, stack);
   }
}
