package com.yuti.kidnapmod.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBlockPaddedPane extends TileEntity {
   private boolean open = false;

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean open) {
      this.open = open;
      this.func_70296_d();
      this.setBlockState();
   }

   public void setBlockState() {
      IBlockState state = this.func_145831_w().func_180495_p(this.func_174877_v());
      this.func_145831_w().func_184138_a(this.func_174877_v(), state, state, 3);
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      this.open = compound.func_74767_n("open");
   }

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      compound.func_74757_a("open", this.open);
      return compound;
   }

   private void notifyBlockUpdate() {
      IBlockState state = this.func_145831_w().func_180495_p(this.func_174877_v());
      this.func_145831_w().func_184138_a(this.func_174877_v(), state, state, 3);
   }

   public void func_70296_d() {
      super.func_70296_d();
      this.notifyBlockUpdate();
   }

   public NBTTagCompound func_189517_E_() {
      return this.func_189515_b(new NBTTagCompound());
   }

   @Nullable
   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.func_174877_v(), 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      this.func_145839_a(pkt.func_148857_g());
      this.notifyBlockUpdate();
   }

   public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
      return oldState.func_177230_c() != newSate.func_177230_c();
   }

   public void handleUpdateTag(NBTTagCompound tag) {
      this.func_145839_a(tag);
      this.setBlockState();
   }
}
