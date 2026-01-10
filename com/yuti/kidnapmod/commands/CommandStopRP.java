package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.loaders.common.StopRpManager;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandStopRP extends CommandKidnapMod {
   public String func_71517_b() {
      return "norp";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.norp";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null && player.field_70170_p != null) {
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               Timer timer = state.getTimerNoRp();
               if (timer != null && timer.getSecondsRemaining() > 0) {
                  int secondsRemaining = timer.getSecondsRemaining();
                  Utils.sendMessageToEntity(player, "You have to wait " + secondsRemaining + " seconds before sending this message again.");
                  return;
               }

               TextComponentString message = new TextComponentString(player.func_70005_c_() + " does not agree with the current RP. Please, let them go.");
               message.func_150256_b().func_150238_a(TextFormatting.YELLOW);
               Utils.sendMessageToPlayersInArea(player, 20.0D, message, true);
               timer = new Timer(45);
               state.setTimerNoRp(timer);
               this.writeNoRpLog(player);
               Timer warnOpTimer = state.getTimerWarnOpNoRp();
               if (warnOpTimer != null && warnOpTimer.getSecondsRemaining() > 0) {
                  Utils.sendMessageToOps(player.field_70170_p, player.func_70005_c_() + " sent few /norp in a short amount of time");
               } else {
                  state.setTimerWarnOpNoRp(new Timer(180));
               }
            }
         }
      }

   }

   private void writeNoRpLog(EntityPlayer player) {
      if (player != null && player.func_70005_c_() != null) {
         List<String> playersName = new ArrayList();
         List<EntityPlayerMP> players = PlayerBindState.getPlayerAround(player.field_70170_p, player.func_180425_c(), 20.0D);
         if (players != null) {
            Iterator var4 = players.iterator();

            while(var4.hasNext()) {
               EntityPlayer playerAround = (EntityPlayer)var4.next();
               if (playerAround != null) {
                  String name = playerAround.func_70005_c_();
                  if (name != null) {
                     playersName.add(name);
                  }
               }
            }
         }

         StopRpManager.writeNoRpLog(player.func_70005_c_(), playersName);
      }

   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }
}
