package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIAvoidKidnapper extends EntityAIAvoidEntity<EntityPlayer> {
   private EntityDamsel damsel;

   public EntityAIAvoidKidnapper(EntityDamsel entityIn, Class<EntityPlayer> classToAvoidIn, float avoidDistanceIn, double farSpeedIn, double nearSpeedIn) {
      super(entityIn, classToAvoidIn, avoidDistanceIn, farSpeedIn, nearSpeedIn);
      this.damsel = entityIn;
   }

   public boolean func_75250_a() {
      return !this.damsel.isTiedUp() && !this.damsel.isBlindfolded() && !this.damsel.hasCollar() && super.func_75250_a();
   }

   public boolean func_75253_b() {
      return !this.damsel.isTiedUp() && !this.damsel.isBlindfolded() && !this.damsel.hasCollar() && super.func_75253_b();
   }

   public void func_75251_c() {
      super.func_75251_c();
      this.damsel.func_70661_as().func_75499_g();
   }
}
