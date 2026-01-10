package com.yuti.kidnapmod.blocks;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemKidnapDoorBase;
import com.yuti.kidnapmod.util.IHasModel;
import java.util.Random;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockKidnapDoorBase extends BlockDoor implements IHasModel {
   public BlockKidnapDoorBase(String name, Material materialIn) {
      super(materialIn);
      this.func_149663_c(name);
      this.setRegistryName(name);
      this.func_149647_a(ModCreativeTabs.kidnapTab);
      this.func_149713_g(0);
      this.func_149711_c(0.9F);
      this.func_149752_b(45.0F);
      ModBlocks.BLOCKS.add(this);
      ModItems.ITEMS.add((new ItemKidnapDoorBase(this)).setRegistryName(name));
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      return state.func_177229_b(field_176523_O) == EnumDoorHalf.UPPER ? Items.field_190931_a : Item.func_150898_a(this);
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      return new ItemStack(this);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(Item.func_150898_a(this), 0, "inventory");
   }
}
