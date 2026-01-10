package com.yuti.kidnapmod.recipes;

import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.tileentity.ITileEntityBondageItemHolder;
import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class RecipeBondageItemLoaderBlock extends RecipeBondageItemLoader {
   public RecipeBondageItemLoaderBlock(Class<?> carrierIn) {
      super(carrierIn);
   }

   public ItemStack func_77572_b(InventoryCrafting inv) {
      Object[] infos = this.getCraftInformation(inv);
      if (infos == null) {
         return ItemStack.field_190927_a;
      } else if (infos.length != 2) {
         return ItemStack.field_190927_a;
      } else if (!(infos[0] instanceof ItemStack)) {
         return ItemStack.field_190927_a;
      } else if (!(infos[1] instanceof List)) {
         return ItemStack.field_190927_a;
      } else {
         ItemStack stackEntity = (ItemStack)infos[0];
         List<?> list = (List)infos[1];
         if (!list.isEmpty() && !stackEntity.func_190926_b()) {
            ItemStack stackResult = stackEntity.func_77946_l();
            stackResult = stackResult.func_77979_a(1);
            ITileEntityBondageItemHolder tileEntity = this.getEntityHolder();
            NBTTagCompound nbt = Utils.getTagComponent(stackResult);
            if (nbt != null && nbt.func_74764_b("BlockEntityTag")) {
               tileEntity.readDataHolder(nbt.func_74775_l("BlockEntityTag"));
            }

            Iterator var8 = list.iterator();

            while(var8.hasNext()) {
               Object o = var8.next();
               if (o instanceof ItemStack) {
                  ItemStack currentStack = (ItemStack)o;
                  if (!currentStack.func_190926_b()) {
                     ItemStack stackToAdd = currentStack.func_77946_l();
                     stackToAdd = stackToAdd.func_77979_a(1);
                     if (stackToAdd.func_77973_b() instanceof ItemBind) {
                        tileEntity.setBind(stackToAdd);
                     }

                     if (stackToAdd.func_77973_b() instanceof ItemGag) {
                        tileEntity.setGag(stackToAdd);
                     }

                     if (stackToAdd.func_77973_b() instanceof ItemBlindfold) {
                        tileEntity.setBlindfold(stackToAdd);
                     }

                     if (stackToAdd.func_77973_b() instanceof ItemEarplugs) {
                        tileEntity.setEarplugs(stackToAdd);
                     }

                     if (stackToAdd.func_77973_b() instanceof ItemCollar) {
                        tileEntity.setCollar(stackToAdd);
                     }

                     if (stackToAdd.func_77973_b() instanceof ItemClothes) {
                        tileEntity.setClothes(stackToAdd);
                     }
                  }
               }
            }

            NBTTagCompound tileData = new NBTTagCompound();
            if (nbt != null) {
               tileData = tileEntity.writeDataHolder(tileData);
               tileData = Utils.removeCoordinates(tileData);
               nbt.func_74782_a("BlockEntityTag", tileData);
               stackResult.func_77982_d(nbt);
            }

            return stackResult;
         } else {
            return ItemStack.field_190927_a;
         }
      }
   }

   public abstract ITileEntityBondageItemHolder getEntityHolder();

   protected boolean checkCurrentStackWithDataStoredInStack(ItemStack currentStack, ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      ITileEntityBondageItemHolder tileEntity = this.getEntityHolder();
      if (nbt != null && nbt.func_74764_b("BlockEntityTag")) {
         tileEntity.readDataHolder(nbt.func_74775_l("BlockEntityTag"));
         if (tileEntity.getBind() != null && currentStack.func_77973_b() instanceof ItemBind) {
            return false;
         }

         if (tileEntity.getGag() != null && currentStack.func_77973_b() instanceof ItemGag) {
            return false;
         }

         if (tileEntity.getBlindfold() != null && currentStack.func_77973_b() instanceof ItemBlindfold) {
            return false;
         }

         if (tileEntity.getEarplugs() != null && currentStack.func_77973_b() instanceof ItemEarplugs) {
            return false;
         }

         if (tileEntity.getCollar() != null && currentStack.func_77973_b() instanceof ItemCollar) {
            return false;
         }

         if (tileEntity.getClothes() != null && currentStack.func_77973_b() instanceof ItemClothes) {
            return false;
         }
      }

      return true;
   }
}
