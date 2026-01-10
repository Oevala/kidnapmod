package com.yuti.kidnapmod.util.handlers;

import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.entities.EntityInvisibleSlaveTransporter;
import com.yuti.kidnapmod.entities.EntityKidnapBomb;
import com.yuti.kidnapmod.entities.EntityRopeArrow;
import com.yuti.kidnapmod.entities.render.RenderEntityDamsel;
import com.yuti.kidnapmod.entities.render.RenderEntityInvisibleSlaveTransport;
import com.yuti.kidnapmod.entities.render.RenderEntityKidnapBomb;
import com.yuti.kidnapmod.entities.render.RenderEntityRopeArrow;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class RenderHandler {
   public static void registerEntityRenders() {
      RenderingRegistry.registerEntityRenderingHandler(EntityDamsel.class, new IRenderFactory<EntityDamsel>() {
         public Render<? super EntityDamsel> createRenderFor(RenderManager manager) {
            return new RenderEntityDamsel(manager, new ModelPlayer(0.0F, false), 0.5F);
         }
      });
      RenderingRegistry.registerEntityRenderingHandler(EntityInvisibleSlaveTransporter.class, new IRenderFactory<EntityInvisibleSlaveTransporter>() {
         public Render<? super EntityInvisibleSlaveTransporter> createRenderFor(RenderManager manager) {
            return new RenderEntityInvisibleSlaveTransport(manager);
         }
      });
      RenderingRegistry.registerEntityRenderingHandler(EntityRopeArrow.class, new IRenderFactory<EntityRopeArrow>() {
         public Render<? super EntityRopeArrow> createRenderFor(RenderManager manager) {
            return new RenderEntityRopeArrow(manager);
         }
      });
      RenderingRegistry.registerEntityRenderingHandler(EntityKidnapBomb.class, new IRenderFactory<EntityKidnapBomb>() {
         public Render<? super EntityKidnapBomb> createRenderFor(RenderManager manager) {
            return new RenderEntityKidnapBomb(manager);
         }
      });
   }
}
