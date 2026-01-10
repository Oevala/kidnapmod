package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandDynamicClothes extends CommandKidnapMod {
   private static List<String> options = new ArrayList();
   private static List<String> subOptionsLayers = new ArrayList();

   public String func_71517_b() {
      return "dynamicclothes";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.dynamicclothes";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (player != null) {
            if (params.length < 1) {
               Utils.sendErrorMessageToEntity(player, "Use : /dynamicclothes [set / reset]");
               return;
            }

            ItemStack stack = player.func_184614_ca();
            if (stack == null || !(stack.func_77973_b() instanceof ItemClothes)) {
               Utils.sendErrorMessageToEntity(player, "You must held clothes!");
               return;
            }

            ItemClothes clothes = (ItemClothes)stack.func_77973_b();
            if (params[0].equals("reset")) {
               clothes.removeDynamicTextureUrl(stack);
               Utils.sendValidMessageToEntity(player, "Dynamic clothes reset!");
               return;
            }

            if (params[0].equals("set")) {
               if (params.length < 2) {
                  Utils.sendErrorMessageToEntity(player, "You must put an URL!");
                  return;
               }

               String url = params[1];
               clothes.setDynamicTextureUrl(stack, url);
               Utils.sendValidMessageToEntity(player, "Dynamic clothes set!");
               return;
            }

            if (params[0].equals("fullskin")) {
               if (clothes.isFullSkinEnabled(stack)) {
                  clothes.disableFullSkin(stack);
                  Utils.sendValidMessageToEntity(player, "Full skin disabled");
               } else {
                  clothes.enableFullSkin(stack);
                  Utils.sendValidMessageToEntity(player, "Full skin enabled");
               }
            } else if (params[0].equals("smallarms")) {
               if (clothes.shouldForceSmallArms(stack)) {
                  clothes.disableForceSmallArms(stack);
                  Utils.sendValidMessageToEntity(player, "Small arms disabled");
               } else {
                  clothes.enableForceSmallArms(stack);
                  Utils.sendValidMessageToEntity(player, "Small arms enabled");
               }
            } else {
               if (!params[0].equals("setlayer")) {
                  Utils.sendErrorMessageToEntity(player, "Use : /dynamicclothes [set / reset]");
                  return;
               }

               if (params.length < 2 || !subOptionsLayers.contains(params[1])) {
                  Utils.sendErrorMessageToEntity(player, "You must put a layer! (head / body / leftarm / rightarm / leftleg / rightleg)");
                  return;
               }

               if (params[1].equals("head")) {
                  if (clothes.isWearerHeadLayerEnabled(stack)) {
                     clothes.setWearerHeadLayer(stack, false);
                     Utils.sendValidMessageToEntity(player, "Wearer's head layer disabled");
                  } else {
                     clothes.setWearerHeadLayer(stack, true);
                     Utils.sendValidMessageToEntity(player, "Wearer's head layer enabled");
                  }
               } else if (params[1].equals("body")) {
                  if (clothes.isWearerBodyLayerEnabled(stack)) {
                     clothes.setWearerBodyLayer(stack, false);
                     Utils.sendValidMessageToEntity(player, "Wearer's body layer disabled");
                  } else {
                     clothes.setWearerBodyLayer(stack, true);
                     Utils.sendValidMessageToEntity(player, "Wearer's body layer enabled");
                  }
               } else if (params[1].equals("leftarm")) {
                  if (clothes.isWearerLeftArmLayerEnabled(stack)) {
                     clothes.setWearerLeftArmLayer(stack, false);
                     Utils.sendValidMessageToEntity(player, "Wearer's left arm layer disabled");
                  } else {
                     clothes.setWearerLeftArmLayer(stack, true);
                     Utils.sendValidMessageToEntity(player, "Wearer's left arm layer enabled");
                  }
               } else if (params[1].equals("rightarm")) {
                  if (clothes.isWearerRightArmLayerEnabled(stack)) {
                     clothes.setWearerRightArmLayer(stack, false);
                     Utils.sendValidMessageToEntity(player, "Wearer's right arm layer disabled");
                  } else {
                     clothes.setWearerRightArmLayer(stack, true);
                     Utils.sendValidMessageToEntity(player, "Wearer's right arm layer enabled");
                  }
               } else if (params[1].equals("leftleg")) {
                  if (clothes.isWearerLeftLegLayerEnabled(stack)) {
                     clothes.setWearerLeftLegLayer(stack, false);
                     Utils.sendValidMessageToEntity(player, "Wearer's left leg layer disabled");
                  } else {
                     clothes.setWearerLeftLegLayer(stack, true);
                     Utils.sendValidMessageToEntity(player, "Wearer's left leg layer enabled");
                  }
               } else if (params[1].equals("rightleg")) {
                  if (clothes.isWearerRightLegLayerEnabled(stack)) {
                     clothes.setWearerRightLegLayer(stack, false);
                     Utils.sendValidMessageToEntity(player, "Wearer's right leg layer disabled");
                  } else {
                     clothes.setWearerRightLegLayer(stack, true);
                     Utils.sendValidMessageToEntity(player, "Wearer's right leg layer enabled");
                  }
               }
            }
         }
      }

   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 1) {
         return func_175762_a(args, options);
      } else {
         return args.length == 2 && args[0].equals("setlayer") ? func_175762_a(args, subOptionsLayers) : super.func_184883_a(server, sender, args, targetPos);
      }
   }

   static {
      options.add("set");
      options.add("reset");
      options.add("fullskin");
      options.add("smallarms");
      options.add("setlayer");
      subOptionsLayers.add("head");
      subOptionsLayers.add("body");
      subOptionsLayers.add("leftarm");
      subOptionsLayers.add("rightarm");
      subOptionsLayers.add("leftleg");
      subOptionsLayers.add("rightleg");
   }
}
