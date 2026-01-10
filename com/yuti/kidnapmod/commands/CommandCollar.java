package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemShockCollar;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandCollar extends CommandTargetPlayer {
   private static List<String> options = new ArrayList();

   public CommandCollar() {
      this.indexPlayerParam = 1;
   }

   public String func_71517_b() {
      return "collar";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.collar";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer && params != null) {
         EntityPlayer player = (EntityPlayer)sender;
         if (params.length < 1) {
            Utils.sendErrorMessageToEntity(player, "Use : /collar [claim / unclaim / rename / addowner / public / whitelist / whitelistadd / whitelistremove / blacklist / blacklistadd / blacklistremove / sethome / setprison / prisonradius / prisonfence / backhome / kidnapping / warnmasters / bondageservice / servicemessage]");
            return;
         }

         if (player != null) {
            ItemStack stack = player.func_184614_ca();
            if (stack != null && stack.func_77973_b() instanceof ItemCollar) {
               ItemCollar collar = (ItemCollar)stack.func_77973_b();
               if (params[0].equals("claim")) {
                  if (!collar.hasOwner(stack)) {
                     collar.addOwner(stack, player);
                     Utils.sendValidMessageToEntity(player, "Collar successfully claimed!");
                  } else {
                     Utils.sendErrorMessageToEntity(player, "This collar is already claimed");
                  }
               } else {
                  String message;
                  if (params[0].equals("rename")) {
                     if (params.length < 2) {
                        Utils.sendErrorMessageToEntity(player, "You must specify a nickane.");
                        return;
                     }

                     if (!collar.isOwner(stack, player)) {
                        Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                        return;
                     }

                     message = Utils.getMessageFromArray(params, 1, params.length);
                     if (message.length() < 3 || message.length() > 20) {
                        Utils.sendErrorMessageToEntity(player, "The name must contains between 3 and 20 characters");
                        return;
                     }

                     collar.setNickname(stack, message);
                     Utils.sendValidMessageToEntity(player, "Nickname set on collar!");
                  } else {
                     Iterator var8;
                     EntityPlayerMP target;
                     List players;
                     if (params[0].equals("addowner")) {
                        if (!collar.isOwner(stack, player)) {
                           if (!collar.hasOwner(stack)) {
                              Utils.sendErrorMessageToEntity(player, "The collar is not claimed yet");
                           } else {
                              Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                           }

                           return;
                        }

                        players = this.getTargets(server, sender, params);
                        var8 = players.iterator();

                        while(var8.hasNext()) {
                           target = (EntityPlayerMP)var8.next();
                           if (target != null) {
                              if (!collar.isOwner(stack, (EntityPlayer)target)) {
                                 collar.addOwner(stack, target);
                                 Utils.sendValidMessageToEntity(player, target.func_70005_c_() + " added to owners");
                                 if (!player.equals(target)) {
                                    Utils.sendValidMessageToEntity(target, player.func_70005_c_() + " added you as owner of a collar'");
                                 }
                              } else {
                                 Utils.sendErrorMessageToEntity(player, target.func_70005_c_() + " is already owner of this collar");
                              }
                           }
                        }
                     } else {
                        if (params[0].equals("unclaim")) {
                           if (!collar.isOwner(stack, player)) {
                              if (!collar.hasOwner(stack)) {
                                 Utils.sendErrorMessageToEntity(player, "The collar is not claimed yet");
                              } else {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                              }

                              return;
                           }

                           collar.removeOwner(stack, player);
                           Utils.sendValidMessageToEntity(player, "Collar successfully unclaimed!");
                           return;
                        }

                        if (params[0].equals("whitelist")) {
                           if (!collar.isOwner(stack, player)) {
                              Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                              return;
                           }

                           collar.setBlackListState(stack, false);
                           Utils.sendValidMessageToEntity(player, "Whitelist enabled (and blacklist disbaled)");
                           return;
                        }

                        if (params[0].equals("whitelistadd")) {
                           if (!collar.isOwner(stack, player)) {
                              if (!collar.hasOwner(stack)) {
                                 Utils.sendErrorMessageToEntity(player, "The collar is not claimed yet");
                              } else {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                              }

                              return;
                           }

                           players = this.getTargets(server, sender, params);
                           var8 = players.iterator();

                           while(var8.hasNext()) {
                              target = (EntityPlayerMP)var8.next();
                              if (target != null) {
                                 if (!collar.isTargetException(stack, (EntityLivingBase)target)) {
                                    collar.addTargetException(stack, target);
                                    Utils.sendValidMessageToEntity(player, target.func_70005_c_() + " added to the whitelist.");
                                 } else {
                                    Utils.sendErrorMessageToEntity(player, target.func_70005_c_() + " is already on the whitelist.");
                                 }
                              }
                           }
                        } else if (params[0].equals("whitelistremove")) {
                           if (params.length < 2) {
                              Utils.sendErrorMessageToEntity(player, "You must specify a name.");
                              return;
                           }

                           if (!collar.isOwner(stack, player)) {
                              Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                              return;
                           }

                           message = params[1];
                           if (message != null) {
                              if (!collar.isTargetExceptionByName(stack, message)) {
                                 Utils.sendErrorMessageToEntity(player, "This player is not on the whitelist.");
                                 return;
                              }

                              collar.removeTargetException(stack, message);
                              Utils.sendValidMessageToEntity(player, message + " has been removed from the whitelist.");
                              return;
                           }
                        } else {
                           if (params[0].equals("blacklist")) {
                              if (!collar.isOwner(stack, player)) {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                 return;
                              }

                              collar.setBlackListState(stack, true);
                              Utils.sendValidMessageToEntity(player, "Blacklist enabled (and whitelist disbaled)");
                              return;
                           }

                           if (params[0].equals("blacklistadd")) {
                              if (!collar.isOwner(stack, player)) {
                                 if (!collar.hasOwner(stack)) {
                                    Utils.sendErrorMessageToEntity(player, "The collar is not claimed yet");
                                 } else {
                                    Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                 }

                                 return;
                              }

                              players = this.getTargets(server, sender, params);
                              var8 = players.iterator();

                              while(var8.hasNext()) {
                                 target = (EntityPlayerMP)var8.next();
                                 if (target != null) {
                                    if (!collar.isOnBlackList(stack, (EntityLivingBase)target)) {
                                       collar.addToBlackList(stack, target);
                                       Utils.sendValidMessageToEntity(player, target.func_70005_c_() + " added to the blacklist.");
                                    } else {
                                       Utils.sendErrorMessageToEntity(player, target.func_70005_c_() + " is already on the blacklist.");
                                    }
                                 }
                              }
                           } else if (params[0].equals("blacklistremove")) {
                              if (params.length < 2) {
                                 Utils.sendErrorMessageToEntity(player, "You must specify a name.");
                                 return;
                              }

                              if (!collar.isOwner(stack, player)) {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                 return;
                              }

                              message = params[1];
                              if (message != null) {
                                 if (!collar.isOnBlackListByName(stack, message)) {
                                    Utils.sendErrorMessageToEntity(player, "This player is not on the blacklist.");
                                    return;
                                 }

                                 collar.removeFromBlackList(stack, message);
                                 Utils.sendValidMessageToEntity(player, message + " has been removed from the blacklist.");
                                 return;
                              }
                           } else if (params[0].equals("public")) {
                              if (!(stack.func_77973_b() instanceof ItemShockCollar)) {
                                 Utils.sendErrorMessageToEntity(player, "You can only use this on a shocker collar");
                                 return;
                              }

                              if (!collar.isOwner(stack, player)) {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                 return;
                              }

                              ItemShockCollar shockCollar = (ItemShockCollar)stack.func_77973_b();
                              if (shockCollar.isPublic(stack)) {
                                 shockCollar.disablePublicMode(stack);
                                 Utils.sendValidMessageToEntity(player, "Public mode disabled!");
                              } else {
                                 shockCollar.enablePublicMode(stack);
                                 Utils.sendValidMessageToEntity(player, "Public mode enabled!");
                              }
                           } else if (params[0].equals("prisonfence")) {
                              if (!collar.isOwner(stack, player)) {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                 return;
                              }

                              if (collar.isPrisonFenceEnabled(stack)) {
                                 collar.setPrisonFencee(stack, false);
                                 Utils.sendValidMessageToEntity(player, "Prison fence disabled!");
                              } else {
                                 collar.setPrisonFencee(stack, true);
                                 Utils.sendValidMessageToEntity(player, "Prison fence enabled!");
                              }
                           } else if (params[0].equals("warnmasters")) {
                              if (!collar.isOwner(stack, player)) {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                 return;
                              }

                              if (collar.shouldWarnMasters(stack)) {
                                 collar.disableWarnMasters(stack);
                                 Utils.sendValidMessageToEntity(player, "Do not warn masters after capturing someone.");
                              } else {
                                 collar.enableWarnMasters(stack);
                                 Utils.sendValidMessageToEntity(player, "Warn masters after capturing someone.");
                              }
                           } else if (params[0].equals("backhome")) {
                              if (!collar.isOwner(stack, player)) {
                                 Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                 return;
                              }

                              if (collar.shouldBackHome(stack)) {
                                 collar.setBackHome(stack, false);
                                 Utils.sendValidMessageToEntity(player, "Do not back home after kidnapping.");
                              } else {
                                 collar.setBackHome(stack, true);
                                 Utils.sendValidMessageToEntity(player, "Back home after kidnapping.");
                              }
                           } else {
                              if (params[0].equals("kidnapping")) {
                                 if (!collar.isOwner(stack, player)) {
                                    Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                    return;
                                 }

                                 if (collar.isKidnappingModeEnabled(stack)) {
                                    collar.disableKidnappingMode(stack);
                                    Utils.sendValidMessageToEntity(player, "Kidnapping mode disabled!");
                                    return;
                                 }

                                 collar.enableKidnappingMode(stack);
                                 Utils.sendValidMessageToEntity(player, "Kidnapping mode enabled!");
                                 return;
                              }

                              if (params[0].equals("bondageservice")) {
                                 if (!collar.isOwner(stack, player)) {
                                    Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                    return;
                                 }

                                 if (collar.isBondageServiceEnabled(stack)) {
                                    collar.disableBondageService(stack);
                                    Utils.sendValidMessageToEntity(player, "Bondage service disabled!");
                                    return;
                                 }

                                 collar.enableBondageService(stack);
                                 Utils.sendValidMessageToEntity(player, "Bondage service enabled!");
                                 return;
                              }

                              Position pos;
                              if (params[0].equals("sethome")) {
                                 if (!collar.isOwner(stack, player)) {
                                    Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                    return;
                                 }

                                 if (params.length >= 2) {
                                    message = params[1];
                                    if (message.equals("reset")) {
                                       Utils.sendValidMessageToEntity(player, "Home reset!");
                                       collar.removeHome(stack);
                                       return;
                                    }
                                 } else {
                                    pos = Utils.getEntityPosition(player);
                                    if (pos != null) {
                                       collar.setHome(stack, pos);
                                       Utils.sendValidMessageToEntity(player, "Home set!");
                                       return;
                                    }
                                 }
                              } else if (params[0].equals("setprison")) {
                                 if (!collar.isOwner(stack, player)) {
                                    Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                    return;
                                 }

                                 if (params.length >= 2) {
                                    message = params[1];
                                    if (message.equals("reset")) {
                                       Utils.sendValidMessageToEntity(player, "Prison reset!");
                                       collar.removePrison(stack);
                                       return;
                                    }
                                 } else {
                                    pos = Utils.getEntityPosition(player);
                                    if (pos != null) {
                                       collar.setPrison(stack, pos);
                                       Utils.sendValidMessageToEntity(player, "Prison set!");
                                       return;
                                    }
                                 }
                              } else if (params[0].equals("servicemessage")) {
                                 if (params.length < 2) {
                                    Utils.sendErrorMessageToEntity(player, "You must specify a message. (or reset)");
                                    return;
                                 }

                                 if (!collar.isOwner(stack, player)) {
                                    Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                    return;
                                 }

                                 message = Utils.getMessageFromArray(params, 1, params.length);
                                 if (message != null) {
                                    if (message.length() > 150) {
                                       Utils.sendErrorMessageToEntity(player, "You're message is too long.");
                                       return;
                                    }

                                    if (message.equals("reset")) {
                                       collar.removeServiceSentence(stack);
                                       Utils.sendValidMessageToEntity(player, "Bondage service message reset!");
                                       return;
                                    }

                                    collar.setServiceSentence(stack, message);
                                    Utils.sendValidMessageToEntity(player, "Bondage service message set!");
                                    return;
                                 }
                              } else if (params[0].equals("prisonradius")) {
                                 if (params.length < 2) {
                                    Utils.sendErrorMessageToEntity(player, "You must specify a radius.");
                                    return;
                                 }

                                 if (!collar.isOwner(stack, player)) {
                                    Utils.sendErrorMessageToEntity(player, "You're not one of the owners of this collar");
                                    return;
                                 }

                                 try {
                                    int radius = Integer.valueOf(params[1]);
                                    if (radius < 3) {
                                       Utils.sendErrorMessageToEntity(sender, "You must specify a value which is greater or equal to 3");
                                    } else if (radius > 1000) {
                                       Utils.sendErrorMessageToEntity(sender, "You must specify a value which is less or equal to 1000");
                                    } else {
                                       collar.setPrisonRadius(stack, radius);
                                       Utils.sendValidMessageToEntity(player, "Prison radius set!");
                                    }
                                 } catch (NumberFormatException var10) {
                                    Utils.sendErrorMessageToEntity(sender, "You must specify a radius value (ex : 10)");
                                 }
                              } else {
                                 Utils.sendErrorMessageToEntity(player, "Use : /collar [claim / unclaim / rename / addowner / public / whitelist / whitelistadd / whitelistremove / blacklist / blacklistadd / blacklistremove / sethome / setprison / prisonradius / prisonfence / backhome / kidnapping / warnmasters / bondageservice / servicemessage]");
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               Utils.sendErrorMessageToEntity(player, "You must held a collar.");
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
         List<String> subOptions = new ArrayList();
         if (args.length == 2) {
            if (args[0].equals("addowner") || args[0].equals("whitelistadd") || args[0].equals("blacklistadd")) {
               return super.func_184883_a(server, sender, args, targetPos);
            }

            if ((args[0].equals("whitelistremove") || args[0].equals("blacklistremove")) && sender instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)sender;
               ItemStack stack = player.func_184614_ca();
               if (stack != null && stack.func_77973_b() instanceof ItemCollar) {
                  ItemCollar collar = (ItemCollar)stack.func_77973_b();
                  if (collar != null) {
                     if (args[0].equals("whitelistremove")) {
                        return func_175762_a(args, collar.getTargetExceptionNames(stack));
                     }

                     if (args[0].equals("blacklistremove")) {
                        return func_175762_a(args, collar.getBlacklistNames(stack));
                     }
                  }
               }
            }

            if (args[0].equals("sethome") || args[0].equals("setprison") || args[0].equals("servicemessage")) {
               subOptions.add("reset");
               return func_175762_a(args, subOptions);
            }
         }

         return Collections.emptyList();
      }
   }

   public boolean isBypassingBlock() {
      return true;
   }

   static {
      options.add("claim");
      options.add("unclaim");
      options.add("rename");
      options.add("addowner");
      options.add("whitelist");
      options.add("whitelistadd");
      options.add("whitelistremove");
      options.add("blacklist");
      options.add("blacklistadd");
      options.add("blacklistremove");
      options.add("public");
      options.add("sethome");
      options.add("setprison");
      options.add("prisonradius");
      options.add("prisonfence");
      options.add("backhome");
      options.add("kidnapping");
      options.add("bondageservice");
      options.add("servicemessage");
      options.add("warnmasters");
   }
}
