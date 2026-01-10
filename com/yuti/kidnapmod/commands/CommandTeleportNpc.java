package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.data.KidnappingSettingsData;
import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTeleportNpc extends CommandKidnapMod {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "teleportnpc";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.teleportnpc";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 1 || !options.contains(params[0])) {
            Utils.sendErrorMessageToEntity(player, "Use : /telportnpc [home / prison / kidnapwarp]");
            return;
         }

         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null) {
            boolean valid = false;
            if (params[0].equals("home")) {
               state.setLoadTeleportPointNpc(EntityDamsel.DamselTelportPoints.HOME);
               valid = true;
            } else if (params[0].equals("prison")) {
               state.setLoadTeleportPointNpc(EntityDamsel.DamselTelportPoints.PRISON);
               valid = true;
            } else if (params[0].equals("kidnapwarp")) {
               if (params.length < 2) {
                  Utils.sendErrorMessageToEntity(player, "You must put a kidnapwarp name.");
                  return;
               }

               KidnappingSettingsData settings = KidnappingSettingsData.get(player.field_70170_p);
               if (settings != null) {
                  String warpName = params[1];
                  Position pos = settings.getLocation(player.func_110124_au(), warpName);
                  if (pos == null) {
                     Utils.sendErrorMessageToEntity(player, "This warp doesn't exist.");
                     return;
                  }

                  valid = true;
                  state.setLoadTeleportPointNpc(EntityDamsel.DamselTelportPoints.WARP);
                  state.setTeleportNpcPos(pos);
               }
            }

            if (valid) {
               state.setTimerTeleportNpc(new Timer(15));
               Utils.sendValidMessageToEntity(player, "Teleportation loaded! Right click (with nothing in your hand) on the npc you want to teleport. (expires in 15 seconds)");
            } else {
               Utils.sendErrorMessageToEntity(player, "Use : /telportnpc [home / prison / kidnapwarp]");
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
      if (args.length == 1) {
         return func_175762_a(args, options);
      } else {
         if (args.length == 2 && args[0].equals("kidnapwarp") && sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)sender;
            KidnappingSettingsData settings = KidnappingSettingsData.get(player.field_70170_p);
            if (settings != null) {
               Set<String> warps = settings.getKidnappingLocationsNames(player);
               if (warps != null) {
                  return func_175762_a(args, warps);
               }
            }
         }

         return Collections.emptyList();
      }
   }

   static {
      options.add("home");
      options.add("prison");
      options.add("kidnapwarp");
   }
}
