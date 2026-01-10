package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.item.ItemStack;

public abstract class TyingTask extends TimedInteractTask {
   protected ItemStack bind;

   public TyingTask(ItemStack bind, I_Kidnapped target, int seconds) {
      super(target, seconds);
      this.bind = bind;
   }

   public void update() {
      super.update();
   }

   public void setUpTargetState() {
   }

   public ItemStack getBind() {
      return this.bind;
   }

   public void setBind(ItemStack bind) {
      this.bind = bind;
   }
}
