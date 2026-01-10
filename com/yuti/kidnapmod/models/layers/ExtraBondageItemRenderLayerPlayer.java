package com.yuti.kidnapmod.models.layers;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemHelper;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import com.yuti.kidnapmod.items.ItemCollar;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ExtraBondageItemRenderLayerPlayer extends ExtraBondageItemRenderLayer<EntityPlayer> {
   public ExtraBondageItemRenderLayerPlayer(RenderLivingBase<?> rendererIn) {
      super(rendererIn);
   }

   public void doRenderLayer(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      if (player.func_70660_b(MobEffects.field_76441_p) == null) {
         IExtraBondageItemHandler inv = ExtraBondageItemHelper.getExtraBondageItemHandler(player);

         for(int i = 0; i < inv.getSlots() - 1; ++i) {
            this.renderItem(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, inv.getStackInSlot(i));
         }

      }
   }

   protected void renderItem(EntityPlayer entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof IExtraBondageItem) {
         boolean forceNormalRender = false;
         KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)entity.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
         if (cap != null && !cap.allowDynamicRestraints) {
            forceNormalRender = true;
         }

         IExtraBondageItem itembondage = (IExtraBondageItem)stack.func_77973_b();
         if (entity != null) {
            if (itembondage instanceof ItemCollar && cap != null && !cap.displayCollar) {
               return;
            }

            super.renderItem(entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, stack, forceNormalRender);
         }
      }

   }
}
