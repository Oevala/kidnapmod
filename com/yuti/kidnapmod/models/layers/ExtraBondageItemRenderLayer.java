package com.yuti.kidnapmod.models.layers;

import com.google.common.collect.Maps;
import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.extrainventory.ExtraBondageItemType;
import com.yuti.kidnapmod.extrainventory.ExtraBondageMaterial;
import com.yuti.kidnapmod.extrainventory.IExtraBondageItem;
import com.yuti.kidnapmod.util.images.DynamicOnlineTexture;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class ExtraBondageItemRenderLayer<E extends EntityLivingBase> implements LayerRenderer<E> {
   protected ModelBiped modelArmor;
   private final RenderLivingBase<?> renderer;
   private float alpha = 1.0F;
   private float colorR = 1.0F;
   private float colorG = 1.0F;
   private float colorB = 1.0F;
   protected static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   private static final Map<String, ResourceLocation> ITEMS_TEXTURE_RES_MAP = Maps.newHashMap();

   public ExtraBondageItemRenderLayer(RenderLivingBase<?> rendererIn) {
      this.renderer = rendererIn;
      this.initArmor();
   }

   protected void renderItem(E entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, ItemStack stack, boolean forceNormalRender) {
      if (stack != null && stack.func_77973_b() instanceof IExtraBondageItem) {
         IExtraBondageItem itembondage = (IExtraBondageItem)stack.func_77973_b();
         if (itembondage.canRender()) {
            ExtraBondageItemType slotIn = itembondage.getType(stack);
            ModelBiped t = this.modelArmor;
            t = this.getBondageModel(entity, stack, slotIn, t);
            if (t != null) {
               t.func_178686_a(this.renderer.func_177087_b());
               t.func_78086_a(entity, limbSwing, limbSwingAmount, partialTicks);
               this.setModelSlotVisible(t, slotIn);
               boolean flagNormalRender = true;
               if (!forceNormalRender && ModConfig.displayDynamicTextures) {
                  DynamicOnlineTexture dynamicTexture = itembondage.getDynamicTexture(stack);
                  if (dynamicTexture != null && dynamicTexture.isValid()) {
                     dynamicTexture.bindBase();
                     flagNormalRender = false;
                  }
               }

               if (flagNormalRender) {
                  this.renderer.func_110776_a(this.getItemRessource(entity, itembondage, stack, slotIn, (Object)null));
               }

               GlStateManager.func_179131_c(this.colorR, this.colorG, this.colorB, this.alpha);
               t.func_78088_a(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
               if (stack.func_77962_s()) {
                  renderEnchantedGlint(this.renderer, entity, t, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
               }
            }
         }
      }

   }

   private ResourceLocation getItemRessource(EntityLivingBase player, IExtraBondageItem item, ItemStack stack, ExtraBondageItemType slotIn, Object object) {
      ExtraBondageMaterial material = item.getExtraBondageMaterial(stack);
      String domain = material.getDomain();
      String location = material.getLocation();
      String s1 = String.format("%s:textures/models/kidnapstuff/%s.png", domain, location);
      ResourceLocation resourcelocation = (ResourceLocation)ITEMS_TEXTURE_RES_MAP.get(s1);
      if (resourcelocation == null) {
         resourcelocation = new ResourceLocation(s1);
         ITEMS_TEXTURE_RES_MAP.put(s1, resourcelocation);
      }

      return resourcelocation;
   }

   public boolean func_177142_b() {
      return false;
   }

   protected void initArmor() {
      this.modelArmor = new ModelBiped(1.0F);
   }

   protected void setModelSlotVisible(ModelBiped p_188359_1_, ExtraBondageItemType slotIn) {
      this.setModelVisible(p_188359_1_);
      switch(slotIn) {
      case GAG:
         p_188359_1_.field_78116_c.field_78806_j = true;
         p_188359_1_.field_178720_f.field_78806_j = true;
         break;
      case BIND:
         p_188359_1_.field_78115_e.field_78806_j = true;
         p_188359_1_.field_178723_h.field_78806_j = true;
         p_188359_1_.field_178724_i.field_78806_j = true;
         p_188359_1_.field_178721_j.field_78806_j = true;
         p_188359_1_.field_178722_k.field_78806_j = true;
         break;
      case BLINDFOLD:
         p_188359_1_.field_78116_c.field_78806_j = true;
         p_188359_1_.field_178720_f.field_78806_j = true;
         break;
      case EARPLUGS:
         p_188359_1_.field_78116_c.field_78806_j = true;
         p_188359_1_.field_178720_f.field_78806_j = true;
         break;
      case COLLAR:
         p_188359_1_.field_78116_c.field_78806_j = true;
         p_188359_1_.field_178720_f.field_78806_j = true;
         p_188359_1_.field_78115_e.field_78806_j = true;
         break;
      case CLOTHES:
         p_188359_1_.field_178720_f.field_78806_j = true;
         p_188359_1_.field_78116_c.field_78806_j = true;
         p_188359_1_.field_78115_e.field_78806_j = true;
         p_188359_1_.field_178723_h.field_78806_j = true;
         p_188359_1_.field_178724_i.field_78806_j = true;
         p_188359_1_.field_178721_j.field_78806_j = true;
         p_188359_1_.field_178722_k.field_78806_j = true;
      }

   }

   protected void setModelVisible(ModelBiped model) {
      model.func_178719_a(false);
   }

   protected ModelBiped getBondageModel(EntityLivingBase entity, ItemStack itemStack, ExtraBondageItemType slot, ModelBiped model) {
      if (itemStack != null && !itemStack.func_190926_b()) {
         Item item = itemStack.func_77973_b();
         if (item != null && item instanceof IExtraBondageItem) {
            IExtraBondageItem bondageItem = (IExtraBondageItem)item;
            return bondageItem.getBondageModel(entity, itemStack, slot, model);
         }
      }

      return null;
   }

   public static void renderEnchantedGlint(RenderLivingBase<?> p_188364_0_, EntityLivingBase p_188364_1_, ModelBase model, float p_188364_3_, float p_188364_4_, float p_188364_5_, float p_188364_6_, float p_188364_7_, float p_188364_8_, float p_188364_9_) {
      float f = (float)p_188364_1_.field_70173_aa + p_188364_5_;
      p_188364_0_.func_110776_a(ENCHANTED_ITEM_GLINT_RES);
      Minecraft.func_71410_x().field_71460_t.func_191514_d(true);
      GlStateManager.func_179147_l();
      GlStateManager.func_179143_c(514);
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179131_c(0.5F, 0.5F, 0.5F, 1.0F);

      for(int i = 0; i < 2; ++i) {
         GlStateManager.func_179140_f();
         GlStateManager.func_187401_a(SourceFactor.SRC_COLOR, DestFactor.ONE);
         GlStateManager.func_179131_c(0.38F, 0.19F, 0.608F, 1.0F);
         GlStateManager.func_179128_n(5890);
         GlStateManager.func_179096_D();
         GlStateManager.func_179152_a(0.33333334F, 0.33333334F, 0.33333334F);
         GlStateManager.func_179114_b(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.func_179109_b(0.0F, f * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
         GlStateManager.func_179128_n(5888);
         model.func_78088_a(p_188364_1_, p_188364_3_, p_188364_4_, p_188364_6_, p_188364_7_, p_188364_8_, p_188364_9_);
         GlStateManager.func_187401_a(SourceFactor.ONE, DestFactor.ZERO);
      }

      GlStateManager.func_179128_n(5890);
      GlStateManager.func_179096_D();
      GlStateManager.func_179128_n(5888);
      GlStateManager.func_179145_e();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179143_c(515);
      GlStateManager.func_179084_k();
      Minecraft.func_71410_x().field_71460_t.func_191514_d(false);
   }
}
