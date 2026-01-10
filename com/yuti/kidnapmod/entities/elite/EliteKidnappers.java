package com.yuti.kidnapmod.entities.elite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum EliteKidnappers {
   Suki("Suki"),
   Carol("Carol"),
   Athena("Athena"),
   Evelyn("Evelyn");

   private String name;
   private boolean smallArms;

   private EliteKidnappers(String name, boolean smallArms) {
      this.name = name;
      this.smallArms = smallArms;
   }

   private EliteKidnappers(String name) {
      this(name, false);
   }

   public String getName() {
      return this.name;
   }

   public boolean hasSmallArms() {
      return this.smallArms;
   }

   public static EliteKidnappers getRandomKidnapper() {
      int count = values().length;
      Random rand = new Random();
      return values()[rand.nextInt(count)];
   }

   public static List<String> getEliteNames() {
      List<String> names = new ArrayList();
      EliteKidnappers[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         EliteKidnappers kidnapper = var1[var3];
         names.add(kidnapper.toString());
      }

      return names;
   }

   public static EliteKidnappers getByName(String name) {
      if (name != null) {
         EliteKidnappers[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            EliteKidnappers kidnapper = var1[var3];
            String nameKidnapper = kidnapper.toString();
            if (nameKidnapper != null && nameKidnapper.equals(name)) {
               return kidnapper;
            }
         }
      }

      return null;
   }
}
