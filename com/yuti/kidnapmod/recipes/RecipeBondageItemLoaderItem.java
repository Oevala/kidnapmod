package com.yuti.kidnapmod.recipes;

import com.yuti.kidnapmod.items.IItemBondageItemHolder;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import java.util.Iterator;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class RecipeBondageItemLoaderItem extends RecipeBondageItemLoader {
   public RecipeBondageItemLoaderItem(Class<? extends IItemBondageItemHolder> carrierIn) {
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
            if (stackResult != null && this.carrier.isInstance(stackResult.func_77973_b())) {
               stackResult = stackResult.func_77979_a(1);
               Item resultItem = stackResult.func_77973_b();
               if (resultItem instanceof IItemBondageItemHolder) {
                  IItemBondageItemHolder itemManager = (IItemBondageItemHolder)resultItem;
                  Iterator var8 = list.iterator();

                  while(var8.hasNext()) {
                     Object o = var8.next();
                     if (o instanceof ItemStack) {
                        ItemStack currentStack = (ItemStack)o;
                        if (!currentStack.func_190926_b()) {
                           ItemStack stackToAdd = currentStack.func_77946_l();
                           stackToAdd = stackToAdd.func_77979_a(1);
                           if (stackToAdd.func_77973_b() instanceof ItemBind) {
                              itemManager.setBind(stackResult, stackToAdd);
                           }

                           if (stackToAdd.func_77973_b() instanceof ItemGag) {
                              itemManager.setGag(stackResult, stackToAdd);
                           }

                           if (stackToAdd.func_77973_b() instanceof ItemBlindfold) {
                              itemManager.setBlindfold(stackResult, stackToAdd);
                           }

                           if (stackToAdd.func_77973_b() instanceof ItemEarplugs) {
                              itemManager.setEarplugs(stackResult, stackToAdd);
                           }

                           if (stackToAdd.func_77973_b() instanceof ItemCollar) {
                              itemManager.setCollar(stackResult, stackToAdd);
                           }

                           if (stackToAdd.func_77973_b() instanceof ItemClothes) {
                              itemManager.setClothes(stackResult, stackToAdd);
                           }
                        }
                     }
                  }
               }
            }

            return stackResult;
         } else {
            return ItemStack.field_190927_a;
         }
      }
   }

   protected boolean checkCurrentStackWithDataStoredInStack(ItemStack currentStack, ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof IItemBondageItemHolder) {
         IItemBondageItemHolder itemManager = (IItemBondageItemHolder)stack.func_77973_b();
         if (itemManager != null) {
            if (itemManager.getBind(stack) != null && currentStack.func_77973_b() instanceof ItemBind) {
               return false;
            }

            if (itemManager.getGag(stack) != null && currentStack.func_77973_b() instanceof ItemGag) {
               return false;
            }

            if (itemManager.getBlindfold(stack) != null && currentStack.func_77973_b() instanceof ItemBlindfold) {
               return false;
            }

            if (itemManager.getEarplugs(stack) != null && currentStack.func_77973_b() instanceof ItemEarplugs) {
               return false;
            }

            if (itemManager.getCollar(stack) != null && currentStack.func_77973_b() instanceof ItemCollar) {
               return false;
            }

            if (itemManager.getClothes(stack) != null && currentStack.func_77973_b() instanceof ItemClothes) {
               return false;
            }

            return true;
         }
      }

      return false;
   }
}
