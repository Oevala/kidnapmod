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
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEarplugs extends ItemKidnapWearable implements IExtraBondageItem {
   public ItemEarplugs(String name, ExtraBondageMaterial materialIn) {
      super(name, materialIn);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 16;
      ModItems.EARPLUGS_LIST.add(this);
   }

   public ExtraBondageItemType getType(ItemStack itemstack) {
      return ExtraBondageItemType.EARPLUGS;
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getBondageModel(EntityLivingBase entity, ItemStack stack, ExtraBondageItemType slot, ModelBiped modelBase) {
      return ModelsUtils.MODEL_EARPLUGS;
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
               if (!((I_Kidnapped)state).hasEarplugs()) {
                  ((I_Kidnapped)state).putEarsPlugsOn(stack);
                  stack.func_190918_g(1);
               } else {
                  ItemStack oldEarplugs = ((I_Kidnapped)state).replaceEarPlugs(stack);
                  if (oldEarplugs != null) {
                     stack.func_190918_g(1);
                     ((I_Kidnapped)state).kidnappedDropItem(oldEarplugs);
                  }
               }
            }
         }

         return true;
      }
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      tooltip.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.earplugs", new Object[0]));
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
   }
}
