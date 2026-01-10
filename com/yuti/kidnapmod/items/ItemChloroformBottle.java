package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.UtilsParameters;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemChloroformBottle extends Item implements IHasModel, ItemUsuableOnRestrainedPlayer {
   public ItemChloroformBottle(String name) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 1;
      this.func_77656_e(9);
      ModItems.ITEMS.add(this);
   }

   public ActionResult<ItemStack> func_77659_a(World worldIn, EntityPlayer player, EnumHand hand) {
      if (!worldIn.field_72995_K && player != null) {
         boolean enabled = UtilsParameters.getChloroformEnabled(worldIn);
         if (enabled) {
            EnumHand ragHand;
            if (hand == EnumHand.MAIN_HAND) {
               ragHand = EnumHand.OFF_HAND;
            } else {
               ragHand = EnumHand.MAIN_HAND;
            }

            ItemStack chloroStack = player.func_184586_b(hand);
            if (ragHand != null && chloroStack != null && chloroStack.func_77973_b() instanceof ItemChloroformBottle) {
               ItemStack ragStack = player.func_184586_b(ragHand);
               if (ragStack != null && !ragStack.func_190926_b() && ragStack.func_77973_b() instanceof ItemRag && !ItemRag.isWet(ragStack)) {
                  player.func_184811_cZ().func_185145_a(this, 50);
                  Utils.playSound(player, SoundEvents.field_187624_K, 1.0F);
                  ItemRag.setWet(worldIn, ragStack, true);
                  chloroStack.func_77972_a(1, player);
                  return new ActionResult(EnumActionResult.SUCCESS, chloroStack);
               }
            }
         }
      }

      return super.func_77659_a(worldIn, player, hand);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      tooltip.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.chloroform_bottle", new Object[0]));
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
   }

   public boolean func_77616_k(ItemStack stack) {
      return false;
   }

   public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
      return false;
   }

   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return false;
   }
}
