package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;

public class EntityAIWanderExceptWhenTargeting extends EntityAIWanderExceptWhenTiedUp {
   private EntityKidnapper kidnapper;

   public EntityAIWanderExceptWhenTargeting(EntityKidnapper kidnapper, double speed, float chance) {
      super(kidnapper, speed, chance);
      this.kidnapper = kidnapper;
   }

   public boolean func_75250_a() {
      if (this.kidnapper.getTarget() == null && !this.kidnapper.hasSlaves() && !this.kidnapper.isGetOutState()) {
         return this.kidnapper.isWaitingForJobToBeCompleted() ? false : super.func_75250_a();
      } else {
         return false;
      }
   }
}
