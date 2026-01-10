package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.models.render.ModelsUtils;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlindfold extends ItemKidnapWearable implements IExtraBondageItem, IAdjustable, IHasBlindingEffect, ItemUsuableOnRestrainedPlayer {
   public ItemBlindfold(String name, ExtraBondageMaterial materialIn) {
      super(name, materialIn);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 16;
      ModItems.BLINDFOLD_LIST.add(this);
   }

   public ExtraBondageItemType getType(ItemStack itemstack) {
      return ExtraBondageItemType.BLINDFOLD;
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getBondageModel(EntityLivingBase entity, ItemStack stack, ExtraBondageItemType slot, ModelBiped modelBase) {
      KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)entity.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
      float adjustBlindfold = 0.0F;
      if (cap != null) {
         adjustBlindfold = cap.getAdjustement(this.getRegistryName().toString());
      }

      if (entity instanceof EntityDamsel) {
         EntityDamsel damsel = (EntityDamsel)entity;
         adjustBlindfold = damsel.getBlindfoldsAdjustement();
      }

      return ModelsUtils.getAdjustement(adjustBlindfold);
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      if (player.field_70170_p.field_72995_K) {
         return true;
      } else {
         if (target instanceof EntityPlayer || target instanceof I_Kidnapped) {
            I_Kidnapped state = null;
            if (target instanceof EntityPlayer) {
               EntityPlayer targetPlayer = (EntityPlayer)target;
               state = PlayerBindState.getInstance(targetPlayer);
            } else {
               state = (I_Kidnapped)target;
            }

            if (state != null && ((I_Kidnapped)state).isTiedUp()) {
               if (!((I_Kidnapped)state).isBlindfolded()) {
                  ((I_Kidnapped)state).putBlindfoldOn(stack);
                  stack.func_190918_g(1);
               } else {
                  ItemStack oldBlindfold = ((I_Kidnapped)state).replaceBlindfold(stack);
                  if (oldBlindfold != null) {
                     stack.func_190918_g(1);
                     ((I_Kidnapped)state).kidnappedDropItem(oldBlindfold);
                  }
               }
            }
         }

         return true;
      }
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      tooltip.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.blindfold", new Object[0]));
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
   }

   public boolean canBeAdjusted() {
      return true;
   }
}
