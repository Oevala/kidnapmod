package com.yuti.kidnapmod.tileentity;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBed;

public class TileEntityTrappedBed extends TileEntityBed implements ITileEntityBondageItemHolder {
   private ItemStack bind;
   private ItemStack gag;
   private ItemStack blindfold;
   private ItemStack earplugs;
   private ItemStack collar;
   private ItemStack clothes;

   public TileEntityTrappedBed() {
   }

   public TileEntityTrappedBed(boolean offModeIn) {
   }

   public ItemStack getBind() {
      return this.bind;
   }

   public void setBind(ItemStack bind) {
      this.bind = bind;
      this.func_70296_d();
   }

   public ItemStack getGag() {
      return this.gag;
   }

   public void setGag(ItemStack gag) {
      this.gag = gag;
      this.func_70296_d();
   }

   public ItemStack getBlindfold() {
      return this.blindfold;
   }

   public void setBlindfold(ItemStack blindfold) {
      this.blindfold = blindfold;
      this.func_70296_d();
   }

   public ItemStack getEarplugs() {
      return this.earplugs;
   }

   public void setEarplugs(ItemStack earplugs) {
      this.earplugs = earplugs;
      this.func_70296_d();
   }

   public ItemStack getClothes() {
      return this.clothes;
   }

   public void setClothes(ItemStack clothes) {
      this.clothes = clothes;
      this.func_70296_d();
   }

   public ItemStack getCollar() {
      return this.collar;
   }

   public void setCollar(ItemStack collar) {
      this.collar = collar;
      this.func_70296_d();
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      this.setBondageItemsValues(compound);
   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      NBTTagCompound tagCollar;
      if (this.bind != null) {
         tagCollar = new NBTTagCompound();
         compound.func_74782_a("bind", this.bind.func_77955_b(tagCollar));
      }

      if (this.gag != null) {
         tagCollar = new NBTTagCompound();
         compound.func_74782_a("gag", this.gag.func_77955_b(tagCollar));
      }

      if (this.blindfold != null) {
         tagCollar = new NBTTagCompound();
         compound.func_74782_a("blindfold", this.blindfold.func_77955_b(tagCollar));
      }

      if (this.earplugs != null) {
         tagCollar = new NBTTagCompound();
         compound.func_74782_a("earplugs", this.earplugs.func_77955_b(tagCollar));
      }

      if (this.clothes != null) {
         tagCollar = new NBTTagCompound();
         compound.func_74782_a("clothes", this.clothes.func_77955_b(tagCollar));
      }

      if (this.collar != null) {
         tagCollar = new NBTTagCompound();
         compound.func_74782_a("collar", this.collar.func_77955_b(tagCollar));
      }

      return compound;
   }

   public void setBondageItemsValues(NBTTagCompound compound) {
      ItemStack collarStack;
      if (compound.func_74764_b("bind")) {
         collarStack = new ItemStack(compound.func_74775_l("bind"));
         if (collarStack != null && collarStack.func_77973_b() instanceof ItemBind) {
            this.bind = collarStack;
         }
      }

      if (compound.func_74764_b("gag")) {
         collarStack = new ItemStack(compound.func_74775_l("gag"));
         if (collarStack != null && collarStack.func_77973_b() instanceof ItemGag) {
            this.gag = collarStack;
         }
      }

      if (compound.func_74764_b("blindfold")) {
         collarStack = new ItemStack(compound.func_74775_l("blindfold"));
         if (collarStack != null && collarStack.func_77973_b() instanceof ItemBlindfold) {
            this.blindfold = collarStack;
         }
      }

      if (compound.func_74764_b("earplugs")) {
         collarStack = new ItemStack(compound.func_74775_l("earplugs"));
         if (collarStack != null && collarStack.func_77973_b() instanceof ItemEarplugs) {
            this.earplugs = collarStack;
         }
      }

      if (compound.func_74764_b("clothes")) {
         collarStack = new ItemStack(compound.func_74775_l("clothes"));
         if (collarStack != null && collarStack.func_77973_b() instanceof ItemClothes) {
            this.clothes = collarStack;
         }
      }

      if (compound.func_74764_b("collar")) {
         collarStack = new ItemStack(compound.func_74775_l("collar"));
         if (collarStack != null && collarStack.func_77973_b() instanceof ItemCollar) {
            this.collar = collarStack;
         }
      }

   }

   public void func_193051_a(ItemStack stackIn) {
      super.func_193051_a(stackIn);
      if (stackIn != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stackIn);
         if (nbt != null) {
            this.setBondageItemsValues(nbt);
         }
      }

   }

   public ItemStack func_193049_f() {
      ItemStack stack = new ItemStack(ModItems.TRAPPED_BED, 1);
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt = this.func_189515_b(nbt);
      nbt = Utils.removeCoordinates(nbt);
      EnumDyeColor color = this.func_193048_a();
      if (color != null) {
         stack.func_77964_b(color.func_176765_a());
      }

      stack.func_77982_d(nbt);
      return stack;
   }

   public void resetBondageData() {
      this.setBind((ItemStack)null);
      this.setGag((ItemStack)null);
      this.setBlindfold((ItemStack)null);
      this.setEarplugs((ItemStack)null);
      this.setCollar((ItemStack)null);
      this.setClothes((ItemStack)null);
   }

   public boolean isArmed() {
      return this.bind != null;
   }

   public void readDataHolder(NBTTagCompound compound) {
      this.func_145839_a(compound);
   }

   public NBTTagCompound writeDataHolder(NBTTagCompound compound) {
      return this.writeDataHolder(compound);
   }
}
