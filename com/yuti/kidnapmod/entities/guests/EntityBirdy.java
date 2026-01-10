package com.yuti.kidnapmod.entities.guests;

import com.yuti.kidnapmod.entities.classic.EntityClassicKidnapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityBirdy extends EntityClassicKidnapper {
   public EntityBirdy(World world) {
      super(world);
   }

   public String getDamselName() {
      return "Birdy";
   }

   public ResourceLocation getSkin() {
      return new ResourceLocation("knapm", "textures/entity/special/birdy.png");
   }

   public boolean hasSmallArms() {
      return true;
   }
}
