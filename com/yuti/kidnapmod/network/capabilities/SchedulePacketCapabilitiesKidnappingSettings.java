package com.yuti.kidnapmod.network.capabilities;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SchedulePacketCapabilitiesKidnappingSettings implements Runnable {
   private PacketCapabilityKidnappingSettings message;

   public SchedulePacketCapabilitiesKidnappingSettings(PacketCapabilityKidnappingSettings message) {
      this.message = message;
   }

   public void run() {
      World world = KidnapModMain.proxy.getClientWorld();
      if (world != null) {
         Entity p = world.func_73045_a(this.message.playerId);
         if (p != null && p instanceof EntityPlayer) {
            KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)p.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
            cap.deserializeNBT(this.message.settingsCap);
         }

      }
   }

   @SideOnly(Side.CLIENT)
   private EntityPlayer getPlayer() {
      return Minecraft.func_71410_x().field_71439_g;
   }
}
