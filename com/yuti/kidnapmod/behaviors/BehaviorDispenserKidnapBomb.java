package com.yuti.kidnapmod.behaviors;

import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.tileentity.TileEntityKidnapBomb;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BehaviorDispenserKidnapBomb extends BehaviorDefaultDispenseItem {
   protected ItemStack func_82487_b(IBlockSource source, ItemStack stack) {
      World worldIn = source.func_82618_k();
      EnumFacing enumfacing = (EnumFacing)source.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
      BlockPos pos = source.func_180699_d().func_177972_a(enumfacing);
      IBlockState state = worldIn.func_180495_p(pos);
      if (state != null && state.func_177230_c() == Blocks.field_150350_a) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         TileEntityKidnapBomb tileBomb = new TileEntityKidnapBomb(true);
         if (nbt != null && nbt.func_74764_b("BlockEntityTag")) {
            tileBomb.func_145839_a(nbt.func_74775_l("BlockEntityTag"));
         }

         ModBlocks.KIDNAP_BOMB.explodeSpawnEntity(worldIn, pos, tileBomb, (EntityLivingBase)null);
         stack.func_190918_g(1);
         return stack;
      } else {
         return super.func_82487_b(source, stack);
      }
   }
}
