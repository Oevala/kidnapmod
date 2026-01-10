package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.entities.elite.EliteKidnappers;
import com.yuti.kidnapmod.entities.elite.EntityKidnapperElite;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandDidnapper extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      EntityPlayer player = this.getTarget(server, sender, params);
      if (player != null) {
         BlockPos pos = player.func_180425_c();
         if (pos != null && player.func_130014_f_() != null) {
            EntityKidnapper suki = new EntityKidnapperElite(player.func_130014_f_());
            EntityKidnapper carol = new EntityKidnapperElite(player.func_130014_f_());
            EntityKidnapper athena = new EntityKidnapperElite(player.func_130014_f_());
            EntityKidnapper evelyn = new EntityKidnapperElite(player.func_130014_f_());
            suki.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
            suki.setDamselName(EliteKidnappers.Suki.getName());
            suki.setSmallArms(EliteKidnappers.Suki.hasSmallArms());
            carol.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
            carol.setDamselName(EliteKidnappers.Carol.getName());
            carol.setSmallArms(EliteKidnappers.Carol.hasSmallArms());
            athena.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
            athena.setDamselName(EliteKidnappers.Athena.getName());
            athena.setSmallArms(EliteKidnappers.Athena.hasSmallArms());
            evelyn.func_70012_b(player.field_70165_t, player.field_70163_u, player.field_70161_v, 0.0F, 0.0F);
            evelyn.setDamselName(EliteKidnappers.Evelyn.getName());
            evelyn.setSmallArms(EliteKidnappers.Evelyn.hasSmallArms());
            player.func_130014_f_().func_72838_d(suki);
            player.func_130014_f_().func_72838_d(carol);
            player.func_130014_f_().func_72838_d(athena);
            player.func_130014_f_().func_72838_d(evelyn);
         }
      }

   }

   public String func_71517_b() {
      return "didnapper";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.didnapper";
   }
}
