package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.models.layers.DynamicClothesLayer;
import com.yuti.kidnapmod.models.layers.ExtraBondageItemRenderLayerDamsel;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;

public class TiedUpRenderDamsel extends DamselKidnapModBaseRender {
   public TiedUpRenderDamsel(RenderManager renderManager, ModelPlayer model) {
      super(renderManager, model);
      this.func_177094_a(new LayerArrow(this));
      this.func_177094_a(new LayerCustomHead(this.getMainModel().field_78116_c));
      this.func_177094_a(new DynamicClothesLayer(this));
      this.func_177094_a(new ExtraBondageItemRenderLayerDamsel(this));
   }
}
