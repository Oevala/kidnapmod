package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.world.KidnapExplosion;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandKidnapExplosion extends CommandCheat {
   public String func_71517_b() {
      return "kidnapexplosion";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.kidnapexplosion";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      World world = sender.func_130014_f_();
      if (world != null && !world.field_72995_K) {
         BlockPos pos = sender.func_180425_c();
         if (pos != null) {
            ItemBind bind = ModItems.ROPES;
            ItemGag gag = null;
            ItemBlindfold blindfold = null;
            ItemEarplugs earplugs = null;
            ItemCollar collar = null;
            String url = null;
            int radius = 10;
            if (params.length >= 1) {
               try {
                  radius = Integer.parseInt(params[0]);
                  if (radius < 0) {
                     radius = 10;
                  }
               } catch (NumberFormatException var21) {
                  radius = 10;
               }
            }

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

            ItemStack bindStack = new ItemStack(bind);
            ItemStack gagStack = new ItemStack(gag);
            ItemStack blindfoldStack = new ItemStack(blindfold);
            ItemStack earplugsStack = new ItemStack(earplugs);
            ItemStack collarStack = new ItemStack(collar);
            ItemStack clothesStack = null;
            if (url != null) {
               clothesStack = new ItemStack(ModItems.CLOTHES);
               clothesStack = ModItems.CLOTHES.setDynamicTextureUrl(clothesStack, url);
            }

            if (sender instanceof EntityPlayer && collar != null && collarStack != null) {
               collar.addOwner(collarStack, (EntityPlayer)sender);
            }

            KidnapExplosion explosion = new KidnapExplosion(sender.func_130014_f_(), pos, radius, bindStack, gagStack, blindfoldStack, earplugsStack, collarStack, clothesStack);
            if (sender instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)sender;
               explosion.explode(player);
            } else {
               explosion.explode();
            }
         }

      }
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 1 && args[0] != null && args[0].equals("")) {
         List<String> radius = new ArrayList();
         radius.add("10");
         return radius;
      } else if (args.length == 2 && args[1] != null) {
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
