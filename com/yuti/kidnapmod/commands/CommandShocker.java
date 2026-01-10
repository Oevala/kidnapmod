package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.items.ItemShockerController;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandShocker extends CommandTargetPlayer {
   private static List<String> options = new ArrayList();

   public CommandShocker() {
      this.indexPlayerParam = 1;
   }

   public String func_71517_b() {
      return "shocker";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.shocker";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer && params != null) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 1) {
            Utils.sendErrorMessageToEntity(player, "Use : /shocker [claim / unclaim / connect / broadcast]");
            return;
         }

         if (player != null) {
            ItemStack stack = player.func_184614_ca();
            if (stack != null && stack.func_77973_b() instanceof ItemShockerController) {
               ItemShockerController shocker = (ItemShockerController)stack.func_77973_b();
               if (params[0].equals("claim")) {
                  if (!shocker.hasOwner(stack)) {
                     shocker.setOwner(stack, player);
                     Utils.sendValidMessageToEntity(player, "Shocker controller successfully claimed!");
                  } else {
                     Utils.sendErrorMessageToEntity(player, "This shocker is already claimed");
                  }
               } else if (params[0].equals("unclaim")) {
                  if (!shocker.isOwner(stack, player)) {
                     Utils.sendErrorMessageToEntity(player, "You're not the owner of this shocker");
                     return;
                  }

                  shocker.removeOwner(stack);
                  Utils.sendValidMessageToEntity(player, "Shocker successfully unclaimed!");
               } else if (params[0].equals("connect")) {
                  if (!shocker.hasOwner(stack)) {
                     Utils.sendErrorMessageToEntity(player, "You must claim the shocker first");
                     return;
                  }

                  EntityPlayer target = this.getTarget(server, sender, params);
                  if (target != null) {
                     shocker.setTarget(stack, target);
                     Utils.sendValidMessageToEntity(player, "Shocker successfully connected to " + target.func_70005_c_());
                  }
               } else if (params[0].equals("broadcast")) {
                  if (!shocker.hasOwner(stack)) {
                     Utils.sendErrorMessageToEntity(player, "You must claim the shocker first");
                     return;
                  }

                  if (!shocker.isBroadcastEnabled(stack)) {
                     shocker.enableBroadcast(stack);
                     Utils.sendValidMessageToEntity(player, "Broadcast mode enabled!");
                  } else {
                     shocker.disableBroadcast(stack);
                     Utils.sendValidMessageToEntity(player, "Broadcast mode disabled!");
                  }
               } else {
                  Utils.sendErrorMessageToEntity(player, "Use : /shocker [claim / connect / broadcast]");
               }
            } else {
               Utils.sendErrorMessageToEntity(player, "You must held a shocker.");
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
      return args.length == 1 ? func_175762_a(args, options) : super.func_184883_a(server, sender, args, targetPos);
   }

   static {
      options.add("claim");
      options.add("unclaim");
      options.add("connect");
      options.add("broadcast");
   }
}
