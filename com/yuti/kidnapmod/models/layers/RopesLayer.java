package com.yuti.kidnapmod.models.layers;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RopesLayer extends LayerBipedArmor {
   public RopesLayer(RenderLivingBase<?> rendererIn) {
      super(rendererIn);
   }

   protected void func_188359_a(ModelBiped p_188359_1_, EntityEquipmentSlot slotIn) {
      this.func_177194_a(p_188359_1_);
      switch(slotIn) {
      case HEAD:
         p_188359_1_.field_78116_c.field_78806_j = true;
         p_188359_1_.field_178720_f.field_78806_j = true;
         break;
      case CHEST:
         p_188359_1_.field_78115_e.field_78806_j = true;
         p_188359_1_.field_178723_h.field_78806_j = true;
         p_188359_1_.field_178724_i.field_78806_j = true;
         p_188359_1_.field_78115_e.field_78806_j = true;
         p_188359_1_.field_178721_j.field_78806_j = true;
         p_188359_1_.field_178722_k.field_78806_j = true;
         p_188359_1_.field_178721_j.field_78806_j = true;
         p_188359_1_.field_178722_k.field_78806_j = true;
      }

   }
}
