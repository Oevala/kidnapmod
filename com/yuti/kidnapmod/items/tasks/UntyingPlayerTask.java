package com.yuti.kidnapmod.items.tasks;

import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketRestrainState;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class UntyingPlayerTask extends UntyingTask {
   private PlayerBindState playerTarget;

   public UntyingPlayerTask(EntityPlayer player, PlayerBindState target, int seconds) {
      super(player, target, seconds);
      this.playerTarget = target;
   }

   public synchronized void update() {
      super.update();
      PlayerStateTask restrainState = this.playerTarget.getRestrainedState();
      if (restrainState != null) {
         restrainState.update(this.getState());
         PacketHandler.INSTANCE.sendTo(new PacketRestrainState(this.seconds - this.getState(), this.seconds, false), (EntityPlayerMP)this.playerTarget.getPlayer());
      }

      if (this.timer.getSecondsRemaining() <= 0) {
         this.target.dropBondageItems();
         this.target.untie();
         this.target.free();
         this.stop();
         PacketHandler.INSTANCE.sendTo(new PacketRestrainState(-1, this.seconds, false), (EntityPlayerMP)this.playerTarget.getPlayer());
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
