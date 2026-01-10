package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;

public class EntityAIWatchClosestBlindfolded extends EntityAIWatchClosest {
   private EntityDamsel damsel;

   public EntityAIWatchClosestBlindfolded(EntityDamsel entityIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
      super(entityIn, watchTargetClass, maxDistance);
      this.damsel = entityIn;
   }

   public boolean func_75250_a() {
      return !this.damsel.isBlindfolded() && super.func_75250_a();
   }

   public boolean func_75253_b() {
      return !this.damsel.isBlindfolded() && super.func_75253_b();
   }
}
