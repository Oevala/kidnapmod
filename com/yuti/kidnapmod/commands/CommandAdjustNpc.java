package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandAdjustNpc extends CommandKidnapMod {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "adjustnpc";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.adjustnpc";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length >= 1) {
            float adjustValue = 0.0F;
            if (params.length >= 2) {
               try {
                  adjustValue = Float.parseFloat(params[1]);
                  if ((double)adjustValue < -4.0D || (double)adjustValue > 4.0D) {
                     Utils.sendErrorMessageToEntity(player, "The value must me between -4.0 and 4.0");
                     return;
                  }
               } catch (NumberFormatException var7) {
                  Utils.sendErrorMessageToEntity(player, "Wrong number!");
                  return;
               }
            }

            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               if (params[0].equals("gags")) {
                  state.setLoadedNpcAdjustGag(adjustValue);
                  state.setTimerAdjustGagNpc(new Timer(15));
                  Utils.sendValidMessageToEntity(player, "Adjustment loaded! Right click (with nothing in your hand) on the npc to adjust the gag. (expires in 15 seconds)");
                  return;
               }

               if (params[0].equals("blindfolds")) {
                  state.setLoadedNpcAdjustBlindfold(adjustValue);
                  state.setTimerAdjustBlindfoldNpc(new Timer(15));
                  Utils.sendValidMessageToEntity(player, "Adjustment loaded! Right click (with nothing in your hand) on the npc to adjust the blindfold. (expires in 15 seconds)");
                  return;
               }

               Utils.sendInfoMessageToEntity(player, "Use /adjustnpc [gags / blindfolds] value");
               return;
            }
         }

         Utils.sendInfoMessageToEntity(player, "Use /adjustnpc [gags / blindfolds] value");
      }

   }

   public boolean isCallableWhileTiedUp() {
      return false;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      return args.length == 1 ? func_175762_a(args, options) : Collections.emptyList();
   }

   static {
      options.add("gags");
      options.add("blindfolds");
   }
}
