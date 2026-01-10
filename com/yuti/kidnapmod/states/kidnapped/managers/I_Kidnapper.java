package com.yuti.kidnapmod.states.kidnapped.managers;

import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.entity.EntityLivingBase;

public interface I_Kidnapper {
   void addSlave(PlayerBindState var1);

   void removeSlave(PlayerBindState var1);

   boolean canFree(PlayerBindState var1);

   boolean allowSlaveTransfer();

   void transferAllSlavesTo(I_Kidnapper var1);

   void getBackSlaveFromPole(PlayerBindState var1);

   boolean hasSlaves();

   void onSlaveLogout(PlayerBindState var1);

   boolean shouldTelportSlaveToMaster(PlayerBindState var1);

   void teleportSlaveToMaster(PlayerBindState var1);

   void onSlaveReleased(PlayerBindState var1);

   void onSlaveStarving(PlayerBindState var1);

   void onSlaveStruggle(PlayerBindState var1);

   void onSlaveTalk(PlayerBindState var1);

   EntityLivingBase getEntity();

   boolean canEnslave(PlayerBindState var1);

   void removeSlave(PlayerBindState var1, boolean var2);

   boolean allowMultipleSlaves();
}
