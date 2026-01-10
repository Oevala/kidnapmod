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

public class CommandUnblind extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      List<PlayerBindState> states = this.getTargetsState(server, sender, params);
      Iterator var5 = states.iterator();

      while(var5.hasNext()) {
         PlayerBindState state = (PlayerBindState)var5.next();
         if (state != null && state.isBlindfolded()) {
            ItemStack blindfold = state.takesBlindfoldOff();
            EntityPlayer player = state.getPlayer();
            if (blindfold != null && player != null) {
               player.func_71019_a(blindfold, true);
            }

            Utils.sendValidMessageToEntity(sender, player.func_70005_c_() + " is now unblinded.");
            Utils.sendValidMessageToEntity(player, "You have been unblinded!");
         }
      }

   }

   public String func_71517_b() {
      return "unblind";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.unblind";
   }
}
