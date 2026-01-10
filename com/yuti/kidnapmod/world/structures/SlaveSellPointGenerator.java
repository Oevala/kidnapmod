package com.yuti.kidnapmod.world.structures;

import com.yuti.kidnapmod.util.IStructure;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class SlaveSellPointGenerator implements IStructure {
   private static ResourceLocation location = new ResourceLocation("knapm", "slave_sell_point");

   @Nullable
   public static BlockPos generate(World world, EntityLiving entity, int x, int z) {
      BlockPos newPos = new BlockPos(x, calculateGenerationHeight(world, x, z), z);
      if (isSuitablePlace(world, newPos) && entity.func_70661_as().func_188555_b(newPos)) {
         generateSellPoint(world, newPos);
         return newPos.func_177982_a(-1, 0, -1);
      } else {
         return null;
      }
   }

   private static boolean isSuitablePlace(World world, BlockPos pos) {
      IBlockState underBlock = world.func_180495_p(pos.func_177982_a(0, -1, 0));
      IBlockState water = Blocks.field_150355_j.func_176223_P();
      IBlockState lava = Blocks.field_150353_l.func_176223_P();
      return underBlock != water && underBlock != lava && !world.func_175623_d(pos.func_177982_a(0, -1, 0)) && world.func_175623_d(pos.func_177982_a(0, 1, 0));
   }

   private static int calculateGenerationHeight(World world, int x, int z) {
      int y = world.func_72800_K();

      for(boolean foundGround = false; !foundGround && y-- >= 0; foundGround = !world.func_175623_d(new BlockPos(x, y - 1, z))) {
      }

      return y;
   }

   private static void generateSellPoint(World world, BlockPos pos) {
      MinecraftServer mcServer = world.func_73046_m();
      TemplateManager manager = worldServer.func_184163_y();
      Template template = manager.func_189942_b(mcServer, location);
      if (template != null) {
         IBlockState state = world.func_180495_p(pos);
         world.func_184138_a(pos, state, state, 3);
         template.func_186260_a(world, pos, settings);
      }

   }
}
