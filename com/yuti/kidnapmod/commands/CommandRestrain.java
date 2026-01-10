package com.yuti.kidnapmod.commands;

import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandRestrain extends CommandTargetPlayer {
   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] params) throws CommandException {
      List<PlayerBindState> states = this.getTargetsState(server, sender, params);
      ItemBind bind = ModItems.ROPES;
      int meta = 0;
      if (params.length >= 2) {
         String paramItemId = params[1];
         if (paramItemId != null) {
            Item item = Item.func_111206_d(paramItemId);
            if (item instanceof ItemBind) {
               bind = (ItemBind)item;
            }
         }
      }

      if (params.length >= 3) {
         try {
            meta = Integer.parseInt(params[2]);
         } catch (NumberFormatException var9) {
            meta = 0;
         }
      }

      Iterator var10 = states.iterator();

      while(var10.hasNext()) {
         PlayerBindState state = (PlayerBindState)var10.next();
         if (state != null && !state.isTiedUp()) {
            state.putBindOn(new ItemStack(bind, 1, meta));
            Utils.sendValidMessageToEntity(sender, state.getPlayer().func_70005_c_() + " is now tied up.");
            Utils.sendInfoMessageToEntity(state.getPlayer(), "You have been tied up!");
         }
      }

   }

   public String func_71517_b() {
      return "restrain";
   }

   public String func_71518_a(ICommandSender sender) {
      return "command.restrain";
   }

   public List<String> func_184883_a(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
      if (args.length == 2 && args[1] != null) {
         List<ItemBind> items = ModItems.BIND_LIST;
         List<String> itemsIds = new ArrayList();
         Iterator var7 = items.iterator();

         while(var7.hasNext()) {
            Item item = (Item)var7.next();
            String name = item.getRegistryName().toString();
            if (name.contains(args[1])) {
               itemsIds.add(name);
            }
         }

         return itemsIds;
      } else {
         return super.func_184883_a(server, sender, args, targetPos);
      }
   }
}
