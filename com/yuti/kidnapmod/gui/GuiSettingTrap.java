package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.items.tasks.PlayerStateTask;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.util.ResourceLocation;

public class GuiSettingTrap extends GuiTimedState {
   public final ResourceLocation ressource = new ResourceLocation("knapm", "textures/gui/tyingbar.png");

   protected PlayerStateTask getTask(PlayerBindState state) {
      return state.getClientTrapPlaceTask();
   }

   protected ResourceLocation getBar() {
      return this.ressource;
   }

   protected double getBarHeight() {
      return 1.25D;
   }

   protected boolean validState(PlayerBindState state) {
      return !state.isTiedUp();
   }
}
