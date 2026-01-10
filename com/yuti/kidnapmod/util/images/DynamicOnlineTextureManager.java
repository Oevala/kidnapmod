package com.yuti.kidnapmod.util.images;

import com.yuti.kidnapmod.common.ModConfig;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DynamicOnlineTextureManager {
   @SideOnly(Side.CLIENT)
   public static DynamicOnlineTexture loadUrl(String url) {
      if (url == null) {
         return null;
      } else if (!ModConfig.displayDynamicTextures) {
         return null;
      } else if (DynamicTextureCache.INSTANCE.isInFails(url)) {
         return null;
      } else if (!DynamicTextureCache.INSTANCE.loadTexture(url)) {
         if (!DynamicTextureThread.isLoading()) {
            (new DynamicTextureThread(url)).start();
         }

         return null;
      } else {
         return DynamicTextureCache.INSTANCE.getTexture(url);
      }
   }
}
