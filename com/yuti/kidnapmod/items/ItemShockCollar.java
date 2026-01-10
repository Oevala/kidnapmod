package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class ItemShockCollar extends ItemCollar {
   public ItemShockCollar(String name, ExtraBondageMaterial materialIn, int baseResistanceIn, int mergePercentIn) {
      super(name, materialIn, baseResistanceIn, mergePercentIn);
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
      tooltip.add(ChatFormatting.YELLOW + "Shocker");
      if (this.isPublic(stack)) {
         tooltip.add(ChatFormatting.GREEN + "Public");
      }

   }

   public void notifyStruggle(EntityPlayer player) {
      if (player != null && player.field_70170_p != null) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && !state.isTiedUp() && state.hasLockedCollar()) {
            GameRules rules = player.field_70170_p.func_82736_K();
            int shockProba = 20;
            if (rules.func_82765_e("struggle_collar_random_shock")) {
               shockProba = rules.func_180263_c("struggle_collar_random_shock");
            }

            if (Item.field_77697_d.nextInt(100) < shockProba) {
               state.shockKidnapped();
            }
         }
      }

   }

   public ItemStack enablePublicMode(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("public_mode", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack disablePublicMode(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("public_mode", false);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isPublic(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("public_mode") ? nbt.func_74767_n("public_mode") : false;
   }
}
