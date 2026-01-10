package com.yuti.kidnapmod.util.teleport;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class Position {
   private int dimension;
   private double x;
   private double y;
   private double z;
   private float yaw;
   private float pitch;

   public Position(int dimension, double x, double y, double z, float yaw, float pitch) {
      this.dimension = dimension;
      this.x = x;
      this.y = y;
      this.z = z;
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public Position(NBTTagCompound tag) {
      this.readFromNBT(tag);
   }

   public int getDimension() {
      return this.dimension;
   }

   public void setDimension(int dimension) {
      this.dimension = dimension;
   }

   public double getX() {
      return this.x;
   }

   public void setX(double x) {
      this.x = x;
   }

   public double getY() {
      return this.y;
   }

   public void setY(double y) {
      this.y = y;
   }

   public double getZ() {
      return this.z;
   }

   public void setZ(double z) {
      this.z = z;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public void readFromNBT(NBTTagCompound tag) {
      if (tag.func_74764_b("yaw")) {
         this.yaw = tag.func_74760_g("yaw");
      }

      if (tag.func_74764_b("pitch")) {
         this.pitch = tag.func_74760_g("pitch");
      }

      if (tag.func_74764_b("dimension")) {
         this.dimension = tag.func_74762_e("dimension");
      }

      if (tag.func_74764_b("x")) {
         this.x = tag.func_74769_h("x");
      }

      if (tag.func_74764_b("y")) {
         this.y = tag.func_74769_h("y");
      }

      if (tag.func_74764_b("z")) {
         this.z = tag.func_74769_h("z");
      }

   }

   public NBTTagCompound writeToNBT(NBTTagCompound tag) {
      tag.func_74768_a("dimension", this.dimension);
      tag.func_74780_a("x", this.x);
      tag.func_74780_a("y", this.y);
      tag.func_74780_a("z", this.z);
      tag.func_74776_a("yaw", this.yaw);
      tag.func_74776_a("pitch", this.pitch);
      return tag;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         Position other = (Position)obj;
         if (this.dimension != other.dimension) {
            return false;
         } else if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
         } else if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
         } else {
            return Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
         }
      }
   }

   public BlockPos getBlockPos() {
      return new BlockPos(this.x, this.y, this.z);
   }
}
