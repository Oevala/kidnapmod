package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.util.time.Timer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.util.math.Vec3d;

public abstract class EntityAIWanderFastTimed extends EntityAIWanderAvoidWater {
   protected Timer timer;
   private int time;
   protected EntityKidnapper kidnapper;

   public EntityAIWanderFastTimed(EntityKidnapper entity, double speed, int time) {
      super(entity, speed);
      this.time = time;
      this.kidnapper = entity;
   }

   public boolean func_75250_a() {
      if (this.kidnapper.isTiedUp()) {
         return false;
      } else {
         Vec3d vec3d = this.func_190864_f();
         if (vec3d == null) {
            return false;
         } else {
            if (vec3d.field_72450_a > this.field_75455_b || vec3d.field_72449_c > this.field_75453_d) {
               this.field_75455_b = vec3d.field_72450_a;
               this.field_75456_c = vec3d.field_72448_b;
               this.field_75453_d = vec3d.field_72449_c;
               this.field_179482_g = false;
            }

            return true;
         }
      }
   }

   public boolean func_75253_b() {
      if (this.kidnapper.isTiedUp()) {
         return false;
      } else if (this.timer.getSecondsRemaining() <= 0) {
         this.actionTimeOut();
         return false;
      } else {
         return super.func_75253_b();
      }
   }

   public void func_75249_e() {
      if (this.timer == null) {
         this.actionOnResetTimer();
         this.timer = new Timer(this.time);
      }

      super.func_75249_e();
   }

   public void func_75251_c() {
      if (this.shouldResetTimer()) {
         this.timer = null;
      }

      this.field_75457_a.func_70661_as().func_75499_g();
      super.func_75251_c();
   }

   protected abstract void actionTimeOut();

   protected abstract boolean shouldResetTimer();

   protected abstract void actionOnResetTimer();
}
