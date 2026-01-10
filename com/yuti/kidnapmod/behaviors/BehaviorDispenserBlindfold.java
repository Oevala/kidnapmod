package com.yuti.kidnapmod.behaviors;

import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.item.ItemStack;

public class BehaviorDispenserBlindfold extends BehaviorDispenserEquipBondage {
   protected boolean isValid(ItemStack stack) {
      return stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemBlindfold;
   }

   protected boolean canEquip(I_Kidnapped state) {
      return state != null && !state.isBlindfolded();
   }

   protected void equip(I_Kidnapped state, ItemStack stack) {
      if (state != null) {
         state.putBlindfoldOn(stack);
         state.checkBlindfoldAfterApply();
      }

   }
}
