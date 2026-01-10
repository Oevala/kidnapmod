package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.data.BountiesData;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAskForBounties implements IMessage {
   private boolean messageValid = false;

   public void fromBytes(ByteBuf buf) {
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         ;
      }
   }

   public static class Handler implements IMessageHandler<PacketAskForBounties, IMessage> {
      public IMessage onMessage(PacketAskForBounties message, MessageContext ctx) {
         if (!message.messageValid) {
            return null;
         } else {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).func_152344_a(() -> {
               this.processMessage(message, ctx);
            });
            return null;
         }
      }

      void processMessage(PacketAskForBounties message, MessageContext ctx) {
         try {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            BountiesData data = BountiesData.get(player.field_70170_p);
            boolean isOpe = Utils.isOpe(player);
            PacketHandler.INSTANCE.sendTo(new PacketSendBounties(data.getBounties(player.field_70170_p), isOpe), (EntityPlayerMP)player);
         } catch (Exception var6) {
            Utils.getLogger().catching(var6);
         }

      }
   }
}
