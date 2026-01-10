package com.yuti.kidnapmod.network.extrainventory;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenNormalInventory implements IMessage, IMessageHandler<PacketOpenNormalInventory, IMessage> {
   public void toBytes(ByteBuf buffer) {
   }

   public void fromBytes(ByteBuf buffer) {
   }

   public IMessage onMessage(PacketOpenNormalInventory message, final MessageContext ctx) {
      IThreadListener mainThread = (WorldServer)ctx.getServerHandler().field_147369_b.field_70170_p;
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            ctx.getServerHandler().field_147369_b.field_71070_bA.func_75134_a(ctx.getServerHandler().field_147369_b);
            ctx.getServerHandler().field_147369_b.field_71070_bA = ctx.getServerHandler().field_147369_b.field_71069_bz;
         }
      });
      return null;
   }
}
