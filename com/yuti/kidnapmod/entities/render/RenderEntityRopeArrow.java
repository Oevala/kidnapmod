package com.yuti.kidnapmod.entities.render;

import com.yuti.kidnapmod.entities.EntityRopeArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderEntityRopeArrow extends RenderArrow<EntityRopeArrow> {
   public static final ResourceLocation res = new ResourceLocation("knapm", "textures/entity/projectiles/ropearrow.png");

   public RenderEntityRopeArrow(RenderManager rm) {
      super(rm);
   }

   public ResourceLocation getEntityTexture(EntityRopeArrow entity) {
      return res;
   }
}
