package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.items.tasks.PlayerStateTask;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.util.ResourceLocation;

public class GuiRestrainedOverlay extends GuiTimedState {
   public final ResourceLocation ressource = new ResourceLocation("knapm", "textures/gui/restrainedbar.png");

   protected double getBarHeight() {
      return 1.5D;
   }

   protected PlayerStateTask getTask(PlayerBindState state) {
      return state.getRestrainedState();
   }

   protected ResourceLocation getBar() {
      return this.ressource;
   }

   protected boolean validState(PlayerBindState state) {
      return !state.isTiedUp();
   }

   public int validMinState() {
      return 1;
   }
}
