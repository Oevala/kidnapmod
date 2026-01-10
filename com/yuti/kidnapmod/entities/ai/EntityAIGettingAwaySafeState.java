package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;

public class EntityAIGettingAwaySafeState extends EntityAIWanderFastTimed {
   public EntityAIGettingAwaySafeState(EntityKidnapper entity, double speed, int time) {
      super(entity, speed, time);
   }

   public boolean func_75250_a() {
      if (this.kidnapper.hasCollar()) {
         return false;
      } else if (!this.kidnapper.isGetOutState()) {
         return false;
      } else {
         return this.kidnapper.hasSlaves() ? false : super.func_75250_a();
      }
   }

   protected void actionTimeOut() {
      this.kidnapper.setGetOutState(false);
      this.timer = null;
   }

   protected boolean shouldResetTimer() {
      return false;
   }

   protected void actionOnResetTimer() {
   }
}
