package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandPutClothes extends CommandTargetPlayer {
   private static List<String> options = new ArrayList();

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      List<PlayerBindState> states = this.getTargetsState(server, sender, params);
      if (params.length < 2) {
         Utils.sendErrorMessageToEntity(sender, "Use : /putclothes [player] [url] [fullskin]");
      } else {
         String url = params[1];
         Iterator var6 = states.iterator();

         while(var6.hasNext()) {
            PlayerBindState state = (PlayerBindState)var6.next();
            if (state != null && !state.hasCollar()) {
               ItemStack clothes = new ItemStack(ModItems.CLOTHES);
               clothes = ModItems.CLOTHES.setDynamicTextureUrl(clothes, url);
               if (params.length >= 3 && params[2].equals("fullskin")) {
                  clothes = ModItems.CLOTHES.enableFullSkin(clothes);
               }

               state.putClothesOn(clothes);
               Utils.sendValidMessageToEntity(sender, state.getPlayer().func_70005_c_() + " is now wearing clothes.");
               Utils.sendInfoMessageToEntity(state.getPlayer(), "Someone put clothes on you!");
            }
         }

      }
   }

   public String func_71517_b() {
      return "putclothes";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.putclothes";
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      return args.length == 3 ? func_175762_a(args, options) : super.func_184883_a(server, sender, args, targetPos);
   }

   static {
      options.add("fullskin");
   }
}
