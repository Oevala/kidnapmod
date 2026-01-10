package com.yuti.kidnapmod.loaders;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ItemTaskLoader {
   protected List<ItemTask> tasksList;

   protected ItemTaskLoader() {
      this.loadTasks();
   }

   public void reload() {
      this.loadTasks();
   }

   public void loadTasks() {
      this.tasksList = new ArrayList();
      File taskDir = this.getDir();
      Gson gson = new Gson();
      File[] var3 = taskDir.listFiles();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         File taskFile = var3[var5];
         if (taskFile.isFile()) {
            try {
               FileReader jobReader = new FileReader(taskFile);
               ItemTask task = (ItemTask)gson.fromJson(jobReader, ItemTask.class);
               this.tasksList.add(task);
            } catch (Exception var9) {
               var9.printStackTrace();
            }
         }
      }

      this.loadDefaultTasks();
   }

   protected abstract void loadDefaultTasks();

   protected abstract File getDir();

   public ItemTask getRandomTask() {
      if (this.tasksList != null && !this.tasksList.isEmpty()) {
         Random rand = new Random();
         return (ItemTask)this.tasksList.get(rand.nextInt(this.tasksList.size()));
      } else {
         return null;
      }
   }

   public boolean hasTasks() {
      return this.tasksList != null && this.tasksList.size() > 0;
   }

   public abstract void printConfigState();
}
