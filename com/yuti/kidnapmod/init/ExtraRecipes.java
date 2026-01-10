package com.yuti.kidnapmod.init;

import com.yuti.kidnapmod.recipes.RecipeExtendedKidnapArrow;
import com.yuti.kidnapmod.recipes.RecipeExtendedKidnapBomb;
import com.yuti.kidnapmod.recipes.RecipeExtendedTrap;
import com.yuti.kidnapmod.recipes.RecipeExtendedTrappedBed;
import com.yuti.kidnapmod.recipes.RecipeResistanceItemsMerger;
import com.yuti.kidnapmod.recipes.RecipeShockCollar;
import com.yuti.kidnapmod.recipes.RecipeShockerMerger;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.registries.IForgeRegistryEntry.Impl;

public class ExtraRecipes {
   public static List<IRecipe> RECIPES = new ArrayList();
   public static IRecipe recipeExtendedTrap = registerRecipe("extended_trap", new RecipeExtendedTrap());
   public static IRecipe recipeExtendedKidnapArrow = registerRecipe("extended_kidnap_arrow", new RecipeExtendedKidnapArrow());
   public static IRecipe recipeExtendedTrappedBed = registerRecipe("extended_trapped_bed", new RecipeExtendedTrappedBed());
   public static IRecipe recipeExtendedKidnapBomb = registerRecipe("extended_kidnap_bomb", new RecipeExtendedKidnapBomb());
   public static IRecipe recipeMergedShockerControllers = registerRecipe("merged_shocker_controllers", new RecipeShockerMerger());
   public static IRecipe recipeMergedCollars = registerRecipe("merged_collars", new RecipeResistanceItemsMerger("collar"));
   public static IRecipe recipeShockCollar = registerRecipe("recipe_shock_collar", new RecipeShockCollar());

   public static IRecipe registerRecipe(String name, Impl<IRecipe> recipe) {
      recipe.setRegistryName(name);
      if (recipe instanceof IRecipe) {
         IRecipe newRecipe = (IRecipe)recipe;
         RECIPES.add(newRecipe);
         return newRecipe;
      } else {
         return null;
      }
   }
}
