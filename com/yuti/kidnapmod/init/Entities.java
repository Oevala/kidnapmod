package com.yuti.kidnapmod.init;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.entities.EntityInvisibleSlaveTransporter;
import com.yuti.kidnapmod.entities.EntityKidnapBomb;
import com.yuti.kidnapmod.entities.EntityRopeArrow;
import com.yuti.kidnapmod.entities.classic.EntityClassicDamsel;
import com.yuti.kidnapmod.entities.classic.EntityClassicKidnapper;
import com.yuti.kidnapmod.entities.elite.EntityKidnapperElite;
import com.yuti.kidnapmod.entities.guests.EntityBirdy;
import com.yuti.kidnapmod.entities.guests.EntityGuestDamsel;
import com.yuti.kidnapmod.entities.guests.EntityGuestKidnapper;
import com.yuti.kidnapmod.util.EntitiesReference;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class Entities {
   public static void registerEntities() {
      registerEntity("kidnapper", EntityClassicKidnapper.class, EntitiesReference.ENTITY_KIDNAPPER_ID, 50, 11437146, 0);
      registerEntity("invisibleslavetransport", EntityInvisibleSlaveTransporter.class, EntitiesReference.ENTITY_TRANSPORT_ID, 50, 11437146, 0);
      registerEntity("ropearrow", EntityRopeArrow.class, EntitiesReference.ENTITY_ROPE_ARROW, 50, 11437146, 0);
      registerEntity("kidnap_bomb", EntityKidnapBomb.class, EntitiesReference.ENTITY_KIDNAP_BOMB, 50, 11437146, 0);
      registerEntity("kidnapper_elite", EntityKidnapperElite.class, EntitiesReference.ENTITY_ELITE_ID, 50, 11437146, 0);
      registerEntity("damsel", EntityClassicDamsel.class, EntitiesReference.ENTITY_DAMSEL_ID, 50, 11437146, 0);
      registerEntity("birdy", EntityBirdy.class, EntitiesReference.ENTITY_BIRDY_ID, 50, 11437146, 0);
      registerEntity("damsel_guest", EntityGuestDamsel.class, EntitiesReference.ENTITY_GUEST_DAMSEL_ID, 50, 11437146, 0);
      registerEntity("kidnapper_guest", EntityGuestKidnapper.class, EntitiesReference.ENTITY_GUEST_KIDNAPPER_ID, 50, 11437146, 0);
   }

   private static void registerEntity(String name, Class<? extends Entity> entity, int id, int range, int color1, int color2) {
      EntityRegistry.registerModEntity(new ResourceLocation("knapm:" + name), entity, name, id, KidnapModMain.instance, range, 1, true, color1, color2);
   }
}
