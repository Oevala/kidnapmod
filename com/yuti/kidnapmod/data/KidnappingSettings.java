package com.yuti.kidnapmod.data;

import com.yuti.kidnapmod.util.teleport.Position;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class KidnappingSettings {
   private Map<String, Position> kidnappingLocations = new HashMap();
   private boolean consentBoard = false;

   public Map<String, Position> getKidnappingLocations() {
      return this.kidnappingLocations;
   }

   public void readFromNBT(NBTTagCompound tag) {
      if (tag.func_74764_b("kidnappingLocations")) {
         NBTTagList list = (NBTTagList)tag.func_74781_a("kidnappingLocations");

         Position pos;
         String name;
         for(Iterator var3 = list.iterator(); var3.hasNext(); this.kidnappingLocations.put(name, pos)) {
            NBTBase element = (NBTBase)var3.next();
            NBTTagCompound posCompound = (NBTTagCompound)element;
            pos = new Position(posCompound);
            name = "";
            if (posCompound.func_74764_b("name")) {
               name = posCompound.func_74779_i("name");
            }
         }
      }

      if (tag.func_74764_b("consentBoard")) {
         this.consentBoard = tag.func_74767_n("consentBoard");
      }

   }

   public NBTTagCompound writeToNBT(NBTTagCompound tag) {
      NBTTagList list = new NBTTagList();
      Iterator var3 = this.kidnappingLocations.keySet().iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         NBTTagCompound tagLocation = new NBTTagCompound();
         tagLocation.func_74778_a("name", key);
         Position pos = (Position)this.kidnappingLocations.get(key);
         tagLocation = pos.writeToNBT(tagLocation);
         list.func_74742_a(tagLocation);
      }

      tag.func_74782_a("kidnappingLocations", list);
      tag.func_74757_a("consentBoard", this.consentBoard);
      return tag;
   }

   public boolean isOnConsentBoard() {
      return this.consentBoard;
   }

   public void setConsentBoard(boolean consentBoard) {
      this.consentBoard = consentBoard;
   }
}
