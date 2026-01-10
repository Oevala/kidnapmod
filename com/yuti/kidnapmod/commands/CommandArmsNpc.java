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

public class CommandArmsNpc extends CommandKidnapMod {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "armsnpc";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.armsnpc";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 1 || !options.contains(params[0])) {
            Utils.sendErrorMessageToEntity(player, "Use : /armsnpc [default / small]");
            return;
         }

         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null) {
            boolean valid = false;
            if (params[0].equals("default")) {
               state.setLoadSmallArmsNpc(false);
               valid = true;
            } else if (params[0].equals("small")) {
               state.setLoadSmallArmsNpc(true);
               valid = true;
            }

            if (valid) {
               state.setTimerArmsNpc(new Timer(15));
               Utils.sendValidMessageToEntity(player, "Arms model loaded! Right click (with nothing in your hand) on the npc you want to update. (expires in 15 seconds)");
            } else {
               Utils.sendErrorMessageToEntity(player, "Use : /armsnpc [default / small]");
            }
         }
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
      options.add("default");
      options.add("small");
   }
}
