package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.UsersSettingsData;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandAreaPref extends CommandKidnapMod {
   public String func_71517_b() {
      return "talkpref";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.talkpref";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         UsersSettingsData settings = UsersSettingsData.get(player.field_70170_p);
         if (settings != null) {
            int value;
            if (params.length > 0) {
               try {
                  value = Integer.valueOf(params[0]);
                  if (value < 0) {
                     Utils.sendErrorMessageToEntity(sender, "You must specify a positive value (ex : 100)");
                  } else {
                     settings.setTalkAreaPref(player, value);
                     Utils.sendValidMessageToEntity(sender, "Talk area preference set to " + value + " blocks");
                  }
               } catch (NumberFormatException var7) {
                  Utils.sendErrorMessageToEntity(sender, "You must specify a distance value (ex : 100)");
               }
            } else {
               value = settings.getTalkAreaPref(player);
               if (value == -1) {
                  Utils.sendErrorMessageToEntity(sender, "Your talkarea preference is not set yet. You must specify a distance value (ex : 100)");
               } else {
                  settings.setTalkArea(player, value);
                  Utils.sendValidMessageToEntity(sender, "Talk area set to " + value + " blocks");
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
}
