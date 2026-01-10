package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CommandClothesChanging extends CommandKidnapMod {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "clotheschanging";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.clotheschanging";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            if (params.length >= 1 && options.contains(params[0])) {
               KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)player.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
               if (cap != null) {
                  if (params[0].equals("allow")) {
                     cap.setAllowChangingClothes(true);
                     Utils.sendValidMessageToEntity(player, "Players can now change or take off your clothes");
                  } else if (params[0].equals("deny")) {
                     cap.setAllowChangingClothes(false);
                     Utils.sendValidMessageToEntity(player, "Players can't change or take off your clothes anymore");
                  }

                  cap.sync();
               }
            } else {
               Utils.sendErrorMessageToEntity(player, "Use : /clotheschanging [allow / deny]");
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

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      return args.length == 1 ? func_175762_a(args, options) : Collections.emptyList();
   }

   static {
      options.add("allow");
      options.add("deny");
   }
}
