package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;

public class CommandDisplayClothes extends CommandKidnapMod {
   public String func_71517_b() {
      return "displayclothes";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.displayclothes";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)player.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
            if (cap != null) {
               if (cap.allowClothes) {
                  cap.setAllowClothes(false);
                  Utils.sendValidMessageToEntity(player, "Dynamic clothes disabled");
               } else {
                  cap.setAllowClothes(true);
                  Utils.sendValidMessageToEntity(player, "Dynamic clothes enabled");
               }

               cap.sync();
            }
         }
      }

   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }
}
