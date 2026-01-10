package com.yuti.kidnapmod.states.kidnapped.managers;

import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.teleport.Position;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface I_Kidnapped {
   void free();

   void free(boolean var1);

   boolean isEnslavable();

   boolean isSlave();

   boolean canBeTiedUp();

   void transferSlaveryTo(I_Kidnapper var1);

   boolean isGagged();

   boolean isTiedUp();

   boolean isBlindfolded();

   boolean hasEarplugs();

   boolean hasClothes();

   boolean canTakesOffClothes(EntityPlayer var1);

   boolean canChangeClothes(EntityPlayer var1);

   boolean canChangeClothes();

   boolean hasCollar();

   boolean hasLockedCollar();

   boolean hasNamedCollar();

   boolean hasClothesWithSmallArms();

   void putGagOn(ItemStack var1);

   ItemStack takeGagOff();

   void putBindOn(ItemStack var1);

   ItemStack takeBindOff();

   void putBlindfoldOn(ItemStack var1);

   ItemStack takesBlindfoldOff();

   void putEarsPlugsOn(ItemStack var1);

   ItemStack takesEarplugsOff();

   void putClothesOn(ItemStack var1);

   ItemStack takesClothesOff();

   void putCollarOn(ItemStack var1);

   ItemStack takesCollarOff();

   ItemStack takesCollarOff(boolean var1);

   void untie();

   boolean isBoundAndGagged();

   void tighten(EntityPlayer var1);

   ItemStack getCurrentBind();

   ItemStack getCurrentGag();

   ItemStack getCurrentBlindfold();

   ItemStack getCurrentEarplugs();

   ItemStack getCurrentClothes();

   ItemStack getCurrentCollar();

   ItemStack replaceBind(ItemStack var1);

   ItemStack replaceGag(ItemStack var1);

   ItemStack replaceBlindfold(ItemStack var1);

   ItemStack replaceEarPlugs(ItemStack var1);

   ItemStack replaceClothes(ItemStack var1);

   ItemStack replaceCollar(ItemStack var1);

   ItemStack replaceGag(ItemStack var1, boolean var2);

   ItemStack replaceBlindfold(ItemStack var1, boolean var2);

   ItemStack replaceEarPlugs(ItemStack var1, boolean var2);

   ItemStack replaceClothes(ItemStack var1, boolean var2);

   ItemStack replaceCollar(ItemStack var1, boolean var2);

   void dropBondageItems();

   void dropBondageItems(boolean var1);

   void dropBondageItems(boolean var1, boolean var2, boolean var3, boolean var4, boolean var5);

   void dropClothes();

   boolean hasGaggingEffect();

   boolean hasBlindingEffect();

   void takeBondageItemBy(PlayerBindState var1, int var2);

   void applyBondage(ItemStack var1, ItemStack var2, ItemStack var3, ItemStack var4, ItemStack var5, ItemStack var6);

   int getBondageItemsWhichCanBeRemovedCount();

   void applyChloroform(int var1);

   String getNameFromCollar();

   UUID getKidnappedUniqueId();

   boolean isForSell();

   void kidnappedDropItem(ItemStack var1);

   boolean canBeKidnappedByEvents();

   void shockKidnapped();

   String getKidnappedName();

   boolean hasKnives();

   boolean getEnslavedBy(I_Kidnapper var1);

   boolean isTiedToPole();

   boolean onDeathKidnapped();

   void checkGagAfterApply();

   void checkBlindfoldAfterApply();

   void checkEarplugsAfterApply();

   void checkCollarAfterApply();

   void teleportToPosition(Position var1);
}
