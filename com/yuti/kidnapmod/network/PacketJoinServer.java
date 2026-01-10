package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.util.UtilsClient;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketJoinServer implements IMessage {
   private boolean messageValid = false;

   public void fromBytes(ByteBuf buf) {
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         ;
      }
   }

   public static class Handler implements IMessageHandler<PacketJoinServer, IMessage> {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PacketJoinServer message, MessageContext ctx) {
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
      void processMessage(PacketJoinServer message, MessageContext ctx) {
         UtilsClient.displayModInformationToPlayer();
      }
   }
}
