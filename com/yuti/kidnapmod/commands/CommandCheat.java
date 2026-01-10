package com.yuti.kidnapmod.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;

public abstract class CommandCheat extends CommandKidnapMod {
   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      if (sender instanceof MinecraftServer) {
         return true;
      } else if (sender instanceof CommandBlockBaseLogic) {
         return true;
      } else if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender.func_174793_f();
         return server.func_184103_al().func_152596_g(player.func_146103_bH());
      } else {
         return false;
      }
   }
}
