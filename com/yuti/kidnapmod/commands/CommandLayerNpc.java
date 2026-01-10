package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandLayerNpc extends CommandKidnapMod {
   private static List<String> options = new ArrayList();
   private static List<String> subOptions = new ArrayList();

   public String func_71517_b() {
      return "layernpc";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.layernpc";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 2 || !options.contains(params[0])) {
            Utils.sendErrorMessageToEntity(player, "Use : /layernpc [head / body / leftarm / rightarm / leftleg / rightleg] [enable / disable]");
            return;
         }

         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null) {
            boolean valid = true;
            boolean enabled = false;
            if (params[1].equals("enable")) {
               enabled = true;
            } else if (params[1].equals("disable")) {
               enabled = false;
            } else {
               valid = false;
            }

            if (valid) {
               if (params[0].equals("head")) {
                  state.setLoadedDamselLayer(EntityDamsel.LayersEnum.HEAD);
               } else if (params[0].equals("body")) {
                  state.setLoadedDamselLayer(EntityDamsel.LayersEnum.BODY);
               } else if (params[0].equals("leftarm")) {
                  state.setLoadedDamselLayer(EntityDamsel.LayersEnum.LEFTARM);
               } else if (params[0].equals("rightarm")) {
                  state.setLoadedDamselLayer(EntityDamsel.LayersEnum.RIGHTARM);
               } else if (params[0].equals("leftleg")) {
                  state.setLoadedDamselLayer(EntityDamsel.LayersEnum.LEFTLEG);
               } else if (params[0].equals("rightleg")) {
                  state.setLoadedDamselLayer(EntityDamsel.LayersEnum.RIGHTLEG);
               } else {
                  valid = false;
               }
            }

            if (valid) {
               state.setLoadedDamselLayerState(enabled);
               state.setTimerLayerNpc(new Timer(15));
               Utils.sendValidMessageToEntity(player, "Layer state loaded! Right click (with nothing in your hand) on the npc you want to update. (expires in 15 seconds)");
            } else {
               Utils.sendErrorMessageToEntity(player, "Use : /layernpc [head / body / leftarm / rightarm / leftleg / rightleg] [enable / disable]");
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
         return args.length == 2 ? func_175762_a(args, subOptions) : Collections.emptyList();
      }
   }

   static {
      options.add("head");
      options.add("body");
      options.add("leftarm");
      options.add("rightarm");
      options.add("leftleg");
      options.add("rightleg");
      subOptions.add("enable");
      subOptions.add("disable");
   }
}
