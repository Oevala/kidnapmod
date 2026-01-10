package com.yuti.kidnapmod.extrainventory;

public enum ExtraBondageItemType {
   BIND(new int[]{0}),
   GAG(new int[]{1}),
   BLINDFOLD(new int[]{2}),
   EARPLUGS(new int[]{3}),
   COLLAR(new int[]{4}),
   CLOTHES(new int[]{5}),
   BONDAGEEXTRA(new int[]{0, 1, 2, 3, 4, 5});

   int[] validSlots;

   private ExtraBondageItemType(int... validSlots) {
      this.validSlots = validSlots;
   }

   public boolean hasSlot(int slot) {
      int[] var2 = this.validSlots;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         int s = var2[var4];
         if (s == slot) {
            return true;
         }
      }

      return false;
   }

   public int[] getValidSlots() {
      return this.validSlots;
   }

   public int getMainSlot() {
      return this.validSlots.length > 0 ? this.validSlots[0] : -1;
   }
}
