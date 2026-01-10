package com.yuti.kidnapmod.behaviors;

import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.item.ItemStack;

public class BehaviorDispenserCollar extends BehaviorDispenserEquipBondage {
   protected boolean isValid(ItemStack stack) {
      return stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemCollar;
   }

   protected boolean canEquip(I_Kidnapped state) {
      return state != null && !state.hasCollar();
   }

   protected void equip(I_Kidnapped state, ItemStack stack) {
      if (state != null) {
         state.putCollarOn(stack);
         state.checkCollarAfterApply();
      }

   }
}
