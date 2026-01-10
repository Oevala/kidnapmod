package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.models.layers.DynamicClothesLayer;
import com.yuti.kidnapmod.models.layers.ExtraBondageItemRenderLayerPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;

public class TiedUpRenderPlayer extends PlayerKidnapModBaseRender {
   private final boolean smallArms;

   public TiedUpRenderPlayer(RenderManager renderManager, ModelPlayer model) {
      this(renderManager, false, model);
   }

   public TiedUpRenderPlayer(RenderManager renderManager, boolean useSmallArms, ModelPlayer model) {
      super(renderManager, useSmallArms, model);
      this.smallArms = useSmallArms;
      this.func_177094_a(new LayerArrow(this));
      this.func_177094_a(new LayerCustomHead(this.getMainModel().field_78116_c));
      this.func_177094_a(new LayerElytra(this));
      this.func_177094_a(new LayerEntityOnShoulder(renderManager));
      this.func_177094_a(new DynamicClothesLayer(this));
      this.func_177094_a(new ExtraBondageItemRenderLayerPlayer(this));
   }
}
