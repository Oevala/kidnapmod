package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.data.BountiesData;
import com.yuti.kidnapmod.data.Bounty;
import com.yuti.kidnapmod.gui.GuiBounties;
import com.yuti.kidnapmod.util.Utils;
import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSendBounties implements IMessage {
   private boolean messageValid;
   private List<Bounty> bounties;
   private boolean isOp;

   public PacketSendBounties() {
      this.messageValid = false;
   }

   public PacketSendBounties(List<Bounty> bounties, boolean isOp) {
      this.bounties = bounties;
      this.isOp = isOp;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      try {
         this.bounties = BountiesData.deserializeBountiesFromNBT("bounties", ByteBufUtils.readTag(buf));
         this.isOp = buf.readBoolean();
      } catch (IndexOutOfBoundsException var3) {
         Utils.getLogger().catching(var3);
         return;
      }

      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      if (this.messageValid) {
         ByteBufUtils.writeTag(buf, BountiesData.serializeBountiesToNBT("bounties", this.bounties, new NBTTagCompound()));
         buf.writeBoolean(this.isOp);
      }
   }

   public static class Handler implements IMessageHandler<PacketSendBounties, IMessage> {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PacketSendBounties message, MessageContext ctx) {
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
      void processMessage(PacketSendBounties message, MessageContext ctx) {
         try {
            if (message.bounties != null) {
               Minecraft.func_71410_x().func_147108_a(new GuiBounties(message.bounties, message.isOp));
            }
         } catch (Exception var4) {
            Utils.getLogger().catching(var4);
         }

      }
   }
}
