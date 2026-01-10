package com.yuti.kidnapmod.extrainventory;

import com.yuti.kidnapmod.init.KidnapModSoundEvents;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.SoundEvent;

public class ExtraBondageMaterial {
   public static final ExtraBondageMaterial GAG_CLASSIC;
   public static final ExtraBondageMaterial ROPES;
   public static final ExtraBondageMaterial BALLGAG;
   public static final ExtraBondageMaterial BALLGAG_STRAP;
   public static final ExtraBondageMaterial STRAITJACKET;
   public static final ExtraBondageMaterial LATEX;
   public static final ExtraBondageMaterial TAPE;
   public static final ExtraBondageMaterial TAPE_CLEAR;
   public static final ExtraBondageMaterial TAPE_CLEAR_STUFF;
   public static final ExtraBondageMaterial TAPE_CAUTION;
   public static final ExtraBondageMaterial GAG_PANEL;
   public static final ExtraBondageMaterial WRAP;
   public static final ExtraBondageMaterial CLASSIC_BLINDFOLD;
   public static final ExtraBondageMaterial MASK_BLINDFOLD;
   public static final ExtraBondageMaterial CLASSIC_HOOD;
   public static final ExtraBondageMaterial CLASSIC_EARPLUGS;
   public static final ExtraBondageMaterial CLASSIC_COLLAR;
   public static final ExtraBondageMaterial SHOCK_COLLAR;
   public static final ExtraBondageMaterial CHAIN;
   public static final ExtraBondageMaterial ARM_BINDER;
   public static final ExtraBondageMaterial BITE_GAG;
   public static final ExtraBondageMaterial SLIME;
   public static final ExtraBondageMaterial RIBBON;
   public static final ExtraBondageMaterial SHIBARI;
   public static final ExtraBondageMaterial STRAPS;
   public static final ExtraBondageMaterial MEDICAL_STRAPS;
   public static final ExtraBondageMaterial TUBE;
   public static final ExtraBondageMaterial VINE;
   public static final ExtraBondageMaterial WEB;
   public static final ExtraBondageMaterial CLOTHES;
   public static final ExtraBondageMaterial CLEAVE_GAG;
   public static final ExtraBondageMaterial BAGUETTE_GAG;
   public static final ExtraBondageMaterial SPONGE_GAG;
   public static final ExtraBondageMaterial BEAM;
   private final String name;
   private final String domain;
   private final String location;
   private SoundEvent sound;

   public ExtraBondageMaterial(String nameIn, String domain, String locationIn, SoundEvent soundIn) {
      this.name = nameIn;
      this.domain = domain;
      this.location = locationIn;
      this.sound = soundIn;
   }

   public ExtraBondageMaterial(ExtraBondageMaterial material, String color) {
      this.name = material.getName() + "_" + color.toUpperCase();
      this.domain = material.getDomain();
      this.location = material.getLocation() + "_" + color;
      this.sound = material.getSound();
   }

   public String getName() {
      return this.name;
   }

   public String getDomain() {
      return this.domain;
   }

   public String getLocation() {
      return this.location;
   }

   public SoundEvent getSound() {
      return this.sound;
   }

   public static List<ExtraBondageMaterial> registerVariants(EnumDyeColor mainDye, ExtraBondageMaterial base) {
      List<ExtraBondageMaterial> variants = new ArrayList();
      variants.add(base);

      for(int i = 1; i < 16; ++i) {
         EnumDyeColor dye = EnumDyeColor.func_176764_b(i);
         String colorName = dye.func_192396_c();
         if (dye == mainDye) {
            colorName = EnumDyeColor.WHITE.func_192396_c();
         }

         ExtraBondageMaterial newMaterial = new ExtraBondageMaterial(base, colorName);
         variants.add(newMaterial);
      }

      return variants;
   }

