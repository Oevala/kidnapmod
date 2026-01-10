package com.yuti.kidnapmod.loaders.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.util.Strings;

public class StopRpManager {
   private static File getLogsDir() {
      File config = Loader.instance().getConfigDir();
      Path path = config.toPath();
      Path toLogs = Paths.get("knapm", "logs");
      path = path.resolve(toLogs);
      File logsDir = path.toFile();
      if (!logsDir.exists() || !logsDir.isDirectory()) {
         logsDir.mkdir();
      }

      return path.toFile();
   }

   private static File getNoRpFile() {
      File dir = getLogsDir();
      Path path = dir.toPath();
      Path toFile = Paths.get("norp.log");
      path = path.resolve(toFile);
      File noRpFile = path.toFile();
      if (!noRpFile.exists()) {
         try {
            noRpFile.createNewFile();
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

      return noRpFile;
   }

   public static synchronized void writeNoRpLog(String playerName, List<String> playersAround) {
      File file = getNoRpFile();
      if (file != null && file.exists() && playerName != null && playersAround != null) {
         Date date = new Date();
         String log = "[" + date.toString() + "]\n";
         log = log + "Triggered by : " + playerName + "\n";
         log = log + "Players around : [" + Strings.join(playersAround, ',') + "]\n";
         log = log + "\n";

         try {
            FileOutputStream stream = new FileOutputStream(file, true);
            stream.write(log.getBytes());
            stream.close();
         } catch (FileNotFoundException var6) {
            var6.printStackTrace();
         } catch (IOException var7) {
            var7.printStackTrace();
         }
      }

   }
}
