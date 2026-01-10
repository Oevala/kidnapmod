package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.items.IHasResistance;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandRestraintResistance extends CommandCheat {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "restraintresistance";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.restraintresistance";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null && player.field_70170_p != null) {
            if (params.length < 1) {
               Utils.sendErrorMessageToEntity(player, "Use : /restraintresistance [set / struggle] [value]");
               return;
            }

            ItemStack stack = player.func_184614_ca();
            if (stack == null || !(stack.func_77973_b() instanceof IHasResistance)) {
               Utils.sendErrorMessageToEntity(player, "You must held binds or a collar!");
               return;
            }

            IHasResistance resistanceItem = (IHasResistance)stack.func_77973_b();
            if (!params[0].equals("set")) {
               if (params[0].equals("struggle")) {
                  if (resistanceItem.canBeStruggledOut(stack)) {
                     resistanceItem.setCanBeStruggledOut(stack, false);
                     Utils.sendValidMessageToEntity(player, "Struggling disabled on this item.");
                     return;
                  }

                  resistanceItem.setCanBeStruggledOut(stack, true);
                  Utils.sendValidMessageToEntity(player, "Struggling enabled on this item.");
                  return;
               }

               Utils.sendErrorMessageToEntity(player, "Use : /restraintresistance [set / struggle] [value]");
               return;
            }

            if (params.length < 2) {
               int value = resistanceItem.getBaseResistance(player.field_70170_p);
               Utils.setResistance(stack, value);
               Utils.sendValidMessageToEntity(player, "Resistance set to " + value);
               return;
            }

            String valueStr = params[1];

            try {
               int value = Integer.parseInt(valueStr);
               if (value < 0) {
                  Utils.sendErrorMessageToEntity(player, "The value must be >= 0");
                  return;
               }

               Utils.setResistance(stack, value);
               Utils.sendValidMessageToEntity(player, "Resistance set to " + value);
            } catch (NumberFormatException var9) {
               Utils.sendErrorMessageToEntity(player, "Wrong number for the value");
               return;
            }
         }
      }

   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      return args.length == 1 ? func_175762_a(args, options) : super.func_184883_a(server, sender, args, targetPos);
   }

   static {
      options.add("set");
      options.add("struggle");
   }
}
