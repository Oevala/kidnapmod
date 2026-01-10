package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.item.ItemStack;

public class TyingDamselTask extends TyingTask {
   public TyingDamselTask(ItemStack bind, I_Kidnapped target, int seconds) {
      super(bind, target, seconds);
   }

   public synchronized void update() {
      super.update();
      if (this.timer.getSecondsRemaining() <= 0) {
         this.target.putBindOn(this.bind);
         this.stop();
      }

   }
}
