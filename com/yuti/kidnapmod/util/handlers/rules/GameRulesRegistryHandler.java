package com.yuti.kidnapmod.util.handlers.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.ValueType;

public class GameRulesRegistryHandler {
   private static List<ModGameRule> gamerules = new ArrayList();

   public static void preLoad(ModGameRule rule) {
      gamerules.add(rule);
   }

   public static void preLoad(String id, String value, ValueType type) {
      preLoad(new ModGameRule(id, value, type));
   }

   public static void init(MinecraftServer server) {
      preInit();
      if (server != null) {
         World world = server.func_130014_f_();
         if (world != null) {
            GameRules worldRules = world.func_82736_K();
            if (worldRules != null) {
               Iterator var3 = gamerules.iterator();

               while(var3.hasNext()) {
                  ModGameRule rule = (ModGameRule)var3.next();
                  if (rule != null && !worldRules.func_82765_e(rule.getId())) {
                     worldRules.func_180262_a(rule.getId(), rule.getValue(), rule.getType());
                  }
               }
            }
         }
      }

   }

   public static void preInit() {
      preLoad("max_bounties", "5", ValueType.NUMERICAL_VALUE);
      preLoad("time_bounties", "14400", ValueType.NUMERICAL_VALUE);
      preLoad("damsels-spawn", "true", ValueType.BOOLEAN_VALUE);
      preLoad("kidnappers-spawn", "true", ValueType.BOOLEAN_VALUE);
      preLoad("kidnappers-spawn-elite", "true", ValueType.BOOLEAN_VALUE);
      preLoad("kidnappers_jobs", "true", ValueType.BOOLEAN_VALUE);
      preLoad("kidnappers_sell", "true", ValueType.BOOLEAN_VALUE);
      preLoad("struggle_collar_random_shock", "20", ValueType.NUMERICAL_VALUE);
      preLoad("betterGagTalk", "false", ValueType.BOOLEAN_VALUE);
      preLoad("struggle", "true", ValueType.BOOLEAN_VALUE);
      preLoad("probability_struggle", "40", ValueType.NUMERICAL_VALUE);
      preLoad("struggle_min_decrease", "1", ValueType.NUMERICAL_VALUE);
      preLoad("struggle_max_decrease", "10", ValueType.NUMERICAL_VALUE);
      preLoad("struggle_timer", "20", ValueType.NUMERICAL_VALUE);
      preLoad("struggle_collar", "true", ValueType.BOOLEAN_VALUE);
      preLoad("probability_struggle_collar", "40", ValueType.NUMERICAL_VALUE);
      preLoad("struggle_min_decrease_collar", "1", ValueType.NUMERICAL_VALUE);
      preLoad("struggle_max_decrease_collar", "10", ValueType.NUMERICAL_VALUE);
      preLoad("struggle_timer_collar", "20", ValueType.NUMERICAL_VALUE);
      preLoad("slave_starve_warn", "true", ValueType.BOOLEAN_VALUE);
      preLoad("tying_player_time", "5", ValueType.NUMERICAL_VALUE);
      preLoad("tying_kidnapper_time", "5", ValueType.NUMERICAL_VALUE);
      preLoad("tying_kidnapper_elite_time", "3", ValueType.NUMERICAL_VALUE);
      preLoad("untying_player_time", "10", ValueType.NUMERICAL_VALUE);
      preLoad("trap_place_time", "5", ValueType.NUMERICAL_VALUE);
      preLoad("resistance_merge_max_percent", "-1", ValueType.NUMERICAL_VALUE);
      preLoad("shocker_merge_max", "-1", ValueType.NUMERICAL_VALUE);
      preLoad("chloroform", "true", ValueType.BOOLEAN_VALUE);
      preLoad("chloroform_rag_time", "10", ValueType.NUMERICAL_VALUE);
      preLoad("chloroform_effect_duration", "15", ValueType.NUMERICAL_VALUE);
      preLoad("shocker_controller_base_radius", "50", ValueType.NUMERICAL_VALUE);
      preLoad("me_radius", "10", ValueType.NUMERICAL_VALUE);
      preLoad("kidnap_bomb_radius", "10", ValueType.NUMERICAL_VALUE);
      preLoad("probability_rope_arrow_infinity", "5", ValueType.NUMERICAL_VALUE);
      preLoad("probability_rope_arrow", "80", ValueType.NUMERICAL_VALUE);
      preLoad("max_kidnapping_locations", "10", ValueType.NUMERICAL_VALUE);
      preLoad("time_callback_slave", "60", ValueType.NUMERICAL_VALUE);
      preLoad("birdsteregg", "true", ValueType.BOOLEAN_VALUE);
   }
}
