package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public abstract class CommandTargetPlayer extends CommandCheat {
   protected int indexPlayerParam = 0;

   protected EntityPlayer getTarget(MinecraftServer server, ICommandSender sender, String[] params) {
      EntityPlayerMP entityplayer = null;

      try {
         entityplayer = params.length > this.indexPlayerParam ? func_184888_a(server, sender, params[this.indexPlayerParam]) : func_71521_c(sender);
      } catch (CommandException var6) {
         Utils.sendErrorMessageToEntity(sender, "Player not found");
      }

      return entityplayer;
   }

   protected PlayerBindState getTargetState(MinecraftServer server, ICommandSender sender, String[] params) {
      EntityPlayer entityplayer = this.getTarget(server, sender, params);
      if (entityplayer != null) {
         PlayerBindState state = PlayerBindState.getInstance(entityplayer);
         return state;
      } else {
         return null;
      }
   }

   protected List<EntityPlayerMP> getTargets(MinecraftServer server, ICommandSender sender, String[] params) {
      Object players = new ArrayList();

      try {
         if (params.length > this.indexPlayerParam) {
            players = func_193513_a(server, sender, params[this.indexPlayerParam]);
         } else {
            EntityPlayerMP senderPlayer = func_71521_c(sender);
            if (senderPlayer != null) {
               ((List)players).add(senderPlayer);
            }
         }
      } catch (CommandException var6) {
         Utils.sendErrorMessageToEntity(sender, "Target not found");
      }

      return (List)players;
   }

   protected List<PlayerBindState> getTargetsState(MinecraftServer server, ICommandSender sender, String[] params) {
      List<EntityPlayerMP> players = this.getTargets(server, sender, params);
      List<PlayerBindState> states = new ArrayList();
      if (players != null) {
         Iterator var6 = players.iterator();

         while(var6.hasNext()) {
            EntityPlayer player = (EntityPlayer)var6.next();
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               states.add(state);
            }
         }
      }

      return states;
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      return args.length == this.indexPlayerParam + 1 ? func_71530_a(args, server.func_71213_z()) : super.func_184883_a(server, sender, args, targetPos);
   }
}
