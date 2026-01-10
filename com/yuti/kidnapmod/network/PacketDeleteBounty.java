package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.data.BountiesData;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketDeleteBounty implements IMessage {
   private boolean messageValid;
   private String bountyId;

   public PacketDeleteBounty() {
      this.messageValid = false;
   }

   public PacketDeleteBounty(String bountyId) {
      this.bountyId = bountyId;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.bountyId = ByteBufUtils.readUTF8String(buf);
      } catch (IndexOutOfBoundsException var3) {
         Utils.getLogger().catching(var3);
         return;
      }

      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         ByteBufUtils.writeUTF8String(buf, this.bountyId);
      }
   }

   public static class Handler implements IMessageHandler<PacketDeleteBounty, IMessage> {
      public IMessage onMessage(PacketDeleteBounty message, MessageContext ctx) {
         if (!message.messageValid) {
            return null;
         } else {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).func_152344_a(() -> {
               this.processMessage(message, ctx);
            });
            return null;
         }
      }

      void processMessage(PacketDeleteBounty message, MessageContext ctx) {
         try {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            BountiesData data = BountiesData.get(player.field_70170_p);
            data.cancelBounty(player, message.bountyId);
         } catch (Exception var5) {
            Utils.getLogger().catching(var5);
         }

      }
   }
}
