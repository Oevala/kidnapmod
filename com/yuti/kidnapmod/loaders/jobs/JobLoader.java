package com.yuti.kidnapmod.loaders.jobs;

import com.yuti.kidnapmod.common.ModConfig;
import com.yuti.kidnapmod.loaders.ItemTask;
import com.yuti.kidnapmod.loaders.ItemTaskLoader;
import com.yuti.kidnapmod.util.Utils;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nullable;
import net.minecraftforge.fml.common.Loader;

public class JobLoader extends ItemTaskLoader {
   private static JobLoader instance;

   private JobLoader() {
   }

   @Nullable
   public static synchronized JobLoader getInstance() {
      if (!ModConfig.kidnappersJob) {
         return null;
      } else {
         if (instance == null) {
            instance = new JobLoader();
         }

         return instance;
      }
   }

   protected void loadDefaultTasks() {
      if (ModConfig.loadDefaultJobs) {
         this.tasksList.add(new ItemTask("minecraft:iron_ingot", 0, 20));
         this.tasksList.add(new ItemTask("minecraft:gold_ingot", 0, 20));
         this.tasksList.add(new ItemTask("minecraft:diamond", 0, 1));
      }

   }

   protected File getDir() {
      File config = Loader.instance().getConfigDir();
      Path path = config.toPath();
      Path toJobs = Paths.get("knapm", "jobs");
      path = path.resolve(toJobs);
      File jobsDir = path.toFile();
      if (!jobsDir.exists() || !jobsDir.isDirectory()) {
         jobsDir.mkdir();
      }

      return path.toFile();
   }

   public void printConfigState() {
      if (ModConfig.kidnappersJob) {
         Utils.sendMessageToServer("[Kidnap Mod] Jobs enabled");
         if (!ModConfig.loadDefaultJobs) {
            Utils.sendMessageToServer("[Kidnap Mod] Default jobs disabled");
         }

         Utils.sendMessageToServer("[Kidnap Mod] " + this.tasksList.size() + " jobs loaded");
      }

   }
}
