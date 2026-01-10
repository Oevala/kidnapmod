package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemHelper;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.util.UtilsClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

public class RenderUtilsPlayer {
   private static final RenderManager MANAGER = Minecraft.func_71410_x().func_175598_ae();
   public static final PlayerKidnapRender kidnapModRenderDefault;
   public static final PlayerKidnapRender kidnapModRenderSlime;
   public static final TiedUpRenderPlayer playerTiedUprenderBase;
   public static final TiedUpRenderPlayer playertiedUprenderStraitJacket;
   public static final TiedUpRenderPlayer playerTiedUprenderEnclosed;
   public static final TiedUpRenderPlayer playerTiedUprenderWrapped;

   public static RenderLivingBase<AbstractClientPlayer> getKidnapModPlayerRenderer(EntityPlayer player) {
      PlayerBindState state = PlayerBindState.getInstance(player);
      if (state != null) {
         if (!state.isTiedUp()) {
            PlayerKidnapRender render = kidnapModRenderDefault;
            if (player instanceof AbstractClientPlayer && (UtilsClient.hasSmallArms((AbstractClientPlayer)player) && !state.hasClothes() || state.hasClothesWithSmallArms())) {
               render = kidnapModRenderSlime;
            }

            return render;
         }

         Item chest = ExtraBondageItemHelper.getItemStackFromSlot(player, ExtraBondageItemType.BIND).func_77973_b();
         if (chest instanceof ItemBind) {
            ItemBind bind = (ItemBind)chest;
            if (bind != null) {
               TiedUpRenderPlayer render = bind.getTiedUpPlayerRenderer();
               return render;
            }
         }
      }

      return null;
   }

   static {
      kidnapModRenderDefault = new PlayerKidnapRender(MANAGER, new ModelPlayer(0.0F, false));
      kidnapModRenderSlime = new PlayerKidnapRender(MANAGER, new ModelPlayer(0.0F, true));
      playerTiedUprenderBase = new TiedUpRenderPlayer(MANAGER, ModelsUtils.MODEL_TIED_UP);
      playertiedUprenderStraitJacket = new TiedUpRenderPlayer(MANAGER, ModelsUtils.MODEL_STRAITJACKET);
      playerTiedUprenderEnclosed = new TiedUpRenderPlayer(MANAGER, ModelsUtils.MODEL_ENCLOSED);
      playerTiedUprenderWrapped = new TiedUpRenderPlayer(MANAGER, ModelsUtils.MODEL_WRAPPED);
   }
}
