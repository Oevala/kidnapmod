package com.yuti.kidnapmod.world.structures;

import com.yuti.kidnapmod.util.IStructure;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenSlaveCell extends WorldGenerator implements IStructure {
   private static ResourceLocation location = new ResourceLocation("knapm", "slavetradingcell");

   public boolean func_180709_b(World world, Random random, BlockPos pos) {
      IBlockState underBlock = world.func_180495_p(pos.func_177982_a(0, -1, 0));
      IBlockState water = Blocks.field_150355_j.func_176223_P();
      IBlockState lava = Blocks.field_150353_l.func_176223_P();
      if (underBlock != water && underBlock != lava && !world.func_175623_d(pos.func_177982_a(0, -1, 0)) && world.func_175623_d(pos.func_177982_a(0, 1, 0))) {
         this.generateCell(world, random, pos);
         return true;
      } else {
         return false;
      }
   }

   private void generateCell(World world, Random random, BlockPos pos) {
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
