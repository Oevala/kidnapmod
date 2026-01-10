package com.yuti.kidnapmod.world.structures;

import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class SlaveCellStructureGenerator implements IWorldGenerator {
   public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator arg4, IChunkProvider arg5) {
      switch(world.field_73011_w.getDimension()) {
      case -1:
         this.generateNether(world, random, chunkX * 100, chunkZ * 100);
         break;
      case 0:
         this.generateSurface(world, random, chunkX * 16, chunkZ * 16);
         break;
      case 1:
         this.generateEnd(world, random, chunkX * 100, chunkZ * 100);
      }

   }

   private void generateEnd(World world, Random random, int i, int j) {
   }

   private void generateSurface(World world, Random random, int x, int z) {
      int x1 = x + random.nextInt(15);
      int z1 = z + random.nextInt(15);
      int y1 = calculateGenerationHeight(world, x1, z1);
      int randomGenerationChance = random.nextInt(501);
      if (randomGenerationChance >= 500) {
         (new WorldGenSlaveCell()).func_180709_b(world, random, new BlockPos(x1, y1, z1));
      }

   }

   private void generateNether(World world, Random random, int i, int j) {
   }

   private static int calculateGenerationHeight(World world, int x, int z) {
      int y = world.func_72800_K();

      for(boolean foundGround = false; !foundGround && y-- >= 0; foundGround = !world.func_175623_d(new BlockPos(x, y, z))) {
      }

      return y;
   }
}
