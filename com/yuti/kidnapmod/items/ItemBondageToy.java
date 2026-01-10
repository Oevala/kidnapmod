package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.entities.guests.EntityBirdy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class ItemBondageToy extends ItemSoundAttack {
   public ItemBondageToy(String name, int stackSize, SoundEvent soundIn) {
      super(name, stackSize, soundIn);
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (player != null && player.field_70170_p != null && !player.field_70170_p.field_72995_K && entity != null && entity instanceof EntityParrot) {
         EntityParrot parrot = (EntityParrot)entity;
         if (!parrot.func_70909_n()) {
            GameRules rules = player.field_70170_p.func_82736_K();
            if (rules == null || !rules.func_82765_e("birdsteregg") || rules.func_82766_b("birdsteregg")) {
               BlockPos birdyPos = entity.func_180425_c();
               if (birdyPos != null) {
                  entity.func_174810_b(true);
                  entity.func_82142_c(true);
                  entity.func_70106_y();
                  EntityBirdy birdy = new EntityBirdy(player.field_70170_p);
                  birdy.func_70107_b((double)birdyPos.func_177958_n(), (double)birdyPos.func_177956_o(), (double)birdyPos.func_177952_p());
                  player.field_70170_p.func_72838_d(birdy);
               }
            }
         }
      }

      return super.onLeftClickEntity(stack, player, entity);
   }
}
