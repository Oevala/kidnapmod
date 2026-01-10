package com.yuti.kidnapmod.items;

import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemOnwerTarget extends Item implements IHasModel, ItemUsuableOnRestrainedPlayer {
   public ItemOnwerTarget(String name) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 1;
      ModItems.ITEMS.add(this);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }

   public ItemStack setOwner(ItemStack stack, EntityPlayer owner) {
      if (owner != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         UUID ownerId = owner.func_110124_au();
         String ownerName = owner.func_70005_c_();
         if (ownerId != null) {
            nbt.func_74778_a("ownerId", ownerId.toString());
         }

         nbt.func_74778_a("ownerName", ownerName);
         stack.func_77982_d(nbt);
      }

      return stack;
   }

   public ItemStack setOwnerId(ItemStack stack, UUID uuid) {
      if (uuid != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (uuid != null) {
            nbt.func_74778_a("ownerId", uuid.toString());
         }

         stack.func_77982_d(nbt);
      }

      return stack;
   }

   public ItemStack removeOwner(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      if (stack != null && nbt != null) {
         if (nbt.func_74764_b("ownerId")) {
            nbt.func_82580_o("ownerId");
         }

         if (nbt.func_74764_b("ownerName")) {
            nbt.func_82580_o("ownerName");
         }

         stack.func_77982_d(nbt);
      }

      return stack;
   }

   public ItemStack setTarget(ItemStack stack, EntityLivingBase target) {
      if (target != null && (target instanceof EntityPlayer || target instanceof I_Kidnapped)) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         UUID targetId = target.func_110124_au();
         String targetName = target.func_70005_c_();
         if (targetId != null) {
            nbt.func_74778_a("targetId", targetId.toString());
         }

         nbt.func_74778_a("targetName", targetName);
         stack.func_77982_d(nbt);
      }

      return stack;
   }

   public UUID getOwnerId(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      if (nbt.func_74764_b("ownerId")) {
         String uuid = nbt.func_74779_i("ownerId");
         if (uuid != null) {
            return UUID.fromString(uuid);
         }
      }

      return null;
   }

   public String getOwnerName(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("ownerName") ? nbt.func_74779_i("ownerName") : null;
   }

   public UUID getTargetId(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      if (nbt.func_74764_b("targetId")) {
         String uuid = nbt.func_74779_i("targetId");
         if (uuid != null) {
            return UUID.fromString(uuid);
         }
      }

      return null;
   }

   public String getTargetName(ItemStack itemstack) {
      NBTTagCompound nbt = Utils.getTagComponent(itemstack);
      return nbt.func_74764_b("targetName") ? nbt.func_74779_i("targetName") : null;
   }

   public boolean hasOwner(ItemStack stack) {
      return this.getOwnerId(stack) != null;
   }

   public boolean hasTarget(ItemStack stack) {
      return this.getTargetId(stack) != null;
   }

   public boolean isOwner(ItemStack stack, EntityPlayer player) {
      return player != null ? this.isOwner(stack, player.func_110124_au()) : false;
   }

   public boolean isOwner(ItemStack stack, UUID uuid) {
      if (uuid != null && stack != null) {
         UUID ownerUUID = this.getOwnerId(stack);
         return ownerUUID != null && uuid.equals(ownerUUID);
      } else {
         return false;
      }
   }

   public boolean isTarget(ItemStack stack, I_Kidnapped potentitalTarget) {
      if (potentitalTarget != null && stack != null) {
         UUID playerUUID = potentitalTarget.getKidnappedUniqueId();
         UUID targetUUID = this.getTargetId(stack);
         return playerUUID != null && targetUUID != null && playerUUID.equals(targetUUID);
      } else {
         return false;
      }
   }
}
