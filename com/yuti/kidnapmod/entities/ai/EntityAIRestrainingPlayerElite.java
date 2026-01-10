package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.util.UtilsParameters;
import com.yuti.kidnapmod.util.time.Timer;

public class EntityAIRestrainingPlayerElite extends EntityAIRestrainingPlayer {
   public EntityAIRestrainingPlayerElite(EntityKidnapper kidnapper) {
      super(kidnapper, false);
   }

   public void func_75249_e() {
      super.func_75249_e();
      this.TIME_TO_WAIT = UtilsParameters.getTyingUpKidnapperEliteDelay(this.kidnapper.field_70170_p);
      if (this.kidnapper.getTarget() != null) {
         this.timer = new Timer(this.TIME_TO_WAIT);
      }

   }
}
