package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandFullyRestrain extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      List<PlayerBindState> states = this.getTargetsState(server, sender, params);
      ItemBind bind = ModItems.ROPES;
      ItemGag gag = ModItems.CLOTH_GAG;
      ItemBlindfold blindfold = null;
      ItemEarplugs earplugs = null;
      ItemCollar collar = null;
      String url = null;
      Item newEarplugs;
      if (params.length >= 2) {
         newEarplugs = Utils.getItem(params[1], ItemBind.class);
         if (newEarplugs != null) {
            bind = (ItemBind)newEarplugs;
         }
      }

      if (params.length >= 3) {
         newEarplugs = Utils.getItem(params[2], ItemGag.class);
         if (newEarplugs != null) {
            gag = (ItemGag)newEarplugs;
         }
      }

      if (params.length >= 4) {
         newEarplugs = Utils.getItem(params[3], ItemBlindfold.class);
         if (newEarplugs != null) {
            blindfold = (ItemBlindfold)newEarplugs;
         }
      }

      if (params.length >= 5) {
         newEarplugs = Utils.getItem(params[4], ItemCollar.class);
         if (newEarplugs != null) {
            collar = (ItemCollar)newEarplugs;
         }
      }

      if (params.length >= 6) {
         newEarplugs = Utils.getItem(params[5], ItemEarplugs.class);
         if (newEarplugs != null) {
            earplugs = (ItemEarplugs)newEarplugs;
         }
      }

      if (params.length >= 7) {
         url = params[6];
      }

      Iterator var14 = states.iterator();

      while(var14.hasNext()) {
         PlayerBindState state = (PlayerBindState)var14.next();
         if (state != null && !state.isBoundAndGagged()) {
            if (!state.isTiedUp()) {
               state.putBindOn(new ItemStack(bind));
            }

            if (!state.isGagged()) {
               state.putGagOn(new ItemStack(gag));
            }

            if (!state.isBlindfolded() && blindfold != null) {
               state.putBlindfoldOn(new ItemStack(blindfold));
            }

            if (!state.hasEarplugs() && earplugs != null) {
               state.putEarsPlugsOn(new ItemStack(earplugs));
            }

            ItemStack stackCollar;
            if (!state.hasClothes() && url != null) {
               stackCollar = new ItemStack(ModItems.CLOTHES);
               stackCollar = ModItems.CLOTHES.setDynamicTextureUrl(stackCollar, url);
               state.putClothesOn(stackCollar);
            }

            if (!state.hasCollar() && collar != null) {
               stackCollar = new ItemStack(collar);
               if (sender instanceof EntityPlayer && collar != null && stackCollar != null) {
                  collar.addOwner(stackCollar, (EntityPlayer)sender);
               }

               state.putCollarOn(stackCollar);
            }

            Utils.sendValidMessageToEntity(sender, state.getPlayer().func_70005_c_() + " is now fully restrained.");
            Utils.sendInfoMessageToEntity(state.getPlayer(), "You have been completely tied up!");
         }
      }

   }

   public String func_71517_b() {
      return "fullyrestrain";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.fullyrestrain";
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 2 && args[1] != null) {
         return Utils.getItemNameList(ModItems.BIND_LIST, args[1]);
      } else if (args.length == 3 && args[2] != null) {
         return Utils.getItemNameList(ModItems.GAG_LIST, args[2]);
      } else if (args.length == 4 && args[3] != null) {
         return Utils.getItemNameList(ModItems.BLINDFOLD_LIST, args[3]);
      } else if (args.length == 5 && args[4] != null) {
         return Utils.getItemNameList(ModItems.COLLAR_LIST, args[4]);
      } else {
         return args.length == 6 && args[5] != null ? Utils.getItemNameList(ModItems.EARPLUGS_LIST, args[5]) : super.func_184883_a(server, sender, args, targetPos);
      }
   }
}
