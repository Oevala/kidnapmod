package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.gui.GuiBondageItemsManagment;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketRequestOpenGuiTakeOffBondageItem implements IMessage {
   private boolean messageValid;
   private boolean gag;
   private boolean blindfold;
   private boolean earplugs;
   private boolean collar;
   private boolean knives;
   private boolean clothes;
   private String targetUUID;

   public PacketRequestOpenGuiTakeOffBondageItem() {
      this.messageValid = false;
   }

   public PacketRequestOpenGuiTakeOffBondageItem(boolean gag, boolean blindfold, boolean earplugs, boolean collar, boolean knives, boolean clothes, String targetUUID) {
      this.gag = gag;
      this.blindfold = blindfold;
      this.earplugs = earplugs;
      this.collar = collar;
      this.knives = knives;
      this.clothes = clothes;
      this.targetUUID = targetUUID;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.gag = buf.readBoolean();
         this.blindfold = buf.readBoolean();
         this.earplugs = buf.readBoolean();
         this.collar = buf.readBoolean();
         this.knives = buf.readBoolean();
         this.clothes = buf.readBoolean();
         this.targetUUID = ByteBufUtils.readUTF8String(buf);
      } catch (IndexOutOfBoundsException var3) {
         Utils.getLogger().catching(var3);
         return;
      }

      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         buf.writeBoolean(this.gag);
         buf.writeBoolean(this.blindfold);
         buf.writeBoolean(this.earplugs);
         buf.writeBoolean(this.collar);
         buf.writeBoolean(this.knives);
         buf.writeBoolean(this.clothes);
         ByteBufUtils.writeUTF8String(buf, this.targetUUID);
      }
   }

   public static class Handler implements IMessageHandler<PacketRequestOpenGuiTakeOffBondageItem, IMessage> {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PacketRequestOpenGuiTakeOffBondageItem message, MessageContext ctx) {
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
      void processMessage(PacketRequestOpenGuiTakeOffBondageItem message, MessageContext ctx) {
         try {
            Minecraft.func_71410_x().func_147108_a(new GuiBondageItemsManagment(message.gag, message.blindfold, message.earplugs, message.collar, message.knives, message.clothes, message.targetUUID));
         } catch (Exception var4) {
            Utils.getLogger().catching(var4);
         }

      }
   }
}
