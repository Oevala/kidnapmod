package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.models.layers.DynamicClothesLayer;
import com.yuti.kidnapmod.models.layers.ExtraBondageItemRenderLayerDamsel;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;

public class DamselKidnapRender extends DamselKidnapModBaseRender {
   public DamselKidnapRender(RenderManager renderManager, ModelPlayer model) {
      super(renderManager, model);
      this.func_177094_a(new LayerHeldItem(this));
      this.func_177094_a(new LayerArrow(this));
      this.func_177094_a(new LayerCustomHead(this.getMainModel().field_78116_c));
      this.func_177094_a(new DynamicClothesLayer(this));
      this.func_177094_a(new LayerBipedArmor(this));
      this.func_177094_a(new ExtraBondageItemRenderLayerDamsel(this));
   }
}
