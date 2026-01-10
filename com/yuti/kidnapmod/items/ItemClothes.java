package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.models.render.ModelsUtils;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemClothes extends ItemKidnapWearable implements IExtraBondageItem {
   public ItemClothes(String name, ExtraBondageMaterial materialIn) {
      super(name, materialIn);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 16;
      ModItems.CLOTHES_LIST.add(this);
   }

   public ExtraBondageItemType getType(ItemStack itemstack) {
      return ExtraBondageItemType.CLOTHES;
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getBondageModel(EntityLivingBase entity, ItemStack stack, ExtraBondageItemType slot, ModelBiped modelBase) {
      return ModelsUtils.MODEL_CLOTHES;
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

            if (state != null && ((I_Kidnapped)state).isTiedUp() && ((I_Kidnapped)state).canChangeClothes(player)) {
               if (!((I_Kidnapped)state).hasClothes()) {
                  ((I_Kidnapped)state).putClothesOn(stack);
                  stack.func_190918_g(1);
               } else {
                  ItemStack oldClothes = ((I_Kidnapped)state).replaceClothes(stack);
                  if (oldClothes != null) {
                     stack.func_190918_g(1);
                     ((I_Kidnapped)state).kidnappedDropItem(oldClothes);
                  }
               }
            }
         }

         return true;
      }
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      tooltip.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.clothes", new Object[0]));
      if (this.getDynamicTextureUrl(stack) == null) {
         tooltip.add(ChatFormatting.GRAY + "Need to be binded to an URL. Run /dynamicclothes");
      }

      if (this.isFullSkinEnabled(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Full skin");
      }

      if (this.shouldForceSmallArms(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Small arms");
      }

      if (!this.isWearerHeadLayerEnabled(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Wearer's head layer disabled");
      }

      if (!this.isWearerBodyLayerEnabled(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Wearer's body layer disabled");
      }

      if (!this.isWearerLeftArmLayerEnabled(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Wearer's left arm layer disabled");
      }

      if (!this.isWearerRightArmLayerEnabled(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Wearer's right arm layer disabled");
      }

      if (!this.isWearerLeftLegLayerEnabled(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Wearer's left leg layer disabled");
      }

      if (!this.isWearerRightLegLayerEnabled(stack)) {
         tooltip.add(ChatFormatting.LIGHT_PURPLE + "Wearer's right leg layer disabled");
      }

      super.func_77624_a(stack, worldIn, tooltip, flagIn);
   }

   public ItemStack enableFullSkin(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("fullskin", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack disableFullSkin(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("fullskin", false);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isFullSkinEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("fullskin") ? nbt.func_74767_n("fullskin") : false;
   }

   public ItemStack enableForceSmallArms(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("smallarms", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack disableForceSmallArms(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("smallarms", false);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean shouldForceSmallArms(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("smallarms") ? nbt.func_74767_n("smallarms") : false;
   }

   public ItemStack setWearerHeadLayer(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("headlayer", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isWearerHeadLayerEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("headlayer") ? nbt.func_74767_n("headlayer") : true;
   }

   public ItemStack setWearerBodyLayer(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("bodylayer", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isWearerBodyLayerEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("bodylayer") ? nbt.func_74767_n("bodylayer") : true;
   }

   public ItemStack setWearerLeftArmLayer(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("leftarmlayer", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isWearerLeftArmLayerEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("leftarmlayer") ? nbt.func_74767_n("leftarmlayer") : true;
   }

   public ItemStack setWearerRightArmLayer(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("rightarmlayer", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isWearerRightArmLayerEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("rightarmlayer") ? nbt.func_74767_n("rightarmlayer") : true;
   }

   public ItemStack setWearerLeftLegLayer(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("leftleglayer", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isWearerLeftLegLayerEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("leftleglayer") ? nbt.func_74767_n("leftleglayer") : true;
   }

   public ItemStack setWearerRightLegLayer(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("rightleglayer", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isWearerRightLegLayerEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("rightleglayer") ? nbt.func_74767_n("rightleglayer") : true;
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return enchantment.equals(Enchantments.field_190941_k) ? false : super.canApplyAtEnchantingTable(stack, enchantment);
   }
}
