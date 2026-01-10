package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class ItemSoundAttack extends Item implements IHasModel {
   private SoundEvent sound;

   public ItemSoundAttack(String name, int stackSize, SoundEvent soundIn) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.func_77656_e(-1);
      ModItems.ITEMS.add(this);
      this.sound = soundIn;
      this.func_77625_d(stackSize);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (this.sound != null) {
         Utils.playSound(player, this.sound, 0.5F);
      }

      return super.onLeftClickEntity(stack, player, entity);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }
}
