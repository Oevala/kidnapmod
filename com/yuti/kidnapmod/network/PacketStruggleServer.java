package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStruggleServer implements IMessage {
   private boolean messageValid = false;

   public void fromBytes(ByteBuf buf) {
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         ;
      }
   }

   public static class Handler implements IMessageHandler<PacketStruggleServer, IMessage> {
      public IMessage onMessage(PacketStruggleServer message, MessageContext ctx) {
         if (!message.messageValid) {
            return null;
         } else {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).func_152344_a(() -> {
               this.processMessage(message, ctx);
            });
            return null;
         }
      }

      void processMessage(PacketStruggleServer message, MessageContext ctx) {
         try {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               state.struggle();
            }
         } catch (Exception var5) {
            Utils.getLogger().catching(var5);
         }

      }
   }
}
