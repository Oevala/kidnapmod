package com.yuti.kidnapmod.entities.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.util.ResourceLocation;

public class RenderEntityKidnapBomb extends RenderTNTPrimed {
   public static final ResourceLocation res = new ResourceLocation("knapm", "textures/special/blank_explosion.png");

   public RenderEntityKidnapBomb(RenderManager renderManagerIn) {
      super(renderManagerIn);
   }

   protected ResourceLocation func_110775_a(EntityTNTPrimed entity) {
      return res;
   }
}
