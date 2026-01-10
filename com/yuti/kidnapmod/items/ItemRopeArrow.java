package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.entities.EntityRopeArrow;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRopeArrow extends ItemArrow implements IHasModel, IItemBondageItemHolder {
   public ItemRopeArrow(String name) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      ModItems.ITEMS.add(this);
   }

   public void func_77624_a(ItemStack stack, World player, List<String> addList, ITooltipFlag advanced) {
      addList.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.rope_arrow", new Object[0]));
      if (stack != null && stack.func_77973_b() instanceof ItemRopeArrow) {
         ItemRopeArrow itemArrow = (ItemRopeArrow)stack.func_77973_b();
         ItemStack bind = itemArrow.getBind(stack);
         ItemStack gag = itemArrow.getGag(stack);
         ItemStack blindfold = itemArrow.getBlindfold(stack);
         ItemStack earplugs = itemArrow.getEarplugs(stack);
         ItemStack collar = itemArrow.getCollar(stack);
         ItemStack clothes = itemArrow.getClothes(stack);
         if (bind != null) {
            addList.add(ChatFormatting.GOLD + bind.func_82833_r());
         }

         if (gag != null) {
            addList.add(ChatFormatting.GOLD + gag.func_82833_r());
         }

         if (blindfold != null) {
            addList.add(ChatFormatting.GOLD + blindfold.func_82833_r());
         }

         if (earplugs != null) {
            addList.add(ChatFormatting.GOLD + earplugs.func_82833_r());
         }

         if (collar != null) {
            addList.add(ChatFormatting.GOLD + collar.func_82833_r());
         }

         if (clothes != null) {
            addList.add(ChatFormatting.GOLD + clothes.func_82833_r());
         }
      }

   }

   public EntityArrow func_185052_a(World world, ItemStack itemstack, EntityLivingBase shooter) {
      if (shooter instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)shooter;
         ItemStack bow = player.func_184607_cu();
         if (bow != null && bow.func_77973_b() instanceof ItemBow) {
            return new EntityRopeArrow(world, shooter, itemstack, this.isInfinite(itemstack, bow, player));
         }
      }

      return new EntityRopeArrow(world, shooter);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }

   public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
      return EnchantmentHelper.func_77506_a(Enchantments.field_185312_x, bow) > 0;
   }

   private static void setBondageElement(ItemStack stack, ItemStack bondageStack, String key, Class<?> cls) {
      Utils.setBondageElement(stack, bondageStack, key, ItemRopeArrow.class, cls);
   }

   private static ItemStack getBondageElement(ItemStack stack, String key, Class<?> cls) {
      return Utils.getBondageElement(stack, key, ItemRopeArrow.class, cls);
   }

   public void setBind(ItemStack stack, ItemStack bind) {
      setBondageElement(stack, bind, "bind", ItemBind.class);
   }

   public ItemStack getBind(ItemStack stack) {
      return getBondageElement(stack, "bind", ItemBind.class);
   }

   public void setGag(ItemStack stack, ItemStack gag) {
      setBondageElement(stack, gag, "gag", ItemGag.class);
   }

   public ItemStack getGag(ItemStack stack) {
      return getBondageElement(stack, "gag", ItemGag.class);
   }

   public void setBlindfold(ItemStack stack, ItemStack blindfold) {
      setBondageElement(stack, blindfold, "blindfold", ItemBlindfold.class);
   }

   public ItemStack getBlindfold(ItemStack stack) {
      return getBondageElement(stack, "blindfold", ItemBlindfold.class);
   }

   public void setEarplugs(ItemStack stack, ItemStack earplugs) {
      setBondageElement(stack, earplugs, "earplugs", ItemEarplugs.class);
   }

   public ItemStack getEarplugs(ItemStack stack) {
      return getBondageElement(stack, "earplugs", ItemEarplugs.class);
   }

   public void setClothes(ItemStack stack, ItemStack clothes) {
      setBondageElement(stack, clothes, "clothes", ItemClothes.class);
   }

   public ItemStack getClothes(ItemStack stack) {
      return getBondageElement(stack, "clothes", ItemClothes.class);
   }

   public void setCollar(ItemStack stack, ItemStack collar) {
      setBondageElement(stack, collar, "collar", ItemCollar.class);
   }

   public ItemStack getCollar(ItemStack stack) {
      return getBondageElement(stack, "collar", ItemCollar.class);
   }

   public boolean func_77636_d(ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof ItemRopeArrow) {
         ItemRopeArrow itemArrow = (ItemRopeArrow)stack.func_77973_b();
         ItemStack bind = itemArrow.getBind(stack);
         ItemStack gag = itemArrow.getGag(stack);
         ItemStack blindfold = itemArrow.getBlindfold(stack);
         ItemStack earplugs = itemArrow.getEarplugs(stack);
         ItemStack collar = itemArrow.getCollar(stack);
         ItemStack clothes = itemArrow.getClothes(stack);
         return bind != null || gag != null || blindfold != null || earplugs != null || collar != null || clothes != null || super.func_77636_d(stack);
      } else {
         return super.func_77636_d(stack);
      }
   }
}
