package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.loaders.ItemTask;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemShockCollarAuto extends ItemShockCollar {
   private int interval;

   public ItemShockCollarAuto(String name, ExtraBondageMaterial materialIn, int baseResistanceIn, int intervalIn, int mergePercentIn) {
      super(name, materialIn, baseResistanceIn, mergePercentIn);
      this.interval = intervalIn;
      this.func_77637_a((CreativeTabs)null);
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
      if (this.isLocked(stack)) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         int amount;
         if (nbt.func_74764_b("owner-x") && nbt.func_74764_b("owner-y") && nbt.func_74764_b("owner-z")) {
            int x = nbt.func_74762_e("owner-x");
            amount = nbt.func_74762_e("owner-y");
            int z = nbt.func_74762_e("owner-z");
            tooltip.add(ChatFormatting.LIGHT_PURPLE + "Location: : X : " + x + ", Y : " + amount + ", Z : " + z);
         }

         ItemStack job = this.getJobItemStack(stack);
         if (job != null) {
            amount = job.func_190916_E();
            String name = job.func_82833_r();
            tooltip.add(ChatFormatting.DARK_RED + "Job: : " + ChatFormatting.BLUE + "[" + amount + " x " + name + "]");
         }
      }

   }

   public ItemStack setJob(ItemStack stack, ItemTask job, UUID jobUUID, EntityKidnapper kidnapper) {
      if (job != null && jobUUID != null && kidnapper != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         NBTTagCompound jobTag = new NBTTagCompound();
         nbt.func_74782_a("job", job.getItemStack().func_77946_l().func_77955_b(jobTag));
         nbt.func_74768_a("owner-x", (int)kidnapper.field_70165_t);
         nbt.func_74768_a("owner-y", (int)kidnapper.field_70163_u);
         nbt.func_74768_a("owner-z", (int)kidnapper.field_70161_v);
         stack.func_77982_d(nbt);
         this.addOwner(stack, jobUUID, "Kidnapper");
      }

      return stack;
   }

   public boolean doesStackCompleteJob(ItemStack collarStack, ItemStack jobStack) {
      if (collarStack != null && jobStack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(collarStack);
         if (nbt.func_74764_b("job")) {
            ItemStack stack = new ItemStack((NBTTagCompound)nbt.func_74781_a("job"));
            if (stack != null) {
               return ItemTask.doesStackCompleteTask(jobStack, stack);
            }
         }
      }

      return false;
   }

   public ItemStack getJobItemStack(ItemStack collarStack) {
      if (collarStack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(collarStack);
         if (nbt.func_74764_b("job")) {
            ItemStack stack = new ItemStack((NBTTagCompound)nbt.func_74781_a("job"));
            return stack;
         }
      }

      return null;
   }

   public int getInterval() {
      return this.interval;
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase living) {
      if (living instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)living;
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null) {
            state.resetAutoShockTimer();
         }
      }

      super.onUnequipped(itemstack, living);
   }

   public boolean func_77636_d(ItemStack stack) {
      return false;
   }

   public boolean canBeStruggledOut(ItemStack stack) {
      return false;
   }
}