   static {
      GAG_CLASSIC = new ExtraBondageMaterial("GAG_CLASSIC", "knapm", "cloth/cloth_gag", SoundEvents.field_187552_ah);
      ROPES = new ExtraBondageMaterial("ROPES", "knapm", "ropes/ropes", SoundEvents.field_187748_db);
      BALLGAG = new ExtraBondageMaterial("BALLGAG", "knapm", "ballgags/normal/ballgag", SoundEvents.field_187552_ah);
      BALLGAG_STRAP = new ExtraBondageMaterial("BALLGAG_STRAP", "knapm", "ballgags/harness/ballgag_strap", SoundEvents.field_187552_ah);
      STRAITJACKET = new ExtraBondageMaterial("STRAITJACKET", "knapm", "straitjacket/straitjacket", SoundEvents.field_187748_db);
      LATEX = new ExtraBondageMaterial("LATEX", "knapm", "latex/latex", SoundEvents.field_187748_db);
      TAPE = new ExtraBondageMaterial("TAPE", "knapm", "tape/tape", KidnapModSoundEvents.STICKY);
      TAPE_CLEAR = new ExtraBondageMaterial("TAPE_CLEAR", "knapm", "tape/tape_clear", KidnapModSoundEvents.STICKY);
      TAPE_CLEAR_STUFF = new ExtraBondageMaterial("TAPE_CLEAR_STUFF", "knapm", "tape/tape_clear_stuff", KidnapModSoundEvents.STICKY);
      TAPE_CAUTION = new ExtraBondageMaterial("TAPE_CAUTION", "knapm", "tape/tape_caution", KidnapModSoundEvents.STICKY);
      GAG_PANEL = new ExtraBondageMaterial("GAG_PANEL", "knapm", "straitjacket/panel_gag", SoundEvents.field_187552_ah);
      WRAP = new ExtraBondageMaterial("WRAP", "knapm", "wrap/wrapped", KidnapModSoundEvents.STICKY);
      CLASSIC_BLINDFOLD = new ExtraBondageMaterial("CLASSIC_BLINDFOLD", "knapm", "blindfolds/classic/classic_blindfold", SoundEvents.field_187552_ah);
      MASK_BLINDFOLD = new ExtraBondageMaterial("MASK_BLINDFOLD", "knapm", "blindfolds/mask/mask_blindfold", SoundEvents.field_187748_db);
      CLASSIC_HOOD = new ExtraBondageMaterial("CLASSIC_HOOD", "knapm", "hoods/classic_hood", SoundEvents.field_187748_db);
      CLASSIC_EARPLUGS = new ExtraBondageMaterial("CLASSIC_EARPLUGS", "knapm", "earplugs/classic_earplugs", SoundEvents.field_187748_db);
      CLASSIC_COLLAR = new ExtraBondageMaterial("CLASSIC_COLLAR", "knapm", "collars/classic_collar", KidnapModSoundEvents.COLLAR_PUT);
      SHOCK_COLLAR = new ExtraBondageMaterial("SHOCK_COLLAR", "knapm", "collars/shock_collar", KidnapModSoundEvents.COLLAR_PUT);
      CHAIN = new ExtraBondageMaterial("CHAIN", "knapm", "chain/chain", KidnapModSoundEvents.CHAIN);
      ARM_BINDER = new ExtraBondageMaterial("ARM_BINDER", "knapm", "armbinder/armbinder", SoundEvents.field_187748_db);
      BITE_GAG = new ExtraBondageMaterial("BITE_GAG", "knapm", "armbinder/armbinder", SoundEvents.field_187552_ah);
      SLIME = new ExtraBondageMaterial("SLIME", "knapm", "slime/slime", KidnapModSoundEvents.SLIME);
      RIBBON = new ExtraBondageMaterial("RIBBON", "knapm", "ribbon/ribbon", SoundEvents.field_187748_db);
      SHIBARI = new ExtraBondageMaterial("SHIBARI", "knapm", "shibari/shibari", SoundEvents.field_187748_db);
      STRAPS = new ExtraBondageMaterial("STRAPS", "knapm", "straps/straps", SoundEvents.field_187748_db);
      MEDICAL_STRAPS = new ExtraBondageMaterial("MEDICAL_STRAPS", "knapm", "straps/medical_straps", SoundEvents.field_187748_db);
      TUBE = new ExtraBondageMaterial("TUBE", "knapm", "tube/tube", SoundEvents.field_187552_ah);
      VINE = new ExtraBondageMaterial("VINE", "knapm", "vine/vine", KidnapModSoundEvents.VINE);
      WEB = new ExtraBondageMaterial("WEB", "knapm", "web/web", KidnapModSoundEvents.STICKY);
      CLOTHES = new ExtraBondageMaterial("CLOTHES", "knapm", "clothes/clothes", SoundEvents.field_187748_db);
      CLEAVE_GAG = new ExtraBondageMaterial("CLEAVE_GAG", "knapm", "cleave/cleave_gag", SoundEvents.field_187552_ah);
      BAGUETTE_GAG = new ExtraBondageMaterial("BAGUETTE_GAG", "knapm", "baguette/baguette_gag", SoundEvents.field_187552_ah);
      SPONGE_GAG = new ExtraBondageMaterial("SPONGE_GAG", "knapm", "sponge/sponge_gag", SoundEvents.field_187552_ah);
      BEAM = new ExtraBondageMaterial("BEAM", "knapm", "beam/beam", KidnapModSoundEvents.COLLAR_PUT);
   }
}
