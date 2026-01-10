package com.yuti.kidnapmod.util;

import com.yuti.kidnapmod.common.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.ClickEvent.Action;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UtilsClient {
   @SideOnly(Side.CLIENT)
   public static boolean hasSmallArms(AbstractClientPlayer player) {
      if (player == null) {
         return false;
      } else {
         String skinType = player.func_175154_l();
         return skinType != null && skinType.equals("slim");
      }
   }

   public static void displayModInformationToPlayer() {
      if (ModConfig.modInfosOnLog) {
         EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
         if (player != null) {
            TextComponentTranslation introMessage = new TextComponentTranslation("info.intro", new Object[]{"Kidnap Mod", "Yuti"});
            introMessage.func_150256_b().func_150238_a(TextFormatting.GOLD);
            TextComponentString wiki = new TextComponentString(TextFormatting.BLUE + "Wiki, ");
            TextComponentString youtube = new TextComponentString(TextFormatting.BLUE + "Youtube, ");
            TextComponentString deviantart = new TextComponentString(TextFormatting.BLUE + "Deviantart, ");
            TextComponentString discord = new TextComponentString(TextFormatting.BLUE + "Discord");
            ClickEvent clickWiki = new ClickEvent(Action.OPEN_URL, "https://minecraft-kidnap-mod.fandom.com/wiki/Minecraft_Kidnap_Mod_Wiki");
            ClickEvent clickYoutube = new ClickEvent(Action.OPEN_URL, "https://www.youtube.com/channel/UChNcx2B-L01_OsjxYqSPbOg");
            ClickEvent clickDeviantArt = new ClickEvent(Action.OPEN_URL, "https://www.deviantart.com/theyuti35");
            ClickEvent clickDiscord = new ClickEvent(Action.OPEN_URL, "https://discord.com/invite/nP5aPqN");
            wiki.func_150256_b().func_150241_a(clickWiki);
            youtube.func_150256_b().func_150241_a(clickYoutube);
            deviantart.func_150256_b().func_150241_a(clickDeviantArt);
            discord.func_150256_b().func_150241_a(clickDiscord);
            TextComponentString resourcesMessage = new TextComponentString(TextFormatting.GOLD + "-> " + TextFormatting.GREEN);
            TextComponentTranslation linksTranslated = new TextComponentTranslation("info.links", new Object[0]);
            linksTranslated.func_150256_b().func_150238_a(TextFormatting.GREEN);
            resourcesMessage.func_150257_a(linksTranslated);
            resourcesMessage.func_150257_a(new TextComponentString(" : "));
            resourcesMessage.func_150257_a(wiki);
            resourcesMessage.func_150257_a(youtube);
            resourcesMessage.func_150257_a(deviantart);
            resourcesMessage.func_150257_a(discord);
            player.func_145747_a(introMessage);
            player.func_145747_a(resourcesMessage);
         }
      }

   }
}
