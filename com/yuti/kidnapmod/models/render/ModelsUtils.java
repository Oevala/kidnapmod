package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.models.render.enclosed.ModelEnclosed;
import com.yuti.kidnapmod.models.render.enclosed.ModelTiedUpEnclosed;
import com.yuti.kidnapmod.models.render.straitjacket.ModelStraitJacket;
import com.yuti.kidnapmod.models.render.straitjacket.ModelTiedUpStraitJacket;
import com.yuti.kidnapmod.models.render.wrapped.ModelTiedUpWrapped;
import com.yuti.kidnapmod.models.render.wrapped.ModelWrapped;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;

public class ModelsUtils {
   public static final ModelPlayer MODEL_TIED_UP = new ModelTiedUpPlayer(0.0F, true);
   public static final ModelPlayer MODEL_ENCLOSED = new ModelTiedUpEnclosed(0.0F, true);
   public static final ModelPlayer MODEL_STRAITJACKET = new ModelTiedUpStraitJacket(0.0F, true);
   public static final ModelPlayer MODEL_WRAPPED = new ModelTiedUpWrapped(0.0F, true);
   public static final ModelBiped MODEL_COLLAR = new ModelBiped();
   public static final ModelPlayer MODEL_CLOTHES = new ModelPlayer(0.0F, false);
   public static final ModelBiped MODEL_EARPLUGS = new ModelBiped();
   public static final ModelBiped RESTRAINT_BEHIND_BACK = new ModelRopeArmor();
   public static final ModelBiped RESTRAINT_ENCLOSED = new ModelEnclosed();
   public static final ModelBiped RESTRAINT_STRAITJACKET = new ModelStraitJacket();
   public static final ModelBiped RESTRAINT_WRAPPED = new ModelWrapped();
   public static final ModelBiped RESTRAINT_HOOD = new ModelBiped(0.062F);
   private static Map<Float, ModelAdjusted> MODELS_ADJUSTED = new HashMap();

   public static ModelAdjusted getAdjustement(float adjustementValue) {
      if (adjustementValue < -4.0F) {
         adjustementValue = -4.0F;
      }

      if (adjustementValue > 4.0F) {
         adjustementValue = 4.0F;
      }

      if (MODELS_ADJUSTED.containsKey(adjustementValue)) {
         return (ModelAdjusted)MODELS_ADJUSTED.get(adjustementValue);
      } else {
         ModelAdjusted adjusted = new ModelAdjusted(0.058F, adjustementValue);
         MODELS_ADJUSTED.put(adjustementValue, adjusted);
         return adjusted;
      }
   }
}
