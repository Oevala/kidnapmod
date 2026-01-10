package com.yuti.kidnapmod.entities.guests;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityGuestDamsel extends EntityDamsel {
   public EntityGuestDamsel(World world) {
      super(world);
      GuestsDamsels damsel = GuestsDamsels.getRandomDamsel();
      this.field_70180_af.func_187214_a(DAMSEL_NAME, damsel.getName());
      this.field_70180_af.func_187214_a(SMALL_ARMS, damsel.hasSmallArms());
      this.func_96094_a((String)this.field_70180_af.func_187225_a(DAMSEL_NAME));
   }

   public void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111266_c).func_111128_a(7.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.25D);
   }

   public ResourceLocation getSkin() {
      return new ResourceLocation("knapm", "textures/entity/special/guests/damsels/" + this.getDamselName().toLowerCase() + ".png");
   }
}
