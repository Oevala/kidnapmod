package com.yuti.kidnapmod.models.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;

public class ModelAdjusted extends ModelBiped {
   public ModelAdjusted(float modelSize, float adjustementIn) {
      super(modelSize);
      this.field_78116_c = new ModelRenderer(this, 0, 0);
      this.field_78116_c.func_78790_a(-4.0F, -8.0F - adjustementIn, -4.0F, 8, 8, 8, modelSize);
      this.field_78116_c.func_78793_a(0.0F, 0.0F, 0.0F);
      this.field_178720_f = new ModelRenderer(this, 32, 0);
      this.field_178720_f.func_78790_a(-4.0F, -8.0F - adjustementIn, -4.0F, 8, 8, 8, modelSize + 0.5F);
      this.field_178720_f.func_78793_a(0.0F, 0.0F, 0.0F);
   }
}
