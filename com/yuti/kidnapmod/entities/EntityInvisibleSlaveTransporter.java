package com.yuti.kidnapmod.entities;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.time.Timer;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class EntityInvisibleSlaveTransporter extends EntityPig {
   private Timer timerNotRide;

   public EntityInvisibleSlaveTransporter(World world) {
      super(world);
   }

   protected void func_184651_r() {
   }

   public void setUpAndSpawnToSlave(PlayerBindState slave) {
      EntityPlayer player = slave.getPlayer();
      this.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
      player.func_130014_f_().func_72838_d(this);
      this.func_70659_e(0.0F);
      this.func_184224_h(true);
      this.func_70873_a(-100000);
      this.func_174810_b(true);
      this.func_82142_c(true);
      this.func_181013_g(this.field_70761_aq + 30.0F);
   }

   public boolean shouldRiderSit() {
      return false;
   }

   public synchronized void check() {
      if (!this.field_70128_L) {
         this.func_174810_b(true);
         this.func_82142_c(true);
         if (this.func_70874_b() >= -1000) {
            this.func_70873_a(-100000);
         }

         if (this.func_184207_aI()) {
            this.timerNotRide = null;
         } else if (this.timerNotRide != null) {
            if (this.timerNotRide.getSecondsRemaining() <= 0) {
               if (this.func_110167_bD()) {
                  this.func_145779_a(Items.field_151058_ca, 1);
               }

               this.func_70106_y();
            }
         } else {
            this.timerNotRide = new Timer(10);
         }
      }

   }

   public void func_110160_i(boolean sendPacket, boolean dropLead) {
      super.func_110160_i(sendPacket, false);
   }
}
