package com.yuti.kidnapmod.proxy;

import com.yuti.kidnapmod.inventory.container.ContainerExtraBondage;
import com.yuti.kidnapmod.util.handlers.extrainventoryevents.EntityExtraBondageInventoryEventHandler;
import com.yuti.kidnapmod.util.handlers.extrainventoryevents.ItemExtraBondageInventoryEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler {
   public void registerItemRenderer(Item item, int meta, String id) {
   }

   public void registerItemRenderer(Item item, String name, int meta, String id) {
   }

   public void registerColoredItemRenderer(Item item, String id) {
   }

   public void registerColoredItemRenderer(Item item, EnumDyeColor mainDye, String id) {
   }

   public void registerRender() {
   }

   public void postInit() {
   }

   public void preInit() {
   }

   public void init() {
   }

   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      return null;
   }

   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch(ID) {
      case 0:
         return new ContainerExtraBondage(player.field_71071_by, !world.field_72995_K, player);
      default:
         return null;
      }
   }

   public World getClientWorld() {
      return null;
   }

   public void registerEventHandlers() {
      MinecraftForge.EVENT_BUS.register(new EntityExtraBondageInventoryEventHandler());
      MinecraftForge.EVENT_BUS.register(new ItemExtraBondageInventoryEventHandler());
   }
}
