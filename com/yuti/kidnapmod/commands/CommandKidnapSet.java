package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModItems;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandKidnapSet extends CommandCheat {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            ItemStack ropes = new ItemStack(ModItems.ROPES, 16);
            ItemStack gag = new ItemStack(ModItems.CLOTH_GAG, 16);
            ItemStack iron_knife = new ItemStack(ModItems.IRON_KNIFE, 5);
            ItemStack stone_knife = new ItemStack(ModItems.STONE_KNIFE, 5);
            ItemStack gold_knife = new ItemStack(ModItems.GOLD_KNIFE, 5);
            ItemStack traps = new ItemStack(Item.func_150898_a(ModBlocks.ROPES_TRAP), 15);
            ItemStack padded_block = new ItemStack(ModBlocks.PADDED_BLOCK, 64);
            ItemStack padded_pane = new ItemStack(ModBlocks.PADDED_BLOCK, 64);
            ItemStack cell_door = new ItemStack(ModBlocks.CELL_DOOR, 16);
            ItemStack leads = new ItemStack(Items.field_151058_ca, 4);
            player.func_191521_c(ropes);
            player.func_191521_c(gag);
            player.func_191521_c(leads);
            player.func_191521_c(iron_knife);
            player.func_191521_c(stone_knife);
            player.func_191521_c(gold_knife);
            player.func_191521_c(traps);
            player.func_191521_c(padded_block);
            player.func_191521_c(padded_pane);
            player.func_191521_c(cell_door);
         }
      }

   }

   public String func_71517_b() {
      return "kidnapset";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.kidnapset";
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      boolean canExecute = false;
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         canExecute = player.func_184812_l_();
      }

      return canExecute || super.func_184882_a(server, sender);
   }
}
