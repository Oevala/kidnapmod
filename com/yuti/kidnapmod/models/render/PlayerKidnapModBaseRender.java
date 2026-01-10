package com.yuti.kidnapmod.models.render;

import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.states.PlayerBindState;
import com.yuti.kidnapmod.states.kidnapped.managers.I_Kidnapped;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.Profile;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Post;
import net.minecraftforge.client.event.RenderLivingEvent.Specials.Pre;
import net.minecraftforge.common.MinecraftForge;

public class PlayerKidnapModBaseRender extends KidnapModBaseRender<AbstractClientPlayer> {
   private final boolean smallArms;

   public PlayerKidnapModBaseRender(RenderManager renderManager, ModelPlayer model) {
      this(renderManager, false, model);
   }

   public PlayerKidnapModBaseRender(RenderManager renderManager, boolean useSmallArms, ModelPlayer model) {
      super(renderManager, model, 0.5F);
      this.smallArms = useSmallArms;
   }

   public ModelPlayer getMainModel() {
      return super.getMainModel();
   }

   public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (!entity.func_175144_cb() || this.field_76990_c.field_78734_h == entity) {
         double d0 = y;
         if (entity.func_70093_af()) {
            d0 = y - 0.125D;
         }

         this.setModelVisibilities(entity);
         GlStateManager.func_187408_a(Profile.PLAYER_SKIN);
         super.func_76986_a(entity, x, d0, z, entityYaw, partialTicks);
         GlStateManager.func_187440_b(Profile.PLAYER_SKIN);
      }

   }

   private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
      ModelPlayer modelplayer = this.getMainModel();
      if (clientPlayer.func_175149_v()) {
         modelplayer.func_178719_a(false);
         modelplayer.field_78116_c.field_78806_j = true;
         modelplayer.field_178720_f.field_78806_j = true;
      } else {
         ItemStack itemstack = clientPlayer.func_184614_ca();
         ItemStack itemstack1 = clientPlayer.func_184592_cb();
         modelplayer.func_178719_a(true);
         modelplayer.field_178720_f.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.HAT);
         modelplayer.field_178730_v.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.JACKET);
         modelplayer.field_178733_c.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.LEFT_PANTS_LEG);
         modelplayer.field_178731_d.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.RIGHT_PANTS_LEG);
         modelplayer.field_178734_a.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.LEFT_SLEEVE);
         modelplayer.field_178732_b.field_78806_j = clientPlayer.func_175148_a(EnumPlayerModelParts.RIGHT_SLEEVE);
         modelplayer.field_78117_n = clientPlayer.func_70093_af();
         ArmPose modelbiped$armpose = ArmPose.EMPTY;
         ArmPose modelbiped$armpose1 = ArmPose.EMPTY;
         EnumAction enumaction1;
         if (!itemstack.func_190926_b()) {
            modelbiped$armpose = ArmPose.ITEM;
            if (clientPlayer.func_184605_cv() > 0) {
               enumaction1 = itemstack.func_77975_n();
               if (enumaction1 == EnumAction.BLOCK) {
                  modelbiped$armpose = ArmPose.BLOCK;
               } else if (enumaction1 == EnumAction.BOW) {
                  modelbiped$armpose = ArmPose.BOW_AND_ARROW;
               }
            }
         }

         if (!itemstack1.func_190926_b()) {
            modelbiped$armpose1 = ArmPose.ITEM;
            if (clientPlayer.func_184605_cv() > 0) {
               enumaction1 = itemstack1.func_77975_n();
               if (enumaction1 == EnumAction.BLOCK) {
                  modelbiped$armpose1 = ArmPose.BLOCK;
               }
            }
         }

         if (clientPlayer.func_184591_cq() == EnumHandSide.RIGHT) {
            modelplayer.field_187076_m = modelbiped$armpose;
            modelplayer.field_187075_l = modelbiped$armpose1;
         } else {
            modelplayer.field_187076_m = modelbiped$armpose1;
            modelplayer.field_187075_l = modelbiped$armpose;
         }
      }

   }

   public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
      return entity.func_110306_p();
   }

   public void func_82422_c() {
      GlStateManager.func_179109_b(0.0F, 0.1875F, 0.0F);
   }

   protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime) {
      float f = 0.9375F;
      GlStateManager.func_179152_a(0.9375F, 0.9375F, 0.9375F);
   }

   protected void renderEntityName(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq) {
      if (distanceSq < 100.0D) {
         Scoreboard scoreboard = entityIn.func_96123_co();
         ScoreObjective scoreobjective = scoreboard.func_96539_a(2);
         if (scoreobjective != null) {
            Score score = scoreboard.func_96529_a(entityIn.func_70005_c_(), scoreobjective);
            this.func_147906_a(entityIn, score.func_96652_c() + " " + scoreobjective.func_96678_d(), x, y, z, 64);
            y += (double)((float)this.func_76983_a().field_78288_b * 1.15F * 0.025F);
         }
      }

      super.func_188296_a(entityIn, x, y, z, name, distanceSq);
   }

   public void renderRightArm(AbstractClientPlayer clientPlayer) {
      float f = 1.0F;
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      float f1 = 0.0625F;
      ModelPlayer modelplayer = this.getMainModel();
      this.setModelVisibilities(clientPlayer);
      GlStateManager.func_179147_l();
      modelplayer.field_78095_p = 0.0F;
      modelplayer.field_78117_n = false;
      modelplayer.func_78087_a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
      modelplayer.field_178723_h.field_78795_f = 90.0F;
      modelplayer.field_178723_h.func_78785_a(0.0625F);
      modelplayer.field_178732_b.field_78795_f = 0.0F;
      modelplayer.field_178732_b.func_78785_a(0.0625F);
      GlStateManager.func_179084_k();
   }

   public void renderLeftArm(AbstractClientPlayer clientPlayer) {
      float f = 1.0F;
      GlStateManager.func_179124_c(1.0F, 1.0F, 1.0F);
      float f1 = 0.0625F;
      ModelPlayer modelplayer = this.getMainModel();
      this.setModelVisibilities(clientPlayer);
      GlStateManager.func_179147_l();
      modelplayer.field_78117_n = false;
      modelplayer.field_78095_p = 0.0F;
      modelplayer.func_78087_a(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
      modelplayer.field_178724_i.field_78795_f = 0.0F;
      modelplayer.field_178724_i.func_78785_a(0.0625F);
      modelplayer.field_178734_a.field_78795_f = 0.0F;
      modelplayer.field_178734_a.func_78785_a(0.0625F);
      GlStateManager.func_179084_k();
   }

   protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z) {
      if (entityLivingBaseIn.func_70089_S() && entityLivingBaseIn.func_70608_bn()) {
         super.func_77039_a(entityLivingBaseIn, x + (double)entityLivingBaseIn.field_71079_bU, y + (double)entityLivingBaseIn.field_71082_cx, z + (double)entityLivingBaseIn.field_71089_bV);
      } else {
         super.func_77039_a(entityLivingBaseIn, x, y, z);
      }

   }

   protected void applyRotations(AbstractClientPlayer entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
      if (entityLiving.func_70089_S() && entityLiving.func_70608_bn()) {
         GlStateManager.func_179114_b(entityLiving.func_71051_bG(), 0.0F, 1.0F, 0.0F);
         GlStateManager.func_179114_b(this.func_77037_a(entityLiving), 0.0F, 0.0F, 1.0F);
         GlStateManager.func_179114_b(270.0F, 0.0F, 1.0F, 0.0F);
      } else if (entityLiving.func_184613_cA()) {
         super.func_77043_a(entityLiving, p_77043_2_, rotationYaw, partialTicks);
         float f = (float)entityLiving.func_184599_cB() + partialTicks;
         float f1 = MathHelper.func_76131_a(f * f / 100.0F, 0.0F, 1.0F);
         GlStateManager.func_179114_b(f1 * (-90.0F - entityLiving.field_70125_A), 1.0F, 0.0F, 0.0F);
         Vec3d vec3d = entityLiving.func_70676_i(partialTicks);
         double d0 = entityLiving.field_70159_w * entityLiving.field_70159_w + entityLiving.field_70179_y * entityLiving.field_70179_y;
         double d1 = vec3d.field_72450_a * vec3d.field_72450_a + vec3d.field_72449_c * vec3d.field_72449_c;
         if (d0 > 0.0D && d1 > 0.0D) {
            double d2 = (entityLiving.field_70159_w * vec3d.field_72450_a + entityLiving.field_70179_y * vec3d.field_72449_c) / (Math.sqrt(d0) * Math.sqrt(d1));
            double d3 = entityLiving.field_70159_w * vec3d.field_72449_c - entityLiving.field_70179_y * vec3d.field_72450_a;
            GlStateManager.func_179114_b((float)(Math.signum(d3) * Math.acos(d2)) * 180.0F / 3.1415927F, 0.0F, 1.0F, 0.0F);
         }
      } else {
         super.func_77043_a(entityLiving, p_77043_2_, rotationYaw, partialTicks);
      }

   }

   private boolean hasNamedCollar(AbstractClientPlayer entity) {
      if (entity == null) {
         return false;
      } else {
         PlayerBindState state = PlayerBindState.getInstance(entity);
         return state != null && state.hasNamedCollar();
      }
   }

   private boolean isMcPlayerBlindfolded() {
      if (this.field_76990_c.field_78734_h != null && this.field_76990_c.field_78734_h instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)this.field_76990_c.field_78734_h;
         PlayerBindState state = PlayerBindState.getInstance(player);
         return state != null && state.hasBlindingEffect();
      } else {
         return false;
      }
   }

   private boolean slaveRenderCollarDisabled() {
      if (this.field_76990_c.field_78734_h != null && this.field_76990_c.field_78734_h instanceof EntityPlayer) {
         EntityPlayer player = (EntityPlayer)this.field_76990_c.field_78734_h;
         EntityPlayer playerClient = Minecraft.func_71410_x().field_71439_g;
         if (player.equals(playerClient)) {
            return !ModConfig.displaySelfSlaveName;
         }
      }

      return false;
   }

   protected boolean canRenderName(AbstractClientPlayer entity) {
      if (!this.isMcPlayerBlindfolded() && !this.slaveRenderCollarDisabled()) {
         if (this.hasNamedCollar(entity)) {
            EntityPlayerSP entityplayersp = Minecraft.func_71410_x().field_71439_g;
            boolean flag = !entity.func_98034_c(entityplayersp);
            return Minecraft.func_71382_s() && flag && !entity.func_184207_aI();
         } else {
            return super.func_177070_b(entity);
         }
      } else {
         return false;
      }
   }

   public void renderName(AbstractClientPlayer entity, double x, double y, double z) {
      if (!this.hasNamedCollar(entity) && !this.isMcPlayerBlindfolded()) {
         super.func_177067_a(entity, x, y, z);
      } else if (!MinecraftForge.EVENT_BUS.post(new Pre(entity, this, x, y, z))) {
         if (this.canRenderName(entity)) {
            double d0 = entity.func_70068_e(this.field_76990_c.field_78734_h);
            float f = entity.func_70093_af() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
            if (d0 < (double)(f * f)) {
               PlayerBindState state = PlayerBindState.getInstance(entity);
               if (state != null && state.hasCollar()) {
                  ItemStack collarStack = state.getCurrentCollar();
                  if (collarStack.func_77973_b() instanceof ItemCollar) {
                     ItemCollar collar = (ItemCollar)collarStack.func_77973_b();
                     String name = collar.getNickname(collarStack);
                     if (name != null) {
                        String coloredName = TextFormatting.GOLD + "*" + name + "*";
                        GlStateManager.func_179092_a(516, 0.1F);
                        this.renderEntityName(entity, x, y, z, coloredName, d0);
                     }
                  }
               }
            }
         }

         MinecraftForge.EVENT_BUS.post(new Post(entity, this, x, y, z));
      }
   }

   public I_Kidnapped getKidnapped(AbstractClientPlayer entity) {
      if (entity != null) {
         PlayerBindState state = PlayerBindState.getInstance(entity);
         return state;
      } else {
         return null;
      }
   }
}
