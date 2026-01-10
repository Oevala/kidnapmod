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

public class CommandTakeEarplugs extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      List<PlayerBindState> states = this.getTargetsState(server, sender, params);
      Iterator var5 = states.iterator();

      while(var5.hasNext()) {
         PlayerBindState state = (PlayerBindState)var5.next();
         if (state != null && state.hasEarplugs()) {
            ItemStack earplugs = state.takesEarplugsOff();
            EntityPlayer player = state.getPlayer();
            if (earplugs != null && player != null) {
               player.func_71019_a(earplugs, true);
            }

            Utils.sendValidMessageToEntity(sender, "Earplugs of " + player.func_70005_c_() + " have been removed.");
            Utils.sendValidMessageToEntity(player, "Your earplugs have been removed!");
         }
      }

   }

   public String func_71517_b() {
      return "takeearplugs";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.takeearplugs";
   }
}
