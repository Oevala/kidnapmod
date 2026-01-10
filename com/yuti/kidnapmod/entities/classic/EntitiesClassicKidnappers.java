package com.yuti.kidnapmod.entities.classic;

import java.util.Random;

public enum EntitiesClassicKidnappers {
   KIDNAPPER_1(1, true),
   KIDNAPPER_2(2, true),
   KIDNAPPER_3(3, true),
   KIDNAPPER_4(4, true),
   KIDNAPPER_5(5, true),
   KIDNAPPER_6(6, true),
   KIDNAPPER_7(7, true),
   KIDNAPPER_8(8, true),
   KIDNAPPER_9(9, true),
   KIDNAPPER_10(10, true),
   KIDNAPPER_11(11, true),
   KIDNAPPER_12(12, true),
   KIDNAPPER_13(13, true),
   KIDNAPPER_14(14, true),
   KIDNAPPER_15(15, true),
   KIDNAPPER_16(16, true),
   KIDNAPPER_17(17, true),
   KIDNAPPER_18(18, true);

   private int id;
   private boolean smallArms;

   private EntitiesClassicKidnappers(int id, boolean smallArms) {
      this.id = id;
      this.smallArms = smallArms;
   }

   private EntitiesClassicKidnappers(int id) {
      this(id, false);
   }

   public int getId() {
      return this.id;
   }

   public boolean hasSmallArms() {
      return this.smallArms;
   }

   public static EntitiesClassicKidnappers getRandomKidnapper() {
      int count = values().length;
      Random rand = new Random();
      return values()[rand.nextInt(count)];
   }

   public EntitiesClassicKidnappers getById(int id) {
      EntitiesClassicKidnappers[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntitiesClassicKidnappers kidnappers = var2[var4];
         int kidnapperId = kidnappers.getId();
         if (id == kidnapperId) {
            return kidnappers;
         }
      }

      return null;
   }
}
