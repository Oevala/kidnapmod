package com.yuti.kidnapmod.capabilities;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.IAdjustable;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.capabilities.PacketCapabilityKidnappingSettings;
import com.yuti.kidnapmod.util.Utils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.INBTSerializable;

public class KidnapSettingsCapabilities implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {
   public Map<String, Float> mapAdjustements = new HashMap();
   public boolean allowClothes = true;
   public boolean allowDynamicRestraints = true;
   public boolean displayCollar = true;
   public boolean allowChangingClothes = true;
   public boolean allowTransparentSkin = true;
   private EntityPlayer player;

   public static void register() {
      CapabilityManager.INSTANCE.register(KidnapSettingsCapabilities.class, new KidnapSettingsCapabilities.Storage(), new KidnapSettingsCapabilities.Factory());
   }

   public KidnapSettingsCapabilities(EntityPlayer player) {
      this.player = player;
   }

   public void sync() {
      PacketCapabilityKidnappingSettings packet = new PacketCapabilityKidnappingSettings(this.player.func_145782_y(), this);
      if (!this.player.field_70170_p.field_72995_K) {
         List<EntityPlayerMP> players = Utils.getWorldPlayers(this.player.field_70170_p);
         if (players != null) {
            Iterator var3 = players.iterator();

            while(var3.hasNext()) {
               EntityPlayerMP playerTarget = (EntityPlayerMP)var3.next();
               PacketHandler.INSTANCE.sendTo(packet, playerTarget);
            }
         }
      }

   }

   public void syncwith(EntityPlayerMP target) {
      if (this.player != null && target != null) {
         PacketCapabilityKidnappingSettings packetPlayer = new PacketCapabilityKidnappingSettings(this.player.func_145782_y(), this);
         KidnapSettingsCapabilities capTarget = (KidnapSettingsCapabilities)target.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
         if (capTarget != null) {
            PacketCapabilityKidnappingSettings packetTarget = new PacketCapabilityKidnappingSettings(target.func_145782_y(), capTarget);
            if (!this.player.field_70170_p.field_72995_K) {
               PacketHandler.INSTANCE.sendTo(packetTarget, (EntityPlayerMP)this.player);
               PacketHandler.INSTANCE.sendTo(packetPlayer, target);
            }

         }
      }
   }

   public static void syncAllJoin(EntityPlayerMP playerJoin) {
      KidnapSettingsCapabilities cap = (KidnapSettingsCapabilities)playerJoin.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
      if (cap != null) {
         cap.sync();
      }

      List<EntityPlayerMP> players = Utils.getWorldPlayers(playerJoin.field_70170_p);
      if (players != null) {
         Iterator var4 = players.iterator();

         while(var4.hasNext()) {
            EntityPlayerMP playerTarget = (EntityPlayerMP)var4.next();
            KidnapSettingsCapabilities capPlayer = (KidnapSettingsCapabilities)playerTarget.getCapability(KidnapCapabilities.KIDNAP_SETTINGS, (EnumFacing)null);
            if (capPlayer != null) {
               PacketCapabilityKidnappingSettings packet = new PacketCapabilityKidnappingSettings(playerTarget.func_145782_y(), capPlayer);
               PacketHandler.INSTANCE.sendTo(packet, playerJoin);
            }
         }
      }

   }

   public float getAdjustement(String itemId) {
      return this.mapAdjustements != null && this.mapAdjustements.containsKey(itemId) ? (Float)this.mapAdjustements.get(itemId) : 0.0F;
   }

   public Map<String, Float> getMapAdjustements() {
      return this.mapAdjustements;
   }

   public void setMapAdjustements(Map<String, Float> mapAdjustements) {
      this.mapAdjustements = mapAdjustements;
   }

   public void setAdjustement(String itemId, float value) {
      if (this.mapAdjustements != null) {
         this.mapAdjustements.put(itemId, this.getRightValue(value));
      }

   }

   public void setAll(float value) {
      this.setAllGags(value);
      this.setAllBlindfolds(value);
   }

