package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class EntityAIEnslavingPlayer extends EntityAIBase {
   private EntityKidnapper kidnapper;

   public EntityAIEnslavingPlayer(EntityKidnapper kidnapper) {
      this.kidnapper = kidnapper;
   }

   public boolean func_75250_a() {
      if (!this.kidnapper.isTiedUp() && !this.kidnapper.isBlindfolded()) {
         if (this.kidnapper.hasCollar() && !this.kidnapper.isSlaveKidnappingModeEnabled()) {
            return false;
         } else if (this.kidnapper.isChloroformed()) {
            return false;
         } else if (this.kidnapper.isGetOutState()) {
            return false;
         } else if (!this.kidnapper.func_70089_S()) {
            return false;
         } else if (this.kidnapper.func_70090_H()) {
            return false;
         } else if (this.kidnapper.hasSlaves()) {
            return false;
         } else if (!this.kidnapper.isCloseToTarget()) {
            return false;
         } else if (this.kidnapper.getTarget() == null) {
            return false;
         } else if (!this.kidnapper.isSuitableTarget(this.kidnapper.getTarget())) {
            return false;
         } else {
            PlayerBindState state = PlayerBindState.getInstance(this.kidnapper.getTarget());
            return state.isBoundAndGagged();
         }
      } else {
         return false;
      }
   }

   public boolean func_75253_b() {
      return !this.func_75250_a() ? false : super.func_75253_b();
   }

   public void func_75249_e() {
      this.kidnapper.clearOldLeashedEntities();
      this.kidnapper.func_184611_a(EnumHand.MAIN_HAND, new ItemStack(Items.field_151058_ca));
      this.kidnapper.func_184611_a(EnumHand.OFF_HAND, new ItemStack(Items.field_190931_a));
      super.func_75249_e();
   }

   public void func_75251_c() {
      if (this.kidnapper.hasSlaves() || !this.kidnapper.isCloseToTarget()) {
         this.kidnapper.setTarget((EntityPlayer)null);
      }

   }

   public void func_75246_d() {
      if (!this.kidnapper.hasSlaves()) {
         PlayerBindState playerState = PlayerBindState.getInstance(this.kidnapper.getTarget());
         if (playerState.isSlave() && playerState.isTiedToPole()) {
            playerState.transferSlaveryTo(this.kidnapper);
         } else {
            playerState.getEnslavedBy(this.kidnapper);
            this.kidnapper.setTarget((EntityPlayer)null);
         }
      }

   }
}
