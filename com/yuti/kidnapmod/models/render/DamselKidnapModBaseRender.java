package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre;
import net.minecraftforge.common.MinecraftForge;

public class DamselKidnapModBaseRender extends KidnapModBaseRender<EntityDamsel> {
   public DamselKidnapModBaseRender(RenderManager renderManagerIn, ModelBiped modelBipedIn) {
      super(renderManagerIn, modelBipedIn, 0.5F);
   }

   public ModelPlayer getMainModel() {
      return super.getMainModel();
   }

   public void renderName(EntityDamsel entity, double x, double y, double z) {
      if (!MinecraftForge.EVENT_BUS.post(new Pre(entity, this, x, y, z))) {
         if (this.func_177070_b(entity)) {
            double d0 = entity.func_70068_e(this.field_76990_c.field_78734_h);
            float f = entity.func_70093_af() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
            if (d0 < (double)(f * f)) {
               String name = entity.getCurrentName();
               if (name != null) {
                  TextFormatting color = entity.getNameColor();
                  String coloredName;
                  if (entity.hasNamedCollar()) {
                     coloredName = color + "*" + name + "*";
                  } else {
                     coloredName = color + name;
                  }

                  GlStateManager.func_179092_a(516, 0.1F);
                  this.func_188296_a(entity, x, y, z, coloredName, d0);
               }
            }
         }

         MinecraftForge.EVENT_BUS.post(new Post(entity, this, x, y, z));
      }
   }

   protected ResourceLocation getEntityTexture(EntityDamsel entity) {
      return entity.getSkin();
   }

   public void func_82422_c() {
      GlStateManager.func_179109_b(0.0F, 0.1875F, 0.0F);
   }

   protected void renderModel(EntityDamsel entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
      ModelPlayer model = this.getMainModel();
      if (model != null) {
         model.field_178720_f.field_78806_j = entitylivingbaseIn.hasHeadLayer();
         model.field_178730_v.field_78806_j = entitylivingbaseIn.hasBodyLayer();
         model.field_178734_a.field_78806_j = entitylivingbaseIn.hasLeftArmLayer();
         model.field_178732_b.field_78806_j = entitylivingbaseIn.hasRightArmLayer();
         model.field_178733_c.field_78806_j = entitylivingbaseIn.hasLeftLegLayer();
         model.field_178731_d.field_78806_j = entitylivingbaseIn.hasRightLegLayer();
      }

      super.func_77036_a(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
   }

   public I_Kidnapped getKidnapped(EntityDamsel damsel) {
      return damsel;
   }
}
