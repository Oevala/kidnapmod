package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.blocks.BlockTrappedBed;
import com.yuti.kidnapmod.init.ExtraRecipes;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.recipes.RecipeBondageColored;
import com.yuti.kidnapmod.tileentity.TileEntityTrappedBed;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemTrappedBed extends ItemBed implements IHasModel, IItemBondageItemHolder, IHasColorVariants {
   public ItemTrappedBed(String name) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      ModItems.ITEMS.add(this);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      ExtraRecipes.registerRecipe("recipe_bondage_colored_" + name, new RecipeBondageColored(this, this.getMaincolor()));
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (worldIn.field_72995_K) {
         return EnumActionResult.SUCCESS;
      } else if (facing != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState iblockstate = worldIn.func_180495_p(pos);
         Block block = iblockstate.func_177230_c();
         boolean flag = block.func_176200_f(worldIn, pos);
         if (!flag) {
            pos = pos.func_177984_a();
         }

         int i = MathHelper.func_76128_c((double)(player.field_70177_z * 4.0F / 360.0F) + 0.5D) & 3;
         EnumFacing enumfacing = EnumFacing.func_176731_b(i);
         BlockPos blockpos = pos.func_177972_a(enumfacing);
         ItemStack itemstack = player.func_184586_b(hand);
         if (player.func_175151_a(pos, facing, itemstack) && player.func_175151_a(blockpos, facing, itemstack)) {
            IBlockState iblockstate1 = worldIn.func_180495_p(blockpos);
            boolean flag1 = iblockstate1.func_177230_c().func_176200_f(worldIn, blockpos);
            boolean flag2 = flag || worldIn.func_175623_d(pos);
            boolean flag3 = flag1 || worldIn.func_175623_d(blockpos);
            if (flag2 && flag3 && worldIn.func_180495_p(pos.func_177977_b()).func_185896_q() && worldIn.func_180495_p(blockpos.func_177977_b()).func_185896_q()) {
               IBlockState iblockstate2 = ModBlocks.TRAPPED_BED.func_176223_P().func_177226_a(BlockTrappedBed.field_176471_b, false).func_177226_a(BlockTrappedBed.field_185512_D, enumfacing).func_177226_a(BlockTrappedBed.field_176472_a, EnumPartType.FOOT);
               worldIn.func_180501_a(pos, iblockstate2, 10);
               worldIn.func_180501_a(blockpos, iblockstate2.func_177226_a(BlockTrappedBed.field_176472_a, EnumPartType.HEAD), 10);
               SoundType soundtype = iblockstate2.func_177230_c().getSoundType(iblockstate2, worldIn, pos, player);
               worldIn.func_184133_a((EntityPlayer)null, pos, soundtype.func_185841_e(), SoundCategory.BLOCKS, (soundtype.func_185843_a() + 1.0F) / 2.0F, soundtype.func_185847_b() * 0.8F);
               TileEntity tileentity = worldIn.func_175625_s(blockpos);
               if (tileentity instanceof TileEntityTrappedBed) {
                  ((TileEntityTrappedBed)tileentity).func_193051_a(itemstack);
               }

               TileEntity tileentity1 = worldIn.func_175625_s(pos);
               if (tileentity1 instanceof TileEntityTrappedBed) {
                  ((TileEntityTrappedBed)tileentity1).func_193051_a(itemstack);
               }

               worldIn.func_175722_b(pos, block, false);
               worldIn.func_175722_b(blockpos, iblockstate1.func_177230_c(), false);
               if (player instanceof EntityPlayerMP) {
                  CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)player, pos, itemstack);
               }

               itemstack.func_190918_g(1);
               return EnumActionResult.SUCCESS;
            } else {
               return EnumActionResult.FAIL;
            }
         } else {
            return EnumActionResult.FAIL;
         }
      }
   }

   public void func_77624_a(ItemStack stack, World player, List<String> addList, ITooltipFlag advanced) {
      super.func_77624_a(stack, player, addList, advanced);
      addList.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.trapped_bed", new Object[0]));
      if (stack != null && stack.func_77973_b() instanceof ItemTrappedBed) {
         ItemTrappedBed itemTrappedBed = (ItemTrappedBed)stack.func_77973_b();
         ItemStack bind = itemTrappedBed.getBind(stack);
         ItemStack gag = itemTrappedBed.getGag(stack);
         ItemStack blindfold = itemTrappedBed.getBlindfold(stack);
         ItemStack earplugs = itemTrappedBed.getEarplugs(stack);
         ItemStack collar = itemTrappedBed.getCollar(stack);
         ItemStack clothes = itemTrappedBed.getClothes(stack);
         if (bind != null) {
            addList.add(ChatFormatting.DARK_RED + "Armed");
            addList.add(ChatFormatting.GOLD + bind.func_82833_r());
         } else {
            addList.add(ChatFormatting.GREEN + "Disarmed");
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

   private static void setBondageElement(ItemStack stack, ItemStack bondageStack, String key, Class<?> cls) {
      Utils.setBondageElement(stack, bondageStack, key, ItemTrappedBed.class, cls);
   }

   private static ItemStack getBondageElement(ItemStack stack, String key, Class<?> cls) {
      return Utils.getBondageElement(stack, key, ItemTrappedBed.class, cls);
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

   public void setCollar(ItemStack stack, ItemStack collar) {
      setBondageElement(stack, collar, "collar", ItemCollar.class);
   }

   public ItemStack getCollar(ItemStack stack) {
      return getBondageElement(stack, "collar", ItemCollar.class);
   }

   public void setClothes(ItemStack stack, ItemStack clothes) {
      setBondageElement(stack, clothes, "clothes", ItemClothes.class);
   }

   public ItemStack getClothes(ItemStack stack) {
      return getBondageElement(stack, "clothes", ItemClothes.class);
   }

   public boolean func_77636_d(ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof ItemTrappedBed) {
         ItemTrappedBed itemTrappedBed = (ItemTrappedBed)stack.func_77973_b();
         ItemStack bind = itemTrappedBed.getBind(stack);
         ItemStack gag = itemTrappedBed.getGag(stack);
         ItemStack blindfold = itemTrappedBed.getBlindfold(stack);
         ItemStack earplugs = itemTrappedBed.getEarplugs(stack);
         ItemStack collar = itemTrappedBed.getCollar(stack);
         ItemStack clothes = itemTrappedBed.getClothes(stack);
         return bind != null || gag != null || blindfold != null || earplugs != null || collar != null || clothes != null || super.func_77636_d(stack);
      } else {
         return super.func_77636_d(stack);
      }
   }

   public void registerModels() {
      KidnapModMain.proxy.registerColoredItemRenderer(this, "inventory");
   }

   public EnumDyeColor getMaincolor() {
      return EnumDyeColor.WHITE;
   }
}
