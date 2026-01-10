package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.entities.classic.EntityClassicKidnapper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandSpawnKidnapper extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      EntityPlayer player = this.getTarget(server, sender, params);
      if (player != null) {
         BlockPos pos = player.func_180425_c();
         if (pos != null && player.func_130014_f_() != null) {
            EntityKidnapper kidnapper = new EntityClassicKidnapper(player.func_130014_f_());
            kidnapper.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
            player.func_130014_f_().func_72838_d(kidnapper);
         }
      }

   }

   public String func_71517_b() {
      return "spawnkidnapper";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.spawnkidnapper";
   }
}
