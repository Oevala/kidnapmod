package com.yuti.kidnapmod.blocks;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.tileentity.TileEntityBlockPaddedPane;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPaddedPane extends BlockPadded {
   public static final PropertyDirection FACING = PropertyDirection.func_177714_a("facing");
   public static final PropertyBool OPEN = PropertyBool.func_177716_a("open");
   public static final AxisAlignedBB AXIS_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
   public static final AxisAlignedBB AXIS_SOUTH = new AxisAlignedBB(1.0D, 1.0D, 1.0D, 0.0D, 0.0D, 0.8115D);
   public static final AxisAlignedBB AXIS_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
   public static final AxisAlignedBB AXIS_EAST = new AxisAlignedBB(1.0D, 1.0D, 1.0D, 0.8115D, 0.0D, 0.0D);

   public BlockPaddedPane(String name, Material material) {
      super(name, material);
      this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(FACING, EnumFacing.NORTH).func_177226_a(OPEN, false));
   }

   public void func_190948_a(ItemStack stack, World player, List<String> addList, ITooltipFlag advanced) {
      addList.add(ChatFormatting.GRAY + I18n.func_135052_a("block.desc.padded_pane", new Object[0]));
   }

   public boolean func_149686_d(IBlockState state) {
      return false;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public TileEntity createTileEntity(World world, IBlockState state) {
      return new TileEntityBlockPaddedPane();
   }

   @Nullable
   public TileEntityBlockPaddedPane getTileEntity(IBlockAccess world, BlockPos pos) {
      return (TileEntityBlockPaddedPane)world.func_175625_s(pos);
   }

   public IBlockState func_176221_a(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
      TileEntityBlockPaddedPane tile = this.getTileEntity(worldIn, pos);
      return state.func_177226_a(FACING, state.func_177229_b(FACING)).func_177226_a(OPEN, tile.isOpen());
   }

   public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
      return this.func_176223_P().func_177226_a(FACING, EnumFacing.func_190914_a(pos, placer));
   }

   public int func_176201_c(IBlockState state) {
      return ((EnumFacing)state.func_177229_b(FACING)).func_176745_a();
   }

   public IBlockState func_176203_a(int meta) {
      return this.func_176223_P().func_177226_a(FACING, EnumFacing.func_82600_a(meta));
   }

   protected BlockStateContainer func_180661_e() {
      return new BlockStateContainer(this, new IProperty[]{FACING, OPEN});
   }

   public void func_180633_a(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
      if (state.func_177229_b(FACING) == EnumFacing.UP || state.func_177229_b(FACING) == EnumFacing.DOWN) {
         worldIn.func_175656_a(pos, state.func_177226_a(FACING, getFaceForBlockAccordingToPlacer(placer)));
      }

   }

   public AxisAlignedBB func_185496_a(IBlockState state, IBlockAccess source, BlockPos pos) {
      if (state == state.func_177226_a(FACING, EnumFacing.NORTH)) {
         return AXIS_NORTH;
      } else if (state == state.func_177226_a(FACING, EnumFacing.WEST)) {
         return AXIS_WEST;
      } else if (state == state.func_177226_a(FACING, EnumFacing.SOUTH)) {
         return AXIS_SOUTH;
      } else if (state == state.func_177226_a(FACING, EnumFacing.EAST)) {
         return AXIS_EAST;
      } else if (state == state.func_177226_a(FACING, EnumFacing.UP)) {
         return AXIS_NORTH;
      } else {
         return state == state.func_177226_a(FACING, EnumFacing.DOWN) ? AXIS_NORTH : super.func_185496_a(state, source, pos);
      }
   }

   public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      SoundType sound = SoundType.field_185852_e;
      worldIn.func_184133_a(playerIn, pos, sound.func_185841_e(), SoundCategory.BLOCKS, (sound.func_185843_a() + 1.0F) / 2.0F, sound.func_185847_b() * 0.8F);
      if (!worldIn.field_72995_K) {
         IBlockState state_open = state.func_177226_a(OPEN, true);
         IBlockState state_close = state.func_177226_a(OPEN, false);
         TileEntityBlockPaddedPane tile = this.getTileEntity(worldIn, pos);
         if (tile.isOpen()) {
            worldIn.func_175656_a(pos, state_close);
            tile.setOpen(false);
         } else {
            worldIn.func_175656_a(pos, state_open);
            tile.setOpen(true);
         }
      }

      return true;
   }

   public BlockRenderLayer func_180664_k() {
      return BlockRenderLayer.TRANSLUCENT;
   }

   private static EnumFacing getFaceForBlockAccordingToPlacer(EntityLivingBase placer) {
      EnumFacing face = placer.func_174811_aO();
      if (face == EnumFacing.NORTH) {
         return EnumFacing.SOUTH;
      } else if (face == EnumFacing.SOUTH) {
         return EnumFacing.NORTH;
      } else {
         return face == EnumFacing.EAST ? EnumFacing.WEST : EnumFacing.EAST;
      }
   }
}
