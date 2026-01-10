package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemKeyCollar extends ItemOnwerTarget implements IHasModel, ItemUsuableOnRestrainedPlayer {
   public ItemKeyCollar(String name) {
      super(name);
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
      if (stack.func_77942_o()) {
         NBTTagCompound nbt = stack.func_77978_p();
         if (nbt.func_74764_b("ownerName")) {
            tooltip.add(ChatFormatting.GOLD + "Owner : " + nbt.func_74779_i("ownerName"));
         } else {
            tooltip.add(ChatFormatting.GRAY + "Need to be claimed (use /collarkey claim)");
         }

         if (nbt.func_74764_b("targetName")) {
            tooltip.add(ChatFormatting.BLUE + "Collar : " + nbt.func_74779_i("targetName"));
         }
      } else {
         tooltip.add(ChatFormatting.GRAY + "Need to be claimed (use /collarkey claim)");
      }

   }

   public boolean canUseKey(ItemStack keyStack, ItemStack collarStack, EntityLivingBase holder) {
      if (keyStack != null && keyStack.func_77973_b() instanceof ItemKeyCollar && collarStack != null && collarStack.func_77973_b() instanceof ItemCollar && holder != null) {
         UUID ownerId = this.getOwnerId(keyStack);
         UUID targetId = this.getTargetId(keyStack);
         UUID playerId = holder.func_110124_au();
         ItemCollar collar = (ItemCollar)collarStack.func_77973_b();
         if (playerId != null && targetId != null && ownerId != null) {
            return collar.isOwner(collarStack, ownerId) && targetId.equals(playerId);
         }
      }

      return false;
   }

   public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (!world.field_72995_K && player != null) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && !state.isTiedUp() && state.hasCollar()) {
            ItemStack key = player.func_184586_b(hand);
            ItemStack collarStack = state.getCurrentCollar();
            if (key.func_77973_b() instanceof ItemKeyCollar) {
               if (!this.hasOwner(key)) {
                  this.setOwner(key, player);
               }

               if (!this.hasTarget(key)) {
                  this.setTarget(key, player);
               }

               if (this.canUseKey(key, collarStack, player)) {
                  if (collarStack != null && collarStack.func_77973_b() instanceof ItemCollar) {
                     ItemCollar collar = (ItemCollar)collarStack.func_77973_b();
                     if (collar.isLocked(collarStack)) {
                        Utils.playUnlockSound(player);
                        collar.unlockCollar(collarStack);
                        Utils.sendValidMessageToEntity(player, "Collar unlocked!");
                     } else {
                        Utils.playLockSound(player);
                        collar.lockCollar(collarStack);
                        Utils.sendValidMessageToEntity(player, "Collar locked!");
                     }
                  }
               } else {
                  Utils.sendErrorMessageToEntity(player, "You can't use this key on this collar");
               }
            }
         }
      }

      return super.func_77659_a(world, player, hand);
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      if (!player.field_70170_p.field_72995_K && (target instanceof EntityPlayer || target instanceof I_Kidnapped)) {
         PlayerBindState playerState = PlayerBindState.getInstance(player);
         I_Kidnapped targetState = null;
         if (target instanceof EntityPlayer) {
            EntityPlayer targetPlayer = (EntityPlayer)target;
            targetState = PlayerBindState.getInstance(targetPlayer);
         } else {
            targetState = (I_Kidnapped)target;
         }

         if (targetState != null && ((I_Kidnapped)targetState).hasCollar() && playerState != null && !playerState.isTiedUp()) {
            ItemStack collarStack = ((I_Kidnapped)targetState).getCurrentCollar();
            ItemStack key = player.func_184586_b(hand);
            if (key != null && key.func_77973_b() instanceof ItemKeyCollar) {
               if (!this.hasOwner(key)) {
                  this.setOwner(key, player);
               }

               if (!this.hasTarget(key)) {
                  this.setTarget(key, target);
               }

               if (this.canUseKey(key, collarStack, target)) {
                  if (collarStack != null && collarStack.func_77973_b() instanceof ItemCollar) {
                     ItemCollar collar = (ItemCollar)collarStack.func_77973_b();
                     if (collar.isLocked(collarStack)) {
                        Utils.playUnlockSound(player);
                        collar.unlockCollar(collarStack);
                        Utils.sendValidMessageToEntity(player, "Collar unlocked!");
                        Utils.sendValidMessageToEntity(target, "Someone unlocked your collar!");
                     } else {
                        Utils.playLockSound(player);
                        collar.lockCollar(collarStack);
                        Utils.sendValidMessageToEntity(player, "Collar locked!");
                        Utils.sendValidMessageToEntity(target, "Someone locked your collar!");
                     }
                  }
               } else {
                  Utils.sendErrorMessageToEntity(player, "You can't use this key on this collar");
               }
            }
         }
      }

      return true;
   }
}
