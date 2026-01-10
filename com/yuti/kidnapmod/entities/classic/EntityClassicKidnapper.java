package com.yuti.kidnapmod.entities.classic;

import com.yuti.kidnapmod.entities.EntityKidnapper;
import com.yuti.kidnapmod.entities.ai.EntityAIRestrainingPlayerClassic;
import com.yuti.kidnapmod.util.Utils;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityClassicKidnapper extends EntityKidnapper {
   private static final DataParameter<Integer> SKIN_NUMBER;

   public EntityClassicKidnapper(World world) {
      super(world);
   }

   protected void func_70088_a() {
      super.func_70088_a();
      EntitiesClassicKidnappers randomKidnapper = EntitiesClassicKidnappers.getRandomKidnapper();
      this.field_70180_af.func_187214_a(SKIN_NUMBER, randomKidnapper.getId());
      this.field_70180_af.func_187214_a(SMALL_ARMS, randomKidnapper.hasSmallArms());
      this.field_70180_af.func_187214_a(DAMSEL_NAME, Utils.getRandomName());
      this.func_96094_a((String)this.field_70180_af.func_187225_a(DAMSEL_NAME));
   }

   protected void func_184651_r() {
      super.func_184651_r();
      this.field_70714_bg.func_75776_a(2, new EntityAIRestrainingPlayerClassic(this));
   }

   public void func_110147_ax() {
      super.func_110147_ax();
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(20.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111266_c).func_111128_a(7.0D);
      this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.25D);
   }

   public void func_70014_b(NBTTagCompound compound) {
      super.func_70014_b(compound);
      compound.func_74768_a("skin_number", (Integer)this.field_70180_af.func_187225_a(SKIN_NUMBER));
   }

   public void func_70037_a(NBTTagCompound compound) {
      super.func_70037_a(compound);
      if (compound.func_74764_b("skin_number")) {
         this.field_70180_af.func_187227_b(SKIN_NUMBER, compound.func_74762_e("skin_number"));
      }

   }

   public int getSkinNumber() {
      return (Integer)this.field_70180_af.func_187225_a(SKIN_NUMBER);
   }

   public ResourceLocation getSkin() {
      return new ResourceLocation("knapm", "textures/entity/kidnapper/knp_mob_" + this.getSkinNumber() + ".png");
   }

   static {
      SKIN_NUMBER = EntityDataManager.func_187226_a(EntityClassicKidnapper.class, DataSerializers.field_187192_b);
   }
}
