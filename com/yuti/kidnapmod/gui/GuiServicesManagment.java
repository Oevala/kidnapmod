package com.yuti.kidnapmod.gui;

import com.mojang.authlib.GameProfile;
import com.yuti.kidnapmod.items.ItemCollar;
import com.yuti.kidnapmod.network.PacketHandler;
import com.yuti.kidnapmod.network.PacketManageSlaveService;
import com.yuti.kidnapmod.util.Utils;
import com.yuti.kidnapmod.util.teleport.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiServicesManagment extends GuiScreen {
   private FontRenderer fontRenderer;
   private GuiCheckBox buttonBondageService;
   private GuiCheckBox buttonKidnappingMode;
   private GuiCheckBox buttonWarnMasters;
   private GuiCheckBox buttonBackHome;
   private GuiCheckBox buttonTiedSlaveFence;
   private GuiButton buttonSetHome;
   private GuiButton buttonSetPrison;
   private GuiButton buttonQuit;
   private GuiTextField playerNameField;
   private GuiCheckBox buttonWhitelist;
   private GuiButton buttonAddWhitelist;
   private GuiButton buttonRemoveWhitelist;
   private GuiCheckBox buttonBlackList;
   private GuiButton buttonAddBlaclist;
   private GuiButton buttonRemoveBlacklist;
   private GuiButton buttonAddOwner;
   private GuiTextField radiusField;
   private ItemStack collar;
   private boolean isKidnapper;
   private Position position;
   private String targetUUID;
   private int middleX;
   private int middleY;

   public GuiServicesManagment(ItemStack collar, boolean isKidnapper, Position position, String targetUUID) {
      this.collar = collar;
      this.isKidnapper = isKidnapper;
      this.position = position;
      this.targetUUID = targetUUID;
      this.field_146297_k = Minecraft.func_71410_x();
      this.fontRenderer = this.field_146297_k.field_71466_p;
   }

   public void func_73866_w_() {
      ScaledResolution sr = new ScaledResolution(this.field_146297_k);
      this.middleX = sr.func_78326_a() / 2;
      this.middleY = sr.func_78328_b() / 2;
      if (this.collar != null && this.collar.func_77973_b() instanceof ItemCollar) {
         ItemCollar itemCollar = (ItemCollar)this.collar.func_77973_b();
         this.buttonBondageService = (GuiCheckBox)this.func_189646_b(new GuiCheckBox(1, this.middleX - 150, this.middleY - 80, I18n.func_135052_a("gui.manageservices.bondage", new Object[0]), itemCollar.isBondageServiceEnabled(this.collar)));
         this.buttonKidnappingMode = (GuiCheckBox)this.func_189646_b(new GuiCheckBox(2, this.middleX - 150, this.middleY - 60, I18n.func_135052_a("gui.manageservices.napmod", new Object[0]), itemCollar.isKidnappingModeEnabled(this.collar)));
         this.buttonWarnMasters = (GuiCheckBox)this.func_189646_b(new GuiCheckBox(3, this.middleX - 150, this.middleY - 40, I18n.func_135052_a("gui.manageservices.warn", new Object[0]), itemCollar.shouldWarnMasters(this.collar)));
         this.buttonTiedSlaveFence = (GuiCheckBox)this.func_189646_b(new GuiCheckBox(4, this.middleX - 150, this.middleY - 20, I18n.func_135052_a("gui.manageservices.fence", new Object[0]), itemCollar.isPrisonFenceEnabled(this.collar)));
         this.buttonBackHome = (GuiCheckBox)this.func_189646_b(new GuiCheckBox(5, this.middleX - 150, this.middleY, I18n.func_135052_a("gui.manageservices.backhome", new Object[0]), itemCollar.shouldBackHome(this.collar)));
         this.buttonSetHome = this.func_189646_b(new GuiButton(6, this.middleX - 150, this.middleY + 20, 120, 20, I18n.func_135052_a("gui.manageservices.sethome", new Object[0])));
         this.buttonSetPrison = this.func_189646_b(new GuiButton(7, this.middleX - 150, this.middleY + 50, 120, 20, I18n.func_135052_a("gui.manageservices.setprison", new Object[0])));
         this.playerNameField = new GuiTextField(8, this.fontRenderer, this.middleX + 40, this.middleY - 70, 120, 15);
         this.buttonWhitelist = (GuiCheckBox)this.func_189646_b(new GuiCheckBox(9, this.middleX + 70, this.middleY - 45, I18n.func_135052_a("gui.manageservices.whitelist", new Object[0]), !itemCollar.isBlacklistenabled(this.collar)));
         this.buttonAddWhitelist = this.func_189646_b(new GuiButton(10, this.middleX + 40, this.middleY - 30, 55, 20, I18n.func_135052_a("gui.manageservices.add", new Object[0])));
         this.buttonRemoveWhitelist = this.func_189646_b(new GuiButton(11, this.middleX + 105, this.middleY - 30, 55, 20, I18n.func_135052_a("gui.manageservices.remove", new Object[0])));
         this.buttonBlackList = (GuiCheckBox)this.func_189646_b(new GuiCheckBox(12, this.middleX + 70, this.middleY, I18n.func_135052_a("gui.manageservices.blacklist", new Object[0]), itemCollar.isBlacklistenabled(this.collar)));
         this.buttonAddBlaclist = this.func_189646_b(new GuiButton(13, this.middleX + 40, this.middleY + 15, 55, 20, I18n.func_135052_a("gui.manageservices.add", new Object[0])));
         this.buttonRemoveBlacklist = this.func_189646_b(new GuiButton(14, this.middleX + 105, this.middleY + 15, 55, 20, I18n.func_135052_a("gui.manageservices.remove", new Object[0])));
         this.buttonAddOwner = this.func_189646_b(new GuiButton(15, this.middleX + 40, this.middleY + 55, 55, 20, I18n.func_135052_a("gui.manageservices.add", new Object[0])));
         this.radiusField = new GuiTextField(16, this.fontRenderer, this.middleX + 105, this.middleY + 55, 55, 18);
         this.radiusField.func_146180_a("" + itemCollar.getPrisonRadius(this.collar));
         this.buttonKidnappingMode.field_146124_l = this.isKidnapper;
         this.buttonBackHome.field_146124_l = this.isKidnapper;
         this.buttonWhitelist.field_146124_l = this.isKidnapper;
         this.buttonAddWhitelist.field_146124_l = this.isKidnapper;
         this.buttonRemoveWhitelist.field_146124_l = this.isKidnapper;
         this.buttonBlackList.field_146124_l = this.isKidnapper;
         this.buttonAddBlaclist.field_146124_l = this.isKidnapper;
         this.buttonRemoveBlacklist.field_146124_l = this.isKidnapper;
         this.radiusField.func_146184_c(this.isKidnapper);
      }

      this.buttonQuit = this.func_189646_b(new GuiButton(17, this.middleX - 30, this.middleY + 90, 70, 20, I18n.func_135052_a("gui.manageservices.quit", new Object[0])));
      super.func_73866_w_();
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.func_73732_a(this.fontRenderer, I18n.func_135052_a("gui.manageservices.intro", new Object[0]), this.middleX, this.middleY - 100, 16777215);
      this.func_73732_a(this.fontRenderer, I18n.func_135052_a("gui.manageservices.target", new Object[0]), this.middleX + 100, this.middleY - 80, 16777215);
      this.func_73732_a(this.fontRenderer, I18n.func_135052_a("gui.manageservices.owners", new Object[0]), this.middleX + 65, this.middleY + 45, 16777215);
      this.func_73732_a(this.fontRenderer, I18n.func_135052_a("gui.manageservices.radius", new Object[0]), this.middleX + 133, this.middleY + 45, 16777215);
      this.playerNameField.func_146194_f();
      this.radiusField.func_146194_f();
      super.func_73863_a(mouseX, mouseY, partialTicks);
   }

   protected synchronized void func_146284_a(GuiButton button) throws IOException {
      if (button.equals(this.buttonQuit)) {
         this.beforeQuit();
         this.field_146297_k.field_71439_g.func_71053_j();
      } else if (this.collar != null && this.collar.func_77973_b() instanceof ItemCollar) {
         ItemCollar itemCollar = (ItemCollar)this.collar.func_77973_b();
         if (button.equals(this.buttonBondageService)) {
            if (this.buttonBondageService.isChecked()) {
               itemCollar.enableBondageService(this.collar);
            } else {
               itemCollar.disableBondageService(this.collar);
            }
         } else if (button.equals(this.buttonKidnappingMode)) {
            if (this.buttonKidnappingMode.isChecked()) {
               itemCollar.enableKidnappingMode(this.collar);
            } else {
               itemCollar.disableKidnappingMode(this.collar);
            }
         } else if (button.equals(this.buttonWarnMasters)) {
            if (this.buttonWarnMasters.isChecked()) {
               itemCollar.enableWarnMasters(this.collar);
            } else {
               itemCollar.disableWarnMasters(this.collar);
            }
         } else if (button.equals(this.buttonWhitelist)) {
            itemCollar.setBlackListState(this.collar, !this.buttonWhitelist.isChecked());
            this.buttonBlackList.setIsChecked(!this.buttonWhitelist.isChecked());
         } else if (button.equals(this.buttonBlackList)) {
            itemCollar.setBlackListState(this.collar, this.buttonBlackList.isChecked());
            this.buttonWhitelist.setIsChecked(!this.buttonBlackList.isChecked());
         } else if (button.equals(this.buttonBackHome)) {
            itemCollar.setBackHome(this.collar, this.buttonBackHome.isChecked());
         } else if (button.equals(this.buttonTiedSlaveFence)) {
            itemCollar.setPrisonFencee(this.collar, this.buttonTiedSlaveFence.isChecked());
         } else if (button.equals(this.buttonSetHome)) {
            if (this.position != null) {
               itemCollar.setHome(this.collar, this.position);
               Utils.sendValidMessageToEntity(this.field_146297_k.field_71439_g, "Home set!");
            }
         } else if (button.equals(this.buttonSetPrison)) {
            if (this.position != null) {
               itemCollar.setPrison(this.collar, this.position);
               Utils.sendValidMessageToEntity(this.field_146297_k.field_71439_g, "Prison set!");
            }
         } else {
            String playerName = this.playerNameField.func_146179_b();
            if (playerName != null && !playerName.equals("")) {
               if (!button.equals(this.buttonAddWhitelist) && !(button.equals(this.buttonAddBlaclist) | button.equals(this.buttonAddOwner))) {
                  if (button.equals(this.buttonRemoveWhitelist)) {
                     if (itemCollar.isTargetExceptionByName(this.collar, playerName)) {
                        itemCollar.removeTargetException(this.collar, playerName);
                        this.playerNameField.func_146180_a("");
                        Utils.sendValidMessageToEntity(this.field_146297_k.field_71439_g, playerName + " has been removed from the whitelist.");
                     } else {
                        Utils.sendErrorMessageToEntity(this.field_146297_k.field_71439_g, playerName + " is not on the whitelist.");
                     }
                  } else if (button.equals(this.buttonRemoveBlacklist)) {
                     if (itemCollar.isOnBlackListByName(this.collar, playerName)) {
                        itemCollar.removeFromBlackList(this.collar, playerName);
                        this.playerNameField.func_146180_a("");
                        Utils.sendValidMessageToEntity(this.field_146297_k.field_71439_g, playerName + " has been removed from the blacklist.");
                     } else {
                        Utils.sendErrorMessageToEntity(this.field_146297_k.field_71439_g, playerName + " is not on the blacklist.");
                     }
                  }
               } else {
                  UUID id = this.getUUIDFromNameOnClientSide(playerName);
                  if (id == null) {
                     Utils.sendErrorMessageToEntity(this.field_146297_k.field_71439_g, "Can't find " + playerName);
                  } else if (button.equals(this.buttonAddWhitelist)) {
                     if (itemCollar.isTargetException(this.collar, id)) {
                        Utils.sendErrorMessageToEntity(this.field_146297_k.field_71439_g, playerName + " is already on the whitelist.");
                     } else {
                        itemCollar.addTargetException(this.collar, id, playerName);
                        this.playerNameField.func_146180_a("");
                        Utils.sendValidMessageToEntity(this.field_146297_k.field_71439_g, playerName + " added to the whitelist.");
                     }
                  } else if (button.equals(this.buttonAddBlaclist)) {
                     if (itemCollar.isOnBlackList(this.collar, id)) {
                        Utils.sendErrorMessageToEntity(this.field_146297_k.field_71439_g, playerName + " is already on the blacklist.");
                     } else {
                        itemCollar.addToBlackList(this.collar, id, playerName);
                        this.playerNameField.func_146180_a("");
                        Utils.sendValidMessageToEntity(this.field_146297_k.field_71439_g, playerName + " added to the blacklist.");
                     }
                  } else if (button.equals(this.buttonAddOwner)) {
                     if (itemCollar.isOwner(this.collar, id)) {
                        Utils.sendErrorMessageToEntity(this.field_146297_k.field_71439_g, playerName + " is already one of the owners of this collar.");
                     } else {
                        itemCollar.addOwner(this.collar, id, playerName);
                        this.playerNameField.func_146180_a("");
                        Utils.sendValidMessageToEntity(this.field_146297_k.field_71439_g, playerName + " added to owners.");
                     }
                  }
               }
            }
         }
      }

      super.func_146284_a(button);
   }

   protected void func_73869_a(char typedChar, int keyCode) throws IOException {
      this.playerNameField.func_146201_a(typedChar, keyCode);
      String numberText;
      if (this.playerNameField.func_146206_l()) {
         if (keyCode == 15) {
            numberText = this.playerNameField.func_146179_b();
            if (numberText != null && !numberText.equals("")) {
               String[] arg = new String[]{numberText};
               List<String> possibleNames = CommandBase.func_175762_a(arg, this.getAllPossiblesNames());
               if (possibleNames != null && !possibleNames.isEmpty()) {
                  this.playerNameField.func_146180_a((String)possibleNames.get(0));
               }
            }
         }
      } else if (this.radiusField.func_146201_a(typedChar, keyCode)) {
         numberText = this.radiusField.func_146179_b();
         if (numberText != null && !numberText.equals("")) {
            try {
               int radius = Integer.parseInt(numberText);
               if (radius < 3) {
                  radius = 3;
               }

               if (radius > 1000) {
                  radius = 1000;
                  this.radiusField.func_146180_a("" + radius);
               }

               if (this.collar != null && this.collar.func_77973_b() instanceof ItemCollar) {
                  ItemCollar itemCollar = (ItemCollar)this.collar.func_77973_b();
                  itemCollar.setPrisonRadius(this.collar, radius);
               }
            } catch (NumberFormatException var6) {
            }
         }
      }

      if (keyCode == 1) {
         this.beforeQuit();
      }

      super.func_73869_a(typedChar, keyCode);
   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      this.playerNameField.func_146192_a(mouseX, mouseY, mouseButton);
      this.radiusField.func_146192_a(mouseX, mouseY, mouseButton);
      super.func_73864_a(mouseX, mouseY, mouseButton);
   }

   public void func_73876_c() {
      this.playerNameField.func_146178_a();
      this.radiusField.func_146178_a();
      super.func_73876_c();
   }

   public void beforeQuit() {
      if (this.targetUUID != null) {
         PacketHandler.INSTANCE.sendToServer(new PacketManageSlaveService(this.collar, this.targetUUID));
      }

   }

   public UUID getUUIDFromNameOnClientSide(String name) {
      if (name != null) {
         Collection<NetworkPlayerInfo> players = Minecraft.func_71410_x().func_147114_u().func_175106_d();
         if (players != null) {
            Iterator<NetworkPlayerInfo> it = players.iterator();
            if (it != null) {
               while(it.hasNext()) {
                  NetworkPlayerInfo info = (NetworkPlayerInfo)it.next();
                  if (info != null) {
                     GameProfile playerProfile = info.func_178845_a();
                     if (playerProfile != null) {
                        UUID id = playerProfile.getId();
                        if (id != null) {
                           String playerName = playerProfile.getName();
                           if (playerName != null && playerName.equals(name)) {
                              return id;
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   public List<String> getAllPossiblesNames() {
      List<String> names = new ArrayList();
      Collection<NetworkPlayerInfo> players = Minecraft.func_71410_x().func_147114_u().func_175106_d();
      if (players != null) {
         Iterator<NetworkPlayerInfo> it = players.iterator();
         if (it != null) {
            while(it.hasNext()) {
               NetworkPlayerInfo info = (NetworkPlayerInfo)it.next();
               if (info != null) {
                  GameProfile playerProfile = info.func_178845_a();
                  if (playerProfile != null) {
                     String playerName = playerProfile.getName();
                     if (playerName != null) {
                        names.add(playerName);
                     }
                  }
               }
            }
         }
      }

      if (this.collar != null && this.collar.func_77973_b() instanceof ItemCollar) {
         ItemCollar itemCollar = (ItemCollar)this.collar.func_77973_b();
         List<String> whitelistNames = itemCollar.getTargetExceptionNames(this.collar);
         if (whitelistNames != null) {
            names.addAll(whitelistNames);
         }

         List<String> blacklistNames = itemCollar.getBlacklistNames(this.collar);
         if (blacklistNames != null) {
            names.addAll(blacklistNames);
         }
      }

      return names;
   }
}
