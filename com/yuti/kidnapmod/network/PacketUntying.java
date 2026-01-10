package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.items.tasks.PlayerStateTask;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketUntying implements IMessage {
   private boolean messageValid;
   private int stateInfo;
   private int maxState;

   public PacketUntying() {
      this.messageValid = false;
   }

   public PacketUntying(int stateInfo, int maxState) {
      this.stateInfo = stateInfo;
      this.maxState = maxState;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.stateInfo = buf.readInt();
         this.maxState = buf.readInt();
      } catch (IndexOutOfBoundsException var3) {
         Utils.getLogger().catching(var3);
         return;
      }

      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         buf.writeInt(this.stateInfo);
         buf.writeInt(this.maxState);
      }
   }

   public static class Handler implements IMessageHandler<PacketUntying, IMessage> {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PacketUntying message, MessageContext ctx) {
         if (!message.messageValid) {
            return null;
         } else {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).func_152344_a(() -> {
               this.processMessage(message, ctx);
            });
            return null;
         }
      }

      @SideOnly(Side.CLIENT)
      void processMessage(PacketUntying message, MessageContext ctx) {
         try {
            int state = message.stateInfo;
            EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
            if (player == null) {
               return;
            }

            PlayerBindState playerState = PlayerBindState.getInstance(player);
            if (playerState == null) {
               return;
            }

            PlayerStateTask untyingTask = playerState.getClientUntyingTask();
            if (untyingTask == null || untyingTask.isOutdated()) {
               untyingTask = new PlayerStateTask(message.maxState);
               untyingTask.update(state);
               playerState.setClientUntyingTask(untyingTask);
            }

            if (untyingTask.getState() != state) {
               untyingTask.update(state);
            }
         } catch (Exception var7) {
            Utils.getLogger().catching(var7);
         }

      }
   }
}
