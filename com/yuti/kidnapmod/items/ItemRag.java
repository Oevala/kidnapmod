package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.KidnapModMain;
import com.yuti.kidnapmod.init.ModCreativeTabs;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.UtilsParameters;
import com.yuti.kidnapmod.util.time.Timer;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemRag extends Item implements IHasModel, ItemUsuableOnRestrainedPlayer {
   public ItemRag(String name) {
      this.func_77655_b(name);
      this.setRegistryName(name);
      this.func_77637_a(ModCreativeTabs.kidnapTab);
      this.field_77777_bU = 1;
      ModItems.ITEMS.add(this);
   }

   public static boolean isWet(ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof ItemRag) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("wet")) {
            Timer timer = getUseTiemr(stack);
            return (timer == null || timer != null && timer.getSecondsRemaining() > 0) && nbt.func_74767_n("wet");
         }
      }

      return false;
   }

   public static void setWet(World world, ItemStack stack, boolean wet) {
      if (stack != null && stack.func_77973_b() instanceof ItemRag) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            nbt.func_74757_a("wet", wet);
            stack.func_77982_d(nbt);
            if (wet) {
               int duration = UtilsParameters.getChloroformRagTime(world);
               Timer timer = new Timer(duration);
               setUseTimer(stack, timer);
            }
         }
      }

   }

   public static Timer getUseTiemr(ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof ItemRag) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("useTimer")) {
            try {
               Timer timer = (Timer)Utils.deserialize(nbt.func_74770_j("useTimer"));
               return timer;
            } catch (IOException | ClassNotFoundException var3) {
               var3.printStackTrace();
            }
         }
      }

      return null;
   }

   public static void setUseTimer(ItemStack stack, Timer useTiemr) {
      if (stack != null && stack.func_77973_b() instanceof ItemRag) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            try {
               nbt.func_74773_a("useTimer", Utils.serialize(useTiemr));
            } catch (IOException var4) {
               var4.printStackTrace();
            }

            stack.func_77982_d(nbt);
         }
      }

   }

   public boolean func_77636_d(ItemStack stack) {
      return isWet(stack);
   }

   public void registerModels() {
      KidnapModMain.proxy.registerItemRenderer(this, 0, "inventory");
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
      if (playerIn != null && !playerIn.field_70170_p.field_72995_K) {
         boolean enabled = UtilsParameters.getChloroformEnabled(playerIn.field_70170_p);
         if (!enabled) {
            return false;
         }

         PlayerBindState state = PlayerBindState.getInstance(playerIn);
         if (state != null && !state.isTiedUp() && (target instanceof EntityPlayer || target instanceof I_Kidnapped)) {
            playerIn.func_184811_cZ().func_185145_a(ModItems.CHLOROFORM_BOTTLE, 50);
            playerIn.func_184811_cZ().func_185145_a(this, 50);
            if (isWet(stack)) {
               I_Kidnapped targetState = null;
               if (target instanceof EntityPlayer) {
                  EntityPlayer targetPlayer = (EntityPlayer)target;
                  targetState = PlayerBindState.getInstance(targetPlayer);
               } else {
                  targetState = (I_Kidnapped)target;
               }

               if (targetState != null) {
                  setWet(playerIn.field_70170_p, stack, false);
                  int duration = UtilsParameters.getChloroformEffectDuration(playerIn.field_70170_p);
                  ((I_Kidnapped)targetState).applyChloroform(duration);
               }
            }
         }
      }

      return super.func_111207_a(stack, playerIn, target, hand);
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
      if (isWet(stack)) {
         tooltip.add(ChatFormatting.GOLD + "Wet");
      }

   }
}
