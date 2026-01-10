package com.yuti.kidnapmod.util;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class UtilsParameters {
   private static int TYING_UP_PLAYER_DELAY = 5;
   private static int UNTYING_UP_DELAY = 10;
   private static int TRAP_PLACE_DELAY = 5;
   private static int TYING_UP_DELAY_KIDNAPPER = 5;
   private static int TYING_UP_DELAY_ELITE = 3;
   private static int RESISTANCE_MERGE_PERCENT = -1;
   private static int SHOCKER_MERGE_MAX = -1;
   private static boolean CHLOROFORM_ENABLED = true;
   private static int CHLOROFORM_RAG_TIME = 10;
   private static int CHLOROFORM_EFFECT_DURATION = 10;
   private static int ME_RADIUS = 10;
   private static int KIDNAP_BOMB_RADIUS = 10;

   public static int getTyingUpPlayerDelay(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("tying_player_time")) {
            return rules.func_180263_c("tying_player_time");
         }
      }

      return TYING_UP_PLAYER_DELAY;
   }

   public static int getTyingUpKidnapperDelay(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("tying_kidnapper_time")) {
            return rules.func_180263_c("tying_kidnapper_time");
         }
      }

      return TYING_UP_DELAY_KIDNAPPER;
   }

   public static int getTyingUpKidnapperEliteDelay(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("tying_kidnapper_elite_time")) {
            return rules.func_180263_c("tying_kidnapper_elite_time");
         }
      }

      return TYING_UP_DELAY_ELITE;
   }

   public static int getUntyingUpPlayerDelay(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("untying_player_time")) {
            return rules.func_180263_c("untying_player_time");
         }
      }

      return UNTYING_UP_DELAY;
   }

   public static int getTrapPlaceDelay(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("trap_place_time")) {
            return rules.func_180263_c("trap_place_time");
         }
      }

      return TRAP_PLACE_DELAY;
   }

   public static int getResistanceMergeMaxPercent(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("resistance_merge_max_percent")) {
            return rules.func_180263_c("resistance_merge_max_percent");
         }
      }

      return RESISTANCE_MERGE_PERCENT;
   }

   public static int getShockerMergeMax(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("shocker_merge_max")) {
            return rules.func_180263_c("shocker_merge_max");
         }
      }

      return SHOCKER_MERGE_MAX;
   }

   public static boolean getChloroformEnabled(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("chloroform")) {
            return rules.func_82766_b("chloroform");
         }
      }

      return CHLOROFORM_ENABLED;
   }

   public static int getChloroformRagTime(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("chloroform_rag_time")) {
            return rules.func_180263_c("chloroform_rag_time");
         }
      }

      return CHLOROFORM_RAG_TIME;
   }

   public static int getChloroformEffectDuration(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("chloroform_effect_duration")) {
            return rules.func_180263_c("chloroform_effect_duration");
         }
      }

      return CHLOROFORM_EFFECT_DURATION;
   }

   public static int getMeRadius(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("me_radius")) {
            return rules.func_180263_c("me_radius");
         }
      }

      return ME_RADIUS;
   }

   public static int getKidnapBombRadius(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("kidnap_bomb_radius")) {
            return rules.func_180263_c("kidnap_bomb_radius");
         }
      }

      return KIDNAP_BOMB_RADIUS;
   }
}
