package com.yuti.kidnapmod.gui.extrainventory;

import com.yuti.kidnapmod.init.KeyBindings;
import com.yuti.kidnapmod.inventory.container.ContainerExtraBondage;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class GuiBondageExtended extends InventoryEffectRenderer {
   public static final ResourceLocation background = new ResourceLocation("knapm", "textures/gui/extra_inventory.png");
   private float oldMouseX;
   private float oldMouseY;

   public GuiBondageExtended(EntityPlayer player) {
      super(new ContainerExtraBondage(player.field_71071_by, !player.func_130014_f_().field_72995_K, player));
      this.field_146291_p = true;
   }

   private void resetGuiLeft() {
      this.field_147003_i = (this.field_146294_l - this.field_146999_f) / 2;
   }

   public void func_73876_c() {
      ((ContainerExtraBondage)this.field_147002_h).extraBondageItems.setEventBlock(false);
      this.func_175378_g();
      this.resetGuiLeft();
   }

   public void func_73866_w_() {
      this.field_146292_n.clear();
      super.func_73866_w_();
      this.resetGuiLeft();
   }

   protected void func_146979_b(int p_146979_1_, int p_146979_2_) {
      this.field_146289_q.func_78276_b(I18n.func_135052_a("container.crafting", new Object[0]), 115, 8, 4210752);
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.oldMouseX = (float)mouseX;
      this.oldMouseY = (float)mouseY;
      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.func_191948_b(mouseX, mouseY);
   }

   protected void func_146976_a(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.func_110434_K().func_110577_a(background);
      int k = this.field_147003_i;
      int l = this.field_147009_r;
      this.func_73729_b(k, l, 0, 0, this.field_146999_f, this.field_147000_g);

      for(int i1 = 0; i1 < this.field_147002_h.field_75151_b.size(); ++i1) {
         Slot slot = (Slot)this.field_147002_h.field_75151_b.get(i1);
         if (slot.func_75216_d() && slot.func_75219_a() == 1) {
            this.func_73729_b(k + slot.field_75223_e, l + slot.field_75221_f, 200, 0, 16, 16);
         }
      }

      GuiInventory.func_147046_a(k + 51, l + 75, 30, (float)(k + 51) - this.oldMouseX, (float)(l + 75 - 50) - this.oldMouseY, this.field_146297_k.field_71439_g);
   }

   protected void func_146284_a(GuiButton button) {
      if (button.field_146127_k == 0) {
      }

      if (button.field_146127_k == 1) {
         this.field_146297_k.func_147108_a(new GuiStats(this, this.field_146297_k.field_71439_g.func_146107_m()));
      }

   }

   protected void func_73869_a(char par1, int par2) throws IOException {
      if (par2 == KeyBindings.KEY_EXTENDED_BONDAGE_INVENTORY.func_151463_i()) {
         this.field_146297_k.field_71439_g.func_71053_j();
      } else {
         super.func_73869_a(par1, par2);
      }

   }

   public void displayNormalInventory() {
      GuiInventory gui = new GuiInventory(this.field_146297_k.field_71439_g);
      ReflectionHelper.setPrivateValue(GuiInventory.class, gui, this.oldMouseX, "oldMouseX", "field_147048_u");
      ReflectionHelper.setPrivateValue(GuiInventory.class, gui, this.oldMouseY, "oldMouseY", "field_147047_v");
      this.field_146297_k.func_147108_a(gui);
   }
}
