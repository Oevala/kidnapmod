package com.yuti.kidnapmod.entities.classic;

import java.util.Random;

public enum EntitiesClassicDamsels {
   DAMSEL_1(1, true),
   DAMSEL_2(2, true),
   DAMSEL_3(3, true),
   DAMSEL_4(4, true),
   DAMSEL_5(5, true),
   DAMSEL_6(6, true),
   DAMSEL_7(7, true),
   DAMSEL_8(8, true),
   DAMSEL_9(9, true),
   DAMSEL_10(10, true),
   DAMSEL_11(11, true),
   DAMSEL_12(12, true),
   DAMSEL_13(13, true),
   DAMSEL_14(14, true),
   DAMSEL_15(15, true),
   DAMSEL_16(16, true),
   DAMSEL_17(17, true),
   DAMSEL_18(18, true);

   private int id;
   private boolean smallArms;

   private EntitiesClassicDamsels(int id, boolean smallArms) {
      this.id = id;
      this.smallArms = smallArms;
   }

   private EntitiesClassicDamsels(int id) {
      this(id, false);
   }

   public int getId() {
      return this.id;
   }

   public boolean hasSmallArms() {
      return this.smallArms;
   }

   public static EntitiesClassicDamsels getRandomDamsel() {
      int count = values().length;
      Random rand = new Random();
      return values()[rand.nextInt(count)];
   }

   public EntitiesClassicDamsels getById(int id) {
      EntitiesClassicDamsels[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EntitiesClassicDamsels damsel = var2[var4];
         int damselId = damsel.getId();
         if (id == damselId) {
            return damsel;
         }
      }

      return null;
   }
}
