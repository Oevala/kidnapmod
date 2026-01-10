package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandRestraintTexture extends CommandKidnapMod {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "restrainttexture";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.restrainttexture";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            if (params.length < 1) {
               Utils.sendErrorMessageToEntity(player, "Use : /restrainttexture [reset / url]");
               return;
            }

            ItemStack stack = player.func_184614_ca();
            if (stack != null && stack.func_77973_b() instanceof IExtraBondageItem) {
               IExtraBondageItem bondageItem = (IExtraBondageItem)stack.func_77973_b();
               if (params[0].equals("reset")) {
                  bondageItem.removeDynamicTextureUrl(stack);
                  Utils.sendValidMessageToEntity(player, "Dynamic texture reset!");
                  return;
               }

               String url = params[0];
               bondageItem.setDynamicTextureUrl(stack, url);
               Utils.sendValidMessageToEntity(player, "Dynamic texture set!");
               return;
            }

            Utils.sendErrorMessageToEntity(player, "You must held a restraint (binds, gag, blindfold, earplugs, collar...)");
            return;
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
      return args.length == 1 ? func_175762_a(args, options) : super.func_184883_a(server, sender, args, targetPos);
   }

   static {
      options.add("reset");
   }
}
