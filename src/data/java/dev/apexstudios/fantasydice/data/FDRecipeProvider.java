package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.common.FantasyDice;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;

final class FDRecipeProvider extends RecipeProvider {
    FDRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    protected void buildRecipes() {
        recipe("wooden", ItemTags.PLANKS);
        recipe("stone", Tags.Items.STONES);
        recipe("bone", Tags.Items.BONES);
        recipe("iron", Tags.Items.INGOTS_IRON);
        recipe("golden", Tags.Items.INGOTS_GOLD);
        recipe("diamond", Tags.Items.GEMS_DIAMOND);
        recipe("emerald", Tags.Items.GEMS_EMERALD);
        recipe("netherite", Tags.Items.INGOTS_NETHERITE);
        recipe("copper", Tags.Items.INGOTS_COPPER);
        recipe("ender", Tags.Items.ENDER_PEARLS);
        recipe("frozen", Items.ICE);
        recipe("slime", Tags.Items.SLIME_BALLS);
        recipe("redstone", Tags.Items.DUSTS_REDSTONE);
        recipe("paper", Items.PAPER);
        recipe("amethyst", Tags.Items.GEMS_AMETHYST);
        recipe("chocolate", Tags.Items.CROPS_COCOA_BEAN);
    }

    private void recipe(String material, TagKey<Item> ingredient) {
        DieDataUtils.recipe(FantasyDice.DICE_ITEM, material, DieDataUtils.getHasName(ingredient), tag(ingredient), has(ingredient), output);
    }

    private void recipe(String material, ItemLike ingredient) {
        DieDataUtils.recipe(FantasyDice.DICE_ITEM, material, getHasName(ingredient), Ingredient.of(ingredient), has(ingredient), output);
    }
}
