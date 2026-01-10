package com.yuti.kidnapmod.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.BlockDoor.EnumHingePosition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemKidnapDoorBase extends ItemBlock {
   public ItemKidnapDoorBase(Block block) {
      super(block);
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (facing != EnumFacing.UP) {
         return EnumActionResult.FAIL;
      } else {
         IBlockState bottomDoorState = worldIn.func_180495_p(pos);
         Block bottomDoorBlock = bottomDoorState.func_177230_c();
         if (!this.field_150939_a.func_176200_f(worldIn, pos)) {
            pos = pos.func_177972_a(facing);
         }

         ItemStack stack = player.func_184586_b(hand);
         if (player.func_175151_a(pos, facing, stack) && this.field_150939_a.func_176196_c(worldIn, pos)) {
            EnumFacing playerFacing = EnumFacing.func_176733_a((double)player.field_70177_z);
            int x = playerFacing.func_82601_c();
            int z = playerFacing.func_82599_e();
            boolean flag = x < 0 && hitZ < 0.5F || x > 0 && hitZ > 0.5F || z < 0 && hitX < 0.5F || z > 0 && hitX > 0.5F;
            placeDoor(worldIn, pos, playerFacing, this.field_150939_a, flag);
            SoundType sound = this.field_150939_a.getSoundType(bottomDoorState, worldIn, pos, player);
            worldIn.func_184133_a(player, pos, sound.func_185841_e(), SoundCategory.BLOCKS, (sound.func_185843_a() + 1.0F) / 2.0F, sound.func_185847_b() * 0.8F);
            stack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
         } else {
            return EnumActionResult.FAIL;
         }
      }
   }

   private static void placeDoor(World worldIn, BlockPos bottomDoorPos, EnumFacing playerFacing, Block door, boolean isRightHinge) {
      BlockPos posYClockwise = bottomDoorPos.func_177972_a(playerFacing.func_176746_e());
      BlockPos posYAntiClockwise = bottomDoorPos.func_177972_a(playerFacing.func_176735_f());
      int i = (worldIn.func_180495_p(posYAntiClockwise).func_185915_l() ? 1 : 0) + (worldIn.func_180495_p(posYAntiClockwise.func_177984_a()).func_185915_l() ? 1 : 0);
      int j = (worldIn.func_180495_p(posYClockwise).func_185915_l() ? 1 : 0) + (worldIn.func_180495_p(posYClockwise.func_177984_a()).func_185915_l() ? 1 : 0);
      boolean flag = worldIn.func_180495_p(posYAntiClockwise).func_177230_c() == door || worldIn.func_180495_p(posYAntiClockwise.func_177984_a()).func_177230_c() == door;
      boolean flag1 = worldIn.func_180495_p(posYClockwise).func_177230_c() == door || worldIn.func_180495_p(posYClockwise.func_177984_a()).func_177230_c() == door;
      if ((!flag || flag1) && j <= i) {
         if (flag1 && !flag || j < i) {
            isRightHinge = false;
         }
      } else {
         isRightHinge = true;
      }

      BlockPos topDoorPos = bottomDoorPos.func_177984_a();
      boolean powered = worldIn.func_175640_z(bottomDoorPos) || worldIn.func_175640_z(topDoorPos);
      IBlockState doorState = door.func_176223_P().func_177226_a(BlockDoor.field_176520_a, playerFacing).func_177226_a(BlockDoor.field_176521_M, isRightHinge ? EnumHingePosition.RIGHT : EnumHingePosition.LEFT).func_177226_a(BlockDoor.field_176522_N, powered).func_177226_a(BlockDoor.field_176519_b, powered);
      worldIn.func_175656_a(bottomDoorPos, doorState.func_177226_a(BlockDoor.field_176523_O, EnumDoorHalf.LOWER));
      worldIn.func_175656_a(topDoorPos, doorState.func_177226_a(BlockDoor.field_176523_O, EnumDoorHalf.UPPER));
      worldIn.func_175685_c(bottomDoorPos, door, false);
      worldIn.func_175685_c(topDoorPos, door, false);
   }
}
