package com.yuti.kidnapmod.util.handlers.extrainventoryevents;

import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItemCapabilities;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemExtraBondageInventoryEventHandler {
   private static ResourceLocation capabilityResourceLocation = new ResourceLocation("knapm", "extra_kidnap_items_cap");

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public void itemCapabilityAttach(AttachCapabilitiesEvent<ItemStack> event) {
      final ItemStack stack = (ItemStack)event.getObject();
      if (!stack.func_190926_b() && stack.func_77973_b() instanceof IExtraBondageItem && !stack.hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null) && !event.getCapabilities().values().stream().anyMatch((c) -> {
         return c.hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null);
      })) {
         event.addCapability(capabilityResourceLocation, new ICapabilityProvider() {
            public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
               return capability == ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE;
            }

            @Nullable
            public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
               return capability == ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE ? ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE.cast((IExtraBondageItem)stack.func_77973_b()) : null;
            }
         });
      }
   }
}
