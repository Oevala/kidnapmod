package com.yuti.kidnapmod.entities.guests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum GuestsKidnappers {
   Wynter("Wynter"),
   Kyra("Kyra", true),
   Ketulu("ketulu", true),
   Ruby("Ruby", true),
   Welphia("Welphia", true),
   Jass("Jass"),
   Yuti("Yuti"),
   Nico("Nico", true),
   Esther("Esther"),
   Nataleigh("Nataleigh"),
   Fleur_Dianthus("Fleur_Dianthus", true),
   Teemo("Teemo"),
   Ryuko("Ryuko", true),
   Darkie("Darkie", true),
   Jack("Jack"),
   Dean("Dean"),
   Kitty("Kitty"),
   Eleanor("Eleanor", true),
   Zephyr("Zephyr"),
   Lucina("Lucina", true),
   Blake("Blake"),
   Fuya("Fuya", true),
   Misty("Misty", true);

   private String name;
   private boolean smallArms;

   private GuestsKidnappers(String name, boolean smallArms) {
      this.name = name;
      this.smallArms = smallArms;
   }

   private GuestsKidnappers(String name) {
      this(name, false);
   }

   public String getName() {
      return this.name;
   }

   public boolean hasSmallArms() {
      return this.smallArms;
   }

   public static GuestsKidnappers getRandomKidnapper() {
      int count = values().length;
      Random rand = new Random();
      return values()[rand.nextInt(count)];
   }

   public static List<String> getGuestsNames() {
      List<String> names = new ArrayList();
      GuestsKidnappers[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         GuestsKidnappers kidnapper = var1[var3];
         names.add(kidnapper.toString());
      }

      return names;
   }

   public static GuestsKidnappers getByName(String name) {
      if (name != null) {
         GuestsKidnappers[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            GuestsKidnappers kidnapper = var1[var3];
            String nameKidnapper = kidnapper.toString();
            if (nameKidnapper != null && nameKidnapper.equals(name)) {
               return kidnapper;
            }
         }
      }

      return null;
   }
}
