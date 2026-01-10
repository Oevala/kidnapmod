package com.yuti.kidnapmod.network;

import com.yuti.kidnapmod.network.capabilities.PacketCapabilityKidnappingSettings;
import com.yuti.kidnapmod.network.extrainventory.PacketOpenExtraBondageInventory;
import com.yuti.kidnapmod.network.extrainventory.PacketOpenNormalInventory;
import com.yuti.kidnapmod.network.extrainventory.PacketSynchronizeInventory;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
   public static SimpleNetworkWrapper INSTANCE;
   private static int ID = 0;

   private static int nextID() {
      return ID++;
   }

   public static void registerMessages(String channelName) {
      INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
      INSTANCE.registerMessage(PacketRestrainState.Handler.class, PacketRestrainState.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketUntying.Handler.class, PacketUntying.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketTying.Handler.class, PacketTying.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketPlaceTrap.Handler.class, PacketPlaceTrap.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketSendBounties.Handler.class, PacketSendBounties.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketStruggleServer.Handler.class, PacketStruggleServer.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketAskForBounties.Handler.class, PacketAskForBounties.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketTightenBinds.Handler.class, PacketTightenBinds.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketDeleteBounty.Handler.class, PacketDeleteBounty.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketOpenExtraBondageInventory.class, PacketOpenExtraBondageInventory.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketOpenNormalInventory.class, PacketOpenNormalInventory.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketSynchronizeInventory.Handler.class, PacketSynchronizeInventory.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketTakeOffBondageItem.Handler.class, PacketTakeOffBondageItem.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketRequestOpenGuiTakeOffBondageItem.Handler.class, PacketRequestOpenGuiTakeOffBondageItem.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketCapabilityKidnappingSettings.ClientHandler.class, PacketCapabilityKidnappingSettings.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketRequestOpenGuiManageSlavesServices.Handler.class, PacketRequestOpenGuiManageSlavesServices.class, nextID(), Side.CLIENT);
      INSTANCE.registerMessage(PacketManageSlaveService.Handler.class, PacketManageSlaveService.class, nextID(), Side.SERVER);
      INSTANCE.registerMessage(PacketJoinServer.Handler.class, PacketJoinServer.class, nextID(), Side.CLIENT);
   }
}
