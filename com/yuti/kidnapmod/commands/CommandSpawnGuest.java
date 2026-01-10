package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.entities.guests.EntityGuestDamsel;
import com.yuti.kidnapmod.entities.guests.EntityGuestKidnapper;
import com.yuti.kidnapmod.entities.guests.GuestsDamsels;
import com.yuti.kidnapmod.entities.guests.GuestsKidnappers;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSpawnGuest extends CommandTargetPlayer {
   private static List<String> options = new ArrayList();

   public CommandSpawnGuest() {
      this.indexPlayerParam = 2;
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      EntityPlayer player = this.getTarget(server, sender, params);
      if (player != null) {
         if (params.length < 2) {
            Utils.sendErrorMessageToEntity(player, "Use : /spawnguest [damsel / kidnapper] [name]");
            return;
         }

         if (!params[0].equals("damsel") && !params[0].equals("kidnapper")) {
            Utils.sendErrorMessageToEntity(player, "Use : /spawnguest [damsel / kidnapper] [name]");
            return;
         }

         String name = params[1];
         if (name != null && !name.equals("")) {
            EntityDamsel toSpawn = null;
            if (params[0].equals("damsel")) {
               GuestsDamsels damsel = GuestsDamsels.getByName(name);
               if (damsel != null) {
                  toSpawn = new EntityGuestDamsel(player.field_70170_p);
                  ((EntityDamsel)toSpawn).setDamselName(damsel.getName());
                  ((EntityDamsel)toSpawn).setSmallArms(damsel.hasSmallArms());
               }
            } else if (params[0].equals("kidnapper")) {
               GuestsKidnappers kidnapper = GuestsKidnappers.getByName(name);
               if (kidnapper != null) {
                  toSpawn = new EntityGuestKidnapper(player.field_70170_p);
                  ((EntityDamsel)toSpawn).setDamselName(kidnapper.getName());
                  ((EntityDamsel)toSpawn).setSmallArms(kidnapper.hasSmallArms());
               }
            }

            if (toSpawn == null) {
               Utils.sendErrorMessageToEntity(player, "Can't find guest");
               return;
            }

            BlockPos pos = player.func_180425_c();
            if (pos != null && player.func_130014_f_() != null) {
               ((EntityDamsel)toSpawn).func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
               player.func_130014_f_().func_72838_d((Entity)toSpawn);
            }
         }
      }

   }

   public String func_71517_b() {
      return "spawnguest";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.spawnguest";
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 1) {
         return func_175762_a(args, options);
      } else {
         if (args.length == 2) {
            if (args[0].equals("damsel")) {
               return func_175762_a(args, GuestsDamsels.getGuestsNames());
            }

            if (args[0].equals("kidnapper")) {
               return func_175762_a(args, GuestsKidnappers.getGuestsNames());
            }
         }

         return super.func_184883_a(server, sender, args, targetPos);
      }
   }

   static {
      options.add("damsel");
      options.add("kidnapper");
   }
}
