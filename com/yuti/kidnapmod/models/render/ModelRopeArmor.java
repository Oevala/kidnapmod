package com.yuti.kidnapmod.models.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelRopeArmor extends ModelBiped {
   public ModelRopeArmor() {
      super(0.51F);
   }

   public void func_78087_a(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      super.func_78087_a(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      this.field_178724_i.field_78795_f = 0.8F;
      this.field_178724_i.field_78796_g = -1.1F;
      this.field_178723_h.field_78795_f = 0.8F;
      this.field_178723_h.field_78796_g = 1.1F;
      this.field_178722_k.field_78795_f = 0.0F;
      this.field_178722_k.field_78808_h = 0.0F;
      this.field_178721_j.field_78795_f = 0.0F;
      this.field_178721_j.field_78808_h = 0.0F;
      this.field_178724_i.field_78808_h = 0.0F;
      this.field_178723_h.field_78808_h = 0.0F;
      GlStateManager.func_179094_E();
      this.field_78115_e.func_78785_a(0.058F);
      this.field_178724_i.func_78785_a(0.058F);
      this.field_178723_h.func_78785_a(0.058F);
      GlStateManager.func_179121_F();
   }
}
