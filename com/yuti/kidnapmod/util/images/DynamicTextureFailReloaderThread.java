package com.yuti.kidnapmod.util.images;

public class DynamicTextureFailReloaderThread extends Thread {
   private String url;

   public DynamicTextureFailReloaderThread(String url) {
      this.url = url;
   }

   public void run() {
      super.run();

      try {
         Thread.sleep(60000L);
         DynamicTextureCache.INSTANCE.removeFail(this.url);
      } catch (InterruptedException var2) {
      }

   }
}
