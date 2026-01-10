package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIInstantRestraining extends EntityAIBase {
   private EntityKidnapper kidnapper;

   public EntityAIInstantRestraining(EntityKidnapper kidnapper) {
      this.kidnapper = kidnapper;
   }

   public boolean func_75250_a() {
      if (!this.kidnapper.isTiedUp() && !this.kidnapper.isBlindfolded()) {
         if (this.kidnapper.hasCollar() && !this.kidnapper.isSlaveKidnappingModeEnabled()) {
            return false;
         } else if (this.kidnapper.isChloroformed()) {
            return false;
         } else if (this.kidnapper.isGetOutState()) {
            return false;
         } else if (!this.kidnapper.func_70089_S()) {
            return false;
         } else if (this.kidnapper.func_70090_H()) {
            return false;
         } else if (this.kidnapper.hasSlaves()) {
            return false;
         } else if (!this.kidnapper.isCloseToTarget()) {
            return false;
         } else if (this.kidnapper.getTarget() == null) {
            return false;
         } else if (!this.kidnapper.isSuitableTarget(this.kidnapper.getTarget())) {
            return false;
         } else {
            PlayerBindState state = PlayerBindState.getInstance(this.kidnapper.getTarget());
            if (state.isBoundAndGagged()) {
               return false;
            } else {
               return !this.kidnapper.isWaitingForJobToBeCompleted();
            }
         }
      } else {
         return false;
      }
   }

   public void func_75251_c() {
      if (!this.kidnapper.isCloseToTarget()) {
         this.kidnapper.setTarget((EntityPlayer)null);
      }

   }

   public boolean func_75253_b() {
      return !this.func_75250_a() ? false : super.func_75253_b();
   }

   public void func_75246_d() {
      if (this.kidnapper.getTarget() != null) {
         PlayerBindState state = PlayerBindState.getInstance(this.kidnapper.getTarget());
         if (state != null && !state.isBoundAndGagged()) {
            if (state.isTiedUp() && !state.isGagged()) {
               state.putGagOn(this.kidnapper.getGagItem());
               this.kidnapper.actionTo(this.kidnapper.getTarget(), "gagged you!");
               return;
            }

            if (state.isGagged()) {
               state.putBindOn(this.kidnapper.getBindItem());
            } else {
               state.restrain(this.kidnapper.getBindItem(), this.kidnapper.getGagItem());
            }

            this.kidnapper.actionTo(this.kidnapper.getTarget(), "tied you up, you can't move anymore!");
         }
      }

   }

   public void func_75249_e() {
      this.kidnapper.clearOldLeashedEntities();
      this.kidnapper.talkTo(this.kidnapper.getTarget(), "Stop struggling!");
      this.kidnapper.actionTo(this.kidnapper.getTarget(), "is tying you up!");
      if (this.kidnapper.getTarget() != null) {
         super.func_75249_e();
      }

   }
}
