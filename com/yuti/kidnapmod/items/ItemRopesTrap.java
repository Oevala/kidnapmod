package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.items.tasks.TimedTask;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketPlaceTrap;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.tileentity.TileEntityTrap;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.UtilsParameters;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRopesTrap extends ItemBlock {
   public ItemRopesTrap(Block block) {
      super(block);
   }

   public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
      if (world.field_72995_K) {
         return EnumActionResult.FAIL;
      } else {
         if (!world.func_180495_p(pos).equals(ModBlocks.ROPES_TRAP.func_176223_P())) {
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               TimedTask trapTask = state.getTrapPlaceTask();
               if (trapTask == null || trapTask.isOutdated()) {
                  trapTask = new TimedTask(UtilsParameters.getTrapPlaceDelay(world));
                  trapTask.start();
                  state.setTrapPlaceTask(trapTask);
               }

               trapTask.update();
               if (trapTask.isOver()) {
                  state.setTrapPlaceTask((TimedTask)null);
                  PacketHandler.INSTANCE.sendTo(new PacketPlaceTrap(-1, UtilsParameters.getTrapPlaceDelay(world)), (EntityPlayerMP)player);
                  return EnumActionResult.PASS;
               }

               PacketHandler.INSTANCE.sendTo(new PacketPlaceTrap(trapTask.getState(), UtilsParameters.getTrapPlaceDelay(world)), (EntityPlayerMP)player);
            }
         }

         return EnumActionResult.FAIL;
      }
   }

   public boolean func_77636_d(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      TileEntityTrap tileTrap = new TileEntityTrap(true);
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
