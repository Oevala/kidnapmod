package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.states.PlayerBindState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiBlindfold extends Gui {
   public static final ResourceLocation ressource = new ResourceLocation("knapm", "textures/gui/blindfolded.png");
   protected static final ResourceLocation WIDGETS_TEX_PATH = new ResourceLocation("textures/gui/widgets.png");

   @SubscribeEvent
   public void renderOverlay(Post event) {
      if (event.getType() == ElementType.HOTBAR) {
         Minecraft mc = Minecraft.func_71410_x();
         PlayerBindState state = PlayerBindState.getInstance(mc.field_71439_g);
         if (state != null && state.hasBlindingEffect()) {
            mc.field_71446_o.func_110577_a(ressource);
            ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
            this.func_73729_b(0, 0, 0, 0, sr.func_78326_a(), sr.func_78328_b());
            this.redrawHotBar(mc, sr);
         }
      }

   }

   public void redrawHotBar(Minecraft mc, ScaledResolution sr) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      mc.func_110434_K().func_110577_a(WIDGETS_TEX_PATH);
      EntityPlayer entityplayer = (EntityPlayer)mc.func_175606_aa();
      ItemStack itemstack = entityplayer.func_184592_cb();
      EnumHandSide enumhandside = entityplayer.func_184591_cq().func_188468_a();
      int i = sr.func_78326_a() / 2;
      this.field_73735_i = -90.0F;
      this.func_73729_b(i - 91, sr.func_78328_b() - 22, 0, 0, 182, 22);
      this.func_73729_b(i - 91 - 1 + entityplayer.field_71071_by.field_70461_c * 20, sr.func_78328_b() - 22 - 1, 0, 22, 24, 22);
      if (!itemstack.func_190926_b()) {
         if (enumhandside == EnumHandSide.LEFT) {
            this.func_73729_b(i - 91 - 29, sr.func_78328_b() - 23, 24, 22, 29, 24);
         } else {
            this.func_73729_b(i + 91, sr.func_78328_b() - 23, 53, 22, 29, 24);
         }
      }

   }
}
