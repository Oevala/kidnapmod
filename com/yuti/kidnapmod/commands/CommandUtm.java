package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandUtm extends CommandCheat {
   public String func_71517_b() {
      return "utm";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.utm";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null && state.isTiedUp()) {
               state.dropBondageItems();
               if (state.isSlave()) {
                  state.free();
               }

               state.untie();
               Utils.sendValidMessageToEntity(player, "You untied yourself!");
            }
         }
      }

   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }
}
