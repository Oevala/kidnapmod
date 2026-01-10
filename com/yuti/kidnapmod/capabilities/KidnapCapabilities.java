package com.yuti.kidnapmod.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class KidnapCapabilities {
   @CapabilityInject(KidnapSettingsCapabilities.class)
   public static final Capability<KidnapSettingsCapabilities> KIDNAP_SETTINGS = null;
}
