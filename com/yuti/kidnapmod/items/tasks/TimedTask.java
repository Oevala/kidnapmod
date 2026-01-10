package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.util.time.Timer;

public class TimedTask {
   protected Timer timer;
   protected int seconds;
   protected boolean stop = false;
   protected Timer timerOutdating;
   protected int secondsState;

   public TimedTask(int seconds) {
      this.seconds = seconds;
      this.secondsState = seconds;
   }

   public synchronized void update() {
      if (!this.stop) {
         this.resetOutdatingTimer();
         this.secondsState = this.timer.getSecondsRemaining();
      }
   }

   public void reset() {
      this.timer = new Timer(this.seconds);
      this.secondsState = this.timer.getSecondsRemaining();
      this.stop = false;
      this.resetOutdatingTimer();
   }

   public void stop() {
      this.stop = true;
   }

   public void start() {
      this.timer = new Timer(this.seconds);
      this.resetOutdatingTimer();
   }

   public boolean isOutdated() {
      return this.timerOutdating != null && this.timerOutdating.getSecondsRemaining() <= 0;
   }

   private void resetOutdatingTimer() {
      this.timerOutdating = new Timer(2);
   }

   public boolean isStopped() {
      return this.stop;
   }

   public int getTaskTime() {
      return this.seconds;
   }

   public int getState() {
      return this.seconds - this.secondsState;
   }

   public boolean isOver() {
      return this.timer.getSecondsRemaining() <= 0 && !this.isOutdated();
   }
}
