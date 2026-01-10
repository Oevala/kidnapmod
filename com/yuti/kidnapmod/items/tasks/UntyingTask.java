package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.entity.player.EntityPlayer;

public abstract class UntyingTask extends TimedInteractTask {
   protected EntityPlayer player;

   public UntyingTask(EntityPlayer player, I_Kidnapped target, int seconds) {
      super(target, seconds);
      this.player = player;
   }

   public synchronized void update() {
      super.update();
   }

   public void setUpTargetState() {
   }
}
