package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;

public abstract class TimedInteractTask extends TimedTask {
   protected I_Kidnapped target;

   public TimedInteractTask(I_Kidnapped target, int seconds) {
      super(seconds);
      this.target = target;
   }

   public boolean isSameTarget(I_Kidnapped state) {
      if (this.target != null && state != null) {
         return this.target.getKidnappedUniqueId() != null && state.getKidnappedUniqueId() != null && this.target.getKidnappedUniqueId().equals(state.getKidnappedUniqueId());
      } else {
         return false;
      }
   }

   public void start() {
      super.start();
      this.setUpTargetState();
   }

   public abstract void setUpTargetState();
}
