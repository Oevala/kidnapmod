package com.yuti.kidnapmod.gui.extrainventory;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent.Post;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiExtraBondageEvents {
   @SubscribeEvent
   public void guiPostInit(Post event) {
      if (event.getGui() instanceof GuiInventory || event.getGui() instanceof GuiBondageExtended) {
         GuiContainer gui = (GuiContainer)event.getGui();
         event.getButtonList().add(new GuiExtraBondageButton(55, gui, 28, 9, 16, 16, I18n.func_135052_a(event.getGui() instanceof GuiInventory ? "button.bondageinventory" : "button.normalinventory", new Object[0])));
      }

   }
}
