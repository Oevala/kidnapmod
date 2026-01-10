package com.yuti.kidnapmod.models.render.straitjacket;

import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelTiedUpStraitJacket extends ModelPlayer {
   public ModelTiedUpStraitJacket(float modelSize, boolean smallArmsIn) {
      super(modelSize, smallArmsIn);
   }

   public void func_78087_a(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
      super.func_78087_a(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
      this.field_178722_k.field_78795_f = 0.0F;
      this.field_178722_k.field_78808_h = 0.0F;
      this.field_178721_j.field_78795_f = 0.0F;
      this.field_178721_j.field_78808_h = 0.0F;
      GlStateManager.func_179094_E();
      this.field_178724_i.func_78785_a(0.0F);
      this.field_178723_h.func_78785_a(0.0F);
      this.field_178734_a.func_78785_a(0.0F);
      this.field_178732_b.func_78785_a(0.0F);
      GlStateManager.func_179121_F();
      func_178685_a(this.field_178722_k, this.field_178733_c);
      func_178685_a(this.field_178721_j, this.field_178731_d);
      func_178685_a(this.field_178724_i, this.field_178734_a);
      func_178685_a(this.field_178723_h, this.field_178732_b);
      func_178685_a(this.field_78115_e, this.field_178730_v);
   }
}
