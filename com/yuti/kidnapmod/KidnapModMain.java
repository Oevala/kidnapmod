package com.yuti.kidnapmod;

import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.loaders.LoadersInit;
import com.yuti.kidnapmod.loaders.jobs.JobLoader;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.proxy.CommonProxy;
import com.yuti.kidnapmod.util.handlers.BehaviorRegistryHandler;
import com.yuti.kidnapmod.util.handlers.CapabilitiesRegistryHandler;
import com.yuti.kidnapmod.util.handlers.CommandsRegisterHandler;
import com.yuti.kidnapmod.util.handlers.RegistryHandler;
import com.yuti.kidnapmod.util.handlers.rules.GameRulesRegistryHandler;
import net.minecraft.block.BlockDispenser;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(
   modid = "knapm",
   name = "Kidnap Mod",
   version = "1.3.0",
   acceptedMinecraftVersions = "[1.12.2]"
)
public class KidnapModMain {
   @Instance("knapm")
   public static KidnapModMain instance;
   @SidedProxy(
      clientSide = "com.yuti.kidnapmod.proxy.ClientProxy",
      serverSide = "com.yuti.kidnapmod.proxy.CommonProxy"
   )
   public static CommonProxy proxy;
   public static ModConfig config;
   public static JobLoader jobloader;

   @EventHandler
   public static void PreInit(FMLPreInitializationEvent event) {
      config = new ModConfig();
      RegistryHandler.preInitRegistries(event);
      PacketHandler.registerMessages("knapm");
      CapabilitiesRegistryHandler.registerCapabilities();
      KidnapSettingsCapabilities.register();
      proxy.preInit();
      proxy.registerEventHandlers();
   }

   @EventHandler
   public static void init(FMLInitializationEvent event) {
      RegistryHandler.registerTileEntities();
      RegistryHandler.registerFurnaceRecipes();
      NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
      proxy.init();
   }

   @EventHandler
   public static void PostInit(FMLPostInitializationEvent event) {
      proxy.postInit();
   }

   @EventHandler
   public static void initServer(FMLServerStartingEvent event) {
      LoadersInit.init();
      GameRulesRegistryHandler.init(event.getServer());
      CommandsRegisterHandler.registerCommands(event);
      BehaviorRegistryHandler.registerBehaviors(BlockDispenser.field_149943_a);
   }
}
