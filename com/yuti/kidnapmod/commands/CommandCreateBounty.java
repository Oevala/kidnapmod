package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.BountiesData;
import com.yuti.kidnapmod.data.Bounty;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandCreateBounty extends CommandTargetPlayer {
   public String func_71517_b() {
      return "bounty";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.bounty";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (!sender.func_130014_f_().field_72995_K) {
         if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            if (player != null) {
               EntityPlayer targetPlayer = this.getTarget(server, sender, params);
               if (targetPlayer != null) {
                  if (targetPlayer.func_110124_au().equals(player.func_110124_au())) {
                     Utils.sendErrorMessageToEntity(sender, "You can't put a bounty on yourself");
                     return;
                  }

                  BountiesData data = BountiesData.get(player.field_70170_p);
                  if (!data.isAllowedToCreateBounty(player)) {
                     int maxBounties = data.getMaxBountiesPerPlayer(player.field_70170_p);
                     Utils.sendErrorMessageToEntity(sender, "Maximum number (" + maxBounties + ") of active bounties reached");
                     return;
                  }

                  ItemStack stack = player.func_184614_ca();
                  if (stack == null || stack.func_77973_b().equals(Items.field_190931_a)) {
                     Utils.sendErrorMessageToEntity(sender, "You have to hold an item stack as a reward");
                     return;
                  }

                  Bounty bounty = new Bounty(player, targetPlayer, stack, data.getTimeForBounties(server.func_130014_f_()));
                  stack.func_190918_g(stack.func_190916_E());
                  data.addBounty(bounty);
                  Utils.sendMessageToEntity(player, "Bounty successfully created.");
                  Utils.broadcastMessage(player.func_70005_c_() + " has put a bounty on " + targetPlayer.func_70005_c_());
               }
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

   public boolean isBypassingBlock() {
      return true;
   }
}
