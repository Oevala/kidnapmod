package com.yuti.kidnapmod.util.handlers;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

@EventBusSubscriber
public class KidnapCapabilitiesHandler {
   @SubscribeEvent
   public static void onPlayerCloned(Clone event) {
      if (event.getOriginal().hasCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null)) {
         KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)event.getOriginal().getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
         KidnapSettingsCapabilities newCap = (KidnapSettingsCapabilities)event.getEntityPlayer().getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
         newCap.setMapAdjustements(cap.getMapAdjustements());
      }

   }

   @SubscribeEvent
   public static void onPlayerRespawn(PlayerRespawnEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         KidnapSettingsCapabilities.syncAllJoin((EntityPlayerMP)event.player);
      }

   }

   @SubscribeEvent
   public static void onPlayerChangeDimension(PlayerChangedDimensionEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         KidnapSettingsCapabilities.syncAllJoin((EntityPlayerMP)event.player);
      }

   }

   @SubscribeEvent
   public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof EntityPlayer) {
         event.addCapability(new ResourceLocation("knapm:ADJUST_GAG"), new KidnapSettingsCapabilities((EntityPlayer)event.getObject()));
      }

   }

   @SubscribeEvent
   public static void playerLog(PlayerLoggedInEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         if (event.player instanceof EntityPlayerMP) {
            KidnapSettingsCapabilities.syncAllJoin((EntityPlayerMP)event.player);
         }

      }
   }

   @SubscribeEvent
   public static void playerJoin(EntityJoinWorldEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)entity;
         KidnapSettingsCapabilities.syncAllJoin(player);
      }

   }

   @SubscribeEvent
   public static void onStartTracking(StartTracking event) {
      EntityPlayer player = event.getEntityPlayer();
      if (player instanceof EntityPlayerMP) {
         KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)player.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
         if (cap != null) {
            Entity target = event.getTarget();
            if (target instanceof EntityPlayerMP) {
               cap.syncwith((EntityPlayerMP)target);
            }
         }
      }

   }
}
