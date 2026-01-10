package com.yuti.kidnapmod.states.kidnapped.managers;

import com.yuti.kidnapmod.data.BountiesData;
import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.teleport.Position;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class PlayerKidnapperManager implements I_Kidnapper {
   private EntityPlayer master;
   private List<PlayerBindState> slaves = new ArrayList();

   public PlayerKidnapperManager(EntityPlayer master) {
      this.master = master;
   }

   public void addSlave(PlayerBindState slave) {
      if (slave != null) {
         if (slave.getTransport() != null && this.master != null) {
            slave.getTransport().func_110162_b(this.master, true);
         }

         this.slaves.remove(slave);
         this.slaves.add(slave);
      }
   }

   public void removeSlave(PlayerBindState slave) {
      this.removeSlave(slave, true);
   }

   public void removeSlave(PlayerBindState slave, boolean transportState) {
      if (slave != null) {
         if (transportState && slave.getTransport() != null && this.master != null) {
            slave.getTransport().func_110162_b(this.master, true);
         }

         this.slaves.remove(slave);
      }
   }

   public boolean canEnslave(PlayerBindState slave) {
      PlayerBindState masterState = PlayerBindState.getInstance(this.master);
      return !masterState.isSlave() && !masterState.isTiedUp();
   }

   public boolean canFree(PlayerBindState slave) {
      return slave != null && this.slaves.contains(slave);
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof PlayerKidnapperManager)) {
         return false;
      } else {
         PlayerKidnapperManager target = (PlayerKidnapperManager)obj;
         return target.master == this.master;
      }
   }

   public boolean allowSlaveTransfer() {
      return true;
   }

   public void transferAllSlavesTo(I_Kidnapper kidnapper) {
      List<PlayerBindState> slavesToTransfer = new ArrayList();
      Iterator var3 = this.slaves.iterator();

      PlayerBindState slave;
      while(var3.hasNext()) {
         slave = (PlayerBindState)var3.next();
         slavesToTransfer.add(slave);
      }

      var3 = slavesToTransfer.iterator();

      while(var3.hasNext()) {
         slave = (PlayerBindState)var3.next();
         slave.transferSlaveryTo(kidnapper);
      }

   }

   public void getBackSlaveFromPole(PlayerBindState slave) {
      if (slave != null) {
         if (this.slaves.contains(slave)) {
            slave.getTransport().func_110162_b(this.master, true);
         }

      }
   }

   public boolean hasSlaves() {
      if (this.hasDamsels()) {
         return true;
      } else {
         Iterator var1 = this.slaves.iterator();

         PlayerBindState state;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            state = (PlayerBindState)var1.next();
         } while(state == null || !state.isOnline());

         return true;
      }
   }

   public boolean allSlavesAreTiedOnPost() {
      Iterator var1 = this.slaves.iterator();

      PlayerBindState slave;
      do {
         if (!var1.hasNext()) {
            if (this.hasDamsels()) {
               return false;
            }

            return true;
         }

         slave = (PlayerBindState)var1.next();
      } while(slave == null || !slave.isOnline() || slave.isTiedToPole());

      return false;
   }

   public boolean hasDamsels() {
      if (this.master != null && this.master.field_70170_p != null) {
         AxisAlignedBB axis = new AxisAlignedBB(this.master.field_70165_t - 30.0D, this.master.field_70163_u - 30.0D, this.master.field_70161_v - 30.0D, this.master.field_70165_t + 30.0D, this.master.field_70163_u + 30.0D, this.master.field_70161_v + 30.0D);
         List<EntityLiving> nearTransports = this.master.field_70170_p.func_72872_a(EntityDamsel.class, axis);
         if (nearTransports != null) {
            Iterator var3 = nearTransports.iterator();

            while(var3.hasNext()) {
               EntityLiving entity = (EntityLiving)var3.next();
               if (entity.func_110166_bE() != null && entity.func_110166_bE().equals(this.master)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public List<EntityDamsel> getSlavesDamsels() {
      List<EntityDamsel> damsels = new ArrayList();
      if (this.master != null && this.master.field_70170_p != null) {
         AxisAlignedBB axis = new AxisAlignedBB(this.master.field_70165_t - 30.0D, this.master.field_70163_u - 30.0D, this.master.field_70161_v - 30.0D, this.master.field_70165_t + 30.0D, this.master.field_70163_u + 30.0D, this.master.field_70161_v + 30.0D);
         List<EntityLiving> nearTransports = this.master.field_70170_p.func_72872_a(EntityDamsel.class, axis);
         if (nearTransports != null) {
            Iterator var4 = nearTransports.iterator();

            while(var4.hasNext()) {
               EntityLiving entity = (EntityLiving)var4.next();
               if (entity.func_110166_bE() != null && entity.func_110166_bE().equals(this.master) && entity instanceof EntityDamsel) {
                  damsels.add((EntityDamsel)entity);
               }
            }
         }
      }

      return damsels;
   }

   public void resetMaster(EntityPlayer master) {
      if (master != null) {
         this.master = master;
         Iterator var2 = this.slaves.iterator();

         while(var2.hasNext()) {
            PlayerBindState slave = (PlayerBindState)var2.next();
            if (slave.getTransport() != null && !slave.isTiedToPole()) {
               slave.getTransport().func_110162_b(this.master, true);
            }
         }

      }
   }

   public void onSlaveLogout(PlayerBindState slave) {
   }

   public boolean shouldTelportSlaveToMaster(PlayerBindState slave) {
      return false;
   }

   public void teleportSlaveToMaster(PlayerBindState slave) {
   }

   public void onSlaveReleased(PlayerBindState slave) {
   }

   public void onSlaveStarving(PlayerBindState slave) {
      if (this.master != null && slave != null && slave.getPlayer() != null) {
         EntityPlayer target = slave.getPlayer();
         this.master.func_145747_a(new TextComponentString("Your slave " + target.func_70005_c_() + " is starving!"));
      }

   }

   public void onSlaveStruggle(PlayerBindState slave) {
      if (slave != null) {
         EntityPlayer slavePlayer = slave.getPlayer();
         if (this.master != null && slavePlayer != null) {
            this.master.func_145747_a(new TextComponentString("Your slave " + slavePlayer.func_70005_c_() + " is struggling!"));
         }

      }
   }

   public EntityLivingBase getEntity() {
      return this.master;
   }

   public void onSlaveTalk(PlayerBindState slave) {
   }

   public void checkBountiesForClient(EntityPlayer client, World world) {
      BountiesData data = BountiesData.get(world);
      PlayerBindState clientState = PlayerBindState.getInstance(client);
      if (clientState != null) {
         I_Kidnapper clientKidnapper = clientState.getSlaveHolderManager();
         if (clientKidnapper != null) {
            Iterator var6 = this.slaves.iterator();

            while(var6.hasNext()) {
               PlayerBindState slaveState = (PlayerBindState)var6.next();
               if (slaveState != null && data.tryDeliverPrisonner(this.master, client, slaveState.getPlayer())) {
                  slaveState.transferSlaveryTo(clientKidnapper);
               }
            }
         }
      }

   }

   public synchronized List<PlayerBindState> getSlavesListCopy() {
      List<PlayerBindState> slavesList = new ArrayList();
      Iterator var2 = this.slaves.iterator();

      while(var2.hasNext()) {
         PlayerBindState slave = (PlayerBindState)var2.next();
         if (slave != null && slave.getPlayer() != null) {
            slavesList.add(slave);
         }
      }

      return slavesList;
   }

   public synchronized void teleportToKidnappingWarp(Position pos) {
      if (pos != null && this.master != null) {
         PlayerBindState state = PlayerBindState.getInstance(this.master);
         if (state != null) {
            state.free(true, true);
         }

         Iterator var3 = this.getSlavesListCopy().iterator();

         while(var3.hasNext()) {
            PlayerBindState slave = (PlayerBindState)var3.next();
            if (slave != null && slave.getPlayer() != null && !slave.isTiedToPole()) {
               slave.teleportToPosition(pos, false, true);
            }
         }

         List<EntityDamsel> damsels = this.getSlavesDamsels();
         Iterator var7 = damsels.iterator();

         while(var7.hasNext()) {
            EntityDamsel damsel = (EntityDamsel)var7.next();
            if (damsel != null) {
               damsel.teleportToPosition(pos);
            }
         }

         if (state != null) {
            state.teleportToPosition(pos);
         }
      }

   }

   public synchronized void freeAllSlaves() {
      Iterator var1 = this.getSlavesListCopy().iterator();

      while(var1.hasNext()) {
         PlayerBindState slave = (PlayerBindState)var1.next();
         if (slave != null && slave.getPlayer() != null) {
            slave.free(false, true);
         }
      }

   }

   public boolean allowMultipleSlaves() {
      return true;
   }
}
