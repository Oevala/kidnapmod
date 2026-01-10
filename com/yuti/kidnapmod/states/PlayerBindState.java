package com.yuti.kidnapmod.states;

import com.yuti.kidnapmod.capabilities.KidnapCapabilities;
import com.yuti.kidnapmod.capabilities.KidnapSettingsCapabilities;
import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.entities.EntityInvisibleSlaveTransporter;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemHelper;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemIdentifier;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.init.KidnapModSoundEvents;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.items.ItemKnife;
import com.yuti.kidnapmod.items.ItemShockCollarAuto;
import com.yuti.kidnapmod.items.tasks.PlayerStateTask;
import com.yuti.kidnapmod.items.tasks.TimedTask;
import com.yuti.kidnapmod.items.tasks.TyingTask;
import com.yuti.kidnapmod.items.tasks.UntyingTask;
import com.yuti.kidnapmod.loaders.ItemTask;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapper;
import com.yuti.kidnapmod.states.kidnapped.managers.PlayerKidnapperManager;
import com.yuti.kidnapmod.states.struggle.StruggleBinds;
import com.yuti.kidnapmod.states.struggle.StruggleCollar;
import com.yuti.kidnapmod.states.struggle.StruggleState;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import com.yuti.kidnapmod.util.teleport.TeleportHelper;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlayerBindState implements I_Kidnapped {
   private static Map<UUID, PlayerBindState> instances = new HashMap();
   private static Map<UUID, PlayerBindState> instances_client = new HashMap();
   public boolean forSell = false;
   public boolean online = true;
   private EntityPlayer player;
   private I_Kidnapper master;
   private PlayerKidnapperManager slaveHolderManager;
   private TyingTask currentTyingTask;
   private PlayerStateTask clientTyingTask;
   private UntyingTask currentUntyingTask;
   private PlayerStateTask clientUntyingTask;
   private PlayerStateTask restrainedState;
   public static final int baseFoodLevelWatcher = 8;
   private int foodLevelWatcher = 8;
   private StruggleState struggleBindState = new StruggleBinds();
   private StruggleState struggleCollarState = new StruggleCollar();
   private TimedTask trapPlaceTask;
   private PlayerStateTask clientTrapPlaceTask;
   private Timer timerNoRp;
   private Timer timerCallBackSlave;
   private Timer timerWarnOpNoRp;
   private Timer timerAutoShockCollar;
   private ItemTask kidnapperSellTask;
   private Object lockTimerAutoShock = new Object();
   private boolean deathState = false;
   private float loadedNpcAdjustGag = 0.0F;
   private float loadedNpcAdjustBlindfold = 0.0F;
   private Timer timerAdjustGagNpc;
   private Timer timerAdjustBlindfoldNpc;
   private Position teleportNpcPosWarp;
   private Timer timerTeleportNpc;
   private Timer timerArmsNpc;
   private Timer timerLayerNpc;
   private EntityDamsel.DamselTelportPoints loadTeleportPointNpc;
   private EntityDamsel.LayersEnum loadedDamselLayer;
   private boolean loadedDamselLayerState;
   private boolean loadSmallArmsNpc;

   private PlayerBindState(EntityPlayer player) {
      this.player = player;
      this.slaveHolderManager = new PlayerKidnapperManager(player);
   }

   public static PlayerBindState getInstance(EntityPlayer player) {
      if (player == null) {
         return null;
      } else {
         Map<UUID, PlayerBindState> inst = getInstances(player.field_70170_p);
         UUID id = player.func_110124_au();
         boolean isKnowed = inst.containsKey(player.func_110124_au());
         PlayerBindState state;
         if (isKnowed) {
            state = (PlayerBindState)inst.get(id);
            if (player.field_70170_p.equals(state.player.field_70170_p)) {
               if (player != state.getPlayer()) {
                  state.resetNewConnection(player);
               }

               return state;
            }
         }

         state = new PlayerBindState(player);
         if (!player.field_70170_p.field_72995_K && !isKnowed) {
            state.clearTransport();
            TeleportHelper.doTeleportEntity(player, Utils.getEntityPosition(player));
         }

         inst.put(id, state);
         return state;
      }
   }

   public static void resetInstances() {
      instances_client = new HashMap();
      instances = new HashMap();
   }

   public static List<PlayerBindState> getAllTiedUpPlayers(World world) {
      List<PlayerBindState> players = new ArrayList();
      List<EntityPlayerMP> playerList = Utils.getWorldPlayers(world);
      if (playerList != null && world != null) {
         Iterator var3 = playerList.iterator();

         while(var3.hasNext()) {
            EntityPlayerMP player = (EntityPlayerMP)var3.next();
            if (player != null) {
               PlayerBindState state = getInstance(player);
               if (state != null && state.isTiedUp()) {
                  players.add(state);
               }
            }
         }
      }

      return players;
   }

   public static List<EntityPlayerMP> getPlayerAround(World world, BlockPos pos, double distance) {
      List<EntityPlayerMP> players = new ArrayList();
      List<EntityPlayerMP> playerList = Utils.getWorldPlayers(world);
      if (playerList != null && world != null && pos != null) {
         Iterator var6 = playerList.iterator();

         while(var6.hasNext()) {
            EntityPlayerMP player = (EntityPlayerMP)var6.next();
            if (player != null && player.field_70170_p != null && player.field_70170_p.equals(world) && player.func_70011_f((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p()) <= distance) {
               players.add(player);
            }
         }
      }

      return players;
   }

   public static List<EntityPlayerMP> getPlayers(World world) {
      List<EntityPlayerMP> playerList = Utils.getWorldPlayers(world);
      return (List)(playerList != null ? playerList : new ArrayList());
   }

   private static Map<UUID, PlayerBindState> getInstances(World world) {
      return world.field_72995_K ? instances_client : instances;
   }

   public void resetNewConnection(EntityPlayer player) {
      this.player = player;
      this.online = true;
      this.slaveHolderManager.resetMaster(player);
      this.deathState = false;
      if (this.isTiedUp()) {
         Utils.changePlayerSpeed(player, -0.10000000149011612D);
      }

   }

   public EntityPlayer getPlayer() {
      return this.player;
   }

   public boolean getEnslavedBy(I_Kidnapper newMaster) {
      if (!this.isTiedUp()) {
         return false;
      } else if (this.isLeashed()) {
         return false;
      } else if (!newMaster.canEnslave(this)) {
         return false;
      } else {
         if (newMaster.allowMultipleSlaves()) {
            this.slaveHolderManager.transferAllSlavesTo(newMaster);
         } else {
            this.slaveHolderManager.freeAllSlaves();
         }

         this.setUpTransport();
         newMaster.addSlave(this);
         this.master = newMaster;
         return true;
      }
   }

   private void setUpTransport() {
      EntityInvisibleSlaveTransporter glue = new EntityInvisibleSlaveTransporter(this.player.func_130014_f_());
      glue.setUpAndSpawnToSlave(this);
      this.player.func_184220_m(glue);
   }

   public void free(boolean transportState, boolean leadState) {
      if (this.isSlave() && leadState && this.player != null) {
         this.player.func_71019_a(new ItemStack(Items.field_151058_ca, 1), true);
      }

      if (this.master != null && this.master.canFree(this)) {
         this.master.removeSlave(this, transportState);
         this.master.onSlaveReleased(this);
      }

      this.clearTransport();
      this.master = null;
      this.resetSale();
   }

   public void free(boolean leadState) {
      this.free(true, leadState);
   }

   public void free() {
      this.free(true, true);
   }

   public void checkStillSlave() {
      EntityInvisibleSlaveTransporter transport = this.getTransport();
      if (transport != null && !transport.func_110167_bD()) {
         this.free();
      } else if (this.isSlave() && !this.isTiedUp()) {
         this.free();
      } else {
         Entity holder;
         if (this.master != null && transport != null) {
            holder = transport.func_110166_bE();
            if (holder != null && (holder instanceof EntityPlayer || holder instanceof I_Kidnapper)) {
               Entity entityMaster = this.master.getEntity();
               if (entityMaster != null && !holder.equals(entityMaster)) {
                  this.free();
                  return;
               }
            }
         }

         if (this.master == null && transport != null) {
            holder = transport.func_110166_bE();
            if (holder != null && (holder instanceof EntityPlayer || holder instanceof I_Kidnapper)) {
               this.free();
               return;
            }
         }

      }
   }

   private void clearTransport() {
      EntityInvisibleSlaveTransporter transport = this.getTransport();
      if (transport != null) {
         this.player.func_110145_l(transport);
         transport.func_70106_y();
      }

   }

   public boolean isEnslavable() {
      return !this.isLeashed();
   }

   public void transferSlaveryTo(I_Kidnapper newMaster) {
      if (this.master == null || this.master.allowSlaveTransfer()) {
         if (this.master != null) {
            this.master.removeSlave(this);
         }

         newMaster.addSlave(this);
         this.master = newMaster;
      }
   }

   public EntityInvisibleSlaveTransporter getTransport() {
      if (this.player == null) {
         return null;
      } else {
         Entity ridding = this.player.func_184187_bx();
         return this.player.func_184218_aH() && ridding != null && ridding instanceof EntityInvisibleSlaveTransporter ? (EntityInvisibleSlaveTransporter)ridding : null;
      }
   }

   public I_Kidnapper getMaster() {
      return this.master;
   }

   public boolean isSlave() {
      return this.isLeashed();
   }

   public PlayerKidnapperManager getSlaveHolderManager() {
      return this.slaveHolderManager;
   }

   public boolean isTiedToPole() {
      EntityInvisibleSlaveTransporter transport = this.getTransport();
      return transport != null ? transport.func_110166_bE() instanceof EntityLeashKnot : false;
   }

   public void untieSlaveFromPole() {
      this.master.getBackSlaveFromPole(this);
   }

   public synchronized boolean isGagged() {
      return ExtraBondageItemHelper.isTypeEquiped(this.player, ExtraBondageItemType.GAG);
   }

   public synchronized boolean isTiedUp() {
      return ExtraBondageItemHelper.isTypeEquiped(this.player, ExtraBondageItemType.BIND);
   }

   public synchronized boolean isBlindfolded() {
      return ExtraBondageItemHelper.isTypeEquiped(this.player, ExtraBondageItemType.BLINDFOLD);
   }

   public synchronized boolean hasEarplugs() {
      return ExtraBondageItemHelper.isTypeEquiped(this.player, ExtraBondageItemType.EARPLUGS);
   }

   public synchronized boolean hasClothes() {
      return ExtraBondageItemHelper.isTypeEquiped(this.player, ExtraBondageItemType.CLOTHES);
   }

   public synchronized boolean hasCollar() {
      return ExtraBondageItemHelper.isTypeEquiped(this.player, ExtraBondageItemType.COLLAR);
   }

   public boolean hasLockedCollar() {
      ItemStack collar = this.getCurrentCollar();
      if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
         ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
         return itemCollar.isLocked(collar);
      } else {
         return false;
      }
   }

   public boolean hasNamedCollar() {
      ItemStack collar = this.getCurrentCollar();
      if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
         ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
         return itemCollar.hasNickname(collar);
      } else {
         return false;
      }
   }

   private synchronized void putElementOn(ExtraBondageItemType type, ItemStack stack) {
      ItemStack stackToPut = Utils.extractValidStack(stack);
      if (stackToPut != null) {
         this.replaceSlot(type, stackToPut);
      }

   }

   public synchronized void putGagOn(ItemStack gag) {
      if (!this.isGagged() && gag != null && gag.func_77973_b() instanceof ItemGag) {
         this.putElementOn(ExtraBondageItemType.GAG, gag);
      }
   }

   public synchronized ItemStack takeGagOff() {
      if (!this.isGagged()) {
         return null;
      } else {
         ItemStack stackGag = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.GAG);
         if (stackGag == null) {
            return null;
         } else {
            Item head = stackGag.func_77973_b();
            if (!(head instanceof ItemGag)) {
               return null;
            } else {
               ItemStack stackReturned = stackGag.func_77946_l();
               ExtraBondageItemHelper.setStackInSlot(this.player, ExtraBondageItemType.GAG, new ItemStack(Items.field_190931_a, 1));
               return stackReturned;
            }
         }
      }
   }

   public synchronized void putBindOn(ItemStack bind) {
      if (!this.isTiedUp() && bind != null && bind.func_77973_b() instanceof ItemBind) {
         this.putElementOn(ExtraBondageItemType.BIND, bind);
      }
   }

   public synchronized ItemStack takeBindOff() {
      if (!this.isTiedUp()) {
         return null;
      } else {
         ItemStack stackBind = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.BIND);
         if (stackBind == null) {
            return null;
         } else {
            Item bind = stackBind.func_77973_b();
            if (!(bind instanceof ItemBind)) {
               return null;
            } else {
               ItemBind itemBind = (ItemBind)bind;
               ItemStack stackReturned = stackBind.func_77946_l();
               itemBind.resetCurrentResistance(stackReturned);
               ExtraBondageItemHelper.setStackInSlot(this.player, ExtraBondageItemType.BIND, new ItemStack(Items.field_190931_a, 1));
               return stackReturned;
            }
         }
      }
   }

   public synchronized void putBlindfoldOn(ItemStack blindfold) {
      if (!this.isBlindfolded() && blindfold != null && blindfold.func_77973_b() instanceof ItemBlindfold) {
         this.putElementOn(ExtraBondageItemType.BLINDFOLD, blindfold);
      }
   }

   public synchronized ItemStack takesBlindfoldOff() {
      if (!this.isBlindfolded()) {
         return null;
      } else {
         ItemStack stackBlindfold = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.BLINDFOLD);
         if (stackBlindfold == null) {
            return null;
         } else {
            Item blindfoldItem = stackBlindfold.func_77973_b();
            if (!(blindfoldItem instanceof ItemBlindfold)) {
               return null;
            } else {
               ItemStack stackReturned = stackBlindfold.func_77946_l();
               ExtraBondageItemHelper.setStackInSlot(this.player, ExtraBondageItemType.BLINDFOLD, new ItemStack(Items.field_190931_a, 1));
               return stackReturned;
            }
         }
      }
   }

   public synchronized void putEarsPlugsOn(ItemStack earplugs) {
      if (!this.hasEarplugs() && earplugs != null && earplugs.func_77973_b() instanceof ItemEarplugs) {
         this.putElementOn(ExtraBondageItemType.EARPLUGS, earplugs);
      }
   }

   public synchronized ItemStack takesEarplugsOff() {
      if (!this.hasEarplugs()) {
         return null;
      } else {
         ItemStack stackEarplugs = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.EARPLUGS);
         if (stackEarplugs == null) {
            return null;
         } else {
            Item earplugsItem = stackEarplugs.func_77973_b();
            if (!(earplugsItem instanceof ItemEarplugs)) {
               return null;
            } else {
               ItemStack stackReturned = stackEarplugs.func_77946_l();
               ExtraBondageItemHelper.setStackInSlot(this.player, ExtraBondageItemType.EARPLUGS, new ItemStack(Items.field_190931_a, 1));
               return stackReturned;
            }
         }
      }
   }

   public synchronized void putClothesOn(ItemStack clothes) {
      if (!this.hasClothes() && clothes != null && clothes.func_77973_b() instanceof ItemClothes) {
         this.putElementOn(ExtraBondageItemType.CLOTHES, clothes);
      }
   }

   public synchronized ItemStack takesClothesOff() {
      if (!this.hasClothes()) {
         return null;
      } else {
         ItemStack stackClothes = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.CLOTHES);
         if (stackClothes == null) {
            return null;
         } else {
            Item clothesItem = stackClothes.func_77973_b();
            if (!(clothesItem instanceof ItemClothes)) {
               return null;
            } else {
               ItemStack stackReturned = stackClothes.func_77946_l();
               ExtraBondageItemHelper.setStackInSlot(this.player, ExtraBondageItemType.CLOTHES, new ItemStack(Items.field_190931_a, 1));
               return stackReturned;
            }
         }
      }
   }

   public synchronized void putCollarOn(ItemStack collar) {
      if (!this.hasCollar() && collar != null && collar.func_77973_b() instanceof ItemCollar) {
         this.putElementOn(ExtraBondageItemType.COLLAR, collar);
      }
   }

   public synchronized ItemStack takesCollarOff() {
      return this.takesCollarOff(false);
   }

   public synchronized ItemStack takesCollarOff(boolean force) {
      if (!this.hasCollar()) {
         return null;
      } else {
         ItemStack stackCollar = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.COLLAR);
         if (stackCollar == null) {
            return null;
         } else {
            Item collarItem = stackCollar.func_77973_b();
            if (!(collarItem instanceof ItemCollar)) {
               return null;
            } else {
               ItemCollar collar = (ItemCollar)collarItem;
               if (collar.isLocked(stackCollar) && !force) {
                  return null;
               } else {
                  ItemStack stackReturned = stackCollar.func_77946_l();
                  collar.unlockCollar(stackReturned);
                  ExtraBondageItemHelper.setStackInSlot(this.player, ExtraBondageItemType.COLLAR, new ItemStack(Items.field_190931_a, 1));
                  return stackReturned;
               }
            }
         }
      }
   }

   public synchronized void restrain(ItemStack bind, ItemStack gag) {
      this.putGagOn(gag);
      this.putBindOn(bind);
   }

   public void untie() {
      this.takesBlindfoldOff();
      this.takeGagOff();
      this.takeBindOff();
      this.takesEarplugsOff();
      if (this.hasCollar() && !this.hasLockedCollar()) {
         this.takesCollarOff();
      }

      this.resetSale();
   }

   public synchronized boolean isForSell() {
      return this.forSell;
   }

   public boolean isOnline() {
      return this.online;
   }

   public void setOnline(boolean online) {
      this.online = online;
   }

   public synchronized boolean isBoundAndGagged() {
      return this.isTiedUp() && this.isGagged();
   }

   public void replaceSlot(ExtraBondageItemType slot, ItemStack newStack, boolean shouldDrop) {
      ItemStack stack = ExtraBondageItemHelper.getItemStackFromSlot(this.player, slot);
      Item itemInSlot = stack.func_77973_b();
      ExtraBondageItemHelper.setStackInSlot(this.player, slot, newStack);
      if (shouldDrop && !(itemInSlot instanceof ItemAir) && !(itemInSlot instanceof IExtraBondageItem)) {
         this.player.func_71019_a(stack, true);
      }

   }

   public void replaceSlot(ExtraBondageItemType slot, ItemStack newStack) {
      this.replaceSlot(slot, newStack, false);
   }

   public ItemStack clearSlot(ExtraBondageItemType slot) {
      ItemStack stack = ExtraBondageItemHelper.getItemStackFromSlot(this.player, slot);
      ExtraBondageItemHelper.setStackInSlot(this.player, slot, new ItemStack(Items.field_190931_a));
      return stack;
   }

   public synchronized TyingTask getCurrentTyingTask() {
      return this.currentTyingTask;
   }

   public synchronized void setCurrentTyingTask(TyingTask currentTyingTask) {
      this.currentTyingTask = currentTyingTask;
   }

   public synchronized UntyingTask getCurrentUntyingTask() {
      return this.currentUntyingTask;
   }

   public synchronized void setCurrentUntyingTask(UntyingTask currentUntyingTask) {
      this.currentUntyingTask = currentUntyingTask;
   }

   public PlayerStateTask getClientUntyingTask() {
      return this.clientUntyingTask;
   }

   public void setClientUntyingTask(PlayerStateTask clientUntyingTask) {
      this.clientUntyingTask = clientUntyingTask;
   }

   public PlayerStateTask getRestrainedState() {
      return this.restrainedState;
   }

   public void setRestrainedState(PlayerStateTask restrainedState) {
      this.restrainedState = restrainedState;
   }

   public void dropKnives() {
      if (this.player != null && this.player.field_71071_by != null) {
         InventoryPlayer inv = this.player.field_71071_by;

         ItemStack offHand;
         for(int j = 0; j < inv.func_70302_i_(); ++j) {
            offHand = inv.func_70301_a(j);
            if (this.dropKnife(offHand)) {
               inv.func_70299_a(j, ItemStack.field_190927_a);
            }
         }

         ItemStack mainHand = this.player.func_184614_ca();
         if (this.dropKnife(mainHand)) {
            this.player.func_184611_a(EnumHand.MAIN_HAND, ItemStack.field_190927_a);
         }

         offHand = this.player.func_184592_cb();
         if (this.dropKnife(offHand)) {
            this.player.func_184611_a(EnumHand.OFF_HAND, ItemStack.field_190927_a);
         }
      }

   }

   public boolean hasKnives() {
      if (this.player != null && this.player.field_71071_by != null) {
         InventoryPlayer inv = this.player.field_71071_by;

         ItemStack offHand;
         for(int j = 0; j < inv.func_70302_i_(); ++j) {
            offHand = inv.func_70301_a(j);
            if (this.isKnivesStack(offHand)) {
               return true;
            }
         }

         ItemStack mainHand = this.player.func_184614_ca();
         if (this.isKnivesStack(mainHand)) {
            return true;
         }

         offHand = this.player.func_184592_cb();
         if (this.isKnivesStack(offHand)) {
            return true;
         }
      }

      return false;
   }

   private boolean dropKnife(ItemStack stack) {
      if (this.isKnivesStack(stack)) {
         ItemStack copyDrop = stack.func_77946_l();
         this.player.func_71019_a(copyDrop, true);
         return true;
      } else {
         return false;
      }
   }

   private boolean isKnivesStack(ItemStack stack) {
      return stack != null && !stack.func_190926_b() && stack.func_77973_b() instanceof ItemKnife;
   }

   public void reset() {
      if (this.master != null) {
         this.master.removeSlave(this);
         this.master = null;
      }

      this.resetSale();
      this.free();
   }

   public int getFoodLevelWatcher() {
      return this.foodLevelWatcher;
   }

   public void setFoodLevelWatcher(int foodLevelWatcher) {
      if (foodLevelWatcher < 8) {
         this.foodLevelWatcher = foodLevelWatcher;
      } else {
         this.foodLevelWatcher = 8;
      }

   }

   public void struggle() {
      if (this.struggleBindState != null) {
         this.struggleBindState.struggle(this);
      }

      if (this.struggleCollarState != null) {
         this.struggleCollarState.struggle(this);
      }

   }

   public void tighten(EntityPlayer tightener) {
      if (this.struggleBindState != null) {
         this.struggleBindState.tighten(tightener, this);
      }

      if (this.struggleCollarState != null) {
         this.struggleCollarState.tighten(tightener, this);
      }

   }

   public boolean isMasterClose() {
      if (this.isSlave() && this.master != null) {
         EntityLivingBase masterEntity = this.master.getEntity();
         if (this.player != null) {
            float distance = this.player.func_70032_d(masterEntity);
            if (distance <= 30.0F) {
               return true;
            }
         }
      }

      return false;
   }

   public ItemStack getCurrentBind() {
      ItemStack stackBind = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.BIND);
      if (stackBind == null) {
         return null;
      } else {
         Item chest = stackBind.func_77973_b();
         return chest instanceof ItemBind ? stackBind.func_77946_l() : null;
      }
   }

   public synchronized int getCurrentBindResistance() {
      ItemStack stackBind = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.BIND);
      if (stackBind == null) {
         return 0;
      } else {
         Item chest = stackBind.func_77973_b();
         if (chest instanceof ItemBind) {
            ItemBind itemBind = (ItemBind)chest;
            EntityPlayer player = this.getPlayer();
            if (player != null && player.field_70170_p != null) {
               return itemBind.getCurrentResistance(stackBind, player.field_70170_p);
            }
         }

         return 0;
      }
   }

   public synchronized void setCurrentBindResistance(int resistance) {
      ItemStack stackBind = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.BIND);
      if (stackBind != null) {
         Item chest = stackBind.func_77973_b();
         if (chest instanceof ItemBind) {
            ItemBind itemBind = (ItemBind)chest;
            itemBind.setCurrentResistance(stackBind, resistance);
         }

      }
   }

   public ItemStack getCurrentGag() {
      ItemStack stackGag = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.GAG);
      if (stackGag == null) {
         return null;
      } else {
         Item gag = stackGag.func_77973_b();
         return gag instanceof ItemGag ? stackGag.func_77946_l() : null;
      }
   }

   public ItemStack getCurrentBlindfold() {
      ItemStack stackBlindfold = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.BLINDFOLD);
      if (stackBlindfold == null) {
         return null;
      } else {
         Item blindfold = stackBlindfold.func_77973_b();
         return blindfold instanceof ItemBlindfold ? stackBlindfold.func_77946_l() : null;
      }
   }

   public ItemStack getCurrentEarplugs() {
      ItemStack stackEarplugs = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.EARPLUGS);
      if (stackEarplugs == null) {
         return null;
      } else {
         Item earplugs = stackEarplugs.func_77973_b();
         return earplugs instanceof ItemEarplugs ? stackEarplugs.func_77946_l() : null;
      }
   }

   public ItemStack getCurrentCollar() {
      ItemStack stackCollar = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.COLLAR);
      if (stackCollar == null) {
         return null;
      } else {
         Item collar = stackCollar.func_77973_b();
         return collar instanceof ItemCollar ? stackCollar : null;
      }
   }

   public synchronized int getCurrentCollarResistance() {
      ItemStack stackCollar = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.COLLAR);
      if (stackCollar == null) {
         return 0;
      } else {
         Item collar = stackCollar.func_77973_b();
         if (collar instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar;
            EntityPlayer player = this.getPlayer();
            if (player != null && player.field_70170_p != null) {
               return itemCollar.getCurrentResistance(stackCollar, player.field_70170_p);
            }
         }

         return 0;
      }
   }

   public synchronized void setCurrentCollarResistance(int resistance) {
      ItemStack stackCollar = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.COLLAR);
      if (stackCollar != null) {
         Item collar = stackCollar.func_77973_b();
         if (collar instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar;
            itemCollar.setCurrentResistance(stackCollar, resistance);
         }

      }
   }

   public ItemStack getCurrentClothes() {
      ItemStack stackClothes = ExtraBondageItemHelper.getItemStackFromSlot(this.player, ExtraBondageItemType.CLOTHES);
      if (stackClothes == null) {
         return null;
      } else {
         Item clothes = stackClothes.func_77973_b();
         return clothes instanceof ItemClothes ? stackClothes : null;
      }
   }

   public TimedTask getTrapPlaceTask() {
      return this.trapPlaceTask;
   }

   public void setTrapPlaceTask(TimedTask trapPlaceTask) {
      this.trapPlaceTask = trapPlaceTask;
   }

   public boolean isLeashed() {
      Entity ridding = this.player.func_184187_bx();
      return this.player.func_184218_aH() && ridding != null && ridding instanceof EntityInvisibleSlaveTransporter;
   }

   public PlayerStateTask getClientTyingTask() {
      return this.clientTyingTask;
   }

   public void setClientTyingTask(PlayerStateTask clientTyingTask) {
      this.clientTyingTask = clientTyingTask;
   }

   public PlayerStateTask getClientTrapPlaceTask() {
      return this.clientTrapPlaceTask;
   }

   public void setClientTrapPlaceTask(PlayerStateTask clientTrapPlaceTask) {
      this.clientTrapPlaceTask = clientTrapPlaceTask;
   }

   public ItemStack replaceBind(ItemStack newbind) {
      if (!this.isTiedUp()) {
         return null;
      } else {
         ItemStack extractedBind = Utils.extractValidStack(newbind);
         if (extractedBind != null && extractedBind.func_77973_b() instanceof ItemBind) {
            ItemStack current = this.getCurrentBind();
            if (current != null && current.func_77973_b() instanceof ItemBind) {
               if (ItemStack.func_77989_b(current, extractedBind)) {
                  return null;
               } else {
                  ItemBind itemBind = (ItemBind)current.func_77973_b();
                  ItemStack bindReplacement = extractedBind.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  itemBind.resetCurrentResistance(stackReturned);
                  this.replaceSlot(ExtraBondageItemType.BIND, bindReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public ItemStack replaceGag(ItemStack newGag, boolean force) {
      if (this.isBoundAndGagged() || this.isGagged() && force) {
         ItemStack extractedGag = Utils.extractValidStack(newGag);
         if (extractedGag != null && extractedGag.func_77973_b() instanceof ItemGag) {
            ItemStack current = this.getCurrentGag();
            if (current != null && current.func_77973_b() instanceof ItemGag) {
               if (ItemStack.func_77989_b(current, extractedGag)) {
                  return null;
               } else {
                  ItemStack gagReplacement = extractedGag.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.replaceSlot(ExtraBondageItemType.GAG, gagReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceGag(ItemStack newGag) {
      return this.replaceGag(newGag, false);
   }

   public ItemStack replaceBlindfold(ItemStack newBlindfold, boolean force) {
      if ((this.isTiedUp() || force) && this.isBlindfolded()) {
         ItemStack extractedBlindfold = Utils.extractValidStack(newBlindfold);
         if (extractedBlindfold != null && extractedBlindfold.func_77973_b() instanceof ItemBlindfold) {
            ItemStack current = this.getCurrentBlindfold();
            if (current != null && current.func_77973_b() instanceof ItemBlindfold) {
               if (ItemStack.func_77989_b(current, extractedBlindfold)) {
                  return null;
               } else {
                  ItemStack blindfoldReplacement = extractedBlindfold.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.replaceSlot(ExtraBondageItemType.BLINDFOLD, blindfoldReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceBlindfold(ItemStack newBlindfold) {
      return this.replaceBlindfold(newBlindfold, false);
   }

   public ItemStack replaceEarPlugs(ItemStack newEarplugs, boolean force) {
      if ((this.isTiedUp() || force) && this.hasEarplugs()) {
         ItemStack extractedEarplugs = Utils.extractValidStack(newEarplugs);
         if (extractedEarplugs != null && extractedEarplugs.func_77973_b() instanceof ItemEarplugs) {
            ItemStack current = this.getCurrentEarplugs();
            if (current != null && current.func_77973_b() instanceof ItemEarplugs) {
               if (ItemStack.func_77989_b(current, extractedEarplugs)) {
                  return null;
               } else {
                  ItemStack earplugsReplacement = extractedEarplugs.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.replaceSlot(ExtraBondageItemType.EARPLUGS, earplugsReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceEarPlugs(ItemStack newEarplugs) {
      return this.replaceEarPlugs(newEarplugs, false);
   }

   public ItemStack replaceCollar(ItemStack newCollar, boolean force) {
      if ((this.isTiedUp() || force) && this.hasCollar()) {
         ItemStack extractedCollar = Utils.extractValidStack(newCollar);
         ItemStack current = this.getCurrentCollar();
         if (this.hasLockedCollar()) {
            return null;
         } else if (ItemStack.func_77989_b(current, extractedCollar)) {
            return null;
         } else {
            ItemStack oldStackCollar = current.func_77946_l();
            if (oldStackCollar != null && oldStackCollar.func_77973_b() instanceof ItemCollar) {
               ItemCollar collarItem = (ItemCollar)oldStackCollar.func_77973_b();
               collarItem.resetCurrentResistance(oldStackCollar);
            }

            this.replaceSlot(ExtraBondageItemType.COLLAR, extractedCollar.func_77946_l());
            return oldStackCollar;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceCollar(ItemStack newCollar) {
      return this.replaceCollar(newCollar, false);
   }

   public ItemStack replaceClothes(ItemStack newClothes, boolean force) {
      if ((this.isTiedUp() || force) && this.hasClothes()) {
         ItemStack extractedClothes = Utils.extractValidStack(newClothes);
         if (extractedClothes != null && extractedClothes.func_77973_b() instanceof ItemClothes) {
            ItemStack current = this.getCurrentClothes();
            if (current != null && current.func_77973_b() instanceof ItemClothes) {
               if (ItemStack.func_77989_b(current, extractedClothes)) {
                  return null;
               } else {
                  ItemStack clothesReplacement = extractedClothes.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.replaceSlot(ExtraBondageItemType.CLOTHES, clothesReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceClothes(ItemStack newClothes) {
      return this.replaceClothes(newClothes, false);
   }

   public boolean equals(Object obj) {
      if (obj instanceof PlayerBindState) {
         PlayerBindState state = (PlayerBindState)obj;
         EntityPlayer thisPlayer = this.player;
         EntityPlayer statePlayer = state.player;
         if (thisPlayer != null && statePlayer != null) {
            return thisPlayer.func_110124_au().equals(statePlayer.func_110124_au());
         }
      }

      return super.equals(obj);
   }

   public Timer getTimerNoRp() {
      return this.timerNoRp;
   }

   public void setTimerNoRp(Timer timerNoRp) {
      this.timerNoRp = timerNoRp;
   }

   public Timer getTimerCallBackSlave() {
      return this.timerCallBackSlave;
   }

   public void setTimerCallBackSlave(Timer timerBringsSlavesHome) {
      this.timerCallBackSlave = timerBringsSlavesHome;
   }

   public void dropBondageItems() {
      this.dropBondageItems(true, true, true, true, true);
   }

   public void dropBondageItems(boolean includeBind) {
      this.dropBondageItems(includeBind, true, true, true, true);
   }

   public void dropBondageItems(boolean includeBind, boolean includeGag, boolean includeBlindfold, boolean includeEarplugs, boolean includeCollar) {
      if (this.player != null) {
         ItemStack gag = this.getCurrentGag();
         ItemStack bind = this.getCurrentBind();
         ItemStack blindfold = this.getCurrentBlindfold();
         ItemStack earplugs = this.getCurrentEarplugs();
         ItemStack collarStack = this.getCurrentCollar();
         if (this.hasLockedCollar()) {
            collarStack = null;
         }

         if (this.player != null) {
            if (includeGag && gag != null) {
               this.player.func_71019_a(gag, true);
            }

            if (includeBind && bind != null) {
               if (bind.func_77973_b() instanceof ItemBind) {
                  ItemBind bindItem = (ItemBind)bind.func_77973_b();
                  bindItem.resetCurrentResistance(bind);
               }

               this.player.func_71019_a(bind, true);
            }

            if (includeBlindfold && blindfold != null) {
               this.player.func_71019_a(blindfold, true);
            }

            if (includeEarplugs && earplugs != null) {
               this.player.func_71019_a(earplugs, true);
            }

            if (includeCollar && collarStack != null) {
               if (collarStack.func_77973_b() instanceof ItemCollar) {
                  ItemCollar collarItem = (ItemCollar)collarStack.func_77973_b();
                  collarItem.resetCurrentResistance(collarStack);
               }

               this.player.func_71019_a(collarStack, true);
            }
         }
      }

   }

   public void dropClothes() {
      if (this.hasClothes()) {
         ItemStack clothes = this.takesClothesOff();
         if (clothes != null) {
            this.kidnappedDropItem(clothes);
         }
      }

   }

   public boolean hasGaggingEffect() {
      if (this.player == null) {
         return false;
      } else {
         return this.isGagged() || ExtraBondageItemHelper.hasGaggingEffect(this.player);
      }
   }

   public boolean hasBlindingEffect() {
      if (this.player == null) {
         return false;
      } else {
         return this.isBlindfolded() || ExtraBondageItemHelper.hasBlindingEffect(this.player);
      }
   }

   public void checkAutoShockCollar() {
      synchronized(this.lockTimerAutoShock) {
         if (this.player != null) {
            ItemStack collar = this.getCurrentCollar();
            if (collar != null && collar.func_77973_b() instanceof ItemShockCollarAuto) {
               ItemShockCollarAuto collarShock = (ItemShockCollarAuto)collar.func_77973_b();
               if (this.timerAutoShockCollar != null && this.timerAutoShockCollar.getSecondsRemaining() <= 0) {
                  this.shockKidnapped();
               }

               if (this.timerAutoShockCollar == null || this.timerAutoShockCollar.getSecondsRemaining() <= 0) {
                  this.timerAutoShockCollar = new Timer(collarShock.getInterval());
               }
            }
         }

      }
   }

   public void resetAutoShockTimer() {
      synchronized(this.lockTimerAutoShock) {
         this.timerAutoShockCollar = null;
      }
   }

   public void takeBondageItemBy(PlayerBindState playerStateIn, int identifier) {
      if (this.player != null && playerStateIn != null && !playerStateIn.isTiedUp() && this.isTiedUp()) {
         ExtraBondageItemIdentifier item = ExtraBondageItemIdentifier.getIdentifier(identifier);
         ItemStack clothes;
         if (item == ExtraBondageItemIdentifier.GAG) {
            if (this.isGagged()) {
               clothes = this.takeGagOff();
               if (clothes != null) {
                  this.player.func_71019_a(clothes, true);
               }
            }
         } else if (item == ExtraBondageItemIdentifier.BLINFOLD) {
            if (this.isBlindfolded()) {
               clothes = this.takesBlindfoldOff();
               if (clothes != null) {
                  this.player.func_71019_a(clothes, true);
               }
            }
         } else if (item == ExtraBondageItemIdentifier.EARPLUGS) {
            if (this.hasEarplugs()) {
               clothes = this.takesEarplugsOff();
               if (clothes != null) {
                  this.player.func_71019_a(clothes, true);
               }
            }
         } else if (item == ExtraBondageItemIdentifier.COLLAR) {
            if (this.hasCollar() && !this.hasLockedCollar()) {
               clothes = this.getCurrentCollar();
               if (clothes != null && clothes.func_77973_b() instanceof ItemCollar) {
                  ItemStack collarBuf = this.takesCollarOff();
                  this.player.func_71019_a(collarBuf, true);
               }
            }
         } else if (item == ExtraBondageItemIdentifier.CLOTHES) {
            if (this.hasClothes() && this.canTakesOffClothes(this.player)) {
               clothes = this.takesClothesOff();
               if (clothes != null) {
                  this.player.func_71019_a(clothes, true);
               }
            }
         } else if (item == ExtraBondageItemIdentifier.KNIVES) {
            this.dropKnives();
         }

      }
   }

   public boolean canInteractWith(I_Kidnapped targetIn) {
      if (this.player != null && this.player.field_70170_p != null && targetIn != null && !this.equals(targetIn)) {
         List<I_Kidnapped> entitesAround = Utils.getKidnapableEntitiesAround(this.player.field_70170_p, this.player.func_180425_c(), 5.0D);
         Iterator var3 = entitesAround.iterator();

         I_Kidnapped playerAround;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            playerAround = (I_Kidnapped)var3.next();
         } while(!playerAround.equals(targetIn));

         return true;
      } else {
         return false;
      }
   }

   public int getBondageItemsWhichCanBeRemovedCount() {
      int count = 0;
      if (this.isGagged()) {
         ++count;
      }

      if (this.isBlindfolded()) {
         ++count;
      }

      if (this.hasEarplugs()) {
         ++count;
      }

      if (this.hasCollar() && !this.hasLockedCollar()) {
         ++count;
      }

      return count;
   }

   public void applyBondage(ItemStack bind, ItemStack gag, ItemStack blindfold, ItemStack earplugs, ItemStack collar, ItemStack clothes) {
      this.dropBondageItems(bind != null, gag != null, blindfold != null, earplugs != null, collar != null);
      if (bind != null) {
         if (this.isTiedUp()) {
            this.replaceBind(bind);
         } else {
            this.putBindOn(bind);
         }
      }

      if (gag != null) {
         if (this.isGagged()) {
            this.replaceGag(gag, true);
         } else {
            this.putGagOn(gag);
         }
      }

      if (blindfold != null) {
         if (this.isBlindfolded()) {
            this.replaceBlindfold(blindfold, true);
         } else {
            this.putBlindfoldOn(blindfold);
         }
      }

      if (earplugs != null) {
         if (this.hasEarplugs()) {
            this.replaceEarPlugs(earplugs, true);
         } else {
            this.putEarsPlugsOn(earplugs);
         }
      }

      if (collar != null) {
         if (this.hasCollar()) {
            if (!this.hasLockedCollar()) {
               this.replaceCollar(collar, true);
            }
         } else {
            this.putCollarOn(collar);
         }
      }

      if (clothes != null && !this.hasClothes() && this.canChangeClothes()) {
         this.putClothesOn(clothes);
      }

   }

   public void applyChloroform(int duration) {
      if (this.player != null) {
         Potion e1 = Potion.func_188412_a(2);
         Potion e2 = Potion.func_188412_a(4);
         Potion e3 = Potion.func_188412_a(15);
         Potion e4 = Potion.func_188412_a(8);
         int tickDuration = duration * 20;
         this.player.func_70690_d(new PotionEffect(e1, tickDuration, 127));
         this.player.func_70690_d(new PotionEffect(e2, tickDuration, 127));
         this.player.func_70690_d(new PotionEffect(e3, tickDuration, 127));
         this.player.func_70690_d(new PotionEffect(e4, tickDuration, 150));
      }

   }

   public void forceRemoveAutoShockCollar() {
      if (this.player != null) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemShockCollarAuto) {
            this.clearSlot(ExtraBondageItemType.COLLAR);
         }
      }

   }

   public String getNameFromCollar() {
      if (this.hasNamedCollar()) {
         ItemStack collarStack = this.getCurrentCollar();
         if (collarStack.func_77973_b() instanceof ItemCollar) {
            ItemCollar collar = (ItemCollar)collarStack.func_77973_b();
            String name = collar.getNickname(collarStack);
            if (name != null) {
               return name;
            }
         }
      }

      return null;
   }

   public Timer getTimerWarnOpNoRp() {
      return this.timerWarnOpNoRp;
   }

   public void setTimerWarnOpNoRp(Timer timerWarnOpNoRp) {
      this.timerWarnOpNoRp = timerWarnOpNoRp;
   }

   public void putSlaveForSell(ItemTask sellTask) {
      if (sellTask != null) {
         this.forSell = true;
         this.kidnapperSellTask = sellTask;
      }

   }

   public void resetSale() {
      this.forSell = false;
      this.kidnapperSellTask = null;
   }

   public ItemTask getKidnapperSellTask() {
      return this.kidnapperSellTask;
   }

   public boolean isDeathState() {
      return this.deathState;
   }

   public void setDeathState(boolean deathState) {
      this.deathState = deathState;
   }

   public UUID getKidnappedUniqueId() {
      return this.player != null ? this.player.func_110124_au() : null;
   }

   public void kidnappedDropItem(ItemStack stack) {
      if (this.player != null) {
         this.player.func_71019_a(stack, true);
      }

   }

   public boolean canBeKidnappedByEvents() {
      if (this.player != null) {
         return !this.player.func_184812_l_();
      } else {
         return true;
      }
   }

   public void shockKidnapped() {
      if (this.player != null) {
         DamageSource source = DamageSource.field_76377_j;
         if (this.player.field_70170_p != null && !this.player.field_70170_p.field_72995_K) {
            this.player.field_70170_p.func_184133_a((EntityPlayer)null, this.player.func_180425_c(), KidnapModSoundEvents.ELECTRIC_SHOCK, SoundCategory.AMBIENT, 1.0F, 1.0F);
         }

         Utils.sendInfoMessageToEntity(this.player, "You've been shocked!");
         this.player.func_70097_a(source, 3.0F);
      }

   }

   public String getKidnappedName() {
      return this.player != null ? this.player.func_70005_c_() : null;
   }

   public boolean canBeTiedUp() {
      return !this.isTiedUp();
   }

   public boolean onDeathKidnapped() {
      this.setDeathState(true);
      this.reset();
      this.forceRemoveAutoShockCollar();
      return false;
   }

   public void tieToClosestPole() {
      if (this.isTiedUp() && !this.isSlave()) {
         EntityPlayer player = this.getPlayer();
         if (player != null && player.field_70170_p != null) {
            BlockPos fencePose = Utils.getPositionOfTheNearestBlockAroundEntity(player, BlockFence.class, 4);
            if (fencePose != null) {
               List<EntityLeashKnot> knots = player.field_70170_p.func_72872_a(EntityLeashKnot.class, new AxisAlignedBB(fencePose));
               EntityLeashKnot knot = null;
               if (knots != null && !knots.isEmpty()) {
                  knot = (EntityLeashKnot)knots.get(0);
               } else {
                  knot = new EntityLeashKnot(player.field_70170_p);
                  knot.func_70012_b((double)fencePose.func_177958_n(), (double)fencePose.func_177956_o(), (double)fencePose.func_177952_p(), 0.0F, 0.0F);
                  player.field_70170_p.func_72838_d(knot);
               }

               this.setUpTransport();
               EntityInvisibleSlaveTransporter transport = this.getTransport();
               if (transport != null) {
                  transport.func_110162_b(knot, true);
               }
            }
         }
      }

   }

   public float getLoadedNpcAdjustGag() {
      return this.loadedNpcAdjustGag;
   }

   public void setLoadedNpcAdjustGag(float loadedNpcAdjustGag) {
      this.loadedNpcAdjustGag = loadedNpcAdjustGag;
   }

   public float getLoadedNpcAdjustBlindfold() {
      return this.loadedNpcAdjustBlindfold;
   }

   public void setLoadedNpcAdjustBlindfold(float loadedNpcAdjustBlindfold) {
      this.loadedNpcAdjustBlindfold = loadedNpcAdjustBlindfold;
   }

   public synchronized Timer getTimerAdjustGagNpc() {
      return this.timerAdjustGagNpc;
   }

   public synchronized void setTimerAdjustGagNpc(Timer timerAdjustGagNpc) {
      this.timerAdjustGagNpc = timerAdjustGagNpc;
   }

   public synchronized Timer getTimerAdjustBlindfoldNpc() {
      return this.timerAdjustBlindfoldNpc;
   }

   public synchronized void setTimerAdjustBlindfoldNpc(Timer timerAdjustBlindfoldNpc) {
      this.timerAdjustBlindfoldNpc = timerAdjustBlindfoldNpc;
   }

   public synchronized Position getTeleportNpcPos() {
      return this.teleportNpcPosWarp;
   }

   public synchronized void setTeleportNpcPos(Position teleportNpcPos) {
      this.teleportNpcPosWarp = teleportNpcPos;
   }

   public synchronized Timer getTimerTeleportNpc() {
      return this.timerTeleportNpc;
   }

   public synchronized void setTimerTeleportNpc(Timer timerTeleportNpc) {
      this.timerTeleportNpc = timerTeleportNpc;
   }

   public synchronized Timer getTimerArmsNpc() {
      return this.timerArmsNpc;
   }

   public synchronized void setTimerArmsNpc(Timer timerArmsNpc) {
      this.timerArmsNpc = timerArmsNpc;
   }

   public synchronized Timer getTimerLayerNpc() {
      return this.timerLayerNpc;
   }

   public synchronized void setTimerLayerNpc(Timer timerLayerNpc) {
      this.timerLayerNpc = timerLayerNpc;
   }

   public EntityDamsel.DamselTelportPoints getLoadTeleportPointNpc() {
      return this.loadTeleportPointNpc;
   }

   public void setLoadTeleportPointNpc(EntityDamsel.DamselTelportPoints loadTeleportPointNpc) {
      this.loadTeleportPointNpc = loadTeleportPointNpc;
   }

   public boolean getLoadSmallArmsNpc() {
      return this.loadSmallArmsNpc;
   }

   public void setLoadSmallArmsNpc(boolean loadSmallArmsNpc) {
      this.loadSmallArmsNpc = loadSmallArmsNpc;
   }

   public EntityDamsel.LayersEnum getLoadedDamselLayer() {
      return this.loadedDamselLayer;
   }

   public void setLoadedDamselLayer(EntityDamsel.LayersEnum loadedDamselLayer) {
      this.loadedDamselLayer = loadedDamselLayer;
   }

   public boolean getLoadedDamselLayerState() {
      return this.loadedDamselLayerState;
   }

   public void setLoadedDamselLayerState(boolean loadedDamselLayerState) {
      this.loadedDamselLayerState = loadedDamselLayerState;
   }

   public void checkGagAfterApply() {
   }

   public void checkBlindfoldAfterApply() {
   }

   public void checkEarplugsAfterApply() {
   }

   public void checkCollarAfterApply() {
   }

   public void teleportToPosition(Position pos) {
      this.teleportToPosition(pos, true, true);
   }

   public void teleportToPosition(Position pos, boolean lead) {
      this.teleportToPosition(pos, true, lead);
   }

   public void teleportToPosition(Position pos, boolean transportState, boolean lead) {
      if (this.player != null) {
         boolean dropLeash = this.isLeashed() && lead;
         this.free(transportState, false);
         Entity newEntity = TeleportHelper.doTeleportEntity(this.player, pos);
         if (dropLeash && newEntity != null && newEntity instanceof EntityPlayer) {
            EntityPlayer newPlayer = (EntityPlayer)newEntity;
            newPlayer.func_71019_a(new ItemStack(Items.field_151058_ca, 1), true);
         }
      }

   }

   public boolean hasClothesWithSmallArms() {
      ItemStack clothes = this.getCurrentClothes();
      if (clothes != null && clothes.func_77973_b() instanceof ItemClothes) {
         ItemClothes itemClothes = (ItemClothes)clothes.func_77973_b();
         return itemClothes.shouldForceSmallArms(clothes);
      } else {
         return false;
      }
   }

   public boolean canTakesOffClothes(EntityPlayer player) {
      return this.hasClothes() && this.canChangeClothes(player);
   }

   public boolean canChangeClothes() {
      if (this.player != null) {
         KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)this.player.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
         if (cap != null) {
            return cap.isAllowingChangingClothes();
         }
      }

      return true;
   }

   public boolean canChangeClothes(EntityPlayer player) {
      return this.canChangeClothes();
   }
}
