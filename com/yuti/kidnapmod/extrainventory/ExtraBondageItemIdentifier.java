package com.yuti.kidnapmod.extrainventory;

public enum ExtraBondageItemIdentifier {
   BIND(0),
   GAG(1),
   BLINFOLD(2),
   EARPLUGS(3),
   COLLAR(4),
   KNIVES(5),
   CLOTHES(6);

   int id;

   private ExtraBondageItemIdentifier(int identifier) {
      this.id = identifier;
   }

   public int getId() {
      return this.id;
   }

   public static ExtraBondageItemIdentifier getIdentifier(int id) {
      ExtraBondageItemIdentifier[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ExtraBondageItemIdentifier item = var1[var3];
         if (item.id == id) {
            return item;
         }
      }

      return null;
   }
}
