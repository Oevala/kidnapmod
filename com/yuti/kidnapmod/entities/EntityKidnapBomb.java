package com.yuti.kidnapmod.entities;

import com.yuti.kidnapmod.tileentity.TileEntityKidnapBomb;
import com.yuti.kidnapmod.util.UtilsParameters;
import com.yuti.kidnapmod.world.KidnapExplosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityKidnapBomb extends EntityTNTPrimed {
   private ItemStack bind;
   private ItemStack gag;
   private ItemStack blindfold;
   private ItemStack earplugs;
   private ItemStack collar;
   private ItemStack clothes;

   public EntityKidnapBomb(World worldIn, TileEntityKidnapBomb tileBomb, double x, double y, double z, EntityLivingBase igniter) {
      super(worldIn, x, y, z, igniter);
      if (tileBomb != null) {
         this.bind = tileBomb.getBind();
         this.gag = tileBomb.getGag();
         this.blindfold = tileBomb.getBlindfold();
         this.earplugs = tileBomb.getEarplugs();
         this.collar = tileBomb.getCollar();
         this.clothes = tileBomb.getClothes();
      }

   }

   public EntityKidnapBomb(World worldIn) {
      super(worldIn);
   }

   public void func_70071_h_() {
      this.field_70169_q = this.field_70165_t;
      this.field_70167_r = this.field_70163_u;
      this.field_70166_s = this.field_70161_v;
      if (!this.func_189652_ae()) {
         this.field_70181_x -= 0.03999999910593033D;
      }

      this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
      this.field_70159_w *= 0.9800000190734863D;
      this.field_70181_x *= 0.9800000190734863D;
      this.field_70179_y *= 0.9800000190734863D;
      if (this.field_70122_E) {
         this.field_70159_w *= 0.699999988079071D;
         this.field_70179_y *= 0.699999988079071D;
         this.field_70181_x *= -0.5D;
      }

      int fuse = this.func_184536_l();
      --fuse;
      this.func_184534_a(fuse);
      if (fuse <= 0) {
         this.func_70106_y();
         if (!this.field_70170_p.field_72995_K) {
            this.explode();
         }
      } else {
         this.func_70072_I();
         this.field_70170_p.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, this.field_70165_t, this.field_70163_u + 0.5D, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
      }

   }

   private void explode() {
      int radius = UtilsParameters.getKidnapBombRadius(this.field_70170_p);
      KidnapExplosion explosion = new KidnapExplosion(this.field_70170_p, this.func_180425_c(), radius, this.bind, this.gag, this.blindfold, this.earplugs, this.collar, this.clothes);
      explosion.explode();
   }
}
