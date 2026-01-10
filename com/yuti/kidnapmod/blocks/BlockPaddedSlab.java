package com.yuti.kidnapmod.blocks;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.util.IHasModel;
import java.util.Random;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockPaddedSlab extends BlockSlab implements IHasModel {
   public static final PropertyEnum<BlockPaddedSlab.Variant> VARIANT = PropertyEnum.func_177709_a("variant", BlockPaddedSlab.Variant.class);

   public BlockPaddedSlab(String name, Material materialIn) {
      super(materialIn);
      this.func_149663_c(name);
      this.setRegistryName(name);
      this.func_149711_c(0.9F);
      this.func_149752_b(45.0F);
      this.func_149672_a(SoundType.field_185854_g);
      this.func_149713_g(0);
      ModBlocks.BLOCKS.add(this);
      IBlockState iblockstate = this.field_176227_L.func_177621_b().func_177226_a(VARIANT, BlockPaddedSlab.Variant.DEFAULT);
      if (!this.func_176552_j()) {
         iblockstate.func_177226_a(field_176554_a, EnumBlockHalf.BOTTOM);
      }

      this.func_180632_j(iblockstate);
      this.field_149783_u = !this.func_176552_j();
   }

   public String func_150002_b(int meta) {
      return super.func_149739_a();
   }

   public IProperty<?> func_176551_l() {
      return VARIANT;
   }

   public Comparable<?> func_185674_a(ItemStack stack) {
      return BlockPaddedSlab.Variant.DEFAULT;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return Item.func_150898_a(ModBlocks.PADDED_SLAB);
   }

   public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
      return new ItemStack(ModBlocks.PADDED_SLAB);
   }

   public final IBlockState func_176203_a(int meta) {
      IBlockState blockstate = this.field_176227_L.func_177621_b().func_177226_a(VARIANT, BlockPaddedSlab.Variant.DEFAULT);
      if (!this.func_176552_j()) {
         blockstate = blockstate.func_177226_a(field_176554_a, (meta & 8) != 0 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
      }

      return blockstate;
   }

   public final int func_176201_c(IBlockState state) {
      int meta = 0;
      if (!this.func_176552_j() && state.func_177229_b(field_176554_a) == EnumBlockHalf.TOP) {
         meta |= 8;
      }

      return meta;
   }

   protected BlockStateContainer func_180661_e() {
      return !this.func_176552_j() ? new BlockStateContainer(this, new IProperty[]{VARIANT, field_176554_a}) : new BlockStateContainer(this, new IProperty[]{VARIANT});
   }

   public static enum Variant implements IStringSerializable {
      DEFAULT;

      public String func_176610_l() {
         return "default";
      }
   }

   public static class Half extends BlockPaddedSlab {
      public Half(String name, BlockPaddedSlab upper, Material material) {
         super(name, material);
         ModItems.ITEMS.add((new ItemSlab(this, this, upper)).setRegistryName(this.getRegistryName()));
         this.func_149647_a(ModCreativeTabs.kidnapTab);
      }

      public boolean func_176552_j() {
         return false;
      }

      public void registerModels() {
         KidnapModMain.proxy.registerItemRenderer(Item.func_150898_a(this), 0, "inventory");
      }
   }

   public static class Double extends BlockPaddedSlab {
      public Double(String name, Material material) {
         super(name, material);
      }

      public boolean func_176552_j() {
         return true;
      }

      public void registerModels() {
      }
   }
}
