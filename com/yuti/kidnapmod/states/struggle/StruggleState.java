package com.yuti.kidnapmod.states.struggle;

import com.yuti.kidnapmod.items.IHasResistance;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;

public abstract class StruggleState {
   private Timer timer;

   public void struggle(PlayerBindState state) {
      if (this.canStruggle(state) && state != null && state.getPlayer() != null && state.getPlayer().field_70170_p != null) {
         EntityPlayer player = state.getPlayer();
         GameRules rules = player.field_70170_p.func_82736_K();
         if (rules.func_82765_e(this.getStruggleRule()) && !rules.func_82766_b(this.getStruggleRule())) {
            Utils.sendErrorMessageToEntity(player, this.getDisabledMessage());
            return;
         }

         IHasResistance resistanceItem = this.getResistanceItem(state);
         ItemStack resistanceStack = this.getResistanceItemStack(state);
         if (resistanceItem == null || !resistanceItem.canBeStruggledOut(resistanceStack)) {
            Utils.sendErrorMessageToEntity(player, "You can't struggle out of this!");
            return;
         }

         int probability;
         if (this.timer != null && this.timer.getSecondsRemaining() > 0) {
            probability = this.timer.getSecondsRemaining();
            Utils.sendInfoMessageToEntity(player, "You have to wait " + probability + " seconds before struggling again.");
            return;
         }

         probability = 25;
         if (rules.func_82765_e(this.getProbabilityRule())) {
            probability = rules.func_180263_c(this.getProbabilityRule());
         }

         int minDecrease = 1;
         int maxDecrease = 10;
         if (rules.func_82765_e(this.getMinDecreaseRule())) {
            minDecrease = rules.func_180263_c(this.getMinDecreaseRule());
         }

         if (rules.func_82765_e(this.getMaxDecreaseRule())) {
            maxDecrease = rules.func_180263_c(this.getMaxDecreaseRule());
         }

         if (maxDecrease < minDecrease) {
            maxDecrease = minDecrease;
         }

         Random rand = new Random();
         int struggleTime;
         if (rand.nextInt(100) <= probability) {
            struggleTime = rand.nextInt(maxDecrease - minDecrease + 1) + minDecrease;
            int newResistanceValue = this.getResistanceState(state) - struggleTime;
            this.setResistanceState(state, newResistanceValue);
            if (newResistanceValue <= 0) {
               Utils.sendInfoMessageToEntity(player, this.getSuccessMessage());
               this.successAction(state);
               this.timer = null;
               return;
            }

            Utils.sendValidMessageToEntity(player, "Your struggling is effective ! Resistance : " + newResistanceValue);
         } else {
            Utils.sendInfoMessageToEntity(player, "Your struggling is not effective...");
         }

         if (resistanceItem != null) {
            resistanceItem.notifyStruggle(player);
         }

         this.struggleNotify(state);
         struggleTime = 20;
         if (rules.func_82765_e(this.getTimerRule())) {
            struggleTime = rules.func_180263_c(this.getTimerRule());
         }

         this.timer = new Timer(struggleTime);
      }

   }

   protected abstract String getSuccessMessage();

   protected abstract String getDisabledMessage();

   protected abstract boolean canStruggle(PlayerBindState var1);

   public abstract int getResistanceState(PlayerBindState var1);

   public abstract void setResistanceState(PlayerBindState var1, int var2);

   protected abstract String getStruggleRule();

   protected abstract String getProbabilityRule();

   protected abstract String getMinDecreaseRule();

   protected abstract String getMaxDecreaseRule();

   protected abstract String getTimerRule();

   protected abstract void successAction(PlayerBindState var1);

   protected abstract void struggleNotify(PlayerBindState var1);

   public abstract void tighten(EntityPlayer var1, PlayerBindState var2);

   public abstract IHasResistance getResistanceItem(PlayerBindState var1);

   public abstract ItemStack getResistanceItemStack(PlayerBindState var1);
}
