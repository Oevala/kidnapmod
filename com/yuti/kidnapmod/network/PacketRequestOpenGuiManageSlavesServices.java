package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.gui.GuiServicesManagment;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketRequestOpenGuiManageSlavesServices implements IMessage {
   private boolean messageValid;
   private ItemStack collar;
   private boolean isKidnapper;
   private Position position;
   private String targetUUID;

   public PacketRequestOpenGuiManageSlavesServices() {
      this.messageValid = false;
   }

   public PacketRequestOpenGuiManageSlavesServices(ItemStack collar, boolean isKidnapper, Position position, String targetUUID) {
      if (collar == null) {
         this.collar = ItemStack.field_190927_a;
      } else {
         this.collar = collar.func_77946_l();
      }

      this.isKidnapper = isKidnapper;
      this.position = position;
      this.targetUUID = targetUUID;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.collar = ByteBufUtils.readItemStack(buf);
         this.isKidnapper = buf.readBoolean();
         NBTTagCompound posTag = ByteBufUtils.readTag(buf);
         if (posTag != null) {
            this.position = new Position(posTag);
         }

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
         buf.writeBoolean(this.isKidnapper);
         NBTTagCompound posTag = new NBTTagCompound();
         if (this.position != null) {
            this.position.writeToNBT(posTag);
         }

         ByteBufUtils.writeTag(buf, posTag);
         ByteBufUtils.writeUTF8String(buf, this.targetUUID);
      }
   }

   public static class Handler implements IMessageHandler<PacketRequestOpenGuiManageSlavesServices, IMessage> {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PacketRequestOpenGuiManageSlavesServices message, MessageContext ctx) {
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
      void processMessage(PacketRequestOpenGuiManageSlavesServices message, MessageContext ctx) {
         try {
            Minecraft.func_71410_x().func_147108_a(new GuiServicesManagment(message.collar, message.isKidnapper, message.position, message.targetUUID));
         } catch (Exception var4) {
            Utils.getLogger().catching(var4);
         }

      }
   }
}
