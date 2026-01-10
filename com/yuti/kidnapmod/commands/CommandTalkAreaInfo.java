package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.UsersSettingsData;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandTalkAreaInfo extends CommandKidnapMod {
   public String func_71517_b() {
      return "talkinfo";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.talkinfo";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         UsersSettingsData settings = UsersSettingsData.get(player.field_70170_p);
         if (settings != null) {
            int area = settings.getTalkArea(player);
            if (area >= 0) {
               Utils.sendValidMessageToEntity(player, "Your talk area is set on " + area);
            } else {
               Utils.sendValidMessageToEntity(player, "Your talk area is not enabled");
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
