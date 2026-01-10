package com.yuti.kidnapmod.network.extrainventory;

import com.yuti.kidnapmod.KidnapModMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenExtraBondageInventory implements IMessage, IMessageHandler<PacketOpenExtraBondageInventory, IMessage> {
   public void toBytes(ByteBuf buffer) {
   }

   public void fromBytes(ByteBuf buffer) {
   }

   public IMessage onMessage(PacketOpenExtraBondageInventory message, final MessageContext ctx) {
      IThreadListener mainThread = (WorldServer)ctx.getServerHandler().field_147369_b.field_70170_p;
      mainThread.func_152344_a(new Runnable() {
         public void run() {
            ctx.getServerHandler().field_147369_b.field_71070_bA.func_75134_a(ctx.getServerHandler().field_147369_b);
            ctx.getServerHandler().field_147369_b.openGui(KidnapModMain.instance, 0, ctx.getServerHandler().field_147369_b.field_70170_p, 0, 0, 0);
         }
      });
      return null;
   }
}
