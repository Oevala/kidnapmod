package com.yuti.kidnapmod.blocks;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemRopesTrap;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.tileentity.TileEntityTrap;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRopesTrap extends BlockBase implements ICanBeLoaded {
   protected static final AxisAlignedBB TRAP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1D, 1.0D);

   public BlockRopesTrap(String name, Material material) {
      super(name, material);
      ModItems.ITEMS.add((new ItemRopesTrap(this)).setRegistryName(this.getRegistryName()));
      this.func_149711_c(1.0F);
      this.func_149752_b(0.5F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149713_g(1);
      this.setHarvestLevel("shovel", 0);
      this.func_149647_a(ModCreativeTabs.kidnapTab);
   }

   public void func_190948_a(ItemStack stack, World player, List<String> addList, ITooltipFlag advanced) {
      addList.add(ChatFormatting.GRAY + I18n.func_135052_a("block.desc.trap", new Object[0]));
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      TileEntityTrap tileTrap = new TileEntityTrap(true);
      if (nbt != null && nbt.func_74764_b("BlockEntityTag")) {
         tileTrap.func_145839_a(nbt.func_74775_l("BlockEntityTag"));
         if (tileTrap.isArmed()) {
            addList.add(ChatFormatting.DARK_RED + "Armed");
         } else {
            addList.add(ChatFormatting.GREEN + "Disarmed");
         }

         if (tileTrap.getBind() != null) {
            addList.add(ChatFormatting.GOLD + tileTrap.getBind().func_82833_r());
         }

         if (tileTrap.getGag() != null) {
            addList.add(ChatFormatting.GOLD + tileTrap.getGag().func_82833_r());
         }

         if (tileTrap.getBlindfold() != null) {
            addList.add(ChatFormatting.GOLD + tileTrap.getBlindfold().func_82833_r());
         }

         if (tileTrap.getEarplugs() != null) {
            addList.add(ChatFormatting.GOLD + tileTrap.getEarplugs().func_82833_r());
         }

         if (tileTrap.getCollar() != null) {
            addList.add(ChatFormatting.GOLD + tileTrap.getCollar().func_82833_r());
         }

         if (tileTrap.getClothes() != null) {
            addList.add(ChatFormatting.GOLD + tileTrap.getClothes().func_82833_r());
         }
      } else {
         addList.add(ChatFormatting.GREEN + "Disarmed");
      }

   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public void func_180634_a(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
      if ((entityIn instanceof EntityPlayer || entityIn instanceof I_Kidnapped) && !worldIn.field_72995_K) {
         I_Kidnapped targetState = null;
         if (entityIn instanceof EntityPlayer) {
            EntityPlayer targetPlayer = (EntityPlayer)entityIn;
            targetState = PlayerBindState.getInstance(targetPlayer);
         } else {
            targetState = (I_Kidnapped)entityIn;
         }

         if (targetState != null && !((I_Kidnapped)targetState).isTiedUp()) {
            TileEntityTrap trapTile = this.getTileEntity(worldIn, pos);
            if (trapTile != null && trapTile.isArmed()) {
               ItemStack bind = trapTile.getBind();
               ItemStack gag = trapTile.getGag();
               ItemStack blindfold = trapTile.getBlindfold();
               ItemStack earplugs = trapTile.getEarplugs();
               ItemStack collar = trapTile.getCollar();
               ItemStack clothes = trapTile.getClothes();
               ((I_Kidnapped)targetState).applyBondage(bind, gag, blindfold, earplugs, collar, clothes);
               worldIn.func_180501_a(pos, Blocks.field_150350_a.func_176223_P(), 11);
               Utils.sendInfoMessageToEntity(entityIn, "You fell into a trap! You're completly tied up!");
            }
         }
      }

   }

   public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (!worldIn.field_72995_K && playerIn != null && hand == EnumHand.MAIN_HAND) {
         ItemStack heldStack = playerIn.func_184586_b(hand);
         ItemStack stack = Utils.extractValidStack(heldStack);
         Utils.loadItemStack(worldIn, pos, this.getTileEntity(worldIn, pos), heldStack, stack, playerIn);
      }

      return super.func_180639_a(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   @Nullable
   public AxisAlignedBB func_180646_a(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
      return field_185506_k;
   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      return TRAP_AABB;
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   @SideOnly(Side.CLIENT)
   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.CUTOUT;
   }

   public boolean func_176196_c(World worldIn, BlockPos pos) {
      return this.canBePlacedOn(worldIn, pos.func_177977_b());
   }

   public void func_189540_a(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
      if (!this.canBePlacedOn(worldIn, pos.func_177977_b())) {
         this.func_176226_b(worldIn, pos, state, 0);
         worldIn.func_175698_g(pos);
      }

   }

   private boolean canBePlacedOn(World worldIn, BlockPos pos) {
      return worldIn.func_180495_p(pos).func_185917_h();
   }

   @Nullable
   public TileEntityTrap getTileEntity(IBlockAccess world, BlockPos pos) {
      return (TileEntityTrap)world.func_175625_s(pos);
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityTrap();
   }

   public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ItemStack stack = new ItemStack(this);
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      TileEntityTrap trapTile = this.getTileEntity(world, pos);
      if (trapTile != null) {
         NBTTagCompound tileData = new NBTTagCompound();
         tileData = trapTile.func_189515_b(tileData);
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
}
