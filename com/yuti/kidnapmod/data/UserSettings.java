package com.yuti.kidnapmod.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;

public class UserSettings implements Serializable {
   private static final long serialVersionUID = 6799889843044960382L;
   private int talkArea = -1;
   private int talkAreaPref = -1;
   private Set<UUID> blockedPlayers = new HashSet();

   public int getTalkArea() {
      return this.talkArea;
   }

   public void setTalkArea(int talkArea) {
      this.talkArea = talkArea;
   }

   public int getTalkAreaPref() {
      return this.talkAreaPref;
   }

   public void setTalkAreaPref(int talkAreaPref) {
      this.talkAreaPref = talkAreaPref;
   }

   public boolean addBlockedPlayer(EntityPlayer player) {
      return player != null && !this.isBlocked(player) ? this.blockedPlayers.add(player.func_110124_au()) : false;
   }

   public boolean removeBlockedPlayer(EntityPlayer player) {
      return player != null && this.isBlocked(player) ? this.blockedPlayers.remove(player.func_110124_au()) : false;
   }

   public boolean isBlocked(EntityPlayer player) {
      return player != null ? this.blockedPlayers.contains(player.func_110124_au()) : false;
   }
}
