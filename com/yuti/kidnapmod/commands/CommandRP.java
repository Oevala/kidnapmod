package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.UtilsParameters;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandRP extends CommandKidnapMod {
   public String func_71517_b() {
      return "me";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.me";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer && params != null && params.length >= 1) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            String name = player.func_70005_c_();
            PlayerBindState state = PlayerBindState.getInstance(player);
            String collarName;
            if (state != null) {
               collarName = state.getNameFromCollar();
               if (collarName != null) {
                  name = "*" + collarName + "*";
               }
            }

            collarName = Utils.getMessageFromArray(params, 0, params.length);
            TextComponentString message = new TextComponentString(name + " " + collarName);
            message.func_150256_b().func_150217_b(true);
            message.func_150256_b().func_150238_a(TextFormatting.LIGHT_PURPLE);
            int radius = UtilsParameters.getMeRadius(player.field_70170_p);
            Utils.sendMessageToPlayersInArea(player, (double)radius, message, true);
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
