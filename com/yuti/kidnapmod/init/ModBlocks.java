package com.yuti.kidnapmod.init;

import com.yuti.kidnapmod.blocks.BlockKidnapBomb;
import com.yuti.kidnapmod.blocks.BlockKidnapDoorBase;
import com.yuti.kidnapmod.blocks.BlockPadded;
import com.yuti.kidnapmod.blocks.BlockPaddedPane;
import com.yuti.kidnapmod.blocks.BlockPaddedSlab;
import com.yuti.kidnapmod.blocks.BlockPaddedStairs;
import com.yuti.kidnapmod.blocks.BlockRopesTrap;
import com.yuti.kidnapmod.blocks.BlockTrappedBed;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks {
   public static final List<Block> BLOCKS = new ArrayList();
   public static final Block ROPES_TRAP;
   public static final Block PADDED_BLOCK;
   public static final BlockPaddedSlab PADDED_SLAB_DOUBLE;
   public static final BlockPaddedSlab PADDED_SLAB;
   public static final Block PADDED_STAIRS;
   public static final Block PADDED_PANE;
   public static final Block CELL_DOOR;
   public static final Block TRAPPED_BED;
   public static final BlockKidnapBomb KIDNAP_BOMB;

   static {
      ROPES_TRAP = new BlockRopesTrap("ropes_trap", Material.field_151575_d);
      PADDED_BLOCK = new BlockPadded("padded", Material.field_151580_n);
      PADDED_SLAB_DOUBLE = new BlockPaddedSlab.Double("double_padded_slab", Material.field_151580_n);
      PADDED_SLAB = new BlockPaddedSlab.Half("padded_slab", PADDED_SLAB_DOUBLE, Material.field_151580_n);
      PADDED_STAIRS = new BlockPaddedStairs("padded_stairs", PADDED_BLOCK.func_176223_P());
      PADDED_PANE = new BlockPaddedPane("padded_pane", Material.field_151580_n);
      CELL_DOOR = new BlockKidnapDoorBase("cell_door", Material.field_151573_f);
      TRAPPED_BED = new BlockTrappedBed("trapped_bed");
      KIDNAP_BOMB = new BlockKidnapBomb("kidnap_bomb");
   }
}
