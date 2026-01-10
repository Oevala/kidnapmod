package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.util.time.Timer;

public class PlayerStateTask {
   private int maxState;
   private Timer timerOutdating;
   private int state;

   public PlayerStateTask(int maxState) {
      this.maxState = maxState;
      this.state = 0;
   }

   public void update(int state) {
      this.timerOutdating = new Timer(3);
      this.state = state;
   }

   public boolean isOutdated() {
      return this.timerOutdating != null && this.timerOutdating.getSecondsRemaining() <= 0;
   }

   public int getState() {
      return this.state;
   }

   public int getMaxState() {
      return this.maxState;
   }
}
