package com.yuti.kidnapmod.entities;

import com.yuti.kidnapmod.data.SlavesUnloadLocationsData;
import com.yuti.kidnapmod.entities.ai.EntityAIAvoidKidnapper;
import com.yuti.kidnapmod.entities.ai.EntityAIPanicTiedUp;
import com.yuti.kidnapmod.entities.ai.EntityAISwimingTiedUp;
import com.yuti.kidnapmod.entities.ai.EntityAIWanderExceptWhenTiedUp;
import com.yuti.kidnapmod.entities.ai.EntityAIWatchClosestBlindfolded;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemIdentifier;
import com.yuti.kidnapmod.init.KidnapModSoundEvents;
import com.yuti.kidnapmod.init.ModItems;
import com.yuti.kidnapmod.items.IHasBlindingEffect;
import com.yuti.kidnapmod.items.ItemBind;
import com.yuti.kidnapmod.items.ItemBlindfold;
import com.yuti.kidnapmod.items.ItemClothes;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.items.ItemEarplugs;
import com.yuti.kidnapmod.items.ItemGag;
import com.yuti.kidnapmod.items.ItemGaggingEffect;
import com.yuti.kidnapmod.items.ItemKidnapWearable;
import com.yuti.kidnapmod.loaders.common.GagTalkLoader;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapper;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import com.yuti.kidnapmod.util.teleport.TeleportHelper;
import com.yuti.kidnapmod.util.time.Timer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public abstract class EntityDamsel extends EntityCreature implements IAnimals, I_Kidnapped {
   protected static final DataParameter<String> DAMSEL_NAME;
   protected static final DataParameter<Boolean> SMALL_ARMS;
   protected static final DataParameter<ItemStack> BINDS;
   protected static final DataParameter<ItemStack> GAG;
   protected static final DataParameter<ItemStack> BLINDFOLD;
   protected static final DataParameter<ItemStack> COLLAR;
   protected static final DataParameter<ItemStack> EARPLUGS;
   protected static final DataParameter<ItemStack> CLOTHES;
   protected static final DataParameter<Float> ADJUST_GAGS;
   protected static final DataParameter<Float> ADJUST_BLINDFOLDS;
   protected static final DataParameter<Boolean> HEAD_LAYER;
   protected static final DataParameter<Boolean> BODY_LAYER;
   protected static final DataParameter<Boolean> LEFT_ARM_LAYER;
   protected static final DataParameter<Boolean> RIGHT_ARM_LAYER;
   protected static final DataParameter<Boolean> LEFT_LEG_LAYER;
   protected static final DataParameter<Boolean> RIGHT_LEG_LAYER;
   private UUID currentEditor;
   private Position currentEditionPosition;
   private Map<UUID, Timer> serviceTargets = new HashMap();
   private Timer chloroTimer;

   public EntityDamsel(World world) {
      super(world);
      ((PathNavigateGround)this.func_70661_as()).func_179691_c(true);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      this.field_70180_af.func_187214_a(BINDS, ItemStack.field_190927_a);
      this.field_70180_af.func_187214_a(GAG, ItemStack.field_190927_a);
      this.field_70180_af.func_187214_a(BLINDFOLD, ItemStack.field_190927_a);
      this.field_70180_af.func_187214_a(COLLAR, ItemStack.field_190927_a);
      this.field_70180_af.func_187214_a(EARPLUGS, ItemStack.field_190927_a);
      this.field_70180_af.func_187214_a(CLOTHES, ItemStack.field_190927_a);
      this.field_70180_af.func_187214_a(ADJUST_GAGS, 0.0F);
      this.field_70180_af.func_187214_a(ADJUST_BLINDFOLDS, 0.0F);
      this.field_70180_af.func_187214_a(HEAD_LAYER, true);
      this.field_70180_af.func_187214_a(BODY_LAYER, true);
      this.field_70180_af.func_187214_a(LEFT_ARM_LAYER, true);
      this.field_70180_af.func_187214_a(RIGHT_ARM_LAYER, true);
      this.field_70180_af.func_187214_a(LEFT_LEG_LAYER, true);
      this.field_70180_af.func_187214_a(RIGHT_LEG_LAYER, true);
   }

   public String getDamselName() {
      return (String)this.field_70180_af.func_187225_a(DAMSEL_NAME);
   }

   public boolean hasSmallArms() {
      return (Boolean)this.field_70180_af.func_187225_a(SMALL_ARMS);
   }

   protected ItemStack getBinds() {
      return (ItemStack)this.field_70180_af.func_187225_a(BINDS);
   }

   protected ItemStack getGag() {
      return (ItemStack)this.field_70180_af.func_187225_a(GAG);
   }

   protected ItemStack getBlindfold() {
      return (ItemStack)this.field_70180_af.func_187225_a(BLINDFOLD);
   }

   protected ItemStack getCollar() {
      return (ItemStack)this.field_70180_af.func_187225_a(COLLAR);
   }

   protected ItemStack getEarplugs() {
      return (ItemStack)this.field_70180_af.func_187225_a(EARPLUGS);
   }

   protected ItemStack getClothes() {
      return (ItemStack)this.field_70180_af.func_187225_a(CLOTHES);
   }

   public float getGagsAdjustement() {
      return (Float)this.field_70180_af.func_187225_a(ADJUST_GAGS);
   }

   public float getBlindfoldsAdjustement() {
      return (Float)this.field_70180_af.func_187225_a(ADJUST_BLINDFOLDS);
   }

   public boolean hasHeadLayer() {
      return (Boolean)this.field_70180_af.func_187225_a(HEAD_LAYER);
   }

   public boolean hasBodyLayer() {
      return (Boolean)this.field_70180_af.func_187225_a(BODY_LAYER);
   }

   public boolean hasLeftArmLayer() {
      return (Boolean)this.field_70180_af.func_187225_a(LEFT_ARM_LAYER);
   }

   public boolean hasRightArmLayer() {
      return (Boolean)this.field_70180_af.func_187225_a(RIGHT_ARM_LAYER);
   }

   public boolean hasLeftLegLayer() {
      return (Boolean)this.field_70180_af.func_187225_a(LEFT_LEG_LAYER);
   }

   public boolean hasRightLegLayer() {
      return (Boolean)this.field_70180_af.func_187225_a(RIGHT_LEG_LAYER);
   }

   public void setDamselName(String name) {
      this.field_70180_af.func_187227_b(DAMSEL_NAME, name);
   }

   public void setSmallArms(boolean smallArms) {
      this.field_70180_af.func_187227_b(SMALL_ARMS, smallArms);
   }

   protected void setBinds(ItemStack binds, boolean playSound) {
      this.field_70180_af.func_187227_b(BINDS, binds);
      if (playSound) {
         this.playBondageEquipmentSound(binds);
      }

   }

   protected void setBinds(ItemStack binds) {
      this.setBinds(binds, true);
   }

   protected void setGag(ItemStack gag, boolean playSound) {
      this.field_70180_af.func_187227_b(GAG, gag);
      if (playSound) {
         this.playBondageEquipmentSound(gag);
      }

   }

   protected void setGag(ItemStack gag) {
      this.setGag(gag, true);
   }

   protected void setBlindfold(ItemStack blindfold, boolean playSound) {
      this.field_70180_af.func_187227_b(BLINDFOLD, blindfold);
      if (playSound) {
         this.playBondageEquipmentSound(blindfold);
      }

   }

   protected void setBlindfold(ItemStack blindfold) {
      this.setBlindfold(blindfold, true);
   }

   protected void setCollar(ItemStack collar, boolean playSound) {
      this.field_70180_af.func_187227_b(COLLAR, collar);
      this.currentEditor = null;
      if (playSound) {
         this.playBondageEquipmentSound(collar);
      }

   }

   protected void setCollar(ItemStack collar) {
      this.setCollar(collar, true);
   }

   public synchronized void reloadCollar(ItemStack collar) {
      if (this.hasCollar() && collar != null && collar.func_77973_b() instanceof ItemCollar) {
         this.setCollar(collar.func_77946_l(), false);
         this.func_184212_Q().func_187217_b(COLLAR);
      }

   }

   protected void setEarplugs(ItemStack earplugs, boolean playSound) {
      this.field_70180_af.func_187227_b(EARPLUGS, earplugs);
      if (playSound) {
         this.playBondageEquipmentSound(earplugs);
      }

   }

   protected void setEarplugs(ItemStack earplugs) {
      this.setEarplugs(earplugs, true);
   }

   protected void setClothes(ItemStack clothes, boolean playSound) {
      this.field_70180_af.func_187227_b(CLOTHES, clothes);
      if (playSound) {
         this.playBondageEquipmentSound(clothes);
      }

   }

   protected void setClothes(ItemStack clothes) {
      this.setClothes(clothes, true);
   }

   public void setGagsAdjustement(float adjustement) {
      this.field_70180_af.func_187227_b(ADJUST_GAGS, this.getRightAdjustementValue(adjustement));
   }

   public void setBlindfoldsAdjustement(float adjustement) {
      this.field_70180_af.func_187227_b(ADJUST_BLINDFOLDS, this.getRightAdjustementValue(adjustement));
   }

   public void setHeadLayer(boolean state) {
      this.field_70180_af.func_187227_b(HEAD_LAYER, state);
   }

   public void setBodyLayer(boolean state) {
      this.field_70180_af.func_187227_b(BODY_LAYER, state);
   }

   public void setLeftArmLayer(boolean state) {
      this.field_70180_af.func_187227_b(LEFT_ARM_LAYER, state);
   }

   public void setRightArmLayer(boolean state) {
      this.field_70180_af.func_187227_b(RIGHT_ARM_LAYER, state);
   }

   public void setLeftLegLayer(boolean state) {
      this.field_70180_af.func_187227_b(LEFT_LEG_LAYER, state);
   }

   public void setRightLegLayer(boolean state) {
      this.field_70180_af.func_187227_b(RIGHT_LEG_LAYER, state);
   }

   private float getRightAdjustementValue(float value) {
      if (value > 4.0F) {
         return 4.0F;
      } else {
         return value < -4.0F ? -4.0F : value;
      }
   }

   protected void playBondageEquipmentSound(ItemStack stack) {
      if (stack != null && stack.func_77973_b() != null && stack.func_77973_b() instanceof ItemKidnapWearable) {
         ItemKidnapWearable item = (ItemKidnapWearable)stack.func_77973_b();
         item.playSound(stack, this);
      }

   }

   public void func_70014_b(NBTTagCompound compound) {
      super.func_70014_b(compound);
      compound.func_74778_a("damsel_name", (String)this.field_70180_af.func_187225_a(DAMSEL_NAME));
      compound.func_74757_a("smallArms", (Boolean)this.field_70180_af.func_187225_a(SMALL_ARMS));
      compound.func_74757_a("headLayer", (Boolean)this.field_70180_af.func_187225_a(HEAD_LAYER));
      compound.func_74757_a("bodyLayer", (Boolean)this.field_70180_af.func_187225_a(BODY_LAYER));
      compound.func_74757_a("leftArmLayer", (Boolean)this.field_70180_af.func_187225_a(LEFT_ARM_LAYER));
      compound.func_74757_a("rightArmLayer", (Boolean)this.field_70180_af.func_187225_a(RIGHT_ARM_LAYER));
      compound.func_74757_a("leftLegLayer", (Boolean)this.field_70180_af.func_187225_a(LEFT_LEG_LAYER));
      compound.func_74757_a("rightLegLayer", (Boolean)this.field_70180_af.func_187225_a(RIGHT_LEG_LAYER));
      NBTTagCompound tagClothes;
      if (this.getBinds() != null && !this.getBinds().func_190926_b()) {
         tagClothes = new NBTTagCompound();
         this.getBinds().func_77955_b(tagClothes);
         compound.func_74782_a("binds", tagClothes);
      }

      if (this.getGag() != null && !this.getGag().func_190926_b()) {
         tagClothes = new NBTTagCompound();
         this.getGag().func_77955_b(tagClothes);
         compound.func_74782_a("gag", tagClothes);
      }

      if (this.getBlindfold() != null && !this.getBlindfold().func_190926_b()) {
         tagClothes = new NBTTagCompound();
         this.getBlindfold().func_77955_b(tagClothes);
         compound.func_74782_a("blindfold", tagClothes);
      }

      if (this.getCollar() != null && !this.getCollar().func_190926_b()) {
         tagClothes = new NBTTagCompound();
         this.getCollar().func_77955_b(tagClothes);
         compound.func_74782_a("collar", tagClothes);
      }

      if (this.getEarplugs() != null && !this.getEarplugs().func_190926_b()) {
         tagClothes = new NBTTagCompound();
         this.getEarplugs().func_77955_b(tagClothes);
         compound.func_74782_a("earplugs", tagClothes);
      }

      if (this.getClothes() != null && !this.getClothes().func_190926_b()) {
         tagClothes = new NBTTagCompound();
         this.getClothes().func_77955_b(tagClothes);
         compound.func_74782_a("clothes", tagClothes);
      }

      compound.func_74776_a("adjustement_gags", this.getGagsAdjustement());
      compound.func_74776_a("adjustement_blindfolds", this.getBlindfoldsAdjustement());
   }

   public void func_70037_a(NBTTagCompound compound) {
      super.func_70037_a(compound);
      if (compound.func_74764_b("damsel_name")) {
         this.field_70180_af.func_187227_b(DAMSEL_NAME, compound.func_74779_i("damsel_name"));
      }

      if (compound.func_74764_b("smallArms")) {
         this.field_70180_af.func_187227_b(SMALL_ARMS, compound.func_74767_n("smallArms"));
      }

      if (compound.func_74764_b("headLayer")) {
         this.field_70180_af.func_187227_b(HEAD_LAYER, compound.func_74767_n("headLayer"));
      }

      if (compound.func_74764_b("bodyLayer")) {
         this.field_70180_af.func_187227_b(BODY_LAYER, compound.func_74767_n("bodyLayer"));
      }

      if (compound.func_74764_b("leftArmLayer")) {
         this.field_70180_af.func_187227_b(LEFT_ARM_LAYER, compound.func_74767_n("leftArmLayer"));
      }

      if (compound.func_74764_b("rightArmLayer")) {
         this.field_70180_af.func_187227_b(RIGHT_ARM_LAYER, compound.func_74767_n("rightArmLayer"));
      }

      if (compound.func_74764_b("leftLegLayer")) {
         this.field_70180_af.func_187227_b(LEFT_LEG_LAYER, compound.func_74767_n("leftLegLayer"));
      }

      if (compound.func_74764_b("rightLegLayer")) {
         this.field_70180_af.func_187227_b(RIGHT_LEG_LAYER, compound.func_74767_n("rightLegLayer"));
      }

      if (compound.func_74764_b("binds")) {
         this.setBinds(new ItemStack((NBTTagCompound)compound.func_74781_a("binds")), false);
      }

      if (compound.func_74764_b("gag")) {
         this.setGag(new ItemStack((NBTTagCompound)compound.func_74781_a("gag")), false);
      }

      if (compound.func_74764_b("blindfold")) {
         this.setBlindfold(new ItemStack((NBTTagCompound)compound.func_74781_a("blindfold")), false);
      }

      if (compound.func_74764_b("collar")) {
         this.setCollar(new ItemStack((NBTTagCompound)compound.func_74781_a("collar")), false);
      }

      if (compound.func_74764_b("earplugs")) {
         this.setEarplugs(new ItemStack((NBTTagCompound)compound.func_74781_a("earplugs")), false);
      }

      if (compound.func_74764_b("clothes")) {
         this.setClothes(new ItemStack((NBTTagCompound)compound.func_74781_a("clothes")), false);
      }

      if (compound.func_74764_b("adjustement_gags")) {
         this.setGagsAdjustement(compound.func_74760_g("adjustement_gags"));
      }

      if (compound.func_74764_b("adjustement_blindfolds")) {
         this.setBlindfoldsAdjustement(compound.func_74760_g("adjustement_blindfolds"));
      }

      this.func_96094_a(TextFormatting.GREEN + (String)this.field_70180_af.func_187225_a(DAMSEL_NAME));
   }

   public void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111128_a(60.0D);
   }

   protected void func_184651_r() {
      this.field_70714_bg.func_75776_a(0, new EntityAIAvoidKidnapper(this, EntityPlayer.class, 10.0F, 1.1D, 1.3D));
      this.field_70714_bg.func_75776_a(1, new EntityAISwimingTiedUp(this));
      this.field_70714_bg.func_75776_a(3, new EntityAIWatchClosestBlindfolded(this, EntityPlayer.class, 6.0F));
      this.field_70714_bg.func_75776_a(4, new EntityAILookIdle(this));
      this.field_70714_bg.func_75776_a(5, new EntityAIPanicTiedUp(this, 1.0D));
      this.field_70714_bg.func_75776_a(6, new EntityAIWanderExceptWhenTiedUp(this, 1.2D, 10.0F));
      this.field_70714_bg.func_75776_a(7, new EntityAIOpenDoor(this, false));
   }

   public void free(boolean leadState) {
      this.func_110160_i(true, leadState);
   }

   public void free() {
      this.free(true);
   }

   public boolean isEnslavable() {
      return !this.func_110167_bD();
   }

   public boolean isSlave() {
      return this.func_110167_bD();
   }

   public boolean canBeTiedUp() {
      return !this.isTiedUp();
   }

   public boolean canApplyBondageItems() {
      return true;
   }

   public void transferSlaveryTo(I_Kidnapper newMaster) {
      if (newMaster != null && newMaster.getEntity() != null) {
         this.func_110162_b(newMaster.getEntity(), true);
      }

   }

   public boolean isGagged() {
      return this.getGag() != null && !this.getGag().func_190926_b();
   }

   public boolean isTiedUp() {
      return this.getBinds() != null && !this.getBinds().func_190926_b();
   }

   public boolean isBlindfolded() {
      return this.getBlindfold() != null && !this.getBlindfold().func_190926_b();
   }

   public boolean hasEarplugs() {
      return this.getEarplugs() != null && !this.getEarplugs().func_190926_b();
   }

   public boolean hasCollar() {
      return this.getCollar() != null && !this.getCollar().func_190926_b();
   }

   public boolean hasClothes() {
      return this.getClothes() != null && !this.getClothes().func_190926_b();
   }

   public boolean hasLockedCollar() {
      ItemStack collar = this.getCurrentCollar();
      if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
         ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
         return itemCollar.isLocked(collar);
      } else {
         return false;
      }
   }

   public boolean hasNamedCollar() {
      ItemStack collar = this.getCurrentCollar();
      if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
         ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
         return itemCollar.hasNickname(collar);
      } else {
         return false;
      }
   }

   public void putGagOn(ItemStack gag) {
      if (!this.isGagged() && gag != null && gag.func_77973_b() instanceof ItemGag) {
         ItemStack stackToPut = Utils.extractValidStack(gag);
         this.setGag(stackToPut);
      }
   }

   public ItemStack takeGagOff() {
      if (this.isGagged() && this.getGag() != null && !this.getGag().func_190926_b() && this.getGag().func_77973_b() instanceof ItemGag) {
         ItemStack stackReturned = this.getGag().func_77946_l();
         this.setGag(ItemStack.field_190927_a);
         return stackReturned;
      } else {
         return null;
      }
   }

   public void putBindOn(ItemStack bind) {
      if (!this.isTiedUp() && bind != null && bind.func_77973_b() instanceof ItemBind) {
         ItemStack stackToPut = Utils.extractValidStack(bind);
         this.setBinds(stackToPut);
      }
   }

   public ItemStack takeBindOff() {
      if (this.isTiedUp() && this.getBinds() != null && !this.getBinds().func_190926_b() && this.getBinds().func_77973_b() instanceof ItemBind) {
         ItemStack stackReturned = this.getBinds().func_77946_l();
         this.setBinds(ItemStack.field_190927_a);
         return stackReturned;
      } else {
         return null;
      }
   }

   public void putBlindfoldOn(ItemStack blindfold) {
      if (!this.isBlindfolded() && blindfold != null && blindfold.func_77973_b() instanceof ItemBlindfold) {
         ItemStack stackToPut = Utils.extractValidStack(blindfold);
         this.setBlindfold(stackToPut);
      }
   }

   public ItemStack takesBlindfoldOff() {
      if (this.isBlindfolded() && this.getBlindfold() != null && !this.getBlindfold().func_190926_b() && this.getBlindfold().func_77973_b() instanceof ItemBlindfold) {
         ItemStack stackReturned = this.getBlindfold().func_77946_l();
         this.setBlindfold(ItemStack.field_190927_a);
         return stackReturned;
      } else {
         return null;
      }
   }

   public void putEarsPlugsOn(ItemStack earplugs) {
      if (!this.hasEarplugs() && earplugs != null && earplugs.func_77973_b() instanceof ItemEarplugs) {
         ItemStack stackToPut = Utils.extractValidStack(earplugs);
         this.setEarplugs(stackToPut);
      }
   }

   public ItemStack takesEarplugsOff() {
      if (this.hasEarplugs() && this.getEarplugs() != null && !this.getEarplugs().func_190926_b() && this.getEarplugs().func_77973_b() instanceof ItemEarplugs) {
         ItemStack stackReturned = this.getEarplugs().func_77946_l();
         this.setEarplugs(ItemStack.field_190927_a);
         return stackReturned;
      } else {
         return null;
      }
   }

   public void putCollarOn(ItemStack collar) {
      if (!this.hasCollar() && collar != null && collar.func_77973_b() instanceof ItemCollar) {
         ItemStack stackToPut = Utils.extractValidStack(collar);
         this.setCollar(stackToPut);
      }
   }

   public ItemStack takesCollarOff() {
      return this.takesCollarOff(false);
   }

   public ItemStack takesCollarOff(boolean force) {
      if (this.hasCollar() && this.getCollar() != null && !this.getCollar().func_190926_b() && this.getCollar().func_77973_b() instanceof ItemCollar && (force || !this.hasLockedCollar())) {
         ItemStack stackReturned = this.getCollar().func_77946_l();
         this.setCollar(ItemStack.field_190927_a);
         return stackReturned;
      } else {
         return null;
      }
   }

   public void putClothesOn(ItemStack clothes) {
      if (!this.hasClothes() && clothes != null && clothes.func_77973_b() instanceof ItemClothes) {
         ItemStack stackToPut = Utils.extractValidStack(clothes);
         this.setClothes(stackToPut);
      }
   }

   public ItemStack takesClothesOff() {
      if (this.hasClothes() && this.getClothes() != null && !this.getClothes().func_190926_b() && this.getClothes().func_77973_b() instanceof ItemClothes) {
         ItemStack stackReturned = this.getClothes().func_77946_l();
         this.setClothes(ItemStack.field_190927_a);
         return stackReturned;
      } else {
         return null;
      }
   }

   public void untie() {
      this.takesBlindfoldOff();
      this.takeGagOff();
      this.takeBindOff();
      this.takesEarplugsOff();
      if (this.hasCollar() && !this.hasLockedCollar()) {
         this.takesCollarOff();
      }

      this.free();
   }

   public boolean isBoundAndGagged() {
      return this.isTiedUp() && this.isGagged();
   }

   public void tighten(EntityPlayer tightener) {
   }

   public ItemStack getCurrentBind() {
      if (this.getBinds() == null) {
         return null;
      } else {
         Item bindsItem = this.getBinds().func_77973_b();
         return bindsItem instanceof ItemBind ? this.getBinds().func_77946_l() : null;
      }
   }

   public ItemStack getCurrentGag() {
      if (this.getGag() == null) {
         return null;
      } else {
         Item gagItem = this.getGag().func_77973_b();
         return gagItem instanceof ItemGag ? this.getGag().func_77946_l() : null;
      }
   }

   public ItemStack getCurrentBlindfold() {
      if (this.getBlindfold() == null) {
         return null;
      } else {
         Item blindfoldItem = this.getBlindfold().func_77973_b();
         return blindfoldItem instanceof ItemBlindfold ? this.getBlindfold().func_77946_l() : null;
      }
   }

   public ItemStack getCurrentEarplugs() {
      if (this.getEarplugs() == null) {
         return null;
      } else {
         Item earplugsItem = this.getEarplugs().func_77973_b();
         return earplugsItem instanceof ItemEarplugs ? this.getEarplugs().func_77946_l() : null;
      }
   }

   public ItemStack getCurrentClothes() {
      if (this.getClothes() == null) {
         return null;
      } else {
         Item clothesItem = this.getClothes().func_77973_b();
         return clothesItem instanceof ItemClothes ? this.getClothes() : null;
      }
   }

   public ItemStack getCurrentCollar() {
      if (this.getCollar() == null) {
         return null;
      } else {
         Item collarItem = this.getCollar().func_77973_b();
         return collarItem instanceof ItemCollar ? this.getCollar() : null;
      }
   }

   public ItemStack replaceBind(ItemStack newbind) {
      if (!this.isTiedUp()) {
         return null;
      } else {
         ItemStack extractedBind = Utils.extractValidStack(newbind);
         if (extractedBind != null && extractedBind.func_77973_b() instanceof ItemBind) {
            ItemStack current = this.getCurrentBind();
            if (current != null && current.func_77973_b() instanceof ItemBind) {
               if (ItemStack.func_77989_b(current, extractedBind)) {
                  return null;
               } else {
                  ItemStack bindReplacement = extractedBind.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.setBinds(bindReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      }
   }

   public ItemStack replaceGag(ItemStack newGag, boolean force) {
      if (this.isBoundAndGagged() || this.isGagged() && force) {
         ItemStack extractedGag = Utils.extractValidStack(newGag);
         if (extractedGag != null && extractedGag.func_77973_b() instanceof ItemGag) {
            ItemStack current = this.getCurrentGag();
            if (current != null && current.func_77973_b() instanceof ItemGag) {
               if (ItemStack.func_77989_b(current, extractedGag)) {
                  return null;
               } else {
                  ItemStack gagReplacement = extractedGag.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.setGag(gagReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceGag(ItemStack newGag) {
      return this.replaceGag(newGag, false);
   }

   public ItemStack replaceBlindfold(ItemStack newBlindfold, boolean force) {
      if ((this.isTiedUp() || force) && this.isBlindfolded()) {
         ItemStack extractedBlindfold = Utils.extractValidStack(newBlindfold);
         if (extractedBlindfold != null && extractedBlindfold.func_77973_b() instanceof ItemBlindfold) {
            ItemStack current = this.getCurrentBlindfold();
            if (current != null && current.func_77973_b() instanceof ItemBlindfold) {
               if (ItemStack.func_77989_b(current, extractedBlindfold)) {
                  return null;
               } else {
                  ItemStack blindfoldReplacement = extractedBlindfold.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.setBlindfold(blindfoldReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceBlindfold(ItemStack newBlindfold) {
      return this.replaceBlindfold(newBlindfold, false);
   }

   public ItemStack replaceEarPlugs(ItemStack newEarplugs, boolean force) {
      if ((this.isTiedUp() || force) && this.hasEarplugs()) {
         ItemStack extractedEarplugs = Utils.extractValidStack(newEarplugs);
         if (extractedEarplugs != null && extractedEarplugs.func_77973_b() instanceof ItemEarplugs) {
            ItemStack current = this.getCurrentEarplugs();
            if (current != null && current.func_77973_b() instanceof ItemEarplugs) {
               if (ItemStack.func_77989_b(current, extractedEarplugs)) {
                  return null;
               } else {
                  ItemStack earplugsReplacement = extractedEarplugs.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.setEarplugs(earplugsReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceEarPlugs(ItemStack newEarplugs) {
      return this.replaceEarPlugs(newEarplugs, false);
   }

   public ItemStack replaceClothes(ItemStack newClothes, boolean force) {
      if ((this.isTiedUp() || force) && this.hasClothes()) {
         ItemStack extractedClothes = Utils.extractValidStack(newClothes);
         if (extractedClothes != null && extractedClothes.func_77973_b() instanceof ItemClothes) {
            ItemStack current = this.getCurrentClothes();
            if (current != null && current.func_77973_b() instanceof ItemClothes) {
               if (ItemStack.func_77989_b(current, extractedClothes)) {
                  return null;
               } else {
                  ItemStack clothesReplacement = extractedClothes.func_77946_l();
                  ItemStack stackReturned = current.func_77946_l();
                  this.setClothes(clothesReplacement);
                  return stackReturned;
               }
            } else {
               return null;
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceClothes(ItemStack newClothes) {
      return this.replaceClothes(newClothes, false);
   }

   public ItemStack replaceCollar(ItemStack newCollar, boolean force) {
      if ((this.isTiedUp() || force) && this.hasCollar()) {
         ItemStack extractedCollar = Utils.extractValidStack(newCollar);
         ItemStack current = this.getCurrentCollar();
         if (this.hasLockedCollar()) {
            return null;
         } else if (ItemStack.func_77989_b(current, extractedCollar)) {
            return null;
         } else {
            ItemStack oldStackCollar = current.func_77946_l();
            this.setCollar(extractedCollar.func_77946_l());
            return oldStackCollar;
         }
      } else {
         return null;
      }
   }

   public ItemStack replaceCollar(ItemStack newCollar) {
      return this.replaceCollar(newCollar, false);
   }

   public void dropBondageItems() {
      this.dropBondageItems(true, true, true, true, true);
   }

   public void dropBondageItems(boolean includeBind) {
      this.dropBondageItems(includeBind, true, true, true, true);
   }

   public void dropBondageItems(boolean includeBind, boolean includeGag, boolean includeBlindfold, boolean includeEarplugs, boolean includeCollar) {
      ItemStack gag = this.getCurrentGag();
      ItemStack bind = this.getCurrentBind();
      ItemStack blindfold = this.getCurrentBlindfold();
      ItemStack earplugs = this.getCurrentEarplugs();
      ItemStack collarStack = this.getCurrentCollar();
      if (this.hasLockedCollar()) {
         collarStack = null;
      }

      if (includeGag && gag != null) {
         this.kidnappedDropItem(gag);
      }

      if (includeBind && bind != null) {
         this.kidnappedDropItem(bind);
      }

      if (includeBlindfold && blindfold != null) {
         this.kidnappedDropItem(blindfold);
      }

      if (includeEarplugs && earplugs != null) {
         this.kidnappedDropItem(earplugs);
      }

      if (includeCollar && collarStack != null) {
         this.kidnappedDropItem(collarStack);
      }

   }

   public void dropClothes() {
      if (this.hasClothes()) {
         ItemStack clothes = this.takesClothesOff();
         if (clothes != null) {
            this.kidnappedDropItem(clothes);
         }
      }

   }

   public boolean hasGaggingEffect() {
      if (this.isGagged()) {
         return true;
      } else if (this.getBlindfold() != null && !this.getBlindfold().func_190926_b() && this.getBlindfold().func_77973_b() instanceof ItemGaggingEffect) {
         return true;
      } else if (this.getBinds() != null && !this.getBinds().func_190926_b() && this.getBinds().func_77973_b() instanceof ItemGaggingEffect) {
         return true;
      } else if (this.getEarplugs() != null && !this.getEarplugs().func_190926_b() && this.getEarplugs().func_77973_b() instanceof ItemGaggingEffect) {
         return true;
      } else {
         return this.getCollar() != null && !this.getCollar().func_190926_b() && this.getCollar().func_77973_b() instanceof ItemGaggingEffect;
      }
   }

   public boolean hasBlindingEffect() {
      if (this.isBlindfolded()) {
         return true;
      } else if (this.getGag() != null && !this.getGag().func_190926_b() && this.getGag().func_77973_b() instanceof IHasBlindingEffect) {
         return true;
      } else if (this.getBinds() != null && !this.getBinds().func_190926_b() && this.getBinds().func_77973_b() instanceof IHasBlindingEffect) {
         return true;
      } else if (this.getEarplugs() != null && !this.getEarplugs().func_190926_b() && this.getEarplugs().func_77973_b() instanceof IHasBlindingEffect) {
         return true;
      } else {
         return this.getCollar() != null && !this.getCollar().func_190926_b() && this.getCollar().func_77973_b() instanceof IHasBlindingEffect;
      }
   }

   public void takeBondageItemBy(PlayerBindState playerStateIn, int identifier) {
      if (playerStateIn != null && !playerStateIn.isTiedUp() && this.isTiedUp()) {
         ExtraBondageItemIdentifier item = ExtraBondageItemIdentifier.getIdentifier(identifier);
         ItemStack collarStack;
         if (item == ExtraBondageItemIdentifier.GAG) {
            if (this.isGagged()) {
               collarStack = this.takeGagOff();
               if (collarStack != null) {
                  this.kidnappedDropItem(collarStack);
               }
            }
         } else if (item == ExtraBondageItemIdentifier.BLINFOLD) {
            if (this.isBlindfolded()) {
               collarStack = this.takesBlindfoldOff();
               if (collarStack != null) {
                  this.kidnappedDropItem(collarStack);
               }
            }
         } else if (item == ExtraBondageItemIdentifier.EARPLUGS) {
            if (this.hasEarplugs()) {
               collarStack = this.takesEarplugsOff();
               if (collarStack != null) {
                  this.kidnappedDropItem(collarStack);
               }
            }
         } else {
            ItemStack clothes;
            if (item == ExtraBondageItemIdentifier.CLOTHES) {
               EntityPlayer player = playerStateIn.getPlayer();
               if (this.hasClothes() && player != null && this.canTakesOffClothes(player)) {
                  clothes = this.takesClothesOff();
                  if (clothes != null) {
                     this.kidnappedDropItem(clothes);
                  }
               }
            } else if (item == ExtraBondageItemIdentifier.COLLAR && this.hasCollar() && !this.hasLockedCollar()) {
               collarStack = this.getCurrentCollar();
               if (collarStack != null && collarStack.func_77973_b() instanceof ItemCollar) {
                  clothes = this.takesCollarOff();
                  this.kidnappedDropItem(clothes);
               }
            }
         }

      }
   }

   public int getBondageItemsWhichCanBeRemovedCount() {
      int count = 0;
      if (this.isGagged()) {
         ++count;
      }

      if (this.isBlindfolded()) {
         ++count;
      }

      if (this.hasEarplugs()) {
         ++count;
      }

      if (this.hasCollar() && !this.hasLockedCollar()) {
         ++count;
      }

      return count;
   }

   public void applyBondage(ItemStack bind, ItemStack gag, ItemStack blindfold, ItemStack earplugs, ItemStack collar, ItemStack clothes) {
      if (this.canApplyBondageItems()) {
         this.dropBondageItems(bind != null, gag != null, blindfold != null, earplugs != null, collar != null);
         if (bind != null) {
            if (this.isTiedUp()) {
               this.replaceBind(bind);
            } else {
               this.putBindOn(bind);
            }
         }

         if (gag != null) {
            if (this.isGagged()) {
               this.replaceGag(gag, true);
            } else {
               this.putGagOn(gag);
            }

            this.checkGagAfterApply();
         }

         if (blindfold != null) {
            if (this.isBlindfolded()) {
               this.replaceBlindfold(blindfold, true);
            } else {
               this.putBlindfoldOn(blindfold);
            }

            this.checkBlindfoldAfterApply();
         }

         if (earplugs != null) {
            if (this.hasEarplugs()) {
               this.replaceEarPlugs(earplugs, true);
            } else {
               this.putEarsPlugsOn(earplugs);
            }

            this.checkEarplugsAfterApply();
         }

         if (collar != null) {
            if (this.hasCollar()) {
               if (!this.hasLockedCollar()) {
                  this.replaceCollar(collar, true);
               }
            } else {
               this.putCollarOn(collar);
            }

            this.checkCollarAfterApply();
         }

         if (clothes != null && !this.hasClothes() && this.canChangeClothes()) {
            this.putClothesOn(clothes);
         }

      }
   }

   public void applyChloroform(int duration) {
      Potion e1 = Potion.func_188412_a(2);
      Potion e2 = Potion.func_188412_a(4);
      Potion e3 = Potion.func_188412_a(15);
      Potion e4 = Potion.func_188412_a(8);
      int tickDuration = duration * 20;
      this.func_70690_d(new PotionEffect(e1, tickDuration, 127));
      this.func_70690_d(new PotionEffect(e2, tickDuration, 127));
      this.func_70690_d(new PotionEffect(e3, tickDuration, 127));
      this.func_70690_d(new PotionEffect(e4, tickDuration, 150));
      this.chloroTimer = new Timer(duration);
   }

   public synchronized boolean isChloroformed() {
      if (this.chloroTimer != null) {
         return this.chloroTimer.getSecondsRemaining() > 0;
      } else {
         return false;
      }
   }

   public String getNameFromCollar() {
      if (this.hasNamedCollar()) {
         ItemStack collarStack = this.getCurrentCollar();
         if (collarStack.func_77973_b() instanceof ItemCollar) {
            ItemCollar collar = (ItemCollar)collarStack.func_77973_b();
            String name = collar.getNickname(collarStack);
            if (name != null) {
               return name;
            }
         }
      }

      return null;
   }

   public boolean func_184652_a(EntityPlayer player) {
      return this.isTiedUp() && (!this.isBondageServiceEnabled() || this.isMaster(player)) && super.func_184652_a(player);
   }

   public abstract ResourceLocation getSkin();

   public void kidnappedDropItem(ItemStack stack) {
      this.func_70099_a(stack, 0.0F);
   }

   public boolean isForSell() {
      return false;
   }

   public boolean canBeKidnappedByEvents() {
      return true;
   }

   public boolean canBeAttacked() {
      return this.isTiedUp() || !this.isBondageServiceEnabled();
   }

   public void shockKidnapped() {
      DamageSource source = DamageSource.field_76377_j;
      if (this.field_70170_p != null && !this.field_70170_p.field_72995_K) {
         this.field_70170_p.func_184133_a((EntityPlayer)null, this.func_180425_c(), KidnapModSoundEvents.ELECTRIC_SHOCK, SoundCategory.AMBIENT, 1.0F, 1.0F);
      }

      this.func_70097_a(source, 3.0F);
   }

   public String func_70005_c_() {
      return this.getDamselName();
   }

   public boolean hasKnives() {
      return false;
   }

   public boolean func_180431_b(DamageSource source) {
      return source == DamageSource.field_76368_d || source == DamageSource.field_76379_h || !this.canBeAttacked() && source != DamageSource.field_76380_i;
   }

   public boolean func_190530_aW() {
      return !this.canBeAttacked() ? true : super.func_190530_aW();
   }

   public boolean getEnslavedBy(I_Kidnapper playerKidnapper) {
      if (!this.isTiedUp()) {
         return false;
      } else if (this.func_110167_bD()) {
         return false;
      } else if (playerKidnapper != null && playerKidnapper.getEntity() != null) {
         this.func_110162_b(playerKidnapper.getEntity(), true);
         return true;
      } else {
         return false;
      }
   }

   public boolean isTiedToPole() {
      return this.func_110166_bE() != null && this.func_110166_bE() instanceof EntityLeashKnot;
   }

   public ITextComponent func_145748_c_() {
      ITextComponent component = new TextComponentString(this.getCurrentName());
      component.func_150256_b().func_150238_a(this.getNameColor());
      return component;
   }

   public TextFormatting getNameColor() {
      TextFormatting color = TextFormatting.GREEN;
      if (this.isBondageServiceEnabled()) {
         color = TextFormatting.LIGHT_PURPLE;
      }

      return color;
   }

   public boolean func_70601_bi() {
      GameRules rules = this.func_130014_f_().func_82736_K();
      boolean spawn = true;
      if (rules.func_82765_e("damsels-spawn")) {
         spawn = rules.func_82766_b("damsels-spawn");
      }

      return spawn && super.func_70601_bi();
   }

   protected boolean func_70692_ba() {
      GameRules rules = this.func_130014_f_().func_82736_K();
      if (rules.func_82765_e("damsels-spawn")) {
         return !rules.func_82766_b("damsels-spawn");
      } else {
         return false;
      }
   }

   public Position getPrison() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               Position prison = itemCollar.getPrison(collar);
               return prison;
            }
         }
      }

      return null;
   }

   public Position getHome() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               Position home = itemCollar.getHome(collar);
               return home;
            }
         }
      }

      return null;
   }

   public void warnMasters(EntityPlayer playerCaught) {
      if (this.hasCollar() && playerCaught != null) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null && itemCollar.shouldWarnMasters(collar)) {
               List<UUID> owners = itemCollar.getOwners(collar);
               if (owners != null & !owners.isEmpty()) {
                  Iterator var5 = owners.iterator();

                  while(var5.hasNext()) {
                     UUID id = (UUID)var5.next();
                     if (id != null) {
                        EntityPlayer owner = Utils.getPlayerFromUUID(id);
                        if (owner != null) {
                           this.talkTo(owner, "I've locked up " + playerCaught.func_70005_c_() + " in a cell.");
                        }
                     }
                  }
               }
            }
         }
      }

   }

   protected boolean canStayOnPositionOnDeath() {
      return true;
   }

   public boolean onDeathKidnapped() {
      if (this.field_70170_p != null && !this.field_70170_p.field_72995_K) {
         if (this.hasCollar() && this.canStayOnPositionOnDeath()) {
            this.func_70606_j(this.func_110138_aP());
            return true;
         } else {
            Position destination = this.getHome();
            if (destination == null) {
               destination = this.getPrison();
            }

            if (destination != null) {
               this.func_70606_j(this.func_110138_aP());
               this.putBindOn(new ItemStack(ModItems.ROPES));
               this.putGagOn(new ItemStack(ModItems.BALL_GAG));
               this.teleportToPosition(destination);
               return true;
            } else {
               this.dropBondageItems();
               this.dropClothes();
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public boolean isMaster(EntityPlayer player) {
      if (player == null) {
         return false;
      } else {
         if (this.hasCollar()) {
            ItemStack collar = this.getCurrentCollar();
            if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
               ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
               if (itemCollar != null) {
                  return itemCollar.isOwner(collar, player);
               }
            }
         }

         return false;
      }
   }

   public boolean hasOwners() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               return itemCollar.hasOwner(collar);
            }
         }
      }

      return false;
   }

   public String getCurrentName() {
      String damselName = this.getNameFromCollar();
      if (damselName == null) {
         damselName = this.getDamselName();
      }

      return damselName;
   }

   public void talkTo(EntityPlayer player, String message) {
      String finalMessage = message;
      if (this.hasGaggingEffect()) {
         GagTalkLoader loader = GagTalkLoader.getInstance();
         if (loader != null) {
            finalMessage = loader.gagTalkConvertor(player.field_70170_p, message);
         }
      }

      TextComponentString msgComponent = new TextComponentString(finalMessage);
      this.talkTo(player, (ITextComponent)msgComponent);
   }

   public void talkTo(EntityPlayer player, ITextComponent message) {
      if (player != null) {
         String name = "*" + this.getCurrentName() + "*";
         TextComponentString newMessage = new TextComponentString(TextFormatting.GOLD + name);
         TextComponentString msgTransition = new TextComponentString(TextFormatting.WHITE + " : ");
         newMessage.func_150257_a(msgTransition);
         newMessage.func_150257_a(message);
         player.func_145747_a(newMessage);
      }

   }

   public void talkToPlayersInRadius(String message, int radius) {
      List<EntityPlayerMP> players = PlayerBindState.getPlayerAround(this.field_70170_p, this.func_180425_c(), (double)radius);
      if (players != null) {
         Iterator var4 = players.iterator();

         while(var4.hasNext()) {
            EntityPlayerMP player = (EntityPlayerMP)var4.next();
            if (player != null) {
               this.talkTo(player, (String)message);
            }
         }
      }

   }

   public void actionTo(EntityPlayer player, String message) {
      TextComponentString msgComponent = new TextComponentString(message);
      this.actionTo(player, (ITextComponent)msgComponent);
   }

   public void actionTo(EntityPlayer player, ITextComponent message) {
      if (player != null) {
         String name = this.getCurrentName();
         TextComponentString newMessage = new TextComponentString(TextFormatting.GOLD + name + " ");
         newMessage.func_150257_a(message);
         player.func_145747_a(newMessage);
      }

   }

   public boolean shouldTiedSlaveToPoleInPrison() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               return itemCollar.isPrisonFenceEnabled(collar);
            }
         }
      }

      return false;
   }

   public boolean isBondageServiceEnabled() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               Position prison = itemCollar.getPrison(collar);
               return prison != null && itemCollar.isBondageServiceEnabled(collar);
            }
         }
      }

      return false;
   }

   public String getBondageServiceMessage() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               String message = itemCollar.getServiceSentence(collar);
               if (message != null) {
                  return message;
               }
            }
         }
      }

      return "Hello! Would you like to be tied up in my owner's prison ?";
   }

   public synchronized void doBondageServiceOn(EntityPlayer player) {
      if (player != null && this.serviceTargets != null && !this.isTiedUp() && this.isBondageServiceEnabled()) {
         UUID playerID = player.func_110124_au();
         if (playerID != null) {
            Position prison = this.getPrison();
            if (prison != null && this.serviceTargets.containsKey(playerID)) {
               Timer timer = (Timer)this.serviceTargets.get(playerID);
               if (timer != null && timer.getSecondsRemaining() > 0) {
                  PlayerBindState state = PlayerBindState.getInstance(player);
                  if (state != null) {
                     this.warnMasters(player);
                     state.restrain(new ItemStack(ModItems.ROPES, 1), new ItemStack(ModItems.BALL_GAG, 1));
                     state.teleportToPosition(prison, false);
                     state = PlayerBindState.getInstance(player);
                     if (state != null && this.shouldTiedSlaveToPoleInPrison()) {
                        state.tieToClosestPole();
                     }
                  }

                  return;
               }
            }

            String message = this.getBondageServiceMessage();
            message = message + " Hit me again!";
            this.talkTo(player, message);
            Timer timer = new Timer(10);
            this.serviceTargets.put(playerID, timer);
         }
      }

   }

   public void notifyPlayerAttack(EntityPlayer player) {
      if (this.isTiedUp()) {
         int randTalk = this.field_70146_Z.nextInt(100);
         if (randTalk <= 25) {
            String sentence = EntityDamsel.CaptivesSentences.getRandomSentence();
            if (sentence != null) {
               this.talkToPlayersInRadius(sentence, 5);
            }
         }
      } else if (!this.isTiedUp() && this.isBondageServiceEnabled()) {
         this.doBondageServiceOn(player);
      }

   }

   public void checkGagAfterApply() {
      if (!this.isTiedUp() && this.isGagged()) {
         ItemStack gag = this.getCurrentGag();
         if (gag != null && !EnchantmentHelper.func_190938_b(gag)) {
            ItemStack gagDrop = this.takeGagOff();
            if (gagDrop != null) {
               this.kidnappedDropItem(gagDrop);
            }
         }
      }

   }

   public void checkBlindfoldAfterApply() {
      if (!this.isTiedUp() && this.isBlindfolded()) {
         ItemStack blindfold = this.getCurrentBlindfold();
         if (blindfold != null && !EnchantmentHelper.func_190938_b(blindfold)) {
            ItemStack blindfoldDrop = this.takesBlindfoldOff();
            if (blindfoldDrop != null) {
               this.kidnappedDropItem(blindfoldDrop);
            }
         }
      }

   }

   public void checkEarplugsAfterApply() {
      if (!this.isTiedUp() && this.hasEarplugs()) {
         ItemStack earplugs = this.getCurrentEarplugs();
         if (earplugs != null && !EnchantmentHelper.func_190938_b(earplugs)) {
            ItemStack earplugsDrop = this.takesEarplugsOff();
            if (earplugsDrop != null) {
               this.kidnappedDropItem(earplugsDrop);
            }
         }
      }

   }

   public void checkCollarAfterApply() {
      if (!this.isTiedUp() && this.hasCollar() && !this.hasLockedCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && !EnchantmentHelper.func_190938_b(collar)) {
            ItemStack collarDrop = this.takesCollarOff();
            if (collarDrop != null) {
               this.kidnappedDropItem(collarDrop);
            }
         }
      }

   }

   public void teleportToPosition(Position pos) {
      boolean dropLeash = this.func_110167_bD();
      this.free(false);
      Entity newEntity = TeleportHelper.doTeleportEntity(this, pos);
      if (dropLeash && newEntity != null && newEntity instanceof EntityDamsel) {
         EntityDamsel damsel = (EntityDamsel)newEntity;
         damsel.func_70099_a(new ItemStack(Items.field_151058_ca, 1), 1.0F);
      }

   }

   public UUID getKidnappedUniqueId() {
      return this.func_110124_au();
   }

   public String getKidnappedName() {
      return this.func_70005_c_();
   }

   public boolean hasClothesWithSmallArms() {
      ItemStack clothes = this.getCurrentClothes();
      if (clothes != null && clothes.func_77973_b() instanceof ItemClothes) {
         ItemClothes itemClothes = (ItemClothes)clothes.func_77973_b();
         return itemClothes.shouldForceSmallArms(clothes);
      } else {
         return false;
      }
   }

   public UUID getCurrentEditor() {
      return this.currentEditor;
   }

   public void setCurrentEditor(UUID currentEditor) {
      this.currentEditor = currentEditor;
   }

   public Position getCurrentEditionPosition() {
      return this.currentEditionPosition;
   }

   public void setCurrentEditionPosition(Position currentEditionPosition) {
      this.currentEditionPosition = currentEditionPosition;
   }

   public boolean canChangeClothes() {
      return !this.hasOwners();
   }

   public boolean canTakesOffClothes(EntityPlayer player) {
      return this.hasClothes() && this.canChangeClothes(player);
   }

   public boolean canChangeClothes(EntityPlayer player) {
      return this.canChangeClothes() || this.isMaster(player);
   }

   public boolean canPannick() {
      return !this.isTiedUp() && !this.hasCollar();
   }

   public List<UUID> getOwners() {
      if (this.hasCollar()) {
         ItemStack collar = this.getCurrentCollar();
         if (collar != null && collar.func_77973_b() instanceof ItemCollar) {
            ItemCollar itemCollar = (ItemCollar)collar.func_77973_b();
            if (itemCollar != null) {
               return itemCollar.getOwners(collar);
            }
         }
      }

      return Collections.emptyList();
   }

   public void onUnload() {
      World world = this.func_130014_f_();
      UUID idDamsel = this.func_110124_au();
      Position pos = Utils.getEntityPosition(this);
      if (world != null && idDamsel != null && pos != null) {
         List<UUID> owners = this.getOwners();
         if (owners != null) {
            SlavesUnloadLocationsData data = SlavesUnloadLocationsData.get(world);
            if (data != null) {
               Iterator var6 = owners.iterator();

               while(var6.hasNext()) {
                  UUID owner = (UUID)var6.next();
                  if (owner != null) {
                     data.addPosition(owner, idDamsel, pos);
                  }
               }
            }
         }
      }

   }

   public void onLoad() {
      World world = this.func_130014_f_();
      UUID idDamsel = this.func_110124_au();
      if (world != null && idDamsel != null) {
         List<UUID> owners = this.getOwners();
         if (owners != null) {
            SlavesUnloadLocationsData data = SlavesUnloadLocationsData.get(world);
            if (data != null) {
               Iterator var5 = owners.iterator();

               while(var5.hasNext()) {
                  UUID owner = (UUID)var5.next();
                  if (owner != null) {
                     data.removePosition(owner, idDamsel);
                  }
               }
            }
         }
      }

   }

   static {
      DAMSEL_NAME = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187194_d);
      SMALL_ARMS = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187198_h);
      BINDS = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187196_f);
      GAG = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187196_f);
      BLINDFOLD = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187196_f);
      COLLAR = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187196_f);
      EARPLUGS = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187196_f);
      CLOTHES = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187196_f);
      ADJUST_GAGS = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187193_c);
      ADJUST_BLINDFOLDS = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187193_c);
      HEAD_LAYER = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187198_h);
      BODY_LAYER = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187198_h);
      LEFT_ARM_LAYER = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187198_h);
      RIGHT_ARM_LAYER = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187198_h);
      LEFT_LEG_LAYER = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187198_h);
      RIGHT_LEG_LAYER = EntityDataManager.func_187226_a(EntityDamsel.class, DataSerializers.field_187198_h);
   }

   public static enum CaptivesSentences {
      HELP("Help!!!"),
      LET("Let me go!"),
      NO("Nooo!"),
      STOP("Stop!"),
      UNTIE("Please, untie me!");

      private String sentence;

      private CaptivesSentences(String sentence) {
         this.sentence = sentence;
      }

      public static String getRandomSentence() {
         int count = values().length;
         Random rand = new Random();
         EntityDamsel.CaptivesSentences captiveSentence = values()[rand.nextInt(count)];
         return captiveSentence != null ? captiveSentence.sentence : null;
      }
   }

   public static enum DamselTelportPoints {
      HOME,
      PRISON,
      WARP;
   }

   public static enum LayersEnum {
      HEAD,
      BODY,
      LEFTARM,
      RIGHTARM,
      LEFTLEG,
      RIGHTLEG;
   }
}
