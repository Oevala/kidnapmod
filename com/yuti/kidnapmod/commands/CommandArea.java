package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.UsersSettingsData;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandArea extends CommandKidnapMod {
   public String func_71517_b() {
      return "talkarea";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.talkarea";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         UsersSettingsData settings = UsersSettingsData.get(player.field_70170_p);
         if (settings != null) {
            if (params.length > 0) {
               try {
                  int value = Integer.valueOf(params[0]);
                  if (value < 0) {
                     Utils.sendErrorMessageToEntity(sender, "You must specify a positive value (ex : 100)");
                  } else {
                     settings.setTalkArea(player, value);
                     Utils.sendValidMessageToEntity(sender, "Talk area set to " + value + " blocks");
                  }
               } catch (NumberFormatException var7) {
                  Utils.sendErrorMessageToEntity(sender, "You must specify a distance value (ex : 100)");
               }
            } else {
               settings.setTalkArea(player, -1);
               Utils.sendValidMessageToEntity(sender, "Talk area disabled");
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
}
