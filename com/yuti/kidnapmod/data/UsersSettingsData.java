package com.yuti.kidnapmod.data;

import com.yuti.kidnapmod.util.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class UsersSettingsData extends WorldSavedData {
   private static final String DATA_NAME = "knapm_Users_Settings";
   public static final String SETTINGS_ID = "settings";
   private static final Object lock = new Object();
   private Map<UUID, UserSettings> instances = new HashMap();

   public UsersSettingsData() {
      super("knapm_Users_Settings");
   }

   public UsersSettingsData(String s) {
      super(s);
   }

   public void func_76184_a(NBTTagCompound nbt) {
      try {
         this.instances = (Map)Utils.deserialize(nbt.func_74770_j("settings"));
      } catch (IOException | ClassNotFoundException var3) {
         var3.printStackTrace();
      }

   }

   public NBTTagCompound func_189551_b(NBTTagCompound compound) {
      try {
         compound.func_74773_a("settings", Utils.serialize(this.instances));
      } catch (IOException var3) {
         var3.printStackTrace();
      }

      return compound;
   }

   public static UsersSettingsData get(World world) {
      synchronized(lock) {
         MapStorage storage = world.func_175693_T();
         UsersSettingsData instance = (UsersSettingsData)storage.func_75742_a(UsersSettingsData.class, "knapm_Users_Settings");
         if (instance == null) {
            instance = new UsersSettingsData();
            storage.func_75745_a("knapm_Users_Settings", instance);
         }

         return instance;
      }
   }

   public int getTalkArea(EntityPlayer player) {
      synchronized(lock) {
         UserSettings settings = this.getSettings(player);
         return settings.getTalkArea();
      }
   }

   public void setTalkArea(EntityPlayer player, int area) {
      synchronized(lock) {
         UserSettings settings = this.getSettings(player);
         settings.setTalkArea(area);
         this.func_76185_a();
      }
   }

   public int getTalkAreaPref(EntityPlayer player) {
      synchronized(lock) {
         UserSettings settings = this.getSettings(player);
         return settings.getTalkAreaPref();
      }
   }

   public void setTalkAreaPref(EntityPlayer player, int area) {
      synchronized(lock) {
         UserSettings settings = this.getSettings(player);
         settings.setTalkAreaPref(area);
         this.func_76185_a();
      }
   }

   public void blockPlayer(EntityPlayer blocker, EntityPlayer blocked) {
      synchronized(lock) {
         UserSettings settings = this.getSettings(blocker);
         if (blocker != null && blocked != null) {
            if (blocker.func_110124_au().equals(blocked.func_110124_au())) {
               Utils.sendErrorMessageToEntity(blocker, "You can't block yourself");
               return;
            }

            if (settings.addBlockedPlayer(blocked)) {
               Utils.sendValidMessageToEntity(blocker, blocked.func_70005_c_() + " is now blocked");
               this.func_76185_a();
            } else {
               Utils.sendErrorMessageToEntity(blocker, "You've already blocked this player");
            }
         }

      }
   }

   public void unblockPlayer(EntityPlayer blocker, EntityPlayer unblocked) {
      synchronized(lock) {
         UserSettings settings = this.getSettings(blocker);
         if (unblocked != null && settings.removeBlockedPlayer(unblocked)) {
            Utils.sendValidMessageToEntity(blocker, unblocked.func_70005_c_() + " is now unblocked");
            this.func_76185_a();
         } else {
            Utils.sendErrorMessageToEntity(blocker, "This player is not blocked");
         }

      }
   }

   public boolean checkBlocked(EntityPlayer blocker, EntityPlayer target) {
      synchronized(lock) {
         if (Utils.isOpe(target)) {
            return false;
         } else {
            UserSettings settings = this.getSettings(blocker);
            return settings.isBlocked(target);
         }
      }
   }

   private UserSettings getSettings(EntityPlayer player) {
      synchronized(lock) {
         UUID playerId = player.func_110124_au();
         if (this.instances.containsKey(playerId)) {
            return (UserSettings)this.instances.get(playerId);
         } else {
            UserSettings settings = new UserSettings();
            this.instances.put(player.func_110124_au(), settings);
            return settings;
         }
      }
   }
}
