package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandPrivateMessage extends CommandTargetPlayer {
   public String func_71517_b() {
      return "pm";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.pm";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (params != null && params.length >= 2) {
         List<EntityPlayerMP> players = this.getTargets(server, sender, params);
         Iterator var5 = players.iterator();

         while(var5.hasNext()) {
            EntityPlayerMP target = (EntityPlayerMP)var5.next();
            if (target != null) {
               String message = Utils.getMessageFromArray(params, 1, params.length);
               TextComponentString messageToSender = new TextComponentString("You -> " + target.func_70005_c_() + " : " + message);
               messageToSender.func_150256_b().func_150238_a(TextFormatting.AQUA);
               sender.func_145747_a(messageToSender);
               TextComponentString messageToTarget = new TextComponentString(sender.func_70005_c_() + " -> You : " + message);
               messageToTarget.func_150256_b().func_150238_a(TextFormatting.AQUA);
               target.func_145747_a(messageToTarget);
            }
         }
      } else {
         Utils.sendErrorMessageToEntity(sender, "Wrong number of parameters.");
      }

   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }
}
