package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.items.tasks.PlayerStateTask;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.UtilsParameters;
import com.yuti.kidnapmod.util.time.Timer;

public class EntityAIRestrainingPlayerClassic extends EntityAIRestrainingPlayer {
   public EntityAIRestrainingPlayerClassic(EntityKidnapper kidnapper) {
      super(kidnapper, true);
   }

   public void func_75249_e() {
      super.func_75249_e();
      this.TIME_TO_WAIT = UtilsParameters.getTyingUpKidnapperDelay(this.kidnapper.field_70170_p);
      this.kidnapper.talkTo(this.kidnapper.getTarget(), "Stop struggling!");
      this.kidnapper.actionTo(this.kidnapper.getTarget(), "is tying you up!");
      if (this.kidnapper.getTarget() != null) {
         PlayerBindState state = PlayerBindState.getInstance(this.kidnapper.getTarget());
         if (state != null) {
            PlayerStateTask restrainState = state.getRestrainedState();
            if (restrainState == null) {
               restrainState = new PlayerStateTask(this.TIME_TO_WAIT);
               state.setRestrainedState(restrainState);
            }
         }

         this.timer = new Timer(this.TIME_TO_WAIT);
      }

   }
}
