package dev.apexstudios.fantasydice.data;

import dev.apexstudios.fantasydice.api.FantasyDice;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.Tags;

final class RecipeProvider extends dev.apexstudios.apexcore.api.data.RecipeProvider {
    RecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
        super(recipeOutput, advancementOutput);
    }

    @Override
    protected void buildRecipes() {
        dice(ItemTags.PLANKS, FantasyDice.WOODEN_DICE.value());
        dice(ItemTags.STONE_CRAFTING_MATERIALS, FantasyDice.STONE_DICE.value());
        dice(Tags.Items.BONES, FantasyDice.BONE_DICE.value());
        dice(ItemTags.IRON_TOOL_MATERIALS, FantasyDice.IRON_DICE.value());
        dice(ItemTags.GOLD_TOOL_MATERIALS, FantasyDice.GOLDEN_DICE.value());
        dice(ItemTags.DIAMOND_TOOL_MATERIALS, FantasyDice.DIAMOND_DICE.value());
        dice(Tags.Items.GEMS_EMERALD, FantasyDice.EMERALD_DICE.value());
        dice(ItemTags.NETHERITE_TOOL_MATERIALS, FantasyDice.NETHERITE_DICE.value());
        dice(ItemTags.COPPER_TOOL_MATERIALS, FantasyDice.COPPER_DICE.value());
        dice(Tags.Items.ENDER_PEARLS, FantasyDice.ENDER_DICE.value());
        dice(Items.ICE, FantasyDice.FROZEN_DICE.value());
        dice(Tags.Items.SLIME_BALLS, FantasyDice.SLIME_DICE.value());
        dice(Tags.Items.DUSTS_REDSTONE, FantasyDice.REDSTONE_DICE.value());
        dice(Items.PAPER, FantasyDice.PAPER_DICE.value());
        dice(Tags.Items.GEMS_AMETHYST, FantasyDice.AMETHYST_DICE.value());
        dice(Tags.Items.CROPS_COCOA_BEAN, FantasyDice.CHOCOLATE_DICE.value());
    }

    private SingleItemRecipeBuilder diceBuilder(Ingredient ingredient, Item dice) {
        return SingleItemRecipeBuilder.stonecutting(ingredient, RecipeCategory.MISC, dice, 1);
    }

    private void dice(Item material, Item dice) {
        diceBuilder(Ingredient.of(material), dice)
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }

    private void dice(TagKey<Item> material, Item dice) {
        diceBuilder(tag(material), dice)
                .unlockedBy(getHasName(material), has(material))
                .save(output);
    }
}
