package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.items.ItemKeyCollar;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandCollarKey extends CommandTargetPlayer {
   private static List<String> options = new ArrayList();

   public CommandCollarKey() {
      this.indexPlayerParam = 1;
   }

   public String func_71517_b() {
      return "collarkey";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.collarkey";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer && params != null) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 1) {
            Utils.sendErrorMessageToEntity(player, "Use : /collarkey [claim / unclaim / assign]");
            return;
         }

         if (player != null) {
            ItemStack stack = player.func_184614_ca();
            if (stack != null && stack.func_77973_b() instanceof ItemKeyCollar) {
               ItemKeyCollar key = (ItemKeyCollar)stack.func_77973_b();
               if (params[0].equals("claim")) {
                  if (!key.hasOwner(stack)) {
                     key.setOwner(stack, player);
                     Utils.sendValidMessageToEntity(player, "Key successfully claimed!");
                  } else {
                     Utils.sendErrorMessageToEntity(player, "This key is already claimed");
                  }
               } else if (params[0].equals("unclaim")) {
                  if (!key.isOwner(stack, player)) {
                     Utils.sendErrorMessageToEntity(player, "You're not the owner of this key");
                     return;
                  }

                  key.removeOwner(stack);
                  Utils.sendValidMessageToEntity(player, "Key successfully unclaimed!");
               } else if (params[0].equals("assign")) {
                  if (!key.isOwner(stack, player)) {
                     Utils.sendErrorMessageToEntity(player, "You're not the owner of this key");
                     return;
                  }

                  EntityPlayer target = this.getTarget(server, sender, params);
                  if (target != null) {
                     if (!key.hasTarget(stack)) {
                        key.setTarget(stack, target);
                        Utils.sendValidMessageToEntity(player, "Key successfully assigned to " + target.func_70005_c_());
                     } else {
                        Utils.sendErrorMessageToEntity(player, "This key is already assigned");
                     }
                  }
               } else {
                  Utils.sendErrorMessageToEntity(player, "Use : /collarkey [claim / unclaim / assign]");
               }
            } else {
               Utils.sendErrorMessageToEntity(player, "You must held a collar key.");
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
      options.add("assign");
      options.add("unclaim");
   }
}
