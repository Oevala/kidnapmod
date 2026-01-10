package com.yuti.kidnapmod.util.handlers;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageContainer;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItemCapabilities;
import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilitiesRegistryHandler {
   public static void registerCapabilities() {
      CapabilityManager.INSTANCE.register(IExtraBondageItemHandler.class, new ExtraBondageItemCapabilities.CapabilityExtraBondage(), ExtraBondageContainer.class);
      CapabilityManager.INSTANCE.register(IExtraBondageItem.class, new ExtraBondageItemCapabilities.CapabilityExtraBondageItemStorage(), () -> {
         return new ExtraBondageItem(ExtraBondageItemType.BONDAGEEXTRA);
      });
   }
}
