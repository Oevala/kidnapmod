package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.states.PlayerBindState;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIWanderFastTimedForKidnapper extends EntityAIWanderFastTimed {
   public EntityAIWanderFastTimedForKidnapper(EntityKidnapper entity, double speed, int time) {
      super(entity, speed, time);
   }

   public boolean func_75250_a() {
      if (this.kidnapper.isTiedUp()) {
         return false;
      } else if (this.kidnapper.hasCollar()) {
         return false;
      } else if (this.kidnapper.isGetOutState()) {
         return false;
      } else if (this.kidnapper.hasSlaves() && this.kidnapper.getSlave().isForSell()) {
         return false;
      } else if (!this.kidnapper.hasSlaves()) {
         return false;
      } else {
         return this.kidnapper.isWaitingForJobToBeCompleted() ? false : super.func_75250_a();
      }
   }

   protected void actionTimeOut() {
      if (this.kidnapper.hasSlaves()) {
         Random rand = new Random();
         List<EntityKidnapper.SlavesTask> tasks = EntityKidnapper.SlavesTask.getPossiblesTaks(this.kidnapper);
         if (!tasks.isEmpty()) {
            int randVal = rand.nextInt(tasks.size());
            EntityKidnapper.SlavesTask task = (EntityKidnapper.SlavesTask)tasks.get(randVal);
            if (task == EntityKidnapper.SlavesTask.SELL) {
               this.kidnapper.putSlaveForSale();
            } else if (task == EntityKidnapper.SlavesTask.JOB) {
               this.kidnapper.giveJobToSlave();
            }
         } else {
            this.kidnapper.setGetOutState(true);
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
            this.kidnapper.talkTo(slave, "I'll take you somewhere nice.");
         }
      }

   }
}
