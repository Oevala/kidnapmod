package com.yuti.kidnapmod.util.images;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;

public class DynamicOnlineTexture {
   private int baseTextureId = -1;
   private int clothesTextureId = -1;
   private int fullSkinTextureId = -1;
   private boolean flagresize = false;
   private boolean valid = false;

   public DynamicOnlineTexture(BufferedImage bufferedimage) {
      this.load(bufferedimage);
   }

   public void load(BufferedImage bufferedimage) {
      this.valid = this.parseTexture(bufferedimage);
   }

   @Nullable
   private boolean parseTexture(BufferedImage bufferedimage) {
      if (bufferedimage == null) {
         return false;
      } else {
         int height = bufferedimage.getHeight();
         int width = bufferedimage.getWidth();
         if (height != 0 && width != 0 && height % 32 == 0 && width % 64 == 0 && height <= 8192 && width <= 8192) {
            BufferedImage clothesImage = this.getResizedImage(bufferedimage);
            BufferedImage fullSkinImage = new BufferedImage(width, width, 2);
            Graphics graphicsFull = fullSkinImage.getGraphics();
            graphicsFull.drawImage(clothesImage, 0, 0, (ImageObserver)null);
            graphicsFull.dispose();
            int[] imageDataFull = ((DataBufferInt)fullSkinImage.getRaster().getDataBuffer()).getData();
            int widthQuart = width / 4;
            setAreaOpaque(imageDataFull, width, 0, 0, widthQuart, widthQuart);
            setAreaOpaque(imageDataFull, width, 0, widthQuart, width, widthQuart * 2);
            setAreaOpaque(imageDataFull, width, widthQuart, widthQuart * 3, widthQuart * 3, width);
            Minecraft.func_71410_x().func_152344_a(() -> {
               TextureUtil.func_110989_a(this.getBaseTextureId(), bufferedimage, false, false);
               if (this.flagresize) {
                  TextureUtil.func_110989_a(this.getClothesTextureId(), clothesImage, false, false);
               }

               TextureUtil.func_110989_a(this.getFullSkinTextureId(), fullSkinImage, false, false);
            });
            return true;
         } else {
            return false;
         }
      }
   }

   private BufferedImage getResizedImage(BufferedImage bufferedimage) {
      int height = bufferedimage.getHeight();
      int width = bufferedimage.getWidth();
      BufferedImage clothesImage;
      if (height % width != 0) {
         this.flagresize = true;
         clothesImage = new BufferedImage(width, width, 2);
         Graphics graphics = clothesImage.getGraphics();
         graphics.drawImage(bufferedimage, 0, 0, (ImageObserver)null);
         int xMultiplicator = height / 32;
         int yMultiplicator = width / 64;
         graphics.setColor(new Color(0, 0, 0, 0));
         graphics.fillRect(0, height, width, height);
         graphics.drawImage(bufferedimage, 24 * xMultiplicator, 48 * yMultiplicator, 20 * xMultiplicator, 52 * yMultiplicator, 4 * xMultiplicator, 16 * yMultiplicator, 8 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 28 * xMultiplicator, 48 * yMultiplicator, 24 * xMultiplicator, 52 * yMultiplicator, 8 * xMultiplicator, 16 * yMultiplicator, 12 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 20 * xMultiplicator, 52 * yMultiplicator, 16 * xMultiplicator, 64 * yMultiplicator, 8 * xMultiplicator, 20 * yMultiplicator, 12 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 24 * xMultiplicator, 52 * yMultiplicator, 20 * xMultiplicator, 64 * yMultiplicator, 4 * xMultiplicator, 20 * yMultiplicator, 8 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 28 * xMultiplicator, 52 * yMultiplicator, 24 * xMultiplicator, 64 * yMultiplicator, 0 * xMultiplicator, 20 * yMultiplicator, 4 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 32 * xMultiplicator, 52 * yMultiplicator, 28 * xMultiplicator, 64 * yMultiplicator, 12 * xMultiplicator, 20 * yMultiplicator, 16 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 40 * xMultiplicator, 48 * yMultiplicator, 36 * xMultiplicator, 52 * yMultiplicator, 44 * xMultiplicator, 16 * yMultiplicator, 48 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 44 * xMultiplicator, 48 * yMultiplicator, 40 * xMultiplicator, 52 * yMultiplicator, 48 * xMultiplicator, 16 * yMultiplicator, 52 * xMultiplicator, 20 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 36 * xMultiplicator, 52 * yMultiplicator, 32 * xMultiplicator, 64 * yMultiplicator, 48 * xMultiplicator, 20 * yMultiplicator, 52 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 40 * xMultiplicator, 52 * yMultiplicator, 36 * xMultiplicator, 64 * yMultiplicator, 44 * xMultiplicator, 20 * yMultiplicator, 48 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 44 * xMultiplicator, 52 * yMultiplicator, 40 * xMultiplicator, 64 * yMultiplicator, 40 * xMultiplicator, 20 * yMultiplicator, 44 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.drawImage(bufferedimage, 48 * xMultiplicator, 52 * yMultiplicator, 44 * xMultiplicator, 64 * yMultiplicator, 52 * xMultiplicator, 20 * yMultiplicator, 56 * xMultiplicator, 32 * yMultiplicator, (ImageObserver)null);
         graphics.dispose();
         int[] imageData = ((DataBufferInt)clothesImage.getRaster().getDataBuffer()).getData();
         setAreaTransparent(imageData, width, width, height, 0, width, height);
      } else {
         clothesImage = bufferedimage;
      }

      return clothesImage;
   }

   public void bindClothes() {
      GlStateManager.func_179144_i(this.getClothesTextureId());
   }

   public void bindFullSkin() {
      GlStateManager.func_179144_i(this.getFullSkinTextureId());
   }

   public void bindBase() {
      GlStateManager.func_179144_i(this.getBaseTextureId());
   }

   public int getBaseTextureId() {
      if (this.baseTextureId == -1 || !GL11.glIsTexture(this.baseTextureId)) {
         this.baseTextureId = GlStateManager.func_179146_y();
      }

      return this.baseTextureId;
   }

   public int getClothesTextureId() {
      if (!this.flagresize) {
         return this.getBaseTextureId();
      } else {
         if (this.clothesTextureId == -1 || !GL11.glIsTexture(this.clothesTextureId)) {
            this.clothesTextureId = GlStateManager.func_179146_y();
         }

         return this.clothesTextureId;
      }
   }

   public int getFullSkinTextureId() {
      if (this.fullSkinTextureId == -1 || !GL11.glIsTexture(this.fullSkinTextureId)) {
         this.fullSkinTextureId = GlStateManager.func_179146_y();
      }

      return this.fullSkinTextureId;
   }

   private static void setAreaTransparent(int[] imageData, int imageHeight, int imageWidth, int x, int y, int width, int height) {
      int l;
      int i1;
      for(l = x; l < width; ++l) {
         for(i1 = y; i1 < height; ++i1) {
            int k = imageData[l + i1 * imageWidth];
            if ((k >> 24 & 255) < 128) {
               return;
            }
         }
      }

      for(l = x; l < width; ++l) {
         for(i1 = y; i1 < height; ++i1) {
            imageData[l + i1 * imageWidth] &= 16777215;
         }
      }

   }

   private static void setAreaOpaque(int[] imageData, int imageWidth, int x, int y, int width, int height) {
      for(int i = x; i < width; ++i) {
         for(int j = y; j < height; ++j) {
            imageData[i + j * imageWidth] |= -16777216;
         }
      }

   }

   public boolean isValid() {
      return this.valid;
   }
}
