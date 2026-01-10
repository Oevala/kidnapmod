package com.yuti.kidnapmod.loaders.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.annotation.Nullable;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class GagTalkLoader {
   private static GagTalkLoader instance;
   private static char[] acceptedCharsDefault = new char[]{'?', '!', '.'};
   private Map<Character, String> gagTalkDictionnary = new HashMap();

   private GagTalkLoader() {
      this.load();
   }

   public static synchronized GagTalkLoader getInstance() {
      if (instance == null) {
         instance = new GagTalkLoader();
      }

      return instance;
   }

   public void reload() {
      this.gagTalkDictionnary = new HashMap();
      this.load();
   }

   @Nullable
   private static File getGagTalkFile() {
      File config = Loader.instance().getConfigDir();
      Path path = config.toPath();
      Path toLogs = Paths.get("knapm", "gagtalk.cfg");
      path = path.resolve(toLogs);
      File gagtalkfile = path.toFile();
      return !gagtalkfile.exists() ? null : path.toFile();
   }

   public void load() {
      File gagFile = getGagTalkFile();
      if (gagFile != null && gagFile.exists()) {
         Scanner scan = null;

         try {
            scan = new Scanner(gagFile);
            if (scan != null) {
               while(scan.hasNextLine()) {
                  String line = scan.nextLine();
                  if (line != null) {
                     String[] maps = line.split(" ");
                     if (maps != null && maps.length >= 2) {
                        this.gagTalkDictionnary.put(maps[0].charAt(0), maps[1]);
                     }
                  }
               }
            }
         } catch (FileNotFoundException var8) {
            this.loadDefault();
            var8.printStackTrace();
         } finally {
            if (scan != null) {
               scan.close();
            }

         }
      } else {
         this.loadDefault();
      }

   }

   public void loadDefault() {
      this.gagTalkDictionnary.put('a', "n");
      this.gagTalkDictionnary.put('b', "h");
      this.gagTalkDictionnary.put('c', "c");
      this.gagTalkDictionnary.put('d', "n");
      this.gagTalkDictionnary.put('e', "m");
      this.gagTalkDictionnary.put('f', "f");
      this.gagTalkDictionnary.put('g', "g");
      this.gagTalkDictionnary.put('h', "h");
      this.gagTalkDictionnary.put('i', "n");
      this.gagTalkDictionnary.put('j', "n");
      this.gagTalkDictionnary.put('k', "h");
      this.gagTalkDictionnary.put('l', "m");
      this.gagTalkDictionnary.put('m', "m");
      this.gagTalkDictionnary.put('n', "n");
      this.gagTalkDictionnary.put('o', "n");
      this.gagTalkDictionnary.put('p', "p");
      this.gagTalkDictionnary.put('q', "g");
      this.gagTalkDictionnary.put('r', "r");
      this.gagTalkDictionnary.put('s', "ph");
      this.gagTalkDictionnary.put('t', "ph");
      this.gagTalkDictionnary.put('u', "n");
      this.gagTalkDictionnary.put('v', "f");
      this.gagTalkDictionnary.put('w', "mm");
      this.gagTalkDictionnary.put('x', "gh");
      this.gagTalkDictionnary.put('y', "m");
      this.gagTalkDictionnary.put('z', "ph");
      this.gagTalkDictionnary.put('A', "N");
      this.gagTalkDictionnary.put('B', "H");
      this.gagTalkDictionnary.put('C', "C");
      this.gagTalkDictionnary.put('D', "N");
      this.gagTalkDictionnary.put('E', "M");
      this.gagTalkDictionnary.put('F', "F");
      this.gagTalkDictionnary.put('G', "G");
      this.gagTalkDictionnary.put('H', "H");
      this.gagTalkDictionnary.put('I', "N");
      this.gagTalkDictionnary.put('J', "N");
      this.gagTalkDictionnary.put('K', "H");
      this.gagTalkDictionnary.put('L', "M");
      this.gagTalkDictionnary.put('M', "M");
      this.gagTalkDictionnary.put('N', "N");
      this.gagTalkDictionnary.put('O', "N");
      this.gagTalkDictionnary.put('P', "P");
      this.gagTalkDictionnary.put('Q', "G");
      this.gagTalkDictionnary.put('R', "R");
      this.gagTalkDictionnary.put('S', "PH");
      this.gagTalkDictionnary.put('T', "PH");
      this.gagTalkDictionnary.put('U', "N");
      this.gagTalkDictionnary.put('V', "F");
      this.gagTalkDictionnary.put('W', "MM");
      this.gagTalkDictionnary.put('X', "GH");
      this.gagTalkDictionnary.put('Y', "M");
      this.gagTalkDictionnary.put('Z', "PH");
   }

   public String gagTalkConvertor(World world, String message) {
      if (world != null) {
         GameRules rules = world.func_82736_K();
         if (rules != null && rules.func_82765_e("betterGagTalk") && rules.func_82766_b("betterGagTalk")) {
            return this.convertBetterGagTalk(message);
         }
      }

      return this.convertBasicGagTalk(message);
   }

   private String convertBasicGagTalk(String message) {
      String converted = "";
      if (message != null) {
         String[] var3 = message.split(" ");
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String word = var3[var5];
            converted = converted + this.convertDefaultGagWord(word) + " ";
         }
      }

      return converted;
   }

   private String convertDefaultGagWord(String word) {
      if (word.length() == 0) {
         return "";
      } else {
         String converted = "M";
         int tier = word.length() / 3;

         for(int i = 1; i < word.length(); ++i) {
            if (this.isAcceptedChar(word.charAt(i))) {
               converted = converted + word.charAt(i);
            } else if (i < tier) {
               converted = converted + 'm';
            } else if (i < tier * 2) {
               converted = converted + 'p';
            } else {
               converted = converted + 'h';
            }
         }

         return converted;
      }
   }

   private boolean isAcceptedChar(char character) {
      char[] var2 = acceptedCharsDefault;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         char accepted = var2[var4];
         if (accepted == character) {
            return true;
         }
      }

      return false;
   }

   private String convertBetterGagTalk(String message) {
      if (message != null && message.length() != 0) {
         String newMessage = "";

         for(int i = 0; i < message.length(); ++i) {
            char charToAdd = message.charAt(i);
            if (this.gagTalkDictionnary.containsKey(charToAdd)) {
               newMessage = newMessage + (String)this.gagTalkDictionnary.get(charToAdd);
            } else {
               newMessage = newMessage + charToAdd;
            }
         }

         newMessage = newMessage.substring(0, 1).toUpperCase() + newMessage.substring(1);
         return newMessage;
      } else {
         return "";
      }
   }
}
