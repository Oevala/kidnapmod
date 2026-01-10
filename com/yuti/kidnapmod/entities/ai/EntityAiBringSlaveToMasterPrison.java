package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.teleport.Position;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAiBringSlaveToMasterPrison extends EntityAIWanderFastTimed {
   private int searchRadiusAfter;

   public EntityAiBringSlaveToMasterPrison(EntityKidnapper entity, double speed, int time, int searchRadiusAfter) {
      super(entity, speed, time);
      this.searchRadiusAfter = searchRadiusAfter;
   }

   public boolean func_75250_a() {
      if (this.kidnapper.isTiedUp()) {
         return false;
      } else if (this.kidnapper.isGetOutState()) {
         return false;
      } else if (this.kidnapper.hasSlaves() && this.kidnapper.getSlave().isForSell()) {
         return false;
      } else if (!this.kidnapper.hasSlaves()) {
         return false;
      } else if (!this.kidnapper.isSlaveKidnappingModeEnabled()) {
         return false;
      } else {
         return this.kidnapper.isWaitingForJobToBeCompleted() ? false : super.func_75250_a();
      }
   }

   protected void actionTimeOut() {
      if (this.kidnapper.hasSlaves()) {
         this.kidnapper.teleportSlaveToPrison();
         if (this.kidnapper.shouldBackHomeAfterKidnapping()) {
            EntityPlayer potentialTarget = this.kidnapper.getClosestSuitablePlayer(this.searchRadiusAfter);
            if (potentialTarget == null) {
               Position home = this.kidnapper.getHome();
               if (home != null) {
                  this.kidnapper.teleportToPosition(home);
               }
            }
         }
      }

      this.timer = null;
   }

   protected boolean shouldResetTimer() {
      return !this.kidnapper.hasSlaves();
   }

   protected void actionOnResetTimer() {
      PlayerBindState state = this.kidnapper.getSlave();
      if (state != null) {
         EntityPlayer slave = state.getPlayer();
         if (slave != null) {
            this.kidnapper.talkTo(slave, "I'll take you to my owner's prison.");
         }
      }

   }
}
