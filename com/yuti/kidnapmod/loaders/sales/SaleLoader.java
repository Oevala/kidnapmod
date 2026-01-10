package com.yuti.kidnapmod.loaders.sales;

import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.loaders.ItemTask;
import com.yuti.kidnapmod.loaders.ItemTaskLoader;
import com.yuti.kidnapmod.util.Utils;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.Loader;

public class SaleLoader extends ItemTaskLoader {
   private static SaleLoader instance;

   private SaleLoader() {
   }

   @Nullable
   public static synchronized SaleLoader getInstance() {
      if (!ModConfig.kidnappersSell) {
         return null;
      } else {
         if (instance == null) {
            instance = new SaleLoader();
         }

         return instance;
      }
   }

   protected void loadDefaultTasks() {
      if (ModConfig.loadDefaultSales) {
         this.tasksList.add(new ItemTask("minecraft:iron_ingot", 0, 20));
         this.tasksList.add(new ItemTask("minecraft:gold_ingot", 0, 20));
         this.tasksList.add(new ItemTask("minecraft:diamond", 0, 1));
      }

   }

   protected File getDir() {
      File config = Loader.instance().getConfigDir();
      Path path = config.toPath();
      Path toJobs = Paths.get("knapm", "sales");
      path = path.resolve(toJobs);
      File jobsDir = path.toFile();
      if (!jobsDir.exists() || !jobsDir.isDirectory()) {
         jobsDir.mkdir();
      }

      return path.toFile();
   }

   public void printConfigState() {
      if (ModConfig.kidnappersJob) {
         Utils.sendMessageToServer("[Kidnap Mod] Sales enabled");
         if (!ModConfig.loadDefaultJobs) {
            Utils.sendMessageToServer("[Kidnap Mod] Default sales disabled");
         }

         Utils.sendMessageToServer("[Kidnap Mod] " + this.tasksList.size() + " sales loaded");
      }

   }
}
