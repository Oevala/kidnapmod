package com.yuti.kidnapmod.common;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Type;

@Config(
   modid = "knapm",
   name = "knapm/knapm"
)
public class ModConfig {
   @Comment({"Enable or disable render of name (of the client's player) when wearing a collar. (client side only)"})
   public static boolean displaySelfSlaveName = true;
   @Comment({"Enable or disable dynamic textures loading (for dynamic restraints / clothes). (client side only)"})
   public static boolean displayDynamicTextures = true;
   @Comment({"Enable or disable transparents skins with dynamic textures. (client side only)"})
   public static boolean displayTransparentSkins = true;
   @Comment({"Enable or disable the message about the mod when you join a world. (client side only)"})
   public static boolean modInfosOnLog = true;
   @Comment({"Enable or disable kidnapper jobs (server side only)"})
   public static boolean kidnappersJob = true;
   @Comment({"Enable or disable default jobs loading (from the mod) (server side only)"})
   public static boolean loadDefaultJobs = true;
   @Comment({"Enable or disable kidnapper sales, when a player is captrued (server side only)"})
   public static boolean kidnappersSell = true;
   @Comment({"Enable or disable default kidnapper sales loading (from the mod) (server side only)"})
   public static boolean loadDefaultSales = true;

   public void save() {
      ConfigManager.sync("knapm", Type.INSTANCE);
   }
}
