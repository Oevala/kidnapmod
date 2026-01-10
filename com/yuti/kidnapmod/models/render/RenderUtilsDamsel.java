package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.entities.EntityDamsel;
import com.yuti.kidnapmod.items.ItemBind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderUtilsDamsel {
   private static final RenderManager MANAGER = Minecraft.func_71410_x().func_175598_ae();
   public static final DamselKidnapRender damselKidnapModRenderDefault;
   public static final DamselKidnapRender damselKidnapModRenderSlime;
   public static final TiedUpRenderDamsel damselTiedUprenderBase;
   public static final TiedUpRenderDamsel damselTiedUprenderStraitJacket;
   public static final TiedUpRenderDamsel damselTiedUprenderEnclosed;
   public static final TiedUpRenderDamsel damselTiedUprenderWrapped;

   public static RenderLivingBase<EntityDamsel> getKidnapModDamselRenderer(EntityDamsel damsel) {
      if (damsel != null) {
         if (!damsel.isTiedUp()) {
            DamselKidnapRender render;
            if ((!damsel.hasSmallArms() || damsel.hasClothes()) && !damsel.hasClothesWithSmallArms()) {
               render = damselKidnapModRenderDefault;
            } else {
               render = damselKidnapModRenderSlime;
            }

            return render;
         }

         ItemStack currentBind = damsel.getCurrentBind();
         if (currentBind != null && !currentBind.func_190926_b()) {
            Item chest = currentBind.func_77973_b();
            if (chest instanceof ItemBind) {
               ItemBind bind = (ItemBind)chest;
               if (bind != null) {
                  TiedUpRenderDamsel render = bind.getTiedUpDamselRenderer();
                  return render;
               }
            }
         }
      }

      return null;
   }

   static {
      damselKidnapModRenderDefault = new DamselKidnapRender(MANAGER, new ModelPlayer(0.0F, false));
      damselKidnapModRenderSlime = new DamselKidnapRender(MANAGER, new ModelPlayer(0.0F, true));
      damselTiedUprenderBase = new TiedUpRenderDamsel(MANAGER, ModelsUtils.MODEL_TIED_UP);
      damselTiedUprenderStraitJacket = new TiedUpRenderDamsel(MANAGER, ModelsUtils.MODEL_STRAITJACKET);
      damselTiedUprenderEnclosed = new TiedUpRenderDamsel(MANAGER, ModelsUtils.MODEL_ENCLOSED);
      damselTiedUprenderWrapped = new TiedUpRenderDamsel(MANAGER, ModelsUtils.MODEL_WRAPPED);
   }
}
