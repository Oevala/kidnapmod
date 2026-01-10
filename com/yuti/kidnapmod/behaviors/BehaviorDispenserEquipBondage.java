package com.yuti.kidnapmod.behaviors;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import java.util.List;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public abstract class BehaviorDispenserEquipBondage extends BehaviorDispenserBondageItem {
   protected ItemStack func_82487_b(IBlockSource source, ItemStack stack) {
      BlockPos blockpos = source.func_180699_d().func_177972_a((EnumFacing)source.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));
      List<EntityLivingBase> list = source.func_82618_k().func_72872_a(EntityLivingBase.class, new AxisAlignedBB(blockpos));
      if (!list.isEmpty()) {
         EntityLivingBase entity = (EntityLivingBase)list.get(0);
         if (entity != null && (entity instanceof EntityPlayer || entity instanceof I_Kidnapped)) {
            I_Kidnapped state = null;
            if (entity instanceof EntityPlayer) {
               EntityPlayer targetPlayer = (EntityPlayer)entity;
               if (!targetPlayer.func_175149_v()) {
                  state = PlayerBindState.getInstance(targetPlayer);
               }
            } else {
               state = (I_Kidnapped)entity;
            }

            if (state != null && this.canEquip((I_Kidnapped)state) && this.isValid(stack)) {
               ItemStack itemstack = stack.func_77979_a(1);
               this.equip((I_Kidnapped)state, itemstack);
               return stack;
            }
         }
      }

      return super.func_82487_b(source, stack);
   }

   protected abstract boolean isValid(ItemStack var1);

   protected abstract boolean canEquip(I_Kidnapped var1);

   protected abstract void equip(I_Kidnapped var1, ItemStack var2);
}
