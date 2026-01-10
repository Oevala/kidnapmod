package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.util.time.Timer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIWaitForJobToBeCompleted extends EntityAIBase {
   private EntityKidnapper kidnapper;
   private Timer timer;

   public EntityAIWaitForJobToBeCompleted(EntityKidnapper kidnapper) {
      this.kidnapper = kidnapper;
   }

   public boolean func_75250_a() {
      if (this.kidnapper.isTiedUp()) {
         return false;
      } else if (this.kidnapper.hasCollar()) {
         return false;
      } else if (!this.kidnapper.func_70089_S()) {
         return false;
      } else if (this.kidnapper.func_70090_H()) {
         return false;
      } else {
         return this.kidnapper.isWaitingForJobToBeCompleted();
      }
   }

   public boolean func_75253_b() {
      if (!this.func_75250_a()) {
         return false;
      } else {
         if (this.timer.getSecondsRemaining() <= 0) {
            if (!this.kidnapper.shouldStillWaitForJob()) {
               this.kidnapper.func_184611_a(EnumHand.MAIN_HAND, new ItemStack(Items.field_190931_a, 0));
               return false;
            }

            this.timer = new Timer(30);
         }

         return super.func_75253_b();
      }
   }

   public void func_75249_e() {
      this.timer = new Timer(30);
      super.func_75249_e();
   }
}
