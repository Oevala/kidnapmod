package com.yuti.kidnapmod.entities.guests;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.entities.ai.EntityAIRestrainingPlayerClassic;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityGuestKidnapper extends EntityKidnapper {
   public EntityGuestKidnapper(World world) {
      super(world);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      GuestsKidnappers kidnapper = GuestsKidnappers.getRandomKidnapper();
      this.field_70180_af.func_187214_a(DAMSEL_NAME, kidnapper.getName());
      this.field_70180_af.func_187214_a(SMALL_ARMS, kidnapper.hasSmallArms());
      this.func_96094_a((String)this.field_70180_af.func_187225_a(DAMSEL_NAME));
   }

   protected void func_184651_r() {
      super.func_184651_r();
      this.field_70714_bg.func_75776_a(2, new EntityAIRestrainingPlayerClassic(this));
   }

   public void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111266_c).func_111128_a(7.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.25D);
   }

   public ResourceLocation getSkin() {
      return new ResourceLocation("knapm", "textures/entity/special/guests/kidnappers/" + this.getDamselName().toLowerCase() + ".png");
   }
}
