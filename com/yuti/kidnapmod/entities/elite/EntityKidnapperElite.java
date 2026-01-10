package com.yuti.kidnapmod.entities.elite;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.entities.ai.EntityAIRestrainingPlayerElite;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityKidnapperElite extends EntityKidnapper {
   public EntityKidnapperElite(World world) {
      super(world);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      EliteKidnappers kidnapper = EliteKidnappers.getRandomKidnapper();
      this.field_70180_af.func_187214_a(DAMSEL_NAME, kidnapper.getName());
      this.field_70180_af.func_187214_a(SMALL_ARMS, kidnapper.hasSmallArms());
      this.func_96094_a((String)this.field_70180_af.func_187225_a(DAMSEL_NAME));
   }

   protected void func_184651_r() {
      super.func_184651_r();
      this.field_70714_bg.func_75776_a(2, new EntityAIRestrainingPlayerElite(this));
   }

   public void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(40.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111266_c).func_111128_a(15.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.3D);
   }

   public TextFormatting getNameColor() {
      return !this.isSlaveKidnappingModeEnabled() && !this.isBondageServiceEnabled() ? TextFormatting.GOLD : super.getNameColor();
   }

   public ResourceLocation getSkin() {
      return new ResourceLocation("knapm", "textures/entity/kidnapper/elite/" + this.getDamselName().toLowerCase() + ".png");
   }

   public boolean func_70601_bi() {
      GameRules rules = this.func_130014_f_().func_82736_K();
      boolean spawn = true;
      if (rules.func_82765_e("kidnappers-spawn-elite")) {
         spawn = rules.func_82766_b("kidnappers-spawn-elite");
      }

      return spawn && super.func_70601_bi();
   }

   protected boolean func_70692_ba() {
      GameRules rules = this.func_130014_f_().func_82736_K();
      if (rules.func_82765_e("kidnappers-spawn-elite")) {
         return !rules.func_82766_b("kidnappers-spawn-elite");
      } else {
         return false;
      }
   }
}
