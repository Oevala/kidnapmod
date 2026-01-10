package com.yuti.kidnapmod.util.handlers;

import com.yuti.kidnapmod.KidnapModMain;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class ConfigHandler {
   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void onConfigChange(OnConfigChangedEvent event) {
      if (event.getModID().equals("knapm")) {
         KidnapModMain.config.save();
      }

   }
}
