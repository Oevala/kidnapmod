package com.yuti.kidnapmod.commands;

import net.minecraft.command.CommandBase;

public abstract class CommandKidnapMod extends CommandBase {
   public boolean isCallableWhileTiedUp() {
      return false;
   }

   public boolean isBypassingBlock() {
      return false;
   }
}
