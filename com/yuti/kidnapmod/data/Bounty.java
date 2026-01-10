package com.yuti.kidnapmod.data;

import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.time.Timer;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Bounty implements Serializable {
   private String id;
   private String clientName;
   private String targetName;
   private String clientId;
   private String targetId;
   public ItemStack reward;
   private Timer timer;
   private int timeRemaining;

   public Bounty() {
   }

   public Bounty(EntityPlayer client, EntityPlayer target, ItemStack reward, int time) {
      this.id = UUID.randomUUID().toString();
      this.clientId = client.func_110124_au().toString();
      this.targetId = target.func_110124_au().toString();
      this.clientName = client.func_70005_c_();
      this.targetName = target.func_70005_c_();
      this.targetId = target.func_110124_au().toString();
      this.reward = reward.func_77946_l();
      this.timer = new Timer(time);
      this.timeRemaining = this.timer.getSecondsRemaining();
   }

   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      compound.func_74778_a("id", this.id);
      compound.func_74778_a("clientId", this.clientId);
      compound.func_74778_a("targetId", this.targetId);
      compound.func_74778_a("clientName", this.clientName);
      compound.func_74778_a("targetName", this.targetName);
      NBTTagCompound rewardTag = new NBTTagCompound();
      compound.func_74782_a("reward", this.reward.func_77955_b(rewardTag));

      try {
         compound.func_74773_a("timer", Utils.serialize(this.timer));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      compound.func_74768_a("timeRemaining", this.timeRemaining);
      return compound;
   }

   public void readFromNBT(NBTTagCompound nbt) {
      this.id = nbt.func_74779_i("id");
      this.clientId = nbt.func_74779_i("clientId");
      this.targetId = nbt.func_74779_i("targetId");
      this.clientName = nbt.func_74779_i("clientName");
      this.targetName = nbt.func_74779_i("targetName");
      this.reward = new ItemStack((NBTTagCompound)nbt.func_74781_a("reward"));

      try {
         this.timer = (Timer)Utils.deserialize(nbt.func_74770_j("timer"));
      } catch (IOException | ClassNotFoundException var3) {
         var3.printStackTrace();
      }

      this.timeRemaining = nbt.func_74762_e("timeRemaining");
   }

   public String getClientName() {
      return this.clientName;
   }

   public String getTargetName() {
      return this.targetName;
   }

   public UUID getClientId() {
      return UUID.fromString(this.clientId);
   }

   public UUID getTargetId() {
      return UUID.fromString(this.targetId);
   }

   public ItemStack getReward() {
      return this.reward;
   }

   public String getRewardDescription() {
      ItemStack rewardStack = this.getReward();
      if (rewardStack != null) {
         String rewardName = "";
         Item rewardItem = rewardStack.func_77973_b();
         if (rewardItem != null) {
            rewardName = rewardItem.func_77653_i(rewardStack);
            return rewardName + " x  " + rewardStack.func_190916_E();
         }
      }

      return "";
   }

   public String getId() {
      return this.id;
   }

   public void setUpBeforeSend() {
      this.timeRemaining = this.timer.getSecondsRemaining();
      Entity client = FMLCommonHandler.instance().getMinecraftServerInstance().func_175576_a(this.getClientId());
      if (client != null) {
         this.clientName = client.func_70005_c_();
      }

      Entity target = FMLCommonHandler.instance().getMinecraftServerInstance().func_175576_a(this.getTargetId());
      if (target != null) {
         this.targetName = target.func_70005_c_();
      }

   }

   public int[] getRemainingTime() {
      int[] time = new int[2];
      int hours = this.timeRemaining / 3600;
      time[0] = hours;
      int minutes = this.timeRemaining % 3600 / 60;
      time[1] = minutes;
      return time;
   }

   public boolean isOutdated() {
      return this.timer.getSecondsRemaining() <= 0;
   }

   public boolean isClient(EntityPlayer player) {
      if (player != null) {
         UUID idAuthor = this.getClientId();
         return player.func_110124_au().equals(idAuthor);
      } else {
         return false;
      }
   }

   public boolean isTarget(EntityPlayer target) {
      if (target != null) {
         UUID idAuthor = this.getTargetId();
         return target.func_110124_au().equals(idAuthor);
      } else {
         return false;
      }
   }

   public boolean matchRequirements(EntityPlayer client, EntityPlayer slave) {
      return this.isClient(client) && this.isTarget(slave);
   }
}
