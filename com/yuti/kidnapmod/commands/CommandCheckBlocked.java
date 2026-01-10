package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.UsersSettingsData;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandCheckBlocked extends CommandTargetPlayer {
   public String func_71517_b() {
      return "checkblocked";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.checkblocked";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         EntityPlayer blocked = this.getTarget(server, sender, args);
         if (blocked != null) {
            UsersSettingsData settings = UsersSettingsData.get(player.field_70170_p);
            if (settings.checkBlocked(player, blocked)) {
               Utils.sendValidMessageToEntity(player, "This player is blocked");
            } else {
               Utils.sendValidMessageToEntity(player, "This player is not blocked");
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

   public boolean isBypassingBlock() {
      return true;
   }
}
