package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketManageSlaveService implements IMessage {
   private boolean messageValid;
   private ItemStack collar;
   private String targetUUID;

   public PacketManageSlaveService() {
      this.messageValid = false;
   }

   public PacketManageSlaveService(ItemStack collar, String targetUUID) {
      if (collar == null) {
         this.collar = ItemStack.field_190927_a;
      } else {
         this.collar = collar.func_77946_l();
      }

      this.targetUUID = targetUUID;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.collar = ByteBufUtils.readItemStack(buf);
         this.targetUUID = ByteBufUtils.readUTF8String(buf);
      } catch (IndexOutOfBoundsException var3) {
         Utils.getLogger().catching(var3);
         return;
      }

      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         ByteBufUtils.writeItemStack(buf, this.collar);
         ByteBufUtils.writeUTF8String(buf, this.targetUUID);
      }
   }

   public static class Handler implements IMessageHandler<PacketManageSlaveService, IMessage> {
      public IMessage onMessage(PacketManageSlaveService message, MessageContext ctx) {
         if (!message.messageValid) {
            return null;
         } else {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).func_152344_a(() -> {
               this.processMessage(message, ctx);
            });
            return null;
         }
      }

      void processMessage(PacketManageSlaveService message, MessageContext ctx) {
         try {
            EntityPlayer player = ctx.getServerHandler().field_147369_b;
            if (player != null) {
               PlayerBindState state = PlayerBindState.getInstance(player);
               if (state != null && !state.isTiedUp() && player.field_70170_p != null) {
                  MinecraftServer server = player.field_70170_p.func_73046_m();
                  if (server != null && message.targetUUID != null) {
                     UUID targetID = UUID.fromString(message.targetUUID);
                     Entity target = server.func_175576_a(targetID);
                     if (target != null && target instanceof EntityDamsel) {
                        EntityDamsel damsel = (EntityDamsel)target;
                        UUID playerUUID = player.func_110124_au();
                        if (playerUUID != null && damsel != null && damsel.isMaster(player)) {
                           UUID currentEditor = damsel.getCurrentEditor();
                           if (damsel.hasCollar() && currentEditor != null && currentEditor.equals(playerUUID) && message.collar != null && message.collar.func_77973_b() instanceof ItemCollar) {
                              ItemStack currentCollar = damsel.getCurrentCollar();
                              if (currentCollar != null) {
                                 ItemStack collarCopy = currentCollar.func_77946_l();
                                 ItemCollar collarItem = (ItemCollar)message.collar.func_77973_b();
                                 if (collarItem != null) {
                                    collarCopy = collarItem.updateCollarStackFromNetwork(message.collar, collarCopy, damsel.getCurrentEditionPosition());
                                    damsel.reloadCollar(collarCopy);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         } catch (Exception var14) {
            Utils.getLogger().catching(var14);
         }

      }
   }
}
