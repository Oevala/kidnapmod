package com.yuti.kidnapmod.data;

import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class KidnappingSettingsData extends WorldSavedData {
   private static final String DATA_NAME = "knapm_Kidnapping_Settings";
   public static final String SETTINGS_ID = "kidnapping_settings";
   private static final Object lock = new Object();
   public static final String MAX_LOCATIONS_RULE = "max_kidnapping_locations";
   public static final int MAX_LOCATIONS = 10;
   private Map<UUID, KidnappingSettings> instances = new HashMap();

   public KidnappingSettingsData() {
      super("knapm_Kidnapping_Settings");
   }

   public KidnappingSettingsData(String s) {
      super(s);
   }

   public void func_76184_a(NBTTagCompound nbt) {
      if (nbt.func_74764_b("kidnappingSettings")) {
         NBTTagList list = (NBTTagList)nbt.func_74781_a("kidnappingSettings");
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            NBTBase element = (NBTBase)var3.next();
            NBTTagCompound userCompound = (NBTTagCompound)element;
            if (userCompound.func_74764_b("uuid")) {
               String id = userCompound.func_74779_i("uuid");
               if (id != null) {
                  UUID convertedId = UUID.fromString(id);
                  if (convertedId != null) {
                     KidnappingSettings settings = new KidnappingSettings();
                     if (userCompound.func_74764_b("settings")) {
                        NBTTagCompound settingsTag = userCompound.func_74775_l("settings");
                        settings.readFromNBT(settingsTag);
                     }

                     this.instances.put(convertedId, settings);
                  }
               }
            }
         }
      }

   }

   public NBTTagCompound func_189551_b(NBTTagCompound compound) {
      NBTTagList list = new NBTTagList();
      Iterator var3 = this.instances.keySet().iterator();

      while(var3.hasNext()) {
         UUID id = (UUID)var3.next();
         if (id != null) {
            String convertedId = id.toString();
            if (convertedId != null) {
               NBTTagCompound userCompound = new NBTTagCompound();
               userCompound.func_74778_a("uuid", convertedId);
               NBTTagCompound settingsTag = new NBTTagCompound();
               KidnappingSettings settings = (KidnappingSettings)this.instances.get(id);
               if (settings != null) {
                  settings.writeToNBT(settingsTag);
               }

               userCompound.func_74782_a("settings", settingsTag);
               list.func_74742_a(userCompound);
            }
         }
      }

      compound.func_74782_a("kidnappingSettings", list);
      return compound;
   }

   public static KidnappingSettingsData get(World world) {
      synchronized(lock) {
         MapStorage storage = world.func_175693_T();
         KidnappingSettingsData instance = (KidnappingSettingsData)storage.func_75742_a(KidnappingSettingsData.class, "knapm_Kidnapping_Settings");
         if (instance == null) {
            instance = new KidnappingSettingsData();
            storage.func_75745_a("knapm_Kidnapping_Settings", instance);
         }

         return instance;
      }
   }

   public KidnappingSettings getKidnappingSettings(EntityPlayer player) {
      synchronized(lock) {
         if (player != null) {
            UUID id = player.func_110124_au();
            return this.getKidnappingSettings(id);
         } else {
            return null;
         }
      }
   }

   public KidnappingSettings getKidnappingSettings(UUID id) {
      synchronized(lock) {
         if (id != null) {
            KidnappingSettings settings = null;
            if (!this.instances.containsKey(id)) {
               settings = new KidnappingSettings();
               this.instances.put(id, settings);
               this.func_76185_a();
            } else {
               settings = (KidnappingSettings)this.instances.get(id);
            }

            return settings;
         } else {
            return null;
         }
      }
   }

   public Set<String> getKidnappingLocationsNames(EntityPlayer player) {
      KidnappingSettings settings = this.getKidnappingSettings(player);
      if (settings != null) {
         Map<String, Position> locations = settings.getKidnappingLocations();
         if (locations != null) {
            return locations.keySet();
         }
      }

      return null;
   }

   public int getMaxLocationsForPlayers(World world) {
      synchronized(lock) {
         GameRules rules = world.func_82736_K();
         int maxLocations = 10;
         if (rules.func_82765_e("max_kidnapping_locations")) {
            maxLocations = rules.func_180263_c("max_kidnapping_locations");
         }

         return maxLocations;
      }
   }

   public void setLocation(EntityPlayer player, String name) {
      synchronized(lock) {
         KidnappingSettings settings = this.getKidnappingSettings(player);
         if (settings != null && player != null && player.field_70170_p != null) {
            Map<String, Position> locations = settings.getKidnappingLocations();
            if (locations != null) {
               int max_locations = this.getMaxLocationsForPlayers(player.field_70170_p);
               if (!locations.containsKey(name) && locations.size() >= max_locations && !Utils.isOpe(player)) {
                  Utils.sendErrorMessageToEntity(player, "You can't have more than " + max_locations + " kidnapping warps.");
               } else {
                  Position pos = Utils.getEntityPosition(player);
                  if (pos != null) {
                     locations.put(name, pos);
                     this.func_76185_a();
                     Utils.sendValidMessageToEntity(player, name + " has been set!");
                  }
               }
            }
         }

      }
   }

   public void deleteLocations(EntityPlayer player, String name) {
      synchronized(lock) {
         KidnappingSettings settings = this.getKidnappingSettings(player);
         if (settings != null && player != null) {
            Map<String, Position> locations = settings.getKidnappingLocations();
            if (locations != null) {
               if (locations.containsKey(name)) {
                  locations.remove(name);
                  this.func_76185_a();
                  Utils.sendValidMessageToEntity(player, name + " has been deleted!");
               } else {
                  Utils.sendErrorMessageToEntity(player, name + " does not exist.");
               }
            }
         }

      }
   }

   public Position getLocation(UUID id, String name) {
      synchronized(lock) {
         KidnappingSettings settings = this.getKidnappingSettings(id);
         if (settings != null) {
            Map<String, Position> locations = settings.getKidnappingLocations();
            if (locations != null && locations.containsKey(name)) {
               return (Position)locations.get(name);
            }
         }

         return null;
      }
   }

   public boolean isOnConsentBoard(EntityPlayer player) {
      synchronized(lock) {
         KidnappingSettings settings = this.getKidnappingSettings(player);
         return settings != null ? settings.isOnConsentBoard() : false;
      }
   }

   public void addToConsentBoard(EntityPlayer player) {
      synchronized(lock) {
         KidnappingSettings settings = this.getKidnappingSettings(player);
         if (settings != null) {
            if (!settings.isOnConsentBoard()) {
               settings.setConsentBoard(true);
               this.func_76185_a();
               Utils.sendValidMessageToEntity(player, "You've been added to the consent board!");
            } else {
               Utils.sendErrorMessageToEntity(player, "You already are on the consent board.");
            }
         }

      }
   }

   public void removeFromConsentBoard(EntityPlayer player) {
      synchronized(lock) {
         KidnappingSettings settings = this.getKidnappingSettings(player);
         if (settings != null) {
            if (settings.isOnConsentBoard()) {
               settings.setConsentBoard(false);
               this.func_76185_a();
               Utils.sendValidMessageToEntity(player, "You've been removed from the consent board!");
            } else {
               Utils.sendErrorMessageToEntity(player, "You are not on the consent board.");
            }
         }

      }
   }
}
