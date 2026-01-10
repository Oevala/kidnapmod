package com.yuti.kidnapmod.init;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindings {
   public static final List<KeyBinding> keyBindings = new ArrayList();
   public static final KeyBinding KEY_STRUGGLE = registerKeyBinding("key.struggle.desc", 34, "key.kidnapmod.category");
   public static final KeyBinding KEY_TIGHTEN = registerKeyBinding("key.tighten.binds.desc", 37, "key.kidnapmod.category");
   public static final KeyBinding KEY_BOUNTIES_LIST = registerKeyBinding("key.bounties.list.desc", 48, "key.kidnapmod.category");
   public static final KeyBinding KEY_DISABLE_TALK_AREA = registerKeyBinding("key.disabletalkarea.desc", 27, "key.kidnapmod.category");
   public static final KeyBinding KEY_TALKPREF = registerKeyBinding("key.talkpref.desc", 39, "key.kidnapmod.category");
   public static final KeyBinding KEY_EXTENDED_BONDAGE_INVENTORY = registerKeyBinding("key.bondageinventory.desc", 45, "key.kidnapmod.category");
   public static final KeyBinding KEY_NORP = registerKeyBinding("key.norp.desc", 0, "key.kidnapmod.category");

   public static KeyBinding registerKeyBinding(String keyDesc, int keyCode, String category) {
      KeyBinding binding = new KeyBinding(keyDesc, keyCode, category);
      keyBindings.add(binding);
      return binding;
   }
}
