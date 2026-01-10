package com.yuti.kidnapmod.extrainventory.capabilities;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.util.images.DynamicOnlineTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExtraBondageItem implements IExtraBondageItem {
   private ExtraBondageItemType baubleType;

   public ExtraBondageItem(ExtraBondageItemType type) {
      this.baubleType = type;
   }

   public ExtraBondageItemType getType(ItemStack itemstack) {
      return this.baubleType;
   }

   public ModelBiped getBondageModel(EntityLivingBase entity, ItemStack stack, ExtraBondageItemType slot, ModelBiped modelBase) {
      return null;
   }

   public ExtraBondageMaterial getExtraBondageMaterial(ItemStack stack) {
      return null;
   }

   public boolean canRender() {
      return false;
   }

   public ExtraBondageMaterial getExtraBondageMaterial() {
      return null;
   }

   public ItemStack setDynamicTextureUrl(ItemStack stack, String url) {
      return null;
   }

   public ItemStack removeDynamicTextureUrl(ItemStack stack) {
      return null;
   }

   public String getDynamicTextureUrl(ItemStack stack) {
      return null;
   }

   @SideOnly(Side.CLIENT)
   public DynamicOnlineTexture getDynamicTexture(ItemStack stack) {
      return null;
   }
}
