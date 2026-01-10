package com.yuti.kidnapmod.states.struggle;

import com.yuti.kidnapmod.items.IHasResistance;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class StruggleCollar extends StruggleState {
   protected String getSuccessMessage() {
      return "You succesfully unlocked your collar !";
   }

   protected String getDisabledMessage() {
      return "Struggle system for collars is disbaled in this world.";
   }

   protected boolean canStruggle(PlayerBindState state) {
      return state != null && !state.isTiedUp() && state.hasLockedCollar();
   }

   public int getResistanceState(PlayerBindState state) {
      return state == null ? 0 : state.getCurrentCollarResistance();
   }

   protected String getStruggleRule() {
      return "struggle_collar";
   }

   protected String getProbabilityRule() {
      return "probability_struggle_collar";
   }

   protected String getMinDecreaseRule() {
      return "struggle_min_decrease_collar";
   }

   protected String getMaxDecreaseRule() {
      return "struggle_max_decrease_collar";
   }

   protected String getTimerRule() {
      return "struggle_timer_collar";
   }

   protected void successAction(PlayerBindState state) {
      if (state != null && state.getPlayer() != null) {
         ItemStack collarStack = state.getCurrentCollar();
         if (collarStack != null && collarStack.func_77973_b() instanceof ItemCollar) {
            ItemCollar collar = (ItemCollar)collarStack.func_77973_b();
            if (collar != null) {
               Utils.playUnlockSound(state.getPlayer());
               collar.unlockCollar(collarStack);
            }
         }
      }

   }

   protected void struggleNotify(PlayerBindState state) {
   }

   public void tighten(EntityPlayer tightener, PlayerBindState state) {
      if (tightener != null && state != null) {
         ItemStack currentCollar = state.getCurrentCollar();
         if (currentCollar != null && currentCollar.func_77973_b() instanceof ItemCollar) {
            ItemCollar collar = (ItemCollar)currentCollar.func_77973_b();
            if (state.hasLockedCollar() && (state.isTiedUp() || collar.isOwner(currentCollar, tightener))) {
               EntityPlayer player = state.getPlayer();
               if (player != null && player.field_70170_p != null && !player.equals(tightener)) {
                  int currentResistance = this.getResistanceState(state);
                  int newResistance = Utils.getResistance(currentCollar, player.field_70170_p);
                  if (newResistance != currentResistance) {
                     Utils.playLockSound(player);
                     this.setResistanceState(state, newResistance);
                     int resistance = state.getCurrentCollarResistance();
                     Utils.sendInfoMessageToEntity(player, "Someone tightened your collar! Resistance : " + resistance);
                     Utils.sendValidMessageToEntity(tightener, "You tightened collar of " + player.func_70005_c_());
                  }
               }
            }
         }
      }

   }

   public IHasResistance getResistanceItem(PlayerBindState state) {
      if (state != null) {
         ItemStack currentCollar = state.getCurrentCollar();
         if (currentCollar != null && currentCollar.func_77973_b() instanceof IHasResistance) {
            return (IHasResistance)currentCollar.func_77973_b();
         }
      }

      return null;
   }

   public ItemStack getResistanceItemStack(PlayerBindState state) {
      if (state != null) {
         ItemStack currentCollar = state.getCurrentCollar();
         if (currentCollar != null && currentCollar.func_77973_b() instanceof IHasResistance) {
            return currentCollar;
         }
      }

      return null;
   }

   public void setResistanceState(PlayerBindState state, int resistance) {
      if (state != null) {
         state.setCurrentCollarResistance(resistance);
      }

   }
}
