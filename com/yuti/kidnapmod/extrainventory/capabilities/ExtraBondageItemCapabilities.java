package com.yuti.kidnapmod.extrainventory.capabilities;

import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class ExtraBondageItemCapabilities {
   @CapabilityInject(IExtraBondageItemHandler.class)
   public static final Capability<IExtraBondageItemHandler> CAPABILITY_EXTRA_BONDAGE_ITEMS = null;
   @CapabilityInject(IExtraBondageItem.class)
   public static final Capability<IExtraBondageItem> CAPABILITY_EXTRA_ITEM_BONDAGE = null;

   public static class CapabilityExtraBondageItemStorage implements IStorage<IExtraBondageItem> {
      public NBTBase writeNBT(Capability<IExtraBondageItem> capability, IExtraBondageItem instance, EnumFacing side) {
         return null;
      }

      public void readNBT(Capability<IExtraBondageItem> capability, IExtraBondageItem instance, EnumFacing side, NBTBase nbt) {
      }
   }

   public static class CapabilityExtraBondage<T extends IExtraBondageItemHandler> implements IStorage<IExtraBondageItemHandler> {
      public NBTBase writeNBT(Capability<IExtraBondageItemHandler> capability, IExtraBondageItemHandler instance, EnumFacing side) {
         return null;
      }

      public void readNBT(Capability<IExtraBondageItemHandler> capability, IExtraBondageItemHandler instance, EnumFacing side, NBTBase nbt) {
      }
   }
}
