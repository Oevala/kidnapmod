package com.yuti.kidnapmod.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.yuti.kidnapmod.init.KidnapModSoundEvents;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.util.IHasModel;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.UtilsParameters;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class ItemShockerController extends ItemOnwerTarget implements IHasModel, ItemUsuableOnRestrainedPlayer {
   private static int BASE_RADIUS = 50;

   public ItemShockerController(String name) {
      super(name);
   }

   public void func_77624_a(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
      super.func_77624_a(stack, worldIn, tooltip, flagIn);
      if (stack.func_77942_o()) {
         NBTTagCompound nbt = stack.func_77978_p();
         if (nbt.func_74764_b("ownerName")) {
            tooltip.add(ChatFormatting.GOLD + "Owner : " + nbt.func_74779_i("ownerName"));
         } else {
            tooltip.add(ChatFormatting.GRAY + "Need to be claimed (use /shocker claim)");
         }

         if (this.isBroadcastEnabled(stack)) {
            tooltip.add(ChatFormatting.DARK_RED + "Broadcast");
         } else if (nbt.func_74764_b("targetName")) {
            tooltip.add(ChatFormatting.BLUE + "Target : " + nbt.func_74779_i("targetName"));
         }
      } else {
         tooltip.add(ChatFormatting.GRAY + "Need to be claimed (use /shocker claim)");
      }

      tooltip.add(ChatFormatting.GREEN + "Radius : " + getRadius(worldIn, stack));
   }

   public boolean isBroadcastEnabled(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      return nbt.func_74764_b("broadcast") ? nbt.func_74767_n("broadcast") : false;
   }

   public ItemStack enableBroadcast(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("broadcast", true);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ItemStack disableBroadcast(ItemStack stack) {
      NBTTagCompound nbt = Utils.getTagComponent(stack);
      nbt.func_74757_a("broadcast", false);
      stack.func_77982_d(nbt);
      return stack;
   }

   public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      if (!world.field_72995_K && player != null) {
         PlayerBindState state = PlayerBindState.getInstance(player);
         if (state != null && !state.isTiedUp()) {
            ItemStack controllerStack = player.func_184586_b(hand);
            if (controllerStack.func_77973_b() instanceof ItemShockerController) {
               if (this.hasOwner(controllerStack)) {
                  ItemShockerController controller = (ItemShockerController)controllerStack.func_77973_b();
                  List<I_Kidnapped> targets = this.getTargets(world, controllerStack, this.getOwnerId(controllerStack), player);
                  if (controller.isBroadcastEnabled(controllerStack)) {
                     Iterator var8 = targets.iterator();

                     while(var8.hasNext()) {
                        I_Kidnapped target = (I_Kidnapped)var8.next();
                        if (target != null) {
                           Utils.shockEntity(target, player);
                        }
                     }

                     if (targets.isEmpty()) {
                        Utils.sendErrorMessageToEntity(player, "No one has been shocked");
                     } else {
                        this.playSoundActivated(player);
                     }
                  } else if (controller.hasTarget(controllerStack)) {
                     I_Kidnapped target = this.getTargetInList(controllerStack, targets);
                     if (target != null) {
                        this.playSoundActivated(player);
                        target.shockKidnapped();
                     } else {
                        String targetName = this.getTargetName(controllerStack);
                        if (targetName != null) {
                           Utils.sendErrorMessageToEntity(player, targetName + " can't be shocked or is too far away");
                        }
                     }
                  } else {
                     Utils.sendErrorMessageToEntity(player, "You must set a target or enable broadcast");
                  }
               } else {
                  Utils.sendErrorMessageToEntity(player, "You must claim the shocker");
               }
            }
         }
      }

      return super.func_77659_a(world, player, hand);
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      if (!player.field_70170_p.field_72995_K && (target instanceof EntityPlayer || target instanceof I_Kidnapped)) {
         PlayerBindState playerState = PlayerBindState.getInstance(player);
         if (playerState != null && !playerState.isTiedUp()) {
            ItemStack controller = player.func_184586_b(hand);
            if (controller != null && controller.func_77973_b() instanceof ItemShockerController) {
               if (!this.hasOwner(controller)) {
                  this.setOwner(controller, player);
               }

               I_Kidnapped targetState = null;
               if (target instanceof EntityPlayer) {
                  EntityPlayer targetPlayer = (EntityPlayer)target;
                  targetState = PlayerBindState.getInstance(targetPlayer);
               } else {
                  targetState = (I_Kidnapped)target;
               }

               this.setTarget(controller, target);
               Utils.sendValidMessageToEntity(player, "Shocker successfully connected to " + ((I_Kidnapped)targetState).getKidnappedName());
            }
         }
      }

      return true;
   }

   private List<I_Kidnapped> getTargets(World world, ItemStack stack, UUID ownerId, EntityPlayer triggerer) {
      List<I_Kidnapped> potentialTargets = Utils.getKidnapableEntitiesAround(world, triggerer.func_180425_c(), (double)getRadius(world, stack));
      List<I_Kidnapped> targets = new ArrayList();
      Iterator var7 = potentialTargets.iterator();

      while(true) {
         I_Kidnapped target;
         ItemStack stackCollar;
         ItemShockCollar collar;
         do {
            do {
               do {
                  do {
                     do {
                        if (!var7.hasNext()) {
                           return targets;
                        }

                        target = (I_Kidnapped)var7.next();
                     } while(target == null);
                  } while(!target.hasCollar());

                  stackCollar = target.getCurrentCollar();
               } while(stackCollar == null);
            } while(!(stackCollar.func_77973_b() instanceof ItemShockCollar));

            collar = (ItemShockCollar)stackCollar.func_77973_b();
         } while(!collar.isOwner(stackCollar, ownerId) && !collar.isPublic(stackCollar));

         targets.add(target);
      }
   }

   private I_Kidnapped getTargetInList(ItemStack stack, List<I_Kidnapped> targets) {
      Iterator var3 = targets.iterator();

      I_Kidnapped target;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         target = (I_Kidnapped)var3.next();
      } while(!this.isTarget(stack, target));

      return target;
   }

   private void playSoundActivated(EntityPlayer triggerer) {
      if (triggerer != null && triggerer.field_70170_p != null && !triggerer.field_70170_p.field_72995_K) {
         triggerer.field_70170_p.func_184133_a((EntityPlayer)null, triggerer.func_180425_c(), KidnapModSoundEvents.SHOCKER_ACTIVATED, SoundCategory.AMBIENT, 0.15F, 1.0F);
      }

   }

   public static int getRadius(World world, ItemStack stack) {
      if (stack != null && stack.func_77973_b() instanceof ItemShockerController) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null && nbt.func_74764_b("radius")) {
            return nbt.func_74762_e("radius");
         }
      }

      return getBaseRadius(world);
   }

   public static void setRadius(ItemStack stack, int radius) {
      if (radius >= 0 && stack != null && stack.func_77973_b() instanceof ItemShockerController) {
         NBTTagCompound nbt = Utils.getTagComponent(stack);
         if (nbt != null) {
            nbt.func_74768_a("radius", radius);
            stack.func_77982_d(nbt);
         }
      }

   }

   public static ItemStack mergeShockers(List<ItemStack> stacks, World world) {
      if (stacks != null && !stacks.isEmpty() && stacks.size() > 1) {
         int newRadius = 0;

         ItemStack stack;
         for(Iterator var3 = stacks.iterator(); var3.hasNext(); newRadius += getRadius(world, stack)) {
            stack = (ItemStack)var3.next();
            if (stack == null || stack.func_190926_b() || !(stack.func_77973_b() instanceof ItemShockerController)) {
               return ItemStack.field_190927_a;
            }
         }

         ItemStack stackMerged = new ItemStack(ModItems.SHOCKER_CONTROLLER);
         int maxValueShocker = UtilsParameters.getShockerMergeMax(world);
         if (maxValueShocker >= 0 && newRadius > maxValueShocker) {
            newRadius = maxValueShocker;
         }

         setRadius(stackMerged, newRadius);
         return stackMerged.func_77979_a(1).func_77946_l();
      } else {
         return ItemStack.field_190927_a;
      }
   }

   public static int getBaseRadius(World world) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules.func_82765_e("shocker_controller_base_radius")) {
            return rules.func_180263_c("shocker_controller_base_radius");
         }
      }

      return BASE_RADIUS;
   }
}
