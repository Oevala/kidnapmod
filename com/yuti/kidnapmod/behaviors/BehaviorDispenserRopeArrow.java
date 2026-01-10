package com.yuti.kidnapmod.behaviors;

import com.yuti.kidnapmod.entities.EntityRopeArrow;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorDispenserRopeArrow extends BehaviorProjectileDispense {
   protected IProjectile func_82499_a(World worldIn, IPosition position, ItemStack stackIn) {
      EntityRopeArrow arrow = new EntityRopeArrow(worldIn, position, stackIn);
      return arrow;
   }
}
