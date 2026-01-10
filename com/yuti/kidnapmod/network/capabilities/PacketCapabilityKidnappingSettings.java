package com.yuti.kidnapmod.network.capabilities;

import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketCapabilityKidnappingSettings implements IMessage {
   public int playerId;
   public NBTTagCompound settingsCap;

   public PacketCapabilityKidnappingSettings(int playerId, KidnapSettingsCapabilities cap) {
      this.playerId = playerId;
      this.settingsCap = cap.serializeNBT();
   }

   public PacketCapabilityKidnappingSettings() {
   }

   public void fromBytes(ByteBuf buf) {
      this.playerId = buf.readInt();
      this.settingsCap = ByteBufUtils.readTag(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.playerId);
      ByteBufUtils.writeTag(buf, this.settingsCap);
   }

   public static class ClientHandler implements IMessageHandler<PacketCapabilityKidnappingSettings, IMessage> {
      @SideOnly(Side.CLIENT)
      public IMessage onMessage(PacketCapabilityKidnappingSettings message, MessageContext ctx) {
         Minecraft.func_71410_x().func_152344_a(new SchedulePacketCapabilitiesKidnappingSettings(message));
         return null;
      }
   }
}
