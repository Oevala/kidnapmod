package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.images.DynamicOnlineTexture;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public abstract class KidnapModBaseRender<T extends EntityLivingBase> extends RenderLivingBase<T> {
   public KidnapModBaseRender(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn) {
      super(renderManagerIn, modelBaseIn, shadowSizeIn);
   }

   public abstract I_Kidnapped getKidnapped(T var1);

   public ModelPlayer getMainModel() {
      return (ModelPlayer)super.func_177087_b();
   }

   protected boolean bindEntityTexture(T entity) {
      if (entity != null && ModConfig.displayDynamicTextures) {
         KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)entity.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
         if (cap == null || cap != null && cap.allowClothes) {
            I_Kidnapped state = this.getKidnapped(entity);
            if (state != null && state.hasClothes()) {
               ItemStack clothesStack = state.getCurrentClothes();
               if (clothesStack != null && clothesStack.func_77973_b() instanceof ItemClothes) {
                  ItemClothes clothes = (ItemClothes)clothesStack.func_77973_b();
                  if (clothes.isFullSkinEnabled(clothesStack)) {
                     DynamicOnlineTexture dynamicSkin = clothes.getDynamicTexture(clothesStack);
                     if (dynamicSkin != null && dynamicSkin.isValid()) {
                        if (!ModConfig.displayTransparentSkins || cap != null && !cap.allowTransparentSkin) {
                           dynamicSkin.bindFullSkin();
                        } else {
                           dynamicSkin.bindClothes();
                        }

                        return true;
                     }
                  }
               }
            }
         }
      }

      return super.func_180548_c(entity);
   }

   protected void func_77036_a(T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
      if (ModConfig.displayDynamicTextures) {
         I_Kidnapped kidnapped = this.getKidnapped(entitylivingbaseIn);
         if (kidnapped != null) {
            KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)entitylivingbaseIn.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
            if (cap == null || cap != null && cap.allowClothes) {
               ItemStack clothes = kidnapped.getCurrentClothes();
               if (clothes != null && clothes.func_77973_b() instanceof ItemClothes) {
                  ItemClothes itemClothes = (ItemClothes)clothes.func_77973_b();
                  ModelPlayer model = this.getMainModel();
                  if (model != null) {
                     if (!itemClothes.isWearerHeadLayerEnabled(clothes)) {
                        model.field_178720_f.field_78806_j = false;
                     }

                     if (!itemClothes.isWearerBodyLayerEnabled(clothes)) {
                        model.field_178730_v.field_78806_j = false;
                     }

                     if (!itemClothes.isWearerLeftArmLayerEnabled(clothes)) {
                        model.field_178734_a.field_78806_j = false;
                     }

                     if (!itemClothes.isWearerRightArmLayerEnabled(clothes)) {
                        model.field_178732_b.field_78806_j = false;
                     }

                     if (!itemClothes.isWearerLeftLegLayerEnabled(clothes)) {
                        model.field_178733_c.field_78806_j = false;
                     }

                     if (!itemClothes.isWearerRightLegLayerEnabled(clothes)) {
                        model.field_178731_d.field_78806_j = false;
                     }
                  }
               }
            }
         }
      }

      super.func_77036_a(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
   }
}