   public void setAllGags(float value) {
      float realValue = this.getRightValue(value);
      List<ItemGag> items = ModItems.GAG_LIST;
      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         if (item instanceof IAdjustable) {
            IAdjustable itemAdjustable = (IAdjustable)item;
            if (itemAdjustable.canBeAdjusted()) {
               String itemId = item.getRegistryName().toString();
               this.setAdjustement(itemId, realValue);
            }
         }
      }

   }

   public void setAllBlindfolds(float value) {
      float realValue = this.getRightValue(value);
      List<ItemBlindfold> items = ModItems.BLINDFOLD_LIST;
      Iterator var4 = items.iterator();

      while(var4.hasNext()) {
         Item item = (Item)var4.next();
         if (item instanceof IAdjustable) {
            IAdjustable itemAdjustable = (IAdjustable)item;
            if (itemAdjustable.canBeAdjusted()) {
               String itemId = item.getRegistryName().toString();
               this.setAdjustement(itemId, realValue);
            }
         }
      }

   }

   public boolean isAllowingClothes() {
      return this.allowClothes;
   }

   public void setAllowClothes(boolean allowClothes) {
      this.allowClothes = allowClothes;
   }

   public boolean displayCollar() {
      return this.displayCollar;
   }

   public void setDisplayCollar(boolean displayCollar) {
      this.displayCollar = displayCollar;
   }

   public boolean isAllowingDynamicRestraints() {
      return this.allowDynamicRestraints;
   }

   public void setAllowDynamicRestraints(boolean allowDynamicRestraints) {
      this.allowDynamicRestraints = allowDynamicRestraints;
   }

   public boolean isAllowingChangingClothes() {
      return this.allowChangingClothes;
   }

   public void setAllowChangingClothes(boolean allowChangingClothes) {
      this.allowChangingClothes = allowChangingClothes;
   }

   public boolean isAllowingTransparentSkin() {
      return this.allowTransparentSkin;
   }

   public void setAllowTransparentSkin(boolean allowTransparentSkin) {
      this.allowTransparentSkin = allowTransparentSkin;
   }

   public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
      return KidnapCapabilities.KIDNAP_SETTINGS != null && capability == KidnapCapabilities.KIDNAP_SETTINGS;
   }

   public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
      return KidnapCapabilities.KIDNAP_SETTINGS != null && capability == KidnapCapabilities.KIDNAP_SETTINGS ? this : null;
   }

   public NBTTagCompound serializeNBT() {
      NBTTagCompound compound = new NBTTagCompound();
      if (this.mapAdjustements != null) {
         NBTTagCompound adjustements = new NBTTagCompound();
         Iterator var3 = this.mapAdjustements.keySet().iterator();

         while(var3.hasNext()) {
            String itemId = (String)var3.next();
            adjustements.func_74776_a(itemId, (Float)this.mapAdjustements.get(itemId));
         }

         compound.func_74782_a("adjustements", adjustements);
      }

      compound.func_74757_a("allowClothes", this.allowClothes);
      compound.func_74757_a("allowDynamicRestraints", this.allowDynamicRestraints);
      compound.func_74757_a("allowChangingClothes", this.allowChangingClothes);
      compound.func_74757_a("allowTransparentSkin", this.allowTransparentSkin);
      compound.func_74757_a("displayCollar", this.displayCollar);
      return compound;
   }

   public void deserializeNBT(NBTTagCompound compound) {
      this.mapAdjustements = new HashMap();
      if (compound.func_74764_b("adjustements")) {
         NBTBase adjustementsTag = compound.func_74781_a("adjustements");
         if (adjustementsTag instanceof NBTTagCompound) {
            NBTTagCompound adjustements = (NBTTagCompound)adjustementsTag;
            Set<String> keys = adjustements.func_150296_c();
            if (keys != null) {
               Iterator var5 = keys.iterator();

               while(var5.hasNext()) {
                  String key = (String)var5.next();
                  float value = adjustements.func_74760_g(key);
                  this.mapAdjustements.put(key, this.getRightValue(value));
               }
            }
         }
      }

      if (compound.func_74764_b("allowClothes")) {
         this.allowClothes = compound.func_74767_n("allowClothes");
      } else {
         this.allowClothes = true;
      }

      if (compound.func_74764_b("allowChangingClothes")) {
         this.allowChangingClothes = compound.func_74767_n("allowChangingClothes");
      } else {
         this.allowChangingClothes = true;
      }

      if (compound.func_74764_b("allowDynamicRestraints")) {
         this.allowDynamicRestraints = compound.func_74767_n("allowDynamicRestraints");
      } else {
         this.allowDynamicRestraints = true;
      }

      if (compound.func_74764_b("allowTransparentSkin")) {
         this.allowTransparentSkin = compound.func_74767_n("allowTransparentSkin");
      } else {
         this.allowTransparentSkin = true;
      }

      if (compound.func_74764_b("displayCollar")) {
         this.displayCollar = compound.func_74767_n("displayCollar");
      } else {
         this.displayCollar = true;
      }

   }

   public float getRightValue(float value) {
      if (value > 4.0F) {
         return 4.0F;
      } else {
         return value < -4.0F ? -4.0F : value;
      }
   }

   public static class Factory implements Callable<KidnapSettingsCapabilities> {
      public KidnapSettingsCapabilities call() throws Exception {
         return null;
      }
   }

   public static class Storage implements IStorage<KidnapSettingsCapabilities> {
      public NBTBase writeNBT(Capability<KidnapSettingsCapabilities> capability, KidnapSettingsCapabilities instance, EnumFacing side) {
         return instance.serializeNBT();
      }

      public void readNBT(Capability<KidnapSettingsCapabilities> capability, KidnapSettingsCapabilities instance, EnumFacing side, NBTBase nbt) {
         instance.deserializeNBT((NBTTagCompound)nbt);
      }
   }
}
