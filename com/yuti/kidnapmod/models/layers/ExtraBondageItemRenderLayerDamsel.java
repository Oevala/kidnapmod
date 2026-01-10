package com.yuti.kidnapmod.models.layers;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.init.MobEffects;

public class ExtraBondageItemRenderLayerDamsel extends ExtraBondageItemRenderLayer<EntityDamsel> {
   public ExtraBondageItemRenderLayerDamsel(RenderLivingBase<?> rendererIn) {
      super(rendererIn);
   }

   public void doRenderLayer(EntityDamsel damsel, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (damsel.func_70660_b(MobEffects.field_76441_p) == null) {
         this.renderItem(damsel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, damsel.getCurrentBind(), false);
         this.renderItem(damsel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, damsel.getCurrentGag(), false);
         this.renderItem(damsel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, damsel.getCurrentBlindfold(), false);
         this.renderItem(damsel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, damsel.getCurrentEarplugs(), false);
         this.renderItem(damsel, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, damsel.getCurrentCollar(), false);
      }
   }
}
