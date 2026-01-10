package com.yuti.kidnapmod.behaviors;

import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.item.ItemStack;

public class BehaviorDispenserBind extends BehaviorDispenserEquipBondage {
   protected boolean isValid(ItemStack stack) {
      return stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemBind;
   }

   protected boolean canEquip(I_Kidnapped state) {
      return state != null && !state.isTiedUp();
   }

   protected void equip(I_Kidnapped state, ItemStack stack) {
      if (state != null) {
         state.putBindOn(stack);
      }

   }
}
