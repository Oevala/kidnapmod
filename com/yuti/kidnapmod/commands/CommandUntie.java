package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandUntie extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      List<PlayerBindState> states = this.getTargetsState(server, sender, params);
      Iterator var5 = states.iterator();

      while(var5.hasNext()) {
         PlayerBindState state = (PlayerBindState)var5.next();
         if (state != null && state.isTiedUp()) {
            state.dropBondageItems();
            if (state.isSlave()) {
               state.free();
            }

            state.untie();
            Utils.sendValidMessageToEntity(sender, state.getPlayer().func_70005_c_() + " is now untied.");
            Utils.sendValidMessageToEntity(state.getPlayer(), "You have been untied!");
         }
      }

   }

   public String func_71517_b() {
      return "untie";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.untie";
   }
}
