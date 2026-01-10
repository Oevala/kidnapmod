package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.loaders.common.GagTalkLoader;
import com.yuti.kidnapmod.loaders.jobs.JobLoader;
import com.yuti.kidnapmod.loaders.sales.SaleLoader;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandKidnapReload extends CommandCheat {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "kidnapreload";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.kidnapreload";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (args.length >= 1) {
         String toReload = args[0];
         if (toReload.equals("jobs")) {
            if (ModConfig.kidnappersJob) {
               JobLoader jobLoader = JobLoader.getInstance();
               if (jobLoader != null) {
                  jobLoader.reload();
                  Utils.sendValidMessageToEntity(sender, "Kidnappers jobs reloaded");
               }
            } else {
               Utils.sendErrorMessageToEntity(sender, "Kidnappers jobs are disabled");
            }
         } else if (toReload.equals("sales")) {
            if (ModConfig.kidnappersSell) {
               SaleLoader salesLoader = SaleLoader.getInstance();
               if (salesLoader != null) {
                  salesLoader.reload();
                  Utils.sendValidMessageToEntity(sender, "Kidnappers sales reloaded");
               }
            } else {
               Utils.sendErrorMessageToEntity(sender, "Kidnappers sales are disabled");
            }
         } else if (toReload.equals("gagtalk")) {
            GagTalkLoader gagTalkLoader = GagTalkLoader.getInstance();
            if (gagTalkLoader != null) {
               gagTalkLoader.reload();
               Utils.sendValidMessageToEntity(sender, "Gagtalk reloaded");
            }
         } else {
            Utils.sendErrorMessageToEntity(sender, "Option not recognized (use jobs, sales or gagtalk)");
         }
      }

   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      return args.length == 1 ? func_175762_a(args, options) : Collections.emptyList();
   }

   static {
      options.add("jobs");
      options.add("sales");
      options.add("gagtalk");
   }
}
