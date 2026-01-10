package com.yuti.kidnapmod.loaders;

import com.yuti.kidnapmod.loaders.jobs.JobLoader;
import com.yuti.kidnapmod.loaders.sales.SaleLoader;
import com.yuti.kidnapmod.util.Utils;

public class LoadersInit {
   public static void init() {
      JobLoader jobLoader = JobLoader.getInstance();
      if (jobLoader != null) {
         jobLoader.printConfigState();
      } else {
         Utils.sendMessageToServer("[Kidnap Mod] Jobs disabled");
      }

      SaleLoader saleLoader = SaleLoader.getInstance();
      if (saleLoader != null) {
         saleLoader.printConfigState();
      } else {
         Utils.sendMessageToServer("[Kidnap Mod] Sales disabled");
      }

   }
}
