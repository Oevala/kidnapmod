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

public class PacketRestrainState implements IMessage {
   private boolean messageValid;
   private int stateInfo;
   private int maxState;
   private boolean ascendant;

   public PacketRestrainState() {
      this.messageValid = false;
   }

   public PacketRestrainState(int stateInfo, int maxState, boolean ascendant) {
      this(stateInfo, maxState);
      this.ascendant = ascendant;
   }

   public PacketRestrainState(int stateInfo, int maxState) {
      this.stateInfo = stateInfo;
      this.maxState = maxState;
      this.ascendant = true;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.stateInfo = buf.readInt();
         this.maxState = buf.readInt();
         this.ascendant = buf.readBoolean();
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
         buf.writeBoolean(this.ascendant);
      }
   }

   public static class Handler implements IMessageHandler<PacketRestrainState, IMessage> {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PacketRestrainState message, MessageContext ctx) {
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
      void processMessage(PacketRestrainState message, MessageContext ctx) {
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

            PlayerStateTask restrainState = playerState.getRestrainedState();
            if (restrainState == null || restrainState.isOutdated()) {
               restrainState = new PlayerStateTask(message.maxState);
               restrainState.update(state);
               playerState.setRestrainedState(restrainState);
            }

            if (state == -1) {
               playerState.setRestrainedState((PlayerStateTask)null);
            } else {
               int actualState = restrainState.getState();
               if (actualState != state && (message.ascendant && actualState < state || !message.ascendant && actualState > state)) {
                  restrainState.update(state);
               }
            }
         } catch (Exception var8) {
            Utils.getLogger().catching(var8);
         }

      }
   }
}
