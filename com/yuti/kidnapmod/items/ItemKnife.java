package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.handlers.rules.GameRulesRegistryHandler;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumHand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.GameRules.ValueType;

public class ItemKnife extends Item implements IHasModel, ItemUsuableOnRestrainedPlayer {
   private int successProbability;
   private String gameRuleName;

   public ItemKnife(String name, int successProbability) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 8;
      this.successProbability = successProbability;
      this.gameRuleName = "probability_" + name;
      GameRulesRegistryHandler.preLoad(this.gameRuleName, Integer.toString(successProbability), ValueType.NUMERICAL_VALUE);
      ModItems.ITEMS.add(this);
      ModItems.KNIFES_LIST.add(this);
   }

   public void func_77624_a(ItemStack stack, World player, List<String> addList, ITooltipFlag advanced) {
      addList.add(ChatFormatting.GRAY + I18n.func_135052_a("item.desc.knife", new Object[]{this.successProbability}));
   }

   public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (!world.field_72995_K) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state.isTiedUp()) {
            int rand = 1 + player.func_70681_au().nextInt(99);
            if ((double)rand <= this.getSuccessProbabilityOnWorld(world)) {
               state.dropBondageItems(false);
               state.untie();
               state.free();
            }

            ItemStack stack = player.func_184586_b(hand);
            stack.func_190918_g(1);
            if (stack.func_190926_b()) {
               stack = ItemStack.field_190927_a;
            }

            CooldownTracker tracker = player.func_184811_cZ();
            if (tracker != null) {
               tracker.func_185145_a(this, 100);
            }
         }
      }

      return super.func_77659_a(world, player, hand);
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      if (!player.field_70170_p.field_72995_K && (target instanceof EntityPlayer || target instanceof I_Kidnapped)) {
         I_Kidnapped targetState = null;
         if (target instanceof EntityPlayer) {
            EntityPlayer targetPlayer = (EntityPlayer)target;
            targetState = PlayerBindState.getInstance(targetPlayer);
         } else {
            targetState = (I_Kidnapped)target;
         }

         PlayerBindState playerState = PlayerBindState.getInstance(player);
         if (targetState != null && ((I_Kidnapped)targetState).isTiedUp() && !((I_Kidnapped)targetState).isForSell() && playerState != null && !playerState.isTiedUp()) {
            int rand = 1 + player.func_70681_au().nextInt(99);
            if ((double)rand <= this.getSuccessProbabilityOnWorld(player.field_70170_p)) {
               ((I_Kidnapped)targetState).dropBondageItems(false);
               ((I_Kidnapped)targetState).untie();
               ((I_Kidnapped)targetState).free();
            }

            stack.func_190918_g(1);
            CooldownTracker tracker = player.func_184811_cZ();
            if (tracker != null) {
               tracker.func_185145_a(this, 100);
            }
         }
      }

      return true;
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }

   public double getSuccessProbabilityOnWorld(World world) {
      GameRules rules = world.func_82736_K();
      return rules.func_82765_e(this.gameRuleName) ? (double)rules.func_180263_c(this.gameRuleName) : (double)this.successProbability;
   }
}
