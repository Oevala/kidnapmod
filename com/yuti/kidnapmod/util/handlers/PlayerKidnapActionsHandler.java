package com.yuti.kidnapmod.util.handlers;

import com.google.common.base.Predicate;
import com.yuti.kidnapmod.commands.CommandKidnapMod;
import com.yuti.kidnapmod.data.BountiesData;
import com.yuti.kidnapmod.data.UsersSettingsData;
import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.entities.EntityInvisibleSlaveTransporter;
import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.entities.guests.EntityBirdy;
import com.yuti.kidnapmod.entities.render.RenderEntityDamsel;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemKnife;
import com.yuti.kidnapmod.items.tasks.UntyingDamselTask;
import com.yuti.kidnapmod.items.tasks.UntyingPlayerTask;
import com.yuti.kidnapmod.items.tasks.UntyingTask;
import com.yuti.kidnapmod.loaders.ItemTask;
import com.yuti.kidnapmod.loaders.common.GagTalkLoader;
import com.yuti.kidnapmod.models.render.RenderUtilsDamsel;
import com.yuti.kidnapmod.models.render.RenderUtilsPlayer;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketJoinServer;
import com.yuti.kidnapmod.network.PacketRequestOpenGuiManageSlavesServices;
import com.yuti.kidnapmod.network.PacketRequestOpenGuiTakeOffBondageItem;
import com.yuti.kidnapmod.network.PacketUntying;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapper;
import com.yuti.kidnapmod.states.kidnapped.managers.PlayerKidnapperManager;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.UtilsParameters;
import com.yuti.kidnapmod.util.teleport.Position;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemLead;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent.Pre;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.ChunkDataEvent.Save;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.ChunkEvent.Unload;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class PlayerKidnapActionsHandler {
   @SubscribeEvent
   public static void onPlayerTalk(ServerChatEvent event) {
      EntityPlayer player = event.getPlayer();
      if (player != null && player.field_70170_p != null && !player.field_70170_p.field_72995_K) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         event.setCanceled(true);
         if (state != null) {
            if (state.hasGaggingEffect()) {
               GagTalkLoader loader = GagTalkLoader.getInstance();
               if (loader != null) {
                  String newMessage = TextFormatting.WHITE + "<" + player.func_145748_c_().func_150254_d() + "> " + loader.gagTalkConvertor(player.field_70170_p, event.getMessage());
                  event.setComponent(new TextComponentString(newMessage));
               }
            }

            if (state.isSlave()) {
               I_Kidnapper kidnapper = state.getMaster();
               if (kidnapper != null) {
                  kidnapper.onSlaveTalk(state);
               }
            }

            UsersSettingsData settings = UsersSettingsData.get(player.field_70170_p);
            if (settings != null) {
               int area = settings.getTalkArea(player);
               if (area >= 0) {
                  Utils.sendMessageToPlayersInArea(player, (double)area, event.getComponent());
                  return;
               }
            }
         }

         Utils.sendMessageToPlayers(player, event.getComponent());
      }
   }

   @SubscribeEvent
   public static void onPlayerFormatName(NameFormat event) {
      EntityPlayer player = event.getEntityPlayer();
      if (player != null) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.hasNamedCollar()) {
            String nickname = state.getNameFromCollar();
            if (nickname != null && !nickname.equals("")) {
               event.setDisplayname(TextFormatting.GOLD + nickname + " - " + player.func_70005_c_());
            }
         }
      }

   }

   @SubscribeEvent
   public static synchronized void onPlayerEnslave(EntityInteract e) {
      if (!e.getWorld().field_72995_K) {
         if (e.getTarget() instanceof EntityPlayer) {
            EntityPlayer player = e.getEntityPlayer();
            if (e.getHand() == EnumHand.MAIN_HAND) {
               EntityPlayer target = (EntityPlayer)e.getTarget();
               PlayerBindState targetState = PlayerBindState.getInstance(target);
               PlayerBindState playerState = PlayerBindState.getInstance(player);
               if (playerState != null && !playerState.isTiedUp()) {
                  I_Kidnapper playerKidnapper = playerState.getSlaveHolderManager();
                  if (player.func_184614_ca().func_77973_b() == Items.field_151058_ca) {
                     if (targetState != null && targetState.isTiedUp() && targetState.isEnslavable() && targetState.getEnslavedBy(playerKidnapper)) {
                        player.func_184614_ca().func_190918_g(1);
                     }
                  } else if (!Utils.isSpecialBindItem(player.func_184614_ca().func_77973_b()) && !player.func_70093_af() && targetState != null && targetState.isTiedUp() && targetState.isSlave()) {
                     I_Kidnapper master = targetState.getMaster();
                     if (master != null && master.equals(playerKidnapper)) {
                        if (targetState.isTiedToPole()) {
                           targetState.untieSlaveFromPole();
                        } else {
                           targetState.free();
                        }
                     } else {
                        targetState.transferSlaveryTo(playerKidnapper);
                     }
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent(
      priority = EventPriority.LOWEST
   )
   public static synchronized void onDamselInteract(EntityInteract e) {
      if (e.getTarget() instanceof EntityDamsel) {
         EntityPlayer player = e.getEntityPlayer();
         if (e.getHand() == EnumHand.MAIN_HAND) {
            EntityDamsel target = (EntityDamsel)e.getTarget();
            PlayerBindState playerState = PlayerBindState.getInstance(player);
            if (playerState != null && !playerState.isTiedUp() && target.func_110167_bD()) {
               boolean itemUsed = Utils.isSpecialBindItem(player.func_184614_ca().func_77973_b());
               if (!itemUsed && !player.func_70093_af() && (target.isTiedToPole() || target.func_110166_bE() != null && !target.func_110166_bE().equals(player))) {
                  e.setCanceled(true);
                  e.setCancellationResult(EnumActionResult.SUCCESS);
                  I_Kidnapper playerKidnapper = playerState.getSlaveHolderManager();
                  target.transferSlaveryTo(playerKidnapper);
               } else if (itemUsed || player.func_70093_af()) {
                  e.setCanceled(true);
                  e.setCancellationResult(EnumActionResult.SUCCESS);
                  if (itemUsed) {
                     Item item = player.func_184614_ca().func_77973_b();
                     item.func_111207_a(player.func_184614_ca(), player, target, EnumHand.MAIN_HAND);
                  }
               }
            }
         }
      }

   }

   @SubscribeEvent
   public static void onPlayerCheckStillSlaveOfPlayer(PlayerTickEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         PlayerBindState bindState = PlayerBindState.getInstance(event.player);
         bindState.checkStillSlave();
      }
   }

   @SubscribeEvent
   public static void onPlayerCheckSlaveTransport(WorldTickEvent event) {
      if (!event.world.field_72995_K) {
         Predicate<EntityInvisibleSlaveTransporter> prediTransport = new Predicate<EntityInvisibleSlaveTransporter>() {
            public boolean apply(EntityInvisibleSlaveTransporter input) {
               return true;
            }
         };
         List<EntityInvisibleSlaveTransporter> entitiesSlaveTransporter = event.world.func_175644_a(EntityInvisibleSlaveTransporter.class, prediTransport);
         if (entitiesSlaveTransporter != null && !entitiesSlaveTransporter.isEmpty()) {
            Iterator var3 = entitiesSlaveTransporter.iterator();

            while(var3.hasNext()) {
               EntityInvisibleSlaveTransporter entity = (EntityInvisibleSlaveTransporter)var3.next();
               if (entity != null) {
                  entity.check();
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static void onPlayerReconnect(PlayerLoggedInEvent event) {
      PacketHandler.INSTANCE.sendTo(new PacketJoinServer(), (EntityPlayerMP)event.player);
      PlayerBindState state = PlayerBindState.getInstance(event.player);
      state.resetNewConnection(event.player);
      UsersSettingsData settings = UsersSettingsData.get(event.player.field_70170_p);
      if (settings != null) {
         int area = settings.getTalkArea(event.player);
         if (area >= 0) {
            Utils.sendValidMessageToEntity(event.player, "Your talk area is set on " + area);
         }
      }

   }

   @SubscribeEvent
   public static void onLogout(PlayerLoggedOutEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         PlayerBindState state = PlayerBindState.getInstance(event.player);
         if (state.getMaster() != null) {
            state.getMaster().onSlaveLogout(state);
         }

         state.setOnline(false);
      }
   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void onLogoutClient(PlayerLoggedOutEvent event) {
      if (event.player.equals(Minecraft.func_71410_x().field_71439_g)) {
         PlayerBindState.resetInstances();
      }

   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void onRenderPlayer(Pre event) {
      EntityPlayer player = event.getEntityPlayer();
      if (player != null) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null) {
            RenderPlayer renderer = event.getRenderer();
            if (renderer == null) {
               return;
            }

            RenderLivingBase<AbstractClientPlayer> render = RenderUtilsPlayer.getKidnapModPlayerRenderer(player);
            if (render != null) {
               event.setCanceled(true);
               render.func_76986_a((AbstractClientPlayer)player, event.getX(), event.getY(), event.getZ(), 0.0F, event.getPartialRenderTick());
            }
         }
      }

   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void onRenderDamsel(net.minecraftforge.client.event.RenderLivingEvent.Pre<EntityDamsel> event) {
      EntityLivingBase entity = event.getEntity();
      if (entity != null && entity instanceof EntityDamsel) {
         EntityDamsel damsel = (EntityDamsel)event.getEntity();
         if (damsel != null) {
            RenderLivingBase<EntityDamsel> renderer = event.getRenderer();
            if (renderer == null || !(renderer instanceof RenderEntityDamsel)) {
               return;
            }

            renderer = RenderUtilsDamsel.getKidnapModDamselRenderer(damsel);
            if (renderer != null) {
               event.setCanceled(true);
               renderer.func_76986_a(damsel, event.getX(), event.getY(), event.getZ(), 0.0F, event.getPartialRenderTick());
            }
         }
      }

   }

   @SubscribeEvent
   public static void onPlayerInnvunabilitFallyWhenLeashed(LivingFallEvent event) {
      Entity entity = event.getEntity();
      if (entity != null && !entity.field_70170_p.field_72995_K && entity instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entity;
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.isSlave()) {
            event.setCanceled(true);
         }
      }

   }

   @SubscribeEvent
   public static void onPlayerLeashedNotAttackableByMonster(LivingSetAttackTargetEvent event) {
      EntityLivingBase entity = event.getTarget();
      EntityLivingBase mob = event.getEntityLiving();
      if (entity instanceof EntityPlayer && mob instanceof EntityLiving) {
         EntityPlayer player = (EntityPlayer)entity;
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.isSlave()) {
            EntityLiving realMob = (EntityLiving)mob;
            realMob.func_70624_b((EntityLivingBase)null);
         }
      }

   }

   @SubscribeEvent(
      priority = EventPriority.HIGHEST
   )
   public static void onSlaveClick(PlayerInteractEvent event) {
      EntityPlayer player = event.getEntityPlayer();
      PlayerBindState state = PlayerBindState.getInstance(player);
      if (state != null && state.isTiedUp()) {
         ItemStack item = event.getItemStack();
         if (event instanceof RightClickItem && item != null && item.func_77973_b() instanceof ItemKnife) {
            return;
         }

         if (event.isCancelable()) {
            event.setCanceled(true);
         }
      }

   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void preventPlayerSneakingWhileTiedUp(InputUpdateEvent event) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      if (player != null) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.isTiedUp() && event.getMovementInput().field_78899_d) {
            event.getMovementInput().field_78899_d = false;
         }
      }

   }

   @SubscribeEvent
   public static void preventSneakingTiedUpServer(PlayerTickEvent event) {
      EntityPlayer player = event.player;
      if (player != null && !player.field_70170_p.field_72995_K) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.isTiedUp()) {
            player.func_70095_a(false);
         }
      }

   }

   @SubscribeEvent
   public static void preventSlaveToAttack(AttackEntityEvent event) {
      EntityPlayer player = event.getEntityPlayer();
      PlayerBindState state = PlayerBindState.getInstance(player);
      if (state != null && state.isTiedUp()) {
         event.setCanceled(true);
      }

   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void onRenderTiedUpHand(RenderSpecificHandEvent event) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      if (player != null) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.isTiedUp()) {
            event.setCanceled(true);
         }
      }

   }

   @SubscribeEvent
   public static synchronized void onPlayerInteractWithTiedUpPlayer(EntityInteract e) {
      if (!e.getEntity().field_70170_p.field_72995_K) {
         if (e.getTarget() instanceof EntityLivingBase && (e.getTarget() instanceof EntityPlayer || e.getTarget() instanceof I_Kidnapped)) {
            EntityPlayer player = e.getEntityPlayer();
            I_Kidnapped targetState = null;
            EntityLivingBase target = (EntityLivingBase)e.getTarget();
            if (e.getTarget() instanceof EntityPlayer) {
               EntityPlayer targetPlayer = (EntityPlayer)e.getTarget();
               targetState = PlayerBindState.getInstance(targetPlayer);
            } else {
               targetState = (I_Kidnapped)e.getTarget();
            }

            if (player != null && targetState != null) {
               PlayerBindState playerState = PlayerBindState.getInstance(player);
               if (playerState != null && !playerState.isTiedUp() && ((I_Kidnapped)targetState).isTiedUp() && e.getHand() == EnumHand.MAIN_HAND) {
                  ItemStack stack = player.func_184614_ca();
                  Item item = stack.func_77973_b();
                  if (e.getTarget() instanceof EntityPlayer && targetState instanceof PlayerBindState && item instanceof ItemFood) {
                     onPlayerFeedSlave(player, (EntityPlayer)e.getTarget(), (PlayerBindState)targetState);
                  } else if (item instanceof ItemPotion) {
                     onPlayerForceDrinkSlave(player, target);
                  } else if (item instanceof ItemBucketMilk) {
                     onPlayerGiveMilToTarget(stack, player, target);
                  }

                  if (!Utils.isSpecialBindItem(item) && player.func_70093_af()) {
                     onPlayerRemoveExtraBondageItem(player, playerState, e.getTarget(), (I_Kidnapped)targetState);
                  }
               }
            }
         }

      }
   }

   private static synchronized void onPlayerRemoveExtraBondageItem(EntityPlayer player, PlayerBindState playerState, Entity target, I_Kidnapped targetState) {
      if (!playerState.isTiedUp() && targetState.isTiedUp() && player.func_70093_af()) {
         UUID targetUUID = target.func_110124_au();
         if (targetUUID != null) {
            boolean gagged = targetState.isGagged();
            boolean blindfolded = targetState.isBlindfolded();
            boolean earsplugsOn = targetState.hasEarplugs();
            boolean collarState = targetState.hasCollar() && !targetState.hasLockedCollar();
            boolean knivesState = targetState.hasKnives();
            boolean clothesState = targetState.canTakesOffClothes(player);
            if (gagged || blindfolded || earsplugsOn || collarState || knivesState || clothesState) {
               PacketHandler.INSTANCE.sendTo(new PacketRequestOpenGuiTakeOffBondageItem(gagged, blindfolded, earsplugsOn, collarState, knivesState, clothesState, targetUUID.toString()), (EntityPlayerMP)player);
            }
         }
      }

   }

   private static void onPlayerGiveMilToTarget(ItemStack stack, EntityPlayer player, EntityLivingBase target) {
      if (player != null && target != null) {
         Collection<PotionEffect> effects = target.func_70651_bq();
         if (effects != null && !effects.isEmpty()) {
            if (!player.func_184812_l_()) {
               stack.func_190918_g(1);
               player.func_191521_c(new ItemStack(Items.field_151133_ar));
            }

            target.func_70674_bp();
         }
      }

   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static synchronized void preventFOVUpdate(FOVUpdateEvent event) {
      EntityPlayer player = event.getEntity();
      PlayerBindState state = PlayerBindState.getInstance(player);
      if (state != null && state.isTiedUp()) {
         event.setNewfov(1.0F);
      }

   }

   @SubscribeEvent
   public static void preventSprintTiedUp(PlayerTickEvent event) {
      EntityPlayer player = event.player;
      if (player.func_70051_ag()) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && state.isTiedUp()) {
            player.func_70031_b(false);
         }
      }

   }

   @SubscribeEvent
   public static synchronized void onPlayerInteractWithKidnapper(EntityInteract e) {
      if (!e.getWorld().field_72995_K) {
         if (e.getTarget() instanceof EntityKidnapper) {
            EntityKidnapper kidnapper = (EntityKidnapper)e.getTarget();
            EntityPlayer player = e.getEntityPlayer();
            PlayerBindState playerState = PlayerBindState.getInstance(player);
            if (playerState != null && !playerState.isTiedUp() && e.getHand() == EnumHand.MAIN_HAND && !kidnapper.isTiedUp()) {
               ItemStack stackHeld = player.func_184614_ca();
               if (kidnapper.hasSlaves() && kidnapper.getSlave() != null && kidnapper.getSlave().isForSell()) {
                  ItemTask taskSell = kidnapper.getSlave().getKidnapperSellTask();
                  if (taskSell == null) {
                     return;
                  }

                  ItemStack taskStack = taskSell.getItemStack();
                  if (stackHeld != null && !stackHeld.func_190926_b() && ItemTask.doesStackCompleteTask(stackHeld, taskSell.getItemStack())) {
                     stackHeld.func_190918_g(taskStack.func_190916_E());
                     kidnapper.talkTo(player, "Thanks you! Here's your slave!");
                     PlayerBindState slave = kidnapper.getSlave();
                     if (slave != null && slave.getPlayer() != null) {
                        kidnapper.sellSlaveTo(slave, playerState);
                     }
                  } else {
                     kidnapper.talkTo(player, "This slave cost : " + taskSell.toString());
                  }
               } else if (kidnapper.isWaitingForJobToBeCompleted()) {
                  kidnapper.checkJobComplete(player, player.func_184614_ca());
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static void onRespawnEvent(PlayerRespawnEvent e) {
      PlayerBindState state = PlayerBindState.getInstance(e.player);
      if (state != null) {
         state.resetNewConnection(e.player);
      }

   }

   @SubscribeEvent
   public static void onClonePlayer(Clone e) {
      PlayerBindState state = PlayerBindState.getInstance(e.getEntityPlayer());
      if (state != null) {
         state.resetNewConnection(e.getEntityPlayer());
      }

   }

   @SubscribeEvent
   public static void onChangeDimension(PlayerChangedDimensionEvent e) {
      if (e.player != null) {
         PlayerBindState state = PlayerBindState.getInstance(e.player);
         if (state != null) {
            state.resetNewConnection(e.player);
         }
      }

   }

   @SubscribeEvent
   @SideOnly(Side.SERVER)
   public static void onCommandManagement(CommandEvent e) {
      if (e.getSender() instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)e.getSender();
         String[] parameters = e.getParameters();
         if (parameters != null && parameters.length >= 1) {
            boolean checkBlockByPass = false;
            if (e.getCommand() instanceof CommandKidnapMod) {
               CommandKidnapMod command = (CommandKidnapMod)e.getCommand();
               checkBlockByPass = command.isBypassingBlock();
            }

            if (!checkBlockByPass) {
               String potentialName = parameters[0];
               EntityPlayer potentialTarget = Utils.getPlayerFromName(potentialName, player.field_70170_p);
               if (potentialTarget != null) {
                  UsersSettingsData settings = UsersSettingsData.get(potentialTarget.field_70170_p);
                  if (settings != null && settings.checkBlocked(potentialTarget, player)) {
                     e.setCanceled(true);
                     Utils.sendErrorMessageToEntity(player, "You can't use commands on this player");
                     return;
                  }
               }
            }
         }

         if (e.getCommand() instanceof CommandKidnapMod) {
            CommandKidnapMod command = (CommandKidnapMod)e.getCommand();
            if (command.isCallableWhileTiedUp()) {
               return;
            }
         }

         if (!player.func_184812_l_() && !Utils.isOpe(player)) {
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null && state.isTiedUp()) {
               Utils.sendErrorMessageToEntity(player, "You can't perform commands while you're tied up!");
               e.setCanceled(true);
            }
         }
      }

   }

   @SubscribeEvent
   public static void manageAttackDamsel(AttackEntityEvent e) {
      if (!e.getEntity().field_70170_p.field_72995_K) {
         if (e.getTarget() instanceof EntityDamsel) {
            EntityDamsel damsel = (EntityDamsel)e.getTarget();
            if (!damsel.canBeAttacked()) {
               e.setCanceled(true);
            }

            damsel.notifyPlayerAttack(e.getEntityPlayer());
         }

      }
   }

   @SubscribeEvent
   public static void onUntyingTarget(EntityInteract e) {
      if (!e.getEntity().field_70170_p.field_72995_K) {
         Entity target = e.getTarget();
         EntityPlayer player = e.getEntityPlayer();
         if ((target instanceof EntityPlayer || target instanceof I_Kidnapped) && e.getHand() == EnumHand.MAIN_HAND && player.func_184614_ca().func_77973_b() == Items.field_190931_a) {
            I_Kidnapped state = null;
            UntyingTask process_to_load = null;
            if (target instanceof EntityPlayer) {
               state = PlayerBindState.getInstance((EntityPlayer)target);
               process_to_load = new UntyingPlayerTask(player, (PlayerBindState)state, UtilsParameters.getUntyingUpPlayerDelay(player.field_70170_p));
            } else {
               state = (I_Kidnapped)target;
               process_to_load = new UntyingDamselTask(player, (I_Kidnapped)state, UtilsParameters.getUntyingUpPlayerDelay(player.field_70170_p));
            }

            if (state != null && ((I_Kidnapped)state).isTiedUp() && !((I_Kidnapped)state).isForSell()) {
               PlayerBindState kidnapperState = PlayerBindState.getInstance(player);
               if (kidnapperState != null && !kidnapperState.isTiedUp()) {
                  UntyingTask process = kidnapperState.getCurrentUntyingTask();
                  if (process == null || !((UntyingTask)process).isSameTarget((I_Kidnapped)state) || ((UntyingTask)process).isOutdated()) {
                     process = process_to_load;
                     kidnapperState.setCurrentUntyingTask((UntyingTask)process_to_load);
                     ((UntyingTask)process_to_load).start();
                  }

                  ((UntyingTask)process).update();
                  PacketHandler.INSTANCE.sendTo(new PacketUntying(((UntyingTask)process).getState(), UtilsParameters.getUntyingUpPlayerDelay(player.field_70170_p)), (EntityPlayerMP)player);
               }
            }
         }

      }
   }

   public static synchronized void onPlayerFeedSlave(EntityPlayer player, EntityPlayer target, PlayerBindState targetState) {
      if (target.func_71024_bL().func_75121_c()) {
         ItemStack stack = player.func_184614_ca();
         ItemFood food = (ItemFood)stack.func_77973_b();
         target.func_71024_bL().func_151686_a(food, stack);
         target.field_70170_p.func_184148_a((EntityPlayer)null, target.field_70165_t, target.field_70163_u, target.field_70161_v, SoundEvents.field_187739_dZ, SoundCategory.PLAYERS, 0.5F, target.field_70170_p.field_73012_v.nextFloat() * 0.1F + 0.9F);
         target.func_71029_a(StatList.func_188057_b(food));
         targetState.setFoodLevelWatcher(target.func_71024_bL().func_75116_a());
         if (!player.func_184812_l_()) {
            stack.func_190918_g(1);
         }
      } else {
         Utils.sendInfoMessageToEntity(player, "The target isn't hungry");
      }

   }

   public static synchronized void onPlayerForceDrinkSlave(EntityPlayer player, EntityLivingBase target) {
      ItemStack stack = player.func_184614_ca();
      ItemPotion potion = (ItemPotion)stack.func_77973_b();
      if (!player.func_184812_l_()) {
         stack.func_190918_g(1);
      }

      Iterator var4 = PotionUtils.func_185189_a(stack).iterator();

      while(var4.hasNext()) {
         PotionEffect potioneffect = (PotionEffect)var4.next();
         if (potioneffect.func_188419_a().func_76403_b()) {
            potioneffect.func_188419_a().func_180793_a(target, target, target, potioneffect.func_76458_c(), 1.0D);
         } else {
            target.func_70690_d(new PotionEffect(potioneffect));
         }
      }

      if (target != null && target instanceof EntityPlayer) {
         ((EntityPlayer)target).func_71029_a(StatList.func_188057_b(potion));
      }

      if (!player.func_184812_l_()) {
         player.func_191521_c(new ItemStack(Items.field_151069_bo));
      }

   }

   @SubscribeEvent
   public static void onSlaveCheckFood(PlayerTickEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         GameRules rules = event.player.field_70170_p.func_82736_K();
         if (!rules.func_82765_e("slave_starve_warn") || rules.func_82766_b("slave_starve_warn")) {
            PlayerBindState bindState = PlayerBindState.getInstance(event.player);
            if (bindState != null && bindState.isSlave()) {
               int foodLevel = event.player.func_71024_bL().func_75116_a();
               if (foodLevel < bindState.getFoodLevelWatcher() - 1) {
                  bindState.setFoodLevelWatcher(foodLevel);
                  I_Kidnapper master = bindState.getMaster();
                  if (master != null) {
                     master.onSlaveStarving(bindState);
                  }
               }
            }

         }
      }
   }

   @SubscribeEvent
   public static synchronized void onPlayerDeliverSlave(EntityInteract e) {
      if (!e.getWorld().field_72995_K) {
         if (e.getTarget() instanceof EntityPlayer) {
            EntityPlayer player = e.getEntityPlayer();
            EntityPlayer target = (EntityPlayer)e.getTarget();
            PlayerBindState targetState = PlayerBindState.getInstance(target);
            PlayerBindState playerState = PlayerBindState.getInstance(player);
            if (playerState != null && !playerState.isTiedUp()) {
               PlayerKidnapperManager kidnapper = playerState.getSlaveHolderManager();
               if (kidnapper != null && kidnapper.hasSlaves() && targetState != null && !targetState.isTiedUp()) {
                  kidnapper.checkBountiesForClient(target, e.getWorld());
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static synchronized void onPlayerJoinWorld(EntityJoinWorldEvent e) {
      if (!e.getWorld().field_72995_K) {
         if (e.getEntity() instanceof EntityPlayer) {
            BountiesData data = BountiesData.get(e.getWorld());
            data.checkForOldBounties((EntityPlayer)e.getEntity());
         }

      }
   }

   @SubscribeEvent
   public static synchronized void onPlayerPickUpKidnapItem(EntityItemPickupEvent e) {
      EntityPlayer player = e.getEntityPlayer();
      if (player != null) {
         if (player.field_70170_p.field_72995_K) {
            return;
         }

         EntityItem entityItem = e.getItem();
         if (entityItem != null) {
            ItemStack stack = entityItem.func_92059_d();
            if (stack != null) {
               Item item = stack.func_77973_b();
               if (item != null && (item instanceof IExtraBondageItem || item instanceof ItemLead)) {
                  PlayerBindState state = PlayerBindState.getInstance(player);
                  if (state != null && state.isTiedUp()) {
                     e.setCanceled(true);
                  }
               }
            }
         }
      }

   }

   @SubscribeEvent
   @SideOnly(Side.CLIENT)
   public static void onBlindfoldCheckScreen(PlayerTickEvent e) {
      Minecraft mc = Minecraft.func_71410_x();
      PlayerBindState state = PlayerBindState.getInstance(mc.field_71439_g);
      if (state != null && state.hasBlindingEffect()) {
         mc.field_71474_y.field_74319_N = false;
         mc.field_71474_y.field_74330_P = false;
      }

   }

   @SubscribeEvent
   public static void onCheckAutoCheckCollar(PlayerTickEvent event) {
      if (!event.player.field_70170_p.field_72995_K) {
         PlayerBindState bindState = PlayerBindState.getInstance(event.player);
         if (bindState != null) {
            bindState.checkAutoShockCollar();
         }

      }
   }

   @SubscribeEvent
   public static void onPreventAttackArrowOnKidnapper(ProjectileImpactEvent event) {
      if (!event.getEntity().field_70170_p.field_72995_K) {
         RayTraceResult trace = event.getRayTraceResult();
         if (trace != null) {
            Entity target = trace.field_72308_g;
            if (target != null && target instanceof EntityKidnapper) {
               EntityKidnapper kidnapper = (EntityKidnapper)target;
               if (!kidnapper.canBeAttacked()) {
                  event.setCanceled(true);
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static void preventAttackChloroformed(AttackEntityEvent event) {
      Entity target = event.getTarget();
      if (target != null && target.field_70170_p != null && !target.field_70170_p.field_72995_K) {
         if (target instanceof EntityPlayer) {
            EntityPlayer playerTarget = (EntityPlayer)target;
            PotionEffect effect = playerTarget.func_70660_b(Potion.func_188412_a(8));
            if (effect != null && effect.func_76458_c() == 150) {
               event.setCanceled(true);
            }
         }

      }
   }

   @SubscribeEvent
   public static void preventHurtWall(LivingHurtEvent event) {
      EntityLivingBase target = event.getEntityLiving();
      if (target != null && target.field_70170_p != null && !target.field_70170_p.field_72995_K) {
         if (target instanceof EntityPlayer || target instanceof I_Kidnapped) {
            I_Kidnapped targetState = null;
            if (target instanceof EntityPlayer) {
               EntityPlayer targetPlayer = (EntityPlayer)target;
               targetState = PlayerBindState.getInstance(targetPlayer);
            } else {
               targetState = (I_Kidnapped)target;
            }

            DamageSource source = event.getSource();
            if (source == DamageSource.field_76368_d && targetState != null && ((I_Kidnapped)targetState).isTiedUp()) {
               event.setCanceled(true);
            }
         }

      }
   }

   @SubscribeEvent
   public static void onKidnappedDeath(LivingDeathEvent event) {
      EntityLivingBase target = event.getEntityLiving();
      if (target != null && target.field_70170_p != null && !target.field_70170_p.field_72995_K) {
         if (target instanceof EntityPlayer || target instanceof I_Kidnapped) {
            I_Kidnapped targetState = null;
            if (target instanceof EntityPlayer) {
               EntityPlayer targetPlayer = (EntityPlayer)target;
               targetState = PlayerBindState.getInstance(targetPlayer);
            } else {
               targetState = (I_Kidnapped)target;
            }

            if (targetState != null && ((I_Kidnapped)targetState).onDeathKidnapped()) {
               event.setCanceled(true);
            }
         }

      }
   }

   @SubscribeEvent
   public static void onGiveSeedsToBirdy(EntityInteract e) {
      if (!e.getWorld().field_72995_K) {
         if (e.getTarget() instanceof EntityBirdy) {
            GameRules rules = e.getWorld().func_82736_K();
            if (rules == null || !rules.func_82765_e("birdsteregg") || rules.func_82766_b("birdsteregg")) {
               EntityPlayer player = e.getEntityPlayer();
               if (e.getHand() == EnumHand.MAIN_HAND) {
                  EntityBirdy target = (EntityBirdy)e.getTarget();
                  if (!target.hasOwners() && !target.hasLockedCollar()) {
                     ItemStack stackInHand = player.func_184614_ca();
                     if (stackInHand != null && !stackInHand.func_190926_b() && stackInHand.func_77973_b() instanceof ItemSeeds) {
                        BlockPos birdyPos = target.func_180425_c();
                        if (birdyPos != null) {
                           stackInHand.func_190918_g(1);
                           target.free();
                           target.dropBondageItems();
                           target.dropClothes();
                           target.func_174810_b(true);
                           target.func_82142_c(true);
                           target.func_70106_y();
                           EntityParrot parrot = new EntityParrot(e.getWorld());
                           parrot.func_96094_a("Birdy");
                           parrot.func_70107_b((double)birdyPos.func_177958_n(), (double)birdyPos.func_177956_o(), (double)birdyPos.func_177952_p());
                           e.getWorld().func_72838_d(parrot);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static synchronized void onRightClickEmptyInteractionNpc(EntityInteract e) {
      if (!e.getEntity().field_70170_p.field_72995_K) {
         Entity target = e.getTarget();
         EntityPlayer player = e.getEntityPlayer();
         if (player != null && target instanceof EntityDamsel && e.getHand() == EnumHand.MAIN_HAND && player.func_184614_ca().func_77973_b() == Items.field_190931_a) {
            EntityDamsel damsel = (EntityDamsel)target;
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null && !state.isTiedUp()) {
               Timer armsTimer;
               if (!damsel.hasCollar() || damsel.isMaster(player)) {
                  Timer layerTimer;
                  if (damsel.isTiedUp()) {
                     armsTimer = state.getTimerAdjustGagNpc();
                     if (armsTimer != null && armsTimer.getSecondsRemaining() > 0) {
                        state.setTimerAdjustGagNpc((Timer)null);
                        float adjustGag = state.getLoadedNpcAdjustGag();
                        damsel.setGagsAdjustement(adjustGag);
                        Utils.sendValidMessageToEntity(player, "Gags adjusted!");
                     }

                     layerTimer = state.getTimerAdjustBlindfoldNpc();
                     if (layerTimer != null && layerTimer.getSecondsRemaining() > 0) {
                        state.setTimerAdjustBlindfoldNpc((Timer)null);
                        float adjustBlindfold = state.getLoadedNpcAdjustBlindfold();
                        damsel.setBlindfoldsAdjustement(adjustBlindfold);
                        Utils.sendValidMessageToEntity(player, "Blindfolds adjusted!");
                     }
                  }

                  armsTimer = state.getTimerArmsNpc();
                  if (armsTimer != null && armsTimer.getSecondsRemaining() > 0) {
                     state.setTimerArmsNpc((Timer)null);
                     damsel.setSmallArms(state.getLoadSmallArmsNpc());
                     Utils.sendValidMessageToEntity(player, "Arms adjusted!");
                  }

                  layerTimer = state.getTimerLayerNpc();
                  if (layerTimer != null && layerTimer.getSecondsRemaining() > 0) {
                     state.setTimerLayerNpc((Timer)null);
                     EntityDamsel.LayersEnum layer = state.getLoadedDamselLayer();
                     if (layer != null) {
                        boolean enabled = state.getLoadedDamselLayerState();
                        if (layer == EntityDamsel.LayersEnum.HEAD) {
                           damsel.setHeadLayer(enabled);
                        } else if (layer == EntityDamsel.LayersEnum.BODY) {
                           damsel.setBodyLayer(enabled);
                        } else if (layer == EntityDamsel.LayersEnum.LEFTARM) {
                           damsel.setLeftArmLayer(enabled);
                        } else if (layer == EntityDamsel.LayersEnum.RIGHTARM) {
                           damsel.setRightArmLayer(enabled);
                        } else if (layer == EntityDamsel.LayersEnum.LEFTLEG) {
                           damsel.setLeftLegLayer(enabled);
                        } else if (layer == EntityDamsel.LayersEnum.RIGHTLEG) {
                           damsel.setRightLegLayer(enabled);
                        }

                        Utils.sendValidMessageToEntity(player, "Layer adjusted!");
                     }
                  }
               }

               armsTimer = state.getTimerTeleportNpc();
               if (armsTimer != null && armsTimer.getSecondsRemaining() > 0) {
                  state.setTimerTeleportNpc((Timer)null);
                  EntityDamsel.DamselTelportPoints point = state.getLoadTeleportPointNpc();
                  if (point != null) {
                     if (damsel.isMaster(player)) {
                        Position tpPosition = null;
                        if (point == EntityDamsel.DamselTelportPoints.HOME) {
                           tpPosition = damsel.getHome();
                        } else if (point == EntityDamsel.DamselTelportPoints.PRISON) {
                           tpPosition = damsel.getPrison();
                        } else if (point == EntityDamsel.DamselTelportPoints.WARP) {
                           tpPosition = state.getTeleportNpcPos();
                        }

                        if (tpPosition != null) {
                           damsel.teleportToPosition(tpPosition);
                           Utils.sendValidMessageToEntity(player, "The NPC has been teleported!");
                        } else {
                           Utils.sendErrorMessageToEntity(player, "Can't find destination.");
                        }
                     } else {
                        Utils.sendErrorMessageToEntity(player, "You're not the owner of this NPC.");
                     }
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static synchronized void onPlayerInteractWithCollaredSlave(EntityInteract e) {
      if (!e.getEntity().field_70170_p.field_72995_K) {
         if (e.getTarget() instanceof EntityDamsel) {
            EntityPlayer player = e.getEntityPlayer();
            EntityDamsel damsel = (EntityDamsel)e.getTarget();
            if (player != null && damsel != null && player.func_70093_af()) {
               PlayerBindState playerState = PlayerBindState.getInstance(player);
               UUID targetUUID = damsel.getKidnappedUniqueId();
               if (targetUUID != null && playerState != null && !playerState.isTiedUp() && !damsel.isTiedUp() && damsel.hasCollar() && damsel.isMaster(player) && e.getHand() == EnumHand.MAIN_HAND && player.func_184614_ca().func_77973_b() == Items.field_190931_a) {
                  ItemStack collar = damsel.getCurrentCollar();
                  if (collar != null && !collar.func_190926_b() && collar.func_77973_b() != null && collar.func_77973_b() instanceof ItemCollar) {
                     ItemCollar collarItem = (ItemCollar)collar.func_77973_b();
                     if (collarItem != null) {
                        boolean isKidnapper = damsel instanceof EntityKidnapper;
                        Position pos = Utils.getEntityPosition(damsel);
                        damsel.setCurrentEditor(player.func_110124_au());
                        damsel.setCurrentEditionPosition(pos);
                        PacketHandler.INSTANCE.sendTo(new PacketRequestOpenGuiManageSlavesServices(collar, isKidnapper, pos, targetUUID.toString()), (EntityPlayerMP)player);
                     }
                  }
               }
            }
         }

      }
   }

   @SubscribeEvent
   public static synchronized void avoidTransportTransform(EntityStruckByLightningEvent e) {
      if (!e.getEntity().field_70170_p.field_72995_K) {
         if (e.getEntity() instanceof EntityInvisibleSlaveTransporter) {
            e.setCanceled(true);
         }

      }
   }

   @SubscribeEvent
   public static void checkUnloadDamselsUnloadChunk(Unload e) {
      if (!e.getWorld().field_72995_K) {
         Chunk chunk = e.getChunk();
         if (chunk != null) {
            triggerUnloadDamsels(chunk);
         }

      }
   }

   @SubscribeEvent
   public static void checkUnloadDamselsSaveChunk(Save e) {
      if (!e.getWorld().field_72995_K) {
         Chunk chunk = e.getChunk();
         if (chunk != null) {
            triggerUnloadDamsels(chunk);
         }

      }
   }

   @SubscribeEvent
   public static void checkLoadDamselsLoadChunk(Load e) {
      if (!e.getWorld().field_72995_K) {
         Chunk chunk = e.getChunk();
         if (chunk != null) {
            triggerLoadDamsels(chunk);
         }

      }
   }

   private static void triggerUnloadDamsels(Chunk chunk) {
      if (chunk != null) {
         ClassInheritanceMultiMap<Entity>[] entities = chunk.func_177429_s();
         if (entities != null) {
            ClassInheritanceMultiMap[] var2 = entities;
            int var3 = entities.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ClassInheritanceMultiMap<Entity> entityMap = var2[var4];
               if (entityMap != null) {
                  Iterator var6 = entityMap.iterator();

                  while(var6.hasNext()) {
                     Entity entity = (Entity)var6.next();
                     if (entity != null && entity instanceof EntityDamsel) {
                        EntityDamsel damsel = (EntityDamsel)entity;
                        if (damsel.hasCollar() && damsel.hasOwners()) {
                           damsel.onUnload();
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private static void triggerLoadDamsels(Chunk chunk) {
      if (chunk != null) {
         ClassInheritanceMultiMap<Entity>[] entities = chunk.func_177429_s();
         if (entities != null) {
            ClassInheritanceMultiMap[] var2 = entities;
            int var3 = entities.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ClassInheritanceMultiMap<Entity> entityMap = var2[var4];
               if (entityMap != null) {
                  Iterator var6 = entityMap.iterator();

                  while(var6.hasNext()) {
                     Entity entity = (Entity)var6.next();
                     if (entity != null && entity instanceof EntityDamsel) {
                        EntityDamsel damsel = (EntityDamsel)entity;
                        if (damsel.hasCollar() && damsel.hasOwners()) {
                           damsel.onLoad();
                        }
                     }
                  }
               }
            }
         }
      }

   }
}
