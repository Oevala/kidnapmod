package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.items.tasks.TimedTask;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class GuiTimedOverlay extends Gui {
   private final int tex_width = 102;
   private final int tex_height = 8;
   private final int bar_width = 100;

   protected abstract TimedTask getTask(PlayerBindState var1);

   protected abstract ResourceLocation getBar();

   @SubscribeEvent
   public void renderOverlay(RenderGameOverlayEvent event) {
      if (event.getType() == ElementType.TEXT) {
         Minecraft mc = Minecraft.func_71410_x();
         EntityPlayer player = mc.field_71439_g;
         if (player != null) {
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               TimedTask task = this.getTask(state);
               if (task != null && this.isTaskValid(task)) {
                  mc.field_71446_o.func_110577_a(this.getBar());
                  float oneUnit = 100.0F / (float)task.getTaskTime();
                  int currentWidth = (int)(oneUnit * (float)task.getState());
                  ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
                  int x = (sr.func_78326_a() - 102) / 2;
                  int y = (int)((double)(sr.func_78328_b() - 8) / 1.25D);
                  this.func_73729_b(x, y, 0, 0, 102, 8);
                  this.func_73729_b(x + 1, y, 1, 8, currentWidth, 8);
               }
            }
         }
      }

   }

   protected boolean isTaskValid(TimedTask task) {
      return !task.isStopped() && task.getState() < task.getTaskTime() && !task.isOutdated();
   }
}
