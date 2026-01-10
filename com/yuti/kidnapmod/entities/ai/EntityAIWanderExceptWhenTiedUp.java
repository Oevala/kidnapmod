package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;

public class EntityAIWanderExceptWhenTiedUp extends EntityAIWanderAvoidWater {
   private EntityDamsel damsel;

   public EntityAIWanderExceptWhenTiedUp(EntityDamsel damsel, double speed, float chance) {
      super(damsel, speed, chance);
      this.damsel = damsel;
   }

   public boolean func_75250_a() {
      if (this.damsel.isTiedUp()) {
         return false;
      } else {
         return this.damsel.hasCollar() ? false : super.func_75250_a();
      }
   }
}
