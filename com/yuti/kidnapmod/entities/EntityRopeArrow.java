package com.yuti.kidnapmod.entities;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemRopeArrow;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import java.util.Random;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityRopeArrow extends EntityArrow {
   private boolean isInfinite;
   private ItemStack bind;
   private ItemStack gag;
   private ItemStack blindfold;
   private ItemStack earplugs;
   private ItemStack collar;
   private ItemStack clothes;

   public EntityRopeArrow(World worldIn) {
      super(worldIn);
      this.isInfinite = false;
      this.func_70239_b(1.0E-4D);
   }

   public EntityRopeArrow(World worldIn, EntityLivingBase shooter) {
      super(worldIn, shooter);
      this.isInfinite = false;
      this.func_70239_b(1.0E-4D);
   }

   public EntityRopeArrow(World worldIn, double x, double y, double z) {
      super(worldIn, x, y, z);
      this.isInfinite = false;
      this.func_70239_b(1.0E-4D);
   }

   public EntityRopeArrow(World worldIn, IPosition position) {
      super(worldIn, position.func_82615_a(), position.func_82617_b(), position.func_82616_c());
      this.isInfinite = false;
      this.func_70239_b(1.0E-4D);
   }

   public EntityRopeArrow(World worldIn, IPosition position, ItemStack stack) {
      this(worldIn, position);
      this.injectBondageStacks(stack);
   }

   public EntityRopeArrow(World world, EntityLivingBase shooter, ItemStack stack, boolean infinite) {
      this(world, shooter, stack);
      this.isInfinite = infinite;
   }

   public EntityRopeArrow(World world, EntityLivingBase shooter, ItemStack stack) {
      this(world, shooter);
      this.injectBondageStacks(stack);
   }

   private void injectBondageStacks(ItemStack stackArrow) {
      if (stackArrow != null && stackArrow.func_77973_b() instanceof ItemRopeArrow) {
         ItemRopeArrow itemArrow = (ItemRopeArrow)stackArrow.func_77973_b();
         this.bind = itemArrow.getBind(stackArrow);
         this.gag = itemArrow.getGag(stackArrow);
         this.blindfold = itemArrow.getBlindfold(stackArrow);
         this.earplugs = itemArrow.getEarplugs(stackArrow);
         this.collar = itemArrow.getCollar(stackArrow);
         this.clothes = itemArrow.getClothes(stackArrow);
      }

   }

   protected ItemStack func_184550_j() {
      return new ItemStack(ModItems.ROPE_ARROW);
   }

   public void func_184548_a(EntityLivingBase living) {
      if (!living.field_70170_p.field_72995_K) {
         if (living instanceof EntityPlayer || living instanceof I_Kidnapped) {
            I_Kidnapped targetState = null;
            if (living instanceof EntityPlayer) {
               EntityPlayer targetPlayer = (EntityPlayer)living;
               targetState = PlayerBindState.getInstance(targetPlayer);
            } else {
               targetState = (I_Kidnapped)living;
            }

            if (((I_Kidnapped)targetState).canBeKidnappedByEvents()) {
               Random rand = new Random();
               int proba = this.getProbabilityRopeArrow(living.func_130014_f_());
               if (rand.nextInt(100) < proba) {
                  ((I_Kidnapped)targetState).applyBondage(this.bind, this.gag, this.blindfold, this.earplugs, this.collar, this.clothes);
               }
            }
         }

      }
   }

   public boolean func_70241_g() {
      return false;
   }

   public int getProbabilityRopeArrow(World world) {
      int proba = 80;
      GameRules rules = world.func_82736_K();
      if (this.isInfinite) {
         proba = 5;
         if (rules.func_82765_e("probability_rope_arrow_infinity")) {
            proba = rules.func_180263_c("probability_rope_arrow_infinity");
         }
      } else if (rules.func_82765_e("probability_rope_arrow")) {
         proba = rules.func_180263_c("probability_rope_arrow");
      }

      return proba;
   }
}
