package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIWanderFastGettingAway extends EntityAIWanderFastTimed {
   public EntityAIWanderFastGettingAway(EntityKidnapper entity, double speed, int time) {
      super(entity, speed, time);
   }

   public boolean func_75250_a() {
      if (this.kidnapper.hasCollar()) {
         return false;
      } else if (!this.kidnapper.isGetOutState()) {
         return false;
      } else {
         return !this.kidnapper.hasSlaves() ? false : super.func_75250_a();
      }
   }

   protected void actionTimeOut() {
      this.kidnapper.setGetOutState(false);
      this.timer = null;
   }

   protected boolean shouldResetTimer() {
      return false;
   }

   protected void actionOnResetTimer() {
      PlayerBindState state = this.kidnapper.getSlave();
      if (state != null) {
         EntityPlayer slave = state.getPlayer();
         if (slave != null) {
            this.kidnapper.talkTo(slave, "I can't find someone who could buy you...");
            this.kidnapper.talkTo(slave, "I'm leaving you a knife, have fun untying yourself");
            state.free();
            this.kidnapper.func_145778_a(ModItems.GOLD_KNIFE, 1, 3.0F);
         }
      }

   }
}
