package com.yuti.kidnapmod.world;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class KidnapExplosion {
   private World world;
   private BlockPos pos;
   private int radius;
   private ItemStack bind;
   private ItemStack gag;
   private ItemStack blindfold;
   private ItemStack earplugs;
   private ItemStack collar;
   private ItemStack clothes;

   public KidnapExplosion(World worldIn, BlockPos posIn, int radiusIn, ItemStack bindIn, ItemStack gagIn, ItemStack blindfoldIn, ItemStack earplugsIn, ItemStack collarIn, ItemStack clothesIn) {
      this.world = worldIn;
      this.pos = posIn;
      this.radius = radiusIn;
      this.bind = bindIn;
      this.gag = gagIn;
      this.blindfold = blindfoldIn;
      this.earplugs = earplugsIn;
      this.collar = collarIn;
      this.clothes = clothesIn;
   }

   public void explode() {
      this.explode((EntityPlayer)null);
   }

   public void explode(EntityPlayer toExclude) {
      if (this.world != null && !this.world.field_72995_K && this.pos != null) {
         this.world.func_184133_a((EntityPlayer)null, this.pos, SoundEvents.field_187539_bB, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.field_73012_v.nextFloat() - this.world.field_73012_v.nextFloat()) * 0.2F) * 0.7F);
         this.world.func_175688_a(EnumParticleTypes.EXPLOSION_LARGE, (double)this.pos.func_177958_n(), (double)this.pos.func_177956_o(), (double)this.pos.func_177952_p(), 1.0D, 0.0D, 0.0D, new int[0]);
         List<I_Kidnapped> toKidnap = Utils.getKidnapableEntitiesAround(this.world, this.pos, (double)this.radius);
         if (toKidnap != null) {
            Iterator var4 = toKidnap.iterator();

            while(true) {
               boolean exclude;
               I_Kidnapped entityToKidnap;
               do {
                  if (!var4.hasNext()) {
                     return;
                  }

                  entityToKidnap = (I_Kidnapped)var4.next();
                  exclude = false;
               } while(entityToKidnap == null);

               if (toExclude != null && entityToKidnap instanceof PlayerBindState) {
                  PlayerBindState state = (PlayerBindState)entityToKidnap;
                  EntityPlayer player = state.getPlayer();
                  if (player != null && (player.func_175149_v() || player.equals(toExclude))) {
                     exclude = true;
                  }
               }

               if (!exclude) {
                  entityToKidnap.applyBondage(this.bind, this.gag, this.blindfold, this.earplugs, this.collar, this.clothes);
               }
            }
         }
      }
   }
}
