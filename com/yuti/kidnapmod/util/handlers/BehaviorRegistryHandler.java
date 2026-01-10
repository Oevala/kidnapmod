package com.yuti.kidnapmod.util.handlers;

import com.yuti.kidnapmod.behaviors.BehaviorDispenserBind;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserBlindfold;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserClothes;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserCollar;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserEarplugs;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserGag;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserKidnapBomb;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserPlaceBlock;
import com.yuti.kidnapmod.behaviors.BehaviorDispenserRopeArrow;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import java.util.Iterator;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.RegistryDefaulted;

public class BehaviorRegistryHandler {
   public static void registerBehaviors(RegistryDefaulted<Item, IBehaviorDispenseItem> registry) {
      registry.func_82595_a(ModItems.ROPE_ARROW, new BehaviorDispenserRopeArrow());
      registry.func_82595_a(Item.func_150898_a(ModBlocks.ROPES_TRAP), new BehaviorDispenserPlaceBlock(ModBlocks.ROPES_TRAP));
      registry.func_82595_a(Item.func_150898_a(ModBlocks.KIDNAP_BOMB), new BehaviorDispenserKidnapBomb());
      Iterator var1 = ModItems.BIND_LIST.iterator();

      while(var1.hasNext()) {
         ItemBind bind = (ItemBind)var1.next();
         registry.func_82595_a(bind, new BehaviorDispenserBind());
      }

      var1 = ModItems.GAG_LIST.iterator();

      while(var1.hasNext()) {
         ItemGag gag = (ItemGag)var1.next();
         registry.func_82595_a(gag, new BehaviorDispenserGag());
      }

      var1 = ModItems.BLINDFOLD_LIST.iterator();

      while(var1.hasNext()) {
         ItemBlindfold blindfold = (ItemBlindfold)var1.next();
         registry.func_82595_a(blindfold, new BehaviorDispenserBlindfold());
      }

      var1 = ModItems.EARPLUGS_LIST.iterator();

      while(var1.hasNext()) {
         ItemEarplugs earplugs = (ItemEarplugs)var1.next();
         registry.func_82595_a(earplugs, new BehaviorDispenserEarplugs());
      }

      var1 = ModItems.COLLAR_LIST.iterator();

      while(var1.hasNext()) {
         ItemCollar collar = (ItemCollar)var1.next();
         registry.func_82595_a(collar, new BehaviorDispenserCollar());
      }

      var1 = ModItems.CLOTHES_LIST.iterator();

      while(var1.hasNext()) {
         ItemClothes clothes = (ItemClothes)var1.next();
         registry.func_82595_a(clothes, new BehaviorDispenserClothes());
      }

   }
}
