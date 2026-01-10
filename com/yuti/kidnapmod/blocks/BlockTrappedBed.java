package com.yuti.kidnapmod.blocks;

import com.yuti.kidnapmod.init.ModBlocks;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemKidnapWearable;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.tileentity.TileEntityTrappedBed;
import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockBed.EnumPartType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider.WorldSleepResult;

public class BlockTrappedBed extends BlockBed implements ICanBeLoaded {
   public BlockTrappedBed(String name) {
      this.func_149663_c(name);
      this.setRegistryName(name);
      ModBlocks.BLOCKS.add(this);
      this.func_149711_c(0.2F);
      this.func_149752_b(0.2F);
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new TileEntityTrappedBed();
   }

   public ItemStack getDroppedStack(IBlockAccess world, BlockPos pos) {
      ItemStack stack = new ItemStack(ModItems.TRAPPED_BED, 1);
      TileEntityTrappedBed trappedBedTile = this.getTileEntity(world, pos);
      if (trappedBedTile != null) {
         stack = trappedBedTile.func_193049_f();
      }

      return stack;
   }

   public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ItemStack stack = this.getDroppedStack(world, pos);
      if (stack != null && !stack.func_190926_b()) {
         drops.add(stack);
      }

   }

   public void func_180653_a(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
      if (state.func_177229_b(field_176472_a) == EnumPartType.HEAD) {
         ItemStack stack = this.getDroppedStack(worldIn, pos);
         if (stack != null && !stack.func_190926_b()) {
            func_180635_a(worldIn, pos, stack);
         }
      }

   }

   public ItemStack func_185473_a(World worldIn, BlockPos pos, IBlockState state) {
      BlockPos blockpos = pos;
      if (state.func_177229_b(field_176472_a) == EnumPartType.FOOT) {
         blockpos = pos.func_177972_a((EnumFacing)state.func_177229_b(field_185512_D));
      }

      return this.getDroppedStack(worldIn, blockpos);
   }

   public void func_180657_a(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
      if (state.func_177229_b(field_176472_a) == EnumPartType.HEAD && te instanceof TileEntityTrappedBed) {
         TileEntityTrappedBed tileentitybed = (TileEntityTrappedBed)te;
         ItemStack itemstack = tileentitybed.func_193049_f();
         func_180635_a(worldIn, pos, itemstack);
      } else {
         super.func_180657_a(worldIn, player, pos, state, (TileEntity)null, stack);
      }

   }

   @Nullable
   public TileEntityTrappedBed getTileEntity(IBlockAccess world, BlockPos pos) {
      return (TileEntityTrappedBed)world.func_175625_s(pos);
   }

   public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (worldIn.field_72995_K) {
         return true;
      } else {
         if (playerIn != null && hand == EnumHand.MAIN_HAND) {
            ItemStack heldStack = playerIn.func_184586_b(hand);
            if (heldStack != null && heldStack.func_77973_b() instanceof ItemKidnapWearable) {
               ItemStack stack = Utils.extractValidStack(heldStack);
               BlockPos blockpos = pos;
               if (state.func_177229_b(field_176472_a) == EnumPartType.FOOT) {
                  blockpos = pos.func_177972_a((EnumFacing)state.func_177229_b(field_185512_D));
               }

               Utils.loadItemStack(worldIn, blockpos, this.getTileEntity(worldIn, blockpos), heldStack, stack, playerIn);
               return true;
            }
         }

         if (state.func_177229_b(field_176472_a) != EnumPartType.HEAD) {
            pos = pos.func_177972_a((EnumFacing)state.func_177229_b(field_185512_D));
            state = worldIn.func_180495_p(pos);
            if (state.func_177230_c() != this) {
               return true;
            } else {
               WorldSleepResult sleepResult = worldIn.field_73011_w.canSleepAt(playerIn, pos);
               if (sleepResult != WorldSleepResult.BED_EXPLODES) {
                  if (sleepResult == WorldSleepResult.DENY) {
                     return true;
                  } else {
                     if ((Boolean)state.func_177229_b(field_176471_b)) {
                        EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);
                        if (entityplayer != null) {
                           playerIn.func_146105_b(new TextComponentTranslation("tile.bed.occupied", new Object[0]), true);
                           return true;
                        }

                        this.setBedOccupied(worldIn, pos, playerIn, false);
                     }

                     SleepResult entityplayer$sleepresult = playerIn.func_180469_a(pos);
                     if (entityplayer$sleepresult == SleepResult.OK) {
                        this.setBedOccupied(worldIn, pos, playerIn, true);
                        PlayerBindState playerState = PlayerBindState.getInstance(playerIn);
                        if (playerState != null) {
                           TileEntityTrappedBed tileEntityTrappedBed = this.getTileEntity(worldIn, pos);
                           if (tileEntityTrappedBed != null && tileEntityTrappedBed.isArmed()) {
                              ItemStack bind = tileEntityTrappedBed.getBind();
                              ItemStack gag = tileEntityTrappedBed.getGag();
                              ItemStack blindfold = tileEntityTrappedBed.getBlindfold();
                              ItemStack earplugs = tileEntityTrappedBed.getEarplugs();
                              ItemStack collar = tileEntityTrappedBed.getCollar();
                              ItemStack clothes = tileEntityTrappedBed.getClothes();
                              playerState.applyBondage(bind, gag, blindfold, earplugs, collar, clothes);
                              Utils.sendInfoMessageToEntity(playerIn, "The bed was trapped!");
                              tileEntityTrappedBed.resetBondageData();
                           }
                        }

                        return true;
                     } else {
                        if (entityplayer$sleepresult == SleepResult.NOT_POSSIBLE_NOW) {
                           playerIn.func_146105_b(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
                        } else if (entityplayer$sleepresult == SleepResult.NOT_SAFE) {
                           playerIn.func_146105_b(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
                        } else if (entityplayer$sleepresult == SleepResult.TOO_FAR_AWAY) {
                           playerIn.func_146105_b(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]), true);
                        }

                        return true;
                     }
                  }
               } else {
                  worldIn.func_175698_g(pos);
                  BlockPos blockpos = pos.func_177972_a(((EnumFacing)state.func_177229_b(field_185512_D)).func_176734_d());
                  if (worldIn.func_180495_p(blockpos).func_177230_c() == this) {
                     worldIn.func_175698_g(blockpos);
                  }

                  worldIn.func_72885_a((Entity)null, (double)pos.func_177958_n() + 0.5D, (double)pos.func_177956_o() + 0.5D, (double)pos.func_177952_p() + 0.5D, 5.0F, true, true);
                  return true;
               }
            }
         } else {
            return true;
         }
      }
   }

   @Nullable
   private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos) {
      Iterator var3 = worldIn.field_73010_i.iterator();

      EntityPlayer entityplayer;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         entityplayer = (EntityPlayer)var3.next();
      } while(!entityplayer.func_70608_bn() || !entityplayer.field_71081_bT.equals(pos));

      return entityplayer;
   }

   public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player) {
      return true;
   }

   public void setBedOccupied(IBlockAccess blockAccess, BlockPos pos, EntityPlayer player, boolean occupied) {
      if (blockAccess instanceof World) {
         World world = (World)blockAccess;
         TileEntity tileEntity = world.func_175625_s(pos);
         IBlockState state = world.func_180495_p(pos);
         state = state.func_177230_c().func_176221_a(state, world, pos);
         state = state.func_177226_a(field_176471_b, occupied);
         world.func_180501_a(pos, state, 4);
         if (tileEntity != null) {
            tileEntity.func_145829_t();
            world.func_175690_a(pos, tileEntity);
         }
      }

   }
}
