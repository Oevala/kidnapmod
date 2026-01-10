package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.init.ExtraRecipes;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.tasks.TyingDamselTask;
import com.yuti.kidnapmod.items.tasks.TyingPlayerTask;
import com.yuti.kidnapmod.items.tasks.TyingTask;
import com.yuti.kidnapmod.models.render.ModelsUtils;
import com.yuti.kidnapmod.models.render.RenderUtilsDamsel;
import com.yuti.kidnapmod.models.render.RenderUtilsPlayer;
import com.yuti.kidnapmod.models.render.TiedUpRenderDamsel;
import com.yuti.kidnapmod.models.render.TiedUpRenderPlayer;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketTying;
import com.yuti.kidnapmod.recipes.RecipeResistanceItemsMerger;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.UtilsParameters;
import com.yuti.kidnapmod.util.handlers.rules.GameRulesRegistryHandler;
import java.util.List;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.ValueType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBind extends ItemKidnapWearable implements IHasModel, ItemRestraining, ItemUsuableOnRestrainedPlayer, IHasResistance {
   private final int resistance;
   private final int mergePercent;
   private String gameRuleName;
   private String mergeGameRuleName;

   public ItemBind(String name, ExtraBondageMaterial materialIn, int resistance, int mergePercent) {
      super(name, materialIn);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 16;
      this.resistance = resistance;
      this.mergePercent = mergePercent;
      this.gameRuleName = "resistance_" + name;
      GameRulesRegistryHandler.preLoad(this.gameRuleName, Integer.toString(resistance), ValueType.NUMERICAL_VALUE);
      this.mergeGameRuleName = "merge_percent_resistance_" + name;
      ModItems.BIND_LIST.add(this);
      ExtraRecipes.registerRecipe("recipe_merge_binds" + name, new RecipeResistanceItemsMerger(this));
      GameRulesRegistryHandler.preLoad(this.mergeGameRuleName, Integer.toString(mergePercent), ValueType.NUMERICAL_VALUE);
   }

   public void func_77624_a(ItemStack stack, World world, List<String> addList, ITooltipFlag advanced) {
      addList.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.ropes", new Object[0]));
      if (world != null) {
         int resistance = Utils.getResistance(stack, world);
         if (resistance > 0) {
            addList.add(ChatFormatting.GREEN + "Resistance : " + resistance);
         }
      }

      if (!this.canBeStruggledOut(stack)) {
         addList.add(ChatFormatting.RED + "No struggling");
      }

      super.func_77624_a(stack, world, addList, advanced);
   }

   public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack stack = player.func_184586_b(hand);
      if (!player.field_70170_p.field_72995_K) {
         PlayerBindState kidnapperState = PlayerBindState.getInstance(player);
         if (kidnapperState != null) {
            TyingTask process = kidnapperState.getCurrentTyingTask();
            if (process != null) {
               process.stop();
               kidnapperState.setCurrentTyingTask((TyingTask)null);
               PacketHandler.INSTANCE.sendTo(new PacketTying(-1, UtilsParameters.getTyingUpPlayerDelay(world)), (EntityPlayerMP)player);
            }
         }
      }

      return new ActionResult(EnumActionResult.FAIL, stack);
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      if (player.field_70170_p.field_72995_K) {
         return true;
      } else {
         if (target instanceof EntityPlayer || target instanceof I_Kidnapped) {
            I_Kidnapped state = null;
            TyingTask process_to_load = null;
            if (target instanceof EntityPlayer) {
               state = PlayerBindState.getInstance((EntityPlayer)target);
               process_to_load = new TyingPlayerTask(stack, (PlayerBindState)state, UtilsParameters.getTyingUpPlayerDelay(player.field_70170_p));
            } else {
               state = (I_Kidnapped)target;
               process_to_load = new TyingDamselTask(stack, (I_Kidnapped)state, UtilsParameters.getTyingUpPlayerDelay(player.field_70170_p));
            }

            if (state != null) {
               if (((I_Kidnapped)state).canBeTiedUp()) {
                  PlayerBindState kidnapperState = PlayerBindState.getInstance(player);
                  if (kidnapperState != null) {
                     TyingTask process = kidnapperState.getCurrentTyingTask();
                     if (process == null || !((TyingTask)process).isSameTarget((I_Kidnapped)state) || ((TyingTask)process).isOutdated() || !ItemStack.func_77989_b(((TyingTask)process).getBind(), stack)) {
                        process = process_to_load;
                        kidnapperState.setCurrentTyingTask((TyingTask)process_to_load);
                        ((TyingTask)process_to_load).start();
                     }

                     ((TyingTask)process).update();
                     PacketHandler.INSTANCE.sendTo(new PacketTying(((TyingTask)process).getState(), UtilsParameters.getTyingUpPlayerDelay(player.field_70170_p)), (EntityPlayerMP)player);
                     if (((TyingTask)process).isStopped()) {
                        stack.func_190918_g(1);
                     }
                  }
               } else {
                  ItemStack oldBind = ((I_Kidnapped)state).replaceBind(stack);
                  if (oldBind != null) {
                     stack.func_190918_g(1);
                     ((I_Kidnapped)state).kidnappedDropItem(oldBind);
                  }
               }
            }
         }

         return true;
      }
   }

   public int getBaseResistance(World world) {
      GameRules rules = world.func_82736_K();
      return rules.func_82765_e(this.gameRuleName) ? rules.func_180263_c(this.gameRuleName) : this.resistance;
   }

   @SideOnly(Side.CLIENT)
   public TiedUpRenderPlayer getTiedUpPlayerRenderer() {
      return RenderUtilsPlayer.playerTiedUprenderBase;
   }

   @SideOnly(Side.CLIENT)
   public TiedUpRenderDamsel getTiedUpDamselRenderer() {
      return RenderUtilsDamsel.damselTiedUprenderBase;
   }

   public ExtraBondageItemType getType(ItemStack itemstack) {
      return ExtraBondageItemType.BIND;
   }

   @SideOnly(Side.CLIENT)
   public ModelBiped getBondageModel(EntityLivingBase entity, ItemStack stack, ExtraBondageItemType slot, ModelBiped modelBase) {
      return ModelsUtils.RESTRAINT_BEHIND_BACK;
   }

   public void onEquipped(ItemStack itemstack, EntityLivingBase living) {
      if (living instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)living;
         Utils.changePlayerSpeed(player, -0.10000000149011612D);
      }

      super.onEquipped(itemstack, living);
   }

   public void onUnequipped(ItemStack itemstack, EntityLivingBase living) {
      if (living instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)living;
         Utils.changePlayerSpeed(player, 0.0D);
         this.resetCurrentResistance(itemstack);
      }

      super.onUnequipped(itemstack, living);
   }

   public int getMergePercentRule(World world) {
      GameRules rules = world.func_82736_K();
      return rules.func_82765_e(this.mergeGameRuleName) ? rules.func_180263_c(this.mergeGameRuleName) : this.mergePercent;
   }

   public String getResistanceId() {
      return this.func_77658_a();
   }

   public boolean canBeStruggledOut(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      return nbt.func_74764_b("struggle") ? nbt.func_74767_n("struggle") : true;
   }

   public ItemStack setCanBeStruggledOut(ItemStack stack, boolean state) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("struggle", state);
      stack.func_77982_d(nbt);
      return stack;
   }

   public void notifyStruggle(EntityPlayer player) {
   }

   public ItemStack setCurrentResistance(ItemStack stack, int resistance) {
      if (stack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            nbt.func_74768_a("currentresistance", resistance);
            stack.func_77982_d(nbt);
         }
      }

      return stack;
   }

   public int getCurrentResistance(ItemStack stack, World world) {
      if (stack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("currentresistance")) {
            int resistance = nbt.func_74762_e("currentresistance");
            if (resistance > 0) {
               return resistance;
            }
         }
      }

      return Utils.getResistance(stack, world);
   }

   public ItemStack resetCurrentResistance(ItemStack stack) {
      if (stack != null) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("currentresistance")) {
            nbt.func_82580_o("currentresistance");
            if (nbt.func_186856_d() == 0) {
               stack.func_77982_d((NBTTagCompound)null);
            } else {
               stack.func_77982_d(nbt);
            }
         }
      }

      return stack;
   }
}
