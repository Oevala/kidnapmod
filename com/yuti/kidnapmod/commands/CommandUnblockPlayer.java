package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.UsersSettingsData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandUnblockPlayer extends CommandTargetPlayer {
   public String func_71517_b() {
      return "unblockplayer";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.unblockplayer";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         EntityPlayer blocked = this.getTarget(server, sender, args);
         if (blocked != null) {
            UsersSettingsData settings = UsersSettingsData.get(player.field_70170_p);
            if (settings != null) {
               settings.unblockPlayer(player, blocked);
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
