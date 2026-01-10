package com.yuti.kidnapmod.entities.guests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum GuestsDamsels {
   Fuya_kitty("Fuya_kitty"),
   Junichi("Junichi", true),
   Ceras("Ceras", true),
   Creamy("Creamy", true),
   Mani("Mani", true),
   Blizz("Blizz", true),
   Hazey("Hazey", true),
   Risette("Risette"),
   Pika("Pika", true),
   Sayari("Sayari", true),
   Kyky("Kyky", true),
   Glacie("Glacie", true),
   Kitty("Kitty"),
   Anastasia("Anastasia", true),
   El("El", true),
   Raph("Raph"),
   Nobu("Nobu"),
   Sui("Sui", true),
   Roxy("Roxy"),
   Laureen("Laureen", true);

   private String name;
   private boolean smallArms;

   private GuestsDamsels(String name, boolean smallAarms) {
      this.name = name;
      this.smallArms = smallAarms;
   }

   private GuestsDamsels(String name) {
      this(name, false);
   }

   public String getName() {
      return this.name;
   }

   public boolean hasSmallArms() {
      return this.smallArms;
   }

   public static GuestsDamsels getRandomDamsel() {
      int count = values().length;
      Random rand = new Random();
      return values()[rand.nextInt(count)];
   }

   public static List<String> getGuestsNames() {
      List<String> names = new ArrayList();
      GuestsDamsels[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         GuestsDamsels damsel = var1[var3];
         names.add(damsel.toString());
      }

      return names;
   }

   public static GuestsDamsels getByName(String name) {
      if (name != null) {
         GuestsDamsels[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            GuestsDamsels damsel = var1[var3];
            String nameDamsel = damsel.toString();
            if (nameDamsel != null && nameDamsel.equals(name)) {
               return damsel;
            }
         }
      }

      return null;
   }
}
