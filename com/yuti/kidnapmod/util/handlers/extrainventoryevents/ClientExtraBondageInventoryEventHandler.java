package com.yuti.kidnapmod.util.handlers.extrainventoryevents;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItemCapabilities;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientExtraBondageInventoryEventHandler {
   @SubscribeEvent
   public void tooltipEvent(ItemTooltipEvent event) {
      if (!event.getItemStack().func_190926_b() && event.getItemStack().hasCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null)) {
         IExtraBondageItem bondageItem = (IExtraBondageItem)event.getItemStack().getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null);
         ExtraBondageItemType bt = bondageItem.getType(event.getItemStack());
         event.getToolTip().add(TextFormatting.GOLD + I18n.func_135052_a("name." + bt, new Object[0]));
      }

   }
}
