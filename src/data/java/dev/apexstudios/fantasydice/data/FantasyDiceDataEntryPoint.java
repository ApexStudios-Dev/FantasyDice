package dev.apexstudios.fantasydice.data;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import dev.apexstudios.apexcore.core.data.provider.ItemStackRecipeBuilder;
import dev.apexstudios.apexcore.lib.data.ProviderTypes;
import dev.apexstudios.apexcore.lib.data.ResourceGenerator;
import dev.apexstudios.apexcore.lib.data.provider.RecipeProvider;
import dev.apexstudios.apexcore.lib.util.StringHelper;
import dev.apexstudios.fantasydice.FantasyDice;
import dev.apexstudios.fantasydice.client.DiceMaterialSelectModelProperty;
import dev.apexstudios.fantasydice.client.DiceSidesSelectModelProperty;
import dev.apexstudios.fantasydice.util.Dice;
import dev.apexstudios.fantasydice.util.DiceRegistries;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.Tags;

@Mod(value = FantasyDice.ID, dist = Dist.CLIENT)
public final class FantasyDiceDataEntryPoint {
    private final Table<Integer, String, ItemModel.Unbaked> diceModels = HashBasedTable.create();

    public FantasyDiceDataEntryPoint(IEventBus modBus) {
        ResourceGenerator.of(modBus, generator -> {
            generator.pack()
                    .providing(ProviderTypes.LANGUAGE, (context, provider) -> {
                        provider.addCreativeModeTab(DiceRegistries.CREATIVE_MODE_TAB, "Fantasy's Dice");
                        provider.addEntityType(DiceRegistries.DICE_ENTITY, "Dice");
                        provider.addGameRule(DiceRegistries.RULE_DICE_LIFETIME, "Dice entity lifetime", "How long thrown dice should persist in world (in seconds)");
                        provider.addItem(DiceRegistries.DICE_ITEM, "Dice");
                        provider.add(Dice.DESCRIPTION_ID, "%s d%s");

                        for(var material : Dice.DEFAULT_MATERIALS) {
                            provider.add(Dice.getMaterialKey(material), StringHelper.toEnglishName(material));
                        }
                    })
                    .providing(ProviderTypes.MODELS, (context, provider) -> {
                        var models = provider.itemModels();

                        models.itemModelOutput.accept(
                                DiceRegistries.DICE_ITEM.value(),
                                diceModels(models.modelOutput)
                        );
                    })
                    .providing(ProviderTypes.RECIPES, (context, provider) -> {
                        diceRecipe("wooden", ItemTags.PLANKS, provider);
                        diceRecipe("stone", Tags.Items.STONES, provider);
                        diceRecipe("bone", Tags.Items.BONES, provider);
                        diceRecipe("iron", Tags.Items.INGOTS_IRON, provider);
                        diceRecipe("golden", Tags.Items.INGOTS_GOLD, provider);
                        diceRecipe("diamond", Tags.Items.GEMS_DIAMOND, provider);
                        diceRecipe("emerald", Tags.Items.GEMS_EMERALD, provider);
                        diceRecipe("netherite", Tags.Items.INGOTS_NETHERITE, provider);
                        diceRecipe("copper", Tags.Items.INGOTS_COPPER, provider);
                        diceRecipe("ender", Tags.Items.ENDER_PEARLS, provider);
                        diceRecipe("frozen", Items.ICE, provider);
                        diceRecipe("slime", Tags.Items.SLIME_BALLS, provider);
                        diceRecipe("redstone", Tags.Items.DUSTS_REDSTONE, provider);
                        diceRecipe("paper", Items.PAPER, provider);
                        diceRecipe("amethyst", Tags.Items.GEMS_AMETHYST, provider);
                        diceRecipe("chocolate", Tags.Items.CROPS_COCOA_BEAN, provider);
                    })
                    .providing(ProviderTypes.ENTITY_TYPE_TAGS, (context, provider) -> provider
                            .tag(Tags.EntityTypes.CAPTURING_NOT_SUPPORTED).withElement(DiceRegistries.DICE_ENTITY)
                    );
        });
    }

    private ItemModel.Unbaked diceModel(int sides, String material, BiConsumer<Identifier, ModelInstance> modelOutput) {
        var model = diceModels.get(sides, material);

        if(model == null) {
            model = ItemModelUtils.plainModel(ModelTemplates.FLAT_ITEM.create(
                    FantasyDice.identifier("item/" + material + '/' + sides + "_sided"),
                    TextureMapping.layer0(FantasyDice.identifier("item/" + material + '/' + sides + "_sided")),
                    modelOutput
            ));
            diceModels.put(sides, material, model);
        }

        return model;
    }

    private ItemModel.Unbaked diceModels(String material, BiConsumer<Identifier, ModelInstance> modelOutput) {
        return ItemModelUtils.select(
                DiceSidesSelectModelProperty.INSTANCE,
                IntStream.of(Dice.DEFAULT_SIDES).mapToObj(sides -> ItemModelUtils.when(sides, diceModel(sides, material, modelOutput))).toList()
        );
    }

    private ItemModel.Unbaked diceModels(BiConsumer<Identifier, ModelInstance> modelOutput) {
        return ItemModelUtils.select(
                DiceMaterialSelectModelProperty.INSTANCE,
                Stream.of(Dice.DEFAULT_MATERIALS).map(material -> ItemModelUtils.when(material, diceModels(material, modelOutput))).toList()
        );
    }

    private void diceRecipe(String material, String hasIngredientName, Ingredient ingredient, Criterion<InventoryChangeTrigger.TriggerInstance> hasIngredient, RecipeProvider provider) {
        var output = provider.output();

        for(var sides : Dice.DEFAULT_SIDES) {
            ItemStackRecipeBuilder.stonecutting(ingredient, RecipeCategory.MISC, Dice.create(sides, material))
                    .unlockedBy(hasIngredientName, hasIngredient)
                    .save(output, material + '/' + sides + "_sided_dice_stonecutting");
        }
    }

    private void diceRecipe(String material, TagKey<Item> ingredient, RecipeProvider provider) {
        diceRecipe(material, RecipeProvider.getHasName(ingredient), provider.tag(ingredient), provider.has(ingredient), provider);
    }

    private void diceRecipe(String material, ItemLike ingredient, RecipeProvider provider) {
        diceRecipe(material, RecipeProvider.getHasName(ingredient), Ingredient.of(ingredient), provider.has(ingredient), provider);
    }
}
