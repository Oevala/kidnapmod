package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.IAdjustable;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class CommandAdjust extends CommandKidnapMod {
   private static List<String> options = new ArrayList();

   public String func_71517_b() {
      return "adjust";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.adjust";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length >= 1) {
            float adjustValue = 0.0F;
            if (params.length >= 2) {
               try {
                  adjustValue = Float.parseFloat(params[1]);
                  if ((double)adjustValue < -4.0D || (double)adjustValue > 4.0D) {
                     Utils.sendErrorMessageToEntity(player, "The value must me between -4.0 and 4.0");
                     return;
                  }
               } catch (NumberFormatException var13) {
                  Utils.sendErrorMessageToEntity(player, "Wrong number!");
                  return;
               }
            }

            KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)player.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
            if (cap != null) {
               if (params[0].equals("allgags")) {
                  if (params.length >= 2) {
                     cap.setAllGags(adjustValue);
                     cap.sync();
                     Utils.sendValidMessageToEntity(player, "All gags adjusted!");
                  } else {
                     Utils.sendErrorMessageToEntity(player, "You must set a number");
                  }

                  return;
               }

               if (params[0].equals("allblindfolds")) {
                  if (params.length >= 2) {
                     cap.setAllBlindfolds(adjustValue);
                     cap.sync();
                     Utils.sendValidMessageToEntity(player, "All blindfolds adjusted!");
                  } else {
                     Utils.sendErrorMessageToEntity(player, "You must set a number");
                  }

                  return;
               }

               if (params[0].equals("all")) {
                  if (params.length >= 2) {
                     cap.setAll(adjustValue);
                     cap.sync();
                     Utils.sendValidMessageToEntity(player, "All gags and blindfolds adjusted!");
                  } else {
                     Utils.sendErrorMessageToEntity(player, "You must set a number");
                  }

                  return;
               }

               String itemId;
               float current;
               PlayerBindState state;
               ItemStack currentBlindfold;
               Item gag;
               IAdjustable blindAdjust;
               if (params[0].equals("currentgag")) {
                  state = PlayerBindState.getInstance(player);
                  if (state != null) {
                     currentBlindfold = state.getCurrentGag();
                     if (currentBlindfold != null && !currentBlindfold.func_190926_b() && currentBlindfold.func_77973_b() instanceof IAdjustable) {
                        gag = currentBlindfold.func_77973_b();
                        blindAdjust = (IAdjustable)gag;
                        if (blindAdjust.canBeAdjusted()) {
                           itemId = gag.getRegistryName().toString();
                           if (params.length >= 2) {
                              cap.setAdjustement(itemId, adjustValue);
                              cap.sync();
                              Utils.sendValidMessageToEntity(player, "Gag adjusted!");
                           } else {
                              current = cap.getAdjustement(itemId);
                              Utils.sendValidMessageToEntity(player, "Current : " + current);
                           }

                           return;
                        }
                     }

                     Utils.sendErrorMessageToEntity(player, "This item can't be adjusted");
                     return;
                  }

                  return;
               }

               if (params[0].equals("currentblindfold")) {
                  state = PlayerBindState.getInstance(player);
                  if (state != null) {
                     currentBlindfold = state.getCurrentBlindfold();
                     if (currentBlindfold != null && !currentBlindfold.func_190926_b() && currentBlindfold.func_77973_b() instanceof IAdjustable) {
                        gag = currentBlindfold.func_77973_b();
                        blindAdjust = (IAdjustable)gag;
                        if (blindAdjust.canBeAdjusted()) {
                           itemId = gag.getRegistryName().toString();
                           if (params.length >= 2) {
                              cap.setAdjustement(itemId, adjustValue);
                              cap.sync();
                              Utils.sendValidMessageToEntity(player, "Blindfold adjusted!");
                           } else {
                              current = cap.getAdjustement(itemId);
                              Utils.sendValidMessageToEntity(player, "Current : " + current);
                           }

                           return;
                        }
                     }

                     Utils.sendErrorMessageToEntity(player, "This item can't be adjusted");
                     return;
                  }

                  return;
               }

               String itemId = params[0];
               Item item = Item.func_111206_d(itemId);
               if (item != null && item instanceof IAdjustable) {
                  IAdjustable itemAdjust = (IAdjustable)item;
                  if (itemAdjust.canBeAdjusted()) {
                     if (params.length >= 2) {
                        cap.setAdjustement(itemId, adjustValue);
                        cap.sync();
                        Utils.sendValidMessageToEntity(player, "Model adjusted!");
                     } else {
                        float current = cap.getAdjustement(itemId);
                        Utils.sendValidMessageToEntity(player, "Current : " + current);
                     }

                     return;
                  }
               }

               Utils.sendErrorMessageToEntity(player, "This item can't be adjusted");
               return;
            }
         }

         Utils.sendInfoMessageToEntity(player, "Use /adjust [all / allgags / allblindfolds  / currentgag / currentblindfold] value");
      }

   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 1) {
         List<String> optionsList = new ArrayList();
         optionsList.addAll(func_175762_a(args, options));
         List<Item> items = new ArrayList();
         items.addAll(ModItems.GAG_LIST);
         items.addAll(ModItems.BLINDFOLD_LIST);
         Iterator var7 = items.iterator();

         while(var7.hasNext()) {
            Item item = (Item)var7.next();
            if (item != null && item instanceof IAdjustable) {
               IAdjustable itemAdjustable = (IAdjustable)item;
               if (itemAdjustable.canBeAdjusted()) {
                  String name = item.getRegistryName().toString();
                  if (name.contains(args[0])) {
                     optionsList.add(name);
                  }
               }
            }
         }

         return optionsList;
      } else {
         return super.func_184883_a(server, sender, args, targetPos);
      }
   }

   public boolean isCallableWhileTiedUp() {
      return true;
   }

   public boolean func_184882_a(MinecraftServer server, ICommandSender sender) {
      return true;
   }

   static {
      options.add("all");
      options.add("allblindfolds");
      options.add("allgags");
      options.add("currentgag");
      options.add("currentblindfold");
   }
}
