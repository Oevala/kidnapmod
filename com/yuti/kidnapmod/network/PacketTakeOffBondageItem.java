package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketTakeOffBondageItem implements IMessage {
   private boolean messageValid;
   private int bondageItem;
   private String targetUUID;

   public PacketTakeOffBondageItem() {
      this.messageValid = false;
   }

   public PacketTakeOffBondageItem(int bondageItemIn, String targetUUID) {
      this.bondageItem = bondageItemIn;
      this.targetUUID = targetUUID;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.bondageItem = buf.readInt();
         this.targetUUID = ByteBufUtils.readUTF8String(buf);
      } catch (IndexOutOfBoundsException var3) {
         Utils.getLogger().catching(var3);
         return;
      }

      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         buf.writeInt(this.bondageItem);
         ByteBufUtils.writeUTF8String(buf, this.targetUUID);
      }
   }

   public static class Handler implements IMessageHandler<PacketTakeOffBondageItem, IMessage> {
      public IMessage onMessage(PacketTakeOffBondageItem message, MessageContext ctx) {
         if (!message.messageValid) {
            return null;
         } else {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).func_152344_a(() -> {
               this.processMessage(message, ctx);
            });
            return null;
         }
      }

      void processMessage(PacketTakeOffBondageItem message, MessageContext ctx) {
         try {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            if (player != null) {
               PlayerBindState state = PlayerBindState.getInstance(player);
               if (state != null && !state.isTiedUp() && player.field_70170_p != null) {
                  MinecraftServer server = player.field_70170_p.func_73046_m();
                  if (server != null && message.targetUUID != null) {
                     UUID targetID = UUID.fromString(message.targetUUID);
                     Entity target = server.func_175576_a(targetID);
                     if (target != null && (target instanceof EntityPlayer || target instanceof I_Kidnapped)) {
                        I_Kidnapped targetState = null;
                        if (target instanceof EntityPlayer) {
                           EntityPlayer targetPlayer = (EntityPlayer)target;
                           targetState = PlayerBindState.getInstance(targetPlayer);
                        } else {
                           targetState = (I_Kidnapped)target;
                        }

                        if (targetState != null && state.canInteractWith((I_Kidnapped)targetState)) {
                           ((I_Kidnapped)targetState).takeBondageItemBy(state, message.bondageItem);
                        }
                     }
                  }
               }
            }
         } catch (Exception var10) {
            Utils.getLogger().catching(var10);
         }

      }
   }
}
