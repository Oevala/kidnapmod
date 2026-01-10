package com.yuti.kidnapmod.entities.render;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityDamsel extends RenderBiped<EntityDamsel> {
   public RenderEntityDamsel(RenderManager renderManager, ModelBiped model, float shadow) {
      super(renderManager, model, shadow);
   }

   protected ResourceLocation getEntityTexture(EntityDamsel living) {
      return this.getKidnapperTexture(living);
   }

   private ResourceLocation getKidnapperTexture(EntityDamsel mob) {
      return mob.getSkin();
   }

   protected void applyRotations(EntityDamsel p_applyRotations_1_, float p_applyRotations_2_, float p_applyRotations_3_, float p_applyRotations_4_) {
      super.func_77043_a(p_applyRotations_1_, p_applyRotations_2_, p_applyRotations_3_, p_applyRotations_4_);
   }
}
