package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityDamsel;
import net.minecraft.entity.ai.EntityAIPanic;

public class EntityAIPanicTiedUp extends EntityAIPanic {
   private EntityDamsel damsel;

   public EntityAIPanicTiedUp(EntityDamsel creature, double speedIn) {
      super(creature, speedIn);
      this.damsel = creature;
   }

   public boolean func_75250_a() {
      return this.damsel.canPannick() && super.func_75250_a();
   }

   public boolean func_75253_b() {
      return this.damsel.canPannick() && super.func_75253_b();
   }
}
