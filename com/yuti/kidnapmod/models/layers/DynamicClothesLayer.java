package com.yuti.kidnapmod.models.layers;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.images.DynamicOnlineTexture;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class DynamicClothesLayer<E extends EntityLivingBase> implements LayerRenderer<E> {
   private final RenderLivingBase<?> renderer;

   public DynamicClothesLayer(RenderLivingBase<?> rendererIn) {
      this.renderer = rendererIn;
   }

   public void func_177141_a(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (ModConfig.displayDynamicTextures && entitylivingbaseIn != null && (entitylivingbaseIn instanceof I_Kidnapped || entitylivingbaseIn instanceof EntityPlayer)) {
         I_Kidnapped targetState = null;
         if (entitylivingbaseIn instanceof EntityPlayer) {
            EntityPlayer targetPlayer = (EntityPlayer)entitylivingbaseIn;
            KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)targetPlayer.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
            if (cap != null && !cap.allowClothes) {
               return;
            }

            targetState = PlayerBindState.getInstance(targetPlayer);
         } else {
            targetState = (I_Kidnapped)entitylivingbaseIn;
         }

         if (targetState != null && ((I_Kidnapped)targetState).hasClothes()) {
            ItemStack clothesStack = ((I_Kidnapped)targetState).getCurrentClothes();
            if (clothesStack != null && clothesStack.func_77973_b() instanceof ItemClothes) {
               ItemClothes clothes = (ItemClothes)clothesStack.func_77973_b();
               if (!clothes.isFullSkinEnabled(clothesStack)) {
                  DynamicOnlineTexture dynamicTexture = clothes.getDynamicTexture(clothesStack);
                  if (dynamicTexture != null && dynamicTexture.isValid()) {
                     ModelBase model = this.renderer.func_177087_b();
                     if (model instanceof ModelPlayer) {
                        ModelPlayer modelPlayer = (ModelPlayer)model;
                        dynamicTexture.bindClothes();
                        modelPlayer.func_78088_a(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                     }
                  }
               }
            }
         }
      }

   }

   public boolean func_177142_b() {
      return false;
   }
}
