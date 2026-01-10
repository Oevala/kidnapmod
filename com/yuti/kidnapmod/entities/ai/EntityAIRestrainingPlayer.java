package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.items.tasks.PlayerStateTask;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketRestrainState;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.time.Timer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class EntityAIRestrainingPlayer extends EntityAIBase {
   protected Timer timer;
   protected int TIME_TO_WAIT;
   protected EntityKidnapper kidnapper;
   protected boolean shouldSendPackets;

   public EntityAIRestrainingPlayer(EntityKidnapper kidnapper, boolean shouldSendPackets) {
      this.kidnapper = kidnapper;
      this.shouldSendPackets = shouldSendPackets;
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

      this.timer = null;
   }

   public boolean func_75253_b() {
      return !this.func_75250_a() ? false : super.func_75253_b();
   }

   public void func_75246_d() {
      if (this.kidnapper.getTarget() != null && this.timer != null) {
         PlayerBindState state = PlayerBindState.getInstance(this.kidnapper.getTarget());
         if (state != null && !state.isBoundAndGagged()) {
            if (state.isTiedUp() && !state.isGagged()) {
               state.putGagOn(this.kidnapper.getGagItem());
               this.timer = null;
               this.kidnapper.actionTo(this.kidnapper.getTarget(), "gagged you!");
               return;
            }

            int secondsRemaining = this.timer.getSecondsRemaining();
            PlayerStateTask restrainState = state.getRestrainedState();
            if (this.shouldSendPackets && state != null && restrainState != null) {
               int newState = this.TIME_TO_WAIT - secondsRemaining;
               restrainState.update(newState);
               PacketHandler.INSTANCE.sendTo(new PacketRestrainState(newState, this.TIME_TO_WAIT), (EntityPlayerMP)this.kidnapper.getTarget());
            }

            if (secondsRemaining <= 0) {
               this.timer = null;
               if (this.shouldSendPackets && restrainState != null) {
                  PacketHandler.INSTANCE.sendTo(new PacketRestrainState(-1, this.TIME_TO_WAIT), (EntityPlayerMP)this.kidnapper.getTarget());
               }

               if (state.isGagged()) {
                  state.putBindOn(this.kidnapper.getBindItem());
               } else {
                  state.restrain(this.kidnapper.getBindItem(), this.kidnapper.getGagItem());
               }

               this.kidnapper.actionTo(this.kidnapper.getTarget(), "tied you up, you can't move anymore!");
               if (this.kidnapper.isSlaveKidnappingModeEnabled() && this.kidnapper.isTiedUpInPrison(this.kidnapper.getTarget())) {
                  state.tieToClosestPole();
               }
            }
         }
      }

   }

   public void func_75249_e() {
      this.kidnapper.clearOldLeashedEntities();
   }
}
