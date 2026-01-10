package com.yuti.kidnapmod.util.images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class DynamicTextureCache {
   public static final DynamicTextureCache INSTANCE = new DynamicTextureCache();
   private Map<String, DynamicOnlineTexture> mapTextures = new HashMap();
   private Set<String> setFail = new HashSet();

   @Nullable
   public DynamicOnlineTexture getTexture(String url) {
      if (url == null) {
         return null;
      } else {
         synchronized(this) {
            DynamicOnlineTexture texture = (DynamicOnlineTexture)this.mapTextures.get(url);
            return texture != null ? texture : null;
         }
      }
   }

   public boolean addTexture(String url, byte[] data) {
      synchronized(this) {
         boolean var10000;
         try {
            if (!this.mapTextures.containsKey(url)) {
               ByteArrayInputStream bais = new ByteArrayInputStream(data);
               BufferedImage image = ImageIO.read(bais);
               DynamicOnlineTexture texture = new DynamicOnlineTexture(image);
               this.mapTextures.put(url, texture);
            }

            var10000 = true;
         } catch (IOException var8) {
            var8.printStackTrace();
            return false;
         }

         return var10000;
      }
   }

   public void addFail(String url) {
      synchronized(this) {
         if (url != null && !this.setFail.contains(url)) {
            this.setFail.add(url);
            (new DynamicTextureFailReloaderThread(url)).start();
         }

      }
   }

   public void removeFail(String url) {
      synchronized(this) {
         if (url != null && this.setFail.contains(url)) {
            this.setFail.remove(url);
         }

      }
   }

   public boolean loadTexture(String url) {
      synchronized(this) {
         return this.mapTextures.containsKey(url);
      }
   }

   public boolean isInCache(String url) {
      synchronized(this) {
         return this.mapTextures.containsKey(url);
      }
   }

   public boolean isInFails(String url) {
      synchronized(this) {
         return this.setFail.contains(url);
      }
   }

   public void purgeCache() {
      synchronized(this) {
         this.mapTextures = new HashMap();
      }
   }

   @SubscribeEvent
   public void onClientShutDown(ClientDisconnectionFromServerEvent e) {
      synchronized(this) {
         this.setFail = new HashSet();
      }
   }
}
