package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.entity.ai.EntityAISwimming;

public class EntityAISwimingTiedUp extends EntityAISwimming {
   private EntityDamsel damsel;

   public EntityAISwimingTiedUp(EntityDamsel entityIn) {
      super(entityIn);
      this.damsel = entityIn;
   }

   public boolean func_75250_a() {
      return !this.damsel.isTiedUp() && !this.damsel.isBlindfolded() && super.func_75250_a();
   }

   public boolean func_75253_b() {
      return !this.damsel.isTiedUp() && !this.damsel.isBlindfolded() && super.func_75253_b();
   }
}
