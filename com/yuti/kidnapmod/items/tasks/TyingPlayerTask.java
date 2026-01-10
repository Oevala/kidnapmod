package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketRestrainState;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class TyingPlayerTask extends TyingTask {
   private PlayerBindState playerTarget;

   public TyingPlayerTask(ItemStack bind, PlayerBindState target, int seconds) {
      super(bind, target, seconds);
      this.playerTarget = target;
   }

   public synchronized void update() {
      super.update();
      PlayerStateTask restrainState = this.playerTarget.getRestrainedState();
      if (restrainState != null) {
         restrainState.update(this.getState());
         PacketHandler.INSTANCE.sendTo(new PacketRestrainState(this.getState(), restrainState.getMaxState()), (EntityPlayerMP)this.playerTarget.getPlayer());
      }

      if (this.timer.getSecondsRemaining() <= 0) {
         this.target.putBindOn(this.bind);
         this.stop();
         PacketHandler.INSTANCE.sendTo(new PacketRestrainState(-1, restrainState.getMaxState()), (EntityPlayerMP)this.playerTarget.getPlayer());
      }

   }

   public void setUpTargetState() {
      PlayerStateTask restrainState = this.playerTarget.getRestrainedState();
      if (restrainState == null || restrainState.isOutdated()) {
         restrainState = new PlayerStateTask(this.seconds);
         this.playerTarget.setRestrainedState(restrainState);
      }

   }
}
