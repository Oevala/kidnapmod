package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;

public class EntityAILookForPlayer extends EntityAIBase {
   private EntityKidnapper kidnapper;
   private int searchRadius;

   public EntityAILookForPlayer(EntityKidnapper kidnapper, int searchRadiusIn) {
      this.kidnapper = kidnapper;
      this.searchRadius = searchRadiusIn;
   }

   public boolean func_75250_a() {
      if (!this.kidnapper.isTiedUp() && !this.kidnapper.isBlindfolded()) {
         if (this.kidnapper.isChloroformed()) {
            return false;
         } else if (this.kidnapper.hasCollar() && !this.kidnapper.isSlaveKidnappingModeEnabled()) {
            return false;
         } else if (this.kidnapper.isGetOutState()) {
            return false;
         } else if (!this.kidnapper.func_70089_S()) {
            return false;
         } else if (this.kidnapper.func_70090_H()) {
            return false;
         } else if (this.kidnapper.hasSlaves()) {
            return false;
         } else if (this.kidnapper.isCloseToTarget()) {
            return false;
         } else {
            return !this.kidnapper.isWaitingForJobToBeCompleted();
         }
      } else {
         return false;
      }
   }

   public boolean func_75253_b() {
      if (!this.kidnapper.isTiedUp() && !this.kidnapper.isBlindfolded()) {
         if (this.kidnapper.isChloroformed()) {
            return false;
         } else if (this.kidnapper.isCloseToTarget()) {
            return false;
         } else {
            return this.kidnapper.getTarget() != null ? this.kidnapper.isSuitableTarget(this.kidnapper.getTarget()) : super.func_75253_b();
         }
      } else {
         return false;
      }
   }

   public void func_75251_c() {
      if (!this.kidnapper.isCloseToTarget() || !this.kidnapper.isSuitableTarget(this.kidnapper.getTarget())) {
         this.kidnapper.setTarget((EntityPlayer)null);
      }

      this.kidnapper.func_70661_as().func_75499_g();
   }

   public void func_75246_d() {
      if (this.kidnapper.getTarget() != null && this.kidnapper.isCloseToTarget()) {
         this.kidnapper.func_70661_as().func_75499_g();
      } else {
         EntityPlayer potentialTarget = this.kidnapper.getClosestSuitablePlayer(this.searchRadius);
         if (potentialTarget != null) {
            this.kidnapper.setTarget(potentialTarget);
            Path toPlayer = this.kidnapper.func_70661_as().func_179680_a(this.kidnapper.getTarget().func_180425_c());
            this.kidnapper.func_70661_as().func_75484_a(toPlayer, 1.1D);
            this.kidnapper.func_70661_as().func_188554_j();
         }

      }
   }

   public void func_75249_e() {
      this.kidnapper.clearOldLeashedEntities();
      this.kidnapper.setUpHoldBind();
      this.kidnapper.setUpHoldGag();
      super.func_75249_e();
   }
}
