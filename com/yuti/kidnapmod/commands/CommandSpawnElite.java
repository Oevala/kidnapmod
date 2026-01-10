package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.entities.elite.EliteKidnappers;
import com.yuti.kidnapmod.entities.elite.EntityKidnapperElite;
import com.yuti.kidnapmod.util.Utils;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSpawnElite extends CommandTargetPlayer {
   public CommandSpawnElite() {
      this.indexPlayerParam = 1;
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      EntityPlayer player = this.getTarget(server, sender, params);
      if (player != null) {
         if (params.length < 1) {
            Utils.sendErrorMessageToEntity(player, "Use : /spawnelite [name]");
            return;
         }

         String name = params[0];
         if (name == null || name.equals("")) {
            Utils.sendErrorMessageToEntity(player, "Use : /spawnelite [name]");
            return;
         }

         EliteKidnappers elite = EliteKidnappers.getByName(name);
         if (elite == null) {
            Utils.sendErrorMessageToEntity(player, "Can't find elite");
            return;
         }

         EntityKidnapperElite toSpawn = new EntityKidnapperElite(player.field_70170_p);
         toSpawn.setDamselName(elite.getName());
         toSpawn.setSmallArms(elite.hasSmallArms());
         BlockPos pos = player.func_180425_c();
         if (pos != null && player.func_130014_f_() != null) {
            toSpawn.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
            player.func_130014_f_().func_72838_d(toSpawn);
         }
      }

   }

   public String func_71517_b() {
      return "spawnelite";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.spawnelite";
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      return args.length == 1 ? func_175762_a(args, EliteKidnappers.getEliteNames()) : super.func_184883_a(server, sender, args, targetPos);
   }
}
