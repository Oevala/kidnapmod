package com.yuti.kidnapmod.proxy;

import com.yuti.kidnapmod.gui.GuiBeingUntiedOverlay;
import com.yuti.kidnapmod.gui.GuiBlindfold;
import com.yuti.kidnapmod.gui.GuiKidnappingOverlay;
import com.yuti.kidnapmod.gui.GuiRestrainedOverlay;
import com.yuti.kidnapmod.gui.GuiSettingTrap;
import com.yuti.kidnapmod.gui.GuiUntyingOverlay;
import com.yuti.kidnapmod.gui.extrainventory.GuiBondageExtended;
import com.yuti.kidnapmod.gui.extrainventory.GuiExtraBondageEvents;
import com.yuti.kidnapmod.init.KeyBindings;
import com.yuti.kidnapmod.models.layers.ExtraBondageItemRenderLayerPlayer;
import com.yuti.kidnapmod.util.handlers.RenderHandler;
import com.yuti.kidnapmod.util.handlers.extrainventoryevents.ClientExtraBondageInventoryEventHandler;
import com.yuti.kidnapmod.util.images.DynamicTextureCache;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
   public void registerItemRenderer(Item item, int meta, String id) {
      ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
   }

   public void registerItemRenderer(Item item, String name, int meta, String id) {
      ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(name, id));
   }

   public void registerColoredItemRenderer(Item item, String id) {
      for(int i = 0; i < 16; ++i) {
         String colorName = EnumDyeColor.func_176764_b(i).func_192396_c();
         ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName() + "_" + colorName, id));
      }

   }

   public void registerColoredItemRenderer(Item item, EnumDyeColor mainDye, String id) {
      ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), id));

      for(int i = 1; i < 16; ++i) {
         EnumDyeColor dye = EnumDyeColor.func_176764_b(i);
         String colorName = dye.func_192396_c();
         if (dye == mainDye) {
            colorName = EnumDyeColor.WHITE.func_192396_c();
         }

         ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(item.getRegistryName() + "_" + colorName, id));
      }

   }

   public void registerRender() {
      RenderHandler.registerEntityRenders();
   }

   public void postInit() {
      MinecraftForge.EVENT_BUS.register(new GuiBlindfold());
      MinecraftForge.EVENT_BUS.register(new GuiKidnappingOverlay());
      MinecraftForge.EVENT_BUS.register(new GuiRestrainedOverlay());
      MinecraftForge.EVENT_BUS.register(new GuiBeingUntiedOverlay());
      MinecraftForge.EVENT_BUS.register(new GuiUntyingOverlay());
      MinecraftForge.EVENT_BUS.register(new GuiSettingTrap());
      this.registerKeyBindings();
   }

   public void preInit() {
   }

   private void registerKeyBindings() {
      Iterator var1 = KeyBindings.keyBindings.iterator();

      while(var1.hasNext()) {
         KeyBinding binding = (KeyBinding)var1.next();
         ClientRegistry.registerKeyBinding(binding);
      }

   }

   public void registerEventHandlers() {
      super.registerEventHandlers();
      MinecraftForge.EVENT_BUS.register(new ClientExtraBondageInventoryEventHandler());
      MinecraftForge.EVENT_BUS.register(DynamicTextureCache.INSTANCE);
      MinecraftForge.EVENT_BUS.register(new GuiExtraBondageEvents());
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      if (world instanceof WorldClient) {
         switch(ID) {
         case 0:
            return new GuiBondageExtended(player);
         }
      }

      return null;
   }

   public World getClientWorld() {
      return FMLClientHandler.instance().getClient().field_71441_e;
   }

   public void init() {
      Map<String, RenderPlayer> skinMap = Minecraft.func_71410_x().func_175598_ae().getSkinMap();
      RenderPlayer render = (RenderPlayer)skinMap.get("default");
      render.func_177094_a(new ExtraBondageItemRenderLayerPlayer(render));
      render = (RenderPlayer)skinMap.get("slim");
      render.func_177094_a(new ExtraBondageItemRenderLayerPlayer(render));
   }
}
