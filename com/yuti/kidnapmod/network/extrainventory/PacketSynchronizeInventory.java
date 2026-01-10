package com.yuti.kidnapmod.network.extrainventory;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemHelper;
import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSynchronizeInventory implements IMessage {
   int playerId;
   byte slot = 0;
   ItemStack bodnageItem;

   public PacketSynchronizeInventory() {
   }

   public PacketSynchronizeInventory(EntityPlayer p, int slot, ItemStack bodnageItem) {
      this.slot = (byte)slot;
      this.bodnageItem = bodnageItem;
      this.playerId = p.func_145782_y();
   }

   public void toBytes(ByteBuf buffer) {
      buffer.writeInt(this.playerId);
      buffer.writeByte(this.slot);
      ByteBufUtils.writeItemStack(buffer, this.bodnageItem);
   }

   public void fromBytes(ByteBuf buffer) {
      this.playerId = buffer.readInt();
      this.slot = buffer.readByte();
      this.bodnageItem = ByteBufUtils.readItemStack(buffer);
   }

   public static class Handler implements IMessageHandler<PacketSynchronizeInventory, IMessage> {
      public IMessage onMessage(final PacketSynchronizeInventory message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(new Runnable() {
            public void run() {
               World world = KidnapModMain.proxy.getClientWorld();
               if (world != null) {
                  Entity p = world.func_73045_a(message.playerId);
                  if (p != null && p instanceof EntityPlayer) {
                     IExtraBondageItemHandler extraBondage = ExtraBondageItemHelper.getExtraBondageItemHandler((EntityPlayer)p);
                     extraBondage.setStackInSlot(message.slot, message.bodnageItem);
                  }

               }
            }
         });
         return null;
      }
   }
}
