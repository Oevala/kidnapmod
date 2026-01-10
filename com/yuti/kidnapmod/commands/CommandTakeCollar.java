package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandTakeCollar extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      List<PlayerBindState> states = this.getTargetsState(server, sender, params);
      Iterator var5 = states.iterator();

      while(var5.hasNext()) {
         PlayerBindState state = (PlayerBindState)var5.next();
         if (state != null && state.hasCollar()) {
            ItemStack collar = state.takesCollarOff(true);
            EntityPlayer player = state.getPlayer();
            if (collar != null && player != null) {
               player.func_71019_a(collar, true);
            }

            Utils.sendValidMessageToEntity(sender, "Collar of " + player.func_70005_c_() + " has been removed.");
            Utils.sendValidMessageToEntity(player, "Your collar has been removed!");
         }
      }

   }

   public String func_71517_b() {
      return "takecollar";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.takecollar";
   }
}
