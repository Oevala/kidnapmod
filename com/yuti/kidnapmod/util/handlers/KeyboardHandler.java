package com.yuti.kidnapmod.util.handlers;

import com.yuti.kidnapmod.init.KeyBindings;
import com.yuti.kidnapmod.network.PacketAskForBounties;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketStruggleServer;
import com.yuti.kidnapmod.network.PacketTightenBinds;
import com.yuti.kidnapmod.network.extrainventory.PacketOpenExtraBondageInventory;
import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
public class KeyboardHandler {
   @SideOnly(Side.CLIENT)
   @SubscribeEvent(
      priority = EventPriority.NORMAL,
      receiveCanceled = true
   )
   public static void onEvent(KeyInputEvent event) {
      EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
      PlayerBindState state = PlayerBindState.getInstance(player);
      if (state != null) {
         if (KeyBindings.KEY_STRUGGLE.func_151468_f()) {
            PacketHandler.INSTANCE.sendToServer(new PacketStruggleServer());
         }

         if (KeyBindings.KEY_TIGHTEN.func_151468_f() && !state.isTiedUp()) {
            PacketHandler.INSTANCE.sendToServer(new PacketTightenBinds());
         }

         if (KeyBindings.KEY_BOUNTIES_LIST.func_151468_f()) {
            PacketHandler.INSTANCE.sendToServer(new PacketAskForBounties());
         }

         if (KeyBindings.KEY_DISABLE_TALK_AREA.func_151468_f()) {
            Minecraft.func_71410_x().field_71439_g.func_71165_d("/talkarea");
         }

         if (KeyBindings.KEY_TALKPREF.func_151468_f()) {
            Minecraft.func_71410_x().field_71439_g.func_71165_d("/talkpref");
         }

         if (KeyBindings.KEY_NORP.func_151468_f()) {
            Minecraft.func_71410_x().field_71439_g.func_71165_d("/norp");
         }

         if (KeyBindings.KEY_EXTENDED_BONDAGE_INVENTORY.func_151468_f()) {
            PacketHandler.INSTANCE.sendToServer(new PacketOpenExtraBondageInventory());
         }

      }
   }
}
