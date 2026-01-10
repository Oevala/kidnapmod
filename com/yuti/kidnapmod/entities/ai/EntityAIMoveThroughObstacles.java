package com.yuti.kidnapmod.entities.ai;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;

public class EntityAIMoveThroughObstacles extends EntityAIBase {
   private EntityKidnapper kidnapper;

   public EntityAIMoveThroughObstacles(EntityKidnapper kidnapper) {
      this.kidnapper = kidnapper;
      PathNavigateGround navigator = (PathNavigateGround)this.kidnapper.func_70661_as();
      navigator.func_179691_c(true);
   }

   public boolean func_75250_a() {
      if (this.kidnapper.isTiedUp()) {
         return false;
      } else if (this.kidnapper.hasCollar()) {
         return false;
      } else if (!this.kidnapper.func_70089_S()) {
         return false;
      } else if (this.kidnapper.func_70090_H()) {
         return false;
      } else if (this.kidnapper.getTarget() == null && !this.kidnapper.hasSlaves() && !this.kidnapper.isGetOutState()) {
         return false;
      } else {
         return !this.kidnapper.isWaitingForJobToBeCompleted();
      }
   }

   public boolean func_75253_b() {
      return this.func_75250_a() ? true : super.func_75253_b();
   }

   public void func_75246_d() {
      BlockPos pos = this.kidnapper.func_180425_c();
      this.replaceLavaObstacles(pos.func_177982_a(1, -1, 0));
      this.replaceLavaObstacles(pos.func_177982_a(0, -1, 1));
      this.replaceLavaObstacles(pos.func_177982_a(-1, -1, 0));
      this.replaceLavaObstacles(pos.func_177982_a(0, -1, -1));
      this.replaceLavaObstacles(pos.func_177982_a(1, -1, 1));
      this.replaceLavaObstacles(pos.func_177982_a(-1, -1, 1));
      this.replaceLavaObstacles(pos.func_177982_a(1, -1, -1));
      this.replaceLavaObstacles(pos.func_177982_a(-1, -1, -1));
   }

   protected void replaceLavaObstacles(BlockPos pos) {
      if (this.kidnapper.field_70170_p.func_180495_p(pos).equals(Blocks.field_150353_l.func_176223_P())) {
         this.kidnapper.field_70170_p.func_175656_a(pos, Blocks.field_150346_d.func_176223_P());
      }

   }
}
