package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.KidnappingSettingsData;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandConsentBoard extends CommandTargetPlayer {
   private static List<String> options = new ArrayList();

   public CommandConsentBoard() {
      this.indexPlayerParam = 1;
   }

   public String func_71517_b() {
      return "consentboard";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.consentboard";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 1) {
            Utils.sendErrorMessageToEntity(player, "Use : /consentboard [check / join / leave]");
            return;
         }

         KidnappingSettingsData settings;
         if (params[0].equals("join")) {
            settings = KidnappingSettingsData.get(player.field_70170_p);
            if (settings != null) {
               settings.addToConsentBoard(player);
            }
         } else if (params[0].equals("leave")) {
            settings = KidnappingSettingsData.get(player.field_70170_p);
            if (settings != null) {
               settings.removeFromConsentBoard(player);
            }
         } else if (params[0].equals("check")) {
            EntityPlayer target = this.getTarget(server, sender, params);
            if (target != null) {
               KidnappingSettingsData settings = KidnappingSettingsData.get(target.field_70170_p);
               if (settings != null) {
                  if (settings.isOnConsentBoard(target)) {
                     Utils.sendValidMessageToEntity(player, target.func_70005_c_() + " is on the consent board!");
                  } else {
                     Utils.sendErrorMessageToEntity(player, target.func_70005_c_() + " is not on the consent board.");
                  }
               }
            }
         }
      }

   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 1) {
         return func_175762_a(args, options);
      } else {
         return args.length == 2 && args[0].equals("check") ? super.func_184883_a(server, sender, args, targetPos) : Collections.emptyList();
      }
   }

   static {
      options.add("check");
      options.add("join");
      options.add("leave");
   }
}
