package com.yuti.kidnapmod.util.handlers;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.entities.classic.EntityClassicDamsel;
import com.yuti.kidnapmod.entities.classic.EntityClassicKidnapper;
import com.yuti.kidnapmod.entities.elite.EntityKidnapperElite;
import com.yuti.kidnapmod.entities.guests.EntityGuestDamsel;
import com.yuti.kidnapmod.entities.guests.EntityGuestKidnapper;
import com.yuti.kidnapmod.init.Entities;
import com.yuti.kidnapmod.init.ExtraRecipes;
import com.yuti.kidnapmod.init.KidnapModSoundEvents;
import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.IHasVariants;
import com.yuti.kidnapmod.tileentity.TileEntityBlockPaddedPane;
import com.yuti.kidnapmod.tileentity.TileEntityKidnapBomb;
import com.yuti.kidnapmod.tileentity.TileEntityTrap;
import com.yuti.kidnapmod.tileentity.TileEntityTrappedBed;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber
public class RegistryHandler {
   @SubscribeEvent
   public static void onItemRegister(Register<Item> event) {
      event.getRegistry().registerAll((IForgeRegistryEntry[])ModItems.ITEMS.toArray(new Item[0]));
   }

   @SubscribeEvent
   public static void onBlockRegister(Register<Block> event) {
      event.getRegistry().registerAll((IForgeRegistryEntry[])ModBlocks.BLOCKS.toArray(new Block[0]));
   }

   @SubscribeEvent
   public static void onModelRegister(ModelRegistryEvent event) {
      Iterator var1 = ModItems.ITEMS.iterator();

      while(var1.hasNext()) {
         Item item = (Item)var1.next();
         if (item instanceof IHasModel) {
            ((IHasModel)item).registerModels();
         }

         if (item instanceof IHasVariants) {
            ((IHasVariants)item).registerVariants();
         }
      }

      var1 = ModBlocks.BLOCKS.iterator();

      while(var1.hasNext()) {
         Block block = (Block)var1.next();
         if (block instanceof IHasModel) {
            ((IHasModel)block).registerModels();
         }
      }

   }

   @SubscribeEvent
   public static void onRegisterSounds(Register<SoundEvent> event) {
      event.getRegistry().registerAll((IForgeRegistryEntry[])KidnapModSoundEvents.SOUNDS.toArray(new SoundEvent[0]));
   }

   @SubscribeEvent
   public static void onRegisterExtraRecipes(Register<IRecipe> event) {
      event.getRegistry().registerAll((IForgeRegistryEntry[])ExtraRecipes.RECIPES.toArray(new IRecipe[0]));
   }

   public static void preInitRegistries(FMLPreInitializationEvent event) {
      Entities.registerEntities();
      KidnapModMain.proxy.registerRender();
      EntityRegistry.addSpawn(EntityClassicKidnapper.class, 8, 1, 1, EnumCreatureType.CREATURE, Utils.biomesForKidnappers);
      EntityRegistry.addSpawn(EntityClassicDamsel.class, 8, 1, 1, EnumCreatureType.CREATURE, Utils.biomesForKidnappers);
      EntityRegistry.addSpawn(EntityGuestDamsel.class, 4, 1, 1, EnumCreatureType.CREATURE, Utils.biomesForKidnappers);
      EntityRegistry.addSpawn(EntityGuestKidnapper.class, 4, 1, 1, EnumCreatureType.CREATURE, Utils.biomesForKidnappers);
      EntityRegistry.addSpawn(EntityKidnapperElite.class, 6, 1, 1, EnumCreatureType.CREATURE, Utils.biomesForKidnappers);
   }

   public static void registerTileEntities() {
      GameRegistry.registerTileEntity(TileEntityBlockPaddedPane.class, "knapm_padded_pane_tile");
      GameRegistry.registerTileEntity(TileEntityTrap.class, "knapm_bind_trap_tile");
      GameRegistry.registerTileEntity(TileEntityTrappedBed.class, "knapm_trapped_bondage_bed_tile");
      GameRegistry.registerTileEntity(TileEntityKidnapBomb.class, "knapm_kidnap_bomb_tile");
   }

   public static void registerFurnaceRecipes() {
      GameRegistry.addSmelting(Blocks.field_150325_L, new ItemStack(ModBlocks.PADDED_BLOCK, 1), 0.7F);
      GameRegistry.addSmelting(Items.field_151129_at, new ItemStack(ModItems.CHLOROFORM_BOTTLE, 1), 0.7F);
   }
}
