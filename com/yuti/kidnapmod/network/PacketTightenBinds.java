package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTightenBinds implements IMessage {
   private boolean messageValid = false;

   public void fromBytes(ByteBuf buf) {
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         ;
      }
   }

   public static class Handler implements IMessageHandler<PacketTightenBinds, IMessage> {
      public IMessage onMessage(PacketTightenBinds message, MessageContext ctx) {
         if (!message.messageValid) {
            return null;
         } else {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).func_152344_a(() -> {
               this.processMessage(message, ctx);
            });
            return null;
         }
      }

      void processMessage(PacketTightenBinds message, MessageContext ctx) {
         try {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            PlayerBindState playerState = PlayerBindState.getInstance(player);
            if (playerState != null && !playerState.isTiedUp() && player.field_70170_p != null && player.func_180425_c() != null) {
               List<EntityPlayerMP> players = PlayerBindState.getPlayerAround(player.field_70170_p, player.func_180425_c(), 2.0D);
               Iterator var6 = players.iterator();

               while(var6.hasNext()) {
                  EntityPlayer target = (EntityPlayer)var6.next();
                  if (target != null) {
                     PlayerBindState state = PlayerBindState.getInstance(target);
                     if (state != null) {
                        state.tighten(player);
                     }
                  }
               }
            }
         } catch (Exception var9) {
            Utils.getLogger().catching(var9);
         }

      }
   }
}
