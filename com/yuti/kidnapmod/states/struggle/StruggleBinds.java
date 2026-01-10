package com.yuti.kidnapmod.states.struggle;

import com.yuti.kidnapmod.items.IHasResistance;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapper;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class StruggleBinds extends StruggleState {
   protected String getSuccessMessage() {
      return "You succesfully untied yourself !";
   }

   protected String getDisabledMessage() {
      return "Struggle system is disbaled in this world.";
   }

   public int getResistanceState(PlayerBindState state) {
      return state == null ? 0 : state.getCurrentBindResistance();
   }

   protected String getStruggleRule() {
      return "struggle";
   }

   protected String getProbabilityRule() {
      return "probability_struggle";
   }

   protected String getMinDecreaseRule() {
      return "struggle_min_decrease";
   }

   protected String getMaxDecreaseRule() {
      return "struggle_max_decrease";
   }

   protected String getTimerRule() {
      return "struggle_timer";
   }

   protected boolean canStruggle(PlayerBindState state) {
      return state != null && state.isTiedUp();
   }

   protected void successAction(PlayerBindState state) {
      if (state != null) {
         if (state.getPlayer() != null) {
            state.dropBondageItems();
         }

         state.untie();
         state.free();
      }

   }

   protected void struggleNotify(PlayerBindState state) {
      if (state != null) {
         I_Kidnapper master = state.getMaster();
         if (master != null && state.isSlave() && state.isMasterClose()) {
            master.onSlaveStruggle(state);
         }
      }

   }

   public void tighten(EntityPlayer tightener, PlayerBindState state) {
      if (tightener != null && state != null && state.isTiedUp()) {
         ItemStack currentBind = state.getCurrentBind();
         if (currentBind != null && currentBind.func_77973_b() instanceof ItemBind) {
            EntityPlayer player = state.getPlayer();
            if (player != null && player.field_70170_p != null) {
               this.setResistanceState(state, Utils.getResistance(currentBind, player.field_70170_p));
               int resistance = state.getCurrentBindResistance();
               Utils.sendInfoMessageToEntity(player, "Someone tightened your binds! Resistance : " + resistance);
               Utils.sendValidMessageToEntity(tightener, "You tightened binds of " + player.func_70005_c_());
            }
         }
      }

   }

   public IHasResistance getResistanceItem(PlayerBindState state) {
      if (state != null) {
         ItemStack currentBind = state.getCurrentBind();
         if (currentBind != null && currentBind.func_77973_b() instanceof IHasResistance) {
            return (IHasResistance)currentBind.func_77973_b();
         }
      }

      return null;
   }

   public ItemStack getResistanceItemStack(PlayerBindState state) {
      if (state != null) {
         ItemStack currentBind = state.getCurrentBind();
         if (currentBind != null && currentBind.func_77973_b() instanceof IHasResistance) {
            return currentBind;
         }
      }

      return null;
   }

   public void setResistanceState(PlayerBindState state, int resistance) {
      if (state != null) {
         state.setCurrentBindResistance(resistance);
      }

   }
}
