package com.yuti.kidnapmod.util.images;

import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;

public class DynamicTextureThread extends Thread {
   private static final String[] FORMATS = new String[]{"image/png", "image/jpeg", "image/bmp"};
   private static final long MAX_FILE_SIZE = 2097152L;
   private static boolean LOADING = false;
   private String url;

   public DynamicTextureThread(String url) {
      this.url = url;
   }

   public synchronized void start() {
      if (!isLoading()) {
         setLoading(true);
         super.start();
      }

   }

   public void run() {
      if (this.url == null) {
         setLoading(false);
      } else if (DynamicTextureCache.INSTANCE.loadTexture(this.url)) {
         setLoading(false);
      } else {
         try {
            URLConnection connection = (new URL(this.url)).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            boolean failed = true;
            String[] var3 = FORMATS;
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String format = var3[var5];
               if (format.equals(connection.getContentType())) {
                  failed = false;
                  break;
               }
            }

            if (failed) {
               DynamicTextureCache.INSTANCE.addFail(this.url);
               setLoading(false);
               return;
            }

            long length = Long.parseLong(connection.getHeaderField("Content-Length"));
            if (length > 2097152L) {
               DynamicTextureCache.INSTANCE.addFail(this.url);
               setLoading(false);
               return;
            }

            byte[] data = IOUtils.toByteArray(connection);
            if (DynamicTextureCache.INSTANCE.addTexture(this.url, data)) {
               setLoading(false);
               return;
            }
         } catch (Exception var7) {
            DynamicTextureCache.INSTANCE.addFail(this.url);
            setLoading(false);
         }

         setLoading(false);
      }
   }

   public static boolean isLoading() {
      return LOADING;
   }

   private static void setLoading(boolean loading) {
      LOADING = loading;
   }
}
