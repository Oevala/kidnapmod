package com.yuti.kidnapmod.data;

import com.yuti.kidnapmod.util.teleport.Position;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class SlavesUnloadLocationsData extends WorldSavedData {
   private static final String DATA_NAME = "knapm_NPC_SLAVES_UNLOAD_LOCATIONS_DATA";
   public static final String SETTINGS_ID = "npc_slaves_unload_locations";
   private static final Object lock = new Object();
   private Map<UUID, Map<UUID, Position>> instances = new HashMap();

   public SlavesUnloadLocationsData(String name) {
      super(name);
   }

   public SlavesUnloadLocationsData() {
      super("knapm_NPC_SLAVES_UNLOAD_LOCATIONS_DATA");
   }

   public void func_76184_a(NBTTagCompound nbt) {
      if (nbt.func_74764_b("slaves_unload_positions")) {
         NBTTagList list = (NBTTagList)nbt.func_74781_a("slaves_unload_positions");
         Iterator var3 = list.iterator();

         while(true) {
            NBTTagCompound userCompound;
            UUID convertedId;
            do {
               String id;
               do {
                  do {
                     if (!var3.hasNext()) {
                        return;
                     }

                     NBTBase element = (NBTBase)var3.next();
                     userCompound = (NBTTagCompound)element;
                  } while(!userCompound.func_74764_b("uuid"));

                  id = userCompound.func_74779_i("uuid");
               } while(id == null);

               convertedId = UUID.fromString(id);
            } while(convertedId == null);

            Map<UUID, Position> slavesPositions = new HashMap();
            if (userCompound.func_74764_b("positions")) {
               NBTTagList positionsTag = (NBTTagList)userCompound.func_74781_a("positions");
               Iterator var10 = positionsTag.iterator();

               while(var10.hasNext()) {
                  NBTBase mapPositionTag = (NBTBase)var10.next();
                  if (mapPositionTag instanceof NBTTagCompound) {
                     NBTTagCompound compoundMap = (NBTTagCompound)mapPositionTag;
                     if (compoundMap.func_74764_b("damselId")) {
                        String damselId = compoundMap.func_74779_i("damselId");
                        if (damselId != null) {
                           UUID convertedDamselId = UUID.fromString(damselId);
                           if (compoundMap.func_74764_b("position")) {
                              NBTTagCompound tagPos = compoundMap.func_74775_l("position");
                              if (tagPos != null) {
                                 Position newPos = new Position(tagPos);
                                 slavesPositions.put(convertedDamselId, newPos);
                              }
                           }
                        }
                     }
                  }
               }
            }

            this.instances.put(convertedId, slavesPositions);
         }
      }
   }

   public NBTTagCompound func_189551_b(NBTTagCompound compound) {
      NBTTagList list = new NBTTagList();
      Iterator var3 = this.instances.keySet().iterator();

      while(true) {
         UUID id;
         String convertedId;
         do {
            do {
               if (!var3.hasNext()) {
                  compound.func_74782_a("slaves_unload_positions", list);
                  return compound;
               }

               id = (UUID)var3.next();
            } while(id == null);

            convertedId = id.toString();
         } while(convertedId == null);

         NBTTagCompound userCompound = new NBTTagCompound();
         userCompound.func_74778_a("uuid", convertedId);
         NBTTagList positionsMapTag = new NBTTagList();
         Map<UUID, Position> positions = (Map)this.instances.get(id);
         if (positions != null) {
            Iterator var9 = positions.keySet().iterator();

            while(var9.hasNext()) {
               UUID damselID = (UUID)var9.next();
               NBTTagCompound tagMapPos = new NBTTagCompound();
               if (damselID != null) {
                  String convertedDamselId = damselID.toString();
                  if (convertedDamselId != null) {
                     Position pos = (Position)positions.get(damselID);
                     if (pos != null) {
                        NBTTagCompound tagPos = new NBTTagCompound();
                        tagPos = pos.writeToNBT(tagPos);
                        tagMapPos.func_74778_a("damselId", convertedDamselId);
                        tagMapPos.func_74782_a("position", tagPos);
                        positionsMapTag.func_74742_a(tagMapPos);
                     }
                  }
               }
            }
         }

         userCompound.func_74782_a("positions", positionsMapTag);
         list.func_74742_a(userCompound);
      }
   }

   public static SlavesUnloadLocationsData get(World world) {
      synchronized(lock) {
         MapStorage storage = world.func_175693_T();
         SlavesUnloadLocationsData instance = (SlavesUnloadLocationsData)storage.func_75742_a(SlavesUnloadLocationsData.class, "knapm_NPC_SLAVES_UNLOAD_LOCATIONS_DATA");
         if (instance == null) {
            instance = new SlavesUnloadLocationsData();
            storage.func_75745_a("knapm_NPC_SLAVES_UNLOAD_LOCATIONS_DATA", instance);
         }

         return instance;
      }
   }

   public void addPosition(UUID idPlayer, UUID idDamsel, Position pos) {
      synchronized(lock) {
         if (pos != null && idPlayer != null && idDamsel != null && this.instances != null) {
            Map<UUID, Position> positions = (Map)this.instances.get(idPlayer);
            if (positions == null) {
               positions = new HashMap();
               this.instances.put(idPlayer, positions);
            }

            ((Map)positions).put(idDamsel, pos);
            this.func_76185_a();
         }

      }
   }

   public void removePosition(UUID idPlayer, UUID idDamsel) {
      synchronized(lock) {
         if (idDamsel != null && idPlayer != null && this.instances != null) {
            Map<UUID, Position> positions = (Map)this.instances.get(idPlayer);
            if (positions == null) {
               positions = new HashMap();
               this.instances.put(idPlayer, positions);
            }

            if (((Map)positions).containsKey(idDamsel)) {
               ((Map)positions).remove(idDamsel);
               this.func_76185_a();
            }
         }

      }
   }

   public Set<Position> getPositionsCopy(UUID id) {
      synchronized(lock) {
         if (this.instances != null && id != null) {
            Map<UUID, Position> positions = (Map)this.instances.get(id);
            Set<Position> newPositions = new HashSet();
            if (positions != null) {
               Iterator var5 = positions.keySet().iterator();

               while(var5.hasNext()) {
                  UUID damselId = (UUID)var5.next();
                  if (damselId != null) {
                     Position pos = (Position)positions.get(damselId);
                     if (pos != null) {
                        newPositions.add(pos);
                     }
                  }
               }

               return newPositions;
            }
         }

         return Collections.emptySet();
      }
   }
}
