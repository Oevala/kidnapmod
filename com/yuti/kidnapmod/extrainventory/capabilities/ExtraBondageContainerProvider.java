package com.yuti.kidnapmod.extrainventory.capabilities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class ExtraBondageContainerProvider implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {
   private final ExtraBondageContainer container;

   public ExtraBondageContainerProvider(ExtraBondageContainer container) {
      this.container = container;
   }

   public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
      return capability == ExtraBondageItemCapabilities.CAPABILITY_EXTRA_BONDAGE_ITEMS;
   }

   public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
      return capability == ExtraBondageItemCapabilities.CAPABILITY_EXTRA_BONDAGE_ITEMS ? this.container : null;
   }

   public NBTTagCompound serializeNBT() {
      return this.container.serializeNBT();
   }

   public void deserializeNBT(NBTTagCompound nbt) {
      this.container.deserializeNBT(nbt);
   }
}
