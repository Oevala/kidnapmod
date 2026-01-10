package com.yuti.kidnapmod.init;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class KidnapModSoundEvents {
   public static List<SoundEvent> SOUNDS = new ArrayList();
   public static final SoundEvent COLLAR_KEY_CLOSE = registerSound("knapm", "collar_key_close");
   public static final SoundEvent COLLAR_KEY_OPEN = registerSound("knapm", "collar_key_open");
   public static final SoundEvent COLLAR_PUT = registerSound("knapm", "collar_put");
   public static final SoundEvent ELECTRIC_SHOCK = registerSound("knapm", "electric_shock");
   public static final SoundEvent SHOCKER_ACTIVATED = registerSound("knapm", "shocker_activated");
   public static final SoundEvent STICKY = registerSound("knapm", "sticky");
   public static final SoundEvent SLAP = registerSound("knapm", "slap");
   public static final SoundEvent WHIP = registerSound("knapm", "whip");
   public static final SoundEvent CHAIN = registerSound("knapm", "chain");
   public static final SoundEvent SLIME = registerSound("knapm", "slime");
   public static final SoundEvent VINE = registerSound("knapm", "vine");

   private static SoundEvent registerSound(String id, String ressource) {
      SoundEvent newSound = new SoundEvent(new ResourceLocation(id, ressource));
      newSound.setRegistryName(ressource);
      SOUNDS.add(newSound);
      return newSound;
   }
}
