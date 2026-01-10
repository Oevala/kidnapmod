package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.models.render.ModelsUtils;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.handlers.rules.GameRulesRegistryHandler;
import com.yuti.kidnapmod.util.teleport.Position;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.ValueType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCollar extends ItemKidnapWearable implements IExtraBondageItem, IHasResistance, ItemUsuableOnRestrainedPlayer {
   private int baseResistance;
   private int mergePercent;
   private String gameRuleName;
   private String mergeGameRuleName;
   public static final String RESISTANCE_ID = "collar";

   public ItemCollar(String name, ExtraBondageMaterial materialIn, int baseResistanceIn, int mergePercentIn) {
      super(name, materialIn);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 1;
      ModItems.COLLAR_LIST.add(this);
      this.baseResistance = baseResistanceIn;
      this.mergePercent = mergePercentIn;
      this.gameRuleName = "collar_resistance_" + name;
      GameRulesRegistryHandler.preLoad(this.gameRuleName, Integer.toString(this.baseResistance), ValueType.NUMERICAL_VALUE);
      this.mergeGameRuleName = "collar_merge_percent_resistance_" + name;
      GameRulesRegistryHandler.preLoad(this.mergeGameRuleName, Integer.toString(this.mergePercent), ValueType.NUMERICAL_VALUE);
   }

   public ExtraBondageItemType getType(ItemStack itemstack) {
      return ExtraBondageItemType.COLLAR;
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getBondageModel(EntityLivingBase entity, ItemStack stack, ExtraBondageItemType slot, ModelBiped modelBase) {
      return ModelsUtils.MODEL_COLLAR;
   }

   public ItemStack addOwner(ItemStack stack, EntityPlayer owner) {
      UUID ownerId = owner.func_110124_au();
      String ownerName = owner.func_70005_c_();
      this.addOwner(stack, ownerId, ownerName);
      return stack;
   }

   public ItemStack addOwner(ItemStack stack, UUID id, String name) {
      if (id != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         List<UUID> currentOwners = this.getOwners(stack);
         if (!currentOwners.contains(id)) {
            NBTTagList list;
            if (!nbt.func_74764_b("owners")) {
               list = new NBTTagList();
               nbt.func_74782_a("owners", list);
            }

            list = (NBTTagList)nbt.func_74781_a("owners");
            NBTTagCompound ownerInfo = new NBTTagCompound();
            ownerInfo.func_74778_a("ownerId", id.toString());
            if (name != null) {
               ownerInfo.func_74778_a("ownerName", name);
            }

            list.func_74742_a(ownerInfo);
            nbt.func_74782_a("owners", list);
         }

         stack.func_77982_d(nbt);
      }

      this.addTargetException(stack, id, name);
      return stack;
   }

   protected ItemStack removeOwner(ItemStack stack, UUID id) {
      if (id != null && stack != null && stack.func_77973_b() instanceof ItemCollar) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt.func_74764_b("owners")) {
            NBTTagList list = (NBTTagList)nbt.func_74781_a("owners");
            Iterator it = list.iterator();

            while(it.hasNext()) {
               NBTBase tag = (NBTBase)it.next();
               if (tag instanceof NBTTagCompound) {
                  NBTTagCompound nbtCompound = (NBTTagCompound)tag;
                  if (nbtCompound.func_74764_b("ownerId")) {
                     String targetStringId = nbtCompound.func_74779_i("ownerId");
                     if (targetStringId != null) {
                        UUID targetId = UUID.fromString(targetStringId);
                        if (targetId != null && targetId.equals(id)) {
                           it.remove();
                           this.removeTargetException(stack, id);
                        }
                     }
                  }
               }
            }
         }
      }

      return stack;
   }

   public ItemStack removeOwner(ItemStack stack, EntityPlayer owner) {
      UUID ownerId = owner.func_110124_au();
      this.removeOwner(stack, ownerId);
      return stack;
   }

   public List<UUID> getOwners(ItemStack stack) {
      List<UUID> owners = new ArrayList();
      if (stack != null && stack.func_77973_b() instanceof ItemCollar) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt.func_74764_b("owners")) {
            NBTTagList list = (NBTTagList)nbt.func_74781_a("owners");
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
               NBTBase tag = (NBTBase)var5.next();
               if (tag instanceof NBTTagCompound) {
                  NBTTagCompound nbtCompound = (NBTTagCompound)tag;
                  if (nbtCompound.func_74764_b("ownerId")) {
                     String ownerId = nbtCompound.func_74779_i("ownerId");
                     if (ownerId != null) {
                        owners.add(UUID.fromString(ownerId));
                     }
                  }
               }
            }
         }
      }

      return owners;
   }

   public List<String> getOwnersNames(ItemStack stack) {
      List<String> ownerNames = new ArrayList();
      if (stack != null && stack.func_77973_b() instanceof ItemCollar) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt.func_74764_b("owners")) {
            NBTTagList list = (NBTTagList)nbt.func_74781_a("owners");
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
               NBTBase tag = (NBTBase)var5.next();
               if (tag instanceof NBTTagCompound) {
                  NBTTagCompound nbtCompound = (NBTTagCompound)tag;
                  if (nbtCompound.func_74764_b("ownerName")) {
                     String ownerName = nbtCompound.func_74779_i("ownerName");
                     if (ownerName != null) {
                        ownerNames.add(ownerName);
                     }
                  }
               }
            }
         }
      }

      return ownerNames;
   }

   public ItemStack setNickname(ItemStack stack, String nickname) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74778_a("nickname", nickname);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack lockCollar(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("locked", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack unlockCollar(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("locked", false);
      stack.func_77982_d(nbt);
      stack = this.resetCurrentResistance(stack);
      return stack;
   }

   public ItemStack setPrisonFencee(ItemStack stack, boolean enabled) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("prisonFence", enabled);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack setBlackListState(ItemStack stack, boolean enabled) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("blackliststate", enabled);
      stack.func_77982_d(nbt);
      return stack;
   }

   public List<UUID> getTargetExceptions(ItemStack stack) {
      return this.getList(stack, "targetexception", "targetID");
   }

   public List<String> getTargetExceptionNames(ItemStack stack) {
      return this.getNamesFromList(stack, "targetexception", "targetName");
   }

   public ItemStack addTargetException(ItemStack stack, UUID id, String name) {
      return this.addInList(stack, id, name, "targetexception", "targetID", "targetName");
   }

   public ItemStack addTargetException(ItemStack stack, EntityPlayer owner) {
      return this.addInList(stack, owner, "targetexception", "targetID", "targetName");
   }

   public ItemStack removeTargetException(ItemStack stack, String name) {
      return this.removeInList(stack, name, "targetexception", "targetName");
   }

   public synchronized ItemStack removeTargetException(ItemStack stack, UUID id) {
      return this.removeInList(stack, id, "targetexception", "targetID");
   }

   public boolean isTargetException(ItemStack stack, UUID id) {
      if (id != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            List<UUID> currentExceptions = this.getTargetExceptions(stack);
            return currentExceptions != null && currentExceptions.contains(id);
         }
      }

      return false;
   }

   public boolean isTargetException(ItemStack stack, EntityLivingBase entity) {
      if (entity != null) {
         UUID entityID = entity.func_110124_au();
         return this.isTargetException(stack, entityID);
      } else {
         return false;
      }
   }

   public boolean isTargetExceptionByName(ItemStack stack, String name) {
      if (name == null) {
         return false;
      } else {
         List<String> names = this.getTargetExceptionNames(stack);
         return names != null && names.contains(name);
      }
   }

   public List<UUID> getBlacklist(ItemStack stack) {
      return this.getList(stack, "blacklist", "targetID");
   }

   public List<String> getBlacklistNames(ItemStack stack) {
      return this.getNamesFromList(stack, "blacklist", "targetName");
   }

   public ItemStack addToBlackList(ItemStack stack, UUID id, String name) {
      return this.addInList(stack, id, name, "blacklist", "targetID", "targetName");
   }

   public ItemStack addToBlackList(ItemStack stack, EntityPlayer owner) {
      return this.addInList(stack, owner, "blacklist", "targetID", "targetName");
   }

   public ItemStack removeFromBlackList(ItemStack stack, String name) {
      return this.removeInList(stack, name, "blacklist", "targetName");
   }

   public synchronized ItemStack removeFromBlackList(ItemStack stack, UUID id) {
      return this.removeInList(stack, id, "blacklist", "targetID");
   }

   public boolean isOnBlackList(ItemStack stack, UUID id) {
      if (id != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            List<UUID> currentblacklist = this.getBlacklist(stack);
            return currentblacklist != null && currentblacklist.contains(id);
         }
      }

      return false;
   }

   public boolean isOnBlackList(ItemStack stack, EntityLivingBase entity) {
      if (entity != null) {
         UUID entityID = entity.func_110124_au();
         return this.isOnBlackList(stack, entityID);
      } else {
         return false;
      }
   }

   public boolean isOnBlackListByName(ItemStack stack, String name) {
      if (name == null) {
         return false;
      } else {
         List<String> names = this.getBlacklistNames(stack);
         return names != null && names.contains(name);
      }
   }

   private List<UUID> getList(ItemStack stack, String key, String idKey) {
      List<UUID> ids = new ArrayList();
      if (stack != null && stack.func_77973_b() instanceof ItemCollar) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt.func_74764_b(key)) {
            NBTTagList list = (NBTTagList)nbt.func_74781_a(key);
            Iterator var7 = list.iterator();

            while(var7.hasNext()) {
               NBTBase tag = (NBTBase)var7.next();
               if (tag instanceof NBTTagCompound) {
                  NBTTagCompound nbtCompound = (NBTTagCompound)tag;
                  if (nbtCompound.func_74764_b(idKey)) {
                     String targetID = nbtCompound.func_74779_i(idKey);
                     if (targetID != null) {
                        ids.add(UUID.fromString(targetID));
                     }
                  }
               }
            }
         }
      }

      return ids;
   }

   private List<String> getNamesFromList(ItemStack stack, String key, String nameKey) {
      List<String> names = new ArrayList();
      if (stack != null && stack.func_77973_b() instanceof ItemCollar) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt.func_74764_b(key)) {
            NBTTagList list = (NBTTagList)nbt.func_74781_a(key);
            Iterator var7 = list.iterator();

            while(var7.hasNext()) {
               NBTBase tag = (NBTBase)var7.next();
               if (tag instanceof NBTTagCompound) {
                  NBTTagCompound nbtCompound = (NBTTagCompound)tag;
                  if (nbtCompound.func_74764_b(nameKey)) {
                     String targetName = nbtCompound.func_74779_i(nameKey);
                     if (targetName != null) {
                        names.add(targetName);
                     }
                  }
               }
            }
         }
      }

      return names;
   }

   private ItemStack addInList(ItemStack stack, UUID id, String name, String key, String idKey, String nameKey) {
      if (id != null) {
         List<UUID> current = this.getList(stack, key, idKey);
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (current == null || current != null && !current.contains(id)) {
            NBTTagList list;
            if (!nbt.func_74764_b(key)) {
               list = new NBTTagList();
               nbt.func_74782_a(key, list);
            }

            list = (NBTTagList)nbt.func_74781_a(key);
            NBTTagCompound targetInfo = new NBTTagCompound();
            targetInfo.func_74778_a(idKey, id.toString());
            if (name != null) {
               targetInfo.func_74778_a(nameKey, name);
            }

            list.func_74742_a(targetInfo);
            nbt.func_74782_a(key, list);
         }

         stack.func_77982_d(nbt);
      }

      return stack;
   }

   private ItemStack addInList(ItemStack stack, EntityPlayer owner, String key, String idKey, String nameKey) {
      UUID targetId = owner.func_110124_au();
      String targetName = owner.func_70005_c_();
      this.addInList(stack, targetId, targetName, key, idKey, nameKey);
      return stack;
   }

   private ItemStack removeInList(ItemStack stack, String name, String key, String nameKey) {
      if (stack != null && stack.func_77973_b() instanceof ItemCollar) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt.func_74764_b(key)) {
            NBTTagList list = (NBTTagList)nbt.func_74781_a(key);
            Iterator it = list.iterator();

            while(it.hasNext()) {
               NBTBase tag = (NBTBase)it.next();
               if (tag instanceof NBTTagCompound) {
                  NBTTagCompound nbtCompound = (NBTTagCompound)tag;
                  if (nbtCompound.func_74764_b(nameKey)) {
                     String targetName = nbtCompound.func_74779_i(nameKey);
                     if (targetName != null && targetName.equals(name)) {
                        it.remove();
                     }
                  }
               }
            }
         }
      }

      return stack;
   }

   private synchronized ItemStack removeInList(ItemStack stack, UUID id, String key, String idKey) {
      if (id != null && stack != null && stack.func_77973_b() instanceof ItemCollar) {
         String idString = id.toString();
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (idString != null && nbt.func_74764_b(key)) {
            NBTTagList list = (NBTTagList)nbt.func_74781_a(key);
            Iterator it = list.iterator();

            while(it.hasNext()) {
               NBTBase tag = (NBTBase)it.next();
               if (tag instanceof NBTTagCompound) {
                  NBTTagCompound nbtCompound = (NBTTagCompound)tag;
                  if (nbtCompound.func_74764_b(idKey)) {
                     String targetId = nbtCompound.func_74779_i(idKey);
                     if (targetId != null && targetId.equals(idString)) {
                        it.remove();
                     }
                  }
               }
            }
         }
      }

      return stack;
   }

   public ItemStack enableKidnappingMode(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("kidnappingmode", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack disableKidnappingMode(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("kidnappingmode", false);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isKidnappingModeEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("kidnappingmode") ? nbt.func_74767_n("kidnappingmode") : false;
   }

   public ItemStack enableBondageService(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("bondageservice", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack disableBondageService(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("bondageservice", false);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean isBondageServiceEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("bondageservice") ? nbt.func_74767_n("bondageservice") : false;
   }

   public ItemStack enableWarnMasters(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("warnmasters", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack disableWarnMasters(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("warnmasters", false);
      stack.func_77982_d(nbt);
      return stack;
   }

   public boolean shouldWarnMasters(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("warnmasters") ? nbt.func_74767_n("warnmasters") : true;
   }

   public ItemStack setPrison(ItemStack stack, Position pos) {
      return this.setTagPosition(stack, "prison", pos);
   }

   public ItemStack setPrisonRadius(ItemStack stack, int radius) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      if (nbt != null) {
         if (radius < 3) {
            radius = 3;
         } else if (radius > 1000) {
            radius = 1000;
         }

         nbt.func_74768_a("prisonRadius", radius);
         stack.func_77982_d(nbt);
      }

      return stack;
   }

   public int getPrisonRadius(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      if (nbt != null && nbt.func_74764_b("prisonRadius")) {
         int radius = nbt.func_74762_e("prisonRadius");
         if (radius >= 0) {
            return nbt.func_74762_e("prisonRadius");
         }
      }

      return 10;
   }

   public ItemStack setHome(ItemStack stack, Position pos) {
      return this.setTagPosition(stack, "home", pos);
   }

   public ItemStack setBackHome(ItemStack stack, boolean backHome) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      if (nbt != null) {
         nbt.func_74757_a("backHome", backHome);
         stack.func_77982_d(nbt);
      }

      return stack;
   }

   public boolean shouldBackHome(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      return nbt != null && nbt.func_74764_b("backHome") ? nbt.func_74767_n("backHome") : true;
   }

   private ItemStack setTagPosition(ItemStack stack, String key, Position pos) {
      if (key != null && pos != null) {
         NBTTagCompound info = new NBTTagCompound();
         info = pos.writeToNBT(info);
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         nbt.func_74782_a(key, info);
         stack.func_77982_d(nbt);
      }

      return stack;
   }

   private Position getPosFromTag(ItemStack stack, String key) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      if (nbt.func_74764_b(key)) {
         NBTTagCompound infoTag = (NBTTagCompound)nbt.func_74781_a(key);
         Position pos = new Position(infoTag);
         return pos;
      } else {
         return null;
      }
   }

   public Position getPrison(ItemStack stack) {
      return this.getPosFromTag(stack, "prison");
   }

   public ItemStack removePrison(ItemStack stack) {
      return this.removeTag(stack, "prison");
   }

   public Position getHome(ItemStack stack) {
      return this.getPosFromTag(stack, "home");
   }

   public ItemStack removeHome(ItemStack stack) {
      return this.removeTag(stack, "home");
   }

   public ItemStack setServiceSentence(ItemStack stack, String url) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74778_a("servicesentence", url);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack removeServiceSentence(ItemStack stack) {
      return this.removeTag(stack, "servicesentence");
   }

   public String getServiceSentence(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      return nbt.func_74764_b("servicesentence") ? nbt.func_74779_i("servicesentence") : null;
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      if (stack.func_77942_o()) {
         NBTTagCompound nbt = stack.func_77978_p();
         if (nbt.func_74764_b("nickname")) {
            tooltip.add(ChatFormatting.BLUE + nbt.func_74779_i("nickname"));
         }

         List<String> owners = this.getOwnersNames(stack);
         if (!owners.isEmpty()) {
            if (owners.size() > 1) {
               tooltip.add(ChatFormatting.DARK_PURPLE + "Owners : ");
               Iterator var7 = owners.iterator();

               while(var7.hasNext()) {
                  String ownerName = (String)var7.next();
                  tooltip.add(ChatFormatting.DARK_PURPLE + ownerName);
               }
            } else {
               tooltip.add(ChatFormatting.DARK_PURPLE + "Owner : " + (String)owners.get(0));
            }
         } else {
            tooltip.add(ChatFormatting.GRAY + "Need to be claimed (use /collar claim)");
         }

         if (this.isLocked(stack)) {
            tooltip.add(ChatFormatting.DARK_RED + "Locked");
         } else {
            tooltip.add(ChatFormatting.GREEN + "Unlocked");
         }

         if (this.isKidnappingModeEnabled(stack)) {
            tooltip.add(ChatFormatting.DARK_RED + "Kidnapping mode");
            String name;
            List blacklistNames;
            Iterator var13;
            if (!this.isBlacklistenabled(stack)) {
               blacklistNames = this.getTargetExceptionNames(stack);
               if (!blacklistNames.isEmpty()) {
                  tooltip.add(ChatFormatting.BLUE + "Whitelist : ");
                  var13 = blacklistNames.iterator();

                  while(var13.hasNext()) {
                     name = (String)var13.next();
                     tooltip.add(ChatFormatting.BLUE + name);
                  }
               }
            } else {
               blacklistNames = this.getBlacklistNames(stack);
               if (!blacklistNames.isEmpty()) {
                  tooltip.add(ChatFormatting.BLUE + "Blacklist : ");
                  var13 = blacklistNames.iterator();

                  while(var13.hasNext()) {
                     name = (String)var13.next();
                     tooltip.add(ChatFormatting.BLUE + name);
                  }
               }
            }
         }

         if (this.isBondageServiceEnabled(stack)) {
            tooltip.add(ChatFormatting.GREEN + "Bondage service");
         }

         Position prison = this.getPrison(stack);
         if (prison != null) {
            tooltip.add(ChatFormatting.GREEN + "Prison : Dimension " + prison.getDimension() + ", X : " + prison.getX() + ", Y : " + prison.getY() + ", Z : " + prison.getZ());
         }

         Position home = this.getHome(stack);
         if (home != null) {
            tooltip.add(ChatFormatting.GREEN + "Home : Dimension " + home.getDimension() + ", X : " + home.getX() + ", Y : " + home.getY() + ", Z : " + home.getZ());
         }
      } else {
         tooltip.add(ChatFormatting.GRAY + "Need to be claimed (use /collar claim)");
      }

      if (worldIn != null) {
         int resistance = Utils.getResistance(stack, worldIn);
         if (resistance > 0) {
            tooltip.add(ChatFormatting.GREEN + "Resistance : " + resistance);
         }
      }

      super.func_77624_a(stack, worldIn, tooltip, flagIn);
   }

   public boolean canUnequip(ItemStack itemstack, EntityLivingBase player) {
      return this.isLocked(itemstack) ? false : super.canUnequip(itemstack, player);
   }

   public boolean isLocked(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("locked") ? nbt.func_74767_n("locked") : false;
   }

   public boolean isPrisonFenceEnabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("prisonFence") ? nbt.func_74767_n("prisonFence") : false;
   }

   public boolean isBlacklistenabled(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("blackliststate") ? nbt.func_74767_n("blackliststate") : false;
   }

   public String getNickname(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("nickname") ? nbt.func_74779_i("nickname") : null;
   }

   public boolean hasOwner(ItemStack stack) {
      return this.getOwners(stack).size() > 0;
   }

   public boolean hasNickname(ItemStack stack) {
      return this.getNickname(stack) != null;
   }

   public boolean isOwner(ItemStack stack, EntityPlayer player) {
      return player != null ? this.isOwner(stack, player.func_110124_au()) : false;
   }

   public boolean isOwner(ItemStack stack, UUID uuid) {
      if (uuid != null && stack != null) {
         List<UUID> owners = this.getOwners(stack);
         if (owners != null) {
            return owners.contains(uuid);
         }
      }

      return false;
   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase living) {
      if (living instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)living;
         if (!this.hasOwner(itemstack) && player != null && player instanceof EntityPlayer) {
            this.addOwner(itemstack, player);
         }

         player.refreshDisplayName();
      }

      super.onEquipped(itemstack, living);
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      if (player.field_70170_p.field_72995_K) {
         return true;
      } else {
         if (target instanceof EntityPlayer || target instanceof I_Kidnapped) {
            I_Kidnapped state = null;
            if (target instanceof EntityPlayer) {
               EntityPlayer targetPlayer = (EntityPlayer)target;
               state = PlayerBindState.getInstance(targetPlayer);
            } else {
               state = (I_Kidnapped)target;
            }

            ItemStack newStack = stack.func_77946_l();
            if (!this.hasOwner(newStack)) {
               this.addOwner(newStack, player);
            }

            if (state != null && ((I_Kidnapped)state).isTiedUp()) {
               if (!((I_Kidnapped)state).hasCollar()) {
                  ((I_Kidnapped)state).putCollarOn(newStack);
                  stack.func_190918_g(1);
               } else {
                  ItemStack oldCollarStack = ((I_Kidnapped)state).getCurrentCollar();
                  if (oldCollarStack != null && oldCollarStack.func_77973_b() instanceof ItemCollar) {
                     if (!this.isLocked(oldCollarStack)) {
                        oldCollarStack = ((I_Kidnapped)state).replaceCollar(newStack);
                        if (oldCollarStack != null) {
                           stack.func_190918_g(1);
                           ((I_Kidnapped)state).kidnappedDropItem(oldCollarStack);
                        }
                     } else {
                        Utils.sendErrorMessageToEntity(player, "The collar is locked");
                     }
                  }
               }
            }
         }

         return true;
      }
   }

   public int getBaseResistance(World world) {
      GameRules rules = world.func_82736_K();
      return rules.func_82765_e(this.gameRuleName) ? rules.func_180263_c(this.gameRuleName) : this.baseResistance;
   }

   public int getMergePercentRule(World world) {
      GameRules rules = world.func_82736_K();
      return rules.func_82765_e(this.mergeGameRuleName) ? rules.func_180263_c(this.mergeGameRuleName) : this.mergePercent;
   }

   public String getResistanceId() {
      return "collar";
   }

   public boolean canBeStruggledOut(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      return nbt.func_74764_b("struggle") ? nbt.func_74767_n("struggle") : true;
   }

   public ItemStack setCanBeStruggledOut(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("struggle", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public void notifyStruggle(EntityPlayer player) {
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase living) {
      EntityPlayer player = (EntityPlayer)living;
      player.refreshDisplayName();
      this.resetCurrentResistance(itemstack);
      super.onUnequipped(itemstack, player);
   }

   public ItemStack setCurrentResistance(ItemStack stack, int resistance) {
      if (stack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            nbt.func_74768_a("currentresistance", resistance);
            stack.func_77982_d(nbt);
         }
      }

      return stack;
   }

   public int getCurrentResistance(ItemStack stack, World world) {
      if (stack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("currentresistance")) {
            int resistance = nbt.func_74762_e("currentresistance");
            if (resistance > 0) {
               return resistance;
            }
         }
      }

      return Utils.getResistance(stack, world);
   }

   public ItemStack resetCurrentResistance(ItemStack stack) {
      if (stack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("currentresistance")) {
            nbt.func_82580_o("currentresistance");
            if (nbt.func_186856_d() == 0) {
               stack.func_77982_d((NBTTagCompound)null);
            } else {
               stack.func_77982_d(nbt);
            }
         }
      }

      return stack;
   }

   public boolean containsAllOwners(ItemStack stackToCheck, ItemStack stackBase) {
      if (stackToCheck != null && stackBase != null) {
         List<UUID> ownersCheck = this.getOwners(stackToCheck);
         List<UUID> ownersContainer = this.getOwners(stackBase);
         if (ownersCheck != null && ownersContainer != null) {
            Iterator var5 = ownersContainer.iterator();

            while(var5.hasNext()) {
               UUID owner = (UUID)var5.next();
               if (!ownersCheck.contains(owner)) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public ItemStack updateCollarStackFromNetwork(ItemStack stackFrom, ItemStack stackTo, Position positionCheck) {
      if (stackFrom != null && stackTo != null) {
         NBTTagCompound tagFrom = Utils.getTagComponent(stackFrom);
         NBTTagCompound tagTo = Utils.getTagComponent(stackTo);
         if (tagFrom != null && tagTo != null && tagFrom != null && tagTo != null) {
            if (tagFrom.func_74764_b("bondageservice")) {
               tagTo.func_74782_a("bondageservice", tagFrom.func_74781_a("bondageservice"));
            }

            if (tagFrom.func_74764_b("kidnappingmode")) {
               tagTo.func_74782_a("kidnappingmode", tagFrom.func_74781_a("kidnappingmode"));
            }

            if (tagFrom.func_74764_b("warnmasters")) {
               tagTo.func_74782_a("warnmasters", tagFrom.func_74781_a("warnmasters"));
            }

            if (tagFrom.func_74764_b("backHome")) {
               tagTo.func_74782_a("backHome", tagFrom.func_74781_a("backHome"));
            }

            if (tagFrom.func_74764_b("prisonFence")) {
               tagTo.func_74782_a("prisonFence", tagFrom.func_74781_a("prisonFence"));
            }

            Position prison;
            if (tagFrom.func_74764_b("home")) {
               prison = this.getHome(stackFrom);
               if (prison != null && positionCheck != null && prison.equals(positionCheck)) {
                  tagTo.func_74782_a("home", tagFrom.func_74781_a("home"));
               }
            }

            if (tagFrom.func_74764_b("prison")) {
               prison = this.getPrison(stackFrom);
               if (prison != null && positionCheck != null && prison.equals(positionCheck)) {
                  tagTo.func_74782_a("prison", tagFrom.func_74781_a("prison"));
               }
            }

            if (tagFrom.func_74764_b("prisonRadius")) {
               stackTo = this.setPrisonRadius(stackTo, this.getPrisonRadius(stackFrom));
            }

            if (tagFrom.func_74764_b("blackliststate")) {
               tagTo.func_74782_a("blackliststate", tagFrom.func_74781_a("blackliststate"));
            }

            if (tagFrom.func_74764_b("targetexception")) {
               tagTo.func_74782_a("targetexception", tagFrom.func_74781_a("targetexception"));
            }

            if (tagFrom.func_74764_b("blacklist")) {
               tagTo.func_74782_a("blacklist", tagFrom.func_74781_a("blacklist"));
            }

            if (tagFrom.func_74764_b("owners") && this.containsAllOwners(stackFrom, stackTo)) {
               tagTo.func_74782_a("owners", tagFrom.func_74781_a("owners"));
            }

            stackTo.func_77982_d(tagTo);
         }
      }

      return stackTo;
   }
}
