package com.yuti.kidnapmod.gui;

import com.yuti.kidnapmod.extrainventory.ExtraBondageItemIdentifier;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketTakeOffBondageItem;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

public class GuiBondageItemsManagment extends GuiScreen {
   private FontRenderer fontRenderer;
   private GuiButton buttonGag;
   private GuiButton buttonBlindfold;
   private GuiButton buttonEarplugs;
   private GuiButton buttonCollar;
   private GuiButton buttonKnives;
   private GuiButton buttonClothes;
   private boolean gag;
   private boolean blindfold;
   private boolean earplugs;
   private boolean collar;
   private boolean knives;
   private boolean clothes;
   private String targetUUID;
   private int middleX;
   private int middleY;

   public GuiBondageItemsManagment(boolean gagIn, boolean blindfoldIn, boolean earplugsIn, boolean collarIn, boolean knivesIn, boolean clothes, String targetUUID) {
      this.gag = gagIn;
      this.blindfold = blindfoldIn;
      this.earplugs = earplugsIn;
      this.collar = collarIn;
      this.knives = knivesIn;
      this.clothes = clothes;
      this.targetUUID = targetUUID;
      this.field_146297_k = Minecraft.func_71410_x();
      this.fontRenderer = this.field_146297_k.field_71466_p;
   }

   public void func_73866_w_() {
      ScaledResolution sr = new ScaledResolution(this.field_146297_k);
      this.middleX = sr.func_78326_a() / 2;
      this.middleY = sr.func_78328_b() / 2;
      this.buttonGag = this.func_189646_b(new GuiButton(1, this.middleX - 170, this.middleY - 20, 70, 20, I18n.func_135052_a("gui.managebondage.gag", new Object[0])));
      this.buttonBlindfold = this.func_189646_b(new GuiButton(2, this.middleX - 80, this.middleY - 20, 70, 20, I18n.func_135052_a("gui.managebondage.blindfold", new Object[0])));
      this.buttonEarplugs = this.func_189646_b(new GuiButton(3, this.middleX + 10, this.middleY - 20, 70, 20, I18n.func_135052_a("gui.managebondage.earplugs", new Object[0])));
      this.buttonCollar = this.func_189646_b(new GuiButton(4, this.middleX + 100, this.middleY - 20, 70, 20, I18n.func_135052_a("gui.managebondage.collar", new Object[0])));
      this.buttonKnives = this.func_189646_b(new GuiButton(5, this.middleX - 80, this.middleY + 10, 70, 20, I18n.func_135052_a("gui.managebondage.knives", new Object[0])));
      this.buttonClothes = this.func_189646_b(new GuiButton(6, this.middleX + 10, this.middleY + 10, 70, 20, I18n.func_135052_a("gui.managebondage.clothes", new Object[0])));
      this.buttonGag.field_146124_l = this.gag;
      this.buttonBlindfold.field_146124_l = this.blindfold;
      this.buttonEarplugs.field_146124_l = this.earplugs;
      this.buttonCollar.field_146124_l = this.collar;
      this.buttonKnives.field_146124_l = this.knives;
      this.buttonClothes.field_146124_l = this.clothes;
      super.func_73866_w_();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.func_73732_a(this.fontRenderer, I18n.func_135052_a("gui.managebondage.message", new Object[0]), this.middleX, this.middleY - 40, 16777215);
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      int selection = -1;
      if (button.equals(this.buttonGag)) {
         selection = ExtraBondageItemIdentifier.GAG.getId();
      } else if (button.equals(this.buttonBlindfold)) {
         selection = ExtraBondageItemIdentifier.BLINFOLD.getId();
      } else if (button.equals(this.buttonEarplugs)) {
         selection = ExtraBondageItemIdentifier.EARPLUGS.getId();
      } else if (button.equals(this.buttonCollar)) {
         selection = ExtraBondageItemIdentifier.COLLAR.getId();
      } else if (button.equals(this.buttonKnives)) {
         selection = ExtraBondageItemIdentifier.KNIVES.getId();
      } else if (button.equals(this.buttonClothes)) {
         selection = ExtraBondageItemIdentifier.CLOTHES.getId();
      }

      if (selection != -1 && this.targetUUID != null) {
         PacketHandler.INSTANCE.sendToServer(new PacketTakeOffBondageItem(selection, this.targetUUID));
      }

      super.func_146284_a(button);
      this.field_146297_k.field_71439_g.func_71053_j();
   }
}
