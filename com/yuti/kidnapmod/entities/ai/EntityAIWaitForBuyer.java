package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.loaders.ItemTask;
import com.yuti.kidnapmod.loaders.common.GagTalkLoader;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.Random;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class EntityAIWaitForBuyer extends EntityAIBase {
   private EntityKidnapper kidnapper;
   private Timer timer;

   public EntityAIWaitForBuyer(EntityKidnapper kidnapper) {
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
      } else if (!this.kidnapper.hasSlaves()) {
         return false;
      } else {
         PlayerBindState state = this.kidnapper.getSlave();
         if (state != null && state.getPlayer() != null && state.getPlayer().func_70089_S()) {
            if (!state.isForSell()) {
               return false;
            } else if (!state.isBoundAndGagged()) {
               return false;
            } else {
               return !this.kidnapper.isWaitingForJobToBeCompleted();
            }
         } else {
            return false;
         }
      }
   }

   public void func_75249_e() {
      PlayerBindState state = this.kidnapper.getSlave();
      if (state != null) {
         String name = state.getPlayer().func_70005_c_();
         BlockPos pos = this.kidnapper.func_180425_c();
         EntityPlayer player = state.getPlayer();
         if (player != null) {
            this.kidnapper.talkTo(player, "We're going to wait here for a buyer.");
            ItemTask taskSale = state.getKidnapperSellTask();
            if (taskSale != null) {
               String kidnapperName = TextFormatting.GOLD + "*" + this.kidnapper.getCurrentName() + "*";
               String finalMessage = "I'm selling " + name + " for " + taskSale.toString() + TextFormatting.WHITE + " here";
               if (this.kidnapper.hasGaggingEffect()) {
                  GagTalkLoader loader = GagTalkLoader.getInstance();
                  if (loader != null) {
                     finalMessage = loader.gagTalkConvertor(player.field_70170_p, finalMessage);
                  }
               }

               this.kidnapper.field_70170_p.func_73046_m().func_184103_al().func_148539_a(new TextComponentString(kidnapperName + TextFormatting.WHITE + " : " + finalMessage + " : " + pos.func_177958_n() + " " + pos.func_177956_o() + " " + pos.func_177952_p()));
            }
         }
      }

      Random rand = new Random();
      this.timer = new Timer(300 + rand.nextInt(180));
      super.func_75249_e();
   }

   public void func_75251_c() {
      PlayerBindState state = this.kidnapper.getSlave();
      if (state != null) {
         state.resetSale();
      }

      this.timer = null;
   }

   public void func_75246_d() {
      if (this.timer != null && this.kidnapper.hasSlaves() && this.kidnapper.getSlave().isForSell()) {
         int secondsRemaining = this.timer.getSecondsRemaining();
         if (secondsRemaining <= 0) {
            this.timer = null;
            PlayerBindState state = this.kidnapper.getSlave();
            this.kidnapper.setGetOutState(true);
            state.resetSale();
         }
      }

   }

   public boolean func_75253_b() {
      return !this.func_75250_a() ? false : super.func_75253_b();
   }
}
