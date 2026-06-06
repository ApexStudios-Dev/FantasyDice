package dev.apexstudios.fantasydice.data;

import dev.apexstudios.apexcore.api.data.provider.RecipeProvider;
import dev.apexstudios.apexcore.common.data.provider.ItemStackRecipeBuilder;
import dev.apexstudios.fantasydice.client.DieSelectModelProperty;
import dev.apexstudios.fantasydice.common.Die;
import dev.apexstudios.fantasydice.common.FantasyDice;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.SelectItemModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public interface DieDataUtils {
    static ItemStackTemplate template(Holder<Item> item, String material, int sides) {
        return new ItemStackTemplate(
                item,
                DataComponentPatch.builder()
                        .set(FantasyDice.DIE_COMPONENT.value(), Die.DEFAULT
                                .withMaterial(material)
                                .withSides(sides)
                        )
                .build()
        );
    }

    static Identifier createSingleModel(Die die, ItemModelGenerators itemModels) {
        return ModelTemplates.FLAT_ITEM.create(
                FantasyDice.REGISTREE.registryName("item/" + die.material() + '/' + die.sides() + "_sided"),
                TextureMapping.layer0(new Material(FantasyDice.REGISTREE.registryName("item/" + die.material() + '/' + die.sides() + "_sided"))),
                itemModels.modelOutput
        );
    }

    static ItemModel.Unbaked singleModel(Die die, ItemModelGenerators itemModels) {
        return ItemModelUtils.plainModel(createSingleModel(die, itemModels));
    }

    static SelectItemModel.SwitchCase<Die> whenDie(Die die, ItemModelGenerators itemModels) {
        return ItemModelUtils.when(die, singleModel(die, itemModels));
    }

    static List<SelectItemModel.SwitchCase<Die>> whenDice(Stream<Die> dice, ItemModelGenerators itemModels) {
        return dice.map(die -> whenDie(die, itemModels)).toList();
    }

    static ItemModel.Unbaked fullModel(ItemModelGenerators itemModels) {
        return ItemModelUtils.select(DieSelectModelProperty.INSTANCE, whenDice(Die.builtIn(), itemModels));
    }

    static void createDiceModel(Holder<Item> item, ItemModelGenerators itemModels) {
        itemModels.itemModelOutput.accept(item.value(), fullModel(itemModels));
    }

    static void recipe(Holder<Item> item, String material, int sides, String hasIngredientName, Ingredient ingredient, Criterion<InventoryChangeTrigger.TriggerInstance> hasIngredient, RecipeOutput output) {
        ItemStackRecipeBuilder.stonecutting(ingredient, RecipeCategory.MISC, template(item, material, sides))
                .unlockedBy(hasIngredientName, hasIngredient)
                .save(output, material + '/' + sides + "_sided_dice_stonecutting");
    }

    static void recipe(Holder<Item> item, String material, String hasIngredientName, Ingredient ingredient, Criterion<InventoryChangeTrigger.TriggerInstance> hasIngredient, RecipeOutput output) {
        for(var sides : Die.DEFAULT_SIDES) {
            recipe(item, material, sides, hasIngredientName, ingredient, hasIngredient, output);
        }
    }

    static void recipe(Holder<Item> item, String material, TagKey<Item> ingredient, RecipeProvider provider) {
        recipe(item, material, RecipeProvider.getHasName(ingredient), provider.tag(ingredient), provider.has(ingredient), provider.output());
    }

    static void recipe(Holder<Item> item, String material, ItemLike ingredient, RecipeProvider provider) {
        recipe(item, material, RecipeProvider.getHasName(ingredient), Ingredient.of(ingredient), provider.has(ingredient), provider.output());
    }

    static String getMaterialKey(String material) {
        return FantasyDice.ID + ".material." + material;
    }
}
