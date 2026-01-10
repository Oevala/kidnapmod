package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.models.render.ModelsUtils;
import com.yuti.kidnapmod.models.render.RenderUtilsDamsel;
import com.yuti.kidnapmod.models.render.RenderUtilsPlayer;
import com.yuti.kidnapmod.models.render.TiedUpRenderDamsel;
import com.yuti.kidnapmod.models.render.TiedUpRenderPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEnclosed extends ItemBind {
   public ItemEnclosed(String name, ExtraBondageMaterial materialIn, int resistance, int mergePercent) {
      super(name, materialIn, resistance, mergePercent);
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getBondageModel(EntityLivingBase entity, ItemStack stack, ExtraBondageItemType slot, ModelBiped modelBase) {
      return ModelsUtils.RESTRAINT_ENCLOSED;
   }

   @SideOnly(Side.CLIENT)
   public TiedUpRenderPlayer getTiedUpPlayerRenderer() {
      return RenderUtilsPlayer.playerTiedUprenderEnclosed;
   }

   @SideOnly(Side.CLIENT)
   public TiedUpRenderDamsel getTiedUpDamselRenderer() {
      return RenderUtilsDamsel.damselTiedUprenderEnclosed;
   }
}
