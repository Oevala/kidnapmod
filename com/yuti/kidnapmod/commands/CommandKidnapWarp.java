package com.yuti.kidnapmod.commands;

import com.google.common.base.Joiner;
import com.yuti.kidnapmod.data.KidnappingSettingsData;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.PlayerKidnapperManager;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKidnapWarp extends CommandKidnapMod {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "kidnapwarp";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.kidnapwarp";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 1 || !options.contains(params[0])) {
            Utils.sendErrorMessageToEntity(player, "Use : /kidnapwarp [list / set / remove / tp] [warp_name]");
            return;
         }

         KidnappingSettingsData settings = KidnappingSettingsData.get(player.field_70170_p);
         if (settings != null) {
            if (params[0].equals("list")) {
               Set<String> warps = settings.getKidnappingLocationsNames(player);
               if (warps != null && !warps.isEmpty()) {
                  String toShow = Joiner.on(", ").join(warps);
                  Utils.sendMessageToEntity(player, toShow);
               } else {
                  Utils.sendMessageToEntity(player, "You have no kidnapping warps.");
               }
            } else if (params.length >= 2) {
               String warpName = params[1];
               if (params[0].equals("set")) {
                  settings.setLocation(player, warpName);
               } else if (params[0].equals("remove")) {
                  settings.deleteLocations(player, warpName);
               } else if (params[0].equals("tp") && player != null) {
                  Position pos = settings.getLocation(player.func_110124_au(), warpName);
                  if (pos != null && player != null) {
                     PlayerBindState state = PlayerBindState.getInstance(player);
                     if (state != null) {
                        PlayerKidnapperManager manager = state.getSlaveHolderManager();
                        if (manager != null) {
                           manager.teleportToKidnappingWarp(pos);
                        }
                     }
                  } else {
                     Utils.sendErrorMessageToEntity(player, "This warp doesn't exist.");
                  }
               }
            } else {
               Utils.sendErrorMessageToEntity(player, "Use : /kidnapwarp [list / set / remove / tp] [warp_name]");
            }
         }
      }

   }

   public boolean isCallableWhileTiedUp() {
      return false;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 1) {
         return func_175762_a(args, options);
      } else {
         if (args.length == 2 && !args[0].equals("list") && sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            KidnappingSettingsData settings = KidnappingSettingsData.get(player.field_70170_p);
            if (settings != null) {
               Set<String> warps = settings.getKidnappingLocationsNames(player);
               if (warps != null) {
                  return func_175762_a(args, warps);
               }
            }
         }

         return Collections.emptyList();
      }
   }

   static {
      options.add("list");
      options.add("set");
      options.add("remove");
      options.add("tp");
   }
}
