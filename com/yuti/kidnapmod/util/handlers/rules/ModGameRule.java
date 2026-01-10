package com.yuti.kidnapmod.util.handlers.rules;

import net.minecraft.world.GameRules.ValueType;

public class ModGameRule {
   private String id;
   private String value;
   private ValueType type;

   public ModGameRule(String id, String value, ValueType type) {
      this.id = id;
      this.value = value;
      this.type = type;
   }

   public String getId() {
      return this.id;
   }

   public String getValue() {
      return this.value;
   }

   public ValueType getType() {
      return this.type;
   }
}
