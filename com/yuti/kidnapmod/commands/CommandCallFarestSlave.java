package com.yuti.kidnapmod.commands;

import com.google.common.base.Predicate;
import com.yuti.kidnapmod.data.SlavesUnloadLocationsData;
import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class CommandCallFarestSlave extends CommandKidnapMod {
   public String func_71517_b() {
      return "callfarestslave";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.callfarestslave";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      if (sender instanceof EntityPlayer) {
         final EntityPlayer player = (EntityPlayer)sender;
         if (player != null && server != null) {
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null) {
               long radius = -1L;
               boolean hasRadius = false;
               if (params.length > 0) {
                  String radiusStr = params[0];

                  try {
                     radius = Long.parseLong(radiusStr);
                     hasRadius = true;
                  } catch (NumberFormatException var24) {
                     Utils.sendErrorMessageToEntity(player, "Wrong number for radius");
                     return;
                  }

                  if (radius < 0L) {
                     Utils.sendErrorMessageToEntity(player, "Radius must be >= 0");
                     return;
                  }
               }

               Timer timer = state.getTimerCallBackSlave();
               int timeToWait;
               if (timer != null && timer.getSecondsRemaining() > 0 && !Utils.isOpe(player)) {
                  timeToWait = timer.getSecondsRemaining();
                  Utils.sendMessageToEntity(player, "You have to wait " + timeToWait + " seconds before executing this command again.");
                  return;
               }

               timeToWait = 60;
               if (player.field_70170_p != null) {
                  GameRules rules = player.field_70170_p.func_82736_K();
                  if (rules != null && rules.func_82765_e("time_callback_slave")) {
                     timeToWait = rules.func_180263_c("time_callback_slave");
                  }
               }

               timer = new Timer(timeToWait);
               state.setTimerCallBackSlave(timer);
               World playerWorld = player.func_130014_f_();
               UUID playerID = player.func_110124_au();
               radius *= radius;
               if (playerWorld != null && playerID != null) {
                  Position farPosition = null;
                  SlavesUnloadLocationsData data = SlavesUnloadLocationsData.get(playerWorld);
                  if (data != null) {
                     Set<Position> positionsToLoad = data.getPositionsCopy(playerID);
                     if (positionsToLoad != null) {
                        Iterator var16 = positionsToLoad.iterator();

                        label120:
                        while(true) {
                           Position posLoad;
                           double distanceToPosLoad;
                           do {
                              do {
                                 do {
                                    do {
                                       if (!var16.hasNext()) {
                                          break label120;
                                       }

                                       posLoad = (Position)var16.next();
                                    } while(posLoad == null);
                                 } while(posLoad.getDimension() != player.field_71093_bK);

                                 distanceToPosLoad = player.func_174818_b(posLoad.getBlockPos());
                              } while(hasRadius && !(distanceToPosLoad < (double)radius));
                           } while(farPosition != null && !(player.func_174818_b(farPosition.getBlockPos()) < distanceToPosLoad));

                           farPosition = posLoad;
                        }
                     }

                     if (farPosition != null) {
                        Utils.loadChunkFromPosition(farPosition);
                     }

                     Predicate<EntityDamsel> filter = new Predicate<EntityDamsel>() {
                        public boolean apply(EntityDamsel input) {
                           return input == null ? false : input.isMaster(player);
                        }
                     };
                     EntityDamsel damselToTp = null;
                     farPosition = null;
                     List<EntityDamsel> slaves = playerWorld.func_175644_a(EntityDamsel.class, filter);
                     if (slaves != null) {
                        Iterator var19 = slaves.iterator();

                        label91:
                        while(true) {
                           EntityDamsel slave;
                           Position slavePosition;
                           double distanceToSlavePosition;
                           do {
                              do {
                                 do {
                                    if (!var19.hasNext()) {
                                       break label91;
                                    }

                                    slave = (EntityDamsel)var19.next();
                                    slavePosition = Utils.getEntityPosition(slave);
                                 } while(slavePosition == null);

                                 distanceToSlavePosition = player.func_174818_b(slavePosition.getBlockPos());
                              } while(hasRadius && !(distanceToSlavePosition < (double)radius));
                           } while(farPosition != null && !(player.func_174818_b(farPosition.getBlockPos()) < distanceToSlavePosition));

                           farPosition = slavePosition;
                           damselToTp = slave;
                        }
                     }

                     if (damselToTp != null) {
                        Position playerPosition = Utils.getEntityPosition(player);
                        if (playerPosition != null) {
                           damselToTp.teleportToPosition(playerPosition);
                           Utils.sendValidMessageToEntity(player, "Slave teleported!");
                        }
                     } else {
                        Utils.sendInfoMessageToEntity(player, "No slaves were telported.");
                     }
                  }
               }
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
}
