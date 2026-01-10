package com.yuti.kidnapmod.entities;

import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.entities.ai.EntityAIEnslavingPlayer;
import com.yuti.kidnapmod.entities.ai.EntityAIGettingAwaySafeState;
import com.yuti.kidnapmod.entities.ai.EntityAILookForPlayer;
import com.yuti.kidnapmod.entities.ai.EntityAIMoveThroughObstacles;
import com.yuti.kidnapmod.entities.ai.EntityAISwimingTiedUp;
import com.yuti.kidnapmod.entities.ai.EntityAIWaitForBuyer;
import com.yuti.kidnapmod.entities.ai.EntityAIWaitForJobToBeCompleted;
import com.yuti.kidnapmod.entities.ai.EntityAIWanderExceptWhenTargeting;
import com.yuti.kidnapmod.entities.ai.EntityAIWanderFastGettingAway;
import com.yuti.kidnapmod.entities.ai.EntityAIWanderFastTimedForKidnapper;
import com.yuti.kidnapmod.entities.ai.EntityAIWatchClosestBlindfolded;
import com.yuti.kidnapmod.entities.ai.EntityAiBringSlaveToMasterPrison;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.items.ItemKeyCollar;
import com.yuti.kidnapmod.items.ItemShockCollarAuto;
import com.yuti.kidnapmod.loaders.ItemTask;
import com.yuti.kidnapmod.loaders.jobs.JobLoader;
import com.yuti.kidnapmod.loaders.sales.SaleLoader;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapper;
import com.yuti.kidnapmod.states.kidnapped.managers.PlayerKidnapperManager;
import com.yuti.kidnapmod.util.teleport.Position;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public abstract class EntityKidnapper extends EntityDamsel implements IAnimals, I_Kidnapper {
   private PlayerBindState slave;
   private EntityPlayer target;
   private boolean getOutState = false;
   private boolean allowTranfer = false;

   public EntityKidnapper(World world) {
      super(world);
   }

   public void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(60.0D);
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAISwimingTiedUp(this));
      this.field_70714_bg.func_75776_a(1, new EntityAILookForPlayer(this, 25));
      this.field_70714_bg.func_75776_a(3, new EntityAIEnslavingPlayer(this));
      this.field_70714_bg.func_75776_a(4, new EntityAiBringSlaveToMasterPrison(this, 1.0D, 5, 25));
      this.field_70714_bg.func_75776_a(5, new EntityAIWanderFastTimedForKidnapper(this, 1.2D, 120));
      this.field_70714_bg.func_75776_a(6, new EntityAIWanderFastGettingAway(this, 1.3D, 120));
      this.field_70714_bg.func_75776_a(7, new EntityAIGettingAwaySafeState(this, 1.3D, 120));
      this.field_70714_bg.func_75776_a(8, new EntityAIWanderExceptWhenTargeting(this, 1.2D, 10.0F));
      this.field_70714_bg.func_75776_a(9, new EntityAIWaitForJobToBeCompleted(this));
      this.field_70714_bg.func_75776_a(10, new EntityAIMoveThroughObstacles(this));
      this.field_70714_bg.func_75776_a(11, new EntityAIWaitForBuyer(this));
      this.field_70714_bg.func_75776_a(12, new EntityAIWatchClosestBlindfolded(this, EntityPlayer.class, 6.0F));
      this.field_70714_bg.func_75776_a(13, new EntityAILookIdle(this));
      this.field_70714_bg.func_75776_a(14, new EntityAIOpenDoor(this, false));
   }

   public abstract ResourceLocation getSkin();

   public void addSlave(PlayerBindState slave) {
      if (slave != null) {
         EntityInvisibleSlaveTransporter transport = slave.getTransport();
         if (transport != null) {
            transport.func_110162_b(this, true);
         }
      }

      this.slave = slave;
   }

   public void removeSlave(PlayerBindState slave) {
      if (slave != null) {
         EntityInvisibleSlaveTransporter transport = slave.getTransport();
         if (transport != null) {
            transport.func_110162_b(this, false);
         }
      }

      this.slave = null;
   }

   public void removeSlave(PlayerBindState playerBindState, boolean transportState) {
      this.removeSlave(playerBindState);
   }

   public boolean canEnslave(PlayerBindState slave) {
      return this.slave != slave;
   }

   public boolean canFree(PlayerBindState slave) {
      return this.slave.equals(slave);
   }

   public boolean canBeTiedUp() {
      return super.canBeTiedUp() && !this.isWaitingForJobToBeCompleted() && !this.isSellingSlave();
   }

   public boolean canApplyBondageItems() {
      return super.canApplyBondageItems() && !this.isWaitingForJobToBeCompleted() && !this.isSellingSlave();
   }

   public void setBinds(ItemStack bind) {
      if (this.field_70170_p != null && !this.field_70170_p.field_72995_K) {
         this.getOutState = false;
         if (this.slave != null) {
            this.slave.resetSale();
            this.slave.free();
         }

         this.func_184611_a(EnumHand.MAIN_HAND, new ItemStack(Items.field_190931_a, 0));
         this.func_184611_a(EnumHand.OFF_HAND, new ItemStack(Items.field_190931_a, 0));
      }

      super.setBinds(bind);
   }

   public boolean allowSlaveTransfer() {
      return this.allowTranfer || this.isTiedUp();
   }

   public void transferAllSlavesTo(I_Kidnapper kidnapper) {
      if (kidnapper != null) {
         this.allowTranfer = true;
         this.slave.transferSlaveryTo(kidnapper);
         this.allowTranfer = false;
      }

   }

   public void getBackSlaveFromPole(PlayerBindState slave) {
      if (slave == this.slave && slave != null) {
         EntityInvisibleSlaveTransporter transport = slave.getTransport();
         if (transport != null) {
            transport.func_110162_b(this, true);
         }
      }

   }

   public boolean hasSlaves() {
      return this.slave != null;
   }

   public EntityPlayer getTarget() {
      return this.target;
   }

   public void setTarget(EntityPlayer target) {
      this.target = target;
   }

   public boolean isCloseToTarget() {
      if (this.target == null) {
         return false;
      } else {
         PlayerBindState state = PlayerBindState.getInstance(this.target);
         return this.func_70032_d(this.target) <= 2.0F && state.isOnline();
      }
   }

   public boolean tooFarFromTarget(int radius) {
      if (this.target == null) {
         return false;
      } else {
         PlayerBindState state = PlayerBindState.getInstance(this.target);
         return this.func_70032_d(this.target) > (float)radius && state.isOnline();
      }
   }

   public boolean isSuitableTarget(EntityPlayer player) {
      if (player == null) {
         return false;
      } else {
         PlayerBindState state = PlayerBindState.getInstance(player);
         PlayerKidnapperManager manager = state.getSlaveHolderManager();
         boolean kidnappingMode = this.isSlaveKidnappingModeEnabled();
         return player != null && !player.func_184812_l_() && !player.field_70128_L && state.isOnline() && !state.isDeathState() && this.func_70635_at().func_75522_a(player) && (kidnappingMode || !manager.hasSlaves() || manager.allSlavesAreTiedOnPost()) && (!state.isSlave() || state.isSlave() && state.isTiedToPole()) && (!kidnappingMode || this.isSlaveKidnappingModeSuitableTarget(player));
      }
   }

   public boolean isSlaveKidnappingModeSuitableTarget(EntityPlayer player) {
      if (player != null && this.isSlaveKidnappingModeEnabled() && this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               boolean validTarget;
               if (itemCollar.isBlacklistenabled(collar)) {
                  validTarget = itemCollar.isOnBlackList(collar, (EntityLivingBase)player);
               } else {
                  validTarget = !itemCollar.isTargetException(collar, (EntityLivingBase)player);
               }

               return validTarget && !this.isTiedUpInPrison(player);
            }
         }
      }

      return false;
   }

   public void clearOldLeashedEntities() {
      AxisAlignedBB axis = new AxisAlignedBB(this.field_70165_t - 30.0D, this.field_70163_u - 30.0D, this.field_70161_v - 30.0D, this.field_70165_t + 30.0D, this.field_70163_u + 30.0D, this.field_70161_v + 30.0D);
      List<EntityLiving> nearTransports = this.func_130014_f_().func_72872_a(EntityInvisibleSlaveTransporter.class, axis);
      if (nearTransports != null) {
         Iterator var3 = nearTransports.iterator();

         while(var3.hasNext()) {
            EntityLiving entity = (EntityLiving)var3.next();
            if (entity.func_110166_bE() == this) {
               entity.func_110162_b(this, false);
               entity.func_70106_y();
            }
         }
      }

   }

   public PlayerBindState getSlave() {
      return this.slave;
   }

   public void onSlaveLogout(PlayerBindState slave) {
      if (slave != null) {
         if (this.slave == slave) {
            this.slave.free();
         }

      }
   }

   public boolean shouldTelportSlaveToMaster(PlayerBindState slave) {
      if (slave == null) {
         return false;
      } else {
         EntityPlayer player = slave.getPlayer();
         return this.slave != null && player != null && slave == this.slave && this.func_70032_d(player) >= 11.0F;
      }
   }

   public void teleportSlaveToMaster(PlayerBindState slave) {
      if (slave != null) {
         BlockPos pos = this.func_180425_c();
         slave.getPlayer().func_184595_k((double)(pos.func_177958_n() - 1), (double)pos.func_177956_o(), (double)pos.func_177952_p());
      }
   }

   public boolean func_70601_bi() {
      GameRules rules = this.func_130014_f_().func_82736_K();
      boolean spawn = true;
      if (rules.func_82765_e("kidnappers-spawn")) {
         spawn = rules.func_82766_b("kidnappers-spawn");
      }

      return spawn && super.func_70601_bi();
   }

   protected boolean func_70692_ba() {
      GameRules rules = this.func_130014_f_().func_82736_K();
      if (rules.func_82765_e("kidnappers-spawn")) {
         return !rules.func_82766_b("kidnappers-spawn");
      } else {
         return false;
      }
   }

   public boolean canBeAttacked() {
      return super.canBeAttacked() && !this.isSellingSlave() && !this.isWaitingForJobToBeCompleted();
   }

   public boolean isGetOutState() {
      return this.getOutState;
   }

   public void setGetOutState(boolean getOutState) {
      this.getOutState = getOutState;
   }

   public void putSlaveForSale() {
      if (this.canSell() && this.slave != null) {
         SaleLoader loader = SaleLoader.getInstance();
         if (loader != null) {
            ItemTask task = loader.getRandomTask();
            if (task != null) {
               this.slave.putSlaveForSell(task);
            }
         }
      }

   }

   public synchronized void sellSlaveTo(PlayerBindState slaveSold, PlayerBindState state) {
      if (this.slave == slaveSold && this.slave != null && this.slave.getPlayer() != null) {
         this.talkTo(slaveSold.getPlayer(), "Bye bye!");
         this.allowTranfer = true;
         slaveSold.transferSlaveryTo(state.getSlaveHolderManager());
         this.allowTranfer = false;
      }
   }

   public void onSlaveReleased(PlayerBindState slave) {
   }

   public boolean isSellingSlave() {
      return this.hasSlaves() && this.slave.isForSell();
   }

   public void onSlaveStarving(PlayerBindState slave) {
   }

   public void onSlaveStruggle(PlayerBindState slave) {
   }

   public EntityLivingBase getEntity() {
      return this;
   }

   public void onSlaveTalk(PlayerBindState slave) {
      if (slave != null) {
         EntityPlayer player = slave.getPlayer();
         if (player != null) {
            this.talkTo(player, "Shush! Shut up slave!");
         }

      }
   }

   public ItemStack getBindItem() {
      ItemStack hold = this.func_184614_ca();
      if (hold != null) {
         Item holdItem = hold.func_77973_b();
         if (holdItem != null && holdItem instanceof ItemBind) {
            return hold;
         }
      }

      return new ItemStack(ModItems.ROPES);
   }

   public ItemStack getGagItem() {
      ItemStack hold = this.func_184592_cb();
      if (hold != null) {
         Item holdItem = hold.func_77973_b();
         if (holdItem != null && holdItem instanceof ItemGag) {
            return hold;
         }
      }

      return new ItemStack(ModItems.BALL_GAG);
   }

   public void setUpHoldBind() {
      if (this.isSlaveKidnappingModeEnabled()) {
         this.func_184611_a(EnumHand.MAIN_HAND, new ItemStack(ModItems.ROPES));
      } else {
         ItemStack hold = this.func_184614_ca();
         if (hold != null && hold.func_77973_b() instanceof ItemBind) {
            this.func_184611_a(EnumHand.MAIN_HAND, new ItemStack(hold.func_77973_b()));
         } else {
            ItemBind bind = (ItemBind)ModItems.BIND_LIST.get(this.field_70146_Z.nextInt(ModItems.BIND_LIST.size()));
            int randMeta = this.field_70146_Z.nextInt(bind.getItemsCount());
            this.func_184611_a(EnumHand.MAIN_HAND, new ItemStack(bind, 1, randMeta));
         }
      }

   }

   public void setUpHoldGag() {
      if (this.isSlaveKidnappingModeEnabled()) {
         this.func_184611_a(EnumHand.OFF_HAND, new ItemStack(ModItems.BALL_GAG));
      } else {
         ItemStack hold = this.func_184592_cb();
         if (hold != null && hold.func_77973_b() instanceof ItemGag) {
            this.func_184611_a(EnumHand.OFF_HAND, new ItemStack(hold.func_77973_b()));
         } else {
            ItemGag gag = (ItemGag)ModItems.GAG_LIST.get(this.field_70146_Z.nextInt(ModItems.GAG_LIST.size()));
            int randMeta = this.field_70146_Z.nextInt(gag.getItemsCount());
            this.func_184611_a(EnumHand.OFF_HAND, new ItemStack(gag, 1, randMeta));
         }
      }

   }

   public void giveJobToSlave() {
      if (this.slave != null) {
         PlayerBindState currentSlave = this.slave;
         if (!currentSlave.hasLockedCollar()) {
            ItemTask job = JobLoader.getInstance().getRandomTask();
            if (job != null) {
               UUID jobUUID = UUID.randomUUID();
               ItemStack keyJob = new ItemStack(ModItems.COLLAR_KEY, 1);
               keyJob = ModItems.COLLAR_KEY.setOwnerId(keyJob, jobUUID);
               ItemStack slaveCollar = new ItemStack(ModItems.SHOCK_COLLAR_AUTO, 1);
               slaveCollar = ModItems.SHOCK_COLLAR_AUTO.setJob(slaveCollar, job, jobUUID, this);
               slaveCollar = ModItems.SHOCK_COLLAR_AUTO.lockCollar(slaveCollar);
               slaveCollar = ModItems.SHOCK_COLLAR_AUTO.enablePublicMode(slaveCollar);
               slaveCollar = ModItems.SHOCK_COLLAR_AUTO.setNickname(slaveCollar, "Slave");
               currentSlave.replaceSlot(ExtraBondageItemType.COLLAR, slaveCollar);
               this.func_184611_a(EnumHand.MAIN_HAND, keyJob);
               this.func_184611_a(EnumHand.OFF_HAND, new ItemStack(ModItems.SHOCKER_CONTROLLER));
               EntityPlayer player = currentSlave.getPlayer();
               if (player != null) {
                  this.actionTo(player, "put a shock collar on you!");
                  this.talkTo(player, "I command you to bring this to me : " + job.toString());
                  this.talkTo(player, "You better hurry up...");
               }

               currentSlave.free();
               currentSlave.untie();
            }
         }
      }

   }

   public boolean isWaitingForJobToBeCompleted() {
      ItemStack key = this.func_184586_b(EnumHand.MAIN_HAND);
      if (key != null && !key.func_190926_b() && key.func_77973_b() instanceof ItemKeyCollar) {
         ItemKeyCollar keyCollar = (ItemKeyCollar)key.func_77973_b();
         return keyCollar.hasOwner(key);
      } else {
         return false;
      }
   }

   public boolean shouldStillWaitForJob() {
      ItemStack key = this.func_184586_b(EnumHand.MAIN_HAND);
      if (key != null && !key.func_190926_b() && key.func_77973_b() instanceof ItemKeyCollar) {
         ItemKeyCollar keyCollar = (ItemKeyCollar)key.func_77973_b();
         if (this.field_70170_p != null && this.field_70170_p.func_73046_m() != null) {
            List<EntityPlayerMP> players = this.field_70170_p.func_73046_m().func_184103_al().func_181057_v();
            Iterator var4 = players.iterator();

            while(var4.hasNext()) {
               EntityPlayerMP player = (EntityPlayerMP)var4.next();
               if (this.isSlaveWorkerForKey(player, key, keyCollar)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void checkJobComplete(EntityPlayer player, ItemStack stack) {
      if (player != null && stack != null) {
         ItemStack key = this.func_184586_b(EnumHand.MAIN_HAND);
         if (key != null && !key.func_190926_b() && key.func_77973_b() instanceof ItemKeyCollar) {
            ItemKeyCollar keyCollar = (ItemKeyCollar)key.func_77973_b();
            if (this.isSlaveWorkerForKey(player, key, keyCollar)) {
               PlayerBindState state = PlayerBindState.getInstance(player);
               if (state != null && !state.isTiedUp()) {
                  ItemStack collarStack = state.getCurrentCollar();
                  if (collarStack.func_77973_b() instanceof ItemShockCollarAuto) {
                     ItemShockCollarAuto collar = (ItemShockCollarAuto)collarStack.func_77973_b();
                     if (collar.doesStackCompleteJob(collarStack, stack)) {
                        ItemStack jobStack = collar.getJobItemStack(collarStack);
                        stack.func_190918_g(jobStack.func_190916_E());
                        this.talkTo(player, "Well done slave, you've earned your freedom.");
                        this.func_184611_a(EnumHand.MAIN_HAND, new ItemStack(Items.field_190931_a));
                        this.func_184611_a(EnumHand.OFF_HAND, new ItemStack(Items.field_190931_a));
                        this.setGetOutState(true);
                        state.clearSlot(ExtraBondageItemType.COLLAR);
                        this.actionTo(player, "removed your collar!");
                     } else {
                        this.talkTo(player, "This is not what I requested.");
                        state.shockKidnapped();
                     }
                  }
               }
            } else {
               this.talkTo(player, "Get out.");
            }
         }

      }
   }

   private boolean isSlaveWorkerForKey(EntityPlayer player, ItemStack key, ItemKeyCollar keyCollar) {
      UUID id = keyCollar.getOwnerId(key);
      PlayerBindState state = PlayerBindState.getInstance(player);
      if (state != null && state.hasLockedCollar()) {
         ItemStack collarStack = state.getCurrentCollar();
         if (collarStack.func_77973_b() instanceof ItemShockCollarAuto) {
            ItemShockCollarAuto collar = (ItemShockCollarAuto)collarStack.func_77973_b();
            return collar.isOwner(collarStack, id);
         }
      }

      return false;
   }

   public boolean canSetUpJob() {
      if (this.field_70170_p == null) {
         return false;
      } else {
         GameRules rules = this.field_70170_p.func_82736_K();
         if (rules != null && rules.func_82765_e("kidnappers_jobs") && !rules.func_82766_b("kidnappers_jobs")) {
            return false;
         } else if (!ModConfig.kidnappersJob) {
            return false;
         } else {
            JobLoader loader = JobLoader.getInstance();
            if (loader != null && loader.hasTasks() && this.slave != null) {
               return !this.slave.hasLockedCollar();
            } else {
               return false;
            }
         }
      }
   }

   public boolean canSell() {
      if (this.field_70170_p == null) {
         return false;
      } else {
         GameRules rules = this.field_70170_p.func_82736_K();
         if (rules != null && rules.func_82765_e("kidnappers_sell") && !rules.func_82766_b("kidnappers_sell")) {
            return false;
         } else if (!ModConfig.kidnappersSell) {
            return false;
         } else {
            SaleLoader sales = SaleLoader.getInstance();
            if (sales != null && sales.hasTasks()) {
               if (this.field_70170_p != null) {
                  MinecraftServer server = this.field_70170_p.func_73046_m();
                  if (server != null) {
                     PlayerList playerList = server.func_184103_al();
                     if (playerList != null) {
                        return playerList.func_72394_k() > 1;
                     }
                  }
               }

               return false;
            } else {
               return false;
            }
         }
      }
   }

   public void notifyPlayerAttack(EntityPlayer player) {
      super.notifyPlayerAttack(player);
      if (player != null) {
         ItemStack key = this.func_184586_b(EnumHand.MAIN_HAND);
         if (key != null && !key.func_190926_b() && key.func_77973_b() instanceof ItemKeyCollar) {
            ItemKeyCollar keyCollar = (ItemKeyCollar)key.func_77973_b();
            if (this.isSlaveWorkerForKey(player, key, keyCollar)) {
               this.talkTo(player, "Don't you ever do that again");
               PlayerBindState state = PlayerBindState.getInstance(player);
               if (state != null) {
                  state.shockKidnapped();
                  state.shockKidnapped();
                  state.shockKidnapped();
               }
            }
         }
      }

   }

   public void func_110162_b(Entity entityIn, boolean sendAttachNotification) {
      if (entityIn != null && entityIn instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)entityIn;
         if (this.hasSlaves()) {
            PlayerBindState stateKidnapper = PlayerBindState.getInstance(player);
            if (stateKidnapper != null) {
               I_Kidnapper manager = stateKidnapper.getSlaveHolderManager();
               this.transferAllSlavesTo(manager);
            }
         }
      }

      super.func_110162_b(entityIn, sendAttachNotification);
   }

   public TextFormatting getNameColor() {
      TextFormatting color = TextFormatting.RED;
      if (this.isBondageServiceEnabled()) {
         color = TextFormatting.LIGHT_PURPLE;
      }

      if (this.isSlaveKidnappingModeEnabled()) {
         color = TextFormatting.RED;
      }

      return color;
   }

   public boolean isSlaveKidnappingModeEnabled() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               Position prison = itemCollar.getPrison(collar);
               boolean kidnappingMode = itemCollar.isKidnappingModeEnabled(collar);
               return prison != null && kidnappingMode;
            }
         }
      }

      return false;
   }

   public boolean allowMultipleSlaves() {
      return false;
   }

   public EntityPlayer getClosestSuitablePlayer(int radius) {
      BlockPos pos = this.func_180425_c();
      if (pos != null) {
         int x = pos.func_177958_n();
         int y = pos.func_177956_o();
         int z = pos.func_177952_p();
         AxisAlignedBB axis = new AxisAlignedBB((double)(x - radius), (double)(y - radius), (double)(z - radius), (double)(x + radius), (double)(y + radius), (double)(z + radius));
         List<EntityPlayer> playersList = this.field_70170_p.func_72872_a(EntityPlayer.class, axis);
         Iterator var8 = playersList.iterator();

         while(var8.hasNext()) {
            EntityPlayer player = (EntityPlayer)var8.next();
            if (player != null && this.isSuitableTarget(player)) {
               return player;
            }
         }
      }

      return null;
   }

   public synchronized void teleportSlaveToPrison() {
      Position prison = this.getPrison();
      if (prison != null && this.slave != null) {
         EntityPlayer player = this.slave.getPlayer();
         if (player != null) {
            this.slave.teleportToPosition(prison, false);
            this.warnMasters(player);
            PlayerBindState state = PlayerBindState.getInstance(player);
            if (state != null && this.shouldTiedSlaveToPoleInPrison()) {
               state.tieToClosestPole();
            }
         }
      }

   }

   public boolean shouldBackHomeAfterKidnapping() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               return itemCollar.shouldBackHome(collar);
            }
         }
      }

      return false;
   }

   public boolean isTiedUpInPrison(EntityPlayer target) {
      if (target != null && this.hasCollar()) {
         PlayerBindState state = PlayerBindState.getInstance(target);
         if (state != null && state.isTiedUp()) {
            ItemStack collar = this.getCurrentCollar();
            if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
               ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
               if (itemCollar != null) {
                  Position prison = this.getPrison();
                  if (prison != null) {
                     int radius = itemCollar.getPrisonRadius(collar);
                     if (radius >= 0) {
                        double x = prison.getX();
                        double y = prison.getY();
                        double z = prison.getZ();
                        double distance = target.func_70011_f(x, y, z);
                        if (distance <= (double)radius) {
                           return true;
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   public boolean isBondageServiceEnabled() {
      return this.isSlaveKidnappingModeEnabled() ? false : super.isBondageServiceEnabled();
   }

   protected boolean canStayOnPositionOnDeath() {
      return super.canStayOnPositionOnDeath() && !this.isSlaveKidnappingModeEnabled();
   }

   public ITextComponent func_145748_c_() {
      if (this.isSlaveKidnappingModeEnabled()) {
         ITextComponent component = new TextComponentString(this.getDamselName());
         component.func_150256_b().func_150238_a(TextFormatting.RED);
         return component;
      } else {
         return super.func_145748_c_();
      }
   }

   public synchronized void reloadCollar(ItemStack collar) {
      super.reloadCollar(collar);
      if (!this.isSlaveKidnappingModeEnabled() && this.slave != null) {
         this.slave.free();
      }

   }

   public boolean canPannick() {
      return false;
   }

   public static enum SlavesTask {
      SELL,
      JOB;

      public static List<EntityKidnapper.SlavesTask> getPossiblesTaks(EntityKidnapper kidnapper) {
         List<EntityKidnapper.SlavesTask> possibleTaks = new ArrayList();
         if (kidnapper.canSell()) {
            possibleTaks.add(SELL);
         }

         if (kidnapper.canSetUpJob()) {
            possibleTaks.add(JOB);
         }

         return possibleTaks;
      }
   }
}
