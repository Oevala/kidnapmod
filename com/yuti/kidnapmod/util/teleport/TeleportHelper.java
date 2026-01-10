package com.yuti.kidnapmod.util.teleport;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TeleportHelper {
   public static void doTeleport(EntityPlayerMP player, Position position) {
      if (player != null && position != null && player.field_71135_a != null) {
         player.func_184210_p();
         if (player.field_71093_bK != position.getDimension()) {
            transferPlayerToDimension(player, position.getDimension());
         }

         player.field_71135_a.func_147364_a(position.getX(), position.getY(), position.getZ(), position.getYaw(), position.getPitch());
      }
   }

   public static Entity doTeleportEntity(Entity entity, Position position) {
      if (entity != null && position != null) {
         if (entity instanceof EntityPlayerMP) {
            doTeleport((EntityPlayerMP)entity, position);
            return entity;
         } else {
            Entity target = entity;
            if (entity.field_71093_bK != position.getDimension()) {
               entity.func_181015_d(entity.func_180425_c());
               target = entity.func_184204_a(position.getDimension());
            }

            if (target != null) {
               target.func_70012_b(position.getX(), position.getY(), position.getZ(), position.getYaw(), position.getPitch());
            }

            return target;
         }
      } else {
         return entity;
      }
   }

   public static void transferPlayerToDimension(EntityPlayerMP player, int dimension) {
      int oldDim = player.field_71093_bK;
      MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
      WorldServer oldWorld = mcServer.func_71218_a(player.field_71093_bK);
      player.field_71093_bK = dimension;
      WorldServer newWorld = mcServer.func_71218_a(player.field_71093_bK);
      player.field_71135_a.func_147359_a(new SPacketRespawn(player.field_71093_bK, newWorld.func_175659_aa(), newWorld.func_72912_H().func_76067_t(), player.field_71134_c.func_73081_b()));
      oldWorld.func_72973_f(player);
      player.field_70128_L = false;
      transferEntityToWorld(player, oldDim, oldWorld, newWorld);
      mcServer.func_184103_al().func_72375_a(player, oldWorld);
      player.field_71135_a.func_147364_a(player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70177_z, player.field_70125_A);
      player.field_71134_c.func_73080_a(newWorld);
      mcServer.func_184103_al().func_72354_b(player, newWorld);
      mcServer.func_184103_al().func_72385_f(player);
      Iterator iterator = player.func_70651_bq().iterator();

      while(iterator.hasNext()) {
         PotionEffect potioneffect = (PotionEffect)iterator.next();
         player.field_71135_a.func_147359_a(new SPacketEntityEffect(player.func_145782_y(), potioneffect));
      }

      player.func_71016_p();
      player.field_71135_a.func_147359_a(new SPacketSetExperience(player.field_71106_cc, player.field_71067_cb, player.field_71068_ca));
      FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
   }

   public static void transferEntityToWorld(Entity entity, int oldDim, WorldServer oldWorld, WorldServer newWorld) {
      WorldProvider pOld = oldWorld.field_73011_w;
      WorldProvider pNew = newWorld.field_73011_w;
      double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
      double d0 = entity.field_70165_t * moveFactor;
      double d1 = entity.field_70161_v * moveFactor;
      d0 = (double)MathHelper.func_76125_a((int)d0, -29999872, 29999872);
      d1 = (double)MathHelper.func_76125_a((int)d1, -29999872, 29999872);
      if (entity.func_70089_S()) {
         entity.func_70012_b(d0, entity.field_70163_u, d1, entity.field_70177_z, entity.field_70125_A);
         int i = MathHelper.func_76128_c(entity.field_70165_t);
         int j = MathHelper.func_76128_c(entity.field_70163_u) - 1;
         int k = MathHelper.func_76128_c(entity.field_70161_v);
         entity.func_70012_b((double)i, (double)j, (double)k, entity.field_70177_z, 0.0F);
         newWorld.func_72838_d(entity);
         newWorld.func_72866_a(entity, false);
      }

      entity.func_70029_a(newWorld);
   }
}
