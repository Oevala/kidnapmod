package com.yuti.kidnapmod.data;

import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class BountiesData extends WorldSavedData {
   private static final String DATA_NAME = "knapm_Bounties";
   public static final String BOUNTIES_ID = "bounties";
   public static final String OLD_BOUNTIES_ID = "old_bounties";
   private static final int DEFAULT_MAX_BOUNTIES_PER_PLAYER = 5;
   private static final String RULE_NAME_MAX = "max_bounties";
   private static final int DEFAULT_BOUNTY_TIME = 14400;
   private static final String RULE_NAME_TIME = "time_bounties";
   private static final Object lock = new Object();
   private List<Bounty> bounties = new ArrayList();
   private List<Bounty> old_bounties = new ArrayList();

   public BountiesData() {
      super("knapm_Bounties");
   }

   public BountiesData(String s) {
      super(s);
   }

   public void func_76184_a(NBTTagCompound nbt) {
      this.bounties = deserializeBountiesFromNBT("bounties", nbt);
      this.old_bounties = deserializeBountiesFromNBT("old_bounties", nbt);
   }

   public NBTTagCompound func_189551_b(NBTTagCompound compound) {
      compound = serializeBountiesToNBT("bounties", this.bounties, compound);
      compound = serializeBountiesToNBT("old_bounties", this.old_bounties, compound);
      return compound;
   }

   public static List<Bounty> deserializeBountiesFromNBT(String key, NBTTagCompound nbt) {
      NBTTagList list = (NBTTagList)nbt.func_74781_a(key);
      List<Bounty> bounties = new ArrayList();
      if (list != null) {
         Iterator var4 = list.iterator();

         while(var4.hasNext()) {
            NBTBase tag = (NBTBase)var4.next();
            Bounty bounty = new Bounty();
            bounty.readFromNBT((NBTTagCompound)tag);
            bounties.add(bounty);
         }
      }

      return bounties;
   }

   public static NBTTagCompound serializeBountiesToNBT(String key, List<Bounty> bounties, NBTTagCompound compound) {
      NBTTagList list = new NBTTagList();
      if (bounties != null) {
         Iterator var4 = bounties.iterator();

         while(var4.hasNext()) {
            Bounty bounty = (Bounty)var4.next();
            NBTTagCompound tag = new NBTTagCompound();
            bounty.writeToNBT(tag);
            list.func_74742_a(tag);
         }
      }

      compound.func_74782_a(key, list);
      return compound;
   }

   public static BountiesData get(World world) {
      synchronized(lock) {
         MapStorage storage = world.func_175693_T();
         BountiesData instance = (BountiesData)storage.func_75742_a(BountiesData.class, "knapm_Bounties");
         if (instance == null) {
            instance = new BountiesData();
            storage.func_75745_a("knapm_Bounties", instance);
         }

         return instance;
      }
   }

   public synchronized List<Bounty> getBounties(World world) {
      synchronized(lock) {
         Iterator it = this.bounties.iterator();

         while(it.hasNext()) {
            Bounty bounty = (Bounty)it.next();
            bounty.setUpBeforeSend();
            if (bounty.isOutdated()) {
               it.remove();
               this.onBountyOutdate(world, bounty);
            }
         }

         this.func_76185_a();
         return this.bounties;
      }
   }

   public void addBounty(Bounty bounty) {
      synchronized(lock) {
         this.bounties.add(bounty);
         this.func_76185_a();
      }
   }

   public Bounty getBountyById(String id) {
      synchronized(lock) {
         Iterator var3 = this.bounties.iterator();

         Bounty bounty;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            bounty = (Bounty)var3.next();
         } while(!bounty.getId().equals(id));

         return bounty;
      }
   }

   public synchronized void cancelBounty(EntityPlayer player, String bountyId) {
      synchronized(lock) {
         Bounty bounty = this.getBountyById(bountyId);
         if (bounty != null) {
            boolean isOpe = Utils.isOpe(player);
            boolean isClient = bounty.isClient(player);
            if (isClient || isOpe) {
               this.bounties.remove(bounty);
               this.func_76185_a();
               if (isClient) {
                  this.giveReward(player, bounty);
               } else {
                  this.onBountyOutdate(player.field_70170_p, bounty);
               }

               Utils.broadcastMessage(player.func_70005_c_() + " canceled a bounty on " + bounty.getTargetName());
            }

         }
      }
   }

   public synchronized boolean tryDeliverPrisonner(EntityPlayer player, EntityPlayer client, EntityPlayer slave) {
      synchronized(lock) {
         boolean match = false;
         Iterator it = this.bounties.iterator();

         while(it.hasNext()) {
            Bounty bounty = (Bounty)it.next();
            bounty.setUpBeforeSend();
            if (!bounty.isOutdated() && bounty.matchRequirements(client, slave)) {
               it.remove();
               this.func_76185_a();
               match = true;
               this.giveReward(player, bounty);
            }
         }

         if (match) {
            Utils.broadcastMessage(player.func_70005_c_() + " delivered " + slave.func_70005_c_() + " to " + client.func_70005_c_());
         }

         return match;
      }
   }

   private void onBountyOutdate(World world, Bounty bounty) {
      synchronized(lock) {
         if (world != null && bounty != null) {
            EntityPlayer client = world.func_152378_a(bounty.getClientId());
            if (client != null) {
               this.giveReward(client, bounty);
            } else {
               this.old_bounties.add(bounty);
               this.func_76185_a();
            }
         }

      }
   }

   private void giveReward(EntityPlayer player, Bounty bounty) {
      synchronized(lock) {
         if (bounty != null && player != null) {
            ItemStack reward = bounty.getReward();
            player.func_191521_c(reward);
         }

      }
   }

   public synchronized void checkForOldBounties(EntityPlayer player) {
      synchronized(lock) {
         Iterator it = this.old_bounties.iterator();

         while(it.hasNext()) {
            Bounty bounty = (Bounty)it.next();
            if (bounty.isClient(player)) {
               this.giveReward(player, bounty);
               it.remove();
               this.func_76185_a();
            }
         }

      }
   }

   public boolean isAllowedToCreateBounty(EntityPlayer player) {
      synchronized(lock) {
         return Utils.isOpe(player) || this.getBountiesCountForPlayer(player) < this.getMaxBountiesPerPlayer(player.field_70170_p);
      }
   }

   private int getBountiesCountForPlayer(EntityPlayer player) {
      synchronized(lock) {
         Iterator<Bounty> it = this.bounties.iterator();
         int count = 0;

         while(it.hasNext()) {
            Bounty bounty = (Bounty)it.next();
            if (bounty.isClient(player)) {
               ++count;
            }
         }

         return count;
      }
   }

   public int getMaxBountiesPerPlayer(World world) {
      synchronized(lock) {
         GameRules rules = world.func_82736_K();
         int maxBounties = 5;
         if (rules.func_82765_e("max_bounties")) {
            maxBounties = rules.func_180263_c("max_bounties");
         }

         return maxBounties;
      }
   }

   public int getTimeForBounties(World world) {
      synchronized(lock) {
         GameRules rules = world.func_82736_K();
         int time = 14400;
         if (rules.func_82765_e("time_bounties")) {
            time = rules.func_180263_c("time_bounties");
         }

         return time;
      }
   }
}
