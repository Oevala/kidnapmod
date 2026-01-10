package com.yuti.kidnapmod.util.handlers.extrainventoryevents;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemHelper;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageContainer;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageContainerProvider;
import com.yuti.kidnapmod.extrainventory.capabilities.ExtraBondageItemCapabilities;
import com.yuti.kidnapmod.extrainventory.capabilities.IExtraBondageItemHandler;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.extrainventory.PacketSynchronizeInventory;
import com.yuti.kidnapmod.util.Utils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EntityExtraBondageInventoryEventHandler {
   private HashMap<UUID, ItemStack[]> extraBondageSync = new HashMap();

   @SubscribeEvent
   public void cloneCapabilitiesEvent(Clone event) {
      try {
         ExtraBondageContainer bco = (ExtraBondageContainer)ExtraBondageItemHelper.getExtraBondageItemHandler(event.getOriginal());
         NBTTagCompound nbt = bco.serializeNBT();
         ExtraBondageContainer bcn = (ExtraBondageContainer)ExtraBondageItemHelper.getExtraBondageItemHandler(event.getEntityPlayer());
         bcn.deserializeNBT(nbt);
      } catch (Exception var5) {
         Utils.getLogger().error("Could not clone player [" + event.getOriginal().func_70005_c_() + "] extra items when changing dimensions");
      }

   }

   @SubscribeEvent
   public void attachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
      if (event.getObject() instanceof EntityPlayer) {
         event.addCapability(new ResourceLocation("knapm", "container"), new ExtraBondageContainerProvider(new ExtraBondageContainer()));
      }

   }

   @SubscribeEvent
   public void playerJoin(EntityJoinWorldEvent event) {
      Entity entity = event.getEntity();
      if (entity instanceof EntityPlayerMP) {
         EntityPlayerMP player = (EntityPlayerMP)entity;
         this.syncSlots(player, Collections.singletonList(player));
      }

   }

   @SubscribeEvent
   public void onStartTracking(StartTracking event) {
      Entity target = event.getTarget();
      if (target instanceof EntityPlayerMP) {
         this.syncSlots((EntityPlayer)target, Collections.singletonList(event.getEntityPlayer()));
      }

   }

   @SubscribeEvent
   public void onPlayerLoggedOut(PlayerLoggedOutEvent event) {
      this.extraBondageSync.remove(event.player.func_110124_au());
   }

   @SubscribeEvent
   public void playerTick(PlayerTickEvent event) {
      if (event.phase == Phase.END) {
         EntityPlayer player = event.player;
         IExtraBondageItemHandler extraBondage = ExtraBondageItemHelper.getExtraBondageItemHandler(player);

         for(int i = 0; i < extraBondage.getSlots(); ++i) {
            ItemStack stack = extraBondage.getStackInSlot(i);
            IExtraBondageItem bondageItem = (IExtraBondageItem)stack.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null);
            if (bondageItem != null) {
               bondageItem.onWornTick(stack, player);
            }
         }

         if (!player.field_70170_p.field_72995_K) {
            this.syncExtraBondageInventory(player, extraBondage);
         }
      }

   }

   private void syncExtraBondageInventory(EntityPlayer player, IExtraBondageItemHandler extraBondage) {
      ItemStack[] items = (ItemStack[])this.extraBondageSync.get(player.func_110124_au());
      if (items == null) {
         items = new ItemStack[extraBondage.getSlots()];
         Arrays.fill(items, ItemStack.field_190927_a);
         this.extraBondageSync.put(player.func_110124_au(), items);
      }

      if (items.length != extraBondage.getSlots()) {
         ItemStack[] old = items;
         items = new ItemStack[extraBondage.getSlots()];
         System.arraycopy(old, 0, items, 0, Math.min(old.length, items.length));
         this.extraBondageSync.put(player.func_110124_au(), items);
      }

      Set<EntityPlayer> receivers = null;

      for(int i = 0; i < extraBondage.getSlots(); ++i) {
         ItemStack stack = extraBondage.getStackInSlot(i);
         IExtraBondageItem bondageItem = (IExtraBondageItem)stack.getCapability(ExtraBondageItemCapabilities.CAPABILITY_EXTRA_ITEM_BONDAGE, (EnumFacing)null);
         if (extraBondage.isChanged(i) || bondageItem != null && bondageItem.willAutoSync(stack, player) && !ItemStack.func_77989_b(stack, items[i])) {
            if (receivers == null) {
               receivers = new HashSet(((WorldServer)player.field_70170_p).func_73039_n().getTrackingPlayers(player));
               receivers.add(player);
            }

            this.syncSlot(player, i, stack, receivers);
            extraBondage.setChanged(i, false);
            items[i] = stack == null ? ItemStack.field_190927_a : stack.func_77946_l();
         }
      }

   }

   private void syncSlots(EntityPlayer player, Collection<? extends EntityPlayer> receivers) {
      IExtraBondageItemHandler extraBondage = ExtraBondageItemHelper.getExtraBondageItemHandler(player);

      for(int i = 0; i < extraBondage.getSlots(); ++i) {
         this.syncSlot(player, i, extraBondage.getStackInSlot(i), receivers);
      }

   }

   private void syncSlot(EntityPlayer player, int slot, ItemStack stack, Collection<? extends EntityPlayer> receivers) {
      PacketSynchronizeInventory pkt = new PacketSynchronizeInventory(player, slot, stack);
      Iterator var6 = receivers.iterator();

      while(var6.hasNext()) {
         EntityPlayer receiver = (EntityPlayer)var6.next();
         PacketHandler.INSTANCE.sendTo(pkt, (EntityPlayerMP)receiver);
      }

   }

   @SubscribeEvent
   public void playerDeath(PlayerDropsEvent event) {
      if (event.getEntity() instanceof EntityPlayer && !event.getEntity().field_70170_p.field_72995_K && !event.getEntity().field_70170_p.func_82736_K().func_82766_b("keepInventory")) {
         this.dropItemsAt(event.getEntityPlayer(), event.getDrops(), event.getEntityPlayer());
      }

   }

   public void dropItemsAt(EntityPlayer player, List<EntityItem> drops, Entity e) {
      IExtraBondageItemHandler extraBondage = ExtraBondageItemHelper.getExtraBondageItemHandler(player);

      for(int i = 0; i < extraBondage.getSlots(); ++i) {
         if (extraBondage.getStackInSlot(i) != null && !extraBondage.getStackInSlot(i).func_190926_b()) {
            EntityItem ei = new EntityItem(e.field_70170_p, e.field_70165_t, e.field_70163_u + (double)e.func_70047_e(), e.field_70161_v, extraBondage.getStackInSlot(i).func_77946_l());
            ei.func_174867_a(40);
            float f1 = e.field_70170_p.field_73012_v.nextFloat() * 0.5F;
            float f2 = e.field_70170_p.field_73012_v.nextFloat() * 3.1415927F * 2.0F;
            ei.field_70159_w = (double)(-MathHelper.func_76126_a(f2) * f1);
            ei.field_70179_y = (double)(MathHelper.func_76134_b(f2) * f1);
            ei.field_70181_x = 0.20000000298023224D;
            drops.add(ei);
            extraBondage.setStackInSlot(i, ItemStack.field_190927_a);
         }
      }

   }
}
