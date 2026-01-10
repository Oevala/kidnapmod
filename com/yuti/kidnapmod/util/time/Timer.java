package com.yuti.kidnapmod.util.time;

import java.io.Serializable;
import java.util.Date;

public class Timer implements Serializable {
   private int time_to_wait;
   private Date timeStart = null;

   public Timer(int time_to_wait) {
      this.time_to_wait = time_to_wait;
      this.timeStart = new Date();
   }

   public int getSecondsRemaining() {
      Date newDate = new Date();
      return this.time_to_wait - (int)((newDate.getTime() - this.timeStart.getTime()) / 1000L);
   }

   public int getInitialSeconds() {
      return this.time_to_wait;
   }
}
