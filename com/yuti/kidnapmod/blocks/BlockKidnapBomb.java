package com.yuti.kidnapmod.blocks;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.entities.EntityKidnapBomb;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemKidnapBomb;
import com.yuti.kidnapmod.tileentity.TileEntityKidnapBomb;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockKidnapBomb extends BlockTNT implements IHasModel, ICanBeLoaded {
   public BlockKidnapBomb(String name) {
      this.func_149663_c(name);
      this.setRegistryName(name);
      ModBlocks.BLOCKS.add(this);
      ModItems.ITEMS.add((new ItemKidnapBomb(this)).setRegistryName(this.getRegistryName()));
      this.func_149647_a(ModCreativeTabs.kidnapTab);
   }

   public void func_180692_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase igniter) {
      if (!worldIn.field_72995_K && (Boolean)state.func_177229_b(field_176246_a)) {
         TileEntityKidnapBomb tileBomb = this.getTileEntity(worldIn, pos);
         this.explodeSpawnEntity(worldIn, pos, tileBomb, igniter);
      }

   }

   public void explodeSpawnEntity(World worldIn, BlockPos pos, TileEntityKidnapBomb tileBomb, EntityLivingBase igniter) {
      EntityKidnapBomb entitybomb = new EntityKidnapBomb(worldIn, tileBomb, (double)((float)pos.func_177958_n() + 0.5F), (double)pos.func_177956_o(), (double)((float)pos.func_177952_p() + 0.5F), igniter);
      worldIn.func_72838_d(entitybomb);
      worldIn.func_184148_a((EntityPlayer)null, entitybomb.field_70165_t, entitybomb.field_70163_u, entitybomb.field_70161_v, SoundEvents.field_187904_gd, SoundCategory.BLOCKS, 1.0F, 1.0F);
   }

   public void func_190948_a(ItemStack stack, World player, List<String> addList, ITooltipFlag advanced) {
      addList.add(ChatFormatting.GRAY + I18n.func_135052_a("block.desc.bomb", new Object[0]));
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      TileEntityKidnapBomb tilebomb = new TileEntityKidnapBomb(true);
      if (nbt != null && nbt.func_74764_b("BlockEntityTag")) {
         tilebomb.func_145839_a(nbt.func_74775_l("BlockEntityTag"));
         if (tilebomb.getBind() != null) {
            addList.add(ChatFormatting.GOLD + tilebomb.getBind().func_82833_r());
         }

         if (tilebomb.getGag() != null) {
            addList.add(ChatFormatting.GOLD + tilebomb.getGag().func_82833_r());
         }

         if (tilebomb.getBlindfold() != null) {
            addList.add(ChatFormatting.GOLD + tilebomb.getBlindfold().func_82833_r());
         }

         if (tilebomb.getEarplugs() != null) {
            addList.add(ChatFormatting.GOLD + tilebomb.getEarplugs().func_82833_r());
         }

         if (tilebomb.getCollar() != null) {
            addList.add(ChatFormatting.GOLD + tilebomb.getCollar().func_82833_r());
         }

         if (tilebomb.getClothes() != null) {
            addList.add(ChatFormatting.GOLD + tilebomb.getClothes().func_82833_r());
         }
      }

   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!worldIn.field_72995_K && playerIn != null && hand == EnumHand.MAIN_HAND) {
         ItemStack heldStack = playerIn.func_184586_b(hand);
         ItemStack stack = Utils.extractValidStack(heldStack);
         Utils.loadItemStack(worldIn, pos, this.getTileEntity(worldIn, pos), heldStack, stack, playerIn);
      }

      return super.func_180639_a(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
   }

   @Nullable
   public TileEntityKidnapBomb getTileEntity(IBlockAccess world, BlockPos pos) {
      return (TileEntityKidnapBomb)world.func_175625_s(pos);
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityKidnapBomb();
   }

   public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ItemStack stack = new ItemStack(this);
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      TileEntityKidnapBomb bombTile = this.getTileEntity(world, pos);
      if (bombTile != null) {
         NBTTagCompound tileData = new NBTTagCompound();
         tileData = bombTile.func_189515_b(tileData);
         tileData = Utils.removeCoordinates(tileData);
         nbt.func_74782_a("BlockEntityTag", tileData);
      }

      stack.func_77982_d(nbt);
      drops.add(stack);
   }

   public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
      return willHarvest ? true : super.removedByPlayer(state, world, pos, player, willHarvest);
   }

   public void func_180657_a(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool) {
      super.func_180657_a(world, player, pos, state, te, tool);
      world.func_175698_g(pos);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(Item.func_150898_a(this), 0, "inventory");
   }
}
