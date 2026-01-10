package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.images.DynamicOnlineTexture;
import com.yuti.kidnapmod.util.images.DynamicOnlineTextureManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemKidnapWearable extends Item implements IHasModel, IExtraBondageItem, IHasVariants, ItemUsuableOnRestrainedPlayer {
   protected List<ExtraBondageMaterial> variants = new ArrayList();
   private ExtraBondageMaterial material;

   public ItemKidnapWearable(String name, ExtraBondageMaterial materialIn) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.material = materialIn;
      this.func_77656_e(-1);
      ModItems.ITEMS.add(this);
      ModItems.WEARABLE.add(this);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }

   public boolean isRepairable() {
      return false;
   }

   public boolean func_77645_m() {
      return false;
   }

   public boolean canRender() {
      return true;
   }

   public ExtraBondageMaterial getExtraBondageMaterial(ItemStack stack) {
      if (this.variants != null && !this.variants.isEmpty() && stack != null && !stack.func_190926_b()) {
         int meta = stack.func_77960_j();
         if (meta >= 0 && meta < this.variants.size()) {
            ExtraBondageMaterial variant = (ExtraBondageMaterial)this.variants.get(meta);
            if (variant != null) {
               return variant;
            }
         }
      }

      return this.material;
   }

   public ExtraBondageMaterial getExtraBondageMaterial() {
      return this.material;
   }

   public void registerVariants() {
   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
      if (player != null && !player.field_70170_p.field_72995_K) {
         this.playSound(itemstack, player);
      }

   }

   public void playSound(ItemStack itemstack, EntityLivingBase entity) {
      if (entity != null && !entity.field_70170_p.field_72995_K) {
         World world = entity.field_70170_p;
         world.func_184133_a((EntityPlayer)null, entity.func_180425_c(), this.material.getSound(), SoundCategory.AMBIENT, 1.0F, 1.0F);
      }

   }

   public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
      return enchantment.equals(Enchantments.field_190941_k) ? true : super.canApplyAtEnchantingTable(stack, enchantment);
   }

   public int getItemsCount() {
      return 1;
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
      if (stack.func_77942_o()) {
         String textureUrl = this.getDynamicTextureUrl(stack);
         if (textureUrl != null) {
            tooltip.add(ChatFormatting.LIGHT_PURPLE + "Dynamic texture");
         }
      }

   }

   public ItemStack removeTag(ItemStack stack, String key) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      if (nbt.func_74764_b(key)) {
         nbt.func_82580_o(key);
         stack.func_77982_d(nbt);
      }

      return stack;
   }

   public ItemStack setDynamicTextureUrl(ItemStack stack, String url) {
      if (stack != null && url != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            nbt.func_74778_a("dynamicTexture", url);
            stack.func_77982_d(nbt);
         }
      }

      return stack;
   }

   public ItemStack removeDynamicTextureUrl(ItemStack stack) {
      return this.removeTag(stack, "dynamicTexture");
   }

   public String getDynamicTextureUrl(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      return nbt != null && nbt.func_74764_b("dynamicTexture") ? nbt.func_74779_i("dynamicTexture") : null;
   }

   @SideOnly(Side.CLIENT)
   public DynamicOnlineTexture getDynamicTexture(ItemStack stack) {
      String url = this.getDynamicTextureUrl(stack);
      return url != null ? DynamicOnlineTextureManager.loadUrl(url) : null;
   }
}
