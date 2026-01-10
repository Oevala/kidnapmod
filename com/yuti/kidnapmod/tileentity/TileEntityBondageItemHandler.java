package com.yuti.kidnapmod.tileentity;

import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBondageItemHandler extends TileEntity implements ITileEntityBondageItemHolder {
   private ItemStack bind;
   private ItemStack gag;
   private ItemStack blindfold;
   private ItemStack earplugs;
   private ItemStack collar;
   private ItemStack clothes;
   private boolean offMode = false;

   public TileEntityBondageItemHandler() {
   }

   public TileEntityBondageItemHandler(boolean offModeIn) {
      this.offMode = offModeIn;
   }

   public ItemStack getBind() {
      return this.bind;
   }

   public void setBind(ItemStack bind) {
      this.bind = bind;
      this.func_70296_d();
      this.setBlockState();
   }

   public ItemStack getGag() {
      return this.gag;
   }

   public void setGag(ItemStack gag) {
      this.gag = gag;
      this.func_70296_d();
      this.setBlockState();
   }

   public ItemStack getBlindfold() {
      return this.blindfold;
   }

   public void setBlindfold(ItemStack blindfold) {
      this.blindfold = blindfold;
      this.func_70296_d();
      this.setBlockState();
   }

   public ItemStack getEarplugs() {
      return this.earplugs;
   }

   public void setEarplugs(ItemStack earplugs) {
      this.earplugs = earplugs;
      this.func_70296_d();
      this.setBlockState();
   }

   public ItemStack getClothes() {
      return this.clothes;
   }

   public void setClothes(ItemStack clothes) {
      this.clothes = clothes;
      this.func_70296_d();
      this.setBlockState();
   }

   public ItemStack getCollar() {
      return this.collar;
   }

   public void setCollar(ItemStack collar) {
      this.collar = collar;
      this.func_70296_d();
      this.setBlockState();
   }

   public void setBlockState() {
      if (!this.offMode) {
         IBlockState state = this.func_145831_w().func_180495_p(this.func_174877_v());
         this.func_145831_w().func_184138_a(this.func_174877_v(), state, state, 3);
      }

   }

   public boolean isArmed() {
      return this.bind != null;
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
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

   private void notifyBlockUpdate() {
      if (!this.offMode) {
         IBlockState state = this.func_145831_w().func_180495_p(this.func_174877_v());
         this.func_145831_w().func_184138_a(this.func_174877_v(), state, state, 3);
      }

   }

   public void func_70296_d() {
      if (!this.offMode) {
         super.func_70296_d();
         this.notifyBlockUpdate();
      }

   }

   public NBTTagCompound func_189517_E_() {
      return this.func_189515_b(new NBTTagCompound());
   }

   @Nullable
   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.func_174877_v(), 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      if (!this.offMode) {
         this.func_145839_a(pkt.func_148857_g());
         this.notifyBlockUpdate();
      }

   }

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
      return oldState.func_177230_c() != newSate.func_177230_c();
   }

   public void handleUpdateTag(NBTTagCompound tag) {
      if (!this.offMode) {
         this.func_145839_a(tag);
         this.setBlockState();
      }

   }

   public void readDataHolder(NBTTagCompound compound) {
      this.func_145839_a(compound);
   }

   public NBTTagCompound writeDataHolder(NBTTagCompound compound) {
      return this.func_189515_b(compound);
   }
}
