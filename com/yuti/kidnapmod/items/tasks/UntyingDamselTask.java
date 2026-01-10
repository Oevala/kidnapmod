package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.entity.player.EntityPlayer;

public class UntyingDamselTask extends UntyingTask {
   public UntyingDamselTask(EntityPlayer player, I_Kidnapped target, int seconds) {
      super(player, target, seconds);
   }

   public synchronized void update() {
      super.update();
      if (this.timer.getSecondsRemaining() <= 0) {
         this.target.dropBondageItems();
         this.target.untie();
         this.target.free();
         this.stop();
      }

   }
}
