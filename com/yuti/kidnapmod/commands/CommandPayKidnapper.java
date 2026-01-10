package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.items.ItemShockCollarAuto;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class CommandPayKidnapper extends CommandKidnapMod {
   public String func_71517_b() {
      return "paykidnapper";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.paykidnapper";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               ItemStack collar = state.getCurrentCollar();
               if (collar != null && !collar.func_190926_b() && collar.func_77973_b() instanceof ItemShockCollarAuto) {
                  ItemStack stack = player.func_184614_ca();
                  if (stack != null && !stack.func_190926_b()) {
                     ItemShockCollarAuto shockCollar = (ItemShockCollarAuto)collar.func_77973_b();
                     if (shockCollar != null && shockCollar.doesStackCompleteJob(collar, stack)) {
                        ItemStack jobStack = shockCollar.getJobItemStack(collar);
                        stack.func_190918_g(jobStack.func_190916_E());
                        Utils.sendValidMessageToEntity(player, "Well done slave, you've earned your freedom!");
                        state.clearSlot(ExtraBondageItemType.COLLAR);
                     } else {
                        state.shockKidnapped();
                        Utils.sendErrorMessageToEntity(player, "This stack is not what's the kidnapper requested");
                     }
                  } else {
                     Utils.sendErrorMessageToEntity(player, "You must held a stack in your main hand to pay the kidnapper");
                  }
               } else {
                  Utils.sendErrorMessageToEntity(player, "You're not wearing an automatic shock collar");
               }
            }
         }
      }

   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }
}
