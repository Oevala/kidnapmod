package com.yuti.kidnapmod.extrainventory;

import com.yuti.kidnapmod.util.images.DynamicOnlineTexture;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IExtraBondageItem {
   ExtraBondageItemType getType(ItemStack var1);

   default void onWornTick(ItemStack itemstack, EntityLivingBase player) {
   }

   default void onEquipped(ItemStack itemstack, EntityLivingBase player) {
   }

   default void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
   }

   default boolean canEquip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   default boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   default boolean willAutoSync(ItemStack itemstack, EntityLivingBase player) {
      return true;
   }

   @SideOnly(Side.CLIENT)
   ModelBiped getBondageModel(EntityLivingBase var1, ItemStack var2, ExtraBondageItemType var3, ModelBiped var4);

   @SideOnly(Side.CLIENT)
   ExtraBondageMaterial getExtraBondageMaterial(ItemStack var1);

   @SideOnly(Side.CLIENT)
   ExtraBondageMaterial getExtraBondageMaterial();

   @SideOnly(Side.CLIENT)
   boolean canRender();

   ItemStack setDynamicTextureUrl(ItemStack var1, String var2);

   ItemStack removeDynamicTextureUrl(ItemStack var1);

   String getDynamicTextureUrl(ItemStack var1);

   @SideOnly(Side.CLIENT)
   DynamicOnlineTexture getDynamicTexture(ItemStack var1);
}
